package kr.or.dgit.it.cosmeticmngapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends android.support.v7.app.AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SendAdapter{
    private static final String TAG = "Main";
    DrawerLayout drawerLayout;
    boolean permission;
    public static int fragNum = 1;
    android.support.v7.app.ActionBar actionBar;
    private static MyItemList.MyAdapter sendAdapter;

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
        setTitleName();

        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        toolbar.setNavigationIcon(R.drawable.menu);

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

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        /*Button closeNavi = navigationView.findViewById(R.id.naviCloseBtn);
        closeNavi.setOnClickListener(this);*/

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
    }

    private void setTitleName() {
        if(fragNum == 1){
            actionBar.setTitle("화장품");
        }else if(fragNum == 2){
            actionBar.setTitle("화장도구");
        }else if(fragNum == 3){
            actionBar.setTitle("렌즈");
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
        }
        setTitleName();

        MyItemList fragment = MyItemList.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt("frag", fragNum);
        fragment.setArguments(bundle);

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        //fragmentTransaction.detach(fragment).attach(fragment);
        fragmentTransaction.commit();

        android.support.v4.widget.DrawerLayout drawer = (android.support.v4.widget.DrawerLayout) findViewById(R.id.drawerlayout);
        drawer.closeDrawer(android.support.v4.view.GravityCompat.START);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(android.support.v4.view.GravityCompat.START);
                return true;
            case R.id.searchIcon:
                drawerLayout.openDrawer(android.support.v4.view.GravityCompat.START);
                return true;
            case R.id.registerIcon:
                android.content.Intent intent = new android.content.Intent(this, AddActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSendAdapter(MyItemList.MyAdapter adapter) {
        sendAdapter = adapter;
        sendAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if(sendAdapter != null){
            Log.d(TAG, "onSendAdapter: Main 전"+sendAdapter.getItemCount());
            sendAdapter.notifyDataSetChanged();
            Log.d(TAG, "onSendAdapter: Main 후"+sendAdapter.getItemCount());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        if(sendAdapter != null){
            Log.d(TAG, "onSendAdapter: Main 전"+sendAdapter.getItemCount());
            sendAdapter.notifyDataSetChanged();
            Log.d(TAG, "onSendAdapter: Main 후"+sendAdapter.getItemCount());
        }
    }
}
