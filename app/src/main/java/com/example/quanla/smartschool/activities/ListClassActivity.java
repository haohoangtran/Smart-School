package com.example.quanla.smartschool.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
import com.example.quanla.smartschool.database.model.ClassStudent;
import com.example.quanla.smartschool.eventbus.GetDataFaildedEvent;
import com.example.quanla.smartschool.eventbus.GetDataSuccusEvent;
import com.example.quanla.smartschool.eventbus.OnLongClickEvent;
import com.example.quanla.smartschool.networks.NetContextMicrosoft;
import com.example.quanla.smartschool.networks.services.ClassService;
import com.example.quanla.smartschool.sharePrefs.LoginCredentials;
import com.example.quanla.smartschool.sharePrefs.SharedPrefs;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListClassActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = ListClassActivity.class.toString();
    public static final String userKey = "haoht";
    @BindView(R.id.rv_class_list)
    RecyclerView rvClassList;
    ProgressDialog progress;
    String user = "";
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_class);
        EventBus.getDefault().register(this);
//        SharedPrefs.getInstance().putLogin(new LoginCredentials(userKey,"12313","!@312412"));
        ButterKnife.bind(this);
        user = SharedPrefs.getInstance().getLoginCredentials().getUsername();
        if (user.equals(userKey)) {
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
    public void onFabClick() {
        Intent intent = new Intent(this, AddClassActivity.class);
        startActivity(intent);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void OnDeleteGroup(OnLongClickEvent e) {
        if (user.equals(userKey)) {
            deleteClassList(e.getClassStudent());
            EventBus.getDefault().removeStickyEvent(OnLongClickEvent.class);
        }
    }

    public void deleteClassList(final ClassStudent classStudent) {
        new AlertDialog.Builder(this)
                .setTitle("Xoá lớp")
                .setMessage("Xác nhận xoá lớp này?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, final int which) {
                        ClassService classService = NetContextMicrosoft.instance.create(ClassService.class);
                        classService.deleteGroupFace(classStudent.getPersongroupid()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(ListClassActivity.this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                                    DbClassContext.instance.getAllGroup();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(ListClassActivity.this, "Xoá lỗi!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DbClassContext.instance.getAllGroup();
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
