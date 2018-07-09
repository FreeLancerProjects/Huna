package com.semicolon.criuse.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;
import com.semicolon.criuse.Fragments.FragmentMyOrderContainer;
import com.semicolon.criuse.Fragments.Fragment_Car;
import com.semicolon.criuse.Fragments.Fragment_Home;
import com.semicolon.criuse.Fragments.Fragment_Search;
import com.semicolon.criuse.Fragments.Fragment_Setting;
import com.semicolon.criuse.Fragments.Fragment_Subdepartment;
import com.semicolon.criuse.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private BottomNavigationView bottomNav;
    private CircleImageView image;
    private TextView tv_name,tv_address;
    private int pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();

    }

    private void initView() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        View view = navigationView.getHeaderView(0);
        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_address = view.findViewById(R.id.tv_address);
        ////////////////////////////////////////////////////////
        BottomNavigationItem item1 = new BottomNavigationItem(getString(R.string.main),R.color.un_color,R.drawable.home_icon);
        BottomNavigationItem item2 = new BottomNavigationItem(getString(R.string.dept),R.color.un_color,R.drawable.dept_icon);
        BottomNavigationItem item3 = new BottomNavigationItem(getString(R.string.search),R.color.un_color,R.drawable.search_icon);
        BottomNavigationItem item4 = new BottomNavigationItem(getString(R.string.trolley),R.color.un_color,R.drawable.market_icon);
        BottomNavigationItem item5 = new BottomNavigationItem(getString(R.string.settings),R.color.un_color,R.drawable.setting_icon);

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.addTab(item1);
        bottomNav.addTab(item2);
        bottomNav.addTab(item3);
        bottomNav.addTab(item4);
        bottomNav.addTab(item5);
        bottomNav.willNotRecreate(true);
        bottomNav.selectTab(0);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Home.getInstance("")).commit();
        bottomNav.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int position) {
                if (position==0)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Home.getInstance("")).commit();

                }else if (position ==1)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Subdepartment.getInstance()).commit();

                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Department.getInstance("")).commit();
                }
                else if (position ==2)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Search.getInstance("")).commit();
                }
                else if (position ==3)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Car.getInstance("")).commit();
                }
                else if (position ==4)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Setting.getInstance("")).commit();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            bottomNav.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Home.getInstance("")).commit();

        } else if (id == R.id.profile) {

        } else if (id == R.id.msg) {

        } else if (id == R.id.purchases) {
            pos = bottomNav.getCurrentItem();
            Log.e("pos1",pos+"");
            bottomNav.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, FragmentMyOrderContainer.getInstance()).commit();

        } else if (id == R.id.contact) {

        } else if (id == R.id.rule) {

        }
        else if (id == R.id.logout) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setNavPosition()
    {
        switch (pos)
        {
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Home.getInstance("")).commit();

                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Subdepartment.getInstance()).commit();

                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Search.getInstance("")).commit();

                break;
            case 3:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Car.getInstance("")).commit();

                break;
            case 4:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Setting.getInstance("")).commit();

                break;
        }
        bottomNav.setVisibility(View.VISIBLE);
        bottomNav.selectTab(pos);
        navigationView.getMenu().getItem(0).setChecked(true);
    }
}
