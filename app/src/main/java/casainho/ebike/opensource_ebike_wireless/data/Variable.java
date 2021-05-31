package casainho.ebike.opensource_ebike_wireless.data;

import casainho.ebike.opensource_ebike_wireless.MyApp;
import casainho.ebike.opensource_ebike_wireless.R;

public class Variable {
    public DataType dataType;
    public int labelTV;
    public int valueTV;

    public Variable (int labelTV, int valueTV, DataType dataType) {
        this.dataType = dataType;
        this.labelTV = labelTV;
        this.valueTV = valueTV;
    }

    public enum DataType {
        batteryVoltage,
        batteryCurrent,
        batteryResistanceEstimated,
        batterySOC,
        batteryUsedEnergy,
        batteryADCCurrent,
        motorPower,
        motorCurrent,
        motorTemperature,
        motorSpeed,
        humanPower,
        pedalCadence,
        pedalSide,
        pedalWeight,
        pedalWeightWithOffset,
        torqueSensorADC,
        speed,
        odometer,
        throttle,
        throttleADC,
        hallSensors,
        dutyCyle,
        focAngle;

        public String getName() {
            switch (this) {
                case batteryVoltage:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[0];
                case batteryCurrent:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[1];
                case batteryResistanceEstimated:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[2];
                case batterySOC:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[3];
                case batteryUsedEnergy:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[4];
                case batteryADCCurrent:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[5];
                case motorPower:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[6];
                case motorCurrent:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[7];
                case motorTemperature:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[8];
                case motorSpeed:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[9];
                case humanPower:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[10];
                case pedalCadence:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[11];
                case pedalSide:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[12];
                case pedalWeight:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[13];
                case pedalWeightWithOffset:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[14];
                case torqueSensorADC:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[15];
                case speed:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[16];
                case odometer:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[17];
                case throttle:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[18];
                case throttleADC:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[19];
                case hallSensors:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[20];
                case dutyCyle:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[21];
                case focAngle:
                    return MyApp.getInstance().getResources().getStringArray(R.array.variables)[22];
            }
            return "";
        }

        public static DataType fromInteger(int x) {
            switch(x) {
                case 0:
                    return batteryVoltage;
                case 1:
                    return batteryCurrent;
                case 2:
                    return batteryResistanceEstimated;
                case 3:
                    return batterySOC;
                case 4:
                    return batteryUsedEnergy;
                case 5:
                    return batteryADCCurrent;
                case 6:
                    return motorPower;
                case 7:
                    return motorCurrent;
                case 8:
                    return motorTemperature;
                case 9:
                    return motorSpeed;
                case 10:
                    return humanPower;
                case 11:
                    return pedalCadence;
                case 12:
                    return pedalSide;
                case 13:
                    return pedalWeight;
                case 14:
                    return pedalWeightWithOffset;
                case 15:
                    return torqueSensorADC;
                case 16:
                    return speed;
                case 17:
                    return odometer;
                case 18:
                    return throttle;
                case 19:
                    return throttleADC;
                case 20:
                    return hallSensors;
                case 21:
                    return dutyCyle;
                case 22:
                    return focAngle;
            }
            return null;
        }
    }
}
