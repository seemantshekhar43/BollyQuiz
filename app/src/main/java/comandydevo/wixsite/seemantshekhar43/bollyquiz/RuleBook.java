package comandydevo.wixsite.seemantshekhar43.bollyquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class RuleBook extends AppCompatActivity {
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_book);

        // Views
        mToolbar = (Toolbar) findViewById(R.id.rule_book_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Rule Book");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
