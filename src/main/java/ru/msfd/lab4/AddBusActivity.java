package ru.msfd.lab4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import common.IntentResults;
import models.BusInfo;

public class AddBusActivity extends AppCompatActivity {

    Spinner colorsSpinner;
    Spinner manufacturersSpinner;
    Button addBusButton;
    Button cancelButton;
    EditText passengersCapacityEditText;
    EditText enginePowerEditText;
    EditText tankCapacityEditText;
    BusInfo busInfo = new BusInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_bus);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Setup();
    }

    private void Setup()
    {
        SetupElementsReferences();
        SetupEventHandlers();
        SetupSpinners();
        SetupEditText();
    }

    private void SetupElementsReferences()
    {
        colorsSpinner = (Spinner) findViewById(R.id.color_spinner);
        manufacturersSpinner = (Spinner) findViewById(R.id.manufacturer_spinner);
        addBusButton = (Button) findViewById(R.id.add_bus_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        passengersCapacityEditText = (EditText) findViewById(R.id.passenger_capacity_edit_text);
        enginePowerEditText = (EditText) findViewById(R.id.engine_power_edit_text);
        tankCapacityEditText = (EditText) findViewById(R.id.tank_capacity_edit_text);
    }

    private void SetupEventHandlers()
    {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        addBusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.putExtra("bus", busInfo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        colorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                busInfo.setColor(BusInfo.Color.values()[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Context context = adapterView.getContext();
                if (context instanceof AddBusActivity)
                    ((AddBusActivity) context).colorsSpinner.setSelection(0);

            }
        });

        String sone = "1";
        int ione = 1;

        manufacturersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                busInfo.setManufacturer(BusInfo.Manufacturer.values()[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Context context = adapterView.getContext();
                if (context instanceof AddBusActivity)
                    ((AddBusActivity) context).manufacturersSpinner.setSelection(0);
            }
        });

        passengersCapacityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                String input = s.toString();
                if (!input.isEmpty())
                {
                    try
                    {
                        int value = Integer.parseInt(input);

                        if (value <= 0 || value >= BusInfo.MAX_PASSENGER_CAPACITY)
                        {
                            Toast.makeText(AddBusActivity.this, "Вместительность должна быть больше 0 и меньше  " + BusInfo.MAX_PASSENGER_CAPACITY, Toast.LENGTH_SHORT).show();
                            passengersCapacityEditText.setText(sone);
                            busInfo.setPassengerCapacity(ione);
                        }
                        else busInfo.setPassengerCapacity(value);
                    }
                    catch (NumberFormatException e)
                    {
                        Toast.makeText(AddBusActivity.this, "Ошибка ввода", Toast.LENGTH_SHORT).show();
                        passengersCapacityEditText.setText(sone);
                    }
                }
            }
        });

        enginePowerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                String input = s.toString();
                if (!input.isEmpty())
                {
                    try
                    {
                        int value = Integer.parseInt(input);

                        if (value <= 0)
                        {
                            Toast.makeText(AddBusActivity.this, "Мощность двигателя должна быть больше 0 л.с.", Toast.LENGTH_SHORT).show();
                            enginePowerEditText.setText(sone);
                            busInfo.setEnginePower(ione);
                        }
                        else busInfo.setEnginePower(value);
                    }
                    catch (NumberFormatException e)
                    {
                        Toast.makeText(AddBusActivity.this, "Ошибка ввода", Toast.LENGTH_SHORT).show();
                        enginePowerEditText.setText(sone);
                    }
                }
            }
        });

        tankCapacityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                String input = s.toString();
                if (!input.isEmpty())
                {
                    try
                    {
                        int value = Integer.parseInt(input);

                        if (value <= 0)
                        {
                            Toast.makeText(AddBusActivity.this, "Объем бака должен быть больше 0 л.", Toast.LENGTH_SHORT).show();
                            tankCapacityEditText.setText(sone);
                            busInfo.setTankCapacity(ione);
                        }
                        else busInfo.setTankCapacity(value);
                    }
                    catch (NumberFormatException e)
                    {
                        Toast.makeText(AddBusActivity.this, "Ошибка ввода", Toast.LENGTH_SHORT).show();
                        tankCapacityEditText.setText(sone);
                    }
                }
            }
        });
    }

    private void SetupSpinners()
    {
        ArrayAdapter<BusInfo.Color> colorArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BusInfo.Color.values());
        colorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorsSpinner.setAdapter(colorArrayAdapter);

        ArrayAdapter<BusInfo.Manufacturer> manufacturerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BusInfo.Manufacturer.values());
        manufacturerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        manufacturersSpinner.setAdapter(manufacturerArrayAdapter);
    }

    private void SetupEditText()
    {
        String one = "1";
        passengersCapacityEditText.setText(one);
        enginePowerEditText.setText(one);
        tankCapacityEditText.setText(one);
    }

}