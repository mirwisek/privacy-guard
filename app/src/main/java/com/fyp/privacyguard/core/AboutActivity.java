package com.fyp.privacyguard.core;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import com.fyp.privacyguard.BuildConfig;
import com.fyp.privacyguard.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.about);
        }

        ((TextView) findViewById(R.id.version)).setText(
                String.format(Locale.getDefault(),
                        getResources().getString(R.string.version), BuildConfig.VERSION_NAME));
    }
}
