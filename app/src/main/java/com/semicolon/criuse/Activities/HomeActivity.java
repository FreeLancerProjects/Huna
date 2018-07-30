package com.semicolon.criuse.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.irozon.sneaker.Sneaker;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;
import com.semicolon.criuse.Fragments.FragmentClientNotification;
import com.semicolon.criuse.Fragments.FragmentMyOrderContainer;
import com.semicolon.criuse.Fragments.Fragment_Car;
import com.semicolon.criuse.Fragments.Fragment_Categories;
import com.semicolon.criuse.Fragments.Fragment_Client_Register;
import com.semicolon.criuse.Fragments.Fragment_Driver_Register;
import com.semicolon.criuse.Fragments.Fragment_Grocery_Register;
import com.semicolon.criuse.Fragments.Fragment_Home;
import com.semicolon.criuse.Fragments.Fragment_Login;
import com.semicolon.criuse.Fragments.Fragment_Profile;
import com.semicolon.criuse.Fragments.Fragment_Setting;
import com.semicolon.criuse.Models.GroceryPart1;
import com.semicolon.criuse.Models.LocationUpdateModel;
import com.semicolon.criuse.Models.ResponseModel;
import com.semicolon.criuse.Models.UserModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Api;
import com.semicolon.criuse.Services.ServiceUpdateLocation;
import com.semicolon.criuse.Services.Tags;
import com.semicolon.criuse.Share.Common;
import com.semicolon.criuse.SharedPreferences.Preferences;
import com.semicolon.criuse.SingleTones.UserSingletone;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private Fragment_Login fragment_login;
    private Fragment_Profile fragment_profile;
    private ImageView back;
    private View not_root;
    private String fragment_type="";
    private Preferences preferences;
    private UserModel userModel;
    private String session="";
    private UserSingletone userSingletone;
    private Fragment_Setting fragment_setting;
    private AlertDialog logoutDialog;
    private Intent serviceIntent;
    private AlertDialog notalertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        CreateLogoutAlertDialog();
        CreateNotAlertDialog();
    }

    private void initView()
    {
        Log.e("home fragment","initview");
        preferences = Preferences.getInstance();
        userSingletone = UserSingletone.getInstance();
        session = preferences.getSession(this);
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

                if (TextUtils.isEmpty(fragment_type))
                {
                    bottomNav.selectTab(0);

                }
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                navigationView.getMenu().getItem(0).setChecked(true);

            }
        });

        View view = navigationView.getHeaderView(0);
        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_address = view.findViewById(R.id.tv_address);
        ////////////////////////////////////////////////////////

                BottomNavigationItem item1 = new BottomNavigationItem(getString(R.string.main),R.color.un_color,R.drawable.home_icon);
                BottomNavigationItem item2 = new BottomNavigationItem(getString(R.string.not),R.color.un_color,R.drawable.not_icon);
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
                            String session = preferences.getSession(HomeActivity.this);
                            if (session!=null&& !TextUtils.isEmpty(session))
                            {
                                if (session.equals(Tags.session_login))
                                {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, FragmentClientNotification.getInstance("")).commit();

                                }else if (session.equals(Tags.session_logout))
                                {
                                    notalertDialog.show();
                                }
                            }else
                            {
                                notalertDialog.show();

                            }

                        }
                        else if (position ==2)
                        {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Car.getInstance("")).commit();

                        }

                        else if (position ==3)
                        {
                            fragment_setting = Fragment_Setting.getInstance("");
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer,fragment_setting ).commit();

                        }

                    }
                });





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

        if (session!=null&& !TextUtils.isEmpty(session))
        {
            if (session.equals(Tags.session_login))
            {
                userModel = preferences.getUserData(this);
                userSingletone.setUserModel(userModel);
                UpdateUi(userModel);
            }
        }

    }
    private void CreateLogoutAlertDialog()
    {
        logoutDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.out)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Logout();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentsContainer);
                        if (fragment instanceof Fragment_Home)
                        {
                            navigationView.getMenu().getItem(0).setChecked(true);

                        }else if (fragment instanceof Fragment_Categories)
                        {
                            navigationView.getMenu().getItem(2).setChecked(true);

                        }
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true)
                .create();

        logoutDialog.setCanceledOnTouchOutside(false);
    }
    private void CreateNotAlertDialog()
    {
        notalertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(this).inflate(R.layout.custom_notification_dialog,null);
        Button btn_login,btn_reg,btn_close;

        btn_login = view.findViewById(R.id.btn_login);
        btn_reg = view.findViewById(R.id.btn_reg);
        btn_close = view.findViewById(R.id.btn_close);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_type="";
                notalertDialog.dismiss();
                tv_title.setText(getString(R.string.login));
                fragment_login = Fragment_Login.getInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer_register,fragment_login).commit();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    }
                },500);
            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notalertDialog.dismiss();
                bottomNav.selectTab(3);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer,fragment_setting ).commit();

            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNav.selectTab(0);

                notalertDialog.dismiss();
            }
        });

        notalertDialog.setView(view);
        notalertDialog.setCanceledOnTouchOutside(false);
    }
    private void UpdateUi(UserModel userModel)
    {
        if (userModel!=null)
        {
            Log.e("driver",userModel.getUser_type());
            Log.e("driver",userModel.getUser_city()+"city");


            if (userModel.getUser_type().equals(Tags.user_type_client))
            {
                Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser_photo())).placeholder(R.drawable.user_profile).into(image);
                tv_name.setText(userModel.getUser_full_name());
            }else if (userModel.getUser_type().equals(Tags.user_type_driver))
            {
                Log.e("driver","driver");
                Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser_photo())).placeholder(R.drawable.user_profile).into(image);
                tv_name.setText(userModel.getUser_full_name());
                tv_address.setText(userModel.getUser_city());

                serviceIntent = new Intent(this, ServiceUpdateLocation.class);
                startService(serviceIntent);
                EventBus.getDefault().register(this);
            }
        }
    }
    ////from login and registering fragments and profile fragment
    public void UpdateData(UserModel userModel)
    {
        userSingletone.setUserModel(userModel);
        this.userModel = userSingletone.getUserModel();
        preferences.Create_Update_Pref(this, Common.ConvertModelToGson(userModel));
        preferences.Create_Update_Session(this,Tags.session_login);
        this.session = preferences.getSession(this);
        UpdateUi(this.userModel);
    }
    public void UpdateSettingFragmentUi(UserModel userModel)
    {
        if (TextUtils.isEmpty(fragment_type))
        {
            bottomNav.selectTab(0);
        }
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentsContainer);

        if (fragment instanceof Fragment_Setting)
        {

            fragment_setting.UpdateUi(userModel);

        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.home) {
            bottomNav.selectTab(0);
            bottomNav.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Home.getInstance("")).commit();

        }else if (id==R.id.profile)
        {
            String session = preferences.getSession(this);
            if (session!=null&& !TextUtils.isEmpty(session))
            {
                if (session.equals(Tags.session_login))
                {

                    tv_title.setText(getString(R.string.profile));
                    fragment_profile = Fragment_Profile.getInstance("");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer_register,fragment_profile).commit();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    },500);
                }else if (session.equals(Tags.session_logout))
                {
                    navigationView.getMenu().getItem(0).setChecked(true);

                    CreateSneakerAlert(getString(R.string.reg_log));
                }
            }else if (session!=null&& TextUtils.isEmpty(session))
            {
                navigationView.getMenu().getItem(0).setChecked(true);
                CreateSneakerAlert(getString(R.string.reg_log));


            }
        }
        else if (id == R.id.msg) {

            String session = preferences.getSession(this);
            if (session!=null&& !TextUtils.isEmpty(session))
            {
                if (session.equals(Tags.session_login))
                {

                }else if (session.equals(Tags.session_logout))
                {

                    navigationView.getMenu().getItem(0).setChecked(true);

                    CreateSneakerAlert(getString(R.string.reg_log));
                }
            }else if (session!=null&& TextUtils.isEmpty(session))
            {
                navigationView.getMenu().getItem(0).setChecked(true);

                CreateSneakerAlert(getString(R.string.reg_log));


            }
        } else if (id == R.id.purchases) {

            String session = preferences.getSession(this);
            if (session!=null&& !TextUtils.isEmpty(session))
            {
                if (session.equals(Tags.session_login))
                {

                    bottomNav.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, FragmentMyOrderContainer.getInstance()).commit();

                }else if (session.equals(Tags.session_logout))
                {
                    navigationView.getMenu().getItem(0).setChecked(true);

                    CreateSneakerAlert(getString(R.string.reg_log));
                }
            }else if (session!=null&& TextUtils.isEmpty(session))
            {
                navigationView.getMenu().getItem(0).setChecked(true);

                CreateSneakerAlert(getString(R.string.reg_log));


            }


            //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, FragmentMyOrderContainer.getInstance()).commit();

        } else if (id == R.id.contact) {

        } else if (id == R.id.rule) {

        }
        else if (id == R.id.logout) {

            String session = preferences.getSession(this);
            if (session!=null&& !TextUtils.isEmpty(session))
            {
                if (session.equals(Tags.session_login))
                {
                    logoutDialog.show();

                }else if (session.equals(Tags.session_logout))
                {
                    finish();

                }
            }else if (session!=null&& TextUtils.isEmpty(session))
            {
                finish();

            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void CreateSneakerAlert(String msg)
    {
        Sneaker.with(this)
                .autoHide(true)
                .setMessage(msg,R.color.white)
                .setDuration(4000)
                .setHeight(130)
                .sneak(R.color.kohly);
    }
    private void Logout()
    {
        //userModel = userSingletone.getUserModel();

        android.app.AlertDialog progressDialog = Common.CreateProgressDialog(this,getString(R.string.lout));
        progressDialog.show();
        Log.e("id",userModel.getUser_id());
        Api.getServices().logout(userModel.getUser_id())
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            if (response.body().getSuccess()==1)
                            {
                                preferences.Clear_SharedPref(HomeActivity.this);
                                finish();
                            }else if (response.body().getSuccess()==0)
                            {
                                Toast.makeText(HomeActivity.this, R.string.something_error, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(HomeActivity.this, R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });

    }
    //fragment my order container
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

                        fragment_grocery_register = Fragment_Grocery_Register.getInstance(null);
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
                fragment_grocery_register = Fragment_Grocery_Register.getInstance(null);
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

            case "":
                bottomNav.selectTab(0);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                break;
        }
    }
    public void DisplayGroceryNextDataToComplete(GroceryPart1 groceryPart1)
    {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_title.setText(R.string.comp);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer_register, Fragment_Categories.getInstance(groceryPart1)).commit();
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                },500);

    }
    public void BackFromGroceryRegisert2ToRegister1(GroceryPart1 groceryPart1)
    {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_title.setText(getString(R.string.create_accounts));
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer_register, Fragment_Grocery_Register.getInstance(groceryPart1)).commit();
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                },500);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment:fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment:fragmentList)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /// drivers
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateLocation(LocationUpdateModel locationUpdateModel)
    {
        Log.e("ssslllooocc222",locationUpdateModel.getLat()+"");

        Api.getServices()
                .updateLocation(userModel.getUser_id(),String.valueOf(locationUpdateModel.getLat()),String.valueOf(locationUpdateModel.getLng()))
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess()==1)
                            {
                                Log.e("driver location updated","driver location updated successfully");
                            }else if (response.body().getSuccess()==0)
                            {
                                Log.e("driver location updated","driver location ont updated ");

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(HomeActivity.this,R.string.something_error, Toast.LENGTH_SHORT).show();
                        Log.e("Error",t.getMessage());
                    }
                });
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (session!=null&&!TextUtils.isEmpty(session))
        {
            if (session.equals(Tags.session_login))
            {
                if (userModel!=null)
                {
                    if (userModel.getUser_type().equals(Tags.user_type_driver))
                    {
                        if (serviceIntent!=null)
                        {
                            EventBus.getDefault().unregister(this);

                            stopService(serviceIntent);

                        }
                    }
                }

            }
        }

    }

    @Override
    public void onBackPressed()
    {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentsContainer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (behavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
        {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            navigationView.getMenu().getItem(0).setChecked(true);

        }
        else {
            if (fragment instanceof Fragment_Home)
            {
                super.onBackPressed();

            } else
            {

                bottomNav.setVisibility(View.VISIBLE);
                bottomNav.selectTab(0);
                navigationView.getMenu().getItem(0).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Home.getInstance("")).commit();

            }
        }
    }



}
