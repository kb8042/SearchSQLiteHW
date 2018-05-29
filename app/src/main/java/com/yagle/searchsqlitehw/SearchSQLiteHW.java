package com.yagle.searchsqlitehw;

import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class SearchSQLiteHW extends AppCompatActivity {
    private SQLiteDatabase DB = null;
    //建立 table01 資料表
    private final static String CREATE_TABLE = "CREATE TABLE table01(_id INTEGER PRIMARY KEY,name TEXT,price INTERGER)";

    ListView mLView;
    Button mBtn_S, mBtn_SA;
    EditText mEdit;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_sqlite_hw);
        //取得元件
        mEdit = (EditText) findViewById(R.id.EdtID);
        mBtn_S = (Button) findViewById(R.id.Btn_S);
        mBtn_SA = (Button) findViewById(R.id.Btn_SA);
        mLView = (ListView) findViewById(R.id.LView);
        //設定監聽
        mBtn_S.setOnClickListener(myList);
        mBtn_SA.setOnClickListener(myList);
        mLView.setOnItemClickListener(listListener);
        //建立資料庫
        DB = openOrCreateDatabase("db1.db", MODE_PRIVATE, null);
        try {
            DB.execSQL(CREATE_TABLE); //建立資料表
            DB.execSQL("INSERT INTO table01 (name,price) values ('Keelung City',200);"); //新增資料
            DB.execSQL("INSERT INTO table01 (name,price) values ('Taipei City',200);");
            DB.execSQL("INSERT INTO table01 (name,price) values ('Taoyuan City',200);");
            DB.execSQL("INSERT INTO table01 (name,price) values ('Hsinchu City',200);");
            DB.execSQL("INSERT INTO table01 (name,price) values ('Miaoli City',200);");
            DB.execSQL("INSERT INTO table01 (name,price) values ('Taichung City',200);");
            DB.execSQL("INSERT INTO table01 (name,price) values ('Changhua City',200);");
            DB.execSQL("INSERT INTO table01 (name,price) values ('Nantou City',200);");
            DB.execSQL("INSERT INTO table01 (name,price) values ('Yunlin City',200);");
            DB.execSQL("INSERT INTO table01 (name,price) values ('Chiayi City',200);");
        } catch (Exception e) {
        }
        mCursor = getAll();         //查詢所有資料
        UpdateAdapter(mCursor);     //載入資料表至mLView中
    }

    private ListView.OnItemClickListener listListener =
            new ListView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                        int position, long id) {
                    mCursor.moveToPosition(position);
                    Cursor C = get(id);
                    String S = "id=" + id + "\r\n" + "name" + C.getString(1) + "\r\n" + "price" + C.getInt(2);
                    Toast.makeText(getApplicationContext(), S, Toast.LENGTH_SHORT).show();
                }
            };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DB.close();             //關閉資料庫
    }

    private Button.OnClickListener myList = new Button.OnClickListener() {
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.Btn_S: {           //查詢單筆
                        long id = Integer.parseInt(mEdit.getText().toString());
                        mCursor = get(id);
                        UpdateAdapter(mCursor); //戴人資料表至ListView中
                        break;
                    }
                    case R.id.Btn_SA:{                  //查詢全部
                        mCursor = getAll();                //查詢所有資料
                        UpdateAdapter(mCursor);          //載入資料表至ListView中
                        break;
                    }
                }
            }catch(Exception err){
                Toast.makeText(getApplicationContext(), "Not Found!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void UpdateAdapter(Cursor mCursor){
        if (mCursor != null && mCursor.getCount() >= 0){
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
            android.R.layout.simple_list_item_2,          //包含2個資料項
            mCursor,     //資料庫的Cursors物件
            new String[] {"pname","price"},      //name, price欄位
            new int[] { android.R.id.text1, android.R.id.text2 }, //與pname, price對應的元件
            0);  //adapter行為最佳化
            mLView.setAdapter(adapter);  //將adapter增加到mLView中
        }
    }

    public Cursor getAll() {  //查詢所有資料
        Cursor mCursor = DB.rawQuery("SELECT _id, _id||'.'||name pname, price FROM table01", null);
        return mCursor;  //傳回_id, pname, price欄位
    }

    public Cursor get(long rowId) throws SQLException {  //查詢指定ID的資料
        Cursor mCursor = DB.rawQuery("SELECT _id, _id||'.'||name pname, price FROM table01 WHERE _id=" + rowId, null);
        if (mCursor.getCount() > 0)
            mCursor.moveToFirst();
        else
            Toast.makeText(getApplicationContext(), "Not Found!", Toast.LENGTH_SHORT).show();
        return mCursor;  //傳回_id, pname, price欄位
    }
}