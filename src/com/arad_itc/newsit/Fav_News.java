package com.arad_itc.newsit;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Fav_News extends Activity {

	private ListView listview;
	private ArrayAdapter<Row_List_View_object> adapter;
	private SharedPreferences SharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fav_news);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		listview = (ListView) findViewById(R.id.listview_fav);
		adapter = new AdapterList2(Newsit.array_2);
		listview.setAdapter(adapter);
		SharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
		getActionBar().setTitle(SharedPreferences.getString("title_action_bar", "News IT"));
	}

	private void fill_fav_listview() {
		// TODO Auto-generated method stub
		Newsit.array_2.clear();
		Newsit_Data_source source = new Newsit_Data_source(this);
		Newsit.array_2.addAll(source.find_news_fav(SharedPreferences.getString("table", "")));
		if (Newsit.array_2.size()!= 0) {
			adapter.notifyDataSetChanged();
		}
		else {
			adapter.notifyDataSetChanged();
			Toast.makeText(this, "خبر مورد علاقه ای وجود ندارد", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		fill_fav_listview();
		Newsit.currentActivity2 = this;
	}

}
