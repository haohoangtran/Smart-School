package com.example.quanla.smartschool.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.quanla.smartschool.R;
import com.example.quanla.smartschool.database.DbClassContext;
import com.example.quanla.smartschool.database.DbStudentContext;
import com.example.quanla.smartschool.database.model.Student;
import com.example.quanla.smartschool.database.request.UrlImage;
import com.example.quanla.smartschool.database.respon.FaceId;
import com.example.quanla.smartschool.database.respon.IndentifyRespon;
import com.example.quanla.smartschool.database.respon.PersionFaceId;
import com.example.quanla.smartschool.database.respon.PersionId;
import com.example.quanla.smartschool.eventbus.GetFaceIdSuccusEvent;
import com.example.quanla.smartschool.eventbus.GetStudentSuccusEvent;
import com.example.quanla.smartschool.eventbus.IdentifySuccusEvent;
import com.example.quanla.smartschool.eventbus.UploadImageSuccusEvent;
import com.example.quanla.smartschool.eventbus.UploadPersonFaceToServerEvent;
import com.example.quanla.smartschool.networks.NetContextMicrosoft;
import com.example.quanla.smartschool.networks.jsonModels.IndentifyBody;
import com.example.quanla.smartschool.networks.services.FaceService;
import com.example.quanla.smartschool.networks.services.StudentService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.beans.IndexedPropertyChangeEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity {
    private static final String TAG = UploadActivity.class.toString();
    private int count = 0;
    private Context context;
    @BindView(R.id.cv_information)
    CardView cvInformation;
    @BindView(R.id.txt_namestudent)
    TextView txtName;
    @BindView(R.id.txt_doticay)
    TextView txtDoTincay;
    @BindView(R.id.ib_left)
    ImageButton ibRotationLeft;
    @BindView(R.id.ib_right)
    ImageButton ibRotationRight;
    @BindView(R.id.btn_capture)
    Button btCapture;
    @BindView(R.id.btn_fromLocal)
    Button btFromLocal;
    @BindView(R.id.img_photo)
    ImageView imgPhoto;
    @BindView(R.id.txt_idstudent)
    TextView txtID;
    Map uploadResult;
    ProgressDialog progress;

    private static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int PERMISSION_ALL = 3;

    String path;
    private String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Intent intent;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        EventBus.getDefault().register(this);
        context = this;
        ButterKnife.bind(this);
        addListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
            Retrievedata retrievedata = new Retrievedata();
            retrievedata.execute(path);
        }
        if (resultCode != RESULT_CANCELED) {
            progress = ProgressDialog.show(this, "Đang tải",
                    "Đang upload ảnh...", true);
        }
    }

    private void addListener() {
        btFromLocal.setOnClickListener(new View.OnClickListener() {
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

    @Subscribe
    public void uploadPersonFaceToServer(UploadPersonFaceToServerEvent event) {
        Log.e(TAG, "uploadPersonFaceToServer: Vào up ngược");
        StudentService studentService = NetContextMicrosoft.instance.create(StudentService.class);
        studentService.addPersionFace(DbStudentContext.instance.getIdGroup(),
                event.getStudent().getPersonid(), new UrlImage(url))
                .enqueue(new Callback<PersionFaceId>() {
                    @Override
                    public void onResponse(Call<PersionFaceId> call, Response<PersionFaceId> response) {
                        Toast.makeText(UploadActivity.this, "Đã thêm ảnh trên server", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, String.format("onResponse: %s", response.body().toString()));
                    }

                    @Override
                    public void onFailure(Call<PersionFaceId> call, Throwable t) {
                        Toast.makeText(UploadActivity.this, "Lỗi r :(", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Subscribe
    public void onIndentifyPerson(IdentifySuccusEvent event) {
        List<IndentifyRespon> indentifyRespons = event.getIndentifyRespons();
        Log.e(TAG, String.format("onIndentifyPerson: %s", "Xác minh người là ai!"));
        List<Student> students = new Vector<>();
        if (indentifyRespons.size() != 0) {
            for (int i = 0; i < indentifyRespons.size(); i++) {
                List<PersionId> persionIds = indentifyRespons.get(i).getPersonsList();
                for (int j = 0; j < persionIds.size(); j++) {
                    final Student student = DbStudentContext.instance.findStudent(persionIds.get(j).getPersonid());
                    if (student != null) {
                        Log.e(TAG, String.format("onIndentifyPerson: %s", indentifyRespons.get(i).getCandidates().get(0).getConfidence()));
                        students.add(student);
                        Picasso.with(this).load(student.getUrl()).resize(480, 720).centerInside().into(imgPhoto);
                        StringBuilder string = new StringBuilder();
                        string.append("Name: ").append(student.getName());
                        cvInformation.setVisibility(View.VISIBLE);
                        txtID.setText("Id: " + student.getIdStudent());
                        txtName.setText(string);
                        txtDoTincay.setText("Confidence: " + indentifyRespons.get(i).getCandidates().get(j).getConfidence() + "");
                        ibRotationLeft.setVisibility(View.VISIBLE);
                        ibRotationRight.setVisibility(View.VISIBLE);
                        ibRotationLeft.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                count -= 90;
                                Picasso.with(context)
                                        .load(student.getUrl())
                                        .resize(480, 720).centerInside()
                                        .rotate(count)
                                        .into(imgPhoto);

                            }
                        });
                        ibRotationRight.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                count += 90;
                                Picasso.with(context)
                                        .load(student.getUrl())
                                        .resize(480, 720).centerInside()
                                        .rotate(count)
                                        .into(imgPhoto);
                            }
                        });
                        EventBus.getDefault().post(new UploadPersonFaceToServerEvent(student));
                        EventBus.getDefault().postSticky(new GetStudentSuccusEvent(student));
                    }
                }
            }
            for (int i = 0; i < students.size(); i++) {
                Log.e(TAG, String.format("onIndentifyPerson: Phát hiện ra: %s", students.get(i).toString()));
            }
        } else {
            Toast.makeText(this, "Cannot indentify anyone", Toast.LENGTH_SHORT).show();
        }
        path = null;
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

    @Subscribe
    public void onUploadImageSuccus(UploadImageSuccusEvent event) {
        Log.e(TAG, "onUploadImageSuccus: vào event");
        FaceService faceService = NetContextMicrosoft.instance.create(FaceService.class);
        faceService.detectFace(new UrlImage(event.getUrl())).enqueue(new Callback<List<FaceId>>() {
            @Override
            public void onResponse(Call<List<FaceId>> call, Response<List<FaceId>> response) {
                List<FaceId> faceIds = response.body();
                if (faceIds.size() == 0) {
                    cvInformation.setVisibility(View.INVISIBLE);
                    ibRotationLeft.setVisibility(View.INVISIBLE);
                    ibRotationRight.setVisibility(View.INVISIBLE);
                    Toast.makeText(UploadActivity.this, "Không có khuôn mặt nào trong ảnh", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                } else {
                    EventBus.getDefault().post(new GetFaceIdSuccusEvent(faceIds));
                }
            }

            @Override
            public void onFailure(Call<List<FaceId>> call, Throwable t) {
                Log.e(TAG, String.format("onFailure: upload %s", t.toString()));
                progress.dismiss();
                Toast.makeText(UploadActivity.this, "Có lỗi xảy ra, mạng yếu :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe
    public void identifyFace(final GetFaceIdSuccusEvent event) {
        Log.e(TAG, "identifyFace: Vào xác minh người ");
        List<String> strings = new Vector<>();
        for (int i = 0; i < event.getFaceIds().size(); i++) {
            strings.add(event.getFaceIds().get(i).getFaceid());
        }
        FaceService faceService = NetContextMicrosoft.instance.create(FaceService.class);
        final IndentifyBody indentifyBody = new IndentifyBody(DbStudentContext.instance.getIdGroup(), strings);
        faceService.identifyFace(indentifyBody).enqueue(new Callback<List<IndentifyRespon>>() {
            @Override
            public void onResponse(Call<List<IndentifyRespon>> call, Response<List<IndentifyRespon>> response) {
                List<IndentifyRespon> responList = response.body();
                EventBus.getDefault().post(new IdentifySuccusEvent(responList));
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<List<IndentifyRespon>> call, Throwable t) {
                Toast.makeText(UploadActivity.this, "Lỗi mạng :((", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class Retrievedata extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            setBtSummit(new File(params[0]));
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.menu.check_menu) {
            Retrievedata retrievedata = new Retrievedata();
            retrievedata.execute(path);
            return true;
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