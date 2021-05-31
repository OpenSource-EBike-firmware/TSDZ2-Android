package casainho.ebike.opensource_ebike_wireless;

public interface TSDZConst {

    int NO_ERROR                                  = 0;
    int ERROR_MOTOR_BLOCKED                       = 1;
    int ERROR_TORQUE_SENSOR                       = 2;
    /*
    int ERROR_BRAKE_APPLIED_DURING_POWER_ON       = 3;  // currently not used
    int ERROR_THROTTLE_APPLIED_DURING_POWER_ON    = 4;  // currently not used
    int ERROR_NO_SPEED_SENSOR_DETECTED            = 5;  // currently not used
    */
    int ERROR_LOW_CONTROLLER_VOLTAGE              = 6;  // controller works with no less than 15 V so give error code if voltage is too low
    int ERROR_OVERVOLTAGE                         = 8;
    int ERROR_TEMPERATURE_LIMIT                   = 9;
    int ERROR_TEMPERATURE_MAX                     = 10;

    // size in bytes of the Status/Debug BT notifications
    int DEBUG_ADV_SIZE = 16;
    int PERIODIC_ADV_SIZE = 37;
    int CONFIGURATIONS_ADV_SIZE = 161;
    int CONFIGURATIONS_VERSION = (byte) 0xA6;
}
