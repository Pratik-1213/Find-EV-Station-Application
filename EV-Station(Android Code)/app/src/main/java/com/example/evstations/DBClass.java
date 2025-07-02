package com.example.evstations;

import static com.mappls.sdk.maps.Mappls.getApplicationContext;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mappls.sdk.maps.Mappls;

public class DBClass extends SQLiteOpenHelper {

    public static String dbname = "EvStations";

    public static String url = "http://192.168.31.18/evstations/api/";

    public static String urlLogin = url + "login.php";
    public static String urlRegistration = url + "registration.php";
    public static String urlUsers = url + "users.php";
    public static String urlProfile = url + "profile.php";
    public static String urlUpdateProfile = url + "updateprofile.php";
    public static String urlStations = url + "stations.php";
    public static String urlFindStations = url + "findstations.php";
    public static String urlDeleteStation = url + "deletestation.php";

    public static String urlAddStation = url + "add_station.php";
    public static String urlStationSlots = url + "station_slots.php";
    public static String urlAddStationSlot = url + "add_slot.php";
    public static String urlAddVehicle = url + "add_vehicle.php";
    public static String urlVehicles = url + "vehicles.php";
    public static String urlBookSlot = url + "bookslot.php";
    public static String urlBookings = url + "bookings.php";
    public static String urlCancelBooking = url + "cancelbooking.php";


    public static SQLiteDatabase database;


    public DBClass(Context context){

        super(context, DBClass.dbname, null, 1);
    }

    public void onCreate(SQLiteDatabase arg) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    public static void execNonQuery(String query){
        //Execute Insert, Update, Delete, Create table queries
        //Log.e("Quesry", query);
        database.execSQL(query);
    }

    public static Cursor getCursorData(String query){
        //Log.d("SQuery", query);
        Cursor res =  database.rawQuery(query, null);
        return res;
    }

    public static String getSingleValue(String query) {
        try {
            Cursor res = getCursorData(query);
            String value = "";
            if (res.moveToNext()) {
                return res.getString(0);
            }
            return value;
        }
        catch (Exception ex)
        {
            return "";
        }
    }

    public static int getNoOfRows(String query){
        try {
            Cursor res = database.rawQuery(query, null);
            return res.getCount();
        }catch (Exception ex)
        {
            return 0;
        }
    }

    public static boolean checkIfRecordExist(String query){
        //Log.e("CheckQuery", query);
        Cursor res =  database.rawQuery(query, null );
        if(res.getCount() > 0)
            return true;
        else
            return false;
    }


    public static boolean doesTableExists(String tableName)
    {
        try{
            Cursor cursor = getCursorData("SELECT * FROM " + tableName);
            return true;
        }
        catch (Exception ex)
        {
            return  false;
        }
    }

    public static boolean doesFieldExist(String tableName, String fieldName)
    {
        try {
            String query = "SELECT " + fieldName + " FROM " + tableName;
            Cursor cursor = getCursorData(query);
            return  true;
        }
        catch (Exception ex)
        {
            return  false;
        }
    }


}
