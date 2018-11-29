package service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.victgroup.signup.dmsclient.R;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import bean.LoadItem;
import bean.MQTTMessage;
import bean.ProgramItem;
import bean.ProgramPublish;
import bean.UITemplate;
import config.Config;
import helper.PreferenceManager;
import helper.TemplateHelper;
import http.BaseService;
import tvdms.Dms;
import tvdms.DownloadResource;
import tvdms.InstallAPKActivity;
import tvdms.MainActivity;
import util.AppUtils;
import util.CommonUtil;
import util.ErrorListenerCallBack;
import util.FileUtil;
import util.JsonUtil;
import util.NetworkUtil;
import util.ScreenUtil;
import util.TimeUtil;
import util.VolleyController;

public class MQTTservice extends Service {
	private String TAG = "MQTTservice";
	public static String HOST = "tcp://signup.victgroup.com:61613";
	private String userName = "admin";
	private String passWord = "password";
	private Handler handler;
	private MqttClient client;
	// private static final String MQTT_TOPIC = "MQTT_COMMAND";
	private MqttConnectOptions options;
	private ScheduledExecutorService scheduler;
	private MQTTMessage mqtt_message;
	private Map<String, MqttTopic> topicMap;

	@Override
	public IBinder onBind(Intent arg0) {

		return myBinder;
	}
String templatesId;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("cycle", "MQTTservice.onCreate");


		if (FileUtil.checkUpdateResource(MQTTservice.this)) {
			Log.i("connect","MQTTservice.checkUpdateResource=true");
			templatesId =PreferenceManager.getInstance().getRecentlyDownloadProgramPublish();
			String temStr=FileUtil.readFileSdcard(Config.RESOURCE_INFO+templatesId);
			ProgramPublish downloadProgramPublish = TemplateHelper.getDownloadTemplate(getApplicationContext(), temStr, 0);
			//   String templatesId = downloadProgramPublish.getMessageId();
			String publishMessageId=PreferenceManager.getInstance().getPublishMessageId(templatesId);
			List<ProgramItem> programItemList=TemplateHelper.getProgramItemList(this,downloadProgramPublish,temStr);


			Intent intentDownload = new Intent(this, DownloadResource.class);
			intentDownload.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if(DownloadResource.myDownload==null) {
				Log.i("connect", "MQTTservice DownloadResource.myDownload==null");
				DownloadResource.programItemList=programItemList;
				intentDownload.putExtra("isStartDownload", true);
				intentDownload.putExtra("publishMessageId", publishMessageId);
				intentDownload.putExtra("templatesId", templatesId);
				startActivity(intentDownload);
				Log.i("connect", "(MQTTservice)DownloadResource.class.create");
			}else {
				Log.i("connect", "MQTTservice DownloadResource.myDownload!=null");
				intentDownload.putExtra("isStartDownload", false);
				startActivity(intentDownload);
				DownloadResource.myDownload.start(programItemList, publishMessageId,templatesId);
			}

		}


















		connectToServer();

	}

	public class MyBinder extends Binder {

		public MQTTservice getService() {
			return MQTTservice.this;
		}
	}

	private MyBinder myBinder = new MyBinder();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand() executed");
		return super.onStartCommand(intent, flags, startId);
	}

	public void connectToServer() {
		init();

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				if (msg.what == 1) {
					String mqttmsg = (String) msg.obj;
					JsonUtil util = new JsonUtil();

					mqtt_message = util.getMQTTMessage( mqttmsg);

					
					
					executiveCommand();

					
					
				} else if (msg.what == 2) {
					try {
						client.subscribe(Config.topicName, 1);
					} catch (Exception e) {
						Log.e(TAG, "mqtt 连接失败" + e.toString());
						e.printStackTrace();
					}
				} else if (msg.what == 3) {
					Log.e(TAG, "连接失败，系统正在重连");
					// startReconnect();
				}
			}
		};
		startReconnect();
	}

	private void sendClentState() {
		final String strUrl = Config.URL + "android/DMSDevice!getPlayId.action?deviceid=" + Config.serialNumber
				+ "&templateid=" + Config.programId;
		Log.i("connect", "sendClentState() strUrl=" + strUrl);
		new Thread(new Runnable() {

			@Override
			public void run() {
				BaseService.connService(strUrl, 0);
			}
		}).start();
	}

	private void sendClentProgramList() {

		// String recentlyProgramListPath =
		// FileUtil.getRecentlyProgramListPath(getApplicationContext());

		String[] fileNameArr = new File(Config.RESOURCE_INFO).list();

		String program_info = "";
		if (fileNameArr != null) {

			for (String fileName : fileNameArr) {
				String content = FileUtil.readFileSdcard(Config.RESOURCE_INFO + fileName);
				ProgramPublish programPublish = TemplateHelper.getTemplate(getApplicationContext(), content, 1);

				program_info += ",{\"id\":\"" + fileName + "\",\"name\":\"" + programPublish.getRows().get(0).getName()
						+ "\"}";
			}

			final String strUrl = Config.URL + "android/DMSDevice!getAllIds.action?deviceid=" + Config.serialNumber
					+ "&schedulejson=[" + program_info.substring(1) + "]&memory_size="
					+ CommonUtil.getTotalInternalMemorySize() + "&memory_used="
					+ CommonUtil.getAvailableInternalMemorySize();

			Log.i("mytest", "sendClentState() strUrl=" + strUrl);
			new Thread(new Runnable() {

				@Override
				public void run() {
					BaseService.connService(strUrl, 1);
				}
			}).start();
		}
	}

	private void completeTask(final String messageId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (messageId != null && !messageId.equals("")) {
					String path = Config.URL + "android/DMSDevice!completeTask().action?deviceid=" + Config.serialNumber
							+ "&messageId=" + messageId;
					String str = BaseService.connService(path, 0);
				}
			}
		}).start();
	}

	private void screenshot() {
		try {
			Bitmap bmp = ScreenUtil.snapShotWithoutStatusBar(Config.SCREENSHOT_ACTIVITY);
			if (bmp != null) {
				ScreenUtil.savePic(bmp, Config.DIR_SCREENSHOT, Config.serialNumber + ".png");
				new Thread(new Runnable() {
					@Override
					public void run() {
						FileUtil.createDirectory(new File(Config.DIR_SCREENSHOT));
						Message message = new Message();
						try {
							String savepath = Config.DIR_SCREENSHOT + Config.serialNumber + ".png";
							BaseService.uploadFile(
									Config.URL + "servlet/UploadifySerlet?operate=3&messageId="
											+ mqtt_message.getMessageId() + "&device_id=" + Config.serialNumber,
									savepath);
						} catch (Exception ex) {
							message.what = 0;
							System.out.print(ex.toString());
						}
					}

				}).start();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void startReconnect() {
		Log.i("MQTTservice", "Start Reconnection");
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				Bundle bundle = new Bundle();
				Intent intent = new Intent();
					if (!client.isConnected()) {
						Log.i("MQTTservice", "!client.isConnected()");
						bundle.putInt("cmd", 101);
						connect();
					} else {
						Log.i("MQTTservice", "client.isConnected()");
						bundle.putInt("cmd", 102);
					}

				intent.putExtras(bundle);
				intent.setAction(Dms.ACTION_MQTT);
				sendBroadcast(intent);
			}
		}, 0 * 1000, 60 * 1000, TimeUnit.MILLISECONDS);
	}

	private void init() {
		try {
			// hostΪ��������testΪclientid������MQTT�Ŀͻ���ID��һ���Կͻ���Ψһ��ʶ����ʾ��MemoryPersistence����clientid�ı�����ʽ��Ĭ��Ϊ���ڴ汣��
			client = new MqttClient(HOST, Config.serialNumber, new MemoryPersistence());

			options = new MqttConnectOptions(); // MQTT����������
			options.setCleanSession(true); // �����Ƿ����session,�����������Ϊfalse��ʾ�������ᱣ���ͻ��˵����Ӽ�¼����������Ϊtrue��ʾÿ�����ӵ������������µ��������
			// options
			options.setUserName(userName); // �������ӵ��û���
			options.setPassword(passWord.toCharArray()); // �������ӵ�����
			options.setConnectionTimeout(10); // ���ó�ʱʱ�� ��λΪ��
			options.setKeepAliveInterval(20); // ���ûỰ����ʱ�� ��λΪ��
												// ��������ÿ��1.5*20���ʱ����ͻ��˷��͸���Ϣ�жϿͻ����Ƿ����ߣ������������û�������Ļ���

			this.topicMap = new HashMap<String, MqttTopic>();
			this.topicMap.put(Config.topicName, client.getTopic(Config.topicName));

			client.setCallback(new MqttCallback() { // ���ûص�

				@Override
				public void connectionLost(Throwable cause) {
					System.out.println("connectionLost----------");

				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					// publish���ִ�е�����
					System.out.println("deliveryComplete---------" + token.isComplete());
				}

				@Override
				public void messageArrived(String topicName, MqttMessage message) throws Exception {
					// subscribe��õ�����Ϣ��ִ�е�������

					String content = new String(message.getPayload(), "utf-8");
					Log.d(TAG, "messageArrived----------" + content);

					Message msg = new Message();
					msg.what = 1;
					msg.obj = content;
					handler.sendMessage(msg);
					Log.e(TAG, "msg.what = 1;----------" + msg.what);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void connect() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					client.connect(options);
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
					// startReconnect();
				}
			}
		}).start();
	}

	public void sendMsg(String topicName, String ajxStr) {
		MqttTopic topic = this.topicMap.get(topicName);
		if (topic == null) {
			return;
		}
		MqttMessage msg = this.createMessage(ajxStr);
		MqttDeliveryToken token;
		try {
			token = topic.publish(msg);
			token.waitForCompletion();
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	private MqttMessage createMessage(String msgPayload) {

		MqttMessage message = new MqttMessage();
		message.setQos(1);
		message.setRetained(true);
		message.setPayload(msgPayload.getBytes());
		return message;
	}

	// �����d���ݺ��ς�
	private void sendDataTask() {
		try {
			StringRequest stringRequest = new StringRequest(Request.Method.POST,
					Config.URL+"android/DMSDevice!downtask.action", new Listener<String>() {
						@Override
						public void onResponse(String response) {
							try {
							} catch (Exception e) {
							}

						}
					}, new ErrorListenerCallBack()) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					String process = PreferenceManager.getInstance().getProcess();
					String resId = PreferenceManager.getInstance().getResId();
					Log.i("mytest", "process=" + process + ",resId=" + resId);
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("deviceid", Config.serialNumber);
					hashMap.put("process", process);
					hashMap.put("resId", resId);
					hashMap.put("msgId", DownloadResource.MessageId);
					Log.i("mytest_hl02", "sendDataTask  deviceid=" + Config.serialNumber + ",process=" + process+",resId="+resId+",messageId="+DownloadResource.MessageId);
					return hashMap;
				}
			};
			VolleyController.getInstance().getRequestQueue(getApplicationContext()).add(stringRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	String temStr;
	ProgramPublish programPublish;
	private List<UITemplate> templateList;

	List<ProgramItem> programItemList;
	
	public void executiveCommand() {
		if (mqtt_message != null) {
			
			switch (mqtt_message.getSyncCmd()) {
			case Config.MQTT_CMD_RESOURCE_UPDATE:
				Log.i(TAG,"��������:��Դ����");
				final String publishMessageId=mqtt_message.getMessageId();
				new Thread(new Runnable() {

					public void run() {
						updateResource(publishMessageId);
					}
					
				}).start();
				
				


				break;
			case Config.MQTT_CMD_SCREENSHOT:
				Log.i(TAG,"��������:��ͼ");
				if (Config.SCREENSHOT_ACTIVITY != null) {

					screenshot();

				} else {
					Bundle bundle = new Bundle();

					bundle.putString("messageId", mqtt_message.getMessageId());
					bundle.putInt("cmd", mqtt_message.getSyncCmd());

					Intent intent = new Intent();
					intent.putExtras(bundle);
					intent.setAction(Dms.ACTION_MQTT);
					sendBroadcast(intent);
				}
				break;

			case Config.MQTT_CMD_PUBLISH_IM:
				Log.i(TAG,"��������:������ʱ��Ϣ");
				Bundle bundle = new Bundle();
				bundle.putString("messageId", mqtt_message.getMessageId());
				bundle.putInt("cmd", mqtt_message.getSyncCmd());
				bundle.putString("content", mqtt_message.getContent());
				bundle.putInt("time", mqtt_message.getTime());
				bundle.putInt("count", mqtt_message.getCount());
				bundle.putString("font", mqtt_message.getFont());
				bundle.putString("position", mqtt_message.getPosition());
				bundle.putString("fontsize", mqtt_message.getFontsize());

				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setAction(Dms.ACTION_MQTT);
				sendBroadcast(intent);
				completeTask(mqtt_message.getMessageId());
				break;

			case Config.MQTT_CMD_INSTALL_APK:
				Log.i(TAG,"��������:��װ��Ӧ��");
				if (AppUtils.isApplicationAvilible(MainActivity.context, "")) {
					Intent apkIntent = new Intent(getBaseContext(), InstallAPKActivity.class);
					apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					apkIntent.putExtra("messageId", mqtt_message.getMessageId());
					apkIntent.putExtra("apkurl", mqtt_message.getContent());
					Log.e("TAGTAGTA", mqtt_message.getContent());
					apkIntent.putExtra("type", InstallAPKActivity.TYPE_INSTALL_APK);
					getApplication().startActivity(apkIntent);
				}
				break;

			case Config.MQTT_CMD_POWER_PLAN:
				Log.i(TAG,"��������:Զ�̿���");
				Intent intent_power = new Intent();
				Log.i("interger", mqtt_message.getPower());
				switch (Integer.parseInt(mqtt_message.getPower())) {
				case 0:
					intent_power.putExtra("power", 0);
					break;
				case 1:
					intent_power.putExtra("power", 1);
					break;
				default:
					break;
				}
				intent_power.setAction("victgroup.action.POWER_SETTING");
				sendBroadcast(intent_power);

				if (CommonUtil.isNotEmpty(mqtt_message.getOffTime())
						&& CommonUtil.isNotEmpty(mqtt_message.getOnTime())) {
					try {
						CommonUtil.tvTimerOnOff(getBaseContext(), mqtt_message.getOnTime(),
								mqtt_message.getOffTime(), true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case Config.MQTT_CMD_UPGRADE:
				Log.i(TAG,"��������:�����͑���");
				Intent apkIntent_1 = new Intent(getBaseContext(), InstallAPKActivity.class);
				apkIntent_1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				apkIntent_1.putExtra("messageId", mqtt_message.getMessageId());
				apkIntent_1.putExtra("apkurl", mqtt_message.getContent());
				apkIntent_1.putExtra("type", InstallAPKActivity.TYPE_INSTALL_APK);
				getApplication().startActivity(apkIntent_1);
				break;
			case Config.MQTT_CMD_LOOK_DOWNLOAD_STATUS:
				Log.i(TAG,"��������:�鿴���½���");
				sendDataTask();
				break;
			case Config.MQTT_CMD_STOP_MSG:
				Log.i(TAG,"��������:��ֹ��ʱ��Ϣ");
				Bundle bundleStopMsg = new Bundle();
				bundleStopMsg.putInt("cmd", mqtt_message.getSyncCmd());

				Intent intentStopMsg = new Intent();
				intentStopMsg.putExtras(bundleStopMsg);
				intentStopMsg.setAction(Dms.ACTION_MQTT);
				sendBroadcast(intentStopMsg);
				break;
			case Config.MQTT_CMD_GET_CLENT_STATE:
				Log.i(TAG,"��������:��ȡ���Ž�Ŀid");
				// sendClentProgramList();
				sendClentState();
				break;
			case Config.MQTT_CMD_GET_CLENT_PROGRAM:
				Log.i(TAG,"��������:��ȡ�ͻ����ų��б�");
				sendClentProgramList();
				break;
			case Config.MQTT_CMD_DELETE_PROGRAM:
				Log.i(TAG,"��������:ɾ����Ŀ");
				// deleteProgram(mqtt_message.getContent());
				break;
			case Config.MQTT_CMD_POWER_TIMING:
				Log.i(TAG,"��������:��ʱ���ػ�");
				Bundle bundle_power_timing = new Bundle();
				bundle_power_timing.putInt("cmd", mqtt_message.getSyncCmd());
				bundle_power_timing.putString("cycle", mqtt_message.getCycle());
				bundle_power_timing.putString("onTime", mqtt_message.getOnTime());
				bundle_power_timing.putString("offTime", mqtt_message.getOffTime());
				Intent intent_power_timing = new Intent();
				intent_power_timing.putExtras(bundle_power_timing);
				intent_power_timing.setAction(Dms.ACTION_MQTT);
				sendBroadcast(intent_power_timing);
				break;
			}
		}
	}
	public void updateResource(String publishMessageId) {
		if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {

			String url = Config.URL + "android/DMSDevice!getSchedule.action?deviceid="
					+ Config.serialNumber;
			temStr = BaseService.connService(url, 0);

		} else {
			Log.i("connect", "����������");
			return;
		}

		if (temStr != null && !temStr.equals("")) {

			Log.i("connect", "MQTTservice temStr != null");

			
			programPublish = TemplateHelper.getDownloadTemplate(getApplicationContext(), temStr, 0);

			if (programPublish == null) {
				Log.i("connect", "MQTTservice programPublish = null,Dms.currentProgramListPath="
						+ Dms.currentProgramListPath);

				String recentlyDownloadProgramPublish = PreferenceManager.getInstance()
						.getRecentlyDownloadProgramPublish();

				temStr = FileUtil.readFileSdcard(Config.RESOURCE_INFO + recentlyDownloadProgramPublish);

				if (temStr != null && !temStr.equals("")) {
					programPublish = TemplateHelper.getDownloadTemplate(getApplicationContext(), temStr, 0);
					
				} else {
					return;
				}
			} else {

				if(templatesId == programPublish.getMessageId()){
				//	templatesId="";
					return;
				}else{
					templatesId = programPublish.getMessageId();
				}
				List<String> programListList = FileUtil.getProgramListList(getApplicationContext());

				List<ProgramPublish> programPublishList = FileUtil.getProgramListList(getApplicationContext(),
						programListList);

				if (programPublishList == null) {
					PreferenceManager.getInstance()
							.setRecentlyDownloadProgramPublish(templatesId);
					PreferenceManager.getInstance().setPublishTime(templatesId,
							TimeUtil.getSampleTimeStr());
				} else {
					String samePublishMessageId = TemplateHelper.getTheSamePublishMessageId(programPublish,
							programPublishList);
					if (samePublishMessageId == null) {
						PreferenceManager.getInstance()
								.setRecentlyDownloadProgramPublish(templatesId);
						PreferenceManager.getInstance().setPublishTime(templatesId,TimeUtil.getSampleTimeStr());
					} else {

						PreferenceManager.getInstance().setPublishTime(samePublishMessageId,
								TimeUtil.getSampleTimeStr());
						BaseService.sendfinishTask(getApplicationContext(),"0", MainActivity.resources.getString(R.string.download_successinfo),publishMessageId,"0/0");
						if(DownloadResource.taskListnumber<1) {
							Log.i("connect", "MQTTservice DownloadResource.taskListnumber<1");
							Dms.currentDownloadProgramPublishPath=samePublishMessageId;
							Intent intent=new Intent(Dms.ACTION_MQTT);
							Bundle bundle=new Bundle();
							bundle.putInt("cmd", 103);
							intent.putExtras(bundle);
							sendBroadcast(intent);   
						}else {
							Log.i("connect", "MQTTservice DownloadResource.taskListnumber>0");
							DownloadResource.lastTaskMessageId=samePublishMessageId;
						}
						
						
						return;
					}
				}

			}
			if (programPublish == null) {
				return;
			}


			templatesId = programPublish.getMessageId();
			templateList = programPublish.getuITemplateList();

			

			if (templateList != null && templateList.size() > 0) {


				Log.i("connect", "templateList != null");

                programItemList=TemplateHelper.getProgramItemList(getApplicationContext(),programPublish,temStr);

				if(programItemList==null||programItemList.size()==0) {
					BaseService.sendfinishTask(getApplicationContext(),"0",  MainActivity.resources.getString(R.string.download_successinfo),publishMessageId,"0/0");
				}else {
					Intent intentDownload = new Intent(getBaseContext(), DownloadResource.class);
					intentDownload.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					if(DownloadResource.myDownload==null) {
						Log.i("connect", "MQTTservice DownloadResource.myDownload==null");
						DownloadResource.programItemList=programItemList;
						intentDownload.putExtra("isStartDownload", true);
						intentDownload.putExtra("publishMessageId", publishMessageId);
						intentDownload.putExtra("templatesId", templatesId);
						getApplication().startActivity(intentDownload);
						Log.i("connect", "(MQTTservice)DownloadResource.class.create");
					}else {
						Log.i("connect", "MQTTservice DownloadResource.myDownload!=null");
						intentDownload.putExtra("isStartDownload", false);
						getApplication().startActivity(intentDownload);
						DownloadResource.myDownload.start(programItemList, publishMessageId,templatesId);
					}
				}
			}else {
				BaseService.sendfinishTask(getApplicationContext(),"0",  MainActivity.resources.getString(R.string.download_successinfo),publishMessageId,"0/0");

			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("cycle", "MQTTservice.onDestroy");
		try {
			if (scheduler != null) {
				scheduler.shutdown();
				scheduler = null;
			}
			if (client != null) {
				client.disconnect();
				client.close();
				client = null;
			}
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}
}
