package models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class BusInfo implements Parcelable
{
    public static final int MAX_PASSENGER_CAPACITY = 300;

    public enum Manufacturer
    {
        UNKNOWN("Неизвестно"),
        VOLGANIN("Волжанин"),
        KAVZ("КАвЗ (Курганский автобусный завод)"),
        GAZ("ПАО \"ГАЗ\" (Горьковский автомобильный завод)"),
        NEFAZ("НЕФАЗ (Нефтекамский автозавод)"),
        MERCEDESBENZ("Mercedes-Benz (Daimler AG)"),
        MAN("MAN Truck & Bus"),
        VOLVO("Volvo Buses"),
        BYD("BYD Auto"),
        FLYER("New Flyer");

        private String _name;
        Manufacturer(String name) { _name = name; }

        public static Manufacturer fromString(String manufacturer)
        {
            for(Manufacturer m : Manufacturer.values())
            {
                if(m._name.equalsIgnoreCase(manufacturer)) return m;
            }
            return UNKNOWN;
        }

        @Override
        public String toString() { return _name; }
    };

    public enum Color
    {
        UNKNOWN("Неизвестно"),
        RED("Красный"),
        GREEN("Зеленый"),
        BLUE("Синий"),
        WHITE("Белый"),
        YELLOW("Желтый");
        private String _name;
        Color(String name) { _name = name; }

        public static Color fromString(String color)
        {
            for(Color c : Color.values())
            {
                if(c._name.equalsIgnoreCase(color)) return c;
            }
            return UNKNOWN;
        }

        @Override
        public String toString() { return _name; }
    }

    private Color _color = Color.UNKNOWN;
    private Manufacturer _manufacturer = Manufacturer.UNKNOWN;
    private int _passengerCapacity = 1;
    private int _enginePower = 1;
    private int _tankCapacity = 1;
    private int _id = -1;

    public BusInfo() { }

    public BusInfo(int color, int manufacturer, int passengerCapacity, int enginePower, int tankCapacity, int id)
    {
        setColor(Color.values()[color]);
        setManufacturer(Manufacturer.values()[manufacturer]);
        setPassengerCapacity(passengerCapacity);
        setEnginePower(enginePower);
        setTankCapacity(tankCapacity);
        setId(id);
    }

    protected BusInfo(Parcel parcel)
    {
        this(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt());
    }

    public static final Creator<BusInfo> CREATOR = new Creator<BusInfo>() {
        @Override
        public BusInfo createFromParcel(Parcel in) {
            return new BusInfo(in);
        }

        @Override
        public BusInfo[] newArray(int size) {
            return new BusInfo[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i)
    {
        parcel.writeInt(_color.ordinal());
        parcel.writeInt(_manufacturer.ordinal());
        parcel.writeInt(_passengerCapacity);
        parcel.writeInt(_enginePower);
        parcel.writeInt(_tankCapacity);
        parcel.writeInt(_id );

    }

    public int getPassengerCapacity() { return _passengerCapacity; }
    public void setPassengerCapacity(int capacity)
    {
        if(capacity <= 0 || capacity > MAX_PASSENGER_CAPACITY) throw new IllegalArgumentException("Capacity must be greater than zero and lower than " + MAX_PASSENGER_CAPACITY);
        _passengerCapacity = capacity;
    }

    public int getEnginePower() { return _enginePower; }
    public void setEnginePower(int power)
    {
        if(power <= 0) throw new IllegalArgumentException("Power must be greater than zero");
        _enginePower = power;
    }

    public int getTankCapacity() { return _tankCapacity; }
    public void setTankCapacity(int capacity)
    {
        if(capacity <= 0) throw new IllegalArgumentException("Capacity must be greater than zero");
        _tankCapacity = capacity;
    }

    public int getId() { return _id; }
    public void setId(int id) { _id = id; }

    public Color getColor() { return _color; }
    public void setColor(Color color) { _color = color; }

    public Manufacturer getManufacturer() { return _manufacturer; }
    public void setManufacturer(Manufacturer manufacturer) { _manufacturer = manufacturer; }

    @Override
    public String toString()
    {
        return "ID: " + _id + "\nЦвет: " + _color + "\nПроизводитель: " + _manufacturer + "\nВместимость: " + _passengerCapacity + "\nМощность двигателя: " + _enginePower + "\nЕмкость бака: " + _tankCapacity;
    }
}
