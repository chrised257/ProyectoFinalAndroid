package mx.itesm.throughcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

public class HelpActivity extends Activity {
	
	String videoURL = "http://www.youtube.com/embed/DH6w8yh28mo";
	WebView mWebView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		initwebView();
	}
	
	private void initwebView() {
		mWebView = (WebView) findViewById(R.id.webView1);
		mWebView.getSettings().setPluginState(PluginState.ON);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebChromeClient(new MyChromeClient());
		mWebView.loadUrl(videoURL);
	}
	
	class MyChromeClient extends WebChromeClient {
		 
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			Intent intent = new Intent(HelpActivity.this, LandVideoAct.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("video", videoURL);
			startActivity(intent);
		}
 
	}
	
}
