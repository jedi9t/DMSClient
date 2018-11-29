package tvdms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.victgroup.signup.dmsclient.R;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import bean.LoadItem;
import bean.ProgramItem;
import bean.ProgramPublish;
import config.Config;
import helper.PreferenceManager;
import http.BaseService;
import sql.DatabaseHelper;
import sql.SQLiteHelper;
import util.CommonUtil;
import util.FileUtil;

@SuppressLint("NewApi")
public class DownloadResource extends Activity {
	ProgramPublish programPublish;
	static int timerTaskCount;
	public static String TAG = "DownloadResource";
	long restimestamp;
	public static String MessageId;
	public static ProgressBar progressBar;
	public static TextView textView_percent1;
	public static TextView textView_percent2;
	public int progress;
	public static List<ProgramItem> programItemList;
	public static int taskTotal;
	public static int taskCount;
	private static int tasknumber;
	Thread downloadThread;
	Runnable downloadRunnable;
	public static MyDownload myDownload;
	public static int taskListnumber=0;
	public static String lastTaskMessageId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download_resource);
		Log.i("cycle", "DownloadResource.onCreate");
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		textView_percent1 = (TextView) findViewById(R.id.textView_percent1);
		textView_percent2 = (TextView) findViewById(R.id.textView_percent2);
		DatabaseHelper.init(this);
		PreferenceManager.init(this);
		Intent intent = getIntent();
		boolean isStartDownload = intent.getBooleanExtra("isStartDownload", false);
		Log.i("connect", "isStartDownload=" + isStartDownload);
		if (isStartDownload) {
			String publishMessageId = intent.getStringExtra("publishMessageId");
			String templatesId = intent.getStringExtra("templatesId");
			if (myDownload == null) {
				myDownload = new MyDownload();
				myDownload.start(programItemList, publishMessageId, templatesId);
			} else {
				myDownload.start(programItemList, publishMessageId, templatesId);
			}
		}

	}

	public void autoStart() {// ɾ�����õ���Դ�ļ�
		startActivity();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;

			}

		}
	};

	public void startActivity() {
		taskListnumber--;
		Log.i("mytest_hl03", "startActivity taskListnumber="+taskListnumber);
		
		if (taskListnumber<1) {
			Intent intent = new Intent(this, Dms.class);
			if(lastTaskMessageId!=null) {
				Dms.currentDownloadProgramPublishPath=lastTaskMessageId;
				lastTaskMessageId=null;
			}
			startActivity(intent);
			finish();
		}

	}

	Handler handler_ = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				textView_percent1.setText(MainActivity.resources.getString(R.string.tv_downloaded) + taskCount + "/" + taskTotal);
				textView_percent2.setText(MainActivity.resources.getString(R.string.tv_downloadedprogress) + progress + "%");
				progressBar.setProgress(progress);

				PreferenceManager.getInstance().setResId(taskCount + "/" + taskTotal);
			}
			super.handleMessage(msg);
		};
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {

		}
		return super.onKeyDown(keyCode, event);
	}

	// ��ȡ�����ڴ��С
	public boolean getIsEnoughStroage(long fileLenght) {
		File file = Environment.getDataDirectory();
		StatFs statFs = new StatFs(file.getPath());
		long availableBlocksLong = statFs.getAvailableBlocksLong();
		long blockSizeLong = statFs.getBlockSizeLong();
		long availableSize = availableBlocksLong * blockSizeLong;
		long estimateAvailableSize = availableSize - fileLenght;
		return estimateAvailableSize > 1024 * 1024 * 50;

	}


	
	public class MyDownload {

		public void start(final List<ProgramItem> programItemList, final String publishMessageId, final String templatesId) {
			PreferenceManager.getInstance().setPublishMessageId(templatesId,publishMessageId);
			  new Thread(new Runnable() {
			  public void run() {
				  taskListnumber++;
				  Log.i("mytest_hl03", "MyDownload publishMessageId="+publishMessageId+",taskListnumber="+taskListnumber);
				  downLoadRes(programItemList, publishMessageId, templatesId);
			  }
			  }).start();

		}

		public synchronized void downLoadRes(List<ProgramItem> programItemList, String publishMessageId,
				String templatesId) {
			MessageId = publishMessageId;
			taskTotal = 0;
			for (int i = 0; i < programItemList.size(); i++) {
				taskTotal += programItemList.get(i).getLoadItemList().size();
			}
			if (taskTotal == 0) {
				BaseService.sendfinishTask(DownloadResource.this, "0", MainActivity.resources.getString(R.string.download_successinfo), publishMessageId,"0/0");
				PreferenceManager.getInstance().setIsProgramListCompleteDownload(templatesId + "", true);
				PreferenceManager.getInstance().setSelectProgramList(templatesId + "", "");
				// PreferenceManager.getInstance().setTemplatesId(templatesId);
				Dms.currentDownloadProgramPublishPath = templatesId;
				autoStart();
				return;
			}

			if (programItemList != null && programItemList.size() > 0) {
				boolean isDownload = startDownload(programItemList, publishMessageId, templatesId);
				onPostExecute(isDownload, templatesId);
			}
		}

		public boolean startDownload(List<ProgramItem> programItemList, String publishMessageId, String templatesId) {
			Log.i("connect", "urls.length=" + taskTotal);

			tasknumber = taskTotal;
			taskCount = 0;
			BaseService.sendfinishTask(DownloadResource.this, "99", MainActivity.resources.getString(R.string.download_start), publishMessageId,taskCount+"/"+taskTotal);
			for (int i = 0; i < programItemList.size(); i++) {
				List<LoadItem> loadItemList = programItemList.get(i).getLoadItemList();
				for (int j = 0; j < loadItemList.size(); j++) {
					taskCount++;
					Log.i("connect", "doInBackground taskCount=" + taskCount);

					try {
						String dirName = loadItemList.get(j).path;
						String urlStr = loadItemList.get(j).url;
						Log.i("connect", "dirName=" + dirName);
						Log.i("connect", "urlStr=" + urlStr);

						String fileName = dirName.substring(dirName.lastIndexOf("/") + 1);

						File file = new File(dirName);

						if (dirName.endsWith(".html")) {
							if (file.exists()) {
								tasknumber--;
							} else {
								if (FileUtil.createFile(file)) {
									tasknumber--;
								} else {
									return false;
								}
							}

							continue;
						}

						if (file.exists()) {
							if (file.isDirectory()) {
								FileUtil.createFile(file);
							}

						}

						// ����URL
						URL url = new URL(urlStr);
						// ������
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						con.setRequestMethod("GET");

						long fileLength = PreferenceManager.getInstance().getFileLength(dirName);

						long total = file.length();

						Log.i("connect", "fileLength=" + fileLength + ",total=" + total);
						if (fileLength == 0) {

							int code1 = con.getResponseCode();
							if (code1 == HttpURLConnection.HTTP_OK) {
								fileLength = con.getContentLength();
								total = 0;
							} else {
								return false;
							}
							Log.i("connect", "(fileLength=0)fileLength=" + fileLength);
							PreferenceManager.getInstance().setFileLength(dirName, fileLength);
							Log.i("connect", "c file.exists()=" + file.exists());
						} else {
							con.setRequestProperty("Range", "bytes=" + total + "-" + fileLength);
							Log.i("connect", "Range=" + "bytes=" + total + "-" + fileLength);
						}
						if (total >= fileLength) {
							tasknumber--;

							continue;
						}

						long availableInternalMemorySize=CommonUtil.getAvailableInternalMemorySize();

                        double fileMemory=fileLength/(1024*1024.0);
						Log.i("mytest_hl05", "DownloadResource.availableInternalMemorySize=" + availableInternalMemorySize+"M,fileLength="+fileMemory+"M");
						if(availableInternalMemorySize<50||availableInternalMemorySize<fileMemory){

							Log.i("mytest_hl05", "availableInternalMemorySize<50||availableInternalMemorySize<fileLength)");
							AlertDialog dialog_Memory =	new AlertDialog.Builder(DownloadResource.this).setMessage(MainActivity.resources.getString(R.string.msg_memorytip)+dirName)
									.setPositiveButton(MainActivity.resources.getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface arg0, int arg1) {

										}
									}).show();

							Thread.sleep(5000);
							if(dialog_Memory.isShowing()){
								dialog_Memory.dismiss();
							}


                             return false;

						}

						RandomAccessFile raf = new RandomAccessFile(file, "rwd");
	//					raf.setLength(fileLength);
						raf.seek(total);
						Log.i("connect", "total=" + total);
						int code = con.getResponseCode();
						Log.i("connect", "code=" + code);
						if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_PARTIAL) {

							Log.i("connect", "hhh");

							InputStream is = con.getInputStream();

							raf.seek(file.length());
							// 1K�����ݻ���
							byte[] bs = new byte[1024];
							// ��ȡ�������ݳ���
							int len;

							Log.i("connect", "��ʼis.read(bs)");
							while ((len = is.read(bs)) != -1) {

								total += len;
								progress = (int) ((total * 100) / fileLength);
								raf.write(bs, 0, len);

								PreferenceManager.getInstance()
										.setProcess(String.valueOf((int) ((total * 100) / fileLength)));

								Message message = new Message();
								message.what = 1;
								handler_.sendMessage(message);
							}
							tasknumber--;
							Log.i("connect", "����is.read(bs) tasknumber=" + tasknumber);
							ContentValues values = new ContentValues();
							values.put("complete", 1);
							DatabaseHelper.getInstance().update(SQLiteHelper.RESOURCE_TB_NAME, values, "name=?",
									new String[] { fileName });

							// ��ϣ��ر���������
							con.disconnect();
							raf.close();
							is.close();
						} else {
							Log.i("connect", "����������");
							return false;
						}

					} catch (Exception e) {
						BaseService.sendfinishTask(DownloadResource.this, "-1", e.toString(), publishMessageId,taskCount+"/"+taskTotal);
						e.printStackTrace();
						return false;
					}finally {

					}

				}

				if (tasknumber == 0) {
					BaseService.sendfinishTask(DownloadResource.this, "0", MainActivity.resources.getString(R.string.download_successinfo), publishMessageId,taskCount+"/"+taskTotal);
				}
				PreferenceManager.getInstance().setIsProgramCompleteDownload(templatesId + "",
						programItemList.get(i).getProgramId() + "", true);

			}
			return true;
		}

		protected void onPostExecute(Boolean isComplete, String templatesId) {
			if (isComplete) {

					PreferenceManager.getInstance().setIsProgramListCompleteDownload(templatesId + "", isComplete);
					PreferenceManager.getInstance().setSelectProgramList(templatesId + "", "");
					// PreferenceManager.getInstance().setTemplatesId(templatesId);
					Dms.currentDownloadProgramPublishPath = templatesId;

					autoStart();
					Log.i("connect", "����Dms");
	/*if (PreferenceManager.getInstance().getRegisterState() == Config.REGISTER_YES) {
				} else {
					Log.i("registerState", "DownloadResource.registerState=Config.REGISTER_NO(onPostExecute)");
				}*/

			} else {
				Log.i("connect", "δ�������");
				taskListnumber--;

				finish();
				return;
			}

		}
	}

	@Override
	protected void onResume() {
		Log.i("cycle", "DownloadResource.onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.i("cycle", "DownloadResource.onPause");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.i("cycle", "DownloadResource.onRestart");
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		Log.i("cycle", "DownloadResource.onDestroy");
	//	myDownload=null;
		super.onDestroy();
	}
}
