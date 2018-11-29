package tvdms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.victgroup.signup.dmsclient.R;

import config.Config;
import helper.PreferenceManager;
import util.CommonUtil;

public class SettingsActivity extends Activity {
	Button btn_setting;
	Button btn_syssetting;
	Button btn_apps;
	Button btn_serverip;
	Button btn_deviceinfo;
	TextView textView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_settings);
		textView1 = (TextView) findViewById(R.id.textView1);
		if (CommonUtil.isNotEmpty(Config.programId+""))
			textView1.setText("�豸ID:"
					+ PreferenceManager.getInstance().getRMID() + " ��ǰ��Ŀ����"
					+ Config.programId+"");
		btn_setting = (Button) findViewById(R.id.btn_setting);
		btn_syssetting = (Button) findViewById(R.id.btn_syssetting);
		btn_apps = (Button) findViewById(R.id.btn_apps);
		btn_serverip = (Button) findViewById(R.id.btn_serverip);
		btn_deviceinfo = (Button) findViewById(R.id.btn_deviceinfo);
		btn_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Settings.ACTION_SETTINGS);
				startActivity(intent);
			}

		});
		btn_syssetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CommonUtil util = new CommonUtil();
				util.doStartApplicationWithPackageName(SettingsActivity.this,
						"com.mbx.settingsmbox");
			}
		});
		btn_apps.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CommonUtil util = new CommonUtil();
				util.doStartApplicationWithPackageName(SettingsActivity.this,
						"com.fb.FileBrower");
			}
		});
		btn_deviceinfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(SettingsActivity.this)
						.setTitle("�豸����")
						.setMessage(
								"�豸��ϸ��Ϣ\n�豸ID: "
										+ PreferenceManager.getInstance()
												.getRMID()
										+ "\nMAC:   "
										+ Config.serialNumber
										+ "\nIP:    "
										+ PreferenceManager.getInstance()
												.getIP())
						.setPositiveButton("ȷ��", null).show();
			}
		});
		btn_serverip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingsActivity.this,
						ServerIPActivity.class);
				startActivity(intent);
			}
		});
	}



}
