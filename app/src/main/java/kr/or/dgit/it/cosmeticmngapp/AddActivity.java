package kr.or.dgit.it.cosmeticmngapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

        openEditdate = findViewById(R.id.regi_cosmetic_open_date);
        openEditdate.setInputType(0);

        endEditdate = findViewById(R.id.regi_cosmetic_end_date);
        endEditdate.setInputType(0);






        imgview = (ImageView)findViewById(R.id.img);
    }

    public void categoryBtnClick(View view) {
        CharSequence info[] = new CharSequence[]{"내정보", "로그아웃"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(info, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case 0:
                        Toast.makeText(AddActivity.this, "내정보", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(AddActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void cameraClick(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgview.setImageBitmap(imageBitmap);
        }
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
