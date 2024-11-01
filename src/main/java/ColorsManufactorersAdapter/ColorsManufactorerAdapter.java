package BusInfoAdapter;

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

public class ColorsManufactorerAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ColorManufacturerGroup> objects;

    public ColorsManufactorerAdapter(Context context, ArrayList<ColorManufacturerGroup> groups)
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
            view = lInflater.inflate(R.layout.colors_manufactorer_group_item, parent, false);
        }

        ColorManufacturerGroup group = (ColorManufacturerGroup) getItem(position);

        ((TextView) view.findViewById(R.id.group_color_value)).setText(group.color.toString());
        ((TextView) view.findViewById(R.id.group_manufactorer_value)).setText(group.manufacturer.toString());
        ((TextView) view.findViewById(R.id.total_passengers_capacity_value)).setText(String.valueOf(group.totalPassengers));

        return view;
    }
}
