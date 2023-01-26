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
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Request;

public class DetailUser extends AppCompatActivity {

    EditText ETFullname, ETBio, idUSER;
    String id_user,full_name,bio;
    ArrayList<String> array_id_user;
    ProgressDialog progressDialog;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        idUSER      = findViewById(R.id.iduser);
        idUSER.setVisibility(View.INVISIBLE);

        ETFullname  = findViewById(R.id.fullname);
        ETBio       = findViewById(R.id.desc);
        button      = findViewById(R.id.button);

        progressDialog = new ProgressDialog(this);
        //getDataIntent();
        getIDUSER();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Loading ...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                full_name   = ETFullname.getText().toString();
                bio         = ETBio.getText().toString();
                id_user     = idUSER.getText().toString();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() { validateData(); }

                },1000);
            }
        });
    }

    void initializeArray(){
        array_id_user = new ArrayList<String>();

        array_id_user.clear();
    }

    void getIDUSER(){
        initializeArray();
        AndroidNetworking.get("http://tekajeapunya.com/kelompok_7/tess7/getIDUSER.php")
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
                                JSONArray ja = response.getJSONArray("result");
                                for (int i = 0 ; i < ja.length() ; i++) {

                                    //JSONObject jo = ja.getJSONObject(i);

                                    //array_id_user.add(jo.getString("id_user"));



                                    JSONObject jo = ja.getJSONObject(i);

                                    array_id_user.add(jo.getString("id_user"));
                                }

                                String[] id = new String[array_id_user.size()];
                                for (int i = 0 ; i < array_id_user.size(); i++){
                                    id[i] = array_id_user.get(i);
                                }

                                for (String getID : id){
                                    idUSER.setText(getID);
                                }

                            }else {
                                Toast.makeText(DetailUser.this, "Failed to find the ID", Toast.LENGTH_SHORT).show();

                            }



                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(com.androidnetworking.error.ANError anError) {
                        Log.d("Error Take an ID",""+anError.getErrorBody());
                    }

                });
    }

    /*void getDataIntent(){
        Bundle bundle = getIntent().getExtras();
        id_user = bundle.getString("id_user");
    }*/

    void validateData(){
        if(full_name.equals("") || bio.equals("") || id_user.equals("")){
            progressDialog.dismiss();
            Toast.makeText(DetailUser.this,"Please check your data !", Toast.LENGTH_SHORT).show();
        }else  {
            sendData();
        }
    }
    void sendData(){

        AndroidNetworking.post("http://tekajeapunya.com/kelompok_7/tess7/detailuser.php")
                .addBodyParameter("id_user",""+id_user)
                .addBodyParameter("full_name",""+full_name)
                .addBodyParameter("user_desc",""+bio)
                .setPriority(Priority.MEDIUM)
                .setTag("Add Detail User")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            Boolean status = response.getBoolean("status");
                            if(status){
                                new AlertDialog.Builder(DetailUser.this)
                                        .setMessage("Registration Complete, Please Login")
                                        .setCancelable(false)
                                        .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent register = new Intent(DetailUser.this, LoginActivity.class);
                                                startActivity(register);
                                                DetailUser.this.finish();
                                            }
                                        })
                                        .show();
                            }
                            else{
                                new AlertDialog.Builder(DetailUser.this)
                                        .setMessage("Registration failed, please check your data !")
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