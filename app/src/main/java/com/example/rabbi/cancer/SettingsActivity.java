package com.example.rabbi.cancer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.rabbi.digitrecognition.R;
import com.example.rabbi.values.Constants;
import com.example.rabbi.values.MyCacheManager;

public class SettingsActivity extends AppCompatActivity {

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
                    Intent intent_gallery = new Intent(getApplicationContext(),GalleryActivity.class);
                    startActivity(intent_gallery);
                    finish();
                    return true;
                case R.id.navigation_settings:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        View view = navigation.findViewById(R.id.navigation_settings);
        view.performClick();

        getValues();
    }

    private void getValues() {
        EditText ipField = (EditText) findViewById(R.id.server_ip);
        EditText portField = (EditText) findViewById(R.id.server_port);
        EditText routeField = (EditText) findViewById(R.id.server_route);

        String ip = MyCacheManager.getDefaults(Constants.ipTitle,getApplicationContext());
        String port = MyCacheManager.getDefaults(Constants.portTitle,getApplicationContext());
        String route = MyCacheManager.getDefaults(Constants.routeTitle,getApplicationContext());

        ipField.setText(ip);
        portField.setText(port);
        routeField.setText(route);
    }

    public void setInCache(View view) {
        EditText ipField = (EditText) findViewById(R.id.server_ip);
        EditText portField = (EditText) findViewById(R.id.server_port);
        EditText routeField = (EditText) findViewById(R.id.server_route);

        String ip = ipField.getText().toString();
        String port = portField.getText().toString();
        String route = routeField.getText().toString();

        MyCacheManager.setDefaults(Constants.ipTitle,ip,getApplicationContext());
        MyCacheManager.setDefaults(Constants.portTitle,port,getApplicationContext());
        MyCacheManager.setDefaults(Constants.routeTitle,route,getApplicationContext());
    }
}