package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class analysis extends AppCompatActivity {

    private Button backbtn;
    private Button practicebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.script_analysis);

        Button backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(analysis.this,output.class);
                startActivity(intent);
            }
        });

        Button practicebtn = findViewById(R.id.practicebtn);
        practicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(analysis.this,record.class);
                startActivity(intent);
            }
        });
    }
}
