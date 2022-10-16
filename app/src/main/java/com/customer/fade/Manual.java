package com.customer.fade;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Manual extends AppCompatActivity {
    TextView manual;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual);
        manual = (TextView)findViewById(R.id.manual);
        manual.setMovementMethod(new ScrollingMovementMethod());

    }

    public void back(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
