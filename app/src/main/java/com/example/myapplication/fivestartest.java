package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
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
    private TextView pitch_total;
    private TextView intensity_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star);

        homebtn = findViewById(R.id.homebtn);
        pitch = findViewById(R.id.pitch);
        intensity = findViewById(R.id.intensity);
        pitch_total = findViewById(R.id.pitch_total);
        intensity_total = findViewById(R.id.intensity_total);

        /*homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fivestartest.this, File.class);
                startActivity(intent);
            }
        });*/

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref_mp = database.getReference("mp");
        DatabaseReference ref_mi = database.getReference("mi");
        DatabaseReference ref_sp = database.getReference("sp");
        DatabaseReference ref_si = database.getReference("si");

        ref_mp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text1 = dataSnapshot.getValue(String.class);
                if (text1 != null) {
                    // TextView에 데이터를 설정합니다.
                    pitch.setText(text1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the data retrieval is canceled
            }
        });

        ref_mi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text2 = dataSnapshot.getValue(String.class);
                if (text2 != null) {
                    // TextView에 데이터를 설정합니다.
                    intensity.setText(text2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the data retrieval is canceled
            }
        });

        ref_sp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double text3 = dataSnapshot.getValue(Double.class);
                if (text3 != null) {
                    // TextView에 데이터를 설정합니다.
                    String formattedValue = String.format("%.1f", text3);
                    pitch_total.setText(formattedValue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the data retrieval is canceled
            }
        });

        ref_si.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double text4 = dataSnapshot.getValue(Double.class);
                if (text4 != null) {
                    // TextView에 데이터를 설정합니다.
                    String formattedValue = String.format("%.1f", text4);
                    intensity_total.setText(formattedValue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the data retrieval is canceled
            }
        });
    }
}



