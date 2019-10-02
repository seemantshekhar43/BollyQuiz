package comandydevo.wixsite.seemantshekhar43.bollyquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private TextView userName,  userAddress, dateJoined, most_played, xp, points, quizzes;
    private CircleImageView userProfileImage;
    private Button leaderboard_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        toolbar = (Toolbar) findViewById(R.id.activity_profile_view_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        userName = (TextView) findViewById(R.id.userName);
        userAddress = (TextView) findViewById(R.id.userAddress);
        userProfileImage = (CircleImageView) findViewById(R.id.userProfileImage);
        dateJoined = (TextView) findViewById(R.id.dateJoined);
        most_played = (TextView) findViewById(R.id.settings_most_played);
        xp = (TextView) findViewById(R.id.settings_xp);
        points = (TextView) findViewById(R.id.settings_points);
        quizzes =(TextView) findViewById(R.id.settings_quizzes);
        leaderboard_btn = (Button) findViewById(R.id.leaderboard_btn);
        leaderboard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent leaderboardIntent = new Intent(SettingsActivity.this, Leaderboard.class);
                startActivity(leaderboardIntent);
            }
        });

       // Getting the current user and Uid
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_user_Uid = mCurrentUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_Uid);
        mDatabase.keepSynced(true);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for(DataSnapshot cSnapshot: dataSnapshot.getChildren())
                    {
                        final Profile_model profileModel = dataSnapshot.getValue(Profile_model.class);
                        userName.setText(profileModel.getUser_name());
                        userAddress.setText(profileModel.getCity() + " , " + profileModel.getCountry());
                        dateJoined.setText(String.format("Since %s",profileModel.getDate_joined()));

                        Intent leaderboardIntent = new Intent(SettingsActivity.this,Leaderboard.class);
                        leaderboardIntent.putExtra("profile_url",profileModel.getThumb_profile_image());

                        Picasso.with(SettingsActivity.this).load(profileModel.getThumb_profile_image())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.profileimg).into(userProfileImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(SettingsActivity.this).load(profileModel.getThumb_profile_image()).placeholder(R.drawable.profileimg).into(userProfileImage);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Achievements").child(current_user_Uid);
        mDatabase1.keepSynced(true);
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot csnapshot : dataSnapshot.getChildren()) {
                        Achievements_Model achievements_model = dataSnapshot.getValue(Achievements_Model.class);
                        most_played.setText(String.format("%s | ",achievements_model.getMost_played()));
                        xp.setText(String.format("%d",achievements_model.getXp()));
                        points.setText(String.format("%d",achievements_model.getTotal_points()));
                        quizzes.setText(String.format("%d",achievements_model.getTotal_quizzes()));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.settings_activity_menu,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_Edit:
                 editMenu();
                 return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editMenu() {
        Intent editProfileIntent = new Intent(this,EditProfile.class);
             startActivity(editProfileIntent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(mainIntent);
        finish();
    }
}
