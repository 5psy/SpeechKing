package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class output extends AppCompatActivity {

    private TextView script;
    private Button selectagain;
    private Button analysisbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.output);

        script = findViewById(R.id.script);

        Intent intent = getIntent();
        String fileContent = intent.getStringExtra("fileContent");

        script.setText(fileContent);


        TextView text = (TextView)findViewById(R.id.script);
            text.setMovementMethod(new ScrollingMovementMethod());

        Button selectagain = findViewById(R.id.selectagain);
        selectagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(output.this,File.class);
                startActivity(intent);
            }
        });

        Button analysisbtn = findViewById(R.id.analysisbtn);
        analysisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(output.this,analysis.class);
                startActivity(intent);
            }
        });
    }
}
