package com.example.sakalnews;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView t1,t2;
    SQLiteDatabase database;
    private Button admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1= (TextView) findViewById(R.id.photo);
        t2=(TextView) findViewById(R.id.text_desc);
        admin= (Button) findViewById(R.id.admin_login);
        databseConnection();
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddNewsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void databseConnection() {
        DatabaseClass obj=new DatabaseClass(this,"NEWS_TABLE", null, 1);
        database=obj.getReadableDatabase();
        retriveData();
    }

    private void retriveData() {
        Cursor cursor=database.rawQuery("select * from NEWS_TABLE",null);
        String img_text=null;
        String desc_text=null;
        while(cursor.moveToNext())
        {
            String img_text1=cursor.getString(0);
            String desc_text1=cursor.getString(1);
            img_text+=img_text1+"\n\n";
            desc_text+=desc_text1+"\n\n";
        }
        t1.setText(img_text);
        t2.setText(desc_text);
    }
}
