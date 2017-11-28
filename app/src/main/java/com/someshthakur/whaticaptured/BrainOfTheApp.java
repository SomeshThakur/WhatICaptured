package com.someshthakur.whaticaptured;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class BrainOfTheApp extends AppCompatActivity {
    private int CAMERA_PIC_REQUEST = 0;
    private TextView textView;
    private ImageView imageView;
    Uri mCapturedImageURI;
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain_of_the_app);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        Boolean shouldCapture = getIntent().getBooleanExtra("cap", true);
        if (shouldCapture) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "Image File name");
            mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        } else {
            try {
                String uriPath = getIntent().getStringExtra("uriPath");
                Toast.makeText(this, uriPath, Toast.LENGTH_LONG).show();
                mCapturedImageURI = Uri.parse(uriPath);
                File finalFile = new File(getRealPathFromURI(mCapturedImageURI));
                String path = finalFile.getAbsolutePath();
                imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(path);
                    showAllDetials(exif, path);
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage() + e + "", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            // Bitmap photo = (Bitmap) data.getExtras().get("data");
            // imageView.setImageBitmap(photo);
            // Uri tempUri = getImageUri(getApplicationContext(), photo);
            File finalFile = new File(getRealPathFromURI(mCapturedImageURI));
            String path = finalFile.getAbsolutePath();
            imageView.setImageBitmap(BitmapFactory.decodeFile(path));
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(path);
                showAllDetials(exif, path);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage() + e + "", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showAllDetials(ExifInterface exifInterface, String filePath) {
        String exif = "Exif: " + filePath;
        try {
            exif += "\nIMAGE_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            exif += "\nIMAGE_WIDTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            exif += "\n DATETIME: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            exif += "\n TAG_MAKE: " + exifInterface.getAttribute(ExifInterface.TAG_MAKE);
            exif += "\n TAG_MODEL: " + exifInterface.getAttribute(ExifInterface.TAG_MODEL);
            exif += "\n TAG_ORIENTATION: " + exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
            exif += "\n TAG_WHITE_BALANCE: " + exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
            exif += "\n TAG_FOCAL_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            exif += "\n TAG_FLASH: " + exifInterface.getAttribute(ExifInterface.TAG_FLASH);
            exif += "\nGPS related:";
            exif += "\n TAG_GPS_DATESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
            exif += "\n TAG_GPS_TIMESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
            exif += "\n TAG_GPS_LATITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            exif += "\n TAG_GPS_LATITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            exif += "\n TAG_GPS_LONGITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            exif += "\n TAG_GPS_LONGITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            exif += "\n TAG_GPS_PROCESSING_METHOD: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);
            textView.setText(exif);
            saveAllData(exif, filePath);
        } catch (Exception e) {
            Toast.makeText(this, "Error at showAlldetials e:  " + e, Toast.LENGTH_LONG).show();
        }
    }

    private void saveAllData(String exif, String path) {
        String name = path.substring(path.lastIndexOf('/'), path.length());

        // FileOutputStream fo = new FileOutputStream(new File(name));
    }
}