package spider65.ebike.tsdz2_esp32.data;

import android.util.Log;

import static spider65.ebike.tsdz2_esp32.TSDZConst.STATUS_ADV_SIZE;

public class TSDZ_Status {

    private static final String TAG = "TSDZ_Status";

    public RidingMode ridingMode;
    public int assistLevel;
    public float speed;
    public int cadence;
    public float temperature;
    public int pPower;
    public float volts;
    public float amperes;
    public int status;
    public boolean brake;
    public int wattHour;

    public byte[] data;

    public enum RidingMode {
        OFF_MODE(0),
        POWER_ASSIST_MODE(1),
        TORQUE_ASSIST_MODE(2),
        CADENCE_ASSIST_MODE(3),
        eMTB_ASSIST_MODE(4),
        WALK_ASSIST_MODE(5),
        CRUISE_MODE(6),
        CADENCE_SENSOR_CALIBRATION_MODE(7);

        RidingMode(int value) {
            this.value = value;
        }

        public static RidingMode valueOf(int val) {
            for (RidingMode e : values()) {
                if (e.value ==val) {
                    return e;
                }
            }
            return null;
        }

        public final int value;
    }

    /*
    #pragma pack(1)
    typedef struct _tsdz_status
    {
      volatile uint8_t ui8_riding_mode;
      volatile uint8_t ui8_assist_level;
      volatile uint16_t ui16_wheel_speed_x10;
      volatile uint8_t ui8_pedal_cadence_RPM;
      volatile uint16_t ui16_motor_temperaturex10;
      volatile uint16_t ui16_pedal_power_x10;
      volatile uint16_t ui16_battery_voltage_x1000;
      volatile uint8_t ui8_battery_current_x10;
      volatile uint8_t ui8_controller_system_state;
      volatile uint8_t ui8_braking;
      volatile uint16_t ui16_battery_wh;
    } struct_tsdz_status;
    */


    public void setData(byte[] data) {
        if (data.length != STATUS_ADV_SIZE) {
            Log.e(TAG, "Wrong Debug BT message size!");
            return;
        }
        this.data = data;
        this.ridingMode = RidingMode.valueOf(data[0] & 255);
        this.assistLevel = (data[1] & 255);
        this.speed = (float)(((data[3] & 255) << 8) + (data[2] & 255)) / 10;
        this.cadence = (data[4] & 255);
        this.temperature = (float)(((data[6] & 255) << 8) + (data[5] & 255)) / 10;
        this.pPower = ((data[8] & 255) << 8) + ((data[7] & 255));
        this.pPower = (this.pPower+5)/10;
        this.volts = (float)(((data[10] & 255) << 8) + (data[9] & 255)) / 1000;
        this.amperes = (float)(data[11] & 255) / 10;
        this.status = (data[12] & 255);
        this.brake = (data[13] & 255) != 0;
        this.wattHour = ((data[15] & 255) << 8) + ((data[14] & 255));
    }
}