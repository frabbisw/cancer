package com.example.rabbi.cancer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.example.rabbi.values.Fields;
import com.example.rabbi.values.MyCacheManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GalleryActivity extends AppCompatActivity {
    TextView box;
    String ipstring = "";
    Bitmap bmpImage;
    String encoded;

    public final static int REQUEST_GET_SINGLE_FILE = 111;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_camera:
                    Intent intent_camera = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent_camera);
                    finish();
                    return true;
                case R.id.navigation_gallery:
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
        setContentView(R.layout.activity_gallery);

        box = (TextView) findViewById(R.id.gallery_box);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        View view = navigation.findViewById(R.id.navigation_gallery);
        view.performClick();
    }

    public void startGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_GET_SINGLE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_GET_SINGLE_FILE) {
                    Uri selectedImageUri = data.getData();
                    // Get the path from the Uri
                    final String path = getPathFromURI(selectedImageUri);
                    if (path != null) {
                        File f = new File(path);
                        selectedImageUri = Uri.fromFile(f);
                    }
                    // Set the image in ImageView
                    ImageView img = (ImageView) findViewById(R.id.galleryImgView);
                    img.setImageURI(selectedImageUri);

                    bmpImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    bmpImage = ImagePicker.getResized(bmpImage,224,224);
                    encoded = ImagePicker.toBase64(bmpImage);
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public void postGalleryImage(View view) {
        postImage(encoded, getApplicationContext());
    }

    public void postImage(final String encoded, final Context context)
    {
        String url = MyCacheManager.getURI(getApplicationContext());
        Log.e("FURI",Fields.getServerUrl());
        Log.e("CURI",MyCacheManager.getURI(getApplicationContext()));


        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if(response.length()<5)
                            box.setText("No Cancer");
                        else {
                            //box.setText(response);
                            Intent doctorIntent = new Intent(getApplicationContext(), DoctorActivity.class);
                            doctorIntent.putExtra("response",response);
                            startActivity(doctorIntent);
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
