package comandydevo.wixsite.seemantshekhar43.bollyquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

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

public class Leaderboard extends AppCompatActivity {
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private CircleImageView profile_image;
    private TextView masters_in, xp, star_points, star_quizzes, struggler_points, struggler_quizzes, superstar_points, superstar_quizzes,total_points, total_quizzes;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        toolbar = (Toolbar) findViewById(R.id.leaderboard_toolabar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profile_image = (CircleImageView) findViewById(R.id.leaderboard_user_image);
        masters_in = (TextView) findViewById(R.id.leaderboard_masters_in);
        xp = (TextView) findViewById(R.id.leaderboard_xp);
        star_points = (TextView) findViewById(R.id.star_points);
        star_quizzes = (TextView) findViewById((R.id.star_quizzes));
        struggler_points = (TextView) findViewById(R.id.struggler_points);
        struggler_quizzes = (TextView) findViewById(R.id.struggler_quizzes);
        superstar_points = (TextView) findViewById(R.id.superstar_points);
        superstar_quizzes = (TextView) findViewById(R.id.superstar_quizzes);
        total_points = (TextView) findViewById(R.id.total_points);
        total_quizzes = (TextView) findViewById(R.id.total_quizzes);

         mDatabase = databaseReference.child("Users").child(currentUser.getUid());
         mDatabase.keepSynced(true);
         mDatabase.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 final String profile_url = dataSnapshot.child("thumb_profile_image").getValue().toString();
                 Picasso.with(Leaderboard.this).load(profile_url)
                         .networkPolicy(NetworkPolicy.OFFLINE)
                         .placeholder(R.drawable.profileimg).into(profile_image, new Callback() {
                     @Override
                     public void onSuccess() {

                     }

                     @Override
                     public void onError() {
                         Picasso.with(Leaderboard.this).load(profile_url).placeholder(R.drawable.profileimg).into(profile_image);
                     }
                 });


             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

        databaseReference.child("Achievements").child(currentUser.getUid()).keepSynced(true);
        databaseReference.child("Achievements").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot csnapshot : dataSnapshot.getChildren()) {
                        Achievements_Model achievements_model = dataSnapshot.getValue(Achievements_Model.class);

                        masters_in.setText(achievements_model.getMasters_in());
                        xp.setText(String.format("%d",achievements_model.getXp()));
                        star_points.setText(String.format("%d",achievements_model.getStar_points()));
                        star_quizzes.setText(String.format("%d",achievements_model.getStar_quizzes()));
                        struggler_points.setText(String.format("%d",achievements_model.getStruggler_points()));
                        struggler_quizzes.setText(String.format("%d",achievements_model.getStruggler_quizzes()));
                        superstar_points.setText(String.format("%d",achievements_model.getSuperstar_points()));
                        superstar_quizzes.setText(String.format("%d",achievements_model.getSuperstar_quizzes()));
                        total_points.setText(String.format("%d",achievements_model.getTotal_points()));
                        total_quizzes.setText(String.format("%d",achievements_model.getTotal_quizzes()));

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}