package ru.msfd.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import BusInfoAdapter.ColorsManufactorerAdapter;
import ManufacturersGroupsAdapter.ManufacturersGroupsAdapter;


public class ShowGroups extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_groups);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Setup();
    }

    private void Setup()
    {
        Intent intent = getIntent();
        switch(intent.getIntExtra("type", -1))
        {
            case 1: ((ListView) findViewById(R.id.groups_list)).setAdapter(new ColorsManufactorerAdapter(this, intent.getParcelableArrayListExtra("groups")));
            break;
            case 2: ((ListView) findViewById(R.id.groups_list)).setAdapter(new ManufacturersGroupsAdapter(this, intent.getParcelableArrayListExtra("groups")));
            break;
            default: break;
        }

    }
}