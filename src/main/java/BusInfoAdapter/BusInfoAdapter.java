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
import ru.msfd.lab4.R;

public class BusInfoAdapter extends BaseAdapter {

        Context ctx;
        LayoutInflater lInflater;
        ArrayList<BusInfo> objects;

        public BusInfoAdapter(Context context, ArrayList<BusInfo> products)
        {
            ctx = context;
            objects = products;
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
            return objects.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.bus_info_item, parent, false);
            }

            BusInfo busInfo = (BusInfo) getItem(position);

            //((TableLayout) view).setBackgroundColor(position % 2 == 0 ? ctx.getColor(R.color.item_odd) : ctx.getColor(R.color.item_even));
            ((TextView) view.findViewById(R.id.bus_id_text_view)).setText(String.valueOf(busInfo.getId()));
            ((TextView) view.findViewById(R.id.bus_color_text_view)).setText(busInfo.getColor().toString());
            ((TextView) view.findViewById(R.id.bus_manufacturer_text_view)).setText(busInfo.getManufacturer().toString());
            ((TextView) view.findViewById(R.id.bus_passenger_capacity_text_view)).setText(String.valueOf(busInfo.getPassengerCapacity()));
            ((TextView) view.findViewById(R.id.bus_engine_power_text_view)).setText(String.valueOf(busInfo.getEnginePower()));
            ((TextView) view.findViewById(R.id.bus_tank_capacity_text_view)).setText(String.valueOf(busInfo.getTankCapacity()));

            return view;
        }
}
