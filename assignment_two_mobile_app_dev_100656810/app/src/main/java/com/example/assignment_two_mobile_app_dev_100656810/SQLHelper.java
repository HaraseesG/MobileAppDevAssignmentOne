package com.example.assignment_two_mobile_app_dev_100656810;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Geocoder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;

public class SQLHelper extends SQLiteOpenHelper {
    private Context context;
    private static final int DB_VERSION = 1;

    // String[] containing lat/long pairs. Technically comma separated locations
    private final String[] INIT_LAT_LONG_LIST = "[51.213890,-102.462776, 52.321945,-106.584167, 50.288055,-107.793892, 52.757500,-108.286110, 50.393333,-105.551941, 50.930557,-102.807777, 52.856388,-104.610001, 52.289722,-106.666664, 52.201942,-105.123055, 53.278046,-110.005470, 49.136730,-102.990959, 45.484531,-73.597023, 45.266666,-71.900002, 45.349998,-72.516670, 47.333332,-79.433334, 46.049999,-71.966667, 45.400002,-74.033333, 45.683334,-73.433334, 48.099998,-77.783333, 45.500000,-72.316666, 46.349998,-72.550003, 48.119999,-69.180000, 45.599998,-75.250000, 46.099998,-71.300003, 45.700001,-73.633331, 47.680000,-68.879997, 46.716667,-79.099998, 45.016666,-72.099998, 46.033333,-73.116669, 46.566666,-72.750000, 50.216667,-66.383331, 48.383331,-77.233330, 45.529999,-71.279999, 45.250000,-74.129997, 45.900002,-74.169998, 45.266666,-73.616669, 46.900002,-71.833336, 45.500000,-72.900002, 47.533333,-69.800003, 46.966667,-69.783333, 45.883331,-73.150002, 45.849998,-73.766670, 45.500000,-73.516670, 45.400002,-74.133331, 46.299999,-70.883331, 45.783333,-74.000000, 45.316666,-73.266670, 45.616669,-72.949997, 46.116669,-70.666664, 48.650002,-72.449997, 45.570000,-73.900002, 45.633331,-73.849998, 46.216667,-71.783333, 45.383331,-73.983330]".split("\\[")[1].split("]")[0].split(", ");
    private static final String DB_NAME = "locDB";
    private static final String TABLE_NAME = "locsDB";
    private static final String COL_ID = "_id";
    private static final String COL_ADDR = "full_address";
    private static final String COL_LAT = "latitude";
    private static final String COL_LONG="longitude";

    // Constructor
    public SQLHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.getReadableDatabase();
        this.context = context;
    }

    // If database has never existed/does not exist on this device
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY," + COL_ADDR + " TEXT,"
                + COL_LAT +" TEXT,"+ COL_LONG +" TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // If new database version is created
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Initialize empty database with initial LAT_LONG_LIST
    public void initLoc(Geocoder geocoder){
        SQLiteDatabase db = this.getWritableDatabase();
        // for every location available
        for (int i = 0; i < INIT_LAT_LONG_LIST.length; i++) {
            // Get the lat | long : lat_long[0] | lat_long[1]
            String[] lat_long = INIT_LAT_LONG_LIST[i].split(",");
            ContentValues cv = new ContentValues();
            try {
                // Use the geocoder to get the address of the given lat/long pair
                cv.put(COL_ADDR, geocoder.getFromLocation(Double.parseDouble(lat_long[0]), Double.parseDouble(lat_long[1]), 1).get(0).getAddressLine(0));
                cv.put(COL_LAT, Double.parseDouble(lat_long[0]));
                cv.put(COL_LONG, Double.parseDouble(lat_long[1]));
                // Insert into the database. On conflict, remove the old and insert the new
                db.insertWithOnConflict(TABLE_NAME,null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Add new location to list
    public void addNewLoc(String addr, double latitude, double longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_ADDR, addr);
        cv.put(COL_LAT, latitude);
        cv.put(COL_LONG, longitude);
        long result = db.insert(TABLE_NAME,null,cv);

        if(result == -1) {
            Toast.makeText(context,"Something went wrong when adding a location",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context,"Location was successfully added",Toast.LENGTH_SHORT).show();
        }
    }

    // Update existing location in list by id
    public void updateLoc(String id, String addr, double latitude, double longitude){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL_ADDR, addr);
        cv.put(COL_LAT, latitude);
        cv.put(COL_LONG, longitude);
        long res=db.update(TABLE_NAME,cv,"_id = ?",new String[]{id});
        if (res==-1){
            Toast.makeText(context,"Failed to update...",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"Successfully updated",Toast.LENGTH_SHORT).show();
        }

    }

    // delete location from existing list by id
    public void deleteLoc(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        String[] strParams=new String[]{id};
        String DELETE_LOC = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID + " = ?";

        db.execSQL(DELETE_LOC,strParams);
    }

    // Get all database rows
    public Cursor readAll() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

}
