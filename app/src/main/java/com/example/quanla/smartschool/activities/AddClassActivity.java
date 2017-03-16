package com.example.quanla.smartschool.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanla.smartschool.R;
import com.example.quanla.smartschool.networks.NetContextMicrosoft;
import com.example.quanla.smartschool.networks.jsonModels.AddNewGroupBody;
import com.example.quanla.smartschool.networks.services.ClassService;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddClassActivity extends AppCompatActivity {
    @BindView(R.id.et_nameclass)
    EditText name;
    @BindView(R.id.et_classroom)
    EditText classRoom;
    @BindView(R.id.et_datestart)
    EditText dateStart;
    @BindView(R.id.et_dateend)
    EditText dateEnd;
    @BindView(R.id.et_datetime)
    EditText timeStudy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            ClassService classService = NetContextMicrosoft.instance.create(ClassService.class);
            StringBuilder str = new StringBuilder();
            String nameClass = name.getText().toString();
            String room = classRoom.getText().toString();
            String dateS = dateStart.getText().toString();
            String dateE = dateEnd.getText().toString();
            String tiet = timeStudy.getText().toString();
            if (nameClass.isEmpty() || room.isEmpty() || dateE.isEmpty() || dateS.isEmpty() || tiet.isEmpty()) {
                Toast.makeText(this, "Các trường không được để trống!", Toast.LENGTH_SHORT).show();
                return true;
            }
            str.append(room).append("\"")
                    .append(dateS).append("\"")
                    .append(dateE).append("\"")
                    .append(tiet);
            AddNewGroupBody addNewGroupBody = new AddNewGroupBody(nameClass,str.toString());
            classService.addNewGroupFace(UUID.randomUUID().toString(),addNewGroupBody ).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code()==200) {
                        Toast.makeText(AddClassActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        AddClassActivity.this.onBackPressed();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AddClassActivity.this, "Lỗi mạng!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}
