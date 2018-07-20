package com.semicolon.criuse.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;
import com.semicolon.criuse.Fragments.FragmentMyOrderContainer;
import com.semicolon.criuse.Fragments.Fragment_Car;
import com.semicolon.criuse.Fragments.Fragment_Client_Register;
import com.semicolon.criuse.Fragments.Fragment_Driver_Register;
import com.semicolon.criuse.Fragments.Fragment_Grocery_Register;
import com.semicolon.criuse.Fragments.Fragment_Home;
import com.semicolon.criuse.Fragments.Fragment_Login;
import com.semicolon.criuse.Fragments.Fragment_Profile;
import com.semicolon.criuse.Fragments.Fragment_Setting;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;

import java.util.List;

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
    private TextView tv_notf,tv_title;
    private BottomSheetBehavior behavior;
    private View view;
    private Fragment_Client_Register fragment_client_register;
    private Fragment_Grocery_Register fragment_grocery_register;
    private Fragment_Driver_Register fragment_driver_register;
    private ImageView back;
    private View not_root;
    private Fragment_Login fragment_login;
    private String fragment_type="";
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
        not_root =  navigationView.getMenu().findItem(R.id.msg).getActionView();
        tv_notf = not_root.findViewById(R.id.tv_not_txt);

        IncreaseNotification_Counter(5);
        back = findViewById(R.id.back);
        this.view =findViewById(R.id.root);
        tv_title = findViewById(R.id.tv_title);
        behavior = BottomSheetBehavior.from(this.view);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        View view = navigationView.getHeaderView(0);
        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_address = view.findViewById(R.id.tv_address);
        ////////////////////////////////////////////////////////
        new Thread(new Runnable() {
            @Override
            public void run() {
                BottomNavigationItem item1 = new BottomNavigationItem(getString(R.string.main),R.color.un_color,R.drawable.home_icon);
                BottomNavigationItem item2 = new BottomNavigationItem(getString(R.string.profile),R.color.un_color,R.drawable.person_icon);
                BottomNavigationItem item3 = new BottomNavigationItem(getString(R.string.trolley),R.color.un_color,R.drawable.market_icon);
                BottomNavigationItem item4 = new BottomNavigationItem(getString(R.string.settings),R.color.un_color,R.drawable.setting_icon);

                bottomNav = findViewById(R.id.bottomNav);
                bottomNav.addTab(item1);
                bottomNav.addTab(item2);
                bottomNav.addTab(item3);
                bottomNav.addTab(item4);
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
                            bottomNav.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Profile.getInstance("")).commit();

                        }
                        else if (position ==2)
                        {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Car.getInstance("")).commit();

                        }

                        else if (position ==3)
                        {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Setting.getInstance("")).commit();

                        }

                    }
                });

            }
        }).start();

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentsContainer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (behavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
        {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else {
         if (fragment instanceof Fragment_Home)
            {
                super.onBackPressed();

            } else
                {
                    bottomNav.selectTab(0);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Home.getInstance("")).commit();

                }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            bottomNav.selectTab(0);
            bottomNav.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Home.getInstance("")).commit();

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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Profile.getInstance("")).commit();

                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Car.getInstance("")).commit();

                break;
            case 3:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Setting.getInstance("")).commit();

                break;

        }
        bottomNav.setVisibility(View.VISIBLE);
        bottomNav.selectTab(pos);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public void RegisterType(String type)
    {
        tv_title.setText(getString(R.string.create_accounts));

        switch (type)
        {
            case Tags.client_register:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fragment_client_register = Fragment_Client_Register.getInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer_register,fragment_client_register).commit();
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }).start();
                break;
            case Tags.grocery_register:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fragment_grocery_register = Fragment_Grocery_Register.getInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer_register,fragment_grocery_register).commit();
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }).start();
                break;
            case Tags.driver_register:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fragment_driver_register = Fragment_Driver_Register.getInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer_register,fragment_driver_register).commit();
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }).start();
                break;
        }
    }

    public void DisplayLoginLayout(String fragment_type)
    {
        tv_title.setText(getString(R.string.login));
        this.fragment_type = fragment_type;
        fragment_login = Fragment_Login.getInstance();
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer_register,fragment_login).commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        },500);
    }

    public void DisplayRegister_Layout()
    {
        tv_title.setText(getString(R.string.create_accounts));

        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        switch (fragment_type)
        {
            case Tags.client_register:
                fragment_client_register = Fragment_Client_Register.getInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer_register,fragment_client_register).commit();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    }
                },500);
                break;
            case Tags.grocery_register:
                fragment_grocery_register = Fragment_Grocery_Register.getInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer_register,fragment_grocery_register).commit();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    }
                },500);
                break;
            case Tags.driver_register:
                fragment_driver_register = Fragment_Driver_Register.getInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer_register,fragment_driver_register).commit();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    }
                },500);
                break;
        }
    }

    public void IncreaseNotification_Counter(int counter)
    {
        if (counter>0)
        {
            tv_notf.setVisibility(View.VISIBLE);
            tv_notf.setText(String.valueOf(counter));
        }else
            {
                tv_notf.setVisibility(View.INVISIBLE);

            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment:fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
