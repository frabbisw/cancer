package com.example.rabbi.digitrecognition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        box = (TextView) findViewById(R.id.box);
    }
    public static final int PICK_USER_PROFILE_IMAGE = 1000;

    public void startCameraActivity(View view){
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
                    Bitmap bmp = ImagePicker.getImageFromResult(this, resultCode, data);
                    //your compressed rotated bitmap here

                    bmp = ImagePicker.getSquared(bmp);
                    bmp = ImagePicker.getResized(bmp,28,28);
                    String encoded = ImagePicker.toBase64(bmp);

                    //Log.e("BMP",bmp.getHeight()+" "+bmp.getWidth());
                    //Log.e("BMP","encoded: "+encoded);

                    postImage(encoded, getApplicationContext());
                }
            }
        }
    }

    public void postImage(final String encoded, final Context context)
    {
        String url = "http://192.168.0.107:9001/predict_digit";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        box.setText(response);
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
                values.put("encoded",encoded);

                return values;

            }
        };
        Volley.newRequestQueue(context).add(request);
    }
}