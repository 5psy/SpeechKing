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
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class analysis extends AppCompatActivity {
    private TextView script2;
    private TextView textView3;
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

        textView3 = findViewById(R.id.textView3);

        String fullText = "    빨간색 단어는 강조단어입니다.  \n   조금 더 높게 세게 읽으세요. ";
        String subText = "빨간색";

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(fullText);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.RED);
        int startIndex = fullText.indexOf(subText);
        int endIndex = startIndex + subText.length();
        spannableStringBuilder.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView3.setText(spannableStringBuilder);
    }

    private long pressedTime = 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() > pressedTime + 2000 ){
            pressedTime = System.currentTimeMillis();
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }
        else if (System.currentTimeMillis() <= pressedTime + 2000){
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }

}
