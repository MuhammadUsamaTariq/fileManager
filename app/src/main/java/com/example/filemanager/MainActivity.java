package com.example.filemanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ImageView image;
    Button btnForAnyFile;
    Button btnForImageFile;
    Intent path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        btnForAnyFile = findViewById(R.id.button);
        btnForImageFile = findViewById(R.id.button2);
        image = findViewById(R.id.imageView);

        btnForAnyFile.setOnClickListener(v -> {

            path = new Intent(Intent.ACTION_GET_CONTENT);
            path.setType("*/*");
            startActivityForResult(path, 1);
        });

        btnForImageFile.setOnClickListener(v -> {

            path = new Intent(Intent.ACTION_GET_CONTENT);
            path.setType("image/*");
            startActivityForResult(path, 2);
            Toast.makeText(this, "Testing git process", Toast.LENGTH_SHORT).show();

        });


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

    }


}