package io.syslogic.github.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import io.syslogic.github.R;
import io.syslogic.github.model.SpinnerItem;
import io.syslogic.github.view.LanguagesAdapter;
import io.syslogic.github.view.RepositoriesAdapter;
import io.syslogic.github.view.RepositoriesLinearView;

public class MainActivity extends AppCompatActivity {

    private RepositoriesLinearView mRecyclerView;

    private AppCompatSpinner mSpinnerLanguage;

    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        this.mSpinnerLanguage = findViewById(R.id.spinner_language);
        this.mSpinnerLanguage.setAdapter(new LanguagesAdapter(this));
        this.mSpinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            int count = 0;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(count > 0) {
                    SpinnerItem item = (SpinnerItem) view.getTag();
                    mRecyclerView.setQuery(item.getValue());
                    mRecyclerView.clearAdapter();

                }
                count++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        this.mRecyclerView = findViewById(R.id.recyclerview_repositories);
        if(this.mRecyclerView.getAdapter() == null) {
            this.mRecyclerView.setAdapter(new RepositoriesAdapter(this, this.currentPage));
        }
    }
}
