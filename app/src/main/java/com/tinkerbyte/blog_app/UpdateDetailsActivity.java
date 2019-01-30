package com.tinkerbyte.blog_app;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UpdateDetailsActivity extends AppCompatActivity {

    EditText name,email,phone;
    Button update;
    ImageButton upImage;
    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String KEY_NAME = "Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PHONE = "Phone";
    private static final String KEY_IMAGE_URL = "ImageUrl";
    private String Image_Url;
    Context context;
    private FirebaseFirestore db ;
    private StorageReference mReference;
    private SpinKitView mProgressBar;
    private StorageTask mUploadTask;
    StorageReference newReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);
        update = (Button)findViewById(R.id.update);
        context = getApplicationContext();
        FirebaseApp.initializeApp(context);
        db = FirebaseFirestore.getInstance();
        mReference = FirebaseStorage.getInstance().getReference("UserPics");
        name = (EditText)findViewById(R.id.nameUpdate);
        phone = (EditText)findViewById(R.id.phoneUpdate);
        email = (EditText)findViewById(R.id.emailUpdate);
        upImage = (ImageButton)findViewById(R.id.imageUpdate);
        mProgressBar = (SpinKitView)findViewById(R.id.spin_kit);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mUploadTask!= null && mUploadTask.isInProgress())
                {
                Toast.makeText(UpdateDetailsActivity.this ,"Upload In Progress",Toast.LENGTH_SHORT).show();
                }
                else {
                    sendData();
                }
            }
        });

        upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker();

            }
        });
    }

   private   void imagePicker()
    {
        Intent imgIntent = new Intent();
        imgIntent.setType("image/*");
        imgIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imgIntent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            mImageUri = data.getData();
            Glide.with(this).load(mImageUri).apply(new RequestOptions() .fitCenter()).into(upImage);

        }

    }
private String getFileExtension(Uri uri)
{
    ContentResolver cr = getContentResolver();
    MimeTypeMap mime = MimeTypeMap.getSingleton();
    return  mime.getExtensionFromMimeType(cr.getType(uri));

}
 private  void uploadImageMethod()
 {
     if(mImageUri!= null)
     {
         newReference = mReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
        mUploadTask =  newReference.putFile(mImageUri)
                      .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                              Handler mhandler = new Handler();
                              mhandler.postDelayed(new Runnable() {
                                  @Override
                                  public void run() {
                                      mProgressBar.setProgress(0);
                                  }
                              } , 5000);
                              Toast.makeText(UpdateDetailsActivity.this,"Upload Successful",Toast.LENGTH_LONG).show();
                            Image_Url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                          }
                      })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateDetailsActivity.this,"Something Went Error",Toast.LENGTH_SHORT).show();
                            }
                        })
                          .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                              @Override
                              public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                  double progress = (100* taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                                  mProgressBar.setProgress((int) progress);
                              }
                          });

     }
     else
     {
         Toast.makeText(UpdateDetailsActivity.this,"No Image Selected",Toast.LENGTH_SHORT).show();

     }

 }

 public void sendData()
    {
        uploadImageMethod();
        String UserName = name.getText().toString();
        String UserMail = email.getText().toString();
        String UserPhone = phone.getText().toString();

        Map<String, Object> data = new HashMap<>();
        data.put(KEY_NAME , UserName);
        data.put(KEY_PHONE,UserPhone);
        data.put(KEY_EMAIL,UserMail);
        db.collection("Users").document("Data").set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateDetailsActivity.this,"Updated Successfully",Toast.LENGTH_SHORT).show();
                        Intent  mIntent  =new Intent(UpdateDetailsActivity.this,ProfileActivity.class);
                        startActivity(mIntent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateDetailsActivity.this,"Something Went Error",Toast.LENGTH_SHORT).show();

                    }
                });

    }
}
