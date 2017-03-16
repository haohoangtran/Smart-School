package com.example.quanla.smartschool.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.quanla.smartschool.R;
import com.example.quanla.smartschool.adapter.StudentListAdapter;
import com.example.quanla.smartschool.database.DbStudentContext;
import com.example.quanla.smartschool.database.model.Checkin;
import com.example.quanla.smartschool.database.model.StudentIdCheckIn;
import com.example.quanla.smartschool.eventbus.GetDataFaildedEvent;
import com.example.quanla.smartschool.eventbus.GetDataSuccusEvent;
import com.example.quanla.smartschool.eventbus.GetStudentSuccusEvent;
import com.example.quanla.smartschool.networks.NetContextLogin;
import com.example.quanla.smartschool.networks.services.CheckinService;
import com.example.quanla.smartschool.sharePrefs.SharedPrefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentListActivity extends AppCompatActivity {
    private static final String TAG = StudentListActivity.class.toString();
    public static List<String> studentids = new Vector<>();
    ProgressDialog progress;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    @BindView(R.id.fab_check)
    FloatingActionButton fab;
    String user="";
    StudentListAdapter studentListAdapter = new StudentListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        ButterKnife.bind(this);
        user= SharedPrefs.getInstance().getLoginCredentials().getUsername();
        EventBus.getDefault().register(this);
        progress = ProgressDialog.show(this, "Loading",
                "Please waiting...", true);
        progress.show();
        addListener();
    }

    private void addListener() {
        if (!user.equals(ListClassActivity.userKey)) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StudentListActivity.this, UploadActivity.class);
                    startActivity(intent);
                }
            });
            Log.e(TAG, "addListener: vào saii" );
            Log.e(TAG, String.format("addListener: %s", ListClassActivity.userKey) );
        }
        else {
            Log.e(TAG, "addListener: vào đúng" );
            fab.setImageResource(R.drawable.ic_add_white_24px);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(StudentListActivity.this,AddStudentActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            if (studentids.size() != 0) {
                final Checkin checkin = new Checkin(DbStudentContext.instance.getNameGroup(), studentids);
                CheckinService netContextLogin = NetContextLogin.instance.create(CheckinService.class);
                netContextLogin.addNewCheckin(checkin).enqueue(new Callback<Checkin>() {
                    @Override
                    public void onResponse(Call<Checkin> call, Response<Checkin> response) {
                        Log.e(TAG, String.format("onResponse: %s", response.body().toString()));
                        Toast.makeText(StudentListActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                        studentids.clear();
                    }

                    @Override
                    public void onFailure(Call<Checkin> call, Throwable t) {
                        Log.e(TAG, "onFailure: That bai");
                    }
                });
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onCheckStudentInServerSuccus(GetStudentSuccusEvent event) {
        DbStudentContext.instance.setStudentIsHere(event.getStudent());
        studentListAdapter.notifyDataSetChanged();
        EventBus.getDefault().removeStickyEvent(GetStudentSuccusEvent.class);

    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDataLoadComplete(GetDataSuccusEvent event) {
        progress.dismiss();
        rv_list.setAdapter(studentListAdapter);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv_list.addItemDecoration(dividerItemDecoration);
        Log.e(TAG, String.format("onDataLoadComplete: dsadksa"));
        EventBus.getDefault().removeStickyEvent(GetDataSuccusEvent.class);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getDataFail(GetDataFaildedEvent event) {
        progress.dismiss();
        EventBus.getDefault().removeStickyEvent(GetDataFaildedEvent.class);
        Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
