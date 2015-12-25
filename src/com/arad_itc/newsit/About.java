package com.arad_itc.newsit;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

public class About extends Activity {

	private WebView webview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		webview = (WebView)findViewById(R.id.webviewabout);
		webview.loadUrl("file:///android_asset/about.html");
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return false;
	}
}
