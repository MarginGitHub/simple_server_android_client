package com.example.margin.demo;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.margin.demo.net.Net;
import com.example.module.beans.Result;
import com.example.module.beans.demo.RegisterResult;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Net.instance().register("18268854059", new Random().nextInt(10000) + "",
                                        new Net.OnNext<Result<RegisterResult>>() {
                                            @Override
                                            public void onNext(Result<RegisterResult> data) {
                                                Toast.makeText(LoginActivity.this,
                                                        String.format("code: %d\nmessage: %s", data.getCode(), data.getMessage()),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        },
                                        new Net.OnError() {
                                            @Override
                                            public void onError(Throwable t) {
                                                Toast.makeText(LoginActivity.this,
                                                        String.format("err: %s", t.toString()),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        },
                                LoginActivity.class.getSimpleName());
                            }
                        }).show();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        Net.instance().releaseRequest(LoginActivity.class.getSimpleName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
