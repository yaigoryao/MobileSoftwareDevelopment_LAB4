package DBManager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

import common.Constants;
import models.BusInfo;

public class DBManager extends SQLiteOpenHelper
{
    public DBManager(Context context)
    {
        super(context, Constants.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "create table if not exists " + Constants.BUS_TABLE + " (" +
                Constants.BUS_ID_COLUMN + " integer primary key autoincrement not null, " +
                Constants.BUS_MANUFACTURER_COLUMN + " text not null check(trim( " + Constants.BUS_MANUFACTURER_COLUMN + ") <> ''), " +
                Constants.BUS_COLOR_COLUMN + " text not null check(trim( " + Constants.BUS_COLOR_COLUMN + " ) <> ''), " +
                Constants.BUS_PASSENGER_CAPACITY_COLUMN + " integer default 1 not null check(" + Constants.BUS_PASSENGER_CAPACITY_COLUMN + " > 0 and " + Constants.BUS_PASSENGER_CAPACITY_COLUMN + " < 300)," +
                Constants.BUS_ENGINE_POWER_COLUMN + " integer default 1 not null check(" + Constants.BUS_ENGINE_POWER_COLUMN + " > 0)," +
                Constants.BUS_TANK_CAPACITY_COLUMN + " integer default 1 not null check(" + Constants.BUS_TANK_CAPACITY_COLUMN + " > 0)" +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<BusInfo> fetchBusData(Cursor cursor)
    {
        ArrayList<BusInfo> info = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(Constants.BUS_ID_COLUMN);
            int colorColumnIndex = cursor.getColumnIndex(Constants.BUS_COLOR_COLUMN);
            int manufacturerColumnIndex = cursor.getColumnIndex(Constants.BUS_MANUFACTURER_COLUMN);
            int passengerCapacityColumnIndex = cursor.getColumnIndex(Constants.BUS_PASSENGER_CAPACITY_COLUMN);
            int enginePowerColumnIndex = cursor.getColumnIndex(Constants.BUS_ENGINE_POWER_COLUMN);
            int tankCapacityColumnIndex = cursor.getColumnIndex(Constants.BUS_TANK_CAPACITY_COLUMN);

            do {
                BusInfo busInfo = new BusInfo();
                busInfo.setId(cursor.getInt(idColumnIndex));
                busInfo.setColor(BusInfo.Color.fromString(cursor.getString(colorColumnIndex)));
                busInfo.setManufacturer(BusInfo.Manufacturer.fromString(cursor.getString(manufacturerColumnIndex)));
                busInfo.setPassengerCapacity(cursor.getInt(passengerCapacityColumnIndex));
                busInfo.setEnginePower(cursor.getInt(enginePowerColumnIndex));
                busInfo.setTankCapacity(cursor.getInt(tankCapacityColumnIndex));

                info.add(busInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return info;
    }
}