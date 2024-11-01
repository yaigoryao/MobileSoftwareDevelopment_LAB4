package common;

public class Constants
{
    public static final String CUSTOM_TAG = "CUSTOM_TAG";
    public static final String DB_NAME = "bus_db";
    public static final String BUS_TABLE = "bus_db";
    public static final String BUS_ID_COLUMN = "bus_id";
    public static final String BUS_COLOR_COLUMN = "bus_color";
    public static final String BUS_MANUFACTURER_COLUMN = "manufacturer";
    public static final String BUS_PASSENGER_CAPACITY_COLUMN = "passengers_capacity";
    public static final String BUS_ENGINE_POWER_COLUMN = "engine_power";
    public static final String BUS_TANK_CAPACITY_COLUMN = "tank_capacity";
    public static final String FILENAME = "LAB4FILE";
    public static final String ERRORS_TAG = "ERRORS";

    public enum REQUEST_CODES { ADD_BUS, GROUP_BUSES, TOTAL_PASSENGER_CAPACITY, MAX_PASSENGER_CAPACITY, TANK_CAPACITY_LESS_THAN, ENGINE_POWER_LESS_THAN_AVG, PASSENGER_CAPACITY_GREATER_THAN, }
}
