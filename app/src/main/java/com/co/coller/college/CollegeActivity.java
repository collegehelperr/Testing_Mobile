package com.co.coller.college;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import com.co.coller.DashboardActivity;
import com.co.coller.EditProfilActivity;
import com.co.coller.ProfilActivity;
import com.co.coller.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CollegeActivity extends AppCompatActivity {

    BottomNavigationView botNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college);

        botNav = (BottomNavigationView) findViewById(R.id.bot_nav);
        botNav.setItemIconTintList(null);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NoteFragment()).commit();

        botNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch(item.getItemId()){
                    case R.id.nav_note:
                        item.setIcon(R.drawable.ic_note_on);
                        selectedFragment = new NoteFragment();
                        botNav.getMenu().getItem(1).setIcon(R.drawable.ic_schedule_off);
                        botNav.getMenu().getItem(2).setIcon(R.drawable.ic_task_off);
                        botNav.getMenu().getItem(3).setIcon(R.drawable.ic_todolist_off);
                        break;
                    case R.id.nav_schedule:
                        item.setIcon(R.drawable.ic_schedule_on);
                        botNav.getMenu().getItem(0).setIcon(R.drawable.ic_note_off);
                        botNav.getMenu().getItem(2).setIcon(R.drawable.ic_task_off);
                        botNav.getMenu().getItem(3).setIcon(R.drawable.ic_todolist_off);
                        selectedFragment = new ScheduleFragment();
                        break;
                    case R.id.nav_task:
                        item.setIcon(R.drawable.ic_task_on);
                        botNav.getMenu().getItem(0).setIcon(R.drawable.ic_task_off);
                        botNav.getMenu().getItem(1).setIcon(R.drawable.ic_schedule_off);
                        botNav.getMenu().getItem(3).setIcon(R.drawable.ic_todolist_off);
                        selectedFragment = new TaskFragment();
                        break;
                    case R.id.nav_todolist:
                        botNav.getMenu().getItem(1).setIcon(R.drawable.ic_schedule_off);
                        botNav.getMenu().getItem(2).setIcon(R.drawable.ic_task_off);
                        botNav.getMenu().getItem(0).setIcon(R.drawable.ic_note_off);
                        item.setIcon(R.drawable.ic_todolist_on);
                        selectedFragment = new TodolistFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CollegeActivity.this, DashboardActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}