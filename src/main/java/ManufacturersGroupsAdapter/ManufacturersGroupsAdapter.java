package ManufacturersGroupsAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import models.ColorManufacturerGroup;
import models.ManufacturerGroup;
import ru.msfd.lab4.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.ArrayList;

import models.BusInfo;
import models.ColorManufacturerGroup;
import ru.msfd.lab4.MainActivity;
import ru.msfd.lab4.R;

public class ManufacturersGroupsAdapter extends BaseAdapter{
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ManufacturerGroup> objects;

    public ManufacturersGroupsAdapter(Context context, ArrayList<ManufacturerGroup> groups)
    {
        ctx = context;
        objects = groups;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.manufactorer_group_item, parent, false);
        }

        ManufacturerGroup group = (ManufacturerGroup) getItem(position);

        ((TextView) view.findViewById(R.id.manufactorer_group_name_value)).setText(group.manufacturer.toString());
        ((TextView) view.findViewById(R.id.group_avg_engine_power_value)).setText(String.valueOf(group.avgEnginePower));

        return view;
    }
}
