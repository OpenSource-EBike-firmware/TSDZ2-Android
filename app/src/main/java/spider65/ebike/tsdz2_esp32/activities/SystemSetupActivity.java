package spider65.ebike.tsdz2_esp32.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import spider65.ebike.tsdz2_esp32.R;
import spider65.ebike.tsdz2_esp32.TSDZBTService;
import spider65.ebike.tsdz2_esp32.data.TSDZ_Config;
import spider65.ebike.tsdz2_esp32.databinding.ActivitySystemSetupBinding;

public class SystemSetupActivity extends AppCompatActivity {

    private static final String TAG = "MotorSetupActivity";
    private TSDZ_Config cfg = new TSDZ_Config();
    private IntentFilter mIntentFilter = new IntentFilter();
    private ActivitySystemSetupBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_system_setup);
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

    //  invalidate all to hide/show the checkbox dependant fields
    public void onCheckedChanged(View view, boolean checked) {
        switch (view.getId()) {
            case R.id.cadenceModeCB:
                binding.cadenceThET.setEnabled(checked);
                break;
            case R.id.assistCB:
                binding.assistWPRET.setEnabled(checked);
                break;
        }
    }

    private void saveCfg() {
        Integer val;
        boolean checked;

        cfg.ui8_motor_type = binding.motorTypeSP.getSelectedItemPosition();

        if ((val = checkRange(binding.accelerationET, 0, 50)) == null) {
            showDialog(getString(R.string.acceleration), getString(R.string.range_error, 0, 50));
            return;
        }
        cfg.ui8_motor_acceleration = val;

        if ((val = checkRange(binding.maxCurrentET, 1, 18)) == null) {
            showDialog(getString(R.string.max_current), getString(R.string.range_error, 1, 18));
            return;
        }
        cfg.ui8_battery_max_current = val;

        if ((val = checkRange(binding.maxPowerET, 50, 1000)) == null) {
            showDialog(getString(R.string.max_power), getString(R.string.range_error, 50, 1000));
            return;
        }
        cfg.ui8_target_max_battery_power_div25 = val;

        cfg.ui8_lights_configuration = binding.lightConfigSP.getSelectedItemPosition();

        checked = binding.cadenceModeCB.isChecked();
        if (checked) {
            if ((val = checkRange(binding.cadenceThET, 100, 900)) == null) {
                showDialog(getString(R.string.adv_cad_mode), getString(R.string.range_error, 100, 900));
                return;
            }
            cfg.ui16_cadence_sensor_pulse_high_percentage_x10 = val;
        }
        cfg.advanced_cadence_sensor_mode = checked;

        checked = binding.assistCB.isChecked();
        if (checked) {
            if ((val = checkRange(binding.assistWPRET, 0, 100)) == null) {
                showDialog(getString(R.string.assit_wpr), getString(R.string.range_error, 0, 100));
                return;
            }
            cfg.ui8_assist_without_pedal_rotation_threshold = val;
        }
        cfg.assist_without_pedal_rotation = checked;

        if ((val = checkRange(binding.torqueADCET, 0, 255)) == null) {
            showDialog(getString(R.string.torque_adc_step), getString(R.string.range_error, 0, 255));
            return;
        }
        cfg.ui8_pedal_torque_per_10_bit_ADC_step_x100 = val;

        if ((val = checkRange(binding.wheelPerimeterET, 1000, 2500)) == null) {
            showDialog(getString(R.string.wheel_perimeter), getString(R.string.range_error, 1000, 2500));
            return;
        }
        cfg.ui16_wheel_perimeter = val;

        if ((val = checkRange(binding.oemWheelDivET, 50, 200)) == null) {
            showDialog(getString(R.string.wheel_divisor), getString(R.string.range_error, 50, 200));
            return;
        }
        cfg.ui8_oem_wheel_divisor = val;

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

    private void showDialog (String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title != null)
            builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive " + intent.getAction());
        if (intent.getAction() == null)
            return;
        switch (intent.getAction()) {
            case TSDZBTService.TSDZ_CFG_READ_BROADCAST:
                cfg.setData(intent.getByteArrayExtra(TSDZBTService.VALUE_EXTRA));
                binding.setCfg(cfg);
                binding.motorTypeSP.setSelection(cfg.ui8_motor_type);
                binding.lightConfigSP.setSelection(cfg.ui8_lights_configuration);
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