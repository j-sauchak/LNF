package com.treasurehunter.lnf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Request;

public class RegisterActivity extends AppCompatActivity {

    EditText ETName,ETEmail, ETWhatsapp, ETPassword;
    ArrayList<String> array_id_user;
    String id_user,username,email,whatsapp,password;
    ProgressDialog progressDialog;

    ConstraintLayout button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ETName      = findViewById(R.id.name);
        ETEmail     = findViewById(R.id.email);
        ETWhatsapp  = findViewById(R.id.whatsapp);
        ETPassword  = findViewById(R.id.password);
        button      = findViewById(R.id.button);

        progressDialog = new ProgressDialog(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Loading ...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                username    = ETName.getText().toString();
                email       = ETEmail.getText().toString();
                whatsapp    = ETWhatsapp.getText().toString();
                password    = ETPassword.getText().toString();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() { validateData(); }

                },1000);



            }
        });
    }

    void validateData(){
        if(username.equals("") || email.equals("") || whatsapp.equals("") || password.equals("")){
            progressDialog.dismiss();
            Toast.makeText(RegisterActivity.this,"Please check your data !", Toast.LENGTH_SHORT).show();
        }else  {
            sendData();
        }
    }
    void sendData(){

        AndroidNetworking.get("http://tekajeapunya.com/kelompok_7/tess7/register.php?username="+username+"&email="+email+"&phone_number="+whatsapp+"&password="+password)
                .setPriority(Priority.MEDIUM)
                .setTag("Add User Data")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            Boolean status = response.getBoolean("status");

                            if(status){

                                new AlertDialog.Builder(RegisterActivity.this)
                                        .setMessage("Please Complete Your Profile")
                                        .setCancelable(false)
                                        .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent detailUser = new Intent(RegisterActivity.this, DetailUser.class);
                                                //detailUser.putExtra("id_user", id_user);
                                                startActivity(detailUser);
                                                RegisterActivity.this.finish();
                                            }
                                        })
                                        .show();

                                /*JSONArray ja = response.getJSONArray("result");
                                JSONObject jo = ja.getJSONObject(0);
                                array_id_user.add(jo.getString("id_user"));
                                id_user = array_id_user.toString();*/

                            }else{
                                new AlertDialog.Builder(RegisterActivity.this)
                                        .setMessage("Please check your data !")
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
                        Log.d("ErrorTambahData",""+anError.getErrorBody());
                    }

                });
    }

}