package com.treasurehunter.lnf;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class EditProfile extends AppCompatActivity {

    ImageView backTo;
    CircleImageView EditProfileIMG;
    ArrayList<String> array_username, array_full_name, array_profile, array_bio, array_whatsapp;
    EditText ETusername, ETbio, ETfullname, ETwhatsapp, getOLDIMG;
    String id_user, nickname, bio, full_name, phone_number, img_name, old_img;
    Button editBTN;
    ProgressDialog progressDialog;
    private Context mContext;

    String choices;

    SessionManager sessionManager;

    static final int REQUEST_TAKE_PHOTO = 1;
    final int CODE_GALLERY_REQUEST = 999;
    String rChoices[]= {"Take a photo","From Album"};
    public final String APP_TAG = "MyApp";
    Bitmap bitMap = null;
    public String photoFileName = "photo.jpg";
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        backTo = findViewById(R.id.backTo);
        backTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String id_user     = user.get(SessionManager.kunci_id_user);
                Intent page = new Intent(EditProfile.this, ProfileView.class);
                page.putExtra("id_user", id_user);
                startActivity(page);
                EditProfile.this.finish();
            }
        });

        EditProfileIMG = findViewById(R.id.EditProfileUser);
        ETusername     = findViewById(R.id.ETUsername);
        ETfullname     = findViewById(R.id.ETFullname);
        ETbio          = findViewById(R.id.ETBio);
        ETwhatsapp     = findViewById(R.id.ETWhatsapp);
        editBTN        = findViewById(R.id.editBTN);
        getOLDIMG      = findViewById(R.id.testID);
        getOLDIMG.setVisibility(View.INVISIBLE);

        progressDialog = new ProgressDialog(this);

        getDataIntent();
        getDataUser();

        EditProfileIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bitMap != null) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfile.this);
                    alertDialogBuilder.setMessage("Do yo want to take photo again?");

                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            TakePhoto();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                } else {

                    TakePhoto();
                }
            }
        });

        editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //username = ETusername.getText().toString();
                new AlertDialog.Builder(EditProfile.this)
                        .setTitle("Keep changes?")
                        .setCancelable(false)
                        .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setTitle("Please Wait");
                                progressDialog.setMessage("Updating data...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                nickname     = ETusername.getText().toString();
                                full_name    = ETfullname.getText().toString();
                                bio          = ETbio.getText().toString();
                                phone_number = ETwhatsapp.getText().toString();
                                old_img      = getOLDIMG.getText().toString();
                                img_name     = EditProfileIMG.getResources().toString();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        validatingData();
                                    }
                                }, 1000);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

    }

    void initializeArray(){
        array_username = new ArrayList<String>();
        array_full_name = new ArrayList<String>();
        array_profile = new ArrayList<String>();
        array_bio = new ArrayList<String>();
        array_whatsapp = new ArrayList<String>();

        array_username.clear();
        array_full_name.clear();
        array_profile.clear();
        array_bio.clear();
        array_whatsapp.clear();
    }

    void getDataIntent(){
        Bundle bundle = getIntent().getExtras();
        id_user = bundle.getString("id_user");
    }

    void getDataUser(){
        initializeArray();
        AndroidNetworking.get("http://tekajeapunya.com/kelompok_7/tess7/getDataUser.php?id_user="+id_user)
                .setPriority(Priority.MEDIUM)
                .setTag("Get User Data")
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

                                    JSONObject jo = ja.getJSONObject(i);

                                    array_username.add(jo.getString("username"));
                                    array_full_name.add(jo.getString("full_name"));
                                    array_bio.add(jo.getString("user_desc"));
                                    array_whatsapp.add(jo.getString("phone_number"));
                                    array_profile.add(jo.getString("profile"));

                                }

                                String[] username = new String[array_username.size()];
                                for (int i = 0 ; i < array_username.size(); i++){
                                    username[i] = array_username.get(i);
                                }

                                for (String setUsername : username){
                                    ETusername.setText(setUsername);
                                }

                                String[] full_name = new String[array_full_name.size()];
                                for (int i = 0 ; i < array_full_name.size(); i++){
                                    full_name[i] = array_full_name.get(i);
                                }

                                for (String setFullname : full_name){
                                    ETfullname.setText(setFullname);
                                }

                                String[] bio = new String[array_bio.size()];
                                for (int i = 0 ; i < array_bio.size(); i++){
                                    bio[i] = array_bio.get(i);
                                }

                                for (String setBio : bio){
                                    ETbio.setText(setBio);
                                }

                                String[] whatsapp = new String[array_whatsapp.size()];
                                for (int i = 0 ; i < array_whatsapp.size(); i++){
                                    whatsapp[i] = array_whatsapp.get(i);
                                }

                                for (String setWhatsapp : whatsapp){
                                    ETwhatsapp.setText(setWhatsapp);
                                }

                                String[] profile = new String[array_profile.size()];
                                for (int i = 0 ; i < array_profile.size(); i++){
                                    profile[i] = array_profile.get(i);
                                }

                                for (String setProfile : profile){
                                    getOLDIMG.setText(setProfile);
                                    Picasso.get().load("https://tekajeapunya.com/kelompok_7/tess7/LnfAdmin/image/"+setProfile).into(EditProfileIMG);
                                }


                            }else {
                                Toast.makeText(EditProfile.this, "Data not Found!", Toast.LENGTH_SHORT).show();

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

    void validatingData(){
        if(nickname.equals("") || full_name.equals("") || bio.equals("") || phone_number.equals("")){
            progressDialog.dismiss();
            Toast.makeText(EditProfile.this, "Check your input!", Toast.LENGTH_SHORT).show();
        }else {
            updateData();
        }
    }

    public void TakePhoto(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Select");
        builder.setItems(rChoices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                choices = String.valueOf(rChoices[which]);

                if (choices.equals("Take a photo"))
                {
                    // create Intent to take a picture and return control to the calling application
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Create a File reference to access to future access
                    photoFile = getPhotoFileUri(photoFileName);

                    // wrap File object into a content provider
                    // required for API >= 24
                    // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
                    String authorities = getPackageName() + ".fileprovider";
                    Uri fileProvider = FileProvider.getUriForFile(EditProfile.this, authorities, photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                    // So as long as the result is not null, it's safe to use the intent.
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Start the image capture intent to take photo
                        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                    }
                }
                else
                {
                    ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
                }
            }
        });
        builder.show();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODE_GALLERY_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
            }else{
                Toast.makeText(getApplicationContext(), "You don't have permission to access gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                // by this point we have the camera photo on disk
                //Bitmap takenImage = BitmapFactory.decodeFile(String.valueOf(photoFile));
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                bitMap = decodeSampledBitmapFromFile(String.valueOf(photoFile), 1000, 700);
                EditProfileIMG.setImageBitmap(bitMap);
            } else { // Result was a failure
                Toast.makeText(EditProfile.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (intent != null) {
                Uri photoUri = intent.getData();
                // Do something with the photo based on Uri
                bitMap = null;
                try {
                    bitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                EditProfileIMG.setImageBitmap(bitMap);
            }
        }
    }

    public File getPhotoFileUri(String fileName)  {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use getExternalFilesDir on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

            return file;

        }
        return null;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    void updateData(){
        img_name = getStringImage(bitMap);
        AndroidNetworking.post("https://tekajeapunya.com/kelompok_7/tess7/EditProfile.php")
                .addBodyParameter("id_user",""+id_user)
                .addBodyParameter("username",""+nickname)
                .addBodyParameter("full_name",""+full_name)
                .addBodyParameter("user_desc",""+bio)
                .addBodyParameter("phone_number",""+phone_number)
                .addBodyParameter("profile",""+img_name)
                .addBodyParameter("old_img",""+old_img)
                .setPriority(Priority.MEDIUM)
                .setTag("Updating Data")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("Cheking Update",""+response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
                            //Toast.makeText(EditProfile.this, ""+pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);
                            if(status){
                                new AlertDialog.Builder(EditProfile.this)
                                        .setMessage("Changes updated !")
                                        .setCancelable(false)
                                        .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                sessionManager = new SessionManager(getApplicationContext());
                                                HashMap<String, String> user = sessionManager.getUserDetails();
                                                String id_user     = user.get(SessionManager.kunci_id_user);
                                                Intent page = new Intent(EditProfile.this, ProfileView.class);
                                                page.putExtra("id_user", id_user);
                                                startActivity(page);
                                                EditProfile.this.finish();
                                            }
                                        })
                                        .show();
                            }
                            else{
                                new AlertDialog.Builder(EditProfile.this)
                                        .setMessage("Changes updated !")
                                        .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                sessionManager = new SessionManager(getApplicationContext());
                                                HashMap<String, String> user = sessionManager.getUserDetails();
                                                String id_user     = user.get(SessionManager.kunci_id_user);
                                                Intent page = new Intent(EditProfile.this, ProfileView.class);
                                                page.putExtra("id_user", id_user);
                                                startActivity(page);
                                                EditProfile.this.finish();
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
                    public void onError(ANError anError) {
                        Log.d("Failed !",""+anError.getErrorBody());
                    }
                });
    }

}