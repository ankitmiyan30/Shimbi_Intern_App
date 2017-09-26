package com.example.sakalnews;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddDetails extends AppCompatActivity {

    private SQLiteDatabase database;
    EditText msg;
    TextView display_msg;
    ImageButton getimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        display_msg= (TextView) findViewById(R.id.dismsg);
        msg= (EditText) findViewById(R.id.description);
        databaseConnection();
        getimage= (ImageButton) findViewById(R.id.get_image);
        getimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("crop", "true");
                intent.putExtra("outputX", 100);
                intent.putExtra("outputY", 100);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void databaseConnection() {
        DatabaseClass db=new DatabaseClass(this, "NEWS_TABLE", null, 1);
        database=db.getWritableDatabase();
    }

    public void insert_Data(View view)
    {
        ContentValues contentValues=new ContentValues();

        contentValues.put("NEWS_NAME",msg.getText().toString());
        contentValues.put("NEWS_IMAGE","Developer By Ankit Singh");

        long inserted=database.insert("NEWS_TABLE",null,contentValues);
        if(inserted > 0)
        {
            display_msg.setText("News Updated");
        }
        else {
            display_msg.setText("News Updation Failed");
        }
    }
}
