package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class socket_test2 extends AppCompatActivity {

    private TextView script;
    private Button selectagain;
    private Button analysisbtn;

    private static final String SERVER_URL = "http://127.0.0.1:12345/";

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
                Intent intent = new Intent(socket_test2.this, File.class);
                startActivity(intent);
            }
        });

        Button analysisbtn = findViewById(R.id.analysisbtn);
        analysisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send the file content to the Flask server
                sendToServer(fileContent);

                Intent intent = new Intent(socket_test2.this, analysis.class);
                startActivity(intent);
            }
        });
    }

    private void sendToServer(String fileContent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("fileContent", fileContent)
                            .build();

                    Request request = new Request.Builder()
                            .url(SERVER_URL)
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

