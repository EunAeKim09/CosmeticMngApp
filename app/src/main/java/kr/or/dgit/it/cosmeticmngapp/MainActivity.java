package kr.or.dgit.it.cosmeticmngapp;

import android.Manifest;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticToolsDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserLensDAO;
import kr.or.dgit.it.cosmeticmngapp.db.DBhelper;

public class MainActivity extends android.support.v7.app.AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "Main";
    DrawerLayout drawerLayout;
    boolean permission;
    public static int fragNum = 1;
    android.support.v7.app.ActionBar actionBar;
    public static TextView emptyTV;
    private MyItemList fragment;
    AlertDialog alertDialog;
    private BottomNavigationView bottomNavigationView;
    boolean showcheckbox = false;
    UserCosmeticDAO userCosmeticDAO;
    UserLensDAO userLensDAO;
    UserCosmeticToolsDAO userCosmeticToolsDAO;
    ArrayList<Integer> numberlist = new ArrayList<>();
    int favorite = 0;
    private Toolbar toolbar;
    Handler checkedhandler;

    public static MainActivity newInstance() {
        return new MainActivity();
    }

    public Handler getCheckedhandler() {
        return checkedhandler;
    }

    public void setCheckedhandler(Handler checkedhandler) {
        this.checkedhandler = checkedhandler;
    }


    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userCosmeticDAO = new UserCosmeticDAO(this);
        userCosmeticToolsDAO = new UserCosmeticToolsDAO(this);
        userLensDAO = new UserLensDAO(this);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openT, R.string.closeT);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        fragNum = 1;
        MyItemList fragment = MyItemList.newInstance();
        fragment.setToolbarHandler(toolbarHandler);
        Bundle bundle = new Bundle();
        bundle.putInt("frag", fragNum);
        fragment.setArguments(bundle);
        //fragment.getListDatas();

        setTitleName();

        emptyTV = (TextView) findViewById(R.id.emptyTV);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

/*        Button closeNavi = navigationView.findViewById(R.id.naviCloseBtn);
        Log.d(TAG, "closeNavi: " + closeNavi.getText());
        closeNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(android.support.v4.view.GravityCompat.START);
            }
        });*/

        //permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            permission = true;
        }

        if (!permission) {
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
        if (fragNum == 1) {
            actionBar.setTitle("화장품");
        } else if (fragNum == 2) {
            actionBar.setTitle("화장도구");
        } else if (fragNum == 3) {
            actionBar.setTitle("렌즈");
        } else if (fragNum == 4) {
            actionBar.setTitle("설정");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@android.support.annotation.NonNull android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.app_menu1) {
            fragNum = 1;
        } else if (id == R.id.app_menu2) {
            fragNum = 2;
        } else if (id == R.id.app_menu3) {
            fragNum = 3;
        } else if (id == R.id.app_menu4) {
            fragNum = 4;
        }

        setTitleName();

        fragment = MyItemList.newInstance();
        ActionMenuItemView menuItem1 = findViewById(R.id.searchIcon);
        /*ActionMenuItemView menuItem2 = findViewById(R.id.favoriteIcon);*/
        ActionMenuItemView menuItem3 = findViewById(R.id.registerIcon);
        if (fragNum == 4) {
            Fragment settingFragment = new SettingFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, settingFragment).show(settingFragment).commit();
            emptyTV.setVisibility(View.GONE);

            menuItem1.setVisibility(View.INVISIBLE);
            // menuItem2.setVisibility(View.INVISIBLE);
            menuItem3.setVisibility(View.INVISIBLE);
        } else {
            fragment.setToolbarHandler(toolbarHandler);
            Bundle bundle = new Bundle();
            bundle.putInt("frag", fragNum);
            fragment.setArguments(bundle);
            fragment.getListDatas();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            menuItem1.setVisibility(View.VISIBLE);
            //  menuItem2.setVisibility(View.VISIBLE);
            menuItem3.setVisibility(View.VISIBLE);
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

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(android.support.v4.view.GravityCompat.START);
                return true;
            case R.id.searchIcon:
                Intent intentSearch = new Intent(this, SelectActivity.class);
                startActivity(intentSearch);
                return true;
            case R.id.registerIcon:
                Intent intentRegister = new Intent(this, AddActivity.class);
                startActivity(intentRegister);
                return true;
            case R.id.delIcon:
                if (numberlist.size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.drawable.trash);
                    builder.setTitle("삭제");
                    builder.setMessage("정말 삭제 하시겠습니까?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "삭제합니다.", Toast.LENGTH_SHORT).show();
                            int num;
                            if (fragNum == 1 || fragNum == 2 || fragNum == 3) {
                                for (int a = 0; a < numberlist.size(); a++) {
                                    num = numberlist.get(a);
                                    Log.d("number::::::::::", num + "...................");
                                    Log.d("fragment", fragment + "..");
                                    Log.d("deleteLimit",numberlist.size()+"...");
                                    if (fragNum == 1) {
                                        userCosmeticDAO.deleteItemById(num);

                                    } else if (fragNum == 2) {
                                        userCosmeticToolsDAO.deleteItemById(num);
                                    } else if (fragNum == 3) {
                                        userLensDAO.deleteItemById(num);
                                    }

                                    fragment = MyItemList.newInstance();
                                    fragment.setToolbarHandler(toolbarHandler);

                                    Bundle bundle1 = new Bundle();
                                    bundle1.putInt("frag", fragNum);
                                    fragment.setArguments(bundle1);
                                    Message message = Message.obtain(toolbarHandler, 2, null);
                                    toolbarHandler.sendMessage(message);
                                    fragment.getListDatas();
                                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                }


                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "취소하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                }
                //case R.id.favoriteIcon:
            /*    getFragmentManager().beginTransaction().remove(fragment).commit();
                fragment = MyItemList.newInstance();
                fragment.setToolbarHandler(toolbarHandler);
                Bundle bundle3 = new Bundle();
                bundle3.putInt("frag", fragNum);
                fragment.setArguments(bundle3);
                if (favorite == 0) {
                    fragment.getfavoriteListDatas();
                    favorite = 1;
                    Log.d("Dddddddddddddd","들어왓음1");
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } else if (favorite == 1) {
                 *//*   fragment = MyItemList.newInstance();
                    fragment.setToolbarHandler(toolbarHandler);
                    Bundle bundle4 = new Bundle();
                    bundle4.putInt("frag", fragNum);
                    fragment.setArguments(bundle4);*//*
                    fragment.getListDatas();
                    favorite = 0;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }
                //getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                Log.d("Dddddddddddddd","들어왓음2");*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(android.support.v4.view.GravityCompat.START)) {
            drawerLayout.closeDrawer(android.support.v4.view.GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("정말 종료하시겠습니까?");
            builder.setPositiveButton("확인", dialogListener);
            builder.setNegativeButton("취소", null);

            alertDialog = builder.create();
            alertDialog.show();
        }
    }

    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog == alertDialog && which == DialogInterface.BUTTON_POSITIVE) {
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

    Handler toolbarHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //Toast.makeText(getApplicationContext(), "1111", Toast.LENGTH_SHORT).show();//툴바 바꾸기 ~~
                Log.d(TAG, "handlerMassea1");

                showcheckbox = true;

                invalidateOptionsMenu();
            } else if (msg.what == 2) {
                //Toast.makeText(getApplicationContext(), "2222", Toast.LENGTH_SHORT).show();//툴바 바꾸기 ~~
                showcheckbox = false;
                invalidateOptionsMenu();
            } else if (msg.what == 3) {
                //Toast.makeText(getApplicationContext(), "333333", Toast.LENGTH_SHORT).show();//툴바 바꾸기 ~~
                Log.d(TAG, "handlerMassea13" + msg.obj + "입니다ㅏㅏㅏㅏㅏㅏㅏ");
                numberlist.add(Integer.parseInt(String.valueOf(msg.obj)));

            }else if (msg.what == 4) {
                //Toast.makeText(getApplicationContext(), "333333", Toast.LENGTH_SHORT).show();//툴바 바꾸기 ~~

                String error_msg = String.format("%s", numberlist.size());
                Log.d(TAG,"아이템 갯수"+error_msg);
                numberlist.remove(Integer.parseInt(String.valueOf(msg.obj))-1);
                Log.d(TAG,"아이템  해제 갯수"+error_msg);

            }

          //  invalidateOptionsMenu();
        }
    };


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepare");

        if (showcheckbox == true) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.all_delete_button);

            Button button = findViewById(R.id.deleteAllbtn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (button.getText().equals("전체 선택")) {
                        Log.d("dddddddddddddddd", "전체 해제 들어왔음");
                        fragment = MyItemList.newInstance();
                        fragment.setToolbarHandler(toolbarHandler);

                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("frag", fragNum);
                        bundle1.putInt("allcheck", 4);
                        fragment.setArguments(bundle1);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                        button.setText("전체 해제");
                    } else if (button.getText().equals("전체 해제")) {
                        Log.d("dddddddddddddddd", "전체 선택 들어왔음");

                        fragment = MyItemList.newInstance();
                        fragment.setToolbarHandler(toolbarHandler);

                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("frag", fragNum);
                        bundle1.putInt("allcheck", 5);
                        fragment.setArguments(bundle1);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                        button.setText("전체 선택");
                    }
                    }
                });


            menu.removeItem(android.R.id.home);
            menu.removeItem(R.id.searchIcon);
            menu.removeItem(R.id.registerIcon);
            //  menu.removeItem(R.id.favoriteIcon);
            android.view.MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_del, menu);
        } else if (showcheckbox == false) {

            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(false);


            drawerLayout = findViewById(R.id.drawerlayout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openT, R.string.closeT);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            menu.removeItem(R.id.delIcon);

        }

        return true;
    }


    public void btnNaviClose(View view) {
        drawerLayout.closeDrawer(android.support.v4.view.GravityCompat.START);
    }
}