package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


/*
public class fivestartest extends AppCompatActivity {

    private Button homebtn;
    //private TextView pitch;
    //pitch = findViewById(R.id.pitch);
    private TextView pitch = (TextView)findViewById(R.id.pitch);
    private TextView intensity = (TextView)findViewById(R.id.intensity);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star);

        homebtn = findViewById(R.id.homebtn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fivestartest.this, File.class);
                startActivity(intent);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref_mp = database.getReference("mp");
        DatabaseReference ref_mi = database.getReference("mi");

        ref_mp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Text = dataSnapshot.getValue(String.class);
                //Spanned spannedText = Html.fromHtml(htmlText);
                pitch.setText(Text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the data retrieval is canceled
            }

        });
        ref_mi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Text = dataSnapshot.getValue(String.class);
                //Spanned spannedText = Html.fromHtml(htmlText);
                intensity.setText(Text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the data retrieval is canceled
            }

        });

    }
}*/
public class fivestartest extends AppCompatActivity {

    private Button homebtn;
    private TextView pitch;
    private TextView intensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star);

        homebtn = findViewById(R.id.homebtn);
        pitch = findViewById(R.id.pitch);
        intensity = findViewById(R.id.intensity);

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fivestartest.this, File.class);
                startActivity(intent);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref_mp = database.getReference("mp");
        DatabaseReference ref_mi = database.getReference("mi");

        ref_mp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                pitch.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the data retrieval is canceled
            }
        });

        ref_mi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                intensity.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the data retrieval is canceled
            }
        });
    }
}



