package com.example.mybus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mybus.databinding.ActivityMainBinding;
import com.example.mybus.databinding.NaviHeaderBinding;
import com.example.mybus.kakaoLogin.KakaoLogin;
import com.example.mybus.menu.HomeEditActivity;
import com.example.mybus.menu.LoginActivity;
import com.example.mybus.menu.MyAlarmActivity;
import com.example.mybus.menu.OpenSourceActivity;
import com.example.mybus.search.SearchActivity;
import com.example.mybus.viewmodel.MainViewModel;
import com.example.mybus.vo.User;
import com.google.android.material.navigation.NavigationView;
import com.kakao.sdk.user.UserApiClient;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Main extends AppCompatActivity {
    private KakaoLogin kakaoLogin;
    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navi;
    private MainViewModel mainViewModel;
    private View header;
    private NaviHeaderBinding headerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());
        initMenu();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getUser();
        chkUser();

    }

    public void initMenu(){
        kakaoLogin = new KakaoLogin();
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);
        drawerLayout = binding.drawer;
        navi = binding.navigation;
        header = navi.getHeaderView(0);
        headerBinding = NaviHeaderBinding.bind(header);

        // ??????????????? ????????? ?????? ?????????
        navi.setNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()){
                case R.id.move_login:
                    UserApiClient.getInstance().me((user, error) -> {
                        if (error != null){
                            // ????????? ??? ??? ??????
                            Intent intent2 = new Intent(this, LoginActivity.class);
                            startActivity(intent2);
                            finish();
                        }else{
                            // ????????? ??? ??????
                            mainViewModel.delete();
                            UserApiClient.getInstance().logout(errors ->{
                                return null;
                            });
                            Intent intent3 = new Intent(this, LoginActivity.class);
                            startActivity(intent3);
                            finish();
                        }
                        return null;
                    });
                    break;
                case R.id.move_my_alarm:
                    Intent goAlarm = new Intent(this, MyAlarmActivity.class);
                    startActivity(goAlarm);
                    break;
                case R.id.move_home_edit:
                    // ??? ?????? ???????????? ??????
                    Intent goHomeEdit = new Intent(this, HomeEditActivity.class);
                    startActivity(goHomeEdit);
                    break;
                case R.id.move_open_source:
                    // ???????????? ????????? ??????
                    Intent goOpenSource = new Intent(this, OpenSourceActivity.class);
                    startActivity(goOpenSource);
                    break;
            }
            return false;
        });
    }

    public void chkUser(){

        mainViewModel.mUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null){
                    Log.d("kkang", "logined user :" + user.getUser_name());
                    TextView txt = header.findViewById(R.id.name);
                    txt.setText(user.getUser_name());

                    ImageView img = header.findViewById(R.id.profile);
                    Glide.with(Main.this).load(user.getUser_thunbnail()).placeholder(R.drawable.ic_baseline_dehaze_24).error(R.drawable.ic_baseline_dehaze_24).into(img);
                }else{
                    TextView txt = header.findViewById(R.id.name);
                    txt.setText("?????????");

                    ImageView img = header.findViewById(R.id.profile);
                    Glide.with(Main.this).load(R.drawable.ic_baseline_home_24).placeholder(R.drawable.ic_baseline_dehaze_24).error(R.drawable.ic_baseline_dehaze_24).into(img);
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.toolbar_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);

            case R.id.toolbar_home_edit:
                // ??? ?????? ???????????? ????????????.
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }
}


  /* repository.delete();
        UserApiClient.getInstance().logout(error ->{
            return null;
        });

       */