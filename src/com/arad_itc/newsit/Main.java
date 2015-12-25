package com.arad_itc.newsit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import dmax.dialog.SpotsDialog;

public class Main extends Activity implements OnClickListener {

	private ListView listview;
	private ArrayAdapter<Row_List_View_object> adapter;
	private AlertDialog alert;
	private String Mac_address;
	private Newsit_Data_source source;
	private Button button;
	private SharedPreferences SharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
		getmac();
	}

	private void init() {
		// TODO Auto-generated method stub
		listview = (ListView) findViewById(R.id.listview);
		adapter = new AdapterList(Newsit.array);
		alert = new SpotsDialog(this, R.style.Custom);
		listview.setAdapter(adapter);
		source = new Newsit_Data_source(this);
		button = (Button)findViewById(R.id.update);
		button.setOnClickListener(this);
		SharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
		getActionBar().setTitle(SharedPreferences.getString("title_action_bar", "News IT"));
		Newsit.currentActivity = this;
		if(!SharedPreferences.getBoolean("def_url", false)) {
			show_dialog();			
		}
		else {
			auto_update();			
		}
	}

	@SuppressWarnings("deprecation")
	private void show_dialog() {
		// TODO Auto-generated method stub
		final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
		dialogBuilder
		.withTitle("انتخاب کانال")                                 
		.withTitleColor("#FFFFFF")                                  
		.withDividerColor("#ff0000")                            
		.withMessage("لطفا کانال خبری مورد نظر خود را انتخاب نمایید")       
		.withMessageColor("#ffffff")                              
		.withDialogColor("#FFE74C3C")                               
		.withIcon(getResources().getDrawable(R.drawable.ic_launcher))
		.withDuration(700)                                          
		.withEffect(Effectstype.Shake)                                         
		.withButton1Text("اخبار فن آوری اطلاعات")                                     
		.withButton2Text("اخبار سراسری")                                  
		.isCancelableOnTouchOutside(false)                           
		.setButton1Click(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editor = SharedPreferences.edit();
				editor.putString("url", "http://www.newsit.ir/apinews/news.php");
				editor.putString("table", "newsit");
				editor.putString("title_action_bar", "اخبار فن آوری اطلاعات");
				editor.putBoolean("def_url", true);
				editor.commit();
				dialogBuilder.dismiss();
				getActionBar().setTitle(SharedPreferences.getString("title_action_bar","اخبار فن آوری اطلاعات"));
				auto_update();	
			}
		})
		.setButton2Click(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editor = SharedPreferences.edit();
				editor.putString("url", "http://isfictnews.ir/apinews/news.php");
				editor.putString("table", "isfnews");
				editor.putString("title_action_bar", "اخبار سراسری");
				editor.putBoolean("def_url", true);
				editor.commit();
				dialogBuilder.dismiss();
				getActionBar().setTitle(SharedPreferences.getString("title_action_bar","اخبار سراسری"));
				auto_update();	
			}
		})
		.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_about:
			Intent intent  = new Intent(Main.this , About.class);
			startActivity(intent);
			break;
		case R.id.action_fav:
			Intent intent_fav  = new Intent(Main.this , Fav_News.class);
			startActivity(intent_fav);
			break;
		case R.id.action_change:
			show_dialog();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.update:
			Newsit.array.clear();
			if (isOnline()) {
				String DADEGAN = SharedPreferences.getString("url", "");
				requestData(DADEGAN);
			} else {
				data_fill_listview();
				Toast.makeText(this,  "اینترنت در دسترس نیست", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

	public void getmac() {
		WifiManager wifiManager =
				(WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wInfo = wifiManager.getConnectionInfo();
		Mac_address = wInfo.getMacAddress();
		Mac_address = Mac_address.replace(":", "");
	}

	private void auto_update() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Thread() {
			@Override
			public void run() {
				super.run();
				Newsit.array.clear();
				if (isOnline()) {
					String DADEGAN = SharedPreferences.getString("url", "");
					requestData(DADEGAN);
				} else {
					data_fill_listview();
					Toast.makeText(Main.this,  "اینترنت در دسترس نیست", Toast.LENGTH_SHORT).show();
				}
			}
		}, 2000);
	}

	private void requestData(String uri) {
		RequestPackage p = new RequestPackage();
		p.setMethod("GET");
		p.setParam("my_app", Mac_address);
		Log.d("Mac", Mac_address);
		p.setUri(uri);
		MyTask task = new MyTask();
		task.execute(p);
	}

	private class MyTask extends AsyncTask<RequestPackage, String, String> {

		@Override
		protected void onPreExecute() {
			alert.show();
			button.setBackgroundResource(R.drawable.background_perss_button1);
		}

		@Override
		protected String doInBackground(RequestPackage... params) {
			String content = HttpManager.getData(params[0]);
			return content;
		}

		@Override
		protected void onPostExecute(String result) {
			alert.dismiss();
			button.setBackgroundResource(R.drawable.background_normal_button1);
			Log.d("Result", result);
			if (result.equals("Error")) {
				Toast.makeText(Main.this, "خطا در دریافت اطلاعات از مرکز", Toast.LENGTH_LONG).show();
				data_fill_listview();
			} else {
				pares_data(result);
			}

		}
	}

	private void pares_data(String result) {
		try {
			JSONObject json = new JSONObject(result);
			if (json.getString("ok").equals("true")) {
				JSONArray jsonArray = json.getJSONArray("result");
				if (jsonArray.length() != 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = jsonArray.getJSONObject(i);
						Row_List_View_object item = new Row_List_View_object();
						item.ID = object.getString("id");
						item.DATETIME = object.getString("datetime");
						item.TITLE = object.getString("title");
						item.DESCRIPTION = object.getString("content");
						item.URL_PHOTO = object.getString("image");
						item.CATEGORY = object.getString("cat");
						item.URL_LINK = object.getString("link");
						item.FAV_ICON = 0;
						if(!source.CheckIsDataAlreadyInDBorNot(item.ID, SharedPreferences.getString("table", ""))) {
							Newsit.array.add(item);
							Log.i("size_array", Newsit.array.size() + "");
						}
					}
					if(Newsit.array.size()!= 0) {
						boolean ok = source.Insert_value(Newsit.array , SharedPreferences.getString("table", ""));
						if (ok) {
							data_fill_listview();
						}
					}
					else {
						data_fill_listview();
					}
				}
			} else {
				Toast.makeText(this, "خبری موجود نیست", Toast.LENGTH_LONG).show();
				data_fill_listview();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void data_fill_listview() {
		Newsit.array.clear();
		Newsit.array.addAll(source.find_news(SharedPreferences.getString("table", "")));
		if (Newsit.array.size() !=0) {
			adapter.notifyDataSetChanged();	
		}

	}
}

