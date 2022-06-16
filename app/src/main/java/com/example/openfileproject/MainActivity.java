package com.example.openfileproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button kamera, galeri;
    private String cameraFilePath;
    ImageView imageView1, imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kamera = findViewById(R.id.kamera);
        galeri = findViewById(R.id.galeri);
        imageView1 = findViewById(R.id.imgView1);
        imageView2 = findViewById(R.id.imgView2);

        kamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
//                            MainActivity.this, "com.example.openfileproject",
//                            createImageFile()));
//                    startActivityForResult(intent, 1000);
//                }catch (IOException e){
//                    e.printStackTrace();
//                    Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
//                }
                Toast.makeText(MainActivity.this, "under construction", Toast.LENGTH_SHORT).show();
            }
        });

        galeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 2000);
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File locPath = Environment.getExternalStorageDirectory();
        File locDir = new File(locPath.getPath() + "/OpenFileProject");
        File image = File.createTempFile(imageFileName, ".jpg", locDir);

        cameraFilePath = image.getAbsolutePath();
        galleryAddPic();
        return image;
    }

    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(cameraFilePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000){
            imageView1.setImageURI(Uri.parse(cameraFilePath));
        }else if (requestCode == 2000){
            if (data != null){
                Uri selectedImageUri = data.getData();
                cameraFilePath = data.getData().getPath();
                Bitmap dataBitmap = null;
                galleryAddPic();

                try {
                    dataBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    Glide.with(MainActivity.this)
                            .load(dataBitmap)
                            .error(R.drawable.ic_launcher_background)
                            .into(imageView2);
                }catch (IOException e){
                    e.printStackTrace();
                }
                imageView2.setVisibility(View.VISIBLE);
            }
        }
    }
}