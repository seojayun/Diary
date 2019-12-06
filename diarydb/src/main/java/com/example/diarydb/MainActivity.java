package com.example.diarydb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    DatePicker dPicker;
    EditText edtDiary;
    EditText edtResult;
    Button btnResult;
    Button btnSave;
    String fileName;
    int cYear, cMonth, cDay;
    MyDBHelper myDBHelper;
    SQLiteDatabase sqlDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dPicker=(DatePicker)findViewById(R.id.dPicker);
        edtDiary=(EditText)findViewById(R.id.edtDiary);
        btnSave=(Button)findViewById(R.id.btnSave);
        edtResult = (EditText)findViewById(R.id.edtResult);
        btnResult = (Button)findViewById(R.id.btnResult);




        myDBHelper=new MyDBHelper(this);//db생성

        Calendar cal=Calendar.getInstance();
        cYear=cal.get(Calendar.YEAR);
        cMonth=cal.get(Calendar.MONTH);
        cDay=cal.get(Calendar.DAY_OF_MONTH);
        fileName=cYear + "_" + (cMonth+1) + "_" + cDay;
        edtDiary.setText(readyDiary(fileName));
        dPicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  //데이터피커의 날짜가 바뀌면, 실행되는 메소드
                fileName=year + "_" + (monthOfYear+1) + "_" + dayOfMonth;
                edtDiary.setText(readyDiary(fileName));
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myDBHelper.getWritableDatabase();
                if(btnSave.getText().toString().equals("새로 저장")) { //이때는 INSERT

                    String no1 = "INSERT INTO myDiary VALUES ('";
                    sqlDB.execSQL(no1+fileName+"','"+edtDiary.getText().toString()+ "');" );
                    showToast("일기가 저장 되었습니다.");
                }else { // 이때는 UPDATE

                    String no2 = "UPDATE myDiary SET content = '";
                    sqlDB.execSQL(no2 + edtDiary.getText().toString() + "' WHERE diaryDate ='" + fileName + "';");
                    showToast("일기가 수정  되었습니다.");
                }

                sqlDB.close();

            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myDBHelper.getReadableDatabase();
                Cursor cursor;
                String no1 = edtResult.getText().toString();

                if(no1.equals("")||no1.equals(null)) {
                    showToast("검색어를 입력하세요.");
                }else {
                    cursor = sqlDB.rawQuery("SELECT * FROM myDiary WHERE content = '"+edtResult.getText().toString()+" ' ;",null);
                    fileName=edtResult.getText().toString();
                    edtDiary.setText(readyDiary(fileName));
                    Calendar cal=Calendar.getInstance();
                    cYear=cal.get(Calendar.YEAR);
                    cMonth=cal.get(Calendar.MONTH);
                    cDay=cal.get(Calendar.DAY_OF_MONTH);
                    dPicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  //데이터피커의 날짜가 바뀌면, 실행되는 메소드
                            fileName=edtResult.getText().toString();
                            edtDiary.setText(readyDiary(fileName));
                        }
                    });
                    showToast("일기를 불러왔습니다.");
                    cursor.close();
                }

                sqlDB.close();


            }
        });
    }
    //토스트 메서드
    void showToast(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
    //일기를 읽어서 처리하는 메서드
    String readyDiary(String fileName) {
        String diaryStr=null;
        sqlDB=myDBHelper.getReadableDatabase();  //읽기전용
        Cursor cursor;
        String no1 = "SELECT * FROM myDiary WHERE diaryDate = '";
        cursor = sqlDB.rawQuery(no1+ fileName + "';",null);
        if(cursor==null) {
            edtDiary.setHint("일기 없음");
            btnSave.setText("새로 저장");
        }else if (cursor.moveToFirst()) {  //일기의 내용이 있다면,
            diaryStr=cursor.getString(1);  //0번째 필드는 날짜이므로, 1번째 필드인 일기 내용을 담는다.
            btnSave.setText("수정하기");
        }else {  //일기는 있는데, 내가 선택한 날짜의 일기가 없을 경우,
            edtDiary.setHint("일기 없음");
            btnSave.setText("새로 저장");
        }
        cursor.close();
        sqlDB.close();
        return diaryStr;
    }

    public class MyDBHelper extends SQLiteOpenHelper {

        public MyDBHelper(Context context) {
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE myDiary (diaryDate TEXT PRIMARY KEY, content TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}




