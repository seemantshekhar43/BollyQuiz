package comandydevo.wixsite.seemantshekhar43.bollyquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class EditProfile extends AppCompatActivity {
    private Toolbar toolbar;
    Spinner countrySpinner;

   private EditText editUserName, editMobile, editCity;
    private TextView remove,updatePic;
    CircleImageView editProfileImage;
    Button save;
    private static final int GALLERY_PICK = 1;



    ProgressDialog mProgressDialog;

    //Firebase stuffs
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    //Realtime Database Stuffs
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference rootref = mDatabase.getReference(); // root
    DatabaseReference mUserData;

    // Firebase Storage stuffs
    private StorageReference mStorageRef;






    ArrayList<String> countryArrayList = new ArrayList<String>(Arrays.asList(CountryDetails.getCountry()));





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

     //Firebase Stuffs
      mAuth = FirebaseAuth.getInstance();

        // Get Current User
        currentUser = mAuth.getCurrentUser();

        // Database stuffs
        mUserData = rootref.child("Users");

        //Storage stuffs
        mStorageRef = FirebaseStorage.getInstance().getReference();



        toolbar = (Toolbar) findViewById(R.id.edit_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        countrySpinner = (Spinner) findViewById(R.id.countrySpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countryArrayList);

        countrySpinner.setAdapter(adapter);


      editUserName = (EditText) findViewById(R.id.edit_userName);
      editCity = (EditText) findViewById(R.id.edit_city);
      editMobile = (EditText) findViewById(R.id.edit_mobile);
      editProfileImage = (CircleImageView) findViewById(R.id.edit_profileimage);
        remove = (TextView) findViewById(R.id.main_remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveImage();
            }
        });
        updatePic = (TextView) findViewById(R.id.main_upDatePic);
        updatePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeImage();
            }
        });




        mProgressDialog = new ProgressDialog(this);
      mProgressDialog.setMessage("Saving Your Profile_model");
      mProgressDialog.setCanceledOnTouchOutside(false);

      save = (Button) findViewById(R.id.edit_save);
      save.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String getUsername = editUserName.getText().toString();
              String getCity = editCity.getText().toString();
              String getMobile = editMobile.getText().toString();
              String getCountry = countrySpinner.getSelectedItem().toString();



              //Progress Dialog starts
              mProgressDialog.show();

              // create hash map
              Map<String,Object> userMap = new HashMap<>();
              userMap.put("user_name",getUsername);
              userMap.put("city",getCity);
              userMap.put("mobile",getMobile);
              userMap.put("country",getCountry);
             // Get uID
                String uID = currentUser.getUid().toString();
                DatabaseReference userData = mUserData.child(uID);


              userData.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      Toast.makeText(EditProfile.this,"Saved Successfully",Toast.LENGTH_SHORT).show();
                      Intent profileIntent = new Intent(EditProfile.this,SettingsActivity.class);
                      startActivity(profileIntent);
                      finish();
                      mProgressDialog.dismiss();
                  }
              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile.this,"Error Saving Data",Toast.LENGTH_SHORT).show();
                      mProgressDialog.hide();
                  }
              });


          }
      });


    }



    @Override
    protected void onStart() {
        super.onStart();

        // Get Current User
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final String uID = currentUser.getUid().toString();

        DatabaseReference userData = mUserData.child(uID);
        userData.keepSynced(true);
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot csnapshot:dataSnapshot.getChildren()){
                        final Profile_model profileModel = dataSnapshot.getValue(Profile_model.class);
                        editCity.setText(profileModel.getCity());
                        editMobile.setText(profileModel.getMobile());
                        editUserName.setText(profileModel.getUser_name());
                        countrySpinner.setSelection(countryArrayList.indexOf(profileModel.getCountry()));
                        Picasso.with(EditProfile.this).load(profileModel.getThumb_profile_image())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.profileimg).into(editProfileImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(EditProfile.this).load(profileModel.getThumb_profile_image()).placeholder(R.drawable.profileimg).into(editProfileImage);

                            }
                        });

                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void ChangeImage(){

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent,GALLERY_PICK);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri resultUri = data.getData();

            CropImage.activity(resultUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500,500)
                    .start(this);
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                // Progress dialog
                mProgressDialog = new ProgressDialog(EditProfile.this);
                mProgressDialog.setMessage("Updating Profile Pic");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();


                Uri resultUri = result.getUri();
                editProfileImage.setImageURI(resultUri);

                final File thumb_filePath = new File(resultUri.getPath());
                // get uID
                final String uID = currentUser.getUid().toString();


                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(EditProfile.this)
                                        .setMaxHeight(200)
                                        .setMaxWidth(200)
                                        .setQuality(75)
                                        .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    final byte[] thumb_byte = baos.toByteArray();



                StorageReference path = mStorageRef.child("bolly_quiz_profile_picture").child( uID+ ".jpg");
                final StorageReference thumb_Path = mStorageRef.child("bolly_quiz_profile_picture").child("thumb_profile_image").child(uID + ".jpg");


                path.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){


                            // uploading downloadUrl to our database
                            final String downloadUrl = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_Path.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    if(thumb_task.isSuccessful()){

                                        String thumb_downaloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                                        Map<String,Object> userMap = new HashMap<>();
                                        userMap.put("profile_image",downloadUrl);
                                        userMap.put("thumb_profile_image",thumb_downaloadUrl);

                                        DatabaseReference userData = mUserData.child(uID);
                                        userData.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                mProgressDialog.dismiss();
                                                Toast.makeText(EditProfile.this, "Profile Pic Uploaded", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                    else {
                                        mProgressDialog.hide();
                                        Toast.makeText(EditProfile.this, "Error Updating Profile Pic", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });





                        }
                        else {
                            mProgressDialog.hide();
                            Toast.makeText(EditProfile.this, "Error Updating Profile_model Pic", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(EditProfile.this, "Error Updating Profile Pic", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void RemoveImage(){
        mProgressDialog = new ProgressDialog(EditProfile.this);
        mProgressDialog.setMessage("Updating Profile Pic");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        editProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.profileimg));

        // get uID
        final String uID = currentUser.getUid().toString();
        DatabaseReference userData = mUserData.child(uID);
        userData.child("profile_image").setValue("https://firebasestorage.googleapis.com/v0/b/dreamproject-a29ff.appspot.com/o/bolly_quiz_profile_picture%2Fprofileimg.png?alt=media&token=24506611-e60a-4181-8386-c0e88f2251b8").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                mProgressDialog.dismiss();
                Toast.makeText(EditProfile.this, "Profile Pic Removed", Toast.LENGTH_SHORT).show();
            }
        });


    }
}











