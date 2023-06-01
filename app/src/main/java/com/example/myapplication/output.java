package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class output extends AppCompatActivity {

    private TextView script1;
    private Button selectagain;
    private Button analysisbtn;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("text");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.output);

        script1 = findViewById(R.id.script1);

        Intent intent = getIntent();
        String fileContent = intent.getStringExtra("fileContent");

        script1.setText(fileContent);


        TextView text = (TextView) findViewById(R.id.script1);
        text.setMovementMethod(new ScrollingMovementMethod());

        selectagain = findViewById(R.id.selectagain);
        selectagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(output.this, File.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        analysisbtn = findViewById(R.id.analysisbtn);

        conditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //String text = dataSnapshot.getValue(String.class);
                //textView.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        analysisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conditionRef.setValue(script1.getText().toString());
                Intent intent = new Intent(output.this, analysis.class);
                startActivity(intent);
            }
        });
    }
}
