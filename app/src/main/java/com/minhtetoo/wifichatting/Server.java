package com.minhtetoo.wifichatting;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Server extends AppCompatActivity {

    @BindView(R.id.lv_chat)ListView lvChat;
    @BindView(R.id.btn_send)Button btnSend;
    @BindView(R.id.et)EditText editText;
    Socket socket;
    private ServerSocket serverSocket;
    public static final int NETWORK_PORT = 8080;
    ArrayList<GetModel> list;
    private Myadapter mAdapter;

    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        ButterKnife.bind(this,this);

        list = new ArrayList<>();
        GetModel getModel = new GetModel();
        getModel.setMeOrU(false);
        getModel.setText("Waiting for client.....");
        list.add(getModel);

        mAdapter = new Myadapter(getApplicationContext(),list);
        lvChat.setAdapter(mAdapter);

        Thread tcpThread = new Thread(new TCPThread());
        tcpThread.start();
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

    @OnClick(R.id.btn_send)
    public void onTapSend(View view){
        String message = editText.getText().toString();
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            out.println(message);
            show(message,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void show(String read, boolean isMeOrU){
        GetModel getModel = new GetModel();
        getModel.setMeOrU(isMeOrU);
        getModel.setText(read);
        list.add(getModel);

        handler.post(new Runnable() {
            @Override
            public void run() {
             mAdapter.setNewItems(list);
             editText.setText("");
             lvChat.setSelection(list.size());
            }
        });
    }
    class TCPThread implements Runnable{

        @Override
        public void run() {
            socket = null;
            try {
                serverSocket = new ServerSocket(NETWORK_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while(!Thread.currentThread().isInterrupted()){
                try {
                    socket = serverSocket.accept();
                    ReadingThread readingThread = new ReadingThread(socket);
                    new Thread(readingThread).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    class ReadingThread implements Runnable{
        private  Socket mClientSocket;
        private BufferedReader bufferedReader;

        public ReadingThread(Socket clientSocket){
            mClientSocket = clientSocket;

            try {
                bufferedReader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            while(!Thread.currentThread().isInterrupted()){
                String read = null;
                try {
                    read = bufferedReader.readLine();
                    if (read == null){
                        read = " Client is left";
                        socket.close();
                        show(read,false);
                        break;
                    }
                    show(read,false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
