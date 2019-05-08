package com.example.rabbi.cancer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rabbi.digitrecognition.R;
import com.example.rabbi.treatments.DoctorActivity;
import com.example.rabbi.values.MyCacheManager;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView box;
    String ipstring = "";
    Bitmap bmpImage;
    String encoded;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_camera:
                    return true;
                case R.id.navigation_gallery:
                    Intent intent_gallery = new Intent(getApplicationContext(),GalleryActivity.class);
                    startActivity(intent_gallery);
                    finish();
                    return true;
                case R.id.navigation_settings:
                    Intent intent_settings= new Intent(getApplicationContext(),SettingsActivity.class);
                    startActivity(intent_settings);
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        box = (TextView) findViewById(R.id.camera_box);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        View view = navigation.findViewById(R.id.navigation_camera);
        view.performClick();

        //Intent intent = new Intent(getApplicationContext(), DoctorActivity.class);
        //startActivity(intent);
    }
    public static final int PICK_USER_PROFILE_IMAGE = 1000;

    public void startCameraActivity(View view){
        box.setText("-");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
            startActivityForResult(cameraIntent, PICK_USER_PROFILE_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_USER_PROFILE_IMAGE) {
                if (resultCode == RESULT_OK) {
                    bmpImage = ImagePicker.getImageFromResult(this, resultCode, data);
                    //your compressed rotated bitmap here

                    bmpImage = ImagePicker.getSquared(bmpImage);

                    ImageView img = (ImageView) findViewById(R.id.cameraImgView);
                    img.setImageBitmap(bmpImage);

                    bmpImage = ImagePicker.getResized(bmpImage,224,224);
                    encoded = ImagePicker.toBase64(bmpImage);

                    //postImage(encoded, getApplicationContext());
                }
            }
        }
    }

    public void postCameraImage(View view) {
        postImage(encoded, getApplicationContext());
    }

    public void postImage(final String encoded, final Context context)
    {
        String url = MyCacheManager.getURI(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if(response.length()<5)
                            box.setText("No Cancer");
                        else {
                            Intent doctorIntent = new Intent(getApplicationContext(), DoctorActivity.class);
                            doctorIntent.putExtra("response",response);
                            startActivity(doctorIntent);

                            //box.setText(response);
                        }
                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(context, "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            protected Map<String,String> getParams()
            {
                Map <String, String> values = new HashMap<String, String>();
                values.put("sample_image",encoded);

                return values;

            }
        };
        Volley.newRequestQueue(context).add(request);
    }

}