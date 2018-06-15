package kr.or.dgit.it.cosmeticmngapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import kr.or.dgit.it.cosmeticmngapp.db.DBhelper;

import static kr.or.dgit.it.cosmeticmngapp.R.string.addAlldel;

public class MainActivity extends android.support.v7.app.AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "Main";
    DrawerLayout drawerLayout;
    boolean permission;
    public static int fragNum = 1;
    android.support.v7.app.ActionBar actionBar;
    public static TextView emptyTV;
    private MyItemList fragment;
    AlertDialog alertDialog;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        toolbar.setNavigationIcon(R.drawable.menu);

        setTitleName();

        drawerLayout = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.openT,R.string.closeT);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        android.support.design.widget.NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        MyItemList fragment = MyItemList.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt("frag", 1);
        fragment.setArguments(bundle);

        emptyTV = (TextView) findViewById(R.id.emptyTV);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        Button closeNavi = navigationView.findViewById(R.id.naviCloseBtn);
       /* closeNavi.setOnClickListener();*/

        //permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            permission = true;
        }

        if(!permission){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    200);
        }

        Intent serviceIntent = new Intent(this, AlarmService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isServiceRunningCheck();
    }

    public boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("kr.or.dgit.it.cosmeticmngapp.AlarmService".equals(service.service.getClassName())) {
                return true;
            }
            Intent serviceIntent = new Intent(this, AlarmService.class);
            startService(serviceIntent);
        }
        return false;
    }

    private void setTitleName() {
        if(fragNum == 1){
            actionBar.setTitle("화장품");
        }else if(fragNum == 2){
            actionBar.setTitle("화장도구");
        }else if(fragNum == 3){
            actionBar.setTitle("렌즈");
        }else if(fragNum == 4){
            actionBar.setTitle("설정");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@android.support.annotation.NonNull android.view.MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.app_menu1){
            fragNum = 1;
        }else if(id==R.id.app_menu2){
            fragNum = 2;
        }else if(id==R.id.app_menu3){
            fragNum = 3;
        }else if(id==R.id.app_menu4) {
            fragNum = 4;
        }

        setTitleName();
        fragment = MyItemList.newInstance();

        ActionMenuItemView menuItemRegister = (ActionMenuItemView)findViewById(R.id.registerIcon);
        ActionMenuItemView menuItemSearch = (ActionMenuItemView)findViewById(R.id.searchIcon);
        ActionMenuItemView menuItemFavorite = (ActionMenuItemView)findViewById(R.id.favoriteIcon);
        if(fragNum == 4){
            Fragment settingFragment = new SettingFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, settingFragment).commit();

            menuItemRegister.setVisibility(View.INVISIBLE);
            menuItemSearch.setVisibility(View.INVISIBLE);
            menuItemFavorite.setVisibility(View.INVISIBLE);

        }else{
            Bundle bundle = new Bundle();
            bundle.putInt("frag", fragNum);
            fragment.setArguments(bundle);
            fragment.getListDatas();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            menuItemRegister.setVisibility(View.VISIBLE);
            menuItemSearch.setVisibility(View.VISIBLE);
            menuItemFavorite.setVisibility(View.VISIBLE);

        }

        drawerLayout.closeDrawer(android.support.v4.view.GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        android.view.MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        int favorite = 0;
        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(android.support.v4.view.GravityCompat.START);
                return true;
            case R.id.searchIcon:
                Intent intentSearch = new  Intent(this, SelectActivity.class);
                startActivity(intentSearch);
                return true;
            case R.id.registerIcon:
                Intent intentRegister = new Intent(this, AddActivity.class);
                startActivity(intentRegister);
                return true;
            case  R.id.delIcon:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.trash);
                builder.setTitle("삭제");
                builder.setMessage("정말 삭제 하시겠습니까?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"삭제합니다.",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"취소하셨습니다.",Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog=builder.create();
                alertDialog.show();

            case R.id.favoriteIcon:
                fragment = MyItemList.newInstance();
                if(favorite==0){
                    fragment.getfavoriteListDatas();
                    favorite = 1;
                }else{
                    fragment.getListDatas();
                    favorite = 0;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(android.support.v4.view.GravityCompat.START)){
            drawerLayout.closeDrawer(android.support.v4.view.GravityCompat.START);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("정말 종료하시겠습니까?");
            builder.setPositiveButton("확인", dialogListener);
            builder.setNegativeButton("취소", null);

            alertDialog=builder.create();
            alertDialog.show();
        }
    }

    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(dialog==alertDialog && which==DialogInterface.BUTTON_POSITIVE){
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        DBhelper.getInstance(this).dbClose();
    }
}