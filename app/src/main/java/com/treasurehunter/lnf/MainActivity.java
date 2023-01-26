package com.treasurehunter.lnf;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnIMG1 = findViewById(R.id.imageBTN1);
        ImageButton btnIMG2 = findViewById(R.id.imageBTN2);
        ImageButton btnIMG3 = findViewById(R.id.imageBTN3);
        ImageButton btnIMG4 = findViewById(R.id.imageBTN4);
        ImageButton btnIMG5 = findViewById(R.id.imageBTN5);

        btnIMG1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String id_user     = user.get(SessionManager.kunci_id_user);

                Intent page = new Intent(MainActivity.this, ProfileView.class);
                page.putExtra("id_user", id_user);
                startActivity(page);
            }
        });

        btnIMG2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page2 = new Intent(MainActivity.this, LostView.class);
                startActivity(page2);
            }
        });

        btnIMG3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page3 = new Intent(MainActivity.this, FoundView.class);
                startActivity(page3);
            }
        });

        btnIMG4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page4 = new Intent(MainActivity.this, InfoApk.class);
                startActivity(page4);
            }
        });

        btnIMG5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String id_user     = user.get(SessionManager.kunci_id_user);
//                sessionManager.setLogin(false);
                sessionManager.logout();
            }
        });

    }
}