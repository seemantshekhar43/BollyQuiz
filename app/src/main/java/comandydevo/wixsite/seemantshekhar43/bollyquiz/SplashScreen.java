package comandydevo.wixsite.seemantshekhar43.bollyquiz;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    Handler setDelay;
    Runnable startDelay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        setDelay = new Handler();
        startDelay = new Runnable() {
            @Override
            public void run() {

                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    sendTOMain();
                }
                else{
                    sendToWhereToGo();
                }

            }
        };
        setDelay.postDelayed(startDelay,2500);

    }

    private void sendToWhereToGo() {
        Intent WhereToGOIntent = new Intent(SplashScreen.this, WhereToGO.class);
        WhereToGOIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(WhereToGOIntent);
        finish();
    }

    private void sendTOMain(){
        Intent mainIntent = new Intent(SplashScreen.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);

        finish();
    }

}
