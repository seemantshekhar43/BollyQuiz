package comandydevo.wixsite.seemantshekhar43.bollyquiz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Result extends AppCompatActivity {

    // Firebase Stuffs
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    //Database Stuffs
    FirebaseDatabase database;
    DatabaseReference databaseRef;

    Toolbar mtoolbar;
    TextView result_user_name, result_stardom, result_class, result_genre, result_points, result_status;
    Button result_replay;
    CircleImageView result_user_thumb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Database Stuffs
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();


        // Views
        result_user_name = (TextView) findViewById(R.id.result_user_name);
        result_stardom = (TextView) findViewById(R.id.result_stardom);
        result_class = (TextView) findViewById(R.id.result_class);
        result_genre = (TextView) findViewById(R.id.result_genre);
        result_points = (TextView) findViewById(R.id.result_points);
        result_status = (TextView) findViewById(R.id.result_status);

        result_user_thumb = (CircleImageView) findViewById(R.id.result_user_thumb);
        result_replay = (Button) findViewById(R.id.result_replay);


        mtoolbar = (Toolbar) findViewById(R.id.result_page_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Scorecard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            final String random = bundle.getString("random");
            final String id = bundle.getString("id");
            final String userName = bundle.getString("userName");
            final String level = bundle.getString("level");
            final int final_score = bundle.getInt("final_score");
            final int correctAnswers = bundle.getInt("correctAnswers");
            final int totalQuestions = bundle.getInt("totalQuestions");



            result_stardom.setText(String.format("Stardom: %s", random));
            result_genre.setText(String.format("Genre: %s", id));
            result_class.setText(String.format("Class: %s", level));
            result_user_name.setText(userName);
            result_points.setText(String.format("Final Score: %d",final_score));
            result_status.setText(String.format("Correct Answers: %d/%d",correctAnswers,totalQuestions));

            UpdateAchievents();


        }


        result_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(Result.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
                finish();
            }
        });


    }
        @Override
        protected void onStart() {
            super.onStart();

            String uID = currentUser.getUid().toString();

            databaseRef.child("Users").child(uID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()){
                        for(DataSnapshot cSnapshot: dataSnapshot.getChildren())
                        {
                            final Profile_model profileModel = dataSnapshot.getValue(Profile_model.class);
                            result_user_name.setText(profileModel.getUser_name());


                            Picasso.with(Result.this).load(profileModel.getThumb_profile_image())
                                    .networkPolicy(NetworkPolicy.OFFLINE)
                                    .placeholder(R.drawable.profileimg).into(result_user_thumb, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(Result.this).load(profileModel.getThumb_profile_image()).placeholder(R.drawable.profileimg).into(result_user_thumb);
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
        super.onBackPressed();

        Intent mainIntent = new Intent(Result.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(mainIntent);
        finish();
    }

   //                 ----------------Achivements--------------------

    public void UpdateAchievents(){

        databaseRef.child("Achievements").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot csnapshot:dataSnapshot.getChildren()){
                        Achievements_Model achievements_model = dataSnapshot.getValue(Achievements_Model.class);

                        Bundle bundle = getIntent().getExtras();

                            final int final_score = bundle.getInt("final_score");


                        if( Common.classID == "star"){
                            int points = achievements_model.getStar_points() ;
                            int total_points = achievements_model.getTotal_points();
                            int total_quizzes = achievements_model.getTotal_quizzes() + 1;
                            int quizzes = achievements_model.getStar_quizzes() + 1 ;
                            int xp = achievements_model.getXp() + 4;
                            achievements_model.setXp(xp);
                            achievements_model.setStar_points(points + final_score );
                            achievements_model.setStar_quizzes(quizzes);
                            achievements_model.setTotal_points(total_points+ final_score);
                            achievements_model.setTotal_quizzes(total_quizzes);

                        }
                         else if( Common.classID == "struggler"){
                            int points = achievements_model.getStruggler_points() ;
                            int total_points = achievements_model.getTotal_points();
                            int total_quizzes = achievements_model.getTotal_quizzes() + 1 ;
                            int quizzes = achievements_model.getStruggler_quizzes() + 1;
                            int xp = achievements_model.getXp() + 2;
                            achievements_model.setXp(xp);
                            achievements_model.setStruggler_points(points + final_score );
                            achievements_model.setStruggler_quizzes(quizzes);
                            achievements_model.setTotal_points(total_points+ final_score);
                            achievements_model.setTotal_quizzes(total_quizzes);

                        }
                        else if( Common.classID == "superstar"){
                            int points = achievements_model.getSuperstar_points() ;
                            int total_points = achievements_model.getTotal_points();
                            int total_quizzes = achievements_model.getTotal_quizzes() + 1;
                            int quizzes = achievements_model.getSuperstar_quizzes() + 1;
                            int xp = achievements_model.getXp() + 5;
                            achievements_model.setXp(xp);
                            achievements_model.setSuperstar_points(points + final_score );
                            achievements_model.setSuperstar_quizzes(quizzes);
                            achievements_model.setTotal_points(total_points+ final_score);
                            achievements_model.setTotal_quizzes(total_quizzes);

                        }

                        int mastersIn = Math.max(achievements_model.getStar_points(),Math.max(achievements_model.getStruggler_points(),achievements_model.getSuperstar_points()));

                        if (mastersIn ==achievements_model.getStar_points()){
                            achievements_model.setMasters_in("Star");
                        }
                        if (mastersIn ==achievements_model.getStruggler_points()){
                            achievements_model.setMasters_in("Struggler");
                        }
                        if (mastersIn ==achievements_model.getSuperstar_points()){
                            achievements_model.setMasters_in("SuperStar");
                        }

                        int mostPlayed = Math.max(achievements_model.getStar_quizzes(),Math.max(achievements_model.getStruggler_quizzes(),achievements_model.getSuperstar_quizzes()));

                        if (mostPlayed ==achievements_model.getStar_quizzes()){
                            achievements_model.setMost_played("Star");
                        }
                        if (mostPlayed ==achievements_model.getStruggler_quizzes()){
                            achievements_model.setMost_played("Struggler");
                        }
                        if (mostPlayed ==achievements_model.getSuperstar_quizzes()){
                            achievements_model.setMost_played("SuperStar");
                        }
                        if (Common.qwe ==1){
                            UploadResult(achievements_model.getStar_points(),achievements_model.getStar_quizzes(),achievements_model.getStruggler_points(),achievements_model.getStruggler_quizzes(),achievements_model.getSuperstar_points(),achievements_model.getSuperstar_quizzes(),achievements_model.getTotal_points(),achievements_model.getTotal_quizzes(),achievements_model.getXp(),achievements_model.getMost_played(),achievements_model.getMasters_in());

                        }

                    }
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

        private void UploadResult(int star_points, int star_quizzes, int struggler_points, int struggler_quizzes, int superstar_points, int superstar_quizzes, int total_points, int total_quizzes, int xp, String most_played, String masters_in){
        Common.qwe = 2;
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("star_points", star_points);
        userMap.put("star_quizzes", star_quizzes);
        userMap.put("struggler_points", struggler_points);
        userMap.put("struggler_quizzes",struggler_quizzes);
        userMap.put("superstar_points", superstar_points);
        userMap.put("superstar_quizzes", superstar_quizzes);
        userMap.put("total_points", total_points);
        userMap.put("total_quizzes", total_quizzes);
        userMap.put("xp",xp);
        userMap.put("masters_in", masters_in);
        userMap.put("most_played",most_played);

        databaseRef.child("Achievements").child(currentUser.getUid()).updateChildren(userMap).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Result.this,"Achievemts Were Not Uploaded",Toast.LENGTH_SHORT).show();
            }


        });
    }


    }

