package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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
    //3.파일이름을 저장하기 위해 String 변수 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dPicker=(DatePicker)findViewById(R.id.dPicker);
        edtDiary=(EditText)findViewById(R.id.edtDiary);
        btnSave=(Button)findViewById(R.id.btnSave);
        Calendar cal = Calendar.getInstance();
        //4.Calendar cal = Calendar.getInstance();  Calendar 의 변수 cal 이 오늘 날짜 정보를 받아옴
        int cYear = cal.get(Calendar.YEAR);
        //5. cYear 이라는 변수에 연도 정보 가져옴
        int cMonth = cal.get(Calendar.MONTH);
        //6. cMonth 이라는 변수에 월 정보 가져옴
        int cDay = cal.get(Calendar.DAY_OF_MONTH);
        //7. cDay 이라는 변수에 일 정보 가져옴

        //335p 직접풀어보기 8-1
        fileName=cYear+"_"+(cMonth+1)+"_"+cDay+".txt";
        edtDiary.setText(readDiary(fileName));
        //335p 직접풀어보기 8-1



        dPicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            //8.  .init = 초기화   즉 초기화 정보를 어디서 받아올 것인가에 대한 변수를 각각 입력하였다.
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fileName=year+"_"+(monthOfYear+1)+"_"+dayOfMonth+".txt";
                //9. 파일 네임을 작성하였다. 현재의 경우, 현재설정한 년도_현재설정한월_현재설정한일.txt 로 저장되며, 월은 -1 되기 때문에 +1를 해주었다.

                edtDiary.setText(readDiary(fileName));

            }
        });
    //10. 최종적으로 일기를 쓰고 버튼누르면 나올수 있도록 OnClickListener 생성
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream fileos = openFileOutput(fileName, Context.MODE_PRIVATE);
                    //12. FileOutputStream 의 변수 fileos 선언 후  openFileOutput() 저장할 이름을 설정
                    // ---> 일기장을 저장하면 그때그때 파일이 생성 될 수 있도록 상단에 fileName으로 이름을 설정했다.  그 내용을 불러온다.
                    String str = edtDiary.getText().toString();
                    //14. 에디트 텍스트(edtDiary)에 있는 문자열을 str에 대입
                    fileos.write(str.getBytes());
                    //15. 메모리 장치는 .getBytes() 바이트 단위로 저장해야된다.
                    fileos.close();
                    //16. 파일을 열었으면 반드시 닫아줘야 한다.

                } catch (IOException e) {
                    showToast("파일을 저장할 수 없습니다.");
                    //13. 캐치문에 예외에 대한 토스트 생성
                }finally {
                    showToast(fileName+"이 저장 되었습니다.");
                    //17. 최종적으로 저장버튼을 눌렀을 때, 토스트로 저장됬다는 문구가 뜨도록 작성
                    //이 부분은 try안에 넣어도 실행되지만, 어떤 상황에서도 반드시 한번은 저장이 될 수 있도록 finally 구문을 만들어 집어넣었다.
                }

            }
        });
    }
    //11. 저장할 내용이 길어지기 때문에 각자 토스트를 따로 생상하지 않고, 메소드를 만들어서 정보를 받아도록 코딩
    void showToast(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();


    }

    //335p 직접풀어보기 8-1       ★일기를 읽어서 처리하는 메소드★
    String readDiary(String fileName) {
        String diaryStr = null;

        try {
            FileInputStream fileis=openFileInput(fileName);
            //18.fileName을 읽어들여서 변수 fileis에 대입
            byte txt[]=new byte[fileis.available()];
            //19.  byte[fileis.available()] 저장공간이 얼마든지 저장되있는 내용을 전부 읽어옴
            fileis.read(txt);
            //20. txt파일을 읽어옴
            diaryStr=(new String(txt).trim());
            //21.  .trim()  양쪽에 공백이 있다면 제거하고 읽어옴
            fileis.close();
            //22. 반드시 항상 오픈했으면 닫아줘야 한다.
            btnSave.setText("수정하기");
            //23. 수정해야 될 때에는 버튼 이름이 "수정하기" 라고 변경되도록 설정하였다.

        } catch (IOException e) {
            edtDiary.setHint("일기 없음");
            //24. 만약 읽어올 파일이 없어서 캐치로 넘어오면 힌트로 일기없음 표기가 되도록 설정
            btnSave.setText("새로 저장");
            //25.이 부분은 일기가 저장되어있지 않은 부분이기때문에 버튼의 텍스트가 새로저장이라고 다시 나오도록 설정

        }
        return diaryStr;
    }
    //335p 직접풀어보기 8-1
}


////------------------------------수업내용----------------------------------------------------------------------------------------------------------------------------
//
//public class MainActivity extends AppCompatActivity {
//
//
//    DatePicker dPicker;
//    EditText edtDiary;
//    Button btnSave;
//    String fileName;
//    //3.파일이름을 저장하기 위해 String 변수 선언
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        dPicker=(DatePicker)findViewById(R.id.dPicker);
//        edtDiary=(EditText)findViewById(R.id.edtDiary);
//        btnSave=(Button)findViewById(R.id.btnSave);
//        Calendar cal = Calendar.getInstance();
//        //4.Calendar cal = Calendar.getInstance();  Calendar 의 변수 cal 이 오늘 날짜 정보를 받아옴
//        int cYear = cal.get(Calendar.YEAR);
//        //5. cYear 이라는 변수에 연도 정보 가져옴
//        int cMonth = cal.get(Calendar.MONTH);
//        //6. cMonth 이라는 변수에 월 정보 가져옴
//        int cDay = cal.get(Calendar.DAY_OF_MONTH);
//        //7. cDay 이라는 변수에 일 정보 가져옴
//        dPicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
//            //8.  .init = 초기화   즉 초기화 정보를 어디서 받아올 것인가에 대한 변수를 각각 입력하였다.
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                fileName=year+"_"+(monthOfYear+1)+"_"+dayOfMonth+".txt";
//                //9. 파일 네임을 작성하였다. 현재의 경우, 현재설정한 년도_현재설정한월_현재설정한일.txt 로 저장되며, 월은 -1 되기 때문에 +1를 해주었다.
//                try {
//                    FileInputStream fileis=openFileInput(fileName);
//                    //18.fileName을 읽어들여서 변수 fileis에 대입
//                    byte txt[]=new byte[fileis.available()];
//                    //19.  byte[fileis.available()] 저장공간이 얼마든지 저장되있는 내용을 전부 읽어옴
//                    fileis.read(txt);
//                    //20. txt파일을 읽어옴
//                    edtDiary.setText(new String(txt).trim());
//                    //21.  .trim()  양쪽에 공백이 있다면 제거하고 읽어옴
//                    fileis.close();
//                    //22. 반드시 항상 오픈했으면 닫아줘야 한다.
//                    btnSave.setText("수정하기");
//                    //23. 수정해야 될 때에는 버튼 이름이 "수정하기" 라고 변경되도록 설정하였다.
//
//                } catch (IOException e) {
//                    edtDiary.setText("");
//                    edtDiary.setHint("일기 없음");
//                    //24. 만약 읽어올 파일이 없어서 캐치로 넘어오면 힌트로 일기없음 표기가 되도록 설정
//                    btnSave.setText("새로 저장");
//                    //25.이 부분은 일기가 저장되어있지 않은 부분이기때문에 버튼의 텍스트가 새로저장이라고 다시 나오도록 설정
//
//                }
//
//            }
//        });
//        //10. 최종적으로 일기를 쓰고 버튼누르면 나올수 있도록 버튼 온클릭리스너 생성
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    FileOutputStream fileos = openFileOutput(fileName, Context.MODE_PRIVATE);
//                    //12. FileOutputStream 의 변수 fileos 선언 후  openFileOutput() 저장할 이름을 설정
//                    // ---> 일기장을 저장하면 그때그때 파일이 생성 될 수 있도록 상단에 fileName으로 이름을 설정했다.  그 내용을 불러온다.
//                    String str = edtDiary.getText().toString();
//                    //14. 에디트 텍스트(edtDiary)에 있는 문자열을 str에 대입
//                    fileos.write(str.getBytes());
//                    //15. 메모리 장치는 .getBytes() 바이트 단위로 저장해야된다.
//                    fileos.close();
//                    //16. 파일을 열었으면 반드시 닫아줘야 한다.
//
//                } catch (IOException e) {
//                    showToast("파일을 저장할 수 없습니다.");
//                    //13. 캐치문에 예외에 대한 토스트 생성
//                }finally {
//                    showToast(fileName+"이 저장 되었습니다.");
//                    //17. 최종적으로 저장버튼을 눌렀을 때, 토스트로 저장됬다는 문구가 뜨도록 작성
//                    //이 부분은 try안에 넣어도 실행되지만, 어떤 상황에서도 반드시 한번은 저장이 될 수 있도록 finally 구문을 만들어 집어넣었다.
//                }
//
//            }
//        });
//    }
//    //11. 저장할 내용이 길어지기 때문에 각자 토스트를 따로 생상하지 않고, 메소드를 만들어서 정보를 받아도록 코딩
//    void showToast(String msg) {
//        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
//    }
//}
//
////------------------------------수업내용----------------------------------------------------------------------------------------------------------------------------