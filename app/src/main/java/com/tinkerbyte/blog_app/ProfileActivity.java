package com.tinkerbyte.blog_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {



    TextView mPhone,mEmail,mName,updateInformation;
    String status = "Done";
    private static final String KEY_NAME = "Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PHONE = "Phone";
    ImageView mProfilePic;
    Context context;
   private FirebaseFirestore db ;
   private DocumentReference getdata;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
       context = getApplicationContext();
       FirebaseApp.initializeApp(context);
        db = FirebaseFirestore.getInstance();
        getdata = db.collection("Users").document("Data");
        mProfilePic = findViewById(R.id.ProfileImage);
        final SharedPreferences mpreferences = getSharedPreferences("DISPLAY", Context.MODE_PRIVATE);
        final SharedPreferences.Editor medit = mpreferences.edit();
        if(!status.equals(mpreferences.getString("cond","")))
        {
            medit.putString("cond","Done");
            medit.commit();
            Intent intent = new Intent(ProfileActivity.this,UpdateDetailsActivity.class);
            startActivity(intent);



        }
        else
        {
            mName  = (TextView)findViewById(R.id.disp_Name);
            mEmail = (TextView)findViewById(R.id.email_disp);
            mPhone = (TextView)findViewById(R.id.disp_mobile);
            updateInformation = (TextView)findViewById(R.id.Update_Information);
            retreiveData();
            updateInformation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileActivity.this,UpdateDetailsActivity.class);
                    startActivity(intent);


                }
            });



        }

    }
    public void retreiveData()
    {
        getdata.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                       if(documentSnapshot.exists())
                       {
                           Map<String, Object> data = documentSnapshot.getData();
                           mName.setText(data.get(KEY_NAME).toString());
                           mEmail.setText(data.get(KEY_EMAIL).toString());
                           mPhone.setText(data.get(KEY_PHONE).toString());
                       }
                       else
                       {
                           Toast.makeText(ProfileActivity.this,"Document Doesn't Exist",Toast.LENGTH_SHORT).show();
                       }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ProfileActivity.this,"Something Went Wrongt",Toast.LENGTH_SHORT).show();

                    }
                });



    }

}
