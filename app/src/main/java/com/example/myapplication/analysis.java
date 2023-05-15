package com.example.myapplication;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
    private TextView textview2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.script_analysis);

        textview2 = findViewById(R.id.textview2);

        TextView text = (TextView)findViewById(R.id.textview2);
             text.setMovementMethod(new ScrollingMovementMethod());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("output");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String htmlText = dataSnapshot.getValue(String.class);
                Spanned spannedText = Html.fromHtml(htmlText);
                textview2.setText(spannedText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the data retrieval is canceled
            }
        });
    }
}
