package com.example.evstations.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.evstations.R;
import com.example.evstations.admin.EvStationSlotsActivity;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<User> userArrayList;
    Context context;

    public UserAdapter(Context ctx, ArrayList<User> userArrayList){

        inflater = LayoutInflater.from(ctx);
        this.userArrayList = userArrayList;
    }

    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user, parent, false);
        UserAdapter.MyViewHolder holder = new UserAdapter.MyViewHolder(view);
        return holder;
    }


    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(UserAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.txtName.setText(userArrayList.get(position).name);

        String details = userArrayList.get(position).mobileno + "\n" + userArrayList.get(position).email ;

        holder.txtDetails.setText(details);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
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
