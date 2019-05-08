package com.example.rabbi.treatments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rabbi.digitrecognition.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Doctor>mDataset;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public MyViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public MyAdapter(List<Doctor>myDataset, Context context) {
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.single_doctor, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        TextView tv_doctor = (TextView) holder.view.findViewById(R.id.doctor_name);
        TextView tv_hospital = (TextView) holder.view.findViewById(R.id.hospital_name);

        tv_doctor.setText(mDataset.get(position).getName());
        tv_hospital.setText(mDataset.get(position).getHospital());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, mDataset.get(position).lat+"::"+mDataset.get(position).lon, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}