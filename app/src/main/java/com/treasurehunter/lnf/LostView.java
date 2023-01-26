package com.treasurehunter.lnf;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LostView extends AppCompatActivity  {

    SwipeRefreshLayout srl_lostView;
    ArrayList<String> array_profile, array_username, array_lost_img_name, array_lost_item_name, array_lost_item_desc, array_phone_number;
    ProgressDialog progressDialog;
    ListView lostView;
    ImageView backTo;
    FloatingActionButton postLostItem;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_view);

        lostView = findViewById(R.id.lostView);
        srl_lostView = findViewById(R.id.swipe_container);
        progressDialog = new ProgressDialog(this);

        srl_lostView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollRefresh();
                srl_lostView.setRefreshing(false);
            }
        });
        srl_lostView.setColorSchemeColors(
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
                Intent back = new Intent(LostView.this, MainActivity.class);
                startActivity(back);
                LostView.this.finish();
            }
        });

        postLostItem = findViewById(R.id.postLostItem);
        postLostItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(LostView.this, LostAddPost.class);
                startActivity(back);
            }
        });
    }

    public void scrollRefresh(){
        progressDialog.setMessage("Mengambil Data.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        },2000);
    }

    void initializeArray(){
        array_profile = new ArrayList<String>();
        array_username = new ArrayList<String>();
        array_lost_img_name = new ArrayList<String>();
        array_lost_item_name = new ArrayList<String>();
        array_lost_item_desc = new ArrayList<String>();
        array_phone_number = new ArrayList<String>();

        array_profile.clear();
        array_username.clear();
        array_lost_img_name.clear();
        array_lost_item_name.clear();
        array_lost_item_desc.clear();
        array_phone_number.clear();
    }

    public void getData(){
        initializeArray();
        AndroidNetworking.get("https://tekajeapunya.com/kelompok_7/tess7/getLostData.php")
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
                                    array_lost_img_name.add(jo.getString("lost_img_name"));
                                    array_lost_item_name.add(jo.getString("lost_item_name"));
                                    array_lost_item_desc.add(jo.getString("lost_item_desc"));
                                    array_phone_number.add(jo.getString("phone_number"));

                                }
                                final LostItem AD_LostItem = new LostItem(LostView.this, array_profile,array_username,array_lost_img_name,array_lost_item_name,array_lost_item_desc,array_phone_number);
                                lostView.setAdapter(AD_LostItem);

                            }else {
                                Toast.makeText(LostView.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();

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
