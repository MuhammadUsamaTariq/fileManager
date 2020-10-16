package com.example.filemanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ImageView image;
    Button btnForAnyFile;
    Button btnForCameraImagePicker;
    Button btnForImageFile;
    Intent path;
    public static final int permissionRequestCodeForFilePicker = 001;
    public static final int permissionRequestCodeForImageFilePicker = 002;
    public static final int cameraRequestCode = 003;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        btnForAnyFile = findViewById(R.id.button);
        btnForImageFile = findViewById(R.id.button2);
        image = findViewById(R.id.imageView);

        btnForAnyFile.setOnClickListener(v -> {
            requestPermission(0);
        });

        btnForImageFile.setOnClickListener(v -> {

            requestPermission(1);

        });

        btnForCameraImagePicker.setOnClickListener(v -> {

            requestPermission(2);

        });


    }

    private void openImageFilePicker() {
        path = new Intent(Intent.ACTION_GET_CONTENT);
        path.setType("image/*");
        startActivityForResult(path, 2);
    }

    private void openPickImageFromCamera(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, cameraRequestCode);
    }

    private void requestPermission(int fileStatus) {
        switch (fileStatus) {
            case 0:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissionRequestCodeForFilePicker);
                } else {
                    openFilePicker();
                }
                break;
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissionRequestCodeForImageFilePicker);
                } else {
                    openImageFilePicker();
                }
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, cameraRequestCode);
                } else {
                    openPickImageFromCamera();
                }
                break;
            default:
                Toast.makeText(this, "Invalid Status", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFilePicker() {

        path = new Intent(Intent.ACTION_GET_CONTENT);
        path.setType("*/*");
        startActivityForResult(path, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permissionRequestCodeForFilePicker && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker();
        }else{
            Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == permissionRequestCodeForImageFilePicker && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImageFilePicker();
        }else{
            Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == cameraRequestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openPickImageFromCamera();
        }else{
            Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String path = data.getData().getPath();
                textView.setText(path);
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Uri path = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                    image.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(requestCode == cameraRequestCode){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(photo);
        }

    }


}