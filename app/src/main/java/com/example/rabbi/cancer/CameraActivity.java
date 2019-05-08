package com.example.rabbi.cancer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rabbi.digitrecognition.R;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.square_camera);


    }
    public static final int PICK_USER_PROFILE_IMAGE = 1000;

    public void startCameraActivity(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(CameraActivity.this.getPackageManager()) != null) {
            startActivityForResult(cameraIntent, PICK_USER_PROFILE_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_USER_PROFILE_IMAGE) {
                if (resultCode == RESULT_OK) {
                    Bitmap bmp = ImagePicker.getImageFromResult(this, resultCode, data);
                    //your compressed rotated bitmap here

                }
            }
        }
    }
}
