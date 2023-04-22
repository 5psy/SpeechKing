package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class output extends AppCompatActivity {

    private TextView script;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.script_output);

        script = findViewById(R.id.script);

        Intent intent = getIntent();
        String fileContent = intent.getStringExtra("fileContent");

        script.setText(fileContent);
    }
}
