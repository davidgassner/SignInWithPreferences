package com.example.android.data;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.data.model.DataItem;
import com.example.android.data.model.SampleDataProvider;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.android.data.util.Constants.MY_GLOBAL_PREFS;
import static com.example.android.data.util.Constants.PASSWORD;
import static com.example.android.data.util.Constants.USER_NAME;

public class MainActivity extends AppCompatActivity {

    private static final int ACTION_LOGIN = 1001;
    private static final int REQUEST_LOGIN = 1002;
    private static final int ACTION_SETTINGS = 1003;
    private static final int REQUEST_PREFS_ACTIVITY = 1004;
    private List<DataItem> dataItemList = SampleDataProvider.dataItemList;
    private SharedPreferences settings;
    private OnSharedPreferenceChangeListener prefsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Collections.sort(dataItemList, new Comparator<DataItem>() {
            @Override
            public int compare(DataItem o1, DataItem o2) {
                return o1.getItemName().compareTo(o2.getItemName());
            }
        });

        prefsListener = new OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                Log.i("Prefs", "onSharedPreferenceChanged");
                displayData();
            }
        };

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.registerOnSharedPreferenceChangeListener(prefsListener);

        displayData();

    }

    private void displayData() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvItems);
        DataItemAdapter adapter = new DataItemAdapter(this, dataItemList);
        recyclerView.setAdapter(adapter);

        boolean displayGrid = settings.getBoolean(getString(R.string.pref_display_grid), false);
        Log.i("Prefs", "displayData: " + displayGrid);

        if (displayGrid) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, ACTION_LOGIN, 0, R.string.sign_in);
        menu.add(0, ACTION_SETTINGS, 0, R.string.settings);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case ACTION_LOGIN:
                Intent intent = new Intent(this, SigninActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
                return true;
            case ACTION_SETTINGS:
                Intent prefs_intent = new Intent(this, PrefsActivity.class);
                startActivity(prefs_intent);
                return true;
        }

        return false;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
            String userName = data.getStringExtra(USER_NAME);
            String password = data.getStringExtra(PASSWORD);

            final HashCode hashCode = Hashing.sha1().hashString(password, Charset.defaultCharset());

            SharedPreferences.Editor editor = getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE).edit();
//            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putString(USER_NAME, userName);
            editor.putString(PASSWORD, hashCode.toString());
            editor.apply();

            Toast.makeText(this, "Logged in as " + userName, Toast.LENGTH_SHORT).show();
        }
    }
}
