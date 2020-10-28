package com.example.filemanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ImageView image;
    Button btnForAnyFile;
    Button btnForCameraImagePicker;
    Button btnForImageFile;
    Button btnToSaveAsPdf;
    Button urlGoogle;
    Intent path;
    Button btnForSavingImgToInternalStorage;
    Button mailSendBtn;
    EditText mEditTextTo;
    EditText mEditTextSubject;
    EditText mEditTextMessage;
    public static final int permissionRequestCodeForFilePicker = 001;
    public static final int permissionRequestCodeForImageFilePicker = 002;
    public static final int cameraRequestCode = 003;
    public static final int internalStorageRequestCode = 004;
    public static final int saveAsPdfRequestCode = 005;

    EditText edtFileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        btnForAnyFile = findViewById(R.id.button);
        btnForImageFile = findViewById(R.id.button2);
        btnForCameraImagePicker = findViewById(R.id.button3);
        image = findViewById(R.id.imageView);
        btnForSavingImgToInternalStorage = findViewById(R.id.button4);
        edtFileName = findViewById(R.id.edtFileName);
        btnToSaveAsPdf = findViewById(R.id.saveAsPdf);
        urlGoogle = findViewById(R.id.urlGoogle);
        mEditTextTo = findViewById(R.id.edit_text_to);
        mEditTextSubject = findViewById(R.id.edit_text_subject);
        mEditTextMessage = findViewById(R.id.edit_text_message);
        mailSendBtn = findViewById(R.id.button_send);


        btnForAnyFile.setOnClickListener(v -> {
            requestPermission(0);
        });

        btnForImageFile.setOnClickListener(v -> {

            requestPermission(1);

        });

        btnForCameraImagePicker.setOnClickListener(v -> {

            requestPermission(2);

        });

        btnForSavingImgToInternalStorage.setOnClickListener(v -> {

            requestPermission(3);

        });

        btnToSaveAsPdf.setOnClickListener(v -> {

            requestPermission(4);

        });

        urlGoogle.setOnClickListener(v -> {

            googleUrl();

        });

        mailSendBtn.setOnClickListener(v -> {

            sendMail();

        });


    }

    private void sendMail() {
        String recipientList = mEditTextTo.getText().toString();
        String[] recipients = recipientList.split(",");
        String subject = mEditTextSubject.getText().toString();
        String message = mEditTextMessage.getText().toString();
        Intent send = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode(recipientList) +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(message);
        Uri uri = Uri.parse(uriText);

        send.setData(uri);
        startActivity(Intent.createChooser(send, "Send mail..."));
    }

    private void googleUrl() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(browserIntent);
    }


    private void saveIamgeFile() {

        image.setDrawingCacheEnabled(true);
        Bitmap bitmap = image.getDrawingCache();

        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/fileManagerPics/newFile.jpg");
        file.mkdirs();

        try {
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void savetointernalstorage() {

        String filename;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/fileManagerPics");
        dir.mkdirs();

        if (edtFileName.length() > 0) {

            filename = String.format(edtFileName.getText().toString() + ".png", System.currentTimeMillis());

            File outFile = new File(dir, filename);

            try {
                outputStream = new FileOutputStream(outFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            try {
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            edtFileName.setError("File name must be entered");
        }


    }

    private void openImageFilePicker() {
        path = new Intent(Intent.ACTION_GET_CONTENT);
        path.setType("image/*");
        startActivityForResult(path, 2);
    }

    private void openPickImageFromCamera() {
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
            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, internalStorageRequestCode);
                } else {
                    savetointernalstorage();
                    //saveIamgeFile();
                }
                break;
            case 4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, saveAsPdfRequestCode);
                } else {
                    saveAsPdf();
                    //saveIamgeFile();
                }
                break;
            default:
                Toast.makeText(this, "Invalid Status", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAsPdf() {

        BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);



        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/fileManagerPics");
        dir.mkdirs();
        // write the document content
        if (edtFileName.length() > 0) {
            String targetPdf = String.format(edtFileName.getText().toString() + ".pdf", System.currentTimeMillis());
            File filePath = new File(dir ,targetPdf);
            try {
                document.writeTo(new FileOutputStream(filePath));

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
            }
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);


                intentShareFile.setType("application/pdf");
            intentShareFile.putExtra(Intent.EXTRA_STREAM,
                    Uri.parse(filePath.getAbsolutePath()));

        /*        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Sharing File...");
                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");*/

                startActivity(Intent.createChooser(intentShareFile, "Share File"));

        }else {
            edtFileName.setError("File name must be entered");
        }
        document.close();
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
        } else {
            Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == permissionRequestCodeForImageFilePicker && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImageFilePicker();
        } else {
            Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == cameraRequestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openPickImageFromCamera();
        } else {
            Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == internalStorageRequestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            savetointernalstorage();
            // saveIamgeFile();
        } else {
            Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == saveAsPdfRequestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveAsPdf();
            // saveIamgeFile();
        } else {
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

        if (requestCode == cameraRequestCode) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                image.setImageBitmap(photo);
            }
        }


    }


}