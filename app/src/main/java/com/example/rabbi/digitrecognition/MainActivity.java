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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rabbi.constants.Fields;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView box;
    String ipstring = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        box = (TextView) findViewById(R.id.box);
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
                    Bitmap bmp = ImagePicker.getImageFromResult(this, resultCode, data);
                    //your compressed rotated bitmap here

                    bmp = ImagePicker.getSquared(bmp);
                    //bmp = ImagePicker.getResized(bmp,28,28);
                    String encoded = ImagePicker.toBase64(bmp);

                    postImage(encoded, getApplicationContext());
                }
            }
        }
    }

    public void postImage(final String encoded, final Context context)
    {
        //String protocol = "http://";
        //String ip = edit.getText().toString();
        //String port = "9001";
        //String route = "predict_digit";
        String url = "http://"+Fields.main_server_ip+":"+Fields.main_server_port+"/receiver";
        //String url = protocol+ipstring+"/"+route;

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
                values.put("sample_image",encoded);

                return values;

            }
        };
        Volley.newRequestQueue(context).add(request);
    }
}