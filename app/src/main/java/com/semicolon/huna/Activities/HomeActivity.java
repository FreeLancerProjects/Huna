package com.semicolon.huna.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.irozon.sneaker.Sneaker;
import com.semicolon.huna.Fragments.FragmentMyOrderContainer;
import com.semicolon.huna.Fragments.Fragment_Car;
import com.semicolon.huna.Fragments.Fragment_Categories;
import com.semicolon.huna.Fragments.Fragment_Client_Notification;
import com.semicolon.huna.Fragments.Fragment_Client_Register;
import com.semicolon.huna.Fragments.Fragment_Contactus;
import com.semicolon.huna.Fragments.Fragment_Driver_Notification;
import com.semicolon.huna.Fragments.Fragment_Driver_Register;
import com.semicolon.huna.Fragments.Fragment_ForgetPassword;
import com.semicolon.huna.Fragments.Fragment_Grocery_Notification;
import com.semicolon.huna.Fragments.Fragment_Grocery_Register;
import com.semicolon.huna.Fragments.Fragment_Home;
import com.semicolon.huna.Fragments.Fragment_Location;
import com.semicolon.huna.Fragments.Fragment_Login;
import com.semicolon.huna.Fragments.Fragment_Profile;
import com.semicolon.huna.Fragments.Fragment_Rules;
import com.semicolon.huna.Fragments.Fragment_Setting;
import com.semicolon.huna.Models.GroceryPart1;
import com.semicolon.huna.Models.LocationUpdateModel;
import com.semicolon.huna.Models.ResponseModel;
import com.semicolon.huna.Models.UnreadModel;
import com.semicolon.huna.Models.UserModel;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Api;
import com.semicolon.huna.Services.ServiceUpdateLocation;
import com.semicolon.huna.Services.Tags;
import com.semicolon.huna.Share.Common;
import com.semicolon.huna.SharedPreferences.Preferences;
import com.semicolon.huna.SingleTones.ItemsSingleTone;
import com.semicolon.huna.SingleTones.UserSingletone;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    //private BottomNavigationView bottomNav;
    private AHBottomNavigation ahBottomNavigation;
    private CircleImageView image;
    private TextView tv_name,tv_address,tv_area_title;
    private int pos=0;
    private TextView tv_notf,tv_title;
    private BottomSheetBehavior behavior;
    private View view;
    private Fragment_Client_Register fragment_client_register;
    private Fragment_Grocery_Register fragment_grocery_register;
    private Fragment_Driver_Register fragment_driver_register;
    private Fragment_Login fragment_login;
    private Fragment_Home fragment_home;
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
    public FragmentManager fragmentManager;
    public ItemsSingleTone itemsSingleTone;
    private FabSpeedDial fab;
    private String from_id="",area_id="",area_title;
    private LinearLayout ll_location;
    private CardView card;
    private AutoCompleteTextView searchView;
    private ProgressBar progBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fragmentManager = getSupportFragmentManager();
        initView();
        CreateLogoutAlertDialog();
        CreateNotAlertDialog();
    }



    private void initView()
    {
        Log.e("home fragment_bill","initview");

        itemsSingleTone = ItemsSingleTone.getInstance();
        preferences = Preferences.getInstance();
        userSingletone = UserSingletone.getInstance();
        session = preferences.getSession(this);
        from_id = preferences.getFrom_id(this);
        area_id = preferences.getArea_id(this);
        area_title = preferences.getArea_title(this);
        tv_area_title = findViewById(R.id.tv_area_title);
        tv_area_title.setText(area_title);
        ll_location = findViewById(R.id.ll_location);
        card = findViewById(R.id.card);
        searchView = findViewById(R.id.searchView);
        progBar = findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        ll_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_title.setText(getString(R.string.your_area));

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer_register, Fragment_Location.getInstance()).commit();

                if (behavior.getState()==BottomSheetBehavior.STATE_COLLAPSED)
                {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                }
                else if (behavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
                {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }
            }
        });

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH)
                {
                    String query = searchView.getText().toString();
                    if (!TextUtils.isEmpty(query))
                    {
                        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentsContainer);
                        if (fragment instanceof Fragment_Home)
                        {
                            progBar.setVisibility(View.VISIBLE);
                            Common.CloseKeyBoard(HomeActivity.this,searchView);

                            Fragment_Home fragment_home = (Fragment_Home) fragment;
                            fragment_home.Search(query);

                        }
                    }
                }
                return false;
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchView.getText().toString().length()==0)
                {
                    Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentsContainer);
                    if (fragment instanceof Fragment_Home)
                    {
                        Common.CloseKeyBoard(HomeActivity.this,searchView);

                        Fragment_Home fragment_home = (Fragment_Home) fragment;
                        fragment_home.Search("");

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /////////////////////////////////////////////////////////////////////////
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
        /*not_root =  navigationView.getMenu().findItem(R.id.msg).getActionView();
        tv_notf = not_root.findViewById(R.id.tv_not_txt);*/
       // tv_not_budget = findViewById(R.id.tv_not_budget);
        //IncreaseNotification_Counter(5);
        //IncreaseNotificationBudget_Counter(9);

        fab = findViewById(R.id.fabsd);
        back = findViewById(R.id.back);
        this.view =findViewById(R.id.root);
        tv_title = findViewById(R.id.tv_title);
        behavior = BottomSheetBehavior.from(this.view);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(fragment_type))
                {
                    ahBottomNavigation.setCurrentItem(0);
                    //bottomNav.selectTab(0);

                }
               CloseBottomsheet();

            }
        });

        View view = navigationView.getHeaderView(0);
        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_address = view.findViewById(R.id.tv_address);
        ////////////////////////////////////////////////////////

        ahBottomNavigation = findViewById(R.id.bottomNav);
        ahBottomNavigation.setForceTint(false);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNavigation.setColored(false);
        ahBottomNavigation.setTitleTextSizeInSp(13f,11f);
        ahBottomNavigation.setInactiveColor(ContextCompat.getColor(this,R.color.un_color));
        ahBottomNavigation.setAccentColor(ContextCompat.getColor(this,R.color.colorPrimary));
        ahBottomNavigation.setDefaultBackgroundResource(R.color.white);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.main),R.drawable.home_icon,R.color.un_color);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.not),R.drawable.not_icon,R.color.un_color);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.trolley),R.drawable.market_icon,R.color.un_color);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.settings),R.drawable.setting_icon,R.color.un_color);

        ahBottomNavigation.addItem(item1);
        ahBottomNavigation.addItem(item2);
        ahBottomNavigation.addItem(item3);
        ahBottomNavigation.addItem(item4);
        ahBottomNavigation.setCurrentItem(0);




        fragment_home = Fragment_Home.getInstance(from_id,area_id);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, fragment_home).commit();
        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (position==0)
                {
                    fragment_home = Fragment_Home.getInstance(from_id,area_id);

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, fragment_home).commit();
                    ahBottomNavigation.setCurrentItem(0,false);
                    card.setVisibility(View.VISIBLE);
                    ll_location.setVisibility(View.VISIBLE);
                }else if (position ==1)
                {
                    card.setVisibility(View.GONE);
                    ll_location.setVisibility(View.GONE);

                    ahBottomNavigation.setCurrentItem(1,false);

                    String session = preferences.getSession(HomeActivity.this);
                    if (session!=null&& !TextUtils.isEmpty(session))
                    {
                        if (session.equals(Tags.session_login))
                        {
                            if (userModel!=null)
                            {
                                if (userModel.getUser_type().equals(Tags.user_type_client)){
                                    fragmentManager.beginTransaction().replace(R.id.fragmentsContainer, Fragment_Client_Notification.getInstance(userModel.getUser_id())).commit();
                                    setClientReadAll(userModel.getUser_id());
                                }else if (userModel.getUser_type().equals(Tags.user_type_driver))
                                {
                                    fragmentManager.beginTransaction().replace(R.id.fragmentsContainer, Fragment_Driver_Notification.getInstance(userModel.getUser_id())).commit();
                                    setDriver_GroceryReadAll(userModel.getUser_id());
                                }else if (userModel.getUser_type().equals(Tags.user_type_grocery))
                                {
                                    setDriver_GroceryReadAll(userModel.getUser_id());

                                    fragmentManager.beginTransaction().replace(R.id.fragmentsContainer, Fragment_Grocery_Notification.getInstance(userModel.getUser_id())).commit();


                                }

                            }

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
                    card.setVisibility(View.GONE);
                    ll_location.setVisibility(View.GONE);

                    ahBottomNavigation.setCurrentItem(2,false);

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Car.getInstance("")).commit();

                }

                else if (position ==3)
                {
                    card.setVisibility(View.GONE);
                    ll_location.setVisibility(View.GONE);

                    ahBottomNavigation.setCurrentItem(3,false);

                    fragment_setting = Fragment_Setting.getInstance("");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer,fragment_setting ).commit();

                }


                return false;
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
                EventBus.getDefault().register(this);
                UpdateToken();

                if (userModel.getUser_type().equals(Tags.user_type_driver))
                {
                    fab.setVisibility(View.GONE);
                    serviceIntent = new Intent(this, ServiceUpdateLocation.class);
                    startService(serviceIntent);

                }else if (userModel.getUser_type().equals(Tags.user_type_grocery))
                {
                    UpdateSpeedDial();
                }else
                    {
                        fab.setVisibility(View.GONE);

                    }

                UpdateUi(userModel);
            }
        }

    }

    private void UpdateToken() {
        Task<InstanceIdResult> instanceId = FirebaseInstanceId.getInstance().getInstanceId();
        instanceId.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful())
                {
                    String token = task.getResult().getToken();
                    Api.getServices()
                            .UpdateToken(userModel.getUser_id(),token)
                            .enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    if (response.isSuccessful())
                                    {
                                        if (response.body().getSuccess()==1)
                                        {
                                            Log.e("token","token updated successfully");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {

                                }
                            });
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateLocation(LocationUpdateModel locationUpdateModel)
    {
        Log.e("ssslllooocc222",locationUpdateModel.getLat()+"");

        if (userModel.getUser_type().equals(Tags.user_type_driver))
        {
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

    }


    private void UpdateSpeedDial() {
        Log.e("fb","fb");
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fab_anim);
        fab.setVisibility(View.VISIBLE);
        fab.clearAnimation();
        fab.startAnimation(animation);
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
                ahBottomNavigation.setCurrentItem(3);
                //bottomNav.selectTab(3);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer,fragment_setting ).commit();

            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ahBottomNavigation.setCurrentItem(0);
                //bottomNav.selectTab(0);

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

            UpdateToken();
            if (userModel.getUser_type().equals(Tags.user_type_client))
            {
                Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser_photo())).placeholder(R.drawable.user_profile).into(image);
                tv_name.setText(userModel.getUser_full_name());
                getClientNotCount(userModel.getUser_id());

            }else if (userModel.getUser_type().equals(Tags.user_type_driver))
            {
                Log.e("driver","driver");
                Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser_photo())).placeholder(R.drawable.user_profile).into(image);
                tv_name.setText(userModel.getUser_full_name());
                tv_address.setText(userModel.getCity_title());
                getDriver_grocery_NotCount(userModel.getUser_id());


            }else if (userModel.getUser_type().equals(Tags.user_type_grocery))
            {
                Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser_photo())).placeholder(R.drawable.user_profile).into(image);
                tv_name.setText(userModel.getUser_full_name());
                getDriver_grocery_NotCount(userModel.getUser_id());
                UpdateSpeedDial();
            }
        }
    }

    public void getClientNotCount(String user_id)
    {
        Api.getServices().getClientUnreadNotification(user_id)
                .enqueue(new Callback<UnreadModel>() {
                    @Override
                    public void onResponse(Call<UnreadModel> call, Response<UnreadModel> response) {
                        if (response.isSuccessful())
                        {
                            IncreaseNotification(response.body().getTotal_unread());
                        }
                    }

                    @Override
                    public void onFailure(Call<UnreadModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                    }
                });
    }

    public void getDriver_grocery_NotCount(String user_id)
    {
        Api.getServices().getDriver_Grocery_UnreadNotification(user_id)
                .enqueue(new Callback<UnreadModel>() {
                    @Override
                    public void onResponse(Call<UnreadModel> call, Response<UnreadModel> response) {
                        if (response.isSuccessful())
                        {
                            IncreaseNotification(response.body().getTotal_unread());
                        }
                    }

                    @Override
                    public void onFailure(Call<UnreadModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                    }
                });
    }

    private void setClientReadAll(String user_id)
    {
        Api.getServices().setClientReadNotification(user_id,String.valueOf(1))
                .enqueue(new Callback<UnreadModel>() {
                    @Override
                    public void onResponse(Call<UnreadModel> call, Response<UnreadModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess_read()==1)
                            {
                                IncreaseNotification(0);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UnreadModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                    }
                });
    }

    private void setDriver_GroceryReadAll(String user_id)
    {
        Api.getServices().setDriver_Grocery_ReadNotification(user_id,String.valueOf(1))
                .enqueue(new Callback<UnreadModel>() {
                    @Override
                    public void onResponse(Call<UnreadModel> call, Response<UnreadModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess_read()==1)
                            {
                                IncreaseNotification(0);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UnreadModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                    }
                });
    }

    public void HideLoadProgress()

    {
        progBar.setVisibility(View.GONE);
    }
    public void HideFab()
    {
        Log.e("fabbb","fabbbb");
        fab.setVisibility(View.GONE);
    }

    public void showFab()
    {

        String session = preferences.getSession(this);

        if (session!=null&&!TextUtils.isEmpty(session))
        {
            if (session.equals(Tags.session_login))
            {
                if (userModel!=null)
                {
                    if (userModel.getUser_type().equals(Tags.user_type_grocery))
                    {
                        UpdateSpeedDial();
                    }else
                        {
                            fab.setVisibility(View.GONE);
                        }
                }
            }else
                {
                    fab.setVisibility(View.GONE);
                }
        }else
            {
                fab.setVisibility(View.GONE);
            }

        if (userModel!=null)
        {
            if (userModel.getUser_type().equals(Tags.user_type_grocery))
            {
                UpdateSpeedDial();
            }else
                {
                    fab.setVisibility(View.GONE);
                }
        }else
            {
                fab.setVisibility(View.GONE);
            }
    }
    ////from login and registering fragments and profile fragment_bill
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
            ahBottomNavigation.setCurrentItem(0);
            //bottomNav.selectTab(0);
        }
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentsContainer);

        if (fragment instanceof Fragment_Setting)
        {

            fragment_setting.UpdateUi(userModel);

        }

    }

    public void UpdateFragment_Home(String from_id,String area_id,String area_title)
    {
        this.from_id = from_id;
        this.area_id = area_id;
        this.area_title = area_title;
        tv_area_title.setText(area_title);
        fragment_home = Fragment_Home.getInstance(from_id,area_id);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, fragment_home).commit();
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.home) {

            ahBottomNavigation.setCurrentItem(0);
            ahBottomNavigation.setVisibility(View.VISIBLE);
           /* bottomNav.selectTab(0);
            bottomNav.setVisibility(View.VISIBLE);*/
            fragment_home = Fragment_Home.getInstance(from_id,area_id);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, fragment_home).commit();
            card.setVisibility(View.VISIBLE);
            ll_location.setVisibility(View.VISIBLE);

        }else if (id==R.id.profile)
        {

            card.setVisibility(View.GONE);
            ll_location.setVisibility(View.GONE);

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
            }else
            {
                navigationView.getMenu().getItem(0).setChecked(true);
                CreateSneakerAlert(getString(R.string.reg_log));


            }
        }
       /* else if (id == R.id.msg) {
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
            }else
            {


                ////

                navigationView.getMenu().getItem(0).setChecked(true);

                CreateSneakerAlert(getString(R.string.reg_log));


            }
        }*/ else if (id == R.id.purchases) {

            card.setVisibility(View.GONE);
            ll_location.setVisibility(View.GONE);

            savePos();

            String session = preferences.getSession(this);
            if (session!=null&& !TextUtils.isEmpty(session))
            {
                if (session.equals(Tags.session_login))
                {

                    Hide_Navbottom();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, FragmentMyOrderContainer.getInstance(preferences.getUserData(HomeActivity.this).getUser_type(),preferences.getUserData(HomeActivity.this).getUser_id())).commit();

                }else if (session.equals(Tags.session_logout))
                {
                    navigationView.getMenu().getItem(0).setChecked(true);

                    CreateSneakerAlert(getString(R.string.reg_log));
                }
            }else
            {
                navigationView.getMenu().getItem(0).setChecked(true);

                CreateSneakerAlert(getString(R.string.reg_log));


            }



        } else if (id == R.id.contact) {
            card.setVisibility(View.GONE);
            ll_location.setVisibility(View.GONE);

            savePos();
            Hide_Navbottom();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Contactus.getInstance()).commit();

        } else if (id == R.id.rule) {
            card.setVisibility(View.GONE);
            ll_location.setVisibility(View.GONE);


            savePos();
            Hide_Navbottom();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Rules.getInstance()).commit();

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
            }else
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

        android.app.AlertDialog progressDialog = Common.CreateAlertDialog(this,getString(R.string.lout));
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
                                itemsSingleTone.clear();

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
    //fragment_bill my order container
    public void setNavPosition()
    {
        switch (pos)
        {
            case 0:
                fragment_home = Fragment_Home.getInstance(from_id,area_id);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer,fragment_home).commit();

                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Client_Notification.getInstance(userModel.getUser_id())).commit();

                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Car.getInstance("")).commit();

                break;
            case 3:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Setting.getInstance("")).commit();

                break;

        }
        ahBottomNavigation.setVisibility(View.VISIBLE);
        ahBottomNavigation.setCurrentItem(pos);

        /*bottomNav.setVisibility(View.VISIBLE);
        bottomNav.selectTab(pos);*/
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
                ahBottomNavigation.setCurrentItem(0);
                //bottomNav.selectTab(0);
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
    public void BackFromGroceryRegister2ToRegister1(GroceryPart1 groceryPart1)
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

    public void navigateToSettingFragment()
    {
        if (behavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
        {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
        fragment_setting = Fragment_Setting.getInstance("");
        fragmentManager.beginTransaction().replace(R.id.fragmentsContainer,fragment_setting ).commit();
        ahBottomNavigation.setCurrentItem(3);

       // bottomNav.selectTab(3);
    }
    public void navigateToHomeFragment()
    {
        fragment_home = Fragment_Home.getInstance(from_id,area_id);

        fragmentManager.beginTransaction().replace(R.id.fragmentsContainer,fragment_home ).commit();
        ahBottomNavigation.setCurrentItem(0);

        //bottomNav.selectTab(0);
    }
    public void navigateToForgetPassword()
    {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_title.setText(getString(R.string.password_recovery));
                        fragmentManager.beginTransaction().replace(R.id.fragmentsContainer_register, Fragment_ForgetPassword.getInstance()).commit();
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                },500);
    }
    public void IncreaseNotification_Counter(int counter)
    {
        /*if (counter>0)
        {
            tv_notf.setVisibility(View.VISIBLE);
            tv_notf.setText(String.valueOf(counter));
        }else
            {
                tv_notf.setVisibility(View.INVISIBLE);

            }*/
    }
    public void IncreaseNotificationBudget_Counter(int counter)
    {
        if (counter>0)
        {
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.not_anim);
           /* tv_not_budget.setVisibility(View.VISIBLE);
            tv_not_budget.setText(String.valueOf(counter));*/
           /* new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tv_not_budget.clearAnimation();
                            tv_not_budget.startAnimation(animation);
                        }
                    },2000);*/


        }else
        {
            //tv_notf.setVisibility(View.INVISIBLE);

        }
    }
    public void IncreaseNotification(int counter)
    {
        if (counter>0)
        {

            AHNotification ahNotification = new AHNotification.Builder()
                    .setText(String.valueOf(counter))
                    .setBackgroundColor(ContextCompat.getColor(this,R.color.notf))
                    .setTextColor(ContextCompat.getColor(this,R.color.white))
                    .build();
            ahBottomNavigation.setNotification(ahNotification,1);


        }else
        {
            AHNotification ahNotification = new AHNotification.Builder()
                    .setText("")
                    .build();
            ahBottomNavigation.setNotification(ahNotification,1);

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

    public void CloseBottomsheet()
    {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public void Hide_Navbottom()
    {

        ahBottomNavigation.setVisibility(View.GONE);
        //bottomNav.setVisibility(View.GONE);
    }
    public void savePos()
    {
        this.pos = ahBottomNavigation.getCurrentItem();

        //this.pos = bottomNav.getCurrentItem();
    }
    /// drivers


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
            card.setVisibility(View.VISIBLE);
            ll_location.setVisibility(View.VISIBLE);



        }

        else{
            if (fragment instanceof Fragment_Home)
            {
                if (fragmentManager.getBackStackEntryCount()>0) {
                    itemsSingleTone.clear();
                    fragmentManager.popBackStack();
                }

                card.setVisibility(View.VISIBLE);
                ll_location.setVisibility(View.VISIBLE);

                itemsSingleTone.ClearItemModelList();

                super.onBackPressed();

            } else
            {

                ahBottomNavigation.setVisibility(View.VISIBLE);
                //bottomNav.setVisibility(View.VISIBLE);
                setNavPosition();
               // bottomNav.selectTab(pos);
                navigationView.getMenu().getItem(0).setChecked(true);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, Fragment_Home.getInstance("")).commit();

            }
        }
    }



}
