package com.example.quanla.smartschool.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.quanla.smartschool.R;
import com.example.quanla.smartschool.adapter.ClassListAdapter;
import com.example.quanla.smartschool.database.DbClassContext;
import com.example.quanla.smartschool.database.DbListCheckin;
import com.example.quanla.smartschool.eventbus.GetDataFaildedEvent;
import com.example.quanla.smartschool.eventbus.GetDataSuccusEvent;
import com.example.quanla.smartschool.sharePrefs.SharedPrefs;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListClassActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  final String TAG = ListClassActivity.class.toString();
    @BindView(R.id.rv_class_list)
    RecyclerView rvClassList;
    ProgressDialog progress;
    String user="haoht";
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_class);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
//            user = SharedPrefs.getInstance().getLoginCredentials().getUsername();
            Log.e(TAG, "onCreate: vào đây" );
            if (user.equals("haoht")) {
                Log.e(TAG, "onCreate: Vào tiếp" );
                fab.setVisibility(View.VISIBLE);
            }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        DbListCheckin.instance.getAllListCheckin();
        DbClassContext.instance.getAllGroup();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setupUI();
    }
    @OnClick(R.id.fab)
    public void onFabClick(){
        Intent intent=new Intent(this,AddClassActivity.class);
        startActivity(intent);
    }

    public void setupUI() {
        progress = ProgressDialog.show(this, "Loading",
                "Please waiting...", true);
        if (DbClassContext.instance.getStudents() != null || DbClassContext.instance.getStudents().size() == 0) {
            progress.dismiss();
            DbClassContext.instance.getAllGroup();
            rvClassList.setAdapter(new ClassListAdapter(this));
            rvClassList.setLayoutManager(new LinearLayoutManager(this));
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            rvClassList.addItemDecoration(dividerItemDecoration);
        } else {
            progress.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getDataSuccus(GetDataSuccusEvent event) {
        progress.dismiss();
        rvClassList.setAdapter(new ClassListAdapter(this));
        rvClassList.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvClassList.addItemDecoration(dividerItemDecoration);
        Toast.makeText(this, "Load completed", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().removeStickyEvent(GetDataSuccusEvent.class);
    }

    public void getDataFailed(GetDataFaildedEvent event) {
        progress.dismiss();
        Toast.makeText(this, "Load failed", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().removeStickyEvent(GetDataFaildedEvent.class);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_class, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list_class) {
        } else if (id == R.id.nav_tkb) {

        } else if (id == R.id.nav_checkin) {


        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_about_us) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
