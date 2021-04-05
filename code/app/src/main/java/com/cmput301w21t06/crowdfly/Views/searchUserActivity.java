package com.cmput301w21t06.crowdfly.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cmput301w21t06.crowdfly.Database.CrowdFlyListeners;
import com.cmput301w21t06.crowdfly.Database.UserController;
import com.cmput301w21t06.crowdfly.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import static com.cmput301w21t06.crowdfly.Models.LiveTextUpdater.handleChange;

/**
 * This class provides a view to allow users to look at the user profile of other users
 */
public class searchUserActivity extends AppCompatActivity implements CrowdFlyListeners.OnDoneGetIdsListener {
    private ListView idDisplay;
    private EditText searchBar;
    private ArrayAdapter<String> idAdapter;
    private ArrayList<String> defaultArray;
    private ArrayList<String> changedArray;
    private final String TAG = "COM.CMPUT301W21T06.CROWDFLY.EDITABLE";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        idDisplay = findViewById(R.id.idView);
        searchBar = findViewById(R.id.searchBar);
        defaultArray = new ArrayList<String>();
        changedArray = new ArrayList<String>();
        searchBar.addTextChangedListener(searchWatcher);
        UserController.getUsers(this);
        idDisplay.setOnItemClickListener(itemClickListener);

        drawerLayout = findViewById(R.id.drawer_layout_user_search);
        navigationView = findViewById(R.id.nav_view_user_search);
        toolbar = findViewById(R.id.toolbar_user_search);
        toolbar.setTitle("CrowdFly");

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
            }
        });

    }

    /**
     * This handles the set up once all ids have been pulled from the database
     * @param ids
     * This is the list of all ids currently registered
     */
    @Override
    public void onDoneGetIds(ArrayList<String> ids) {
        this.defaultArray = ids;
        this.changedArray = (ArrayList<String>) defaultArray.clone();
        idAdapter = new ArrayAdapter<String>(this,R.layout.general_content,changedArray);
        idDisplay.setAdapter(idAdapter);
        idAdapter.notifyDataSetChanged();
    }

    //Listeners not given javadoc
    TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String text = String.valueOf(charSequence);
            handleChange(changedArray,defaultArray,text);
            idAdapter.notifyDataSetChanged();


        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
            intent.putExtra(TAG,changedArray.get(i));
            startActivity(intent);
        }
    };

}