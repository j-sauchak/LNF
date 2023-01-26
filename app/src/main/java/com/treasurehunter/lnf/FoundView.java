package com.treasurehunter.lnf;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class FoundView extends AppCompatActivity  {

    SwipeRefreshLayout srl_foundView;
    ArrayList<String> array_profile, array_username, array_found_img_name, array_found_item_name, array_found_item_desc, array_phone_number;
    ProgressDialog progressDialog;
    FloatingActionButton postFoundItem;
    ListView foundView;
    ImageView backTo;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_view);

        foundView = findViewById(R.id.foundView);
        srl_foundView = findViewById(R.id.swipe_container);

        progressDialog = new ProgressDialog(this);

        srl_foundView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollRefresh();
                srl_foundView.setRefreshing(false);
            }
        });
        srl_foundView.setColorSchemeColors(
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
                Intent back = new Intent(FoundView.this, MainActivity.class);
                startActivity(back);
                FoundView.this.finish();
            }
        });

        postFoundItem = findViewById(R.id.postFoundItem);
        postFoundItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postFoundItem = new Intent(FoundView.this, FoundAddPost.class);
                startActivity(postFoundItem);
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
        array_found_img_name = new ArrayList<String>();
        array_found_item_name = new ArrayList<String>();
        array_found_item_desc = new ArrayList<String>();
        array_phone_number = new ArrayList<String>();

        array_profile.clear();
        array_username.clear();
        array_found_img_name.clear();
        array_found_item_name.clear();
        array_found_item_desc.clear();
        array_phone_number.clear();
    }

    public void getData(){
        initializeArray();
        AndroidNetworking.get("https://tekajeapunya.com/kelompok_7/tess7/getFoundData.php")
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
                                    array_found_img_name.add(jo.getString("found_img_name"));
                                    array_found_item_name.add(jo.getString("found_item_name"));
                                    array_found_item_desc.add(jo.getString("found_item_desc"));
                                    array_phone_number.add(jo.getString("phone_number"));
                                }
                                final FoundItem adapter = new FoundItem(FoundView.this, array_profile,array_username,array_found_img_name,array_found_item_name,array_found_item_desc,array_phone_number);
                                foundView.setAdapter(adapter);

                                /*foundView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {

                                        Toast.makeText(mContext, "Position",   Toast.LENGTH_LONG).show();

                                    }
                                });*/

                                foundView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d("TestKlik",""+array_username.get(position));
                                        Toast.makeText(FoundView.this, array_username.get(position), Toast.LENGTH_SHORT).show();

                                        Intent sendData = new Intent(FoundView.this, FoundItemDesc.class);
                                        sendData.putExtra("profile",array_profile.get(position));
                                        sendData.putExtra("username",array_username.get(position));
                                        sendData.putExtra("found_img_name",array_found_img_name.get(position));
                                        sendData.putExtra("found_item_name",array_found_item_name.get(position));
                                        sendData.putExtra("found_item_desc",array_found_item_desc.get(position));
                                        startActivity(sendData);
                                    }
                                });

                            }else {
                                Toast.makeText(FoundView.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();

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
