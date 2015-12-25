package com.arad_itc.newsit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import dmax.dialog.SpotsDialog;

public class Show_web2 extends Activity implements OnClickListener {

	private WebView webView;
	private AlertDialog dialog;
	private RelativeLayout rl[] = new RelativeLayout[4];
	private ImageView imageview;
	private int check = 0 , fav_image = 0;  
	private String URL;
	private SharedPreferences shared;
	private Row_List_View_object object;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_web2);
		init();
		shared = getSharedPreferences("setting", MODE_PRIVATE);
		getActionBar().setTitle(shared.getString("title_action_bar", "News IT"));
		check_fav();
	}

	private void init() {
		dialog = new SpotsDialog(this, R.style.Custom);
		webView = (WebView) findViewById(R.id.webwiew_fav);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebViewClient(new WebClient());
		imageview = (ImageView)findViewById(R.id.subimage_fav);
		imageview.setImageResource(R.drawable.sub);
		rl[0] = (RelativeLayout)findViewById(R.id.cyrcle_menu_fav);
		rl[0].setOnClickListener(this);
		rl[1] = (RelativeLayout)findViewById(R.id.shareicon_fav);
		rl[1].setOnClickListener(this);
		rl[1].setVisibility(View.INVISIBLE);
		rl[2] = (RelativeLayout)findViewById(R.id.telegram_icon_fav);
		rl[2].setOnClickListener(this);
		rl[2].setVisibility(View.INVISIBLE);
		rl[3] = (RelativeLayout)findViewById(R.id.fav_icon_fav);
		rl[3].setOnClickListener(this);
		rl[3].setVisibility(View.INVISIBLE);
		Bundle exters = getIntent().getExtras();
		if (exters != null) {
			dialog.show();
			int position = exters.getInt("POSITION");
			object = Newsit.array_2.get(position);
			URL = object.URL_LINK;
			webView.loadUrl(URL);
		}
	}

	class WebClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}
	private void check_fav() {
		// TODO Auto-generated method stub
		Newsit_Data_source source = new Newsit_Data_source(this);
		if(source.find_fav(shared.getString("table", ""), object.ID)) {
			ImageView iv_2 = (ImageView)findViewById(R.id.fav_image_fav);
			iv_2.setImageResource(android.R.color.transparent);
			iv_2.setImageResource(R.drawable.fav_press);
			fav_image = 1;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cyrcle_menu_fav:
			if(check == 0) {
				setanimation();
			}
			else {
				exitanimation();
			}
			break;
		case R.id.shareicon_fav:
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "خبر");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, URL);
			startActivity(Intent.createChooser(sharingIntent, "اشتراک گذاری"));
			break;
		case R.id.telegram_icon_fav:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/newsit"));
			startActivity(browserIntent);
			break;
		case R.id.fav_icon_fav:
			if(fav_image == 0) {
				setfav();
			}
			else {
				exitfav();
			}
			break;
		}	
	}

	private void exitfav() {
		// TODO Auto-generated method stub
		Newsit_Data_source source = new Newsit_Data_source(this);
		if(source.Update(0, shared.getString("table", ""), object.ID)) {
			ImageView iv_1 = (ImageView)findViewById(R.id.fav_image_fav);
			iv_1.setImageResource(android.R.color.transparent);
			iv_1.setImageResource(R.drawable.fav);
			fav_image = 0;
		}
	}

	private void setfav() {
		// TODO Auto-generated method stub
		Newsit_Data_source source = new Newsit_Data_source(this);
		if(source.Update(1, shared.getString("table", ""), object.ID)) {
			ImageView iv_2 = (ImageView)findViewById(R.id.fav_image_fav);
			iv_2.setImageResource(android.R.color.transparent);
			iv_2.setImageResource(R.drawable.fav_press);
			fav_image = 1;
		}
	}

	private void exitanimation() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Thread(){
			@Override
			public void run() {
				super.run();
				imageview.setImageResource(android.R.color.transparent);
				imageview.setImageResource(R.drawable.sub);
				rl[1].setVisibility(View.INVISIBLE);
				rl[2].setVisibility(View.INVISIBLE);
				rl[3].setVisibility(View.INVISIBLE);
			}
		},300);
		check = 0;
	}

	private void setanimation() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Thread(){
			@Override
			public void run() {
				super.run();
				imageview.setImageResource(android.R.color.transparent);
				imageview.setImageResource(R.drawable.sub_rotate);
				rl[1].setVisibility(View.VISIBLE);
				rl[2].setVisibility(View.VISIBLE);
				rl[3].setVisibility(View.VISIBLE);
			}
		},500);
		check = 1;
	}

}
