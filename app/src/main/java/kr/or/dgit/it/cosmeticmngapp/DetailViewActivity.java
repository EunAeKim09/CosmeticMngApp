package kr.or.dgit.it.cosmeticmngapp;

import android.Manifest;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticToolsDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserLensDAO;
import kr.or.dgit.it.cosmeticmngapp.db.DBhelper;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmetic;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmeticTools;
import kr.or.dgit.it.cosmeticmngapp.dto.UserLens;

import static android.content.ContentValues.TAG;

public class DetailViewActivity extends AppCompatActivity {
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
            }
        }
    }

    public void closeBtnClick(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cosmeticToolsdao.close();
        cosmeticdao.close();
        lensdao.close();
    }

    public void categoryBtnClick(View view) {
        DBhelper helper = new DBhelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        if(fragNum == 1){
            cursor = db.rawQuery("select name from cosmeticCategory order by name", null);
        }else if(fragNum == 2){
            cursor = db.rawQuery("select name from cosmeticToolsCategory order by name", null);
        }else if(fragNum == 3){
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

    public void deleteBtnClick(View view) {
        if(fragNum==1){
            cosmeticdao.deleteItemById(Integer.parseInt(num));
        }else if(fragNum==2){
            cosmeticToolsdao.deleteItemById(Integer.parseInt(num));
        }else if(fragNum==3){
            lensdao.deleteItemById(Integer.parseInt(num));
        }
        finish();
    }
}
