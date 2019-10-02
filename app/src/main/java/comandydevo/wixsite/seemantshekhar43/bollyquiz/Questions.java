package comandydevo.wixsite.seemantshekhar43.bollyquiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Questions extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 1000; // 1 sec
    final static long TIMEOUT = 10000; // 10 sec
    int progressValue = 0;

    CountDownTimer mcountDown;

    Handler setDelay;
    Runnable startDelay;

    // Firebase Stuffs
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    //Database Stuffs
    FirebaseDatabase database;
    DatabaseReference databaseRef;

    ProgressBar progressBar;
    ImageView i_question_image;
    CircleImageView user_thumb;
    Button option_a, option_b, option_c, option_d, yes, no;
    TextView current_score, user_name, time_left, current_status, i_question_text, question_text;

    int index = 0, score = 0, thisQuestion = 0, totalQuestions, correctAnswers = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);


        totalQuestions = 6;

        Bundle bundle = getIntent().getExtras();
        final String category_name = bundle.getString("category_name");
        final String id = bundle.getString("id");
        final String level = bundle.getString("level");


        //Database Stuffs
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        //Views
        i_question_image = (ImageView) findViewById(R.id.i_question_image);
        user_thumb = (CircleImageView) findViewById(R.id.user_thumb);
        current_score = (TextView) findViewById(R.id.current_score);
        time_left = (TextView) findViewById(R.id.time_left);
        current_status = (TextView) findViewById(R.id.current_status);
        i_question_text = (TextView) findViewById(R.id.i_question_text);
        question_text = (TextView) findViewById(R.id.question_text);
        user_name = (TextView) findViewById(R.id.user_name);
        option_a = (Button) findViewById(R.id.option_a);
        option_b = (Button) findViewById(R.id.option_b);
        option_c = (Button) findViewById(R.id.option_c);
        option_d = (Button) findViewById(R.id.option_d);
        progressBar = (ProgressBar) findViewById(R.id.time_progress);
        progressBar.setMax(9);


        option_a.setOnClickListener(this);
        option_b.setOnClickListener(this);
        option_c.setOnClickListener(this);
        option_d.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mcountDown.cancel();

        if (index < totalQuestions)  //still have question in list
        {
            Button clickedButton = (Button) view;

            option_a.setEnabled(false);
            option_b.setEnabled(false);
            option_c.setEnabled(false);
            option_d.setEnabled(false);


            if (clickedButton.getText().equals(Common.questionsList.get(index).getCorrect_answer())) {
                //Tapped Answer Was Correct
                clickedButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                score += 10;
                correctAnswers++;

                setDelay = new Handler();
                startDelay = new Runnable() {
                    @Override
                    public void run() {
                        showQuestions(++index); // next question
                    }
                };
                setDelay.postDelayed(startDelay, 500);

            } else {
                // Tapped Answer Was Wrong
                clickedButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

                if (option_a.getText().equals(Common.questionsList.get(index).getCorrect_answer())) {
                    option_a.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                }
                ;
                if (option_b.getText().equals(Common.questionsList.get(index).getCorrect_answer())) {
                    option_b.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                }
                ;
                if (option_c.getText().equals(Common.questionsList.get(index).getCorrect_answer())) {
                    option_c.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                }
                ;
                if (option_d.getText().equals(Common.questionsList.get(index).getCorrect_answer())) {
                    option_d.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                }
                ;

                setDelay = new Handler();
                startDelay = new Runnable() {
                    @Override
                    public void run() {
                        showQuestions(++index); // next question
                    }
                };
                setDelay.postDelayed(startDelay, 800);

            }
            current_score.setText(String.format("%d", score));

        }

    }

    private void showQuestions(int i) {

        if (index < totalQuestions) {
            thisQuestion++;
            current_status.setText(String.format("%d / %d", thisQuestion, totalQuestions));
            progressBar.setProgress(0);
            progressValue = 0;

            option_a.setEnabled(true);
            option_b.setEnabled(true);
            option_c.setEnabled(true);
            option_d.setEnabled(true);

            option_b.getBackground().clearColorFilter();
            option_a.getBackground().clearColorFilter();
            option_c.getBackground().clearColorFilter();
            option_d.getBackground().clearColorFilter();

            if (Common.questionsList.get(index).getContains_image().equals("1")) {
                // Question contains image
                i_question_text.setText(Common.questionsList.get(index).getText());
                Picasso.with(getBaseContext())
                        .load(Common.questionsList.get(index).getImage_url())
                        .into(i_question_image);
                question_text.setVisibility(View.INVISIBLE);
                i_question_text.setVisibility(View.VISIBLE);
                i_question_image.setVisibility(View.VISIBLE);
            } else {
                question_text.setText(Common.questionsList.get(index).getText());
                question_text.setVisibility(View.VISIBLE);
                i_question_text.setVisibility(View.INVISIBLE);
                i_question_image.setVisibility(View.INVISIBLE);
            }
            option_a.setText(Common.questionsList.get(index).getAnswer_a());
            option_b.setText(Common.questionsList.get(index).getAnswer_b());
            option_c.setText(Common.questionsList.get(index).getAnswer_c());
            option_d.setText(Common.questionsList.get(index).getAnswer_d());

            mcountDown.start();
//            time_left.setText(String.format("%d",progressValue));

        } else {
            // If this is the final question
            Bundle bundle = getIntent().getExtras();
            final String random = bundle.getString("random");
            final String id = bundle.getString("id");
            final String level = bundle.getString("level");


            Intent resultIntent = new Intent(Questions.this, Result.class);
            Bundle bundle1 = new Bundle();
            bundle1.putInt("final_score", score);
            bundle1.putInt("correctAnswers", correctAnswers);
            bundle1.putInt("totalQuestions", totalQuestions);
            bundle1.putString("random", random);
            bundle1.putString("id", id);
            bundle1.putString("level", level);
            resultIntent.putExtras(bundle1);
            startActivity(resultIntent);
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


        mcountDown = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long minisec) {

                time_left.setText(String.valueOf(minisec / 1000));
                progressBar.setProgress(progressValue);
                progressValue++;


            }

            @Override
            public void onFinish() {
                mcountDown.cancel();
                time_left.setText("0");
                showQuestions(++index);
            }
        };
        showQuestions(index);//edited from (++index)
    }

    @Override
    protected void onStart() {
        super.onStart();

        String uID = currentUser.getUid().toString();

        databaseRef.child("Users").child(uID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot cSnapshot : dataSnapshot.getChildren()) {
                        final Profile_model profileModel = dataSnapshot.getValue(Profile_model.class);
                        user_name.setText(profileModel.getUser_name());


                        Picasso.with(Questions.this).load(profileModel.getThumb_profile_image())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.profileimg).into(user_thumb, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(Questions.this).load(profileModel.getThumb_profile_image()).placeholder(R.drawable.profileimg).into(user_thumb);
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




    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.surrender, null);
        yes = (Button) mView.findViewById(R.id.surrender_yes);
        no = (Button) mView.findViewById(R.id.surrender_no);

        builder.setView(mView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Questions.super.onBackPressed();
                dialog.dismiss();
                mcountDown.cancel();
                Intent mainIntent = new Intent(Questions.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
                finish();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               dialog.dismiss();
            }
        });


    }
}

