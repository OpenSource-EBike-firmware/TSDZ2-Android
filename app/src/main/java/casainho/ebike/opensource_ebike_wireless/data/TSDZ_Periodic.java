package casainho.ebike.opensource_ebike_wireless.data;

import android.util.Log;

import java.util.HashMap;

import static casainho.ebike.opensource_ebike_wireless.TSDZConst.PERIODIC_ADV_SIZE;

public class TSDZ_Periodic {

    private static final String TAG = "TSDZ_Status";

    public int pedalCadence;
    public int assistLevel;
    public int assistLevelTarget;
    public float batteryVoltage;
    public float batteryCurrent;
    public float wheelSpeed;
    public int braking;
    public int light;
    public int motorHallSensors;
    public int PASPedalRight;
    public int ADCThrottle;
    public int motorTemperature;
    public int throttle;
    public int ADCPedalTorqueSensor;
    public int pedalWeightWithOffset;
    public int pedalWeight;
    public int dutyCycle;
    public int motorSpeedERPS;
    public int FOCAngle;
    public int errorStates;
    public float motorCurrent;
    public int ADCBatteryCurrent;
    public int humanPedalPower;
    public float batterySOC;
    public int motorPower;
    public float tripDistance;
    public int tripTime;
    public long odometer;
    public float wattsHour;
    public int motorState;
    public int motorStateTarget;

    static public HashMap<Integer, Variable> variablesConfig = new HashMap<>();

    public boolean setData(byte[] data) {
        if (data.length != PERIODIC_ADV_SIZE) {
            Log.e(TAG, "Wrong Status BT message size!");
            return false;
        }

        this.batteryVoltage = (((data[1] & 255) << 8) + (data[0] & 255)) / 10;
        this.batteryCurrent = (data[2] & 255) / 5;
        this.wheelSpeed = (((data[4] & 255) << 8) + (data[3] & 255)) / 10;
        this.braking = ((data[5] & 255) & 1);
        this.light = (((data[5] & 255) >> 1) & 1);
        this.motorHallSensors = (data[6] & 255);
        this.PASPedalRight = (data[7] & 255);
        this.ADCThrottle = (data[8] & 255);
        this.motorTemperature = (data[9] & 255);
        this.throttle = (data[10] & 255);
        this.ADCPedalTorqueSensor = ((data[12] & 255) << 8) + ((data[11] & 255));
        this.pedalWeightWithOffset = (data[13] & 255);
        this.pedalWeight = (data[14] & 255);
        this.pedalCadence = (data[15] & 255);
        this.dutyCycle = (data[16] & 255);
        this.motorSpeedERPS = ((data[18] & 255) << 8) + ((data[17] & 255));
        this.FOCAngle = (data[19] & 255);
        this.errorStates = (data[20] & 255);
        this.motorCurrent = (data[21] & 255) / 5;
        this.ADCBatteryCurrent = ((data[23] & 255) << 8) + ((data[22] & 255));
        this.assistLevel = (data[24] & 255);
        this.humanPedalPower = ((data[26] & 255) << 8) + ((data[25] & 255));
        this.batterySOC = (data[27] & 255);
        this.odometer = (((data[31] & 255) << 8) + ((data[30] & 255) << 8) + ((data[29] & 255) << 8) + (data[28] & 255));
        this.wattsHour = (((data[31] & 255) << 8) + ((data[30] & 255) << 8) + ((data[29] & 255) << 8) + (data[28] & 255)) / 10;
        this.motorState = (data[32] & 255);

        return true;
    }

    public byte[] toByteArray() {
        byte[] data = new byte[PERIODIC_ADV_SIZE];
        data[0] = (byte) assistLevelTarget;
        data[1] = (byte) motorStateTarget;

        return data;
    }
}
