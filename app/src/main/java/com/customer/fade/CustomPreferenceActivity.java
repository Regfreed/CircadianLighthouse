package com.customer.fade;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class CustomPreferenceActivity extends PreferenceActivity {
    Button reset;
    Button back;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setContentView(R.layout.pref_reset_button);

        reset = (Button)findViewById(R.id.reset);
        back = (Button)findViewById(R.id.btn_back);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences,true);

                Intent intent = new Intent(getApplicationContext(), CustomPreferenceActivity.class);
                startActivity(intent);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
