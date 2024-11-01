package models;

import android.os.Parcel;
import android.os.Parcelable;

public class ManufacturerGroup implements Parcelable {
    public BusInfo.Manufacturer manufacturer;
    public float avgEnginePower;

    public ManufacturerGroup()
    {

    }

    public ManufacturerGroup(BusInfo.Manufacturer manufacturer, float avgEnginePower) {
        this.manufacturer = manufacturer;
        this.avgEnginePower = avgEnginePower;
    }

    protected ManufacturerGroup(Parcel in) {
        manufacturer = BusInfo.Manufacturer.fromString(in.readString());
        avgEnginePower = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(manufacturer.toString());
        dest.writeFloat(avgEnginePower);
    }

    public static final Creator<ManufacturerGroup> CREATOR = new Creator<ManufacturerGroup>() {
        @Override
        public ManufacturerGroup createFromParcel(Parcel in) {
            return new ManufacturerGroup(in);
        }

        @Override
        public ManufacturerGroup[] newArray(int size) {
            return new ManufacturerGroup[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Производитель: " + manufacturer.toString() + "\nСредняя мощность двигателя: " + avgEnginePower;
    }
}