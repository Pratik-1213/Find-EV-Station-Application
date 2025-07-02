package com.example.evstations.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.evstations.DBClass;
import com.example.evstations.R;
import com.example.evstations.admin.EvStationSlotsActivity;
import com.example.evstations.user.StationDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EvstationsAdapter extends RecyclerView.Adapter<EvstationsAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Station> stationArrayList;
    Context context;

    public EvstationsAdapter(Context ctx, ArrayList<Station> stationArrayList){

        inflater = LayoutInflater.from(ctx);
        this.stationArrayList = stationArrayList;
    }

    @Override
    public EvstationsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.evstation, parent, false);
        EvstationsAdapter.MyViewHolder holder = new EvstationsAdapter.MyViewHolder(view);
        return holder;
    }


    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(EvstationsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.txtName.setText(stationArrayList.get(position).name);

        String details = stationArrayList.get(position).location + "\n" + stationArrayList.get(position).city ;

        holder.txtDetails.setText(details);




        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.e("id", "onClick: " + stationArrayList.get(position).id);
                    Intent intent = new Intent(context, StationDetailsActivity.class);
                    intent.putExtra("id", "" + stationArrayList.get(position).id);
                    intent.putExtra("name", "" + stationArrayList.get(position).name);
                    intent.putExtra("location", "" + stationArrayList.get(position).location);
                    intent.putExtra("city", "" + stationArrayList.get(position).city);
                    intent.putExtra("latitude", "" + stationArrayList.get(position).latitude);
                    intent.putExtra("longitude", "" + stationArrayList.get(position).longitude);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception ex) {
                    Log.e("Ex", "onClick: " + ex);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return stationArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtDetails;
        LinearLayout layoutCard;
        ImageView imageView;



        public MyViewHolder(View view) {
            super(view);
            context = itemView.getContext();
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            layoutCard = (LinearLayout) view.findViewById(R.id.layoutCard);
            imageView = (ImageView) view.findViewById(R.id.imgView);

        }
    }
}
