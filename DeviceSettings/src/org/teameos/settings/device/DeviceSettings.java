package org.teameos.settings.device;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Boolean;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.os.SystemProperties;

public class DeviceSettings extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "DeviceSettings";
    private static final String CHARGING_LIGHT = "/sys/devices/virtual/gpio/gpio168/value";

    private CheckBoxPreference mEnableChargingLight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        mEnableChargingLight = (CheckBoxPreference) findPreference("charge_light");
        mEnableChargingLight.setOnPreferenceChangeListener(this);
        File dataDirectory = getDir("eos", Context.MODE_PRIVATE);
        File chargingLightFile = new File (dataDirectory.getAbsolutePath() + File.separator + "charging_light");
        mEnableChargingLight.setChecked(chargingLightFile.exists()); 
    }

    public boolean onPreferenceChange(Preference preference, Object value) {

if (preference.equals(mEnableChargingLight)){
            Boolean valueB = (Boolean) value;
            File dataDirectory = getDir("eos", Context.MODE_PRIVATE);
            File chargingLightFile = new File (dataDirectory.getAbsolutePath() + File.separator + "charging_light");

            try {
                if (valueB.booleanValue()){
                    chargingLightFile.createNewFile();
                }else {
                    chargingLightFile.delete();
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(CHARGING_LIGHT));
                String output = "" + (valueB ? 0 : 1);
                writer.write(output.toCharArray(), 0, output.toCharArray().length);
                writer.close();
            } catch (IOException e) {
                return false;
            }
            return true;
        }
        return false;
    }
}
