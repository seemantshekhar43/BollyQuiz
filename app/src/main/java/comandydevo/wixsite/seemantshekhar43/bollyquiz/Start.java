package comandydevo.wixsite.seemantshekhar43.bollyquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class Start extends AppCompatActivity {
    Toolbar mToolbar;
    Button play_button;
    TextView category_level, categoryId, how_to_play;

    //Firebase Database Stuffs
    FirebaseDatabase database;
    DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Bundle bundle = getIntent().getExtras();
        final String category_name = bundle.getString("category_name");
        final String id = bundle.getString("id");
        final String level = bundle.getString("level");


        mToolbar = (Toolbar) findViewById(R.id.start_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.start_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        play_button = (Button) findViewById(R.id.play_button);
        category_level = (TextView) findViewById(R.id.category_level);
        categoryId = (TextView) findViewById(R.id.category_id);
        how_to_play = (TextView) findViewById(R.id.how_to_play);

      // i think its not needed --->  Category_model category_model = new Category_model();
        categoryId.setText(id);
        category_level.setText( category_name + ": " + level);



        //Database Stuffs
        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions").child(category_name);


        loadQuestions(Common.categoryID);

        how_to_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ruleBookIntent = new Intent(Start.this, RuleBook.class);
                startActivity(ruleBookIntent);

            }
        });
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String random = category_name;
                Common.qwe =1;
                Intent questionsIntent = new Intent(Start.this, Questions.class );
                questionsIntent.putExtra("name",category_name);
                questionsIntent.putExtra("random",random);
                questionsIntent.putExtra("id",id);
                questionsIntent.putExtra("level",level);
                startActivity(questionsIntent);
                finish();
            }
        });
    }

    private void loadQuestions(String categoryID) {

        // clearing the list if any
        if (Common.questionsList.size()>0){
            Common.questionsList.clear();
        }

        questions.keepSynced(false);
        questions.orderByChild("category_id").equalTo(categoryID)
                .addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                        {
                            Questions_Model ques = postSnapshot.getValue(Questions_Model.class);
                            Common.questionsList.add(ques);

                            // Generate Random list
                            Collections.shuffle(Common.questionsList);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {



                    }
                });



    }

}
