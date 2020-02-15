package com.jv.hnreader.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.jv.hnreader.R;
import com.jv.hnreader.ui.screens.search.SearchFragment;
import com.jv.hnreader.ui.screens.stories.StoryListFragment;
import com.jv.hnreader.ui.screens.stories.StoryListFragment.OnSearchClickListener;

public class MainActivity extends AppCompatActivity implements OnSearchClickListener {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null){
            createFragment();
        }
    }

    private void createFragment() {
        Log.e(TAG, "createFragment");
        StoryListFragment storyListfragment = new StoryListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, storyListfragment)
                .commit();
    }

    public void onSearchClick(){
        SearchFragment searchFragment = new SearchFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .addToBackStack(null)
                .commit();
    }
}
