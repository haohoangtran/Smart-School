package com.example.quanla.smartschool.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.quanla.smartschool.R;
import com.example.quanla.smartschool.database.request.UrlImage;
import com.example.quanla.smartschool.eventbus.UploadImageSuccusEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddStudentActivity extends AppCompatActivity {
    private static final String TAG = AddStudentActivity.class.toString();
    @BindView(R.id.et_nameStudent)
    EditText etName;
    @BindView(R.id.et_msv)
    EditText msv;
    @BindView(R.id.bt_capture)
    Button btCapture;
    @BindView(R.id.bt_local)
    Button btLocal;

    Map uploadResult;
    ProgressDialog progress;
    String path;
    private String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Intent intent;
    private String url;

    private static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int PERMISSION_ALL = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        ButterKnife.bind(this);
        addListener();
    }

    private void addListener() {
        btLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY_REQUEST);
            }
        });
        btCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Log.d(TAG, "1");
                doAction();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_class, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void doAction() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            Log.d(TAG, "3");
            try {
                File file = getFile();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                Log.d(TAG, "2");
            } catch (Exception e) {
                Log.e(TAG, String.format("doAction: %s", e.toString()));
            }
        } else {
            File file = getFile();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }

    private File getFile() {
        File foder = new File("sdcard/camera_app");
        if (!foder.exists()) {
            foder.mkdir();
        }
        File file = new File(foder, "cam_image.jpg");
        return file;
    }

    public void setBtSummit(File file) {
        Cloudinary cloudinary = new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", "dhtl",
                        "api_key", "767781774363334",
                        "api_secret", "AC5_uhn8LY2JaiWPeONIhz6ZLPg")
        );

        uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            url = (String) uploadResult.get("url");
            UrlImage urlImage = new UrlImage(url);
            Log.e(TAG, String.format("setBtSummit: %s", url));
            EventBus.getDefault().post(new UploadImageSuccusEvent(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            path = cursor.getString(columnIndex);
            cursor.close();
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            path = "sdcard/camera_app/cam_image.jpg";
            Log.e(TAG, String.format("onActivityResult: %s", (new File(path)).getTotalSpace()));
        }
        if (path != null) {
            AddStudentActivity.Retrievedata retrievedata = new AddStudentActivity.Retrievedata();
            retrievedata.execute(path);
        }
        if (resultCode != RESULT_CANCELED) {
            progress = ProgressDialog.show(this, "Đang tải",
                    "Đang upload ảnh...", true);
        }
    }
    class Retrievedata extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            setBtSummit(new File(params[0]));
            return null;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {

        }
        return super.onOptionsItemSelected(item);

    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
