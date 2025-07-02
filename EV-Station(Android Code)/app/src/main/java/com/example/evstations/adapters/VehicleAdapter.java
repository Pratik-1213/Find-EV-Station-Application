package com.example.evstations.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.evstations.R;

import java.util.ArrayList;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Vehicle> vehicleArrayList;
    Context context;

    public VehicleAdapter(Context ctx, ArrayList<Vehicle> vehicleArrayList){

        inflater = LayoutInflater.from(ctx);
        this.vehicleArrayList = vehicleArrayList;
    }

    @Override
    public VehicleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.vehicle, parent, false);
        VehicleAdapter.MyViewHolder holder = new VehicleAdapter.MyViewHolder(view);
        return holder;
    }


    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(VehicleAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.txtName.setText(vehicleArrayList.get(position).veh_number + "\n" + vehicleArrayList.get(position).veh_name);

    }

    @Override
    public int getItemCount() {
        return vehicleArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtDetails;
        LinearLayout layoutCard;


        public MyViewHolder(View view) {
            super(view);
            context = itemView.getContext();
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            layoutCard = (LinearLayout) view.findViewById(R.id.layoutCard);

        }
    }
}