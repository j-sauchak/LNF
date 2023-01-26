package com.treasurehunter.lnf;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileItem extends ArrayAdapter<String> {
    private final Activity context;
    private ArrayList<String> vIdUser;
    private ArrayList<String> vProfile;
    private ArrayList<String> vUsername;
    private ArrayList<String> vFullname;
    private ArrayList<String> vBio;
    private ArrayList<String> vWhatsapp;

    public ProfileItem(Activity context, ArrayList<String> profile, ArrayList<String> username, ArrayList<String> full_name, ArrayList<String> user_desc, ArrayList<String> phone_number){
        super(context, R.layout.profile_view, username);
        this.context        = context;
        this.vProfile       = profile;
        this.vUsername      = username;
        this.vFullname      = full_name;
        this.vBio           = user_desc;
        this.vWhatsapp      = phone_number;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.profile_item, null, true);

        CircleImageView profile     = rowView.findViewById(R.id.profileUser);
        TextView username           = rowView.findViewById(R.id.nickUser);
        TextView full_name          = rowView.findViewById(R.id.fullName);
        TextView user_desc          = rowView.findViewById(R.id.userDesc);
        TextView phone_number       = rowView.findViewById(R.id.userContact);


        username.setText("@"+vUsername.get(position));
        full_name.setText(vFullname.get(position));
        user_desc.setText(vBio.get(position));
        phone_number.setText(vWhatsapp.get(position));

        if (vProfile.get(position).equals("")){
            Picasso.get().load("https://tekajeapunya.com/kelompok_7/tess7/LnfAdmin/image/noimage.jpg").into(profile);
        }else {
            Picasso.get().load("https://tekajeapunya.com/kelompok_7/tess7/LnfAdmin/image/"+vProfile.get(position)).into(profile);
        }

        /*editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String profile = vProfile.get(position);
                String username = vUsername.get(position);
                String full_name = vFullname.get(position);
                String bio = vBio.get(position);
                String whatsapp = vWhatsapp.get(position);

                Intent edit = new Intent(view.getContext(), EditProfile.class);
                edit.putExtra("profile", profile);
                edit.putExtra("username", username);
                edit.putExtra("full_name", full_name);
                edit.putExtra("user_desc", bio);
                edit.putExtra("phone_number", whatsapp);

                context.startActivity(edit);
            }
        });*/

        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        anim.setDuration(300);
        rowView.startAnimation(anim);

        return rowView;
    }
}
