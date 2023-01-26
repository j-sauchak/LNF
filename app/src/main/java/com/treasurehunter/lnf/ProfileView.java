package com.treasurehunter.lnf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileView extends AppCompatActivity {

    SwipeRefreshLayout srl_profileView;
    ImageView backTo;
    String id_user;
    ArrayList<String> array_profile, array_username, array_full_name, array_user_desc, array_whatsapp;
    ProgressDialog progressDialog;
    ListView profileView;
    ConstraintLayout  editBTN;
    SessionManager sessionManager;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view);

        profileView = findViewById(R.id.profileView);
        srl_profileView = findViewById(R.id.swipe_container);
        progressDialog = new ProgressDialog(this);
        getDataIntent();

        srl_profileView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollRefresh();
                srl_profileView.setRefreshing(false);
            }
        });
        srl_profileView.setColorSchemeColors(
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.orange),
                getResources().getColor(R.color.red)
        );
        scrollRefresh();

        backTo = findViewById(R.id.backTo);
        backTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(ProfileView.this, MainActivity.class);
                startActivity(back);
                ProfileView.this.finish();
            }
        });

        editBTN = findViewById(R.id.editProfileBTN);
        editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String id_user     = user.get(SessionManager.kunci_id_user);
                Intent edit = new Intent(ProfileView.this, EditProfile.class);
                edit.putExtra("id_user", id_user);
                startActivity(edit);
                ProfileView.this.finish();
            }
        });

    }

    void getDataIntent(){
        Bundle bundle = getIntent().getExtras();
        id_user = bundle.getString("id_user");
    }

    public void scrollRefresh(){
        progressDialog.setMessage("Loading your profile ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getProfileData();
            }
        },500);
    }

    void initializeArray(){
        array_profile = new ArrayList<String>();
        array_username = new ArrayList<String>();
        array_full_name = new ArrayList<String>();
        array_user_desc = new ArrayList<String>();
        array_whatsapp = new ArrayList<String>();

        array_profile.clear();
        array_username.clear();
        array_full_name.clear();
        array_user_desc.clear();
        array_whatsapp.clear();
    }

    public void getProfileData(){
        initializeArray();
        AndroidNetworking.get("https://tekajeapunya.com/kelompok_7/tess7/getProfileData.php?id_user="+id_user)
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try{
                            Boolean status = response.getBoolean("status");
                            if(status){
                                JSONArray ja = response.getJSONArray("result");
                                Log.d("respon",""+ja);
                                for (int i = 0 ; i < ja.length() ; i++){
                                    JSONObject jo = ja.getJSONObject(i);

                                    array_profile.add(jo.getString("profile"));
                                    array_username.add(jo.getString("username"));
                                    array_full_name.add(jo.getString("full_name"));
                                    array_user_desc.add(jo.getString("user_desc"));
                                    array_whatsapp.add(jo.getString("phone_number"));

                                }
                                final ProfileItem AD_ProfileItem = new ProfileItem(ProfileView.this, array_profile,array_username,array_full_name,array_user_desc,array_whatsapp);
                                profileView.setAdapter(AD_ProfileItem);

                                //profileView.setOnItemClickListener();

                            }else {
                                Toast.makeText(ProfileView.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();

                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

}