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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;

import kr.or.dgit.it.cosmeticmngapp.db.DBhelper;

public class AddActivity extends AppCompatActivity {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;
    private String mCurrentPhotoPath;
    private Uri photoUri = null;
    private Uri albumUri = null;
    private boolean album;
    EditText openEditdate;
    EditText endEditdate;
    int i;



    private CharSequence[] info;
    private TextView categoryTV;


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






        imgview = (ImageView)findViewById(R.id.img);
    }

    public void categoryBtnClick(View view) {
        DBhelper helper = new DBhelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        if(MainActivity.fragNum == 1){
            cursor = db.rawQuery("select name from cosmeticCategory order by name", null);
        }else if(MainActivity.fragNum == 2){
            cursor = db.rawQuery("select name from cosmeticTools order by name", null);
        }else if(MainActivity.fragNum == 3){
            cursor = db.rawQuery("select name from lensCategory order by name", null);
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        // List Adapter 생성
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);

        while(cursor.moveToNext()){
            adapter.add(cursor.getString(0));
        }

        alertBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                categoryTV.setText(adapter.getItem(which));
                dialog.dismiss();
            }
        });
        alertBuilder.show();

        cursor.close();
        db.close();
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

 /*   private void showDayDialog(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DAY_OF_MONTH);

        datedialog = new Dialog(this);
        datedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        datedialog.setContentView(R.layout.calendar_numberpicker_layout);
        confirmBtn = datedialog.findViewById(R.id.btn_dialog_confirm);
        cancelBtn = datedialog.findViewById(R.id.btn_dialog_cancel);

        yearp = datedialog.findViewById(R.id.picker_year);
        monthp = datedialog.findViewById(R.id.picker_month);
        datep = datedialog.findViewById(R.id.picker_date);

        yearp.setMinValue(year-10);
        yearp.setMaxValue(year+10);
        yearp.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);  // 데이터 선택 시 editText 방지
        yearp.setValue(year);
        yearp.setWrapSelectorWheel(true);

        monthp.setMinValue(1);
        monthp.setMaxValue(12);
        monthp.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);  // 데이터 선택 시 editText 방지
        monthp.setValue(month+1);
        monthp.setWrapSelectorWheel(true);

      *//*  String[] stringDate = new String[31];
        for(i=0; i<31; i++){
            stringDate[i] = Integer.toString(i+1);
        }
        datep.setDisplayedValues(stringDate);*//*
        if(monthp.getValue()==1||monthp.getValue()==3||monthp.getValue()==5||monthp.getValue()==9||monthp.getValue()==11){
            datep.setMaxValue(30);
        }else{
            datep.setMaxValue(31);
        }

        datep.setMinValue(1);
        datep.setValue(date);
        datep.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);  // 데이터 선택 시 editText 방지
        datep.setWrapSelectorWheel(true);

        datedialog.show();
    }*/



    public void opendateClick(View view) {
        CalendarValue();
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                pYear = year;
                pMonth = monthOfYear+1;
                pDate = dayOfMonth;
                openEditdate.setText(String.format("%d - %d -%d", pYear, pMonth, pDate));
            }
        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditdate.setText(String.format("%d - %d -%d", pYear, pMonth, pDate));
                datedialog.dismiss();
            }
        });
        datedialog.show();

        /*datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        });
        openEditdate.setText(String.format("%d - %d - %d",datePicker.getYear(),(datePicker.getMonth()+1),datePicker.getDayOfMonth()));
*/





        /*DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                openEditdate.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            }
        },year,month,day);
        datePickerDialog.show();*/
      /*  showDayDialog();
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditdate.setText(String.valueOf(yearp.getValue())+"-"+String.valueOf(monthp.getValue())+"-"+String.valueOf(datep.getValue()));
                datedialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datedialog.cancel();
            }
        });*/

    }

    private void CalendarValue() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        pYear = year;
        pMonth = month+1;
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
        builder.setItems(info, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, int which) {
                switch (which)
                {
                    case 0:
                        CalendarValue();
                        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                pYear = year;
                                pMonth = monthOfYear+1;
                                pDate = dayOfMonth;
                                endEditdate.setText(String.format("%d - %d -%d", pYear, pMonth, pDate));
                            }
                        });


                        confirmBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                endEditdate.setText(String.format("%d - %d -%d", pYear, pMonth, pDate));
                                datedialog.dismiss();
                            }
                        });
                        datedialog.show();

                       /* showDayDialog();
                        confirmBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                endEditdate.setText(String.valueOf(yearp.getValue())+"-"+String.valueOf(monthp.getValue())+"-"+String.valueOf(datep.getValue()));
                                datedialog.dismiss();
                            }
                        });

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                datedialog.cancel();
                            }
                        });
*/
                        break;
                    case 1:
                        Toast.makeText(AddActivity.this, "자동 입력", Toast.LENGTH_SHORT).show();
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
