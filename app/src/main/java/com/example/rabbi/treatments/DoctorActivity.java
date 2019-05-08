package com.example.rabbi.treatments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.rabbi.digitrecognition.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Doctor> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        recyclerView = (RecyclerView) findViewById(R.id.doctors_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        createDoctors();
        makeLayout();
    }

    private void createDoctors() {
        list = new ArrayList<>();

        Intent doctorIntent = getIntent();
        String response = (String) doctorIntent.getSerializableExtra("response");

        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i=0; i<jsonArray.length(); i++)
            {
                JSONObject jsonDoctor = (JSONObject) jsonArray.get(i);
                String name = (String) jsonDoctor.get("name");
                String hospital = (String) jsonDoctor.get("hospital");
                String lat = (String) jsonDoctor.get("lat");
                String lon = (String) jsonDoctor.get("lon");

                Doctor doctor = new Doctor(name, hospital, lat, lon);
                list.add(doctor);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Toast.makeText(getApplicationContext(), response,Toast.LENGTH_SHORT).show();
    }

    private void makeLayout() {
        mAdapter = new MyAdapter(list, getApplicationContext());
        recyclerView.setAdapter(mAdapter);
    }
}