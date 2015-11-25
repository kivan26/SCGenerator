package com.example.milan.scgenerator.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.milan.scgenerator.R;
import com.example.milan.scgenerator.adapters.PageAdapter;
import com.example.milan.scgenerator.services.GenerateService;

import java.util.jar.Manifest;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends AppCompatActivity implements ServiceConnection
        , MaterialTabListener {
    private static final String TAG = "MainActivity";
    private boolean isBind;
    private PageAdapter adapter;
    public static GenerateService genService;
    @Bind(R.id.viewpage) public ViewPager viewPager;
    @Bind(R.id.materialTabHost) MaterialTabHost tabHost;
    private boolean landscape;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configuration configuration = getResources().getConfiguration();
        landscape = (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Butter knife lib
        ButterKnife.bind(this);

        // Page adapter
        adapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // Check is it app default
        isDefaultSmsApp();

        // Start service
        startService();
        viewPager.setOffscreenPageLimit(4);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });

        // Add all tabs
        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(tabHost.newTab().setText(adapter.getPageTitle(i)).setTabListener(this));
        }

        // If orientation is landscape
        if(landscape){
            toolbar.setVisibility(View.GONE);
            tabHost.setVisibility(View.GONE);
        }


    }


    /** Check is sms app default. */
    private void isDefaultSmsApp() {
        final String packageName = this.getPackageName();
        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(packageName)) {
            // App is not default
            dialog(packageName);
        }
    }


    private void askForCallPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_CALL_LOG)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_CALL_LOG}, 2);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == 2){
            Toast.makeText(this, "Thx", Toast.LENGTH_LONG).show();
        }
    }

    /** Alert dialog if app is not default.
     * @param pkName String */
    private void dialog(final String pkName)  {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set app for default");
        builder.setMessage("You have to set this app for your default sms app.");
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, pkName);
                startActivity(intent);
                //startActivityForResult(intent, 0);
            }
        });
        builder.create();
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }



    /** Start and bind service. */
    public void startService() {
        Intent intent = new Intent(this, GenerateService.class);
        startService(intent);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }


    /** Stop and unbind service. */
    public void stopService() {
        Intent intent = new Intent(this, GenerateService.class);
        unbindService(this);
        stopService(intent);
    }


    // Connect / bind
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        genService = ((GenerateService.ServiceBinder) service).getService();
        isBind = true;
        Log.i(TAG, "connected!");
    }


    // Disconnect /  unbind
    @Override
    public void onServiceDisconnected(ComponentName name) {
        isBind = false;
        Log.i(TAG, "disconnected!");
    }


    @Override
    protected void onDestroy() {
        if (isBind) {
            stopService();
        }
        super.onDestroy();
    }


    // Tabs listeners
    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }


}
