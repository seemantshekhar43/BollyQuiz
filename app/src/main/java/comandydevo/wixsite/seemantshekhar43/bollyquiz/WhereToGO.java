package comandydevo.wixsite.seemantshekhar43.bollyquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WhereToGO extends AppCompatActivity {
    Button logIn, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_where_to_go);
        logIn = (Button) findViewById(R.id.whereToLogin);
        signUp = (Button) findViewById(R.id.whereToSignUp);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLogIn();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTOSignUp();

            }
        });


    }
    private void sendToLogIn() {
        Intent logInIntent = new Intent(WhereToGO.this, LogIn.class);
        logInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logInIntent);
        finish();
    }
    private void sendTOSignUp(){
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
    }
}
