package comandydevo.wixsite.seemantshekhar43.bollyquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.firebase.ui.auth.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.file.FileSystemAlreadyExistsException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

   private EditText email, password, userName,  confirmPassword;
   private Button button;
   private Button google_sign_up;
   private Toolbar toolbar;
   public ProgressDialog progressDialog;
   private String error ="";
   GoogleSignInClient mGoogleSignInClient;

   //Firebase stuffs
    FirebaseAuth mAuth;

  //Database Stuffs
  FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
  DatabaseReference rootref = mDatabase.getReference();
  DatabaseReference userData;
    private final static int RC_SIGN_IN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName = (EditText) findViewById(R.id.userName);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        toolbar = (Toolbar) findViewById(R.id.register_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up");
        progressDialog.setCanceledOnTouchOutside(false);

        google_sign_up = (Button) findViewById(R.id.google_sign_up);
        google_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                signIn();

            }
        });


        //Firebase stuffs
        mAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);




        button = (Button) findViewById(R.id.signUp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //converting text input into String


                final String myEmail = email.getText().toString();
                final String myPassword = password.getText().toString();
                final String myUserName = userName.getText().toString();
                final String myConfirmPassword = confirmPassword.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (!TextUtils.isEmpty(myEmail)&&myEmail.matches(emailPattern) && !TextUtils.isEmpty(myPassword) && !TextUtils.isEmpty(myUserName) && !TextUtils.isEmpty(myConfirmPassword)) {
                    if (!myPassword.equals(myConfirmPassword)) {
                        Toast.makeText(Register.this, "Passwords Don't Match.", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.show();
                        onRegister(myUserName,myEmail,myPassword);


                    }
                } else {
                    Toast.makeText(Register.this, "Please Fill the Credentials Properly", Toast.LENGTH_SHORT).show();

                }


            }


        });
    }

    private void onRegister(final String username, String email, String password){



        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                           // getting user and its Uid
                         FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uID = current_user.getUid();

                            // Get date
                            String currentDate = DateFormat.getDateInstance().format(new Date());

                            //Stroring info to database
                            userData = rootref.child("Users").child(uID);

                            //Creating a hashmap
                            HashMap<String,String> userMap = new HashMap<>();
                            userMap.put("user_name",username);
                            userMap.put("profile_image","https://firebasestorage.googleapis.com/v0/b/dreamproject-a29ff.appspot.com/o/bolly_quiz_profile_picture%2Fprofileimg.png?alt=media&token=24506611-e60a-4181-8386-c0e88f2251b8");
                            userMap.put("city","");
                            userMap.put("mobile","+91");
                            userMap.put("country","India");
                            userMap.put("date_joined",currentDate);
                            userMap.put("thumb_profile_image","https://firebasestorage.googleapis.com/v0/b/dreamproject-a29ff.appspot.com/o/bolly_quiz_profile_picture%2Fprofileimg.png?alt=media&token=24506611-e60a-4181-8386-c0e88f2251b8");
                            userData.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                    String uID = current_user.getUid();

                                    HashMap<String,Integer> userMap1 = new HashMap<>();
                                    userMap1.put("star_points", 0);
                                    userMap1.put("star_quizzes",  0);
                                    userMap1.put("struggler_points",  0);
                                    userMap1.put("struggler_quizzes", 0);
                                    userMap1.put("superstar_points",  0);
                                    userMap1.put("superstar_quizzes",  0);
                                    userMap1.put("total_points", 0);
                                    userMap1.put("total_quizzes", 0);
                                    userMap1.put("xp", 0);
                                    rootref.child("Achievements").child(uID).setValue(userMap1);

                                    progressDialog.dismiss();

                                    // Sign in success,take to the next screen
                                    sendToMain();

                                }
                            });



                        } else {

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.hide();

                if( e instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(Register.this, "This Email Id Is Already Registered", Toast.LENGTH_SHORT).show();
                }
                if( e instanceof FirebaseAuthWeakPasswordException){
                    Toast.makeText(Register.this, "Password Should Contain Minimum 8 Characers", Toast.LENGTH_SHORT).show();
                }
//                if ( e instanceof FirebaseAuthInvalidCredentialsException){
//                    Toast.makeText(Register.this,"Email is not Correct",Toast.LENGTH_SHORT).show();
//                }

            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.i("Tag", "Google sign in failed", e);
                progressDialog.hide();
                Toast.makeText(Register.this,"Google Sign In Failed",Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();


                            // getting user and its Uid
                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uID = current_user.getUid();

                            // Get date
                            String currentDate = DateFormat.getDateInstance().format(new Date());

                            //Stroring info to database
                            userData = rootref.child("Users").child(uID);

                            //Creating a hashmap
                            HashMap<String,String> userMap = new HashMap<>();
                            userMap.put("user_name","Not Set Yet");
                            userMap.put("profile_image","https://firebasestorage.googleapis.com/v0/b/dreamproject-a29ff.appspot.com/o/bolly_quiz_profile_picture%2Fprofileimg.png?alt=media&token=24506611-e60a-4181-8386-c0e88f2251b8");
                            userMap.put("city","Not Set Yet");
                            userMap.put("mobile","+91");
                            userMap.put("country","India");
                            userMap.put("date_joined",currentDate);
                            userMap.put("thumb_profile_image","https://firebasestorage.googleapis.com/v0/b/dreamproject-a29ff.appspot.com/o/bolly_quiz_profile_picture%2Fprofileimg.png?alt=media&token=24506611-e60a-4181-8386-c0e88f2251b8");
                            userData.setValue(userMap);


                            HashMap<String,Integer> userMap1 = new HashMap<>();
                            userMap1.put("star_points", 0);
                            userMap1.put("star_quizzes",  0);
                            userMap1.put("struggler_points",  0);
                            userMap1.put("struggler_quizzes", 0);
                            userMap1.put("superstar_points",  0);
                            userMap1.put("superstar_quizzes",  0);
                            userMap1.put("total_points", 0);
                            userMap1.put("total_quizzes", 0);
                            userMap1.put("xp", 0);
                            rootref.child("Achievements").child(uID).setValue(userMap1);

                            sendToMain();


                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.hide();
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    private void sendToMain(){
        Intent mainIntent =new Intent(Register.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

}
