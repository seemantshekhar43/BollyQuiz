package comandydevo.wixsite.seemantshekhar43.bollyquiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.w3c.dom.Text;

public class LogIn extends AppCompatActivity {


    // creating variables
   private EditText email, password;
   private Button signinButton;
   private TextView forgot_password;
   public ProgressDialog progressDialog;
   private Button reset_password_btn;
   private EditText registered_email;
   SignInButton button;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        forgot_password = (TextView) findViewById(R.id.forgot_password);

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Signing In");
        progressDialog.setCanceledOnTouchOutside(false);

        signinButton = (Button) findViewById(R.id.sign_in_btn);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String myEmail = email.getText().toString();
                final String myPassword = password.getText().toString();
                if(!TextUtils.isEmpty(myEmail)&& !TextUtils.isEmpty(myPassword)){


                    progressDialog.show();

                     onLogIn(myEmail,myPassword);
                }
                else {
                    if (TextUtils.isEmpty(myEmail)&&!TextUtils.isEmpty(myPassword)){
                        Toast.makeText(LogIn.this,"Please fill the Email",Toast.LENGTH_SHORT).show();
                    }
                    if (TextUtils.isEmpty(myPassword)&&!TextUtils.isEmpty(myEmail)){
                        Toast.makeText(LogIn.this,"Please fill the Password",Toast.LENGTH_SHORT).show();
                   }
                    if(TextUtils.isEmpty(myEmail)&&TextUtils.isEmpty(myPassword)){
                        Toast.makeText(LogIn.this,"Please fill the Credentials",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });



        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // calling the button
        button = (SignInButton) findViewById(R.id.googleButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                signIn();
            }
        });


        // reset password
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ResetPasswordDialog();
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
                Toast.makeText(LogIn.this,"Google Sign In Failed",Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           progressDialog.dismiss();
                            sendTOMain();
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.hide();
                            Toast.makeText(LogIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    //logIn method
    public void onLogIn(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();

                            sendTOMain();

                            // lets get the user id i.e. UID
                            String userID = user.getUid().toString();
                            Log.i("UID","USER ID: " + userID);
                        } else {
                            // If sign in fails, display a message to the user.




                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.hide();
                if( e instanceof FirebaseAuthInvalidUserException){
                    Toast.makeText(LogIn.this, "This User Not Found , Create A New Account", Toast.LENGTH_SHORT).show();
                }
                if( e instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(LogIn.this, "The Password Is Invalid, Please Try Valid Password", Toast.LENGTH_SHORT).show();
                }
                if(e instanceof FirebaseNetworkException){
                    Toast.makeText(LogIn.this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }







    public void SignUp (View view){
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
    }

    private void sendTOMain(){
        Intent mainIntent = new Intent(LogIn.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);

        finish();
    }

    public void ResetPasswordDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LogIn.this);
        View mView = getLayoutInflater().inflate(R.layout.reset_password_dialog, null);
        registered_email = (EditText) mView.findViewById(R.id.registered_email);
        reset_password_btn = (Button) mView.findViewById(R.id.reset_password_btn);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        reset_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.sendPasswordResetEmail(registered_email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LogIn.this,"Reset Password link has been sent to " + registered_email.getText().toString(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                }
                            }
                        });
            }
        });

    }

}
