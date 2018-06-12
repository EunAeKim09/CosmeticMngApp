package kr.or.dgit.it.cosmeticmngapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticToolsDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserLensDAO;
import kr.or.dgit.it.cosmeticmngapp.db.DBhelper;
import kr.or.dgit.it.cosmeticmngapp.dto.CosmeticCategoryDTO;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmetic;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmeticTools;
import kr.or.dgit.it.cosmeticmngapp.dto.UserLens;

public class AddActivity extends AppCompatActivity {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private Uri photoUri = null;
    EditText openEditdate;
    EditText endEditdate;
    EditText cosmeticName;
    EditText cosmeticMemo;
    int i;
    private String currentPhotoPath;    //실제 사진 파일 경로
    String mImageCaptureName;           //이미지 이름
    private boolean permission;
    private ImageView imgview;
    private Button confirmBtn;
    private Button cancelBtn;
    private NumberPicker yearp;
    private NumberPicker monthp;
    private NumberPicker datep;
    private Dialog datedialog;
    private DatePicker datePicker;
    private int pYear;
    private int pMonth;
    private int pDate;
    private TextView categoryTV;
    private ArrayAdapter<CosmeticCategoryDTO> adapter;
    TextView categoryId;
    private Bitmap bitmap;
    private String imagePath;
    UserCosmeticDAO userCosmeticDAO;
    private int cosId;
    private UserCosmeticToolsDAO userCosmeticToolsDAO;
    private UserLensDAO userLensDAO;
    private SimpleDateFormat simpleDateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_cosmetic);
        imgview = (ImageView) findViewById(R.id.img);
        categoryTV = (TextView) findViewById(R.id.category);

        openEditdate = findViewById(R.id.regi_cosmetic_open_date);
        openEditdate.setInputType(0);

        endEditdate = findViewById(R.id.regi_cosmetic_end_date);
        endEditdate.setInputType(0);

        cosmeticName = findViewById(R.id.regi_cosmetic_name);
        cosmeticMemo = findViewById(R.id.regi_cosmetic_memo);

        userCosmeticDAO = new UserCosmeticDAO(this);
        userCosmeticDAO.open();

        userCosmeticToolsDAO = new UserCosmeticToolsDAO(this);
        userCosmeticToolsDAO.open();

        userLensDAO = new UserLensDAO(this);
        userLensDAO.open();

        imgview = (ImageView) findViewById(R.id.img);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openEditdate.setFocusable(false);
        openEditdate.setFocusableInTouchMode(true);
        openEditdate.setFocusable(true);

        cosmeticName.setFocusable(false);
        cosmeticName.setFocusableInTouchMode(true);
        cosmeticName.setFocusable(true);


    }

    public void categoryBtnClick(View view) {
        SQLiteDatabase db = DBhelper.getInstance(this).getDb();
        Cursor cursor = null;
        if (MainActivity.fragNum == 1) {
            cursor = db.rawQuery("select * from cosmeticCategory order by name", null);
        } else if (MainActivity.fragNum == 2) {
            cursor = db.rawQuery("select * from cosmeticToolsCategory order by name", null);
        } else if (MainActivity.fragNum == 3) {
            cursor = db.rawQuery("select * from lensCategory order by name", null);
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        // List Adapter 생성
        adapter = new ArrayAdapter<CosmeticCategoryDTO>(this, android.R.layout.select_dialog_item);


        while (cursor.moveToNext()) {
            adapter.add(new CosmeticCategoryDTO(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4)));

        }

        alertBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                categoryTV.setText(adapter.getItem(which).getName());
                cosId = adapter.getItem(which).get_id();
                dialog.dismiss();
            }
        });
        alertBuilder.show();

        cursor.close();
    }

    public void cameraClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            permission = true;
        }

        if (!permission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    200);
            Toast.makeText(this, "권한을 허용하지않아 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (photoFile != null) {
                        photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    }
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/path/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".png";

        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/path/" + mImageCaptureName);
        currentPhotoPath = storageDir.getAbsolutePath();

        return storageDir;
    }

    private Bitmap rotate(Bitmap src, float degree) {
        Matrix matrix = new Matrix();    // Matrix 객체 생성
        matrix.postRotate(degree);       // 회전 각도 셋팅
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true); // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private void getPictureForPhoto() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, options);
        int dstWidth;
        int dstHeight;
        if(bitmap.getHeight()>bitmap.getWidth()){   //세로모드
            float ratioX = 720 / (float)bitmap.getWidth();
            float ratioY = 1280 / (float)bitmap.getHeight();

            dstWidth = Math.round(bitmap.getWidth() * ratioX);
            dstHeight = Math.round(bitmap.getHeight() * ratioY);

        }else{  //가로모드
            float ratioX = 1280 / (float)bitmap.getWidth();
            float ratioY = 720 / (float)bitmap.getHeight();

            dstWidth = Math.round(bitmap.getWidth() * ratioX);
            dstHeight = Math.round(bitmap.getHeight() * ratioY);
        }
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(currentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation;
        int exifDegree;

        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }
        imgview.setImageBitmap(rotate(resized, exifDegree)); //이미지 뷰에 비트맵 넣기
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            getPictureForPhoto();
        }else if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            sendPicture(data.getData());
        }
    }

    public void closeBtnClick(View view) {
        finish();
    }

    public void albumBtnClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            permission = true;
        }
        if (!permission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    200);
            Toast.makeText(this, "권한을 허용하지않아 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_FROM_ALBUM);
        }
    }

    private void sendPicture(Uri imgUri) {
        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        imgview.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
        Toast.makeText(this,"imagePath : "+imagePath, Toast.LENGTH_LONG).show();
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }



    public void opendateClick(View view) {
        CalendarValue();
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                pYear = year;
                pMonth = monthOfYear + 1;
                pDate = dayOfMonth;
/*
                String mDateFormat = "yyy - MM - dd";
                simpleDateFormat = new SimpleDateFormat(mDateFormat);

                simpleDateFormat.format(date);
                openEditdate.setText(simpleDateFormat.toString());
                openEditdate.setText(simpleDateFormat.format());*/
                openEditdate.setText(String.format("%d - %02d - %d", pYear, pMonth, pDate));
            }
        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditdate.setText(String.format("%d - %02d - %d", pYear, pMonth, pDate));
                datedialog.dismiss();
            }
        });
        datedialog.show();

    }

    private void CalendarValue() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        pYear = year;
        pMonth = month + 1;
        pDate = day;

        datedialog = new Dialog(this);
        datedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        datedialog.setContentView(R.layout.calendar_numberpicker_layout);
        confirmBtn = datedialog.findViewById(R.id.btn_dialog_confirm);
        cancelBtn = datedialog.findViewById(R.id.btn_dialog_cancel);
        datePicker = datedialog.findViewById(R.id.datepicker_dialog);
    }

    public void enddateClick(View view) {
        CharSequence info[] = new CharSequence[]{"직접 날짜 입력", "자동 입력"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog.Builder builder1 = builder.setItems(info, new DialogInterface.OnClickListener() {


            @Override
            public void onClick(final DialogInterface dialog, int which) {

                CalendarValue();
                switch (which) {
                    case 0:
                        EndtimeEditCalender(pYear, pMonth, pDate);
                        break;
                    case 1:
                        SQLiteDatabase db = DBhelper.getInstance(getApplicationContext()).getDb();
                        Cursor cursor = null;
                        if (MainActivity.fragNum == 1) {
                          addEndDate();
                        } else if (MainActivity.fragNum == 2) {
                            addEndDate();
                        } else if (MainActivity.fragNum == 3) {
                            addEndDate();
                        }
                        break;
                }
                dialog.dismiss();
            }
        });
        builder1.show();

    }
    private void addEndDate(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        pYear = year;
        pMonth = month + 1;
        pDate = day;

        int dur = 0;

        if(categoryTV.getText().equals("카테고리")||adapter.getCount()==0||adapter.isEmpty()){
            Toast.makeText(AddActivity.this,"카테고리를 선택해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < adapter.getCount(); i++) {

            if (adapter.getItem(i).getName().equals(categoryTV.getText())) {
                if (adapter.getItem(i).getDurationY() > 0) {
                    dur = adapter.getItem(i).getDurationY()*365;
                    break;
                }
                Log.d("getDurationM",adapter.getItem(i).getDurationM()+"..");
                if (adapter.getItem(i).getDurationM() > 0) {
                    dur = adapter.getItem(i).getDurationM()*30;
                    break;
                }
                if (adapter.getItem(i).getDurationD() > 0) {
                    dur = adapter.getItem(i).getDurationD();
                    break;
                }
            }
        }
        Log.d("error", String.valueOf(dur)+"일수..?");

        String open = String.valueOf(openEditdate.getText());
        if(open.isEmpty()||open.equals("")){
            Toast.makeText(AddActivity.this,"개봉일을 선택해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        String dateArray[] = open.split(" - ");
        c.set(Calendar.YEAR, Integer.parseInt(dateArray[0]));
        c.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]) - 1);

        c.add(Calendar.YEAR,0);
        c.add(Calendar.MONTH, 0);
        c.add(Calendar.DAY_OF_MONTH, dur);


        pYear = c.get(Calendar.YEAR);
        pMonth = c.get(Calendar.MONTH)+1;
        pDate = c.get(Calendar.DAY_OF_MONTH);
        endEditdate.setText(String.format("%d - %02d - %d", pYear, pMonth, pDate));
    }


    private void EndtimeEditCalender(int year, int monthOfYear, int dayOfMonth) {
        year = datePicker.getYear();
        monthOfYear = datePicker.getMonth();
        dayOfMonth = datePicker.getDayOfMonth();
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                pYear = year;
                pMonth = monthOfYear + 1;
                pDate = dayOfMonth;
                endEditdate.setText(String.format("%d - %02d - %d", pYear, pMonth, pDate));
            }
        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endEditdate.setText(String.format("%d - %02d - %d", pYear, pMonth, pDate));
                datedialog.dismiss();
            }
        });
        datedialog.show();
    }


    public void addCosmeticClick(View view) {
        String cosName = cosmeticName.getText().toString();
        String cosOpenDate = openEditdate.getText().toString();
        String cosEndDate = endEditdate.getText().toString();
        String cosMemo = cosmeticMemo.getText().toString();
        String cosImg = null;
        if(currentPhotoPath != null){
            cosImg = currentPhotoPath;
        }else if (imagePath != null ){
            cosImg = imagePath;
        }

        if(cosName.equals("")||cosName.isEmpty()||cosOpenDate.equals("")||cosOpenDate.isEmpty()||cosEndDate.equals("")||cosEndDate.isEmpty()||cosMemo.isEmpty()||cosMemo.equals("")){
            Toast.makeText(AddActivity.this,"이름,개봉일,교체권장일,메모를 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if (MainActivity.fragNum == 1) {
            UserCosmetic dto = new UserCosmetic();
            dto.setCate_id(cosId);
            dto.setEndDate(cosEndDate);
            dto.setOpenDate(cosOpenDate);
            dto.setImg(cosImg);
            dto.setMemo(cosMemo);
            dto.setName(cosName);
            dto.setFavorite(0);
            userCosmeticDAO.insertItem(dto);
        } else if (MainActivity.fragNum == 2) {
            UserCosmeticTools dto = new UserCosmeticTools();
            dto.setCate_id(cosId);
            dto.setEndDate(cosEndDate);
            dto.setOpenDate(cosOpenDate);
            dto.setImg(cosImg);
            dto.setMemo(cosMemo);
            dto.setName(cosName);
            dto.setFavorite(0);
            userCosmeticToolsDAO.insertItem(dto);
        } else if (MainActivity.fragNum == 3) {
            UserLens dto = new UserLens();
            dto.setCate_id(cosId);
            dto.setEndDate(cosEndDate);
            dto.setOpenDate(cosOpenDate);
            dto.setImg(cosImg);
            dto.setMemo(cosMemo);
            dto.setName(cosName);
            dto.setFavorite(0);
            userLensDAO.insertItem(dto);
        }
        finish();
    }


}