package com.treasurehunter.lnf;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class FoundItemDesc extends AppCompatActivity {

    String profile, username, found_img_name, found_item_name, found_item_desc;
    ImageView img_name;
    TextView item_name, item_desc;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_item_desc);

        img_name  = findViewById(R.id.foundDescIMG);
        item_name = findViewById(R.id.foundDescName);
        item_desc = findViewById(R.id.foundDescTX);


        progressDialog = new ProgressDialog(this);
        getDataIntent();

    }

    void getDataIntent(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            item_name.setText(bundle.getString("found_item_name"));
            item_desc.setText(bundle.getString("found_item_desc"));
            Picasso.get().load("https://tekajeapunya.com/kelompok_7/tess7/LnfAdmin/image/" + bundle.getString("found_img_name")).into(img_name);
        }else{
            item_name.setText("");
            item_desc.setText("");
            Picasso.get().load("https://tekajeapunya.com/kelompok_7/tess7/LnfAdmin/image/noimage.jpg").into(img_name);
        }

    }

}
