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

public class socket_test extends AppCompatActivity {

    private TextView textView;

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        //textView = findViewById(R.id.FileName);
    }

    public void selectFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                inputStream.close();

                String fileContent = stringBuilder.toString();

                // Send the file content to the Python server
                sendToServer(fileContent);

                Intent intent = new Intent(this, output.class);
                intent.putExtra("fileContent", fileContent);
                startActivity(intent);

                // Update the text of the TextView with the file content
                //textView.setText(fileContent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendToServer(String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(message.getBytes());
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

