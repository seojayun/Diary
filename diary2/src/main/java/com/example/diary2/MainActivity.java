package com.example.diary2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatePicker dPicker;
    EditText edtDiary;
    Button btnSave;
    String fileName;

    int cYear, cMonth, cDay;
    String sdPath;
    File myFolder;   //파일을 다룰 파일변수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dPicker = (DatePicker) findViewById(R.id.dPicker);
        edtDiary = (EditText) findViewById(R.id.edtDiary);
        btnSave = (Button) findViewById(R.id.btnSave);

        Calendar cal = Calendar.getInstance();

        int cYear = cal.get(Calendar.YEAR);

        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);


        int permissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //변수 선언 후 2중 보안 선언

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
        } else {
            sdFileProcess();
            //퍼미션 체크가 완료되면 시행
        }


        dPicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fileName = year + "_" + (monthOfYear + 1) + "_" + dayOfMonth + ".txt";
                edtDiary.setText(readDiary(fileName));

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // FileOutputStream fileos = openFileOutput(fileName, Context.MODE_PRIVATE);
                    FileOutputStream fileos = new FileOutputStream(sdPath + fileName);
                    String str = edtDiary.getText().toString();
                    fileos.write(str.getBytes());
                    fileos.close();


                } catch (IOException e) {
                    showToast("파일을 저장할 수 없습니다.");
                } finally {
                    showToast(fileName + "이 저장 되었습니다.");

                }

            }
        });
    }

    void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


    }

    //335p 직접풀어보기 8-1       ★일기를 읽어서 처리하는 메소드★
    String readDiary(String fileName) {
        String diaryStr = null;

        try {
//            FileInputStream fileis=openFileInput(fileName);
            FileInputStream fileis = new FileInputStream(sdPath + fileName);    //--> 내가 읽어드릴 파일이름
            byte txt[] = new byte[fileis.available()];

            fileis.read(txt);
            diaryStr = (new String(txt).trim());
            fileis.close();
            btnSave.setText("수정하기");


        } catch (IOException e) {
            edtDiary.setHint("일기 없음");
            btnSave.setText("새로 저장");


        }
        return diaryStr;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        sdFileProcess();
    }

    void sdFileProcess() {
        sdPath = "/storage/2A2B-DA07/sDcard";  //실행 전 폴더가 없을경우 폴더생성
        myFolder = new File(sdPath);
        if (!myFolder.isDirectory()) {
            //!myFolder.isDirectory())   -->  폴더를 새로 만드는데, 해당 폴더가 존재하지 않으면, 해당문구 실행
            myFolder.mkdir();   //-> 해당 폴더를 만들어준다.
        }
        fileName = cYear + "_" + (cMonth + 1) + "_" + cDay + ".txt";
        edtDiary.setText(readDiary(fileName));
    }
}
