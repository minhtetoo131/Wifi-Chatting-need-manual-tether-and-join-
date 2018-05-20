package com.minhtetoo.wifichatting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this,this);


    }

    @OnClick(R.id.btn_server)
    public void onTapServer(View view){
        startActivity(new Intent(getApplicationContext(),Server.class));
    }

    @OnClick(R.id.btn_client)
    public void onTapClient(View view){
        startActivity(new Intent(getApplicationContext(),Client.class));
    }
}
