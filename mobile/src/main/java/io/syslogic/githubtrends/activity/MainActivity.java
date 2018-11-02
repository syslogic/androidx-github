package io.syslogic.githubtrends.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import io.syslogic.githubtrends.R;
import io.syslogic.githubtrends.view.RepositoriesAdapter;

public class MainActivity extends AppCompatActivity {

    /** {@link Log} Tag */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        this.mRecyclerView = findViewById(R.id.recyclerview_repositories);
        if(this.mRecyclerView.getAdapter() == null) {
            this.mRecyclerView.setAdapter(new RepositoriesAdapter(this, this.currentPage));
        }
    }
}
