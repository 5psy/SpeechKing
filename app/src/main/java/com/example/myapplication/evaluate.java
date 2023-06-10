package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class evaluate extends AppCompatActivity {

    private Button homebtn;
    private TextView pitch;
    private TextView intensity;
    private TextView pitch_total;
    private TextView intensity_total;
    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eva_star);

        homebtn = findViewById(R.id.homebtn);
        pitch = findViewById(R.id.pitch);
        intensity = findViewById(R.id.intensity);
        pitch_total = findViewById(R.id.pitch_total);
        intensity_total = findViewById(R.id.intensity_total);
        imageView2 = findViewById(R.id.imageView2);


        final boolean[] isButtonPressed = {false};

        homebtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(evaluate.this, start.class);
                startActivity(intent);
            }
        }));

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
                    // TextView에 데이터를 설정
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
                    // TextView에 데이터를 설정
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
                    // TextView에 데이터를 설정
                    String formattedValue = String.format("%.1f", text3);
                    pitch_total.setText(formattedValue);

                    // Firebase에서 값이 변경되었을 때 별점 계산 및 출력
                    calculateStarRating();
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
                    // TextView에 데이터를 설정
                    String formattedValue = String.format("%.1f", text4);
                    intensity_total.setText(formattedValue);

                    // Firebase에서 값이 변경되었을 때 별점 계산 및 출력
                    calculateStarRating();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the data retrieval is canceled
            }
        });
    }

    private void calculateStarRating() {
        try {
            double number1 = Double.parseDouble(pitch_total.getText().toString());
            double number2 = Double.parseDouble(intensity_total.getText().toString());
            double sum = number1 + number2;
            double average = sum / 2;

            int starRating;
            if (average >= 5.0) {
                starRating = 9;
            } else if (average >= 4.1 && average <= 4.9) {
                starRating = 8;
            } else if (average >= 4.0) {
                starRating = 7;
            } else if (average >= 3.1 && average <= 3.9) {
                starRating = 6;
            } else if (average >= 3.0) {
                starRating = 5;
            } else if (average >= 2.1 && average <= 2.9) {
                starRating = 4;
            } else if (average >= 2.0) {
                starRating = 3;
            } else if (average >= 1.1 && average <= 1.9) {
                starRating = 2;
            } else if (average >= 1.0) {
                starRating = 1;
            } else {
                starRating = 0;
            }

            setStarRating(starRating);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStarRating(int starRating){
        switch (starRating){
            case 0:
                imageView2.setImageResource(R.drawable.star0_5);
                break;
            case 1:
                imageView2.setImageResource(R.drawable.star1);
                break;
            case 2:
                imageView2.setImageResource(R.drawable.star1_5);
                break;
            case 3:
                imageView2.setImageResource(R.drawable.star2);
                break;
            case 4:
                imageView2.setImageResource(R.drawable.star2_5);
                break;
            case 5:
                imageView2.setImageResource(R.drawable.star3);
                break;
            case 6:
                imageView2.setImageResource(R.drawable.star3_5);
                break;
            case 7:
                imageView2.setImageResource(R.drawable.star4);
                break;
            case 8:
                imageView2.setImageResource(R.drawable.star4_5);
                break;
            case 9:
                imageView2.setImageResource(R.drawable.star5);
                break;
        }
    }
}



