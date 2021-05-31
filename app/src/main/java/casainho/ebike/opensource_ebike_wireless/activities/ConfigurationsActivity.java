package casainho.ebike.opensource_ebike_wireless.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import casainho.ebike.opensource_ebike_wireless.R;
import casainho.ebike.opensource_ebike_wireless.TSDZBTService;
import casainho.ebike.opensource_ebike_wireless.data.TSDZ_Configurations;
import casainho.ebike.opensource_ebike_wireless.databinding.ActivityConfigurationsBinding;

public class ConfigurationsActivity extends AppCompatActivity {

    private static final String TAG = "ConfigurationsActivity";
    private final TSDZ_Configurations cfg = new TSDZ_Configurations();
    private final IntentFilter mIntentFilter = new IntentFilter();
    private ActivityConfigurationsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_configurations);
        binding.setHandler(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mIntentFilter.addAction(TSDZBTService.TSDZ_CFG_READ_BROADCAST);
        mIntentFilter.addAction(TSDZBTService.TSDZ_CFG_WRITE_BROADCAST);
        TSDZBTService service = TSDZBTService.getBluetoothService();
        if (service != null && service.getConnectionStatus() == TSDZBTService.ConnectionState.CONNECTED)
            service.readCfg();
        else {
            showDialog(getString(R.string.error), getString(R.string.connection_error));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    public void onOkCancelClick(View view) {
        switch (view.getId()) {
            case R.id.okButton:
                saveCfg();
                break;
            case R.id.cancelButton:
                finish();
                break;
        }
    }
    
    private void saveCfg() {
        Integer val;
        Float valFloat;

        if ((val = checkRange(binding.wheelPerimeterET, 750, 3000)) == null) {
            showDialog(getString(R.string.wheel_perimeter), getString(R.string.range_error, 750, 3000));
            return;
        }
        cfg.ui16_wheel_perimeter = val;

        if ((val = checkRange(binding.maxSpeedET, 1, 99)) == null) {
            showDialog(getString(R.string.wheel_speed), getString(R.string.range_error, 0, 99));
            return;
        }
        cfg.ui8_wheel_max_speed = val;

        if ((val = checkRange(binding.maxCurrentET, 1, 20)) == null) {
            showDialog(getString(R.string.max_current), getString(R.string.range_error, 1, 20));
            return;
        }
        cfg.ui8_battery_max_current = val;

        if ((valFloat = checkRange(binding.LVCVoltageET, 16.0f, 63.0f)) == null) {
            showDialog(getString(R.string.lvc_voltage), getString(R.string.range_error_float, 16.0f, 63.0f));
            return;
        }
        cfg.ui16_battery_low_voltage_cut_off_x10 = (int) (valFloat * 10);

        if ((val = checkRange(binding.resistanceET, 0, 1000)) == null) {
            showDialog(getString(R.string.resistance), getString(R.string.range_error, 0, 1000));
            return;
        }
        cfg.ui16_battery_pack_resistance_x1000 = val;

        if ((valFloat = checkRange(binding.totalWhET, 0.0f, 999.0f)) == null) {
            showDialog(getString(R.string.total_wh), getString(R.string.range_error_float, 0.0f, 999.0f));
            return;
        }
        cfg.f_wh_100_percent = (long) (valFloat * 10);

        if ((valFloat = checkRange(binding.resetVoltageET, 16.0f, 63.0f)) == null) {
            showDialog(getString(R.string.reset_voltage), getString(R.string.range_error_float, 16.0f, 63.0f));
            return;
        }
        cfg.ui16_battery_voltage_reset_wh_counter_x10 = (int) (valFloat * 10);

        if ((valFloat = checkRange(binding.batterySocUsedWhET, 0.0f, 9990.0f)) == null) {
            showDialog(getString(R.string.used_wh), getString(R.string.range_error_float, 0.0f, 9990.0f));
            return;
        }
        cfg.ui32_wh_x10 = (long) (valFloat * 10);

        cfg.ui8_motor_type = binding.motorVoltageSpinner.getSelectedItemPosition();

        if ((val = checkRange(binding.motorMaxCurrentET, 1, 30)) == null) {
            showDialog(getString(R.string.motor_max_current), getString(R.string.range_error, 1, 30));
            return;
        }
        cfg.ui8_motor_max_current = val;

        if ((valFloat = checkRange(binding.motorCurrentRampET, 0.4f, 10.0f)) == null) {
            showDialog(getString(R.string.current_ramp), getString(R.string.range_error_float, 0.4f, 10.0f));
            return;
        }
        cfg.ui8_ramp_up_amps_per_second_x10 = (int) (valFloat * 10);

        cfg.ui8_motor_current_control_mode = binding.motorCurrentControlSpinner.getSelectedItemPosition();

        if ((val = checkRange(binding.motorMinCurrentADCStepsET, 0, 13)) == null) {
            showDialog(getString(R.string.motor_min_current_adc_steps), getString(R.string.range_error, 1, 13));
            return;
        }
        cfg.ui8_motor_current_min_adc = val;

        cfg.ui8_field_weakening = binding.motorFieldWeakeningSpinner.getSelectedItemPosition();

        cfg.ui8_temperature_limit_feature_enabled = binding.motorTemperatureFeatureSpinner.getSelectedItemPosition();

        if ((val = checkRange(binding.motorTemperatureMaxLimitET, 0, 255)) == null) {
            showDialog(getString(R.string.motor_temperature_max_limit), getString(R.string.range_error, 1, 255));
            return;
        }
        cfg.ui8_motor_temperature_max_value_to_limit = val;

        if ((val = checkRange(binding.motorTemperatureMinLimitET, 0, 255)) == null) {
            showDialog(getString(R.string.motor_temperature_min_limit), getString(R.string.range_error, 1, 255));
            return;
        }
        cfg.ui8_motor_temperature_min_value_to_limit = val;

        cfg.ui8_pedal_cadence_fast_stop = binding.cadenceFastStopSpinner.getSelectedItemPosition();

        if ((val = checkRange(binding.torqueSensorADCThresholdET, 5, 100)) == null) {
            showDialog(getString(R.string.torque_sensor_adc_threshold), getString(R.string.range_error, 5, 100));
            return;
        }
        cfg.ui8_torque_sensor_adc_threshold = val;

        cfg.ui8_motor_assistance_startup_without_pedal_rotation = binding.torqueSensorAssistWithoutPedalRotationSpinner.getSelectedItemPosition();

        cfg.ui8_coast_brake_enable = binding.torqueSensorCoastBrakeSpinner.getSelectedItemPosition();

        if ((val = checkRange(binding.torqueSensorCoastBrakeADCET, 5, 255)) == null) {
            showDialog(getString(R.string.torque_sensor_coast_brake_adc), getString(R.string.range_error, 5, 255));
            return;
        }
        cfg.ui8_coast_brake_adc = val;

        cfg.ui8_torque_sensor_calibration_feature_enabled = binding.torqueSensorCalibrationSpinner.getSelectedItemPosition();

        cfg.ui8_torque_sensor_calibration_pedal_ground = binding.torqueSensorStartPedalGroundSpinner.getSelectedItemPosition();

        if ((val = checkRange(binding.torqueSensorFilterET, 0, 100)) == null) {
            showDialog(getString(R.string.torque_sensor_filter), getString(R.string.range_error, 0, 100));
            return;
        }
        cfg.ui8_torque_sensor_filter = val;

        if ((val = checkRange(binding.torqueSensorWeight1LeftET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_1), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[0][0] = val;

        if ((val = checkRange(binding.torqueSensorADC1LeftET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_1), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[0][1] = val;

        if ((val = checkRange(binding.torqueSensorWeight2LeftET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_2), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[1][0] = val;

        if ((val = checkRange(binding.torqueSensorADC2LeftET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_2), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[1][1] = val;

        if ((val = checkRange(binding.torqueSensorWeight3LeftET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_3), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[2][0] = val;

        if ((val = checkRange(binding.torqueSensorADC3LeftET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_3), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[2][1] = val;

        if ((val = checkRange(binding.torqueSensorWeight4LeftET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_4), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[3][0] = val;

        if ((val = checkRange(binding.torqueSensorADC4LeftET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_4), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[3][1] = val;

        if ((val = checkRange(binding.torqueSensorWeight5LeftET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_5), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[4][0] = val;

        if ((val = checkRange(binding.torqueSensorADC5LeftET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_5), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[4][1] = val;


        if ((val = checkRange(binding.torqueSensorWeight6LeftET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_6), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[5][0] = val;

        if ((val = checkRange(binding.torqueSensorADC6LeftET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_6), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[5][1] = val;

        if ((val = checkRange(binding.torqueSensorWeight7LeftET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_7), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[6][0] = val;

        if ((val = checkRange(binding.torqueSensorADC7LeftET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_7), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[6][1] = val;
        
        if ((val = checkRange(binding.torqueSensorWeight8LeftET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_8), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[7][0] = val;

        if ((val = checkRange(binding.torqueSensorADC8LeftET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_8), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_left[7][1] = val;
        
        if ((val = checkRange(binding.torqueSensorWeight1RightET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_1), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[0][0] = val;

        if ((val = checkRange(binding.torqueSensorADC1RightET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_1), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[0][1] = val;

        if ((val = checkRange(binding.torqueSensorWeight2RightET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_2), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[1][0] = val;

        if ((val = checkRange(binding.torqueSensorADC2RightET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_2), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[1][1] = val;

        if ((val = checkRange(binding.torqueSensorWeight3RightET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_3), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[2][0] = val;

        if ((val = checkRange(binding.torqueSensorADC3RightET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_3), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[2][1] = val;

        if ((val = checkRange(binding.torqueSensorWeight4RightET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_4), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[3][0] = val;

        if ((val = checkRange(binding.torqueSensorADC4RightET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_4), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[3][1] = val;

        if ((val = checkRange(binding.torqueSensorWeight5RightET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_5), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[4][0] = val;

        if ((val = checkRange(binding.torqueSensorADC5RightET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_5), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[4][1] = val;


        if ((val = checkRange(binding.torqueSensorWeight6RightET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_6), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[5][0] = val;

        if ((val = checkRange(binding.torqueSensorADC6RightET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_6), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[5][1] = val;

        if ((val = checkRange(binding.torqueSensorWeight7RightET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_7), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[6][0] = val;

        if ((val = checkRange(binding.torqueSensorADC7RightET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_7), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[6][1] = val;

        if ((val = checkRange(binding.torqueSensorWeight8RightET, 0, 200)) == null) {
            showDialog(getString(R.string.torque_sensor_weight_8), getString(R.string.range_error, 0, 200));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[7][0] = val;

        if ((val = checkRange(binding.torqueSensorADC8RightET, 0, 1023)) == null) {
            showDialog(getString(R.string.torque_sensor_ADC_8), getString(R.string.range_error, 0, 1023));
            return;
        }
        cfg.ui16_torque_sensor_calibration_table_right[7][1] = val;

        if ((valFloat = checkRange(binding.assistLevel1ET, 0.001f, 65.530f)) == null) {
            showDialog(getString(R.string.level_1), getString(R.string.range_error_float, 0.001f, 65.530f));
            return;
        }
        cfg.ui16_assist_level_factor[0] = (int) (valFloat * 1000);

        if ((valFloat = checkRange(binding.assistLevel2ET, 0.001f, 65.530f)) == null) {
            showDialog(getString(R.string.level_2), getString(R.string.range_error_float, 0.001f, 65.530f));
            return;
        }
        cfg.ui16_assist_level_factor[1] = (int) (valFloat * 1000);

        if ((valFloat = checkRange(binding.assistLevel3ET, 0.001f, 65.530f)) == null) {
            showDialog(getString(R.string.level_3), getString(R.string.range_error_float, 0.001f, 65.530f));
            return;
        }
        cfg.ui16_assist_level_factor[2] = (int) (valFloat * 1000);

        if ((valFloat = checkRange(binding.assistLevel4ET, 0.001f, 65.530f)) == null) {
            showDialog(getString(R.string.level_4), getString(R.string.range_error_float, 0.001f, 65.530f));
            return;
        }
        cfg.ui16_assist_level_factor[3] = (int) (valFloat * 1000);

        if ((valFloat = checkRange(binding.assistLevel5ET, 0.001f, 65.530f)) == null) {
            showDialog(getString(R.string.level_5), getString(R.string.range_error_float, 0.001f, 65.530f));
            return;
        }
        cfg.ui16_assist_level_factor[4] = (int) (valFloat * 1000);

        if ((valFloat = checkRange(binding.assistLevel6ET, 0.001f, 65.530f)) == null) {
            showDialog(getString(R.string.level_6), getString(R.string.range_error_float, 0.001f, 65.530f));
            return;
        }
        cfg.ui16_assist_level_factor[5] = (int) (valFloat * 1000);

        if ((valFloat = checkRange(binding.assistLevel7ET, 0.001f, 65.530f)) == null) {
            showDialog(getString(R.string.level_7), getString(R.string.range_error_float, 0.001f, 65.530f));
            return;
        }
        cfg.ui16_assist_level_factor[6] = (int) (valFloat * 1000);

        cfg.ui8_walk_assist_feature_enabled = binding.walkAssistFeatureSpinner.getSelectedItemPosition();

        if ((val = checkRange(binding.walkAssistLevel1ET, 0, 100)) == null) {
            showDialog(getString(R.string.walk_assist_level_1), getString(R.string.range_error, 0, 100));
            return;
        }
        cfg.ui8_walk_assist_level_factor[0] = val;

        if ((val = checkRange(binding.walkAssistLevel2ET, 0, 100)) == null) {
            showDialog(getString(R.string.walk_assist_level_2), getString(R.string.range_error, 0, 100));
            return;
        }
        cfg.ui8_walk_assist_level_factor[1] = val;

        if ((val = checkRange(binding.walkAssistLevel3ET, 0, 100)) == null) {
            showDialog(getString(R.string.walk_assist_level_3), getString(R.string.range_error, 0, 100));
            return;
        }
        cfg.ui8_walk_assist_level_factor[2] = val;

        if ((val = checkRange(binding.walkAssistLevel4ET, 0, 100)) == null) {
            showDialog(getString(R.string.walk_assist_level_4), getString(R.string.range_error, 0, 100));
            return;
        }
        cfg.ui8_walk_assist_level_factor[3] = val;

        if ((val = checkRange(binding.walkAssistLevel5ET, 0, 100)) == null) {
            showDialog(getString(R.string.walk_assist_level_5), getString(R.string.range_error, 0, 100));
            return;
        }
        cfg.ui8_walk_assist_level_factor[4] = val;

        if ((val = checkRange(binding.walkAssistLevel6ET, 0, 100)) == null) {
            showDialog(getString(R.string.walk_assist_level_6), getString(R.string.range_error, 0, 100));
            return;
        }
        cfg.ui8_walk_assist_level_factor[5] = val;

        if ((val = checkRange(binding.walkAssistLevel7ET, 0, 100)) == null) {
            showDialog(getString(R.string.walk_assist_level_7), getString(R.string.range_error, 0, 100));
            return;
        }
        cfg.ui8_walk_assist_level_factor[6] = val;

        cfg.ui8_startup_motor_power_boost_feature_enabled = binding.startupBoostFeatureSpinner.getSelectedItemPosition();

        cfg.ui8_startup_motor_power_boost_always = binding.startupBoostActiveOnSpinner.getSelectedItemPosition();

        cfg.ui8_startup_motor_power_boost_limit_power = binding.startupBoostLimitToMaxPowerSpinner.getSelectedItemPosition();

        if ((valFloat = checkRange(binding.startupBoostDurationET, 0.1f, 25.5f)) == null) {
            showDialog(getString(R.string.startup_boost_duration), getString(R.string.range_error_float, 0.1f, 25.5f));
            return;
        }
        cfg.ui8_startup_motor_power_boost_time = (int) (valFloat * 10);

        if ((valFloat = checkRange(binding.startupBoostFadeET, 0.1f, 25.5f)) == null) {
            showDialog(getString(R.string.startup_boost_fade), getString(R.string.range_error_float, 0.1f, 25.5f));
            return;
        }
        cfg.ui8_startup_motor_power_boost_fade_time = (int) (valFloat * 10);

        if ((valFloat = checkRange(binding.startupBoostAssistLevel1ET, 0.001f, 65.530f)) == null) {
            showDialog(getString(R.string.level_1), getString(R.string.range_error_float, 0.001f, 65.530f));
            return;
        }
        cfg.ui16_startup_motor_power_boost_factor[0] = (int) (valFloat * 1000);

        if ((valFloat = checkRange(binding.startupBoostAssistLevel2ET, 0.001f, 65.530f)) == null) {
            showDialog(getString(R.string.level_2), getString(R.string.range_error_float, 0.001f, 65.530f));
            return;
        }
        cfg.ui16_startup_motor_power_boost_factor[1] = (int) (valFloat * 1000);

        if ((valFloat = checkRange(binding.startupBoostAssistLevel3ET, 0.001f, 65.530f)) == null) {
            showDialog(getString(R.string.level_3), getString(R.string.range_error_float, 0.001f, 65.530f));
            return;
        }
        cfg.ui16_startup_motor_power_boost_factor[2] = (int) (valFloat * 1000);

        if ((valFloat = checkRange(binding.startupBoostAssistLevel4ET, 0.001f, 65.530f)) == null) {
            showDialog(getString(R.string.level_4), getString(R.string.range_error_float, 0.001f, 65.530f));
            return;
        }
        cfg.ui16_startup_motor_power_boost_factor[3] = (int) (valFloat * 1000);

        if ((valFloat = checkRange(binding.startupBoostAssistLevel5ET, 0.001f, 65.530f)) == null) {
            showDialog(getString(R.string.level_5), getString(R.string.range_error_float, 0.001f, 65.530f));
            return;
        }
        cfg.ui16_startup_motor_power_boost_factor[4] = (int) (valFloat * 1000);

        if ((valFloat = checkRange(binding.startupBoostAssistLevel6ET, 0.001f, 65.530f)) == null) {
            showDialog(getString(R.string.level_6), getString(R.string.range_error_float, 0.001f, 65.530f));
            return;
        }
        cfg.ui16_startup_motor_power_boost_factor[5] = (int) (valFloat * 1000);

        if ((valFloat = checkRange(binding.startupBoostAssistLevel7ET, 0.001f, 65.530f)) == null) {
            showDialog(getString(R.string.level_7), getString(R.string.range_error_float, 0.001f, 65.530f));
            return;
        }
        cfg.ui16_startup_motor_power_boost_factor[6] = (int) (valFloat * 1000);

cfg.ui8_street_mode_enabled = 0; // TODO: force disable, as currently there is no way to enable or disable it
//        cfg.ui8_street_mode_enabled = binding.streetModeFeatureSpinner.getSelectedItemPosition();
//
//        cfg.ui8_street_mode_enabled_on_startup = binding.streetModeEnableAtStartupSpinner.getSelectedItemPosition();
//
//        if ((val = checkRange(binding.streetModeSpeedLimitET, 1, 99)) == null) {
//            showDialog(getString(R.string.street_mode_speed_limit), getString(R.string.range_error, 1, 99));
//            return;
//        }
//        cfg.ui8_street_mode_speed_limit = val;
//
//        if ((val = checkRange(binding.streetModeMotorPowerLimitET, 25, 2500)) == null) {
//            showDialog(getString(R.string.street_mode_motor_power_limit), getString(R.string.range_error, 25, 2500));
//            return;
//        }
//        cfg.ui8_street_mode_power_limit_div25 = (val / 25);
//
//        cfg.ui8_street_mode_throttle_enabled = binding.streetModeThrottleEnableSpinner.getSelectedItemPosition();

        if ((valFloat = checkRange(binding.odometerET, 0.0f, 429496729.0f)) == null) {
            showDialog(getString(R.string.odometer), getString(R.string.range_error_float, 0.0f, 429496729.0f));
            return;
        }
        cfg.ui32_odometer_x10 = (int) (valFloat * 10);

        TSDZBTService service = TSDZBTService.getBluetoothService();
        if (service != null && service.getConnectionStatus() == TSDZBTService.ConnectionState.CONNECTED)
            service.writeCfg(cfg);
        else {
            showDialog(getString(R.string.error), getString(R.string.connection_error));
        }
    }

    Integer checkRange(EditText et, int min, int max) {
        int val = Integer.parseInt(et.getText().toString());
        if (val < min || val > max) {
            et.setError(getString(R.string.range_error, min, max));
            return null;
        }
        return val;
    }

    Float checkRange(EditText et, float min, float max) {
        float val = Float.parseFloat(et.getText().toString());
        if (val < min || val > max) {
            et.setError(getString(R.string.range_error_float, min, max));
            return null;
        }
        return val;
    }

    private void showDialog (String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title != null)
            builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive " + intent.getAction());
        if (intent.getAction() == null)
            return;
        switch (intent.getAction()) {
            case TSDZBTService.TSDZ_CFG_READ_BROADCAST:
                if (cfg.setData(intent.getByteArrayExtra(TSDZBTService.VALUE_EXTRA))) {
                    binding.setCfg(cfg);

                    Resources res = getResources();
                    int itens;

                    itens = cfg.ui8_motor_type;
                    if (itens <= (res.getStringArray(R.array.motor_voltage)).length)
                        binding.motorVoltageSpinner.setSelection(itens);

                    itens = cfg.ui8_motor_current_control_mode;
                    if (itens <= (res.getStringArray(R.array.motor_current_control)).length)
                        binding.motorCurrentControlSpinner.setSelection(itens);

                    itens = cfg.ui8_temperature_limit_feature_enabled;
                    if (itens <= (res.getStringArray(R.array.motor_temperature_feature)).length)
                        binding.motorTemperatureFeatureSpinner.setSelection(itens);

                    itens = cfg.ui8_pedal_cadence_fast_stop;
                    if (itens <= (res.getStringArray(R.array.no_yes)).length)
                        binding.cadenceFastStopSpinner.setSelection(itens);

                    itens = cfg.ui8_field_weakening;
                    if (itens <= (res.getStringArray(R.array.enable_disable)).length)
                        binding.motorFieldWeakeningSpinner.setSelection(itens);

                    itens = cfg.ui8_motor_assistance_startup_without_pedal_rotation;
                    if (itens <= (res.getStringArray(R.array.enable_disable)).length)
                        binding.torqueSensorAssistWithoutPedalRotationSpinner.setSelection(itens);

                    itens = cfg.ui8_coast_brake_enable;
                    if (itens <= (res.getStringArray(R.array.enable_disable)).length)
                        binding.torqueSensorCoastBrakeSpinner.setSelection(itens);

                    itens = cfg.ui8_torque_sensor_calibration_feature_enabled;
                    if (itens <= (res.getStringArray(R.array.enable_disable)).length)
                        binding.torqueSensorCalibrationSpinner.setSelection(itens);

                    itens = cfg.ui8_torque_sensor_calibration_pedal_ground;
                    if (itens <= (res.getStringArray(R.array.left_right)).length)
                        binding.torqueSensorStartPedalGroundSpinner.setSelection(itens);

                    itens = cfg.ui8_walk_assist_feature_enabled;
                    if (itens <= (res.getStringArray(R.array.disable_enable)).length)
                        binding.walkAssistFeatureSpinner.setSelection(itens);

                    itens = cfg.ui8_startup_motor_power_boost_feature_enabled;
                    if (itens <= (res.getStringArray(R.array.disable_enable)).length)
                        binding.startupBoostFeatureSpinner.setSelection(itens);

                    itens = cfg.ui8_startup_motor_power_boost_always;
                    if (itens <= (res.getStringArray(R.array.active_on)).length)
                        binding.startupBoostActiveOnSpinner.setSelection(itens);

                    itens = cfg.ui8_startup_motor_power_boost_limit_power;
                    if (itens <= (res.getStringArray(R.array.no_yes)).length)
                        binding.startupBoostLimitToMaxPowerSpinner.setSelection(itens);

//                    itens = cfg.ui8_street_mode_enabled;
//                    if (itens <= (res.getStringArray(R.array.disable_enable)).length)
//                        binding.streetModeFeatureSpinner.setSelection(itens);
//
//                    itens = cfg.ui8_street_mode_enabled_on_startup;
//                    if (itens <= (res.getStringArray(R.array.no_yes)).length)
//                        binding.streetModeEnableAtStartupSpinner.setSelection(itens);
//
//                    itens = cfg.ui8_street_mode_throttle_enabled;
//                    if (itens <= (res.getStringArray(R.array.no_yes)).length)
//                        binding.streetModeThrottleEnableSpinner.setSelection(itens);
                }
                break;

            case TSDZBTService.TSDZ_CFG_WRITE_BROADCAST:
                if (intent.getBooleanExtra(TSDZBTService.VALUE_EXTRA,false))
                    finish();
                else
                    showDialog(getString(R.string.error), getString(R.string.write_cfg_error));
                break;
         }
        }
    };
}
