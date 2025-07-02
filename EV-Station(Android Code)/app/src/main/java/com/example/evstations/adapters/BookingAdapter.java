package com.example.evstations.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.evstations.user.MyBookingsActivity;
import com.example.evstations.user.ProfileActivity;
import com.example.evstations.user.StationDetailsActivity;
import com.example.evstations.user.UserHomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Booking> bookingArrayList;
    Context context;

    public BookingAdapter(Context ctx, ArrayList<Booking> bookingArrayList){

        inflater = LayoutInflater.from(ctx);
        this.bookingArrayList = bookingArrayList;
    }

    @Override
    public BookingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.booking, parent, false);
        BookingAdapter.MyViewHolder holder = new BookingAdapter.MyViewHolder(view);
        return holder;
    }


    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(BookingAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {



        String stationDetails = bookingArrayList.get(position).station +" - "+ bookingArrayList.get(position).city +"\n "+bookingArrayList.get(position).location+"\n" + "\n"+ bookingArrayList.get(position).slot +"\n Booked For - "+bookingArrayList.get(position).b_date+"\n";
        stationDetails +="From - "+bookingArrayList.get(position).intime + " To "+bookingArrayList.get(position).outtime + "\n Amount : "+ bookingArrayList.get(position).amount;
        holder.txtStationDetails.setText(stationDetails);
        holder.txtVehicleDetails.setText(bookingArrayList.get(position).vehicle);
        holder.txtBookingDetails.setText(bookingArrayList.get(position).bookingon);

        if (bookingArrayList.get(position).b_status.equals("Booked"))
        {
            holder.imgIcon.setImageResource(R.drawable.b_done);
        }
        else
            holder.imgIcon.setImageResource(R.drawable.b_cancel);

        holder.txtStatus.setText(bookingArrayList.get(position).b_status);
        if (bookingArrayList.get(position).from.equals("user"))
        {
            holder.layoutStatus.setVisibility(View.VISIBLE);
           // holder.txtStatus.setText(bookingArrayList.get(position).b_status);
            if (bookingArrayList.get(position).b_status.equals("Booked"))
            {
                holder.btnStatus.setVisibility(View.VISIBLE);
                holder.btnStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBClass.urlCancelBooking,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        JSONObject jsonObject = null;
                                        Log.d("Response", ">> "+response);

                                        try {
                                            jsonObject = new JSONObject(response);

                                            Log.e( "onResponse: ", jsonObject.getString("status") );

                                            if(jsonObject.getString("status").equals("success")) {

//                                                Intent intent = new Intent(context, UserHomeActivity.class);
//                                                context.startActivity(intent);
                                                bookingArrayList.remove(position);
                                                notifyItemRemoved(position);

                                                notifyItemRangeChanged(position, bookingArrayList.size());
                                                Toast.makeText(context, "Booking Cancelled Successfully...", Toast.LENGTH_LONG).show();

                                            }
                                            else
                                            {
                                                Toast.makeText(context, "Failed...", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(context, "Check Internet Connection...", Toast.LENGTH_LONG).show();
                                            Log.e("Exception", ">> "+e);
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Log.e("Exception", error.toString());
                                        Toast.makeText(context, "Check Internet Connection...", Toast.LENGTH_LONG).show();
                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String> params = new HashMap<>();
                                params.put("id", bookingArrayList.get(position).id);
                                Log.e("Params", params.toString());
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);
                    }
                });
            }
            else
            {
                holder.btnStatus.setVisibility(View.GONE);
            }
        }
    }



    @Override
    public int getItemCount() {
        return bookingArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtStationDetails, txtVehicleDetails, txtBookingDetails, txtStatus;
        Button btnStatus;

        LinearLayout layoutCard, layoutStatus;
        ImageView imgIcon;



        public MyViewHolder(View view) {
            super(view);
            context = itemView.getContext();
            txtStationDetails = (TextView) view.findViewById(R.id.txtStationDetails);
            txtVehicleDetails = (TextView) view.findViewById(R.id.txtVehicleDetails);
            txtBookingDetails = (TextView) view.findViewById(R.id.txtBookingDetails);
            layoutCard = (LinearLayout) view.findViewById(R.id.layoutCard);
            layoutStatus = (LinearLayout) view.findViewById(R.id.layoutStatus);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            btnStatus = (Button) view.findViewById(R.id.btnStatus);
            imgIcon = (ImageView) view.findViewById(R.id.imgIcon);

        }
    }
}
