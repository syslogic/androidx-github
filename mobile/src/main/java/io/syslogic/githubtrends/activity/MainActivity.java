package io.syslogic.githubtrends.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import io.syslogic.githubtrends.R;
import io.syslogic.githubtrends.view.RepositoriesAdapter;

public class MainActivity extends AppCompatActivity {

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
