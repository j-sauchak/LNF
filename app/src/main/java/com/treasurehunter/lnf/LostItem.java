package com.treasurehunter.lnf;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LostItem extends ArrayAdapter<String> {
    private final Activity context;
    private ArrayList<String> vProfile;
    private ArrayList<String> vUsername;
    private ArrayList<String> vImgName;
    private ArrayList<String> vItemName;
    private ArrayList<String> vItemDesc;
    private ArrayList<String> vPhoneNumber;

    public LostItem(Activity context, ArrayList<String> profile, ArrayList<String> username, ArrayList<String> lost_img_name, ArrayList<String> lost_item_name, ArrayList<String> lost_item_desc, ArrayList<String> phone_number){
        super(context, R.layout.lost_item, lost_item_name);
        this.context      = context;
        this.vProfile     = profile;
        this.vUsername    = username;
        this.vImgName     = lost_img_name;
        this.vItemName    = lost_item_name;
        this.vItemDesc    = lost_item_desc;
        this.vPhoneNumber = phone_number;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.lost_item, null, true);

        CircleImageView     profile         = rowView.findViewById(R.id.ProfileTS);
        TextView            username        = rowView.findViewById(R.id.UsernameTS);
        TextView            lost_item_name  = rowView.findViewById(R.id.lostName);
        TextView            lost_item_desc  = rowView.findViewById(R.id.lostDesc);
        ImageView           lost_img_name   = rowView.findViewById(R.id.lostIMG);
        ConstraintLayout    chatBTN         = rowView.findViewById(R.id.chatBTN);
        ConstraintLayout    shareBTN        = rowView.findViewById(R.id.shareBTN);

        username.setText(vUsername.get(position));
        lost_item_name.setText(vItemName.get(position));
        lost_item_desc.setText(vItemDesc.get(position));

        if (vProfile.get(position).equals("")){
            Picasso.get().load("https://tekajeapunya.com/kelompok_7/tess7/LnfAdmin/image/noimage.jpg").into(profile);
        }else {
            Picasso.get().load("https://tekajeapunya.com/kelompok_7/tess7/LnfAdmin/image/"+vProfile.get(position)).into(profile);
        }

        if (vImgName.get(position).equals("")){
            Picasso.get().load("https://tekajeapunya.com/kelompok_7/tess7/LnfAdmin/image/noimage.jpg").into(lost_img_name);
        }else {
            Picasso.get().load("https://tekajeapunya.com/kelompok_7/tess7/LnfAdmin/image/"+vImgName.get(position)).into(lost_img_name);
        }

        chatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openWhatsapp = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/62"+vPhoneNumber.get(position)));
                context.startActivity(openWhatsapp);
            }
        });

        shareBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(intent.EXTRA_TEXT, "Hallo.. Saya melihat sepertinya seseorang menemukan barang kamu yang hilang. Ayo kunjungi Lost and Found");
                intent.setType("text/plain");
                context.startActivity(Intent.createChooser(intent, "Share to :"));
            }
        });

        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        anim.setDuration(300);
        rowView.startAnimation(anim);

        return rowView;
    }
}
