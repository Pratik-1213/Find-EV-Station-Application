package com.example.evstations.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.evstations.admin.EVStationsActivity;
import com.example.evstations.admin.EvStationSlotsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Station> stationArrayList;
    Context context;

    public StationAdapter(Context ctx, ArrayList<Station> stationArrayList){

        inflater = LayoutInflater.from(ctx);
        this.stationArrayList = stationArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.station, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.txtName.setText(stationArrayList.get(position).name);

        String details = stationArrayList.get(position).location + "\n" + stationArrayList.get(position).city ;

        holder.txtDetails.setText(details);
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Edit Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, DBClass.urlDeleteStation,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject jsonObject = null;
                                Log.d("Response", ">> "+response);

                                try {
                                    jsonObject = new JSONObject(response);
                                    JSONArray jsonData = jsonObject.getJSONArray("data");
                                    boolean succcess = false;
                                    for(int i = 0; i < jsonData.length(); i++){
                                        JSONObject jo = jsonData.getJSONObject(i);
                                        if(jo.getString("status").equals("success")) {

                                            succcess = true;
//                                            Intent intent = new Intent(context, EVStationsActivity.class);
//                                            context.startActivity(intent);
//                                            ((Activity)context).finish();
                                            stationArrayList.remove(position);
                                            notifyItemRemoved(position);

                                            notifyItemRangeChanged(position, stationArrayList.size());
                                            Toast.makeText(context,"Deleted..", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                    if(succcess) {
                                        Toast.makeText(context,"Deleted..", Toast.LENGTH_SHORT).show();
                                        stationArrayList.remove(position);
                                        notifyItemRemoved(position);

                                        notifyItemRangeChanged(position, stationArrayList.size());
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Delete Failed", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Log.d("Exception", ">> "+e);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.e("Exception 2", error.toString());
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", stationArrayList.get(position).id);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }
        });



        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.e("id", "onClick: " + stationArrayList.get(position).id);
                    Intent intent = new Intent(context, EvStationSlotsActivity.class);
                    intent.putExtra("id", "" + stationArrayList.get(position).id);
                    intent.putExtra("name", "" + stationArrayList.get(position).name);
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
        ImageView imageView, imgEdit, imgDelete;



        public MyViewHolder(View view) {
            super(view);
            context = itemView.getContext();
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            layoutCard = (LinearLayout) view.findViewById(R.id.layoutCard);
            imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
            imageView = (ImageView) view.findViewById(R.id.imgView);

        }
    }
}
