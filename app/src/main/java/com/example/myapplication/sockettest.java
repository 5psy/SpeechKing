package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class sockettest extends AppCompatActivity {

    private static final String host = "192.168.219.103";
    private static final int port = 5555;

    private Socket s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket);

        s = new Socket();
        InetSocketAddress addr = new InetSocketAddress(host, port);
        MyThread thread = new MyThread(addr);
        thread.setDaemon(true);
        thread.start();
    }

    class MyThread extends Thread {

        InetSocketAddress addr;

        public MyThread(InetSocketAddress address) {
            addr = address;
        }

        @Override
        public void run() {
            try {
                s.connect(addr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
