package tvdms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.victgroup.signup.dmsclient.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import config.Config;
import helper.PreferenceManager;
import http.BaseService;
import util.CommonUtil;
import util.FileUtil;
import util.NetworkUtil;


public class InstallAPKActivity extends Activity {
	
	private String messageId;
	private String apkurl;
	private String description;
	private TextView txt_message;
	private TextView txt_percent;
	private ProgressBar progressBar;
	private static int currentPercent = 0;

	public static int TYPE_UPGRADE = 1;
	public static int TYPE_INSTALL_APK = 2;

	private int type = 1;
	private Context context;

	int installFlag = 0;
	boolean installFlag2 = false;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch (msg.what) {
			case 1:
				try {
				txt_message.setText(MainActivity.resources.getString(R.string.tv_startinstall));
				path=path+apkurl.substring(apkurl.lastIndexOf("/")+1);
				 new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String[] command = {"chmod", "777", path};  
							ProcessBuilder builder = new ProcessBuilder(command);  
							try {  
							builder.start(); 						
							
						    } catch (IOException e) {  
						       e.printStackTrace();							     
						  } 										
					  }
					}).start();
	    			
	 				Intent intentinstall=new Intent("com.vigroup.vgservice.common.INSTALL_APK");
					intentinstall.putExtra("apkUrl", path);	
					intentinstall.putExtra("restartFlag", true);//��Ĭ��װ��Ϻ����� true ,������ false
					intentinstall.putExtra("packageName", "com.victgroup.signup.dmsclient");
					startActivity(intentinstall);
				
				}catch(Exception e) {
					MainActivity.isCheckedUpdate=true;
					e.printStackTrace();
					finish();
				}
					
					
					
					
					
					
					
					
					
					
				/*
				final String fileName = path
						+ FileUtil.getFileNameByUrl(apkurl);
				final Message message1 = new Message();
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						String[] command = { "chmod", "777", fileName };
						ProcessBuilder builder = new ProcessBuilder(command);
						try {
							builder.start();
							message1.what = 5;
						} catch (IOException e) {
							e.printStackTrace();
							message1.what = 2;
						}
						handler.sendMessage(message1);
					}
				}).start();*/

				break;
			case 2:
				finish();
				break;

			case 3:
				txt_percent.setText(MainActivity.resources.getString(R.string.tv_downloadedprogress) + currentPercent);
				progressBar.setProgress(currentPercent);
				break;

			case 4:
				txt_message.setText(MainActivity.resources.getString(R.string.tv_reboot));
				finish();
				break;

			case 5:
				final String fileName1 = path
						+ FileUtil.getFileNameByUrl(apkurl);
				progressBar.setVisibility(View.GONE);
				File f = new File(fileName1);
				if (f.exists()) {
					if (type == TYPE_INSTALL_APK)
						installAPK(f);

					if (type == TYPE_UPGRADE) {
						upgradeAPK(f);
					}

				} else {
					Toast.makeText(getBaseContext(), MainActivity.resources.getString(R.string.tip_apknotexist),
							Toast.LENGTH_SHORT).show();
				}

				if (messageId != null && !messageId.equals("")) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Message message = new Message();
							String path = Config.URL
									+ "android/DMSDevice!completeTask().action?deviceid="
									+ Config.serialNumber + "&messageId="
									+ messageId;
							String str = BaseService.connService(path,0);
							System.out.print(str);
							message.what = 2;
							handler.sendMessage(message);
						}
					}).start();
				}
				break;
			}

		}
	};
	File dir;
	String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_apk);
		ExitApplication.getInstance().addActivity(this);
		if (!NetworkUtil.isNetworkAvailable(this)) {
			Toast.makeText(InstallAPKActivity.this, MainActivity.resources.getString(R.string.internet_disconnected),
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		context = this;
		apkurl = getIntent().getStringExtra("apkurl");
		messageId = getIntent().getStringExtra("messageId");
		description = getIntent().getStringExtra("description");
		type = getIntent().getIntExtra("type", 2);

		txt_message = (TextView) findViewById(R.id.txt_message);
		txt_message.setText(MainActivity.resources.getString(R.string.download_start) + description);

		txt_percent = (TextView) findViewById(R.id.textView_percent2);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);

	//	dir = getDir("APKcache", Context.MODE_PRIVATE| Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		path = Config.installAPKPath + "/";
		System.out.println(path);

		if (CommonUtil.isNotEmpty(apkurl)) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String downloadpath = Config.URL + apkurl;
					if (apkurl.contains("http")) {
						downloadpath = apkurl;
					}
					download(path, downloadpath);
					
				}

			}).start();

		}
	}

	public void installAPK(File t) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Log.e("Ҫ��װ��apk", "sssssss------>" + Uri.fromFile(t));
		intent.setDataAndType(Uri.fromFile(t),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	public void upgradeAPK(File t) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(t),
				"application/vnd.android.package-archive");
		startActivity(intent);

		android.os.Process.killProcess(android.os.Process.myPid());
	}



	public void download(String _dirName, String _urlStr) {
		String newFilename = _urlStr.substring(_urlStr.lastIndexOf("/") + 1);
		Log.i("TAGTAGTA", "newFilename----->" + newFilename + ",dirName--->"
				+ _dirName + "_urlStr-----" + _urlStr);
		newFilename = _dirName + newFilename;
		File file = new File(newFilename);
		// ���Ŀ���ļ��Ѿ����ڣ���ɾ�����������Ǿ��ļ���Ч��
		if (file.exists()) {
			file.delete();
		}
		try {
			// ����URL
			URL url = new URL(_urlStr);
			// ������
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			int code = con.getResponseCode();
			if(code==200||code==206) {
			// ����ļ��ĳ���
			int contentLength = con.getContentLength();
			System.out.println("���� :" + contentLength);
			// ������
			InputStream is = con.getInputStream();
			// 1K�����ݻ���
			byte[] bs = new byte[1024];
			// ��ȡ�������ݳ���
			int len;
			// ������ļ���
			OutputStream os = new FileOutputStream(newFilename);
			long total = 0;

			while ((len = is.read(bs)) != -1) {
				total += len;
				currentPercent = ((int) ((total * 100) / contentLength));
				Message message = new Message();
				message.what = 3;
				handler.sendMessage(message);
				os.write(bs, 0, len);
			}
			// ��ϣ��ر���������
			os.flush();
			os.close();
			is.close();
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
			
			PreferenceManager.getInstance().setIsInstallAPKComplete(apkurl, true);
			
			}	else {
				
				con.disconnect();
				MainActivity.isCheckedUpdate=true;
				Message message = new Message();
				message.what = 2;
				handler.sendMessage(message);
				
			}
			
			
		} catch (Exception e) {
			MainActivity.isCheckedUpdate=true;
			Message message = new Message();
			message.what = 2;
			handler.sendMessage(message);
			e.printStackTrace();
		}
	}
}
