package com.minhtetoo.wifichatting;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Client extends AppCompatActivity {

    @BindView(R.id.lv_chat)ListView lvChat;
    @BindView(R.id.btn_send)Button btnSend;
    @BindView(R.id.et)EditText editText;
    public static final int NETWORK_PORT = 8080;
    String serverIP;
    Socket socket;

    ArrayList<GetModel> list;
    Myadapter myadapter;

    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        ButterKnife.bind(this,this);

        list = new ArrayList<>();
        GetModel getModel = new GetModel();
        getModel.setMeOrU(false);
        getModel.setText("Let's Chat....");
        list.add(getModel);

        myadapter = new Myadapter(getApplicationContext(),list);
        lvChat.setAdapter(myadapter);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        DhcpInfo info = wifiManager.getDhcpInfo();
        serverIP = Formatter.formatIpAddress(info.gateway);

        TCPclientsocket();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void TCPclientsocket(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String host = serverIP;
                BufferedReader bufferedReader = null;

                try {
                    socket = new Socket(host,NETWORK_PORT);
                } catch (IOException e) {
                    e.printStackTrace();

                }

                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while(true){
                    String message = null;
                    try {
                        message = bufferedReader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (message == null){
                        show("Server left",false);
                        break;
                    }else{
                        show(message,false);
                    }
                }
            }
        }).start();
    }

    private void show(String read, boolean isMeOrU){
        GetModel getModel = new GetModel();
        getModel.setMeOrU(isMeOrU);
        getModel.setText(read);
        list.add(getModel);

        handler.post(new Runnable() {
            @Override
            public void run() {
                myadapter.setNewItems(list);
                editText.setText("");
                lvChat.setSelection(list.size());
            }
        });
    }

    @OnClick(R.id.btn_send)
    public void onTapSend(View view){

        try {
            String message = editText.getText().toString();
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            printWriter.println(message);
            show(message,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
