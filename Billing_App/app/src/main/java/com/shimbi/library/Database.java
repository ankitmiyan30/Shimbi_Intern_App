//Copyright � 2014 All Rights Reserved With ShimBi Computing Laboratories Pvt. Ltd.
//Database
package com.shimbi.library;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper{
	SQLiteDatabase sqLiteDatabase;
	public Database(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL("CREATE TABLE DOMAIN_LINK(DOMAIN_NAME TEXT NOT NULL)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		arg0.execSQL("DROP TABLE IF EXIST CONTEACT");
		onCreate(arg0);
	}
	public void insert(String name)
	{
		ContentValues cv=new ContentValues();
		cv.put("DOMAIN_NAME", name);
	
		sqLiteDatabase=getWritableDatabase();
	
		sqLiteDatabase.insert("DOMAIN_LINK", null,cv);
	}
	public String getname()
	{
		String s;
		Cursor c = null;
		try
		{
			sqLiteDatabase=getReadableDatabase();
			c=sqLiteDatabase.rawQuery("SELECT * FROM DOMAIN_LINK", null);
			if(c!=null && c.getCount()!=0)
			{
				c.moveToFirst();
				do
				{
						s=c.getString(c.getColumnIndex("DOMAIN_NAME"));
				}while(c.moveToNext());
				return s;
			}
			else
			{	
				return null;
			}
		}
		
		finally
		{
			c.close();
		}
	}
	
	


}