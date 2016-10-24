package kr.hs.emirim.kyr9909.sqlitetest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editName, editCount, editNameResult, editCountResult;
    Button butInit, butInput, butSelect;
    MyDBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper=new MyDBHelper(getApplicationContext());
        setContentView(R.layout.activity_main);
        editName=(EditText)findViewById(R.id.edit_groupname);
        editCount=(EditText)findViewById(R.id.edit_groupcount);
        editNameResult=(EditText)findViewById(R.id.edit_name_result);
        editCountResult=(EditText)findViewById(R.id.edit_count_result);
        butInit=(Button)findViewById(R.id.but_init);
        butInput=(Button)findViewById(R.id.but_input);
        butSelect=(Button)findViewById(R.id.but_select);

        butInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=dbHelper.getWritableDatabase();
                dbHelper.onUpgrade(db,1,2);
                db.close();
            }
        });

        butInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=dbHelper.getWritableDatabase();
                db.execSQL("insert into idoltable values('"
                                +editName.getText().toString()+"', "
                                +editCount.getText().toString()+
                                ");");
                db.close();
                Toast.makeText(getApplicationContext(),"정상적으로 입력 완료",Toast.LENGTH_SHORT).show();
            }
        });

        butSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=dbHelper.getReadableDatabase();
                Cursor rs=db.rawQuery("select * from idoltable",null);
                String gname="그룹이름"+"\n"+"=================="+"\n";
                String gcount="인원수"+"\n"+"=================="+"\n";
                while(rs.moveToNext()){
                    gname+=rs.getString(0)+"\n";
                    gcount+=rs.getInt(0)+"\n";
                }
                editNameResult.setText(gname);
                editCountResult.setText(gcount);

                rs.close();
                db.close();
            }
        });
    }
    public class MyDBHelper extends SQLiteOpenHelper {
        public MyDBHelper(Context context){
            super(context, "idoldb", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table idoltable(gname char(40) primary key, gcount integer);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}