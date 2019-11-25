package com.weebly.thetechtube.calculatorgo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

    }

    public void openDevelopmentActivity(View view) {

        Intent intent = new Intent(getApplicationContext(), DevelopmentActivity.class);
        startActivity(intent);

    }

}
