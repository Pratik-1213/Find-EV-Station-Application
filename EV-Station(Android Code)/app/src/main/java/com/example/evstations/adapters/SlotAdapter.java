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

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Slot> slotArrayList;
    Context context;

    public SlotAdapter(Context ctx, ArrayList<Slot> slotArrayList){

        inflater = LayoutInflater.from(ctx);
        this.slotArrayList = slotArrayList;
    }

    @Override
    public SlotAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.slot, parent, false);
        SlotAdapter.MyViewHolder holder = new SlotAdapter.MyViewHolder(view);
        return holder;
    }


    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(SlotAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.txtName.setText("Voltage : "+slotArrayList.get(position).voltage + "\n Price : " + slotArrayList.get(position).price);

//        String details = userArrayList.get(position).mobileno + "\n" + userArrayList.get(position).email ;
//
//        holder.txtDetails.setText(details);
    }

    @Override
    public int getItemCount() {
        return slotArrayList.size();
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
