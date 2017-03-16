package com.example.quanla.smartschool.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dd.CircularProgressButton;
import com.example.quanla.smartschool.R;
import com.example.quanla.smartschool.database.DbClassContext;
import com.example.quanla.smartschool.database.model.ClassStudent;
import com.example.quanla.smartschool.networks.NetContextLogin;
import com.example.quanla.smartschool.networks.NetContextMicrosoft;
import com.example.quanla.smartschool.networks.jsonModels.JsonBody;
import com.example.quanla.smartschool.networks.jsonModels.ResponseBody;
import com.example.quanla.smartschool.networks.services.ClassService;
import com.example.quanla.smartschool.networks.services.UserService;
import com.example.quanla.smartschool.sharePrefs.LoginCredentials;
import com.example.quanla.smartschool.sharePrefs.SharedPrefs;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "test";

    private EditText edt_username;
    private EditText edt_password;

    private CircularProgressButton btn_login;
    private CircularProgressButton btn_register;

    private TextInputLayout tilUsername;
    private TextInputLayout tilPassword;

    private String username;
    private String password;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_login);
        }catch (UnsupportedOperationException e){
            Toast.makeText(this, "Không hỗ trợ animation phiên bản android này", Toast.LENGTH_SHORT).show();
        }
        setupUI();
        addListener();
    }

    public void setupUI() {
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);

        btn_login = (CircularProgressButton) findViewById(R.id.btn_login);
        btn_register = (CircularProgressButton) findViewById(R.id.btn_register);

        tilUsername = (TextInputLayout) findViewById(R.id.til_username);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
    }

    public void attempLogin() {
        username = edt_username.getText().toString();
        password = edt_password.getText().toString();
        sendLogin(username, password);

    }

    public void sendLogin(String username, String password) {
        btn_login.setIndeterminateProgressMode(true);
        btn_login.setProgress(1);
        UserService service = NetContextLogin.instance.create(UserService.class);

        MediaType jsonType = MediaType.parse("application/json");
        String loginJson = (new Gson()).toJson(new JsonBody(username, password));
        RequestBody loginBody = RequestBody.create(jsonType, loginJson);

        Call<ResponseBody> loginCall = service.login(loginBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                if (response.code() == 200) {
                    token = responseBody.get_id().get$oid();
                    Log.d(TAG, String.format("token: %s", token));
                    onLoginSuccess();
                    btn_login.setProgress(100);
                    //progress.dismiss();
                } else {
                    btn_login.setProgress(-1);
                    btn_register.setEnabled(true);
                    shakeText();
                    try {
                        Thread.sleep(2000);
                        btn_login.setProgress(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                //progress.dismiss();
                btn_login.setProgress(-1);
                btn_register.setEnabled(true);
                shakeText();
                try {
                    Thread.sleep(2000);
                    btn_login.setProgress(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void onLoginSuccess() {
        SharedPrefs.getInstance().putLogin(new LoginCredentials(username, password, token));
        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
        gotoMainActivity();
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, ListClassActivity.class);
        startActivity(intent);
        intent.putExtra("name",username);
        overridePendingTransition(R.transition.slide_in, R.transition.slide_out);
    }

    public void shakeText() {
        YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(5)
                .playOn(findViewById(R.id.edt_username));

        YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(5)
                .playOn(findViewById(R.id.edt_password));

        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }

    public void sendRegister(String username, String password) {
        btn_register.setIndeterminateProgressMode(true);
        btn_register.setProgress(1);
        UserService service = NetContextLogin.instance.create(UserService.class);

        MediaType jsonType = MediaType.parse("application/json");
        String registerJson = (new Gson()).toJson(new JsonBody(username, password));
        RequestBody registerBody = RequestBody.create(jsonType, registerJson);

        Call<ResponseBody> registerCall = service.register(registerBody);
        registerCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(LoginActivity.this, "register success", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, String.format("register %s", response.body()));
                    btn_register.setProgress(100);
                } else {
                    Toast.makeText(LoginActivity.this, "Username existed", Toast.LENGTH_SHORT).show();
                    btn_register.setProgress(-1);
                    shakeText();
                    try {
                        Thread.sleep(2000);
                        btn_register.setProgress(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                btn_login.setEnabled(true);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                btn_register.setProgress(-1);
                btn_login.setEnabled(true);
                shakeText();
                try {
                    Thread.sleep(2000);
                    btn_register.setProgress(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public void tryRegister() {
        int checkun = 0;

        int checkpw = 0;
        username = edt_username.getText().toString();
        password = edt_password.getText().toString();
        if (username.length() < 5) checkun = 1;

        for (int i = 0; i < username.length(); i++) {
            char temp = username.charAt(i);
            if (temp < '0' || (temp > '9' && temp < 'A') || (temp > 'Z' && temp < 'a') || temp > 'z') {
                checkun = 2;
            }
        }
        if (password.length() < 5) {
            checkpw = 1;
        }
        for (int i = 0; i < password.length(); i++) {
            char temp = password.charAt(i);
            if (temp < '0' || (temp > '9' && temp < 'A') || (temp > 'Z' && temp < 'a') || temp > 'z') {
                checkpw = 2;
            }
        }

        if (checkpw == 0 && checkun == 0) sendRegister(username, password);
        else {
            if (checkun == 1) {
                tilUsername.setError("Username must have at least 5 characters");
            } else {
                tilUsername.setError("Letters and numbers are permitted");
            }
            if (checkpw == 1) {
                tilPassword.setError("Password must have at least 5 characters");
            } else {
                tilPassword.setError("Letters and numbers are permitted");
            }
        }
        btn_login.setEnabled(true);
    }

    public void addListener() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click register");
                btn_register.setProgress(0);
                btn_login.setEnabled(false);
                tryRegister();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_login.setProgress(0);
                btn_register.setEnabled(false);
                attempLogin();
            }
        });
    }
}
