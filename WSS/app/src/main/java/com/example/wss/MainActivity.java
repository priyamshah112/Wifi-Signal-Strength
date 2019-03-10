package com.example.wss;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    TextView st;
    Button bt;
    Button store;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        st = (TextView)findViewById(R.id.textView);

        bt = (Button)findViewById(R.id.button5);
        store = (Button)findViewById(R.id.button6);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disp();

            }
        });

        store.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                savetofile();

            }
        });

        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                disp();
                savetofile();
                handler.postDelayed(this, 60000);
            }
        };

        handler.postDelayed(r, 1000);
    }
    // Strength calc and display
    private void disp(){
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int rssi = wifiManager.getConnectionInfo().getRssi();
        int level = WifiManager.calculateSignalLevel(rssi,5);
        String ssid = wifiManager.getConnectionInfo().getSSID();
        String MacAddr = wifiManager.getConnectionInfo().getMacAddress();

        st.setText("\t\tSignal Strength of "+ ssid+"\n\n\t\tMac Address = "+ MacAddr+"\n\n\t\tRSSI = "+ rssi + " dbm \n\n\t\tLevel = "+ level + " out of 5");
    }

    //Store in a file
    private void savetofile(){

        File directory = new File(Environment.getExternalStorageDirectory() + java.io.File.separator +"WSS");
        if (!directory.exists())
            Toast.makeText(this, (directory.mkdirs() ? "Directory has been created" : "Directory not created"), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "File Updated", Toast.LENGTH_SHORT).show();
        System.out.println(directory);
        File file = new File(Environment.getExternalStorageDirectory() + java.io.File.separator +"WSS" + java.io.File.separator + "WSS.txt");
        System.out.println(file);

        Date currentTime = Calendar.getInstance().getTime();
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }

        try {
            OutputStreamWriter file_writer = new OutputStreamWriter(new FileOutputStream(file,true));
            BufferedWriter buffered_writer = new BufferedWriter(file_writer);
            buffered_writer.write("############\n"+currentTime+"\n"+st.getText().toString()+"\n");
            buffered_writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
