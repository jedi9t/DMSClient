package tvdms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.victgroup.signup.dmsclient.R;

import config.Config;

public class ServerIPActivity extends Activity {
	Config config;
	Button btn_affirm;
	Button btn_return;
	EditText mEditText;
	String str;
	String url;
	String ip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_ip);
		ExitApplication.getInstance().addActivity(this);
		mEditText = (EditText) findViewById(R.id.editText_id);
		btn_affirm = (Button) findViewById(R.id.btn_affirm);
		btn_return = (Button) findViewById(R.id.btn_return);
		url = config.URL.replace("http://", "");
		ip = url.substring(0, url.indexOf('/'));
		System.out.println("ip" + url + config.URL);
		mEditText.setHint(ip);
		btn_affirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				str = mEditText.getText().toString();
				config.URL = "http://" + str + "/dms/";
				new AlertDialog.Builder(ServerIPActivity.this)
						.setMessage(MainActivity.resources.getString(R.string.tip_modifysuccess)).setTitle(MainActivity.resources.getString(R.string.tip_tip))
						.setPositiveButton(MainActivity.resources.getString(R.string.btn_ok), null).show();
				url = config.URL.replace("http://", "");
				ip = url.substring(0, url.indexOf('/'));
				mEditText.setHint(ip);
			}

		});
		btn_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ServerIPActivity.this,
						SettingsActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}



}
