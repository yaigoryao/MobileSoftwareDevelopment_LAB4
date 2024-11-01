package models;

import android.os.Parcel;
import android.os.Parcelable;

public class ColorManufacturerGroup implements Parcelable {
    public BusInfo.Color color;
    public BusInfo.Manufacturer manufacturer;
    public int totalPassengers;

    // Конструктор по умолчанию
    public ColorManufacturerGroup() {}

    // Конструктор для создания объекта из Parcel
    protected ColorManufacturerGroup(Parcel in) {
        // Восстанавливаем перечисления из строк
        color = BusInfo.Color.valueOf(in.readString());
        manufacturer = BusInfo.Manufacturer.valueOf(in.readString());
        totalPassengers = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Записываем перечисления как строки
        dest.writeString(color.name());
        dest.writeString(manufacturer.name());
        dest.writeInt(totalPassengers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable.Creator реализован корректно
    public static final Parcelable.Creator<ColorManufacturerGroup> CREATOR = new Parcelable.Creator<ColorManufacturerGroup>() {
        @Override
        public ColorManufacturerGroup createFromParcel(Parcel in) {
            // Здесь можно вызывать конструктор, так как контекст статический
            return new ColorManufacturerGroup(in);
        }

        @Override
        public ColorManufacturerGroup[] newArray(int size) {
            return new ColorManufacturerGroup[size];
        }
    };

    @Override
    public String toString() {
        return "Цвет: " + color.toString() + "\nПроизводитель: " + manufacturer.toString() + "\nВсего пассажиров: " + totalPassengers;
    }
}