package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.text.Html;
import android.text.Spanned;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class analysis extends AppCompatActivity {
    private TextView script2;
    private Button practicebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.script_analysis);

        practicebtn = findViewById(R.id.practicebtn);
        practicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(analysis.this,record.class);
                startActivity(intent);
            }
        });

        script2 = findViewById(R.id.script2);
        TextView text = (TextView)findViewById(R.id.script2);
             text.setMovementMethod(new ScrollingMovementMethod());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("output");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String htmlText = dataSnapshot.getValue(String.class);
                Spanned spannedText = Html.fromHtml(htmlText);
                script2.setText(spannedText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the data retrieval is canceled
            }
        });


        practicebtn = findViewById(R.id.practicebtn);
        practicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(analysis.this,record.class);
                startActivity(intent);
            }
        });
    }

}
