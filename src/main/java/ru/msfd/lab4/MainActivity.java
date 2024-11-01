package ru.msfd.lab4;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import BusInfoAdapter.BusInfoAdapter;
import DBManager.DBManager;
import common.Constants;
import models.BusInfo;
import models.ColorManufacturerGroup;
import models.ManufacturerGroup;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    DBManager dbManager;
    Button addBusButton;
    Button loadBusesButton;
    Button removeBusButton;
    ListView busesList;

    ArrayList<BusInfo> busesInfo;
    BusInfo selectedBusInfo;

    SQLiteDatabase db;

    BusInfoAdapter busInfoAdapter;

    Map<Integer, Runnable> itemsFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Setup();

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        dbManager.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (Constants.REQUEST_CODES.values()[requestCode])
        {
            case ADD_BUS:
                if(resultCode == RESULT_OK)
                {
                    if (data == null) Toast.makeText(this, "Данные автобуса не выбраны!", Toast.LENGTH_LONG).show();
                    else
                    {
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                BusInfo busInfo = data.getParcelableExtra("bus");
                                if (busInfo != null) {
                                    ContentValues cv = new ContentValues();
                                    cv.put(Constants.BUS_COLOR_COLUMN, busInfo.getColor().toString());
                                    cv.put(Constants.BUS_MANUFACTURER_COLUMN, busInfo.getManufacturer().toString());
                                    cv.put(Constants.BUS_PASSENGER_CAPACITY_COLUMN, busInfo.getPassengerCapacity());
                                    cv.put(Constants.BUS_ENGINE_POWER_COLUMN, busInfo.getEnginePower());
                                    cv.put(Constants.BUS_TANK_CAPACITY_COLUMN, busInfo.getTankCapacity());
                                    db.insert(Constants.BUS_TABLE, null, cv);
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadBusesButton.performClick();
                                        Toast.makeText(MainActivity.this, "Автобус добавлен", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        });
                        executorService.shutdown();
                    }
                }
                else if (resultCode == RESULT_CANCELED) Toast.makeText(this, "Отменено", Toast.LENGTH_LONG).show();
                break;

        }
    }

    private void SetupMenu()
    {
        itemsFunctions = new HashMap<>();
        itemsFunctions.put(R.id.menu_sort_buses, new Runnable() {
            @Override
            public void run() {
                String query = "select * from " + Constants.BUS_TABLE +
                        " order by " + Constants.BUS_PASSENGER_CAPACITY_COLUMN + " asc";

                String label = "=====Сортировка по вместимости=====";
                ArrayList<BusInfo> data = dbManager.fetchBusData(db.rawQuery(query, null));
                Log.d(Constants.CUSTOM_TAG, label);
                for (BusInfo info : data) Log.d(Constants.CUSTOM_TAG, info.toString());
                FileOutputStream fos = null;
                try
                {
                    fos = openFileOutput(Constants.FILENAME, MODE_PRIVATE);
                    Log.d(Constants.CUSTOM_TAG, "Начало записи");
                    fos.write((label+"\n").getBytes());
                    StringBuilder sb = new StringBuilder();
                    for (BusInfo info : data) sb.append(info.toString() + "\n");
                    fos.write(sb.toString().getBytes());
                    Log.d(Constants.CUSTOM_TAG, "Файл записан");
                }
                catch(IOException e) { Log.d(Constants.ERRORS_TAG, e.toString()); }
                finally
                {
                    try
                    {
                        if(fos!=null) fos.close();
                    }
                    catch(IOException e){ Log.d(Constants.ERRORS_TAG, e.toString()); }
                }

                Intent intent = new Intent("android.intent.action.sortedbuses");
                intent.putExtra("buses", data);
                startActivity(intent);
            }
        });

        itemsFunctions.put(R.id.menu_group_buses, new Runnable() {
            @Override
            public void run() {


                final String totalPassengers = "total_passengers";
                String query = "select " + Constants.BUS_COLOR_COLUMN + ", " + Constants.BUS_MANUFACTURER_COLUMN +
                        ", sum(" + Constants.BUS_PASSENGER_CAPACITY_COLUMN + ") as " + totalPassengers +
                        " from " + Constants.BUS_TABLE + " group by " + Constants.BUS_COLOR_COLUMN + ", " + Constants.BUS_MANUFACTURER_COLUMN;

                Log.d(Constants.CUSTOM_TAG, "=====Группировка по цвет и производителю=====");
                ArrayList<ColorManufacturerGroup> groups = new ArrayList<>();
                Cursor cursor = db.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    int totalPassengersColumnIndex = cursor.getColumnIndex(totalPassengers);
                    int colorColumnIndex = cursor.getColumnIndex(Constants.BUS_COLOR_COLUMN);
                    int manufacturerColumnIndex = cursor.getColumnIndex(Constants.BUS_MANUFACTURER_COLUMN);

                    do {
                        ColorManufacturerGroup group = new ColorManufacturerGroup();
                        group.totalPassengers = cursor.getInt(totalPassengersColumnIndex);
                        group.color = BusInfo.Color.fromString(cursor.getString(colorColumnIndex));
                        group.manufacturer = BusInfo.Manufacturer.fromString(cursor.getString(manufacturerColumnIndex));
                        Log.d(Constants.CUSTOM_TAG, group.toString());
                        groups.add(group);
                    } while (cursor.moveToNext());
                }
                Intent intent = new Intent("android.intent.action.showgroups");
                intent.putExtra("groups", groups);
                intent.putExtra("type", 1);
                startActivity(intent);
                cursor.close();

                //ArrayList<BusInfo> data = dbManager.fetchBusData(db.rawQuery(query, null));
                //for (BusInfo info : data) Log.d(Constants.CUSTOM_TAG, info.toString());

                //Intent intent = new Intent("android.intent.action.sortedbuses");
                //intent.putExtra("buses", new ArrayList<BusInfo>(data));
                //startActivity(intent);
            }
        });

        itemsFunctions.put(R.id.menu_total_passenger_capacity, new Runnable() {
            @Override
            public void run() {
                String totalPassengersCountColumn = "total_passengers";
                String query = "select sum("+ Constants.BUS_PASSENGER_CAPACITY_COLUMN +") as " + totalPassengersCountColumn + " from " + Constants.BUS_TABLE;
                Cursor cursor = db.rawQuery(query, null);
                int totalPassengersCount = 0;
                if (cursor.moveToFirst()) {
                    totalPassengersCount = cursor.getInt(cursor.getColumnIndex(totalPassengersCountColumn));
                }
                cursor.close();
                Log.d(Constants.CUSTOM_TAG, "=====Всего пассажиров=====");
                String result = "Всего пассажиров может поместиться во всех автобусах: " + totalPassengersCount;
                FileOutputStream fos = null;
                try
                {
                    fos = openFileOutput(Constants.FILENAME, MODE_PRIVATE);
                    fos.write(("\n"+result+"\n").getBytes());
                }
                catch(IOException e) { Log.d(Constants.ERRORS_TAG, e.toString()); }
                finally
                {
                    try
                    {
                        if(fos!=null) fos.close();
                    }
                    catch(IOException e){ Log.d(Constants.ERRORS_TAG, e.toString()); }
                }
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                Log.d(Constants.CUSTOM_TAG, result);
            }
        });

        itemsFunctions.put(R.id.menu_avg_engine_power, new Runnable() {
            @Override
            public void run() {
                final String avgEnginePower = "avg_engine_power";
                String query = "select " + Constants.BUS_MANUFACTURER_COLUMN +
                        ", avg(" + Constants.BUS_ENGINE_POWER_COLUMN + ") as " + avgEnginePower +
                        " from " + Constants.BUS_TABLE + " group by " + Constants.BUS_MANUFACTURER_COLUMN;
                String label = "=====Средняя мощность двигателя в группе=====";
                Log.d(Constants.CUSTOM_TAG, label);
                ArrayList<models.ManufacturerGroup> groups = new ArrayList<>();
                Cursor cursor = db.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    int avgEnginePowerColumnIndex = cursor.getColumnIndex(avgEnginePower);
                    int manufacturerColumnIndex = cursor.getColumnIndex(Constants.BUS_MANUFACTURER_COLUMN);

                    do {
                        models.ManufacturerGroup group = new models.ManufacturerGroup();
                        group.avgEnginePower = cursor.getFloat(avgEnginePowerColumnIndex);
                        group.manufacturer = BusInfo.Manufacturer.fromString(cursor.getString(manufacturerColumnIndex));
                        groups.add(group);
                        Log.d(Constants.CUSTOM_TAG, group.toString());
                    } while (cursor.moveToNext());
                }
                cursor.close();
                FileOutputStream fos = null;
                try
                {
                    fos = openFileOutput(Constants.FILENAME, MODE_PRIVATE);
                    fos.write(label.getBytes());
                    StringBuilder sb = new StringBuilder();
                    for (models.ManufacturerGroup info : groups) sb.append(info.toString() + "\n");
                    fos.write(sb.toString().getBytes());
                }
                catch(IOException e) { Log.d(Constants.ERRORS_TAG, e.toString()); }
                finally
                {
                    try
                    {
                        if(fos!=null) fos.close();
                    }
                    catch(IOException e){ Log.d(Constants.ERRORS_TAG, e.toString()); }
                }
                Intent intent = new Intent("android.intent.action.showgroups");
                intent.putExtra("groups", groups);
                intent.putExtra("type", 2);
                startActivity(intent);
                cursor.close();
            }
        });

        itemsFunctions.put(R.id.menu_max_passenger_capacity, new Runnable() {
            @Override
            public void run() {

                final String avgEnginePower = "avg_engine_power";
                String query = "select * " +
                        "from " + Constants.BUS_TABLE + " where " + Constants.BUS_PASSENGER_CAPACITY_COLUMN + " = (select max(" + Constants.BUS_PASSENGER_CAPACITY_COLUMN + ") from " + Constants.BUS_TABLE + ")";

                Log.d(Constants.CUSTOM_TAG, "=====Строка с максимальным значением вместимости=====");
                ArrayList<BusInfo> data = dbManager.fetchBusData(db.rawQuery(query, null));
                if(data.size() > 0)
                {
                    String busInfo = data.get(0).toString();
                    Toast.makeText(MainActivity.this, busInfo, Toast.LENGTH_LONG).show();
                    Log.d(Constants.CUSTOM_TAG, busInfo);
                }
            }
        });

//        itemsFunctions.put(R.id.menu_, new Runnable() {
//            @Override
//            public void run() {
//
//                final String avgEnginePower = "avg_engine_power";
//                String query = "select * " +
//                        "from " + Constants.BUS_TABLE + " where " + Constants.BUS_PASSENGER_CAPACITY_COLUMN + " = (select max(" + Constants.BUS_PASSENGER_CAPACITY_COLUMN + ") from " + Constants.BUS_TABLE + ")";
//
//                Log.d(Constants.CUSTOM_TAG, "=====Строка с максимальным значением вместимости=====");
//                ArrayList<BusInfo> data = dbManager.fetchBusData(db.rawQuery(query, null));
//                if(data.size() > 0)
//                {
//                    String busInfo = data.get(0).toString();
//                    Toast.makeText(MainActivity.this, busInfo, Toast.LENGTH_LONG).show();
//                    Log.d(Constants.CUSTOM_TAG, busInfo);
//                }
//            }
//        });

        itemsFunctions.put(R.id.menu_tank_capacity_less_than, new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Введите минимальное число: ");

                final EditText input = new EditText(MainActivity.this);
                input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent("android.intent.action.showlist");
                        int minValue = Integer.parseInt(input.getText().toString());
                        String query = "select * from " + Constants.BUS_TABLE +
                                " where " + Constants.BUS_TANK_CAPACITY_COLUMN + " > " + minValue;
                        ArrayList<BusInfo> data = dbManager.fetchBusData(db.rawQuery(query, null));
                        Log.d(Constants.CUSTOM_TAG, "=====Автобусы, у которых емкость бака больше заданного значения=====");
                        for (BusInfo info : data) Log.d(Constants.CUSTOM_TAG, info.toString());
                        intent.putExtra("buses", data);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        itemsFunctions.put(R.id.menu_engine_power_less_than_avg, new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent("android.intent.action.showlist");
                String query = "select * from " + Constants.BUS_TABLE +
                        " where " + Constants.BUS_ENGINE_POWER_COLUMN + " < " + "(select avg(" + Constants.BUS_ENGINE_POWER_COLUMN+ ") from " + Constants.BUS_TABLE + ")";
                ArrayList<BusInfo> data = dbManager.fetchBusData(db.rawQuery(query, null));
                Log.d(Constants.CUSTOM_TAG, "=====Автобусы, у которых мощность двигателя меньше, чем средняя мощность двигателей=====");
                for (BusInfo info : data) Log.d(Constants.CUSTOM_TAG, info.toString());
                intent.putExtra("buses", data);
                startActivity(intent);
            }
        });

        itemsFunctions.put(R.id.menu_passenger_capacity_greater_than, new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Введите минимальное число: ");

                final EditText input = new EditText(MainActivity.this);
                input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int minValue = Integer.parseInt(input.getText().toString());
                        String query = "select * from " + Constants.BUS_TABLE +
                                " where " + Constants.BUS_TANK_CAPACITY_COLUMN + " > " + minValue +
                                " or " + Constants.BUS_PASSENGER_CAPACITY_COLUMN + " > " + minValue +
                                " or " + Constants.BUS_ENGINE_POWER_COLUMN + " > " + minValue + " limit 1";
                        ArrayList<BusInfo> data = dbManager.fetchBusData(db.rawQuery(query, null));
                        String header = "=====Автобус (1), у которых емкость бака больше заданного значения=====";
                        Log.d(Constants.CUSTOM_TAG, header);
                        Log.d(Constants.CUSTOM_TAG, data.get(0).toString());
                        Toast.makeText(MainActivity.this, header + "\n" + data.get(0).toString(), Toast.LENGTH_LONG).show();

                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    private void Setup()
    {
        busesInfo = new ArrayList<>();
        SetupMenu();
        SetupDB();
        db = dbManager.getWritableDatabase();
        SetupViewReferences();
        SetupButtons();
        SetupList();
        loadBusesButton.performClick();
    }

    private void SetupViewReferences()
    {
        addBusButton = (Button) findViewById(R.id.add_bus_button);
        removeBusButton = (Button) findViewById(R.id.remove_bus_button);
        loadBusesButton = (Button) findViewById(R.id.load_bus_button);
        busesList = (ListView) findViewById(R.id.buses_list);
    }

    private void SetupList()
    {
        busInfoAdapter = new BusInfoAdapter(this, busesInfo);
        busesList.setAdapter(busInfoAdapter);

        busesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBusInfo = busesInfo.get(i);
            }
        });
    }

    private void SetupButtons()
    {
        addBusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent("android.intent.action.addbus"), Constants.REQUEST_CODES.ADD_BUS.ordinal());
            }
        });

        loadBusesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExecutorService executorService = Executors.newSingleThreadExecutor();

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        busesInfo.clear();
                        busesInfo.addAll(dbManager.fetchBusData(db.query(Constants.BUS_TABLE, null, null, null, null, null, null)));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                busInfoAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Список обновлен", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });
                executorService.shutdown();
            }
        });

        removeBusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.delete(Constants.BUS_TABLE, Constants.BUS_ID_COLUMN + " = ?", new String[] { String.valueOf(selectedBusInfo.getId()) });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadBusesButton.performClick();
                                Toast.makeText(MainActivity.this, "Автобус удален", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                executorService.shutdown();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Runnable fun = itemsFunctions.getOrDefault(item.getItemId(), null);
        if(fun != null) fun.run();
        return super.onOptionsItemSelected(item);
    }

    private void SetupDB()
    {
        dbManager = new DBManager(this);
    }
}