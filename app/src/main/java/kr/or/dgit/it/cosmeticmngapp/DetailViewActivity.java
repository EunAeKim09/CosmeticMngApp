package kr.or.dgit.it.cosmeticmngapp;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticToolsDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserLensDAO;
import kr.or.dgit.it.cosmeticmngapp.db.DBhelper;
import kr.or.dgit.it.cosmeticmngapp.dto.CosmeticCategoryDTO;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmetic;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmeticTools;
import kr.or.dgit.it.cosmeticmngapp.dto.UserLens;

public class DetailViewActivity extends AppCompatActivity{
    private int fragNum = MainActivity.fragNum;
    private UserCosmeticDAO cosmeticdao;
    private UserCosmeticToolsDAO cosmeticToolsdao;
    private UserLensDAO lensdao;
    private static final String TAG = "DetailViewActivity";
    private TextView categoryTV;
    private EditText name;
    private EditText openDate;
    private EditText endDate;
    private EditText memo;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private Uri photoUri = null;
    int i;
    private String currentPhotoPath;    //실제 사진 파일 경로
    String mImageCaptureName;           //이미지 이름
    private boolean permission;
    private ImageView imgview;
    private String num;
    private MyAdapter sendAdapter;
    private ArrayAdapter<CosmeticCategoryDTO> adapter;
    private int cosId;
    private String imagePath;
    private Button confirmBtn;
    private Button cancelBtn;
    private Dialog datedialog;
    private DatePicker datePicker;
    private int pYear;
    private int pMonth;
    private int pDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        imgview = findViewById(R.id.img);
        categoryTV = findViewById(R.id.category);
        name = findViewById(R.id.item_product_name);
        openDate = findViewById(R.id.item_opendate);
        endDate = findViewById(R.id.item_endDay);
        memo = findViewById(R.id.item_memo);

        num = getIntent().getStringExtra("idNum");
        Log.d(TAG, "Num : "+ num);

        cosmeticdao = new UserCosmeticDAO(this);
        cosmeticdao.open();

        cosmeticToolsdao = new UserCosmeticToolsDAO(this);
        cosmeticToolsdao.open();

        lensdao = new UserLensDAO(this);
        lensdao.open();

        if (fragNum == 1) {
            Cursor cursor = cosmeticdao.selectItemAll("_id=?", new String[]{num});
            while (cursor.moveToNext()){
                Cursor category = cosmeticdao.selectCateName("_id=?", new String[]{cursor.getString(7)});
                while(category.moveToNext()){
                    categoryTV.setText(category.getString(0));
                }
                name.setText(cursor.getString(1));
                openDate.setText(cursor.getString(3));
                endDate.setText(cursor.getString(4));
                memo.setText(cursor.getString(5));
                imgview.setImageBitmap(resize(cursor.getString(2)));
            }
        }else if (fragNum == 2) {
            Cursor cursor = cosmeticToolsdao.selectItemAll("_id=?", new String[]{num});
            while (cursor.moveToNext()){
                Cursor category = cosmeticToolsdao.selectCateName("_id=?", new String[]{cursor.getString(7)});
                while(category.moveToNext()){
                    categoryTV.setText(category.getString(0));
                }
                name.setText(cursor.getString(1));
                openDate.setText(cursor.getString(3));
                endDate.setText(cursor.getString(4));
                memo.setText(cursor.getString(5));
                imgview.setImageBitmap(resize(cursor.getString(2)));
            }
        } else if (fragNum == 3) {
            Cursor cursor = lensdao.selectItemAll("_id=?", new String[]{num});
            while (cursor.moveToNext()){
                Cursor category = lensdao.selectCateName("_id=?", new String[]{cursor.getString(7)});
                while(category.moveToNext()){
                    categoryTV.setText(category.getString(0));
                }
                name.setText(cursor.getString(1));
                openDate.setText(cursor.getString(3));
                endDate.setText(cursor.getString(4));
                memo.setText(cursor.getString(5));
                imgview.setImageBitmap(resize(cursor.getString(2)));
            }
        }
    }

    public void closeBtnClick(View view) {
        finish();
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

    private Bitmap resize(String src){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(src, options);
        int dstWidth;
        int dstHeight;

        Bitmap resized = null;
        if(bitmap != null){
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
            resized = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);
        }


        return  resized;
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
        // path 경로
        imagePath = getRealPathFromURI(imgUri);
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

    public void deleteBtnClick(View view) {
        if(fragNum==1){
            cosmeticdao.deleteItemById(Integer.parseInt(num));
        }else if(fragNum==2){
            cosmeticToolsdao.deleteItemById(Integer.parseInt(num));
        }else if(fragNum==3) {
            lensdao.deleteItemById(Integer.parseInt(num));
        }
        finish();
    }

    public void opendateClick(View view) {
        CalendarValue();
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                pYear = year;
                pMonth = monthOfYear + 1;
                pDate = dayOfMonth;
                openDate.setText(String.format("%d - %d - %d", pYear, pMonth, pDate));
            }
        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDate.setText(String.format("%d - %d - %d", pYear, pMonth, pDate));
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
            Toast.makeText(DetailViewActivity.this,"카테고리를 선택해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < adapter.getCount(); i++) {

            if (adapter.getItem(i).getName().equals(categoryTV.getText())) {
                if (adapter.getItem(i).getDurationY() > 0) {
                    dur = adapter.getItem(i).getDurationY()*365;
                    break;
                }
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

        String open = String.valueOf(openDate.getText());
        if(open.isEmpty()||open.equals("")){
            Toast.makeText(DetailViewActivity.this,"개봉일을 선택해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        String dateArray[] = open.split(" - ");
        c.set(Calendar.YEAR, Integer.parseInt(dateArray[0]));
        c.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]) - 1);

        c.add(Calendar.YEAR, 0);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, dur);


        pYear = c.get(Calendar.YEAR);
        pMonth = c.get(Calendar.MONTH);
        pDate = c.get(Calendar.DAY_OF_MONTH);
        endDate.setText(String.format("%d - %d - %d", pYear, pMonth, pDate));
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
                endDate.setText(String.format("%d - %d - %d", pYear, pMonth, pDate));
            }
        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDate.setText(String.format("%d - %d - %d", pYear, pMonth, pDate));
                datedialog.dismiss();
            }
        });
        datedialog.show();
    }


    public void updateCosmeticClick(View view) {
        String cosName = name.getText().toString();
        String cosOpenDate = openDate.getText().toString();
        String cosEndDate = endDate.getText().toString();
        String cosMemo = memo.getText().toString();
        String cosImg = null;
        if(currentPhotoPath != null){
            cosImg = currentPhotoPath;
        }else if (imagePath != null ){
            cosImg = imagePath;
        }

        if(cosName.equals("")||cosName.isEmpty()||cosOpenDate.equals("")||cosOpenDate.isEmpty()||cosEndDate.equals("")||cosEndDate.isEmpty()||cosMemo.isEmpty()||cosMemo.equals("")){
            Toast.makeText(DetailViewActivity.this,"이름,개봉일,교체권장일,메모를 입력해주세요.",Toast.LENGTH_SHORT).show();
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
            dto.set_id(Integer.parseInt(num));
            Log.d("usercosmetic",dto.toString());
            cosmeticdao.updateItem(dto);
        } else if (MainActivity.fragNum == 2) {
            UserCosmeticTools dto = new UserCosmeticTools();
            dto.setCate_id(cosId);
            dto.setEndDate(cosEndDate);
            dto.setOpenDate(cosOpenDate);
            dto.setImg(cosImg);
            dto.setMemo(cosMemo);
            dto.setName(cosName);
            dto.setFavorite(0);
            cosmeticToolsdao.updateItem(dto);
        } else if (MainActivity.fragNum == 3) {
            UserLens dto = new UserLens();
            dto.setCate_id(cosId);
            dto.setEndDate(cosEndDate);
            dto.setOpenDate(cosOpenDate);
            dto.setImg(cosImg);
            dto.setMemo(cosMemo);
            dto.setName(cosName);
            dto.setFavorite(0);
            lensdao.updateItem(dto);
        }
        finish();

    }
}