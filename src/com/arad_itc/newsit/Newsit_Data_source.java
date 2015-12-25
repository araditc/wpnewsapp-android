package com.arad_itc.newsit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class Newsit_Data_source {


	Newsit_DBOpenHelper dbHelper;
	SQLiteDatabase database;

	public Newsit_Data_source(Context context) {

		dbHelper = new Newsit_DBOpenHelper(context);
	}

	public void open() {
		try {
			dbHelper.createDataBase();
			database = dbHelper.openDataBase();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void close() {
		dbHelper.close();
	}
	//---------------------------------------------------------------------------------
	public boolean Insert_value(ArrayList<Row_List_View_object> array , String Table) {
		open();
		if (array.size() != 0) {
			for (int i = 0; i < array.size(); i++) {
				ContentValues value = new ContentValues();
				value.put("id", array.get(i).ID);
				value.put("datetime", array.get(i).DATETIME);
				value.put("title", array.get(i).TITLE);
				value.put("content", array.get(i).DESCRIPTION);
				value.put("image", array.get(i).URL_PHOTO);
				value.put("cat", array.get(i).CATEGORY);
				value.put("link", array.get(i).URL_LINK);
				value.put("fav", array.get(i).FAV_ICON);
				database.insert(Table, null, value);
				Log.i("insert_item", array.get(i).ID);
			}
			return true;
		} else {
			close();
			return false;
		}
	}
	//---------------------------------------------------------------------------------
	public ArrayList<Row_List_View_object> find_news(String Table) {
		open();
		String command = "SELECT * FROM " + Table + " ORDER BY id DESC";
		Cursor cursor = database.rawQuery(command, null);
		ArrayList<Row_List_View_object> arrayList = new ArrayList<Row_List_View_object>();
		if (cursor.moveToFirst()) {
			do {
				Row_List_View_object object = new Row_List_View_object();
				object.ID = cursor.getString(cursor.getColumnIndex("id"));
				object.DATETIME = cursor.getString(cursor.getColumnIndex("datetime"));
				object.TITLE = cursor.getString(cursor.getColumnIndex("title"));
				object.DESCRIPTION = cursor.getString(cursor.getColumnIndex("content"));
				object.URL_PHOTO = cursor.getString(cursor.getColumnIndex("image"));
				object.CATEGORY = cursor.getString(cursor.getColumnIndex("cat"));
				object.URL_LINK = cursor.getString(cursor.getColumnIndex("link"));
				arrayList.add(object);
				Log.d("Len", arrayList.size() + "" + object.ID);
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();
		return arrayList;
	}
	//----------------------------------------------------------------------------------
	public boolean CheckIsDataAlreadyInDBorNot(String Value , String Table) {
		open();
		String Query = "SELECT * FROM " + Table + " WHERE " + "id" + " = " + Value;
		Cursor cursor = database.rawQuery(Query, null);
		if(cursor.getCount() > 0){
			cursor.close();
			close();
			Log.i("ok_row", "True" + cursor.getCount());
			return true;
		}
		cursor.close();
		close();
		Log.i("ok_row", "False");
		return false;
	}
	//----------------------------------------------------------------------------------
	public ArrayList<Row_List_View_object> find_news_fav(String Table) {
		open();
		String command = "SELECT * FROM " + Table + " WHERE fav = 1 " ;
		Cursor cursor = database.rawQuery(command, null);
		ArrayList<Row_List_View_object> arrayList = new ArrayList<Row_List_View_object>();
		if (cursor.moveToFirst()) {
			do {
				Row_List_View_object object = new Row_List_View_object();
				object.ID = cursor.getString(cursor.getColumnIndex("id"));
				object.DATETIME = cursor.getString(cursor.getColumnIndex("datetime"));
				object.TITLE = cursor.getString(cursor.getColumnIndex("title"));
				object.DESCRIPTION = cursor.getString(cursor.getColumnIndex("content"));
				object.URL_PHOTO = cursor.getString(cursor.getColumnIndex("image"));
				object.CATEGORY = cursor.getString(cursor.getColumnIndex("cat"));
				object.URL_LINK = cursor.getString(cursor.getColumnIndex("link"));
				object.FAV_ICON = cursor.getInt(cursor.getColumnIndex("fav"));
				arrayList.add(object);
				Log.d("fav _ news ", arrayList.size() + "" + object.ID);
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();
		return arrayList;
	}
	//------------------------------------------------------------------------------------
	public boolean Update(int number , String Table , String id) {
		open();
		ContentValues value = new ContentValues();
		value.put("id", id);
		value.put("fav", number);
		database.update(Table, value, "id" + " = ?", new String[] {id});
		Log.i("Update_fav", "True");
		close();
		return true;
	}
	//------------------------------------------------------------------------------------
	public boolean find_fav(String Table , String id) {
		open();
		String command = "SELECT fav FROM " + Table + " WHERE id = " + id + " AND " + "fav = 1 " ;
		Cursor cursor = database.rawQuery(command, null);
		if(cursor.getCount() > 0){
			cursor.close();
			close();
			Log.i("has_row_fav", "True" + cursor.getCount());
			return true;
		}
		cursor.close();
		close();
		Log.i("has_row_fav", "False");
		return false;
	}
	//-------------------------------------------------------------------------------------
}
