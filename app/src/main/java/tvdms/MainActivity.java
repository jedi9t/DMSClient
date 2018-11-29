package tvdms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.res.Configuration;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.victgroup.signup.dmsclient.R;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.APKVersion;
import bean.Machine;
import bean.RegisterResult;
import bean.UserGroup;
import config.Config;
import helper.PreferenceManager;
import http.BaseService;
import service.MQTTservice;
import service.PowerService;
import util.CommonUtil;
import util.FileUtil;
import util.JsonUtil;
import util.NetworkUtil;
import util.PackageUtils;
import util.ScreenUtil;
import util.StringUtils;
import widget.CustomDialog;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	List<String> programListList;
	boolean isHasProgram;
	public static boolean isApplicationClose;
	// public Intent service;
	private String result, registerStateResult;
	public static Context context;
	private static MainActivity mainActivity;
	private RelativeLayout bg;
	private int oldKeyDown;
	private long oldKeyDownTime;
	private boolean isLand;
	private int registerState;
	private CustomDialog dialog;
	private LayoutInflater inflater;
	private LinearLayout layout_progress, qr_layout;
	private ImageView iv_QRcode;
	// private EditText et_code;
	private EditText et_code1;
	private TextView tv_deviceName, tv_prompt_isRegister, tv_prompt_noRegister;
	private ProgressBar progressBar;
	private Button btn_commit;
	private String REGISTER_URL;
	private Configuration mConfiguration;
	private Bitmap bp_QRcode;
	private Machine machine;
	private APKVersion apkVersion;
	private Handler msgHandler;
	// PopupWindow popupWindow;
	public static Resources resources;
	TextView tv_popup;

	public static MainActivity getInstance() {
		return mainActivity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / (1024*1024));  
		Log.d("mytest01", "Max memory is " + maxMemory + "MB");
		resources=getResources();
		// Intent intent = new Intent(MainActivity.this, DownloadResource.class);// �򿪱���
		// startActivity(intent);
		Log.i("cycle", "MainActivity.onCreate");
		Intent intent = new Intent(this, PowerService.class);
		startService(intent);
		context = MainActivity.this;
		ExitApplication.getInstance().addActivity(this);
		Config.SCREENSHOT_ACTIVITY = this;
		mainActivity = this;



		PreferenceManager.init(context);

	}

	private void welcome() {
		Log.i("mytest", "welcome");
		
		/*Config.DIR_CACHE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TVDMS/dms_download"
				+ File.separator;
		Config.RESOURCE_INFO = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TVDMS/dms_resource"
				+ File.separator;
		Config.DIR_SCREENSHOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TVDMS/screenshot"+ File.separator;*/
		
		 /* Config.DIR_CACHE=getApplicationContext().getFilesDir().getAbsolutePath()+File
		  .separator+"dms_download"+File.separator;
		  Config.RESOURCE_INFO=getApplicationContext().getFilesDir().getAbsolutePath()+
		  File.separator+"dms_resource"+File.separator;
		  
		  Config.DIR_SCREENSHOT=getApplicationContext().getFilesDir().getAbsolutePath()
		  +File.separator+"/TVDMS/screenshot"+File.separator;*/
		  
		  Config.DIR_CACHE=getDir("dms_download",Context.MODE_PRIVATE | Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE).getPath()+File.separator;
		  Config.RESOURCE_INFO=getDir("dms_resource",Context.MODE_PRIVATE | Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE).getPath()+File.separator;
		  
		  Config.DIR_SCREENSHOT=getDir("screenshot",Context.MODE_PRIVATE | Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE).getPath()+File.separator;
		 
		
		Config.SCREEN_HEIGHT = ScreenUtil.getScreenHeight(this);
		Config.SCREEN_WIDTH = ScreenUtil.getScreenWidth(this);
		if(Config.SCREEN_WIDTH>Config.SCREEN_HEIGHT) {
			PreferenceManager.getInstance().setOrientation(0);
		}else if(Config.SCREEN_WIDTH<Config.SCREEN_HEIGHT){
			PreferenceManager.getInstance().setOrientation(1);
		}
		bg = (RelativeLayout) findViewById(R.id.bg);

		if (PreferenceManager.getInstance().getIsFirstBoot()) {
			PreferenceManager.getInstance().setIsFirstBoot(false);
			final File fileDir_cache = new File(Config.DIR_CACHE);
			final File fileResource_info = new File(Config.RESOURCE_INFO);

			if (fileDir_cache.exists() || fileResource_info.exists()) {
				if (fileDir_cache.isFile() || fileResource_info.isFile()) {
					FileUtil.deleteFile(fileDir_cache);
					FileUtil.deleteFile(fileResource_info);
					init();
				} else {
					File[] fileDir_cacheArr = fileDir_cache.listFiles();
					File[] fileResource_infoArr = fileResource_info.listFiles();

					if (fileDir_cacheArr.length > 0 || fileResource_infoArr.length > 0) {
						/*
						 * new
						 * AlertDialog.Builder(this).setMessage("����Ŀ��·���в����ļ����Ƿ����").setPositiveButton(
						 * "ȷ��", new DialogInterface.OnClickListener() {
						 * 
						 * public void onClick(DialogInterface dialog, int which) {
						 * 
						 * } });
						 */

						View popup_view = LayoutInflater.from(context).inflate(R.layout.popup_view, null, false);

						final AlertDialog popup_dialog = new Builder(this).setView(popup_view).show();


						popup_dialog.setOnDismissListener(new OnDismissListener() {

							public void onDismiss(DialogInterface dialog) {
								init();
							}
						});

						// popup_dialog.seton

						tv_popup = (TextView) popup_view.findViewById(R.id.tv_popup);
						Button btn_popup_ok = (Button) popup_view.findViewById(R.id.btn_popup_ok);
						Button btn_popup_cancle = (Button) popup_view.findViewById(R.id.btn_popup_cancle);

						btn_popup_ok.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								tv_popup.setText(MainActivity.resources.getString(R.string.tv_startdelete));
								FileUtil.deleteFile(fileDir_cache);
								FileUtil.deleteFile(fileResource_info);
								popup_dialog.dismiss();
							}
						});

						btn_popup_cancle.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								popup_dialog.dismiss();
							}
						});

					} else {
						init();
					}
				}
			} else {
				init();
			}

		} else {
			init();
		}
	}

	private void init() {

		if (Config.serialNumber == null || "unknown".equals(Config.serialNumber)) {
			Config.serialNumber = "mac" + CommonUtil.getMacId(MainActivity.this);
		}
		Config.installAPKPath = getDir("APKcache",
				Context.MODE_PRIVATE | Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE).getPath();

		/*
		 * Config.DIR_CACHE=getApplicationContext().getFilesDir().getAbsolutePath()+File
		 * .separator+"dms_download"+File.separator;
		 * Config.RESOURCE_INFO=getApplicationContext().getFilesDir().getAbsolutePath()+
		 * File.separator+"dms_resource"+File.separator;
		 * 
		 * Config.DIR_SCREENSHOT=getApplicationContext().getFilesDir().getAbsolutePath()
		 * +File.separator+"/TVDMS/screenshot"+File.separator;
		 */

	//	Config.DIR_SCREENSHOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TVDMS/screenshot"+ File.separator;
		
		Config.versionName = CommonUtil.getAppVersionName(MainActivity.this);

		// Config.DIR_CACHE=Environment.getExternalStorageDirectory()

		Log.i("mytest", "Config.DIR_CACHE=" + Config.DIR_CACHE + ",Config.RESOURCE_INFO=" + Config.RESOURCE_INFO);

		Config.DEVICE_NAME = PreferenceManager.getInstance().getDeviceName();
		Config.USER_GROUP_NAME = PreferenceManager.getInstance().getUserName();

		String url = PreferenceManager.getInstance().getDmsUrl();
		if (url == null || "".equals(url)) {

		} else {

			Config.URL = url;

		}

		Config.init(context);
		Config.IP = CommonUtil.getIP(MainActivity.this);

		if (Config.DEVICE_NAME == null || "".equals(Config.DEVICE_NAME)) {
			if (Config.USER_GROUP_NAME != null && !"".equals(Config.USER_GROUP_NAME)) {
				Config.DEVICE_NAME = Config.USER_GROUP_NAME + "_"
						+ CommonUtil.getSerialNumberRandomStr(Config.serialNumber);
			}
		}

		layout_progress = (LinearLayout) findViewById(R.id.layout_progress);
		tv_deviceName = (TextView) findViewById(R.id.tv_deviceName);

		tv_deviceName.setText("deviceName:" + Config.DEVICE_NAME + "\ndeviceId:" + Config.serialNumber);

		tv_prompt_isRegister = (TextView) findViewById(R.id.tv_prompt_isRegister);
		tv_prompt_noRegister = (TextView) findViewById(R.id.tv_prompt_noRegister);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		REGISTER_URL = "http://weixin.qq.com/r/2ygbAxjESkU1rVxg933J?macid=" + Config.serialNumber + "&memory_size="
				+ CommonUtil.getTotalInternalMemorySize() + "&memory_used="
				+ CommonUtil.getAvailableInternalMemorySize() + "&model=" + Config.DEVICE_MODEL;

		inflater = LayoutInflater.from(MainActivity.this);

		qr_layout = (LinearLayout) inflater.inflate(R.layout.qr_activity, null);

		iv_QRcode = (ImageView) qr_layout.findViewById(R.id.iv_qr);

		et_code1 = (EditText) qr_layout.findViewById(R.id.et_code);

		btn_commit = (Button) qr_layout.findViewById(R.id.btn_commit);

		mConfiguration = this.getResources().getConfiguration(); // ��ȡ���õ�������Ϣ

		if (Config.SCREEN_WIDTH > Config.SCREEN_HEIGHT) {
			bp_QRcode = generateBitmap(REGISTER_URL, Config.SCREEN_HEIGHT / 2, Config.SCREEN_HEIGHT / 2);
			et_code1.setWidth(Config.SCREEN_HEIGHT / 2);
			btn_commit.setWidth(Config.SCREEN_HEIGHT / 10);
		} else {
			bp_QRcode = generateBitmap(REGISTER_URL, Config.SCREEN_WIDTH / 2, Config.SCREEN_WIDTH / 2);
			et_code1.setWidth(Config.SCREEN_WIDTH / 2);
			btn_commit.setWidth(Config.SCREEN_WIDTH / 10);
		}

		iv_QRcode.setImageBitmap(bp_QRcode);
		dialog = new CustomDialog(this, R.style.CustomDialog);

		dialog.setContentView(qr_layout);
		dialog.setTitle(MainActivity.resources.getString(R.string.msg_scan));
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				layout_progress.setVisibility(View.VISIBLE);
				if (registerState == Config.REGISTER_YES) {

					progressBar.setVisibility(View.VISIBLE);
					tv_prompt_isRegister.setVisibility(View.VISIBLE);
					tv_prompt_noRegister.setVisibility(View.GONE);
				} else {
					progressBar.setVisibility(View.GONE);
					tv_prompt_isRegister.setVisibility(View.GONE);
					tv_prompt_noRegister.setVisibility(View.VISIBLE);
				}
				if (isLand) {
					bg.setBackgroundResource(R.drawable.background_land);
				} else {
					bg.setBackgroundResource(R.drawable.background);
				}
			}
		});
		dialog.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				// TODO Auto-generated method stub
				layout_progress.setVisibility(View.GONE);
				tv_prompt_noRegister.setVisibility(View.GONE);
				if (isLand) {
					bg.setBackgroundResource(R.drawable.background_land_qr);
				} else {
					bg.setBackgroundResource(R.drawable.background_qr);
				}
			}
		});
		btn_commit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String newMacId = letterToNum(Config.serialNumber);
				String code = newMacId.substring(newMacId.length() - 6, newMacId.length());
				String codeStr = et_code1.getText().toString().trim();
				if (code.equals(codeStr)) {
					if (NetworkUtil.isNetworkAvailable(MainActivity.this)) {

						new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									String path = Config.URL + "android/DMSDevice!register.action?sn="
											+ Config.serialNumber + "&memory_size="
											+ CommonUtil.getTotalInternalMemorySize() + "&memory_used="
											+ CommonUtil.getAvailableInternalMemorySize() + "&model="
											+ Config.DEVICE_MODEL;
									Log.i("connect", "MainActivity.init path=" + path);
									registerStateResult = BaseService.connService(path, 0);
									msgHandler.sendEmptyMessage(0);
								} catch (Exception ex) {
								}

							}

						}).start();
					} else {
						Toast.makeText(MainActivity.this, MainActivity.resources.getString(R.string.tip_registerfailbyinternet), Toast.LENGTH_SHORT).show();
					}

				} else {
					et_code1.setText("");
					Toast.makeText(MainActivity.this, MainActivity.resources.getString(R.string.tip_notcorrectcode), Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		if (mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			isLand = true;
			bg.setBackgroundResource(R.drawable.background_land);
		} else if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
			isLand = false;
			bg.setBackgroundResource(R.drawable.background);
		}
		msgHandler = new Handler() {

			public void handleMessage(Message msg) {

				switch (msg.what) {
				case 0:
					Log.i("connect", "MainActivity.registerStateResult=" + registerStateResult);
					if (registerStateResult != null && !"".equals(registerStateResult)) {
						Log.i("connect", "MainActivity.msgHandler.handleMessage   registerStateResult != null");
						RegisterResult registerResult = JsonUtil.getRegisterState(registerStateResult);
						if (registerResult.getCustomerId() != 0 && !"".equals(registerResult.getCustomerName())) {
							Log.i("connect",
									"MainActivity.msgHandler.handleMessage   registerResult.getCustomerName()!=null");
							registerState = 1;
							Config.USER_GROUP_NAME = registerResult.getCustomerName();
							PreferenceManager.getInstance().setUserName(registerResult.getCustomerName());
							
							/*Config.DEVICE_NAME = registerResult.getCustomerName() + "_"
									+ CommonUtil.getSerialNumberRandomStr(Config.serialNumber);
							
							PreferenceManager.getInstance().setDeviceName(Config.DEVICE_NAME);*/
							PreferenceManager.getInstance().setRegisterState(registerState);
						} else {
							Log.i("connect",
									"MainActivity.msgHandler.handleMessage   registerResult.getCustomerName()==null");

							if (mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
								isLand = true;
								bg.setBackgroundResource(R.drawable.background_land_qr);
							} else if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
								isLand = false;
								bg.setBackgroundResource(R.drawable.background_qr);
							}
							Toast.makeText(MainActivity.this, MainActivity.resources.getString(R.string.tip_registerfailbynotscan), Toast.LENGTH_SHORT).show();
							showQr();
							return;
						}

					} else {
						if (mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
							isLand = true;

							bg.setBackgroundResource(R.drawable.background_land_qr);

						} else if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
							isLand = false;
							bg.setBackgroundResource(R.drawable.background_qr);
						}
						Toast.makeText(MainActivity.this, MainActivity.resources.getString(R.string.tip_registerfailbynotscan), Toast.LENGTH_SHORT).show();
						showQr();
						return;
					}

					tv_deviceName.setText("deviceName:" + Config.DEVICE_NAME + "\ndeviceId:" + Config.serialNumber);

					if (registerState == Config.REGISTER_YES) {
						dialog.dismiss();
						Toast.makeText(MainActivity.this, MainActivity.resources.getString(R.string.tip_registersuccess), Toast.LENGTH_SHORT).show();
						startTV();
					} else {
						/*
						 * register(); return;
						 */

						if (mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
							isLand = true;
							bg.setBackgroundResource(R.drawable.background_land_qr);
						} else if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
							isLand = false;
							bg.setBackgroundResource(R.drawable.background_qr);
						}
						Toast.makeText(MainActivity.this, MainActivity.resources.getString(R.string.tip_registerfailbynotscan), Toast.LENGTH_SHORT).show();
						showQr();
					}
					break;
				case 1:
					Log.i("connect", "MainActivity.result=" + result);
					if (result != null && !"".equals(result)) {
						Log.i("connect", "result != null");
						apkVersion = APKVersion.getAPKVersion(result);
						machine = JsonUtil.getMachine(result);
						Log.i("connect","MainActivity.isCheckedUpdate="+isCheckedUpdate);
						if (isCheckedUpdate) {
							
							if (machine != null) {
								UserGroup userGroup = machine.getUserGroup();
								String deviceName = machine.getDeviceName();
								String topicName = machine.getTopicName();
								if (userGroup != null) {
									int id = userGroup.getId();
									String name = userGroup.getName();
									if (name != null && !"".equals(name) && !"null".equals(name)) {

										Config.USER_GROUP_NAME = name;
										PreferenceManager.getInstance().setUserName(Config.USER_GROUP_NAME);
									}

								}
								if (deviceName != null && !"".equals(deviceName) && !"null".equals(deviceName)) {
									Config.DEVICE_NAME = deviceName;
									PreferenceManager.getInstance().setDeviceName(deviceName);
								}
								if (topicName != null && !"".equals(topicName) && !"null".equals(topicName)) {
									// Config.topicName = topicName;
									PreferenceManager.getInstance().setTopicName(topicName);
								} else {
									topicName = PreferenceManager.getInstance().getTopicName();
									if (topicName != null && !"".equals(topicName) && !"null".equals(topicName)) {
										// Config.topicName = topicName;
									}
								}
							}
							tv_deviceName
									.setText("deviceName:" + Config.DEVICE_NAME + "\ndeviceId:" + Config.serialNumber);

							Intent intent = null;
							if (isHasProgram) {
								intent = new Intent(MainActivity.this, Dms.class);
								startActivity(intent);
							} else {
								intent = new Intent(MainActivity.this, MQTTservice.class);
								startService(intent);
							}

							
						} else {
							Log.i("connect", "apkVersion.getVersionCode()= "+apkVersion.getVersionCode());
							if (apkVersion != null && StringUtils.compareTo(apkVersion.getVersionCode(),
									PackageUtils.getAppVersionCode(context) + "")) {
								Log.i("connect",
										"MainActivity.apkVersion.getVersionCode()=" + apkVersion.getVersionCode());

								String apkurl = apkVersion.getApkurl();
								String apkName = apkurl.substring(apkurl.lastIndexOf("/") + 1);

								if (PreferenceManager.getInstance().getIsInstallAPKComplete(apkVersion.getApkurl())) {
									Log.i("connect", "MainActivity.InstallAPKComplete=true");

									if (FileUtil.getIsExists(Config.installAPKPath + "/" + apkName)) {
										isCheckedUpdate = true;
										msgHandler.sendEmptyMessage(1);

									} else {
										Log.i("connect",
												"MainActivity.FileUtil.getIsExists(Config.installAPKPath/apkName)=false");
										Intent intent = new Intent(MainActivity.this, InstallAPKActivity.class);
										intent.putExtra("apkurl", apkurl);
										intent.putExtra("description", apkVersion.getDescription());
										intent.putExtra("type", InstallAPKActivity.TYPE_UPGRADE);
										startActivity(intent);
									}

								} else {
									Log.i("connect", "MainActivity.InstallAPKComplete=false");
									Intent intent = new Intent(MainActivity.this, InstallAPKActivity.class);
									intent.putExtra("apkurl", apkurl);
									intent.putExtra("description", apkVersion.getDescription());
									intent.putExtra("type", InstallAPKActivity.TYPE_UPGRADE);
									startActivity(intent);
								}

							} else {
								Log.i("connect", "MainActivity.apkVersion=null");
								isCheckedUpdate = true;
								msgHandler.sendEmptyMessage(1);

							}

						}
					} else {
						Log.i("connect", "result == null");
						Intent intent = null;
						if (isHasProgram) {
							intent = new Intent(MainActivity.this, Dms.class);
							startActivity(intent);
						} else {
							intent = new Intent(MainActivity.this, MQTTservice.class);
							startService(intent);
						}

					}
					break;

				case 2:
					isApplicationClose = false;
					PreferenceManager.getInstance().setRegisterState(Config.REGISTER_YES);
					registerState = Config.REGISTER_YES;

					if (dialog != null) {
						dialog.dismiss();
					}

					startTV();
					Toast.makeText(MainActivity.this, MainActivity.resources.getString(R.string.tip_registersuccess), Toast.LENGTH_SHORT).show();

					break;

				}

			}
		};
		programListList = FileUtil.getProgramListList(MainActivity.this);
		if (programListList == null || programListList.size() == 0) {
			isHasProgram = false;

		} else {
			isHasProgram = true;
		}
		if (isHasProgram) {
			progressBar.setVisibility(View.VISIBLE);
			tv_prompt_isRegister.setText(MainActivity.resources.getString(R.string.tv_startload));
		} else {
			progressBar.setVisibility(View.GONE);
			tv_prompt_isRegister.setText(MainActivity.resources.getString(R.string.tv_hasnotlocalresource));
		}
		registerState = PreferenceManager.getInstance().getRegisterState();

		register();

	}

	public static boolean isCheckedUpdate = false;

	public void register() {
		if (registerState == Config.REGISTER_YES) {
			if (mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
				isLand = true;
				bg.setBackgroundResource(R.drawable.background_land);
			} else if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
				isLand = false;
				bg.setBackgroundResource(R.drawable.background);
			}
			startTV();
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Message message = new Message();
					message.what = 0;
					if (NetworkUtil.isNetworkAvailable(context)) {

						try {
							String path = Config.URL + "android/DMSDevice!register.action?sn=" + Config.serialNumber
									+ "&memory_size=" + CommonUtil.getTotalInternalMemorySize() + "&memory_used="
									+ CommonUtil.getAvailableInternalMemorySize() + "&model=" + Config.DEVICE_MODEL;
							Log.i("connect", "MainActivity.init path=" + path);
							registerStateResult = BaseService.connService(path, 0);
						} catch (Exception ex) {
						}
					}
					msgHandler.sendMessage(message);
				}
			}).start();
		}
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// �ж���һ�ΰ���oldKeyDown�Ƿ���BACK��
			if (oldKeyDown != KeyEvent.KEYCODE_BACK) {
				oldKeyDown = KeyEvent.KEYCODE_BACK;
				oldKeyDownTime = new Date().getTime();

			} else {
				// �ж���һ�ΰ�BACK����ʱ��ͱ�������Ƿ�С1��
				if ((new Date().getTime() - oldKeyDownTime) < 2000) {
					isApplicationClose = true;

					ExitApplication.getInstance().exit();
				} else {
					Toast.makeText(MainActivity.this, MainActivity.resources.getString(R.string.tip_doubleclickexit), Toast.LENGTH_SHORT).show();
					oldKeyDownTime = new Date().getTime();
				}
			}
			return true;
		}
		oldKeyDown = keyCode;
		if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			if (registerState == Config.REGISTER_YES) {
				// startTV();
			} else {
				tv_prompt_noRegister.setVisibility(View.GONE);
				showQr();
			}
		}
		/*
		 * if (keyCode == 41 || keyCode == KeyEvent.KEYCODE_CHANNEL_UP) {
		 * 
		 * count++; if (count % 2 == 0) { Log.i("keyCode", "Toast.show");
		 * Toast.makeText(this, "�豸ID:" + PreferenceManager.getInstance().getRMID() +
		 * "  ��ǰ��Ŀ��" + Config.TEMPLATE_NAME + "  code:" +
		 * PackageUtils.getAppVersionCode(context), Toast.LENGTH_LONG) .show();
		 * Log.i("keyCode", "Toast.dis"); }
		 * 
		 * if (count % 5 == 0) { count = 0; Intent intent = new
		 * Intent(MainActivity.this, SettingsActivity.class); startActivity(intent); }
		 * 
		 * return true; }
		 */
		return super.onKeyDown(keyCode, event);
	}

	public void autoLogin() {
		Log.i("connect", "MainActivity.autoLogin()");
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				// if (NetworkUtil.isNetworkAvailable(context)) {

				try {
					String path = Config.URL + "android/DMSDevice!autoLogin.action?deviceid=" + Config.serialNumber
							+ "&memory_size=" + CommonUtil.getTotalInternalMemorySize() + "&memory_used="
							+ CommonUtil.getAvailableInternalMemorySize() + "&model=" + Config.DEVICE_MODEL;
					Log.i("connect", "MainActivity.init path=" + path);
					result = BaseService.connService(path, 0);
				} catch (Exception ex) {
				}
				// }
				msgHandler.sendMessage(message);
			}
		}).start();
	}

	// ���ɶ�ά��ͼƬ
	private Bitmap generateBitmap(String content, int width, int height) {
		Bitmap qrBitmap = null;
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		try {
			BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			int[] pixels = new int[width * height];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (encode.get(j, i)) {
						pixels[i * width + j] = 0x00000000;
					} else {
						pixels[i * width + j] = 0xffffffff;
					}
				}
			}
			qrBitmap = addLogo(Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565),
					BitmapFactory.decodeResource(getResources(), R.drawable.wx));
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return qrBitmap;
	}

	private Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {
		int qrBitmapWidth = qrBitmap.getWidth();
		int qrBitmapHeight = qrBitmap.getHeight();
		int logoBitmapWidth = logoBitmap.getWidth();
		int logoBitmapHeight = logoBitmap.getHeight();
		Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(blankBitmap);
		canvas.drawBitmap(qrBitmap, 0, 0, null);
	//	canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.save();
		float scaleSize = 1.0f;
		while ((logoBitmapWidth / scaleSize) > (qrBitmapWidth / 5)
				|| (logoBitmapHeight / scaleSize) > (qrBitmapHeight / 5)) {
			scaleSize *= 2;
		}

		// float sx = 1.0f / scaleSize;
		float sx = qrBitmapWidth / 5.0f / logoBitmapWidth;
		Log.i("scale", "qrBitmapWidth=" + qrBitmapWidth + ",logoBitmapWidth=" + logoBitmapWidth + ",sx=" + sx);
		canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
		canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2,
				null);
		canvas.restore();
		return blankBitmap;
	}

	// ����ע����
	public String letterToNum(String input) {
		String reg = "[a-zA-Z]";
		StringBuffer strBuf = new StringBuffer();
		input = input.toLowerCase();
		if (null != input && !"".equals(input)) {
			for (char c : input.toCharArray()) {
				if (String.valueOf(c).matches(reg)) {
					strBuf.append(c - 96);
				} else {
					strBuf.append(c);
				}
			}
			return strBuf.toString();
		} else {
			return input;
		}
	}

	// ��ʾ��ά��
	public void showQr() {
		layout_progress.setVisibility(View.GONE);
		// ��õ�ǰ����
		Window window = dialog.getWindow();
		// ��������
		WindowManager.LayoutParams lp = window.getAttributes();
		// �����߾�
		if (isLand) {
			window.setGravity(Gravity.LEFT);
			lp.x = Config.SCREEN_WIDTH / 5; // ��λ��X����
		} else {
			window.setGravity(Gravity.BOTTOM);
			lp.y = Config.SCREEN_HEIGHT * 2 / 5; // ��λ��Y����
		}
		// ����dialog�򿪺�activity���䰵
		lp.dimAmount = 0f;
		window.setAttributes(lp);
		dialog.show();
	}

	private void startTV() {
		Log.i("connect", "MainActivity.startTV()");

		if (CommonUtil.isNotEmpty(Config.serialNumber)) {
			autoLogin();
		} else {
			new Builder(context).setTitle(MainActivity.resources.getString(R.string.tip_tip)).setMessage(MainActivity.resources.getString(R.string.tip_cannotlogin))
					.setPositiveButton(MainActivity.resources.getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {

						}
					}).show();
		}

	}

	@Override
	protected void onRestart() {
		Log.i("cycle", "MainActivity.onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Config.SCREENSHOT_ACTIVITY = this;
		Log.i("cycle", "MainActivity.onResume");
		 getWindow().getDecorView()
         .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                 | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                 | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE );
		 //| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
		// getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		welcome();
	}

	@Override
	protected void onPause() {
		Log.i("cycle", "MainActivity.onPause");
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		Log.i("cycle", "MainActivity.onDestroy");
		if (dialog != null) {
			dialog.dismiss();
		}
		Intent intent = new Intent(this, MQTTservice.class);
		stopService(intent);
		
		
		super.onDestroy();
	}
}
