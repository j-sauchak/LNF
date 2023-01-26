package com.treasurehunter.lnf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText ETEmail, ETPassword;
    String email, password;
    ProgressDialog progressDialog;
    TextView register;

    SessionManager sessionManager;

    Button Login;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ETEmail     = findViewById(R.id.email);
        ETPassword  = findViewById(R.id.password);
        Login       = findViewById(R.id.loginBTN);
        register    = findViewById(R.id.RegisterBTN);
        progressDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(getApplicationContext());

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Loading ...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                email = ETEmail.getText().toString();
                password = ETPassword.getText().toString();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() { validasiData(); }

                },1000);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regPage  = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regPage);
            }
        });
    }
    void validasiData(){
        if(email.equals("") || password.equals("")){
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this,"Periksa kembali data Anda !", Toast.LENGTH_SHORT).show();
        }else  {
            SignIn();
        }
    }
    void SignIn(){
        AndroidNetworking.post("http://tekajeapunya.com/kelompok_7/tess7/login.php")
                .addBodyParameter("email",""+email)
                .addBodyParameter("password",""+password)
                .setPriority(Priority.HIGH)
                .setTag("Login")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("Login",""+response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String id_user = response.getString("id_user");
                            //Toast.makeText(LoginActivity.this, ""+status, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);
                            if(status){
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setMessage("Welcome To Lost And Found !")
                                        .setCancelable(false)
                                        .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                sessionManager.createSession(id_user);
                                                Intent login = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(login);
                                                LoginActivity.this.finish();
                                            }
                                        })
                                        .show();

                            }else{
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setMessage("Check your Email and Password !")
                                        .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();

                                            }
                                        })
                                        .setCancelable(false)
                                        .show();

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(com.androidnetworking.error.ANError anError) {
                        Log.d("Error Login",""+anError.getErrorBody());
                    }

           });
}
}