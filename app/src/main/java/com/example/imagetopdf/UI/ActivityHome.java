package com.example.imagetopdf.UI;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.example.imagetopdf.KEYS;
import com.example.imagetopdf.R;
import com.example.imagetopdf.Tools;
import com.example.imagetopdf.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityHome extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ActivityHome";
    private ActivityHomeBinding activityHomeBinding;
    private TextView headerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityHomeBinding.getRoot());

        initHeader();
        initNavAndToolbar();


    }

    private void initHeader() {
        View header = activityHomeBinding.navViewActivityHome.getHeaderView(0);
        headerName = header.findViewById(R.id.header_name);
        headerName.setText(Tools.getPref(KEYS.USER_NAME, "User Name"));
    }

    void initNavAndToolbar() {
        setTitle(null);
        setSupportActionBar(activityHomeBinding.toolbarActivityMain);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, activityHomeBinding.drwerlayoutActivityMain,
                activityHomeBinding.toolbarActivityMain,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityHomeBinding.drwerlayoutActivityMain.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.homemenu, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder menuBuilder=(MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
    }
}