package com.fyp.privacyguard.core;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import com.fyp.privacyguard.AppConst;
import com.fyp.privacyguard.BuildConfig;
import com.fyp.privacyguard.R;
import com.fyp.privacyguard.util.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    Switch mSwitchSystem;
    Switch mSwitchUninstall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.settings);
        }

        // hide system
        mSwitchSystem = findViewById(R.id.switch_system_apps);
        mSwitchSystem.setOnCheckedChangeListener((compoundButton, b) -> {
            if (PreferenceManager.getInstance().getSystemSettings(PreferenceManager.PREF_SETTINGS_HIDE_SYSTEM_APPS) != b) {
                PreferenceManager.getInstance().putBoolean(PreferenceManager.PREF_SETTINGS_HIDE_SYSTEM_APPS, b);
                setResult(1);
            }
        });

        findViewById(R.id.group_system).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwitchSystem.performClick();
            }
        });

        // hide uninstall
        mSwitchUninstall = findViewById(R.id.switch_uninstall_appps);
        mSwitchUninstall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (PreferenceManager.getInstance().getUninstallSettings(PreferenceManager.PREF_SETTINGS_HIDE_UNINSTALL_APPS) != b) {
                    PreferenceManager.getInstance().putBoolean(PreferenceManager.PREF_SETTINGS_HIDE_UNINSTALL_APPS, b);
                    setResult(1);
                }
            }
        });

        findViewById(R.id.group_uninstall).setOnClickListener(view -> mSwitchUninstall.performClick());

        // ignore list
        findViewById(R.id.group_ignore).setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, IgnoreActivity.class)));

        // about
        findViewById(R.id.group_about).setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, AboutActivity.class)));

        // share
        findViewById(R.id.group_share).setOnClickListener(view -> {
            String shareText = getResources().getString(R.string.share_desc);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    String.format(Locale.getDefault(), shareText, AppConst.GP_DETAIL_PREFIX, BuildConfig.APPLICATION_ID));
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });

        restoreStatus();
    }

    private void restoreStatus() {
        mSwitchSystem.setChecked(PreferenceManager.getInstance().getSystemSettings(PreferenceManager.PREF_SETTINGS_HIDE_SYSTEM_APPS));
        mSwitchUninstall.setChecked(PreferenceManager.getInstance().getUninstallSettings(PreferenceManager.PREF_SETTINGS_HIDE_UNINSTALL_APPS));
    }
}
