package kr.or.dgit.it.cosmeticmngapp;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends android.support.v7.app.AppCompatActivity implements android.support.design.widget.NavigationView.OnNavigationItemSelectedListener{
    android.support.v4.widget.DrawerLayout drawerLayout;
    private static String[] PERMISSIONS = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("화장품 관리");
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        toolbar.setNavigationIcon(R.drawable.menu);

        drawerLayout = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.openT,R.string.closeT);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        android.support.design.widget.NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        //permission
       /* if(!checkPermission(this, Manifest.permission.CAMERA)||!checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ||!checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            requestExternalPermissions(this);
        }*/
    }

    /*public static int checkSelfPermission(@NonNull android.content.Context context, @NonNull String permission){ //권한 체크
        if(permission == null){
            throw new IllegalArgumentException("permission is null");
        }
        return context.checkPermission(permission, android.os.Process.myPid(), Process.myUid());
    }

    public static boolean checkPermission(Activity activity, String permission){
        int permissionResult = ActivityCompat.checkSelfPermission(activity, permission);

        if(permissionResult == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    public static void requestPermissions(final  @NonNull Activity activity, final @NonNull String[] permissions, final int requestCode){
        if(Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }else if(activity instanceof ActivityCompat.OnRequestPermissionsResultCallback){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(()->{
                final int[] grantResults = new int[permissions.length];
                PackageManager packageManager = activity.getPackageManager();
                String packageName = activity.getPackageName();

                final int permissionCount = permissions.length;
                for(int i=0; i<permissionCount; i++){
                    grantResults[i] = packageManager.checkPermission(permissions[i], packageName);
                }
                ((ActivityCompat.OnRequestPermissionsResultCallback)activity).onRequestPermissionsResult(requestCode, permissions, grantResults);
            });
        }
    }

    public static void requestExternalPermissions(Activity activity){
        ActivityCompat.requestPermissions(activity, PERMISSIONS,1);
    }
*/

    @Override
    public boolean onNavigationItemSelected(@android.support.annotation.NonNull android.view.MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.app_menu1){
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, MyItemList.newInstance());
            fragmentTransaction.commit();
        }else if(id==R.id.app_menu2){
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, MyItemList.newInstance());
            fragmentTransaction.commit();
        }else if(id==R.id.app_menu3){
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, MyItemList.newInstance());
            fragmentTransaction.commit();
        }
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
}
