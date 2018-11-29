package tvdms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.victgroup.signup.dmsclient.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import adapter.MyGalleryImageAdapter;
import bean.ProgramItem;
import bean.ProgramPublish;
import bean.Scheduleoftime;
import bean.UITemplate;
import bean.Widget;
import config.Config;
import helper.PreferenceManager;
import helper.TemplateHelper;
import http.BaseService;
import service.MQTTservice;
import service.PowerService;
import ui.MyMediaPlayer;
import util.BitmapUtil;
import util.CommandUtil;
import util.CommonUtil;
import util.FileUtil;
import util.ScreenUtil;
import util.TextUtil;
import util.TimeUtil;
import util.UnZipUtil;
import widget.AutoScrollTextView;
import widget.Image3DSwitchView;
import widget.Image3DView;
import widget.MyGallery;
import widget.MyListView;

//program_id
@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
public class Dms extends Activity {
	/*
	 * RequestCreator requestCreator1; RequestCreator requestCreator2;
	 */
	static boolean isShowProgramErrStr=false;
	String programErrStr=MainActivity.resources.getString(R.string.file_error);
	ConnectivityManager cm;
	static int otherProgramPublishPosition = 0;
	String webPath;
	String netWorkChangeAction = "android.net.conn.CONNECTIVITY_CHANGE";
	public Intent service;
	String timeStr;
	boolean isAction = true;
	int volume;
	Bitmap bitmap_back;
	List<Bitmap> bitmapList;
	WebView wv_web;
	private int preSelImgIndex_show = 0;
	private String unRegisterResult;
	private int sType;
	private List<Map> mapList;
	public static String currentDownloadProgramPublishPath = null;
	private List<ProgramPublish> programPublishList, immediatelyProgramPublishList, timerProgramPublishList;
	private ProgramPublish programPublish, immediatelyProgramPublish, timerProgramPublish, previousProgramPublish;
	public static String programPublishId;
	private List<Scheduleoftime> scheduleoftimes;
	private Scheduleoftime scheduleoftime, currentScheduleoftime;
	private List<UITemplate> templates, templateList;
	private UITemplate template;
	private ProgramPublish selectProgramPublish;
	private List<Scheduleoftime> selectScheduleoftimes;
	private Scheduleoftime selectScheduleoftime;
	private List<UITemplate> selectTemplates;
	private float old_x,old_y;
	private String paths;
	private MyMediaPlayer player;
	private MyGallery gallery;
	private RelativeLayout main_layout;
	private RelativeLayout r_layout = null;
	private LinearLayout main_setting, device_info_layout, setting_layout, program_layout, schedule_layout;
	private SurfaceView sfv = null;
	private VideoView vv;
	private List<Image3DSwitchView> imageview_3dgroupList;
	private String videourl;
	private int index;
	public static TextView zimu;
	public List<String> urls;
	public boolean isActive;
	 private CmdReceiver receiver;
	public static String ACTION_MQTT = "ACTION_MQTT";
	private String messageId;
	 private BroadcastReceiver progressreceiver;
	private Button btn_isRegister, btn_isSaveSetting, btn_sub, btn_add, btn_delete_cache,btn_deleteAll, btn_saveDeviceInfo,
			btn_cancleSave;
	private EditText et_defaultVolume;
	private TextView tv_time, tv_sn, tv_customerName, et_deviceName, tv_ip, tv_mqtt, tv_mqtt_state, tv_tip1, tv_tip2,
			tv_download, tv_version;
	private EditText et_url;

	private CheckBox cb_isBootAutoStart, cb_isAllDate, cb_sunday, cb_monday, cb_tuesday, cb_wednesday, cb_thurday,
			cb_friday, cb_saturday;
	private Spinner sp_onHourTime, sp_onMinuteTime, sp_offHourTime, sp_offMinuteTime;
	TranslateAnimation txt_translateAnimation;
	public RadioGroup rg, rg_select_setting;
	public RadioButton radio1, radio2;
	// boolean isInit = false;
	private MyListView programlist_lv;
	private ViewFlipper vf_select_setting;
	private ListView program_lv, schedule_lv;
	private BaseAdapter adapter1, adapter2, adapter3;
	private AlertDialog program_dialog;
	private List<CheckBox> cb_dayList;
	private LayoutInflater inflater;
	private int cmdSource;
	private static int currentPosition;
	private static String recentlyProgramListPath;
	public static String currentProgramListPath;
	private static long loopTime = 60;
	static int time;
	static int stateTime;
	private List<String> programListList;
	private Handler timehandler = new Handler();
	String nextProgram = "";
	Timer timer;
	int version;

	TimerTask task = new TimerTask() {

		public void run() {
			// Log.i("timing_tag", "TimerTask time=" + time + ",loopTime=" + loopTime);

			time++;

			if (time > loopTime) {
				if(player==null) {
					
				currentPosition++;

				handler.sendEmptyMessage(0);
				}
			}

		}

	};
	// private String url = "http://signup.victgroup.com/tvdms2/ppt/001/index.html";
//menu
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("cycle", "Dms.onCreate");

		PreferenceManager.init(Dms.this);
		ExitApplication.getInstance().addActivity(this);
		// Intent intent = new Intent(Dms.this, PowerService.class);
	//	cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		service = new Intent(this, MQTTservice.class);
		// startService(intent);
		startService(service);

		this.isActive = true;
		registerBroadcastReceiver();
		registerProgressReceiver();
		time = 0;
		timer = new Timer();
		main_layout = new RelativeLayout(this);
        //	main_layout.setBackgroundColor(Color.TRANSPARENT);
    //    main_layout.setBackgroundResource(R.drawable.defaultback);
		setContentView(main_layout);

		Config.SCREEN_HEIGHT = ScreenUtil.getScreenHeight(getApplicationContext());
		Config.SCREEN_WIDTH = ScreenUtil.getScreenWidth(getApplicationContext());
		initDmsSetting();
		
		timer.schedule(task, 0, 1000);
		mapList = new ArrayList<Map>();

		/*if (FileUtil.checkUpdateResource(Dms.this)) {
Log.i("connect","Dms.checkUpdateResource=true");
			String templatesId =PreferenceManager.getInstance().getRecentlyDownloadProgramPublish();
					String temStr=FileUtil.readFileSdcard(Config.RESOURCE_INFO+templatesId);
            ProgramPublish downloadProgramPublish = TemplateHelper.getDownloadTemplate(getApplicationContext(), temStr, 0);
         //   String templatesId = downloadProgramPublish.getMessageId();
            String publishMessageId=PreferenceManager.getInstance().getPublishMessageId(templatesId);
            List<ProgramItem> programItemList=TemplateHelper.getProgramItemList(this,downloadProgramPublish,temStr);


            Intent intentDownload = new Intent(this, DownloadResource.class);
            if(DownloadResource.myDownload==null) {
                Log.i("connect", "Dms DownloadResource.myDownload==null");
                DownloadResource.programItemList=programItemList;
                intentDownload.putExtra("isStartDownload", true);
                intentDownload.putExtra("publishMessageId", publishMessageId);
                intentDownload.putExtra("templatesId", templatesId);
               startActivity(intentDownload);
                Log.i("connect", "(Dms)DownloadResource.class.create");
            }else {
                Log.i("connect", "Dms DownloadResource.myDownload!=null");
                intentDownload.putExtra("isStartDownload", false);
                startActivity(intentDownload);
                DownloadResource.myDownload.start(programItemList, publishMessageId,templatesId);
            }
            return;
		}*/

	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.i("cycle", "Dms.onResume");
		getWindow().getDecorView()
				.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
		// | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
		// getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		/*if (player != null) {
			player.start();
		}*/

		init();

	}
	public void init() {
		Config.SCREENSHOT_ACTIVITY = this;

		programListList = FileUtil.getProgramListList(Dms.this);

		programPublishList = FileUtil.getProgramListList(Dms.this, programListList);// ��ȡ�ų��б�

		if (programPublishList == null || programPublishList.size() == 0) {
			Log.i("mytest_hl", "Dms programPublishList == null");
			return;
		}

		immediatelyProgramPublishList = TemplateHelper.getImmediatelyProgramPublishList(programPublishList);

		timerProgramPublishList = TemplateHelper.getTimerProgramPublishList(programPublishList);

		// Log.i("connect", "Dms.onResume programPublishList.size=" +
		// programPublishList.size());

		getProgramPublish();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				getUITemplate();
				break;
			case 1:
				PreferenceManager.getInstance().setRegisterState(Config.REGISTER_NO);
				btn_isRegister.setEnabled(false);
				MainActivity.isApplicationClose = false;
				Toast.makeText(Dms.this, MainActivity.resources.getString(R.string.tip_cancleregister), Toast.LENGTH_LONG).show();
				if (bitmap_back != null) {
					bitmap_back.recycle();
					System.gc();
				}
				if (bitmapList != null) {
					for (Bitmap bitmap : bitmapList) {
						if (bitmap != null) {
							bitmap.recycle();
							System.gc();
						}
					}
				}
				template.setId(-1);
				time = 0;
				Config.programId = -1;
				Intent intent = new Intent(Dms.this, MainActivity.class);
				startActivity(intent);
				FileUtil.deleteFile(new File(Config.RESOURCE_INFO));
				FileUtil.deleteFile(new File(Config.DIR_CACHE));
				new Thread(new Runnable() {

					public void run() {
						FileUtil.deleteDatabase(Dms.this);
					}

				}).start();
				finish();

				break;
			case 2:
				String result = (String) msg.obj;

				if (result != null && !"".equals(result) && "true".equals(result)) {
					PreferenceManager.getInstance().setDeviceName(et_deviceName.getText().toString());
					Config.DEVICE_NAME = et_deviceName.getText().toString();
					Toast.makeText(Dms.this, MainActivity.resources.getString(R.string.tip_rename) + et_deviceName.getText().toString(), Toast.LENGTH_SHORT).show();
				} else {
					et_deviceName.setText(PreferenceManager.getInstance().getDeviceName());
					Toast.makeText(Dms.this, MainActivity.resources.getString(R.string.tip_renamefail), Toast.LENGTH_SHORT).show();
				}
				break;
			case 3:
				tv_time.setText(timeStr);
				tv_ip.setText(CommonUtil.getIP(Dms.this));
				if (tv_clock != null) {
					tv_clock.setText(timeStr + nextProgram);
				}
				if (tv_widget_clock != null) {
					tv_widget_clock.setText(timeStr);
				}

				break;
			case 4:
				getProgramPublish();
				break;
			case 5:
				tv_mqtt_state.setText(MainActivity.resources.getString(R.string.mqtt_disconnected));
				tv_mqtt_state.setTextColor(Color.WHITE);
				break;
			case 6:
				tv_mqtt_state.setText(MainActivity.resources.getString(R.string.mqtt_connected));
				tv_mqtt_state.setTextColor(Color.GREEN);
				break;
			case 7:
				   showMessage(netClosed_txt, MainActivity.resources.getString(R.string.internet_disconnected), 30, 1, "FF0000", "30", "top");
				break;
			case 8:
				if (netClosed_txt != null) {
					netClosed_txt.clearAnimation();
					netClosed_txt.setText("");
				}
				break;
			case 9:
				showMessage(err_txt,programErrStr , 10, 1, "FF0000", "30", "top");
				break;
			case 10:
				realseResource();
				if (main_layout != null) {
					main_layout.setBackgroundResource(0);
					main_layout.removeAllViews();
				}
				
				FileUtil.deleteFile(new File(Config.RESOURCE_INFO));
				FileUtil.deleteFile(new File(Config.DIR_CACHE));
				FileUtil.deleteUnuseDatabase(Dms.this);
				
				programListList = FileUtil.getProgramListList(Dms.this);

				programPublishList = FileUtil.getProgramListList(Dms.this, programListList);// ��ȡ�ų��б�

				/*if (programPublishList == null || programPublishList.size() == 0) {
					finish();
					return;
				}*/
				programPublish = null;

				immediatelyProgramPublishList = TemplateHelper.getImmediatelyProgramPublishList(programPublishList);

				timerProgramPublishList = TemplateHelper.getTimerProgramPublishList(programPublishList);

				// Log.i("connect", "Dms.onResume programPublishList.size=" +
				// programPublishList.size());

				getProgramPublish();
				break;
			case 11:
				init();
				break;
			default:
				break;

			}
		}
	};

	public void getProgramPublish() {

		if (currentDownloadProgramPublishPath == null) {

			if (programPublish == null) {
				programPublish = getProgramPublish(programPublishList,
						PreferenceManager.getInstance().getTemplatesId());
				immediatelyProgramPublish = getProgramPublish(programPublishList,
						PreferenceManager.getInstance().getImmediatelyTemplatesId());
				timerProgramPublish = getProgramPublish(programPublishList,
						PreferenceManager.getInstance().getTimerTemplatesId());
			} else {
				if (selectProgramPublish == null) {
				} else {
					programPublish = selectProgramPublish;
					selectProgramPublish = null;
				}
			}

		} else {

			programPublish = getProgramPublish(programPublishList, currentDownloadProgramPublishPath);
			immediatelyProgramPublish = getProgramPublish(programPublishList,
					PreferenceManager.getInstance().getImmediatelyTemplatesId());
			timerProgramPublish = getProgramPublish(programPublishList,
					PreferenceManager.getInstance().getTimerTemplatesId());
			currentDownloadProgramPublishPath = null;
		}

		if (programPublish == null) {
			if (programPublishList != null) {
				programPublish = programPublishList.get(0);
			}
		} else {
			programPublishId = programPublish.getMessageId();

		}

		if (adapter1 != null) {
			adapter1.notifyDataSetChanged();
		}
		getScheduleoftime();
	}

	public void getScheduleoftime() {
		if (programPublish == null) {
			// currentProgramPublish = null;
			currentScheduleoftime = null;

		} else {

			if (programPublish.getRows().get(0).getStype() == 0) {
				immediatelyProgramPublish = programPublish;
				PreferenceManager.getInstance().setImmediatelyTemplatesId(programPublish.getMessageId());
				if (!radio1.isChecked()) {
					isAction = false;
					radio1.setChecked(true);
					schedule_layout.setVisibility(View.GONE);
				}

			} else if (programPublish.getRows().get(0).getStype() == 1) {
				timerProgramPublish = programPublish;
				PreferenceManager.getInstance().setTimerTemplatesId(programPublish.getMessageId());
				if (!radio2.isChecked()) {
					isAction = false;
					radio2.setChecked(true);
					schedule_layout.setVisibility(View.VISIBLE);
				}
			}

			PreferenceManager.getInstance().setTemplatesId(programPublish.getMessageId());

			scheduleoftimes = programPublish.getRows().get(0).getScheduleoftimes();// ��ȡʱ���б�

			mapList.clear();
			for (Scheduleoftime s : scheduleoftimes) {
				Map map = new HashMap();
				long stime = TimeUtil.getTimeFromString(s.getStime());
				long etime = TimeUtil.getTimeFromString(s.getEtime());
				map.put("scheduleoftime", s);
				map.put("stime", stime);
				map.put("etime", etime);
				mapList.add(map);
			}

			scheduleoftime = getScheduleoftime(scheduleoftimes);

			if (scheduleoftime == null) {
				if (immediatelyProgramPublish != null) {
					// currentProgramPublish = immediatelyProgramPublish;
					currentScheduleoftime = immediatelyProgramPublish.getRows().get(0).getScheduleoftimes().get(0);
				} else {
					// currentProgramPublish = null;
					currentScheduleoftime = null;
				}
			} else {
				// currentProgramPublish = programPublish;
				currentScheduleoftime = scheduleoftime;
			}

		}
		getUITemplate();
	}

	public void getUITemplate() {
		UITemplate uITemplate = null;
		if (currentScheduleoftime == null) {
			uITemplate = TemplateHelper.getDefaultUITemplate();
		} else {
			templates = currentScheduleoftime.getTemplates();
			uITemplate = templates.get(currentPosition % templates.size());
		}
		if (uITemplate == null) {
			return;
		} else {
			if (template == null) {
				template = uITemplate;
			} else {
				if (template.getId() == uITemplate.getId()) {
					if (template.getVersion() == uITemplate.getVersion()) {
						
						List<Widget> widgetList=template.getWidgets();
						if(widgetList==null) {
							return;
						}else {
							boolean isContainPPT=false;
							for(Widget widget:widgetList) {
								int type=widget.getType().getId();
								
								if(type==Config.WIDGET_PPT) {
									isContainPPT=true;
									break;
								}
							}
							if(isContainPPT) {
								return;
							}else {
								return;
							}
						}
						
					} else {
						template = uITemplate;
						time = 0;
					}
				} else {
					template = uITemplate;
					time = 0;
				}
			}
		}

		Config.programId = template.getId();
		
		
	
         // �������л�
		Intent intent = new Intent(Config.ORIENTATION_ACTION);

		int screenWidth = template.getScreenWidth();
		int screenHeight = template.getScreenHeight();
		Log.i("mytest", "Dms.template.screenWidth=" + screenWidth + ",template.screenHeight=" + screenHeight
				+ ",Config.SCREEN_WIDTH=" + Config.SCREEN_WIDTH + ",Config.SCREEN_HEIGHT=" + Config.SCREEN_HEIGHT);

		if (screenWidth > screenHeight) {
			Log.i("mytest", "screenWidth > screenHeight");
			
			 
			if (Config.SCREEN_HEIGHT > Config.SCREEN_WIDTH) {
				Log.i("mytest", "Config.SCREEN_HEIGHT > Config.SCREEN_WIDTH");
				intent.putExtra("orientation", 0);
				sendBroadcast(intent);
				return;
			}
		} else if (screenWidth < screenHeight) {
			Log.i("mytest", "screenWidth < screenHeight");
			
			 
			if (Config.SCREEN_HEIGHT < Config.SCREEN_WIDTH) {
				Log.i("mytest", "Config.SCREEN_HEIGHT < Config.SCREEN_WIDTH");
				intent.putExtra("orientation", 1);
				sendBroadcast(intent);
				return;
			}

		}

		Log.i("mytest",
				"Dms Config.SCREEN_WIDTH=" + Config.SCREEN_WIDTH + ",Config.SCREEN_HEIGHT=" + Config.SCREEN_HEIGHT);

		initDms();
	}

	public void initDms() {
		realseResource();
		if (main_layout != null) {
			main_layout.setBackgroundResource(0);
			main_layout.removeAllViews();
		}


		// ����������
		imageview_3dgroupList = new ArrayList<Image3DSwitchView>();
		System.gc();
		try {
		createWidget();// ��ʼ��ģ��
		otherProgramPublishPosition = 0;
		} catch (Exception e) {
			e.printStackTrace();
			String currentMessageId = programPublish.getMessageId();
			playOtherProgramPublish(currentMessageId);

		}
	}

	public void playOtherProgramPublish(String currentMessageId) {
int size=programPublishList.size();
		if (otherProgramPublishPosition > size - 1) {
			Log.i("mytest01","Dms otherProgramPublishPosition="+otherProgramPublishPosition+",programPublishList.size()="+size);
			handler.sendEmptyMessage(9);

			return;
		}
		programPublish = programPublishList.get(otherProgramPublishPosition);

		otherProgramPublishPosition++;
		if (currentMessageId.equals(programPublish.getMessageId())) {
			playOtherProgramPublish(currentMessageId);
		} else {
			isShowProgramErrStr=true;
			Log.i("mytest01","Dms isShowProgramErrStr=true");
			getScheduleoftime();
		//	showMessage(err_txt,programErrStr, 10, 5, "FF0000", "30", "top");
		}

	}
	
	public void createWidget() {
		// ��ȡ����ģ��
		if (template != null) {
			if (template.getId() == 0) {
				main_layout.setBackgroundResource(R.drawable.defaultback);
			} else {
				if (CommonUtil.isNotEmpty(template.getBackground())) {

					String path = template.getBackground_path();

					Target target = new Target() {
						@Override
						public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
							//替换背景
							main_layout.setBackgroundDrawable(new BitmapDrawable(bitmap));

						}

						@Override
						public void onBitmapFailed(Drawable drawable) {

						}

						@Override
						public void onPrepareLoad(Drawable drawable) {

						}
					};

					Picasso.with(this).load(new File(path)).into(target);

				}
			}
			List<Widget> widgets = template.getWidgets();
			// Log.i("connect", "template.getId()=" + template.getId());
			bitmapList = null;
			for (int i = 0; i < widgets.size(); i++) {
				Widget widget = widgets.get(i);
				if (widget.getPlayDuration() < 5) {
					widget.setPlayDuration(5);
				}

					createWidget(widget);
				

			}
			
			
			if(isShowProgramErrStr) {
				Log.i("mytest01","Dms if(isShowProgramErrStr)");
				isShowProgramErrStr=false;
				handler.sendEmptyMessage(9);
			}
			

		}
	}

	

	TextView tv_clock;
	TextView tv_widget_clock;

	private void createWidget(Widget widget) {
		RelativeLayout.LayoutParams params = null;
		double heightPercent = widget.getHeightPercent();
		double widthPercent = widget.getWidthPercent();
		// Log.i("mytest","Dms Config.SCREEN_WIDTH=" + Config.SCREEN_WIDTH +
		// ",Config.SCREEN_HEIGHT=" + Config.SCREEN_HEIGHT);
		int height = (int) (Config.SCREEN_HEIGHT * heightPercent / 100);
		int width = (int) (Config.SCREEN_WIDTH * widthPercent / 100);

		int realHeight=height;
		int realWidth=width;
		
		
		if (heightPercent == 100.0) {

			height = LayoutParams.MATCH_PARENT;
		}
		if (widthPercent == 100.0) {

			width = LayoutParams.MATCH_PARENT;
		}

		// Log.i("connect", "widget.getType()=" + widget.getType().getId());
		params = new RelativeLayout.LayoutParams(width, height);
		params.setMargins(widget.getLeft(), widget.getTop(), 0, 0);

		switch (widget.getType().getId()) {
		case 0:
			if (tv_clock == null) {
				tv_clock = new TextView(Dms.this);
				int tv_clock_id=MainActivity.resources.getInteger(R.integer.tv_clock_id);

		        tv_clock.setId(tv_clock_id);

				tv_clock.setTextSize(30);
			}

			tv_clock.setText(TimeUtil.getTimeStr());
			tv_clock.setLayoutParams(params);
			if (main_layout.findViewById(tv_clock.getId()) == null) {
				main_layout.addView(tv_clock);
			}
			break;
		case Config.WIDGET_MEDIAPLAYER:
			r_layout = new RelativeLayout(this);
			r_layout.setLayoutParams(params);
			main_layout.addView(r_layout);

			RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			sfv = new SurfaceView(this);
			sfv.setLayoutParams(params4);

			if (widget.getResources() == null || widget.getResources().size() == 0) {
				return;
			}
			r_layout.addView(sfv);
			RelativeLayout.LayoutParams txtparams = new RelativeLayout.LayoutParams(width / 2,
					LayoutParams.WRAP_CONTENT);
			txtparams.setMargins(width / 3, height - 38, 0, 0);
			// textview
			// ��߾�Ϊ��Ƶ���1/3
			zimu = new TextView(this);
			zimu.setLayoutParams(txtparams);
			zimu.setTextSize(28);
			zimu.setText("");
			r_layout.addView(zimu);
			player = new MyMediaPlayer(Dms.this, sfv, widget.getResourcePathList());
			// ���һ��web
			// addWebView();
			Config.SCREENSHOT_ACTIVITY = null;
			break;

		case Config.WIDGET_IMAGEVIEWS:
			if (bitmapList == null) {
				bitmapList = new ArrayList<Bitmap>();
			}
			int size = widget.getResources().size();
			Picasso picasso = Picasso.with(Dms.this);
			int showWidth=realWidth;
			int showHeight=realHeight;
	//		RelativeLayout imageView_layout=new RelativeLayout(this);
	//		imageView_layout.setLayoutParams(params);
	//		main_layout.addView(imageView_layout);
			if (size == 1) {// ����ͼƬ
				ImageView imageView = new ImageView(this);
				imageView.setLayoutParams(params);

				String imgPath=widget.getResourcePathList().get(0);
				picasso.load(new File(imgPath))
						.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).config(Bitmap.Config.RGB_565).resize(showWidth, showHeight)
						.into(imageView);
				
				main_layout.addView(imageView);
				return;
			}
			int style = widget.getStyle();
			AnimationSet animationSet = new AnimationSet(true);
			final List<String> imgpaths = widget.getResourcePathList();

			if (style == Config.STYLE_GALLERY) {
				gallery = new MyGallery(Dms.this);
				gallery.setAnimationDuration(2000);
				gallery.startViewAimation(widget.getPlayDuration());
				gallery.setLayoutParams(params);
				gallery.setSoundEffectsEnabled(false);// ����ʱ����
				gallery.setUnselectedAlpha(1);// δѡ��ͼƬ��͸���ȣ���������ø����ԣ�ͼƬ���е�͸��
				gallery.setAdapter(new MyGalleryImageAdapter(Dms.this, imgpaths, realWidth, realHeight));

				// gallery.setSelection(2, true);

				gallery.setFocusable(false);
				main_layout.addView(gallery);

				return;
			}
			if (style == Config.STYLE_IMAGES_3D) {
				Image3DSwitchView imageview_group = new Image3DSwitchView(this, 1);
				imageview_group.setLayoutParams(params);
				/*
				 * RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
				 * LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
				 */

				for (String imgPath : imgpaths) {
					Image3DView img3dview = new Image3DView(this);
					img3dview.setScaleType(ScaleType.FIT_XY);
					
				//	picasso.load(new File(imgPath)).into(img3dview);

					picasso.load(new File(imgPath)).fit()
					.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).config(Bitmap.Config.RGB_565).resize(showWidth, showHeight)
					.into(img3dview);
					
					// img3dview.setImageBitmap(BitmapFactory.decodeFile(br.getFilepath()));

					imageview_group.addView(img3dview);
				}
				main_layout.addView(imageview_group);
				imageview_group.startViewAnimation(widget.getPlayDuration());
				imageview_3dgroupList.add(imageview_group);
				return;
			}
			final FrameLayout flayout = new FrameLayout(this);
			flayout.setLayoutParams(params);
			FrameLayout.LayoutParams fparams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			ImageView mShowPicture = new ImageView(this);
			mShowPicture.setLayoutParams(fparams);
			ImageView mHidePicture = new ImageView(this);
			mHidePicture.setLayoutParams(fparams);

			mHidePicture.setScaleType(ScaleType.FIT_XY);
			mShowPicture.setScaleType(ScaleType.FIT_XY);

			flayout.addView(mHidePicture);
			flayout.addView(mShowPicture);
			
			
			/*// ��ȡtxt�ؼ��߶�
			int flayout_w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			int flayout_h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			flayout.measure(flayout_w, flayout_h);
			int flayout_height = flayout.getMeasuredHeight();
			int flayout_width = flayout.getMeasuredWidth();
			
			
			 */
			main_layout.addView(flayout);
			switch (style) {
			case Config.STYLE_ALPHA:
				animationSet.addAnimation(new AlphaAnimation(0, 1));
				break;
			case Config.STYLE_GALLERY:
				break;
			case Config.STYLE_IMAGES_3D:
				break;
			case Config.STYLE_TOP:
				animationSet.addAnimation(new TranslateAnimation(0, 0,realHeight, 0));
				break;
			case Config.STYLE_SCALE_BIGGER:
				animationSet.addAnimation(new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f));
				break;
			case Config.STYLE_RIGHT:
				animationSet.addAnimation(new TranslateAnimation(-realWidth, 0,0, 0));
				break;
			case Config.STYLE_RIGHT_BOTTOM:
				animationSet.addAnimation(new TranslateAnimation(-realWidth, 0,-realHeight, 0));
				break;
			case Config.STYLE_ROTATE:
				animationSet.addAnimation(new RotateAnimation(720, 0, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f));
				animationSet.addAnimation(new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f));
				break;
			case Config.STYLE_SCALE_SMALLER:
				
				break;
			}
			

			if (imgpaths.size() <= 0) {
				return;
			}

			// Animation animation = new ScaleAnimation(0f, 1f, 0f, 1f,
			// Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);

			animationSet.setDuration(1000);

			fadeIn(picasso, mHidePicture, mShowPicture, imgpaths, 0, widget.getPlayDuration() * 1000,showWidth,
					showHeight, animationSet, template.getId());

			break;

		case Config.WIDGET_TEXT:

			// ScrollView sv = new ScrollView(this);
			// sv.setLayoutParams(params);
			// main_layout.addView(sv);
			// RelativeLayout.LayoutParams params_txt = new
			// RelativeLayout.LayoutParams(width,height);
			
			AutoScrollTextView tv = new AutoScrollTextView(this);
			
			//		TextView tv=new TextView(this);
			
			tv.setLayoutParams(params);
			
			
			int bgColor=Color.TRANSPARENT;
			
			try {
				bgColor=Color.parseColor(TextUtil.getColorStr(widget.getFontbgcolor()));
			}catch (Exception e) {
			}
			
			
			String gravityStr=widget.getHalign() + "_" + widget.getValign();
			int gravity=Gravity.CENTER;
			switch (gravityStr) {
			case "left_top":
				gravity=Gravity.LEFT | Gravity.TOP;
				break;
			case "left_middle":
				gravity=Gravity.LEFT | Gravity.CENTER;
				break;
			case "left_bottom":
				gravity=Gravity.LEFT | Gravity.BOTTOM;
				break;
			case "center_top":
				gravity=Gravity.CENTER | Gravity.TOP;
				break;
			case "center_middle":
				gravity=Gravity.CENTER;
				break;
			case "center_bottom":
				gravity=Gravity.CENTER | Gravity.BOTTOM;
				break;
			case "right_top":
				gravity=Gravity.RIGHT | Gravity.TOP;
				break;
			case "right_middle":
				gravity=Gravity.RIGHT | Gravity.CENTER;
				break;
			case "right_bottom":
				gravity=Gravity.RIGHT | Gravity.BOTTOM;
				break;
			}
			int fontsize = widget.getFontsize();
			if (fontsize == 0) {
				fontsize = 30;
			}
			int color=Color.RED;
			try {
				color=Color.parseColor(TextUtil.getColorStr(widget.getFontcolor()));
			} catch (Exception e) {
				
			}
String text=widget.getText();
if(text==null) {
	text="";
}



			/*// ��ȡtxt�ؼ��߶�
			int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			tv.measure(w, h);
			int tv_height = tv.getMeasuredHeight();
			int tv_width = tv.getMeasuredWidth();*/
tv.setBackgroundColor(bgColor);
tv.init(getWindowManager(),color,fontsize, text, gravity,width, height);

			switch (widget.getShowtype()) {

			case 0:
				tv.stopScroll();
				break;

			case 1:
				tv.startScroll();
				break;

			}


			
			
			
			main_layout.addView(tv);

			break;

		case Config.WIDGET_WEB:
			// Log.i("connect", "Config.WIDGET_WEB=" + Config.WIDGET_WEB);

			wv_web = new WebView(this);

			wv_web.setLayoutParams(params);

			main_layout.addView(wv_web);
			WebSettings webSetting = wv_web.getSettings();

			webSetting.setJavaScriptEnabled(true);

			webSetting.setUseWideViewPort(true);
			webSetting.setLoadWithOverviewMode(true);
			webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

			webSetting.setAppCacheEnabled(true);// ����localstorage���ش洢api
			webSetting.setDomStorageEnabled(true);// ����dom�洢(�ؼ��������)��ò������twitter��ʾ������Ҳ���������û�����õ�ԭ��
			webSetting.setLightTouchEnabled(true);// ����ѡ�й���
			webSetting.setDatabaseEnabled(true);// ����html5���ݿ⹦��

			webSetting.setMediaPlaybackRequiresUserGesture(false);

			webSetting.setCacheMode(webSetting.LOAD_DEFAULT);
			webSetting.setSupportZoom(true);
			webSetting.setSaveFormData(false);
			// webSetting.setBlockNetworkImage(true);// ��ͼƬ��������

			wv_web.setWebViewClient(new WebViewClient() {

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return true;
				}

				@Override
				public void onReceivedError(WebView var1, int var2, String var3, String var4) {
				}
			});

			wv_web.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			wv_web.setFocusable(false);

			final String htmlUrl = widget.getHtmlurl();
			final String htmlPath = widget.getHtmlPath();
			final String zipPath = widget.getZipPath();
			if (htmlUrl != null && !"".equals(htmlUrl)) {
				if (!FileUtil.fileIsExists(widget.getHtmlPath())) {
					new Thread(new Runnable() {

						@Override
						public void run() {

							UnZipUtil uzu = new UnZipUtil();

							try {
								// UnZipStreamUtil.doUnZip(FileUtil.getFileNameByUrl(zipPath),
								// Config.DIR_CACHE);
								uzu.unzip(FileUtil.getFileNameByUrl(zipPath), Config.DIR_CACHE);
							} catch (Exception e) {
								e.printStackTrace();
							}
							Message msg = new Message();
							msg.obj = htmlPath;
							web_handler.sendMessage(msg);

						}
					}).start();
				} else {
					wv_web.loadUrl("file://" + htmlPath);
					/*
					 * String url = "http://signup.victgroup.com/tvdms2/ppt/001/index.html";
					 * wv_web.loadUrl(url);
					 */
				}

			} else {
				String resUrl = widget.getResources().get(0).getResUrl();
				if (resUrl.startsWith("http")) {
					wv_web.loadUrl(resUrl);
				} else {

					wv_web.loadUrl(Config.URL + resUrl);

				}

			}
			break;

		case Config.WIDGET_WEATHER:
			WebView wv_weather = new WebView(this);

			wv_weather.setLayoutParams(params);

			wv_weather.setFocusable(false);
			main_layout.addView(wv_weather);
			// wv.loadUrl(Config.URL + widget.getResourceList().get(0).getFilepath());
			String bodyHTML = widget.getType().getHtmlContent();
			String head = "<head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"><style>img{max-width: 100%; width:auto; height:auto;}</style></head>";
			String data = "<html>" + head + "<body>" + bodyHTML + "</body></html>";

			if (data != null) {

				WebSettings settings = wv_weather.getSettings();
				settings.setDomStorageEnabled(true);
				settings.setMediaPlaybackRequiresUserGesture(false);
				wv_weather.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
				wv_weather.setWebViewClient(new WebViewClient() {
					public boolean shouldOverrideUrlLoading(WebView view, String url) {
						return false;
					}
				});
				settings.setJavaScriptEnabled(true);
				wv_weather.loadData(data, "text/html; charset=UTF-8", null);

			}

			break;
		case Config.WIDGET_PPT:
			/*ImageView pptback = new ImageView(this);
			pptback.setLayoutParams(params);
			pptback.setScaleType(ScaleType.FIT_XY);
			pptback.setImageResource(R.drawable.pptback);
			main_layout.addView(pptback);
			 */
			
			
			
			final String resPath = widget.getResourcePathList().get(0);
			Log.i("mytest_hl","resPath="+resPath);
			
			
			 new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String[] command = {"chmod", "777", resPath};  
						ProcessBuilder builder = new ProcessBuilder(command);  
						try {  
							Runtime.getRuntime().exec("chmod 777 "+resPath);
						builder.start(); 						
						
					    } catch (IOException e) {  
					       e.printStackTrace();							     
					  } 										
				  }
				}).start();
			
			startActivity(getPdfFileIntent(resPath));

			break;
		case Config.WIDGET_CLOCK:

			// params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT);
			// params.setMargins(widget.getLeft(), widget.getTop(), 0, 0);

			if (tv_widget_clock == null) {
				tv_widget_clock = new TextView(Dms.this);
				int tv_widget_clock_id=MainActivity.resources.getInteger(R.integer.tv_widget_clock_id);
				tv_widget_clock.setId(tv_widget_clock_id);
				tv_widget_clock.setTextSize(30);
			}
			// tv_widget_clock.setTextColor(Color.RED);
			// tv_widget_clock.setBackgroundColor(Color.GREEN);
			tv_widget_clock.setText(TimeUtil.getTimeStr());
			tv_widget_clock.setLayoutParams(params);
			tv_widget_clock.setGravity(Gravity.CENTER_VERTICAL);
			if (main_layout.findViewById(tv_widget_clock.getId()) == null) {
				main_layout.addView(tv_widget_clock);
			}

			break;

		}
		
		
	}

	public Intent getPdfFileIntent(String path) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(path));
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	Handler web_handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String path = (String) msg.obj;
			wv_web.loadUrl("file://" + path);

		}
	};

	public void initDmsSetting() {
		if (message_txt == null) {
			message_txt = new TextView(this);
			message_txt.setIncludeFontPadding(false);
			message_txt.setSingleLine();
		}
		int message_txt_id=MainActivity.resources.getInteger(R.integer.message_txt_id);
		message_txt.setId(message_txt_id);
		if (err_txt == null) {
			err_txt = new TextView(this);
			err_txt.setIncludeFontPadding(false);
			err_txt.setSingleLine();
		}
		int err_txt_id=MainActivity.resources.getInteger(R.integer.err_txt_id);
		err_txt.setId(err_txt_id);
		if (netClosed_txt == null) {
			netClosed_txt = new TextView(this);
			netClosed_txt.setIncludeFontPadding(false);
			netClosed_txt.setSingleLine();
		}
		int netClosed_txt_id=MainActivity.resources.getInteger(R.integer.netClosed_txt_id);
		netClosed_txt.setId(netClosed_txt_id);
		inflater = LayoutInflater.from(this);

		main_setting = (LinearLayout) inflater.inflate(R.layout.main_setting, null);
		device_info_layout = (LinearLayout) inflater.inflate(R.layout.device_info_layout, null);
		program_layout = (LinearLayout) inflater.inflate(R.layout.program_layout, null);
		setting_layout = (LinearLayout) inflater.inflate(R.layout.setting_layout, null);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Config.SCREEN_WIDTH * 4 / 5,
				Config.SCREEN_HEIGHT * 4 / 5);
		main_setting.setLayoutParams(params);

		rg_select_setting = (RadioGroup) main_setting.findViewById(R.id.rg_select_setting);
		vf_select_setting = (ViewFlipper) main_setting.findViewById(R.id.vf_select_setting);

		vf_select_setting.addView(device_info_layout);
		vf_select_setting.addView(program_layout);
		vf_select_setting.addView(setting_layout);

		rg_select_setting.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio0:
					vf_select_setting.setDisplayedChild(0);
					break;
				case R.id.radio1:
					vf_select_setting.setDisplayedChild(1);
					break;
				case R.id.radio2:
					vf_select_setting.setDisplayedChild(2);
					break;

				}
			}
		});

		tv_sn = (TextView) device_info_layout.findViewById(R.id.tv_sn);
		tv_customerName = (TextView) device_info_layout.findViewById(R.id.tv_customerName);
		et_deviceName = (EditText) device_info_layout.findViewById(R.id.et_deviceName);
		et_url = (EditText) device_info_layout.findViewById(R.id.et_url);
		tv_ip = (TextView) device_info_layout.findViewById(R.id.tv_ip);
		tv_mqtt = (TextView) device_info_layout.findViewById(R.id.tv_mqtt);
		tv_mqtt_state = (TextView) device_info_layout.findViewById(R.id.tv_mqtt_state);
		tv_tip1 = (TextView) device_info_layout.findViewById(R.id.tv_tip1);
		tv_tip2 = (TextView) device_info_layout.findViewById(R.id.tv_tip2);
		tv_download = (TextView) device_info_layout.findViewById(R.id.tv_download);
		tv_version = (TextView) device_info_layout.findViewById(R.id.tv_version);
		btn_saveDeviceInfo = (Button) device_info_layout.findViewById(R.id.btn_saveDeviceInfo);
		btn_cancleSave = (Button) device_info_layout.findViewById(R.id.btn_cancleSave);

		tv_sn.setText(Config.serialNumber);
		tv_customerName.setText(Config.USER_GROUP_NAME);
		et_deviceName.setText(Config.DEVICE_NAME);

		et_deviceName.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					tv_tip1.setText("");
				} else {
					String deviceName = et_deviceName.getText().toString().trim();
					if (deviceName != null && !"".equals(deviceName)) {
						tv_tip1.setText("");
					} else {
						tv_tip1.setText(MainActivity.resources.getString(R.string.tip_illegal_devicename));
					}

				}
			}
		});
		et_deviceName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String deviceName = et_deviceName.getText().toString().trim();
				if (deviceName != null && !"".equals(deviceName)) {
					tv_tip1.setText("");
				} else {
					tv_tip1.setText(MainActivity.resources.getString(R.string.tip_illegal_devicename));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// ����ǰ�ļ���

			}

			@Override
			public void afterTextChanged(Editable s) {
				// �����ļ���

			}
		});

		et_url.setText(Config.URL);

		et_url.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					tv_tip2.setText("");
				} else {
					String url = et_url.getText().toString();
					if (url != null && !"".equals(url)) {
						tv_tip2.setText("");
					} else {
						tv_tip2.setText(MainActivity.resources.getString(R.string.tip_illegal_serveraddress));
					}

				}
			}
		});

		et_url.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String url = et_url.getText().toString();
				if (url != null && !"".equals(url)) {
					tv_tip2.setText("");
				} else {
					tv_tip2.setText(MainActivity.resources.getString(R.string.tip_illegal_serveraddress));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// ����ǰ�ļ���

			}

			@Override
			public void afterTextChanged(Editable s) {
				// �����ļ���

			}
		});
		tv_download.setText(Config.DIR_CACHE);
		tv_version.setText(Config.versionName);

		btn_cancleSave.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				et_deviceName.setText(Config.DEVICE_NAME);
				et_url.setText(Config.URL);
				tv_tip1.setText("");
				tv_tip2.setText("");
			}
		});
		btn_saveDeviceInfo.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final String deviceName = et_deviceName.getText().toString().trim();
				String url = et_url.getText().toString();
				if (deviceName != null && !"".equals(deviceName) && url != null && !"".equals(url)) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							String reNameUrl = Config.URL + "android/DMSDevice!regName.action?sn=" + Config.serialNumber
									+ "&deviceName=" + deviceName;

							String result = BaseService.connService(reNameUrl, 1);

							Message msg = new Message();
							msg.what = 2;
							msg.obj = result;
							handler.sendMessage(msg);

						}

					}).start();

					PreferenceManager.getInstance().setDmsUrl(url);

					Toast.makeText(Dms.this, MainActivity.resources.getString(R.string.tip_reserveraddress) + url, Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(Dms.this, MainActivity.resources.getString(R.string.tip_reserveraddressfail), Toast.LENGTH_SHORT).show();
				}

			}
		});

		tv_ip.setText(Config.IP);
		tv_mqtt.setText(MQTTservice.HOST);

		schedule_layout = (LinearLayout) program_layout.findViewById(R.id.schedule_layout);
		rg = (RadioGroup) program_layout.findViewById(R.id.radio_group);
		tv_time = (TextView) main_setting.findViewById(R.id.tv_time);
		tv_time.setText(TimeUtil.getTimeStr());
		radio1 = (RadioButton) program_layout.findViewById(R.id.radio1);
		radio2 = (RadioButton) program_layout.findViewById(R.id.radio2);
		radio1.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					sType = 0;
					schedule_layout.setVisibility(View.GONE);
					selectProgramPublish = getProgramPublish(programPublishList,
							PreferenceManager.getInstance().getImmediatelyTemplatesId());
					;
					if (selectProgramPublish == null) {
						if (immediatelyProgramPublishList == null) {

						} else {
							selectProgramPublish = immediatelyProgramPublishList.get(0);
						}
					}
					if (selectProgramPublish == null) {
						selectScheduleoftimes = null;
						selectTemplates = null;
						adapter1.notifyDataSetChanged();
						adapter2.notifyDataSetChanged();
						adapter3.notifyDataSetChanged();
					} else {
						selectScheduleoftimes = selectProgramPublish.getRows().get(0).getScheduleoftimes();
						selectTemplates = selectScheduleoftimes.get(0).getTemplates();
						adapter1.notifyDataSetChanged();
						adapter2.notifyDataSetChanged();
						adapter3.notifyDataSetChanged();
						selectProgramPublish = null;
					}
				}
			}
		});
		radio2.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					sType = 1;
					schedule_layout.setVisibility(View.VISIBLE);
					selectProgramPublish = getProgramPublish(programPublishList,
							PreferenceManager.getInstance().getTimerTemplatesId());
					if (selectProgramPublish == null) {
						if (timerProgramPublishList == null) {

						} else {
							selectProgramPublish = timerProgramPublishList.get(0);
						}
					}
					if (selectProgramPublish == null) {
						selectScheduleoftimes = null;
						selectTemplates = null;

						adapter1.notifyDataSetChanged();
						adapter2.notifyDataSetChanged();
						adapter3.notifyDataSetChanged();
					} else {
						selectScheduleoftimes = selectProgramPublish.getRows().get(0).getScheduleoftimes();
						selectTemplates = selectScheduleoftimes.get(0).getTemplates();

						adapter1.notifyDataSetChanged();
						adapter2.notifyDataSetChanged();
						adapter3.notifyDataSetChanged();
						selectProgramPublish = null;
					}

				}
			}
		});

		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (isAction) {

					switch (checkedId) {
					case R.id.radio1:
						programPublish = getProgramPublish(programPublishList,
								PreferenceManager.getInstance().getImmediatelyTemplatesId());
						getScheduleoftime();

						break;
					case R.id.radio2:
						programPublish = getProgramPublish(programPublishList,
								PreferenceManager.getInstance().getTimerTemplatesId());
						getScheduleoftime();

						break;
					}
					adapter1.notifyDataSetChanged();
				} else {
					isAction = true;
				}
			}

		});
		btn_deleteAll = (Button) program_layout.findViewById(R.id.btn_deleteAll);
		btn_deleteAll.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {


						new AlertDialog.Builder(Dms.this).setMessage(MainActivity.resources.getString(R.string.msg_issuredeleteall))
								.setPositiveButton(MainActivity.resources.getString(R.string.btn_ok), new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {

										new Thread(new Runnable() {

											@Override
											public void run() {
												handler.sendEmptyMessage(10);
											}

										}).start();

									}
								}).setNegativeButton(MainActivity.resources.getString(R.string.btn_cancel),new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
						

			}
		});
		btn_delete_cache = (Button) program_layout.findViewById(R.id.btn_delete_cache);
		btn_delete_cache.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new Thread(new Runnable() {

					public void run() {
						FileUtil.deleteUnuseDatabase(Dms.this);
					}

				}).start();
			}
		});
		programlist_lv = (MyListView) program_layout.findViewById(R.id.programlist_lv);
		schedule_lv = (ListView) program_layout.findViewById(R.id.schedule_lv);
		program_lv = (ListView) program_layout.findViewById(R.id.program_lv);

		adapter1 = new MyProgramListBaseAdapter(inflater);
		adapter2 = new MyScheduleBaseAdapter(inflater);
		adapter3 = new MyProgramBaseAdapter(inflater);

		programlist_lv.setAdapter(adapter1);
		schedule_lv.setAdapter(adapter2);
		program_lv.setAdapter(adapter3);

		programlist_lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});

		schedule_lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectTemplates = selectScheduleoftimes.get(position).getTemplates();
				adapter3.notifyDataSetChanged();

			}
		});

		rg.setFocusable(false);
int setting_layout_id=MainActivity.resources.getInteger(R.integer.setting_layout_id);
		setting_layout.setId(setting_layout_id);

		setting_layout.setBackgroundColor(Color.BLACK);
		// main_layout.addView(setting_layout);
		initSettingView();
		initSetting();
		initSettingListener();
	}

	private Scheduleoftime getScheduleoftime(List<Scheduleoftime> scheduleoftimes2) {
		Calendar c = Calendar.getInstance();
		int hour = 0;
		if (c.get(Calendar.AM_PM) == Calendar.AM) {
			hour = c.get(Calendar.HOUR);
		} else {
			hour = c.get(Calendar.HOUR) + 12;

		}
		int minute = c.get(Calendar.MINUTE);
		for (Scheduleoftime scheduleoftime1 : scheduleoftimes2) {
			long current_time = hour * 60 + minute;
			long stime = TimeUtil.getTimeFromString(scheduleoftime1.getStime());
			long etime = TimeUtil.getTimeFromString(scheduleoftime1.getEtime());
			if (etime > stime) {
				if (current_time >= stime && current_time < etime) {
					return scheduleoftime1;
				}
			} else if (etime == stime) {
				return scheduleoftime1;
			} else {
				if (current_time >= stime || current_time < etime) {
					return scheduleoftime1;
				}
			}

		}
		return null;
	}

	private UITemplate getUITemplate(List<UITemplate> templates2, long recentlyProgramId2) {
		for (UITemplate uITemplate : templates2) {
			if (uITemplate.getId() == recentlyProgramId2) {
				return uITemplate;
			}
		}
		return null;
	}

	/*
	 * private Scheduleoftime getScheduleoftime(List<Scheduleoftime>
	 * scheduleoftimes, long recentlyCheduleoftimeId2) { for (Scheduleoftime
	 * scheduleoftime : scheduleoftimes) { if (scheduleoftime.getId() ==
	 * recentlyCheduleoftimeId2) { return scheduleoftime; } } return null; }
	 */

	private ProgramPublish getProgramPublish(List<ProgramPublish> programPublishList2, String messageid) {
		if (programPublishList2 == null) {
			return null;
		}
		for (ProgramPublish programPublish2 : programPublishList2) {
			if (programPublish2.getMessageId().equals(messageid)) {

				return programPublish2;
			}
		}
		return null;
	}



	public void play(final int style) {
		videourl = urls.get(index);

		vv.setVideoURI(Uri.parse(videourl));
		vv.start();
		vv.setOnPreparedListener(new OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {
				mp.start();

			}
		});

		vv.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				index++;
				if (index >= urls.size()) {
					index = 0;
				}
				videourl = urls.get(index);
				play(style);
			}
		});

	}

	private void fadeIn(final Picasso picasso, final ImageView view1, final ImageView view2,
			final List<String> imgpaths, final int _index, final int duration, final int width, final int height,
			final Animation animation, final long templateId) {
		Log.i("mytest_hl", "Dms.fadeIn");
		if (template != null) {
			if (templateId != template.getId()) {
				/*
				 * view2.setBackgroundResource(0); if (bitmap1 != null) { bitmap1.recycle();
				 * System.gc(); }
				 */
				System.gc();
				return;
			}
		}
		
		
		Log.i("mytest_hl01", "Dms.fadeIn width="+width+",height="+height);
		
		RequestCreator requestCreator1;
		final RequestCreator requestCreator2;
		String path1 = null;
		String path2;

		if (_index == 0) {
			path2 = imgpaths.get((_index) % imgpaths.size());
			requestCreator2 = picasso.load(new File(path2))
					.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).config(Bitmap.Config.RGB_565).resize(width, height);
			requestCreator2.into(view2, new Callback() {

				@Override
				public void onSuccess() {
					view2.startAnimation(animation);
				}

				public void onError() {

				}
			});
		} else {
			
			path1 = imgpaths.get((_index - 1) % imgpaths.size());
			path2 = imgpaths.get((_index) % imgpaths.size());

			
			
			requestCreator1 = picasso.load(new File(path1))
					.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).config(Bitmap.Config.RGB_565).resize(width, height);
			requestCreator2 = picasso.load(new File(path2))
					.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).config(Bitmap.Config.RGB_565).resize(width, height);
			
			requestCreator1.into(view1, new Callback() {

				@Override
				public void onSuccess() {
					Log.i("mytest_hl", "Dms.requestCreator1.onSuccess");

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					requestCreator2.into(view2, new Callback() {

						@Override
						public void onSuccess() {
							 Log.i("mytest_hl", "Dms.requestCreator2.onSuccess");
							view2.startAnimation(animation);
							
						}

						public void onError() {
							 Log.i("mytest_hl", "Dms.requestCreator2.onError");
						}
					});
				}

				public void onError() {
					 Log.i("mytest_hl", "Dms.requestCreator1.onError");
				}
			});

		}
		Log.i("mytest_hl", "Dms.path1="+path1+",path2="+path2);
		// Log.i("mytest", "path1=" + path1 + ",path2=" + path2);

		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation1) {
			}

			public void onAnimationRepeat(Animation animation1) {
			}

			public void onAnimationEnd(final Animation animation1) {
				timehandler.postDelayed(new Runnable() {

					public void run() {

						int newIndex = _index;
						newIndex++;
						fadeIn(picasso, view1, view2, imgpaths, newIndex, duration, width, height, animation,
								templateId);
					}
				}, duration);
			}
		});

		/*
		 * if (_index == 0 ) {
		 * 
		 * Picasso.with(Dms.this).load(new File(imgpaths.get(_index %
		 * imgpaths.size()))).fit().centerCrop() .memoryPolicy(MemoryPolicy.NO_CACHE,
		 * MemoryPolicy.NO_STORE).config(Bitmap.Config.RGB_565) .into(view2); } else {
		 * Picasso.with(Dms.this).load(new File(imgpaths.get((_index - 1) %
		 * imgpaths.size()))).fit().centerCrop() .memoryPolicy(MemoryPolicy.NO_CACHE,
		 * MemoryPolicy.NO_STORE).config(Bitmap.Config.RGB_565) .into(view1);
		 * Picasso.with(Dms.this).load(new File(imgpaths.get(_index %
		 * imgpaths.size()))).fit().centerCrop() .memoryPolicy(MemoryPolicy.NO_CACHE,
		 * MemoryPolicy.NO_STORE).config(Bitmap.Config.RGB_565) .into(view2); }
		 * 
		 * view2.startAnimation(animation);
		 * 
		 * animation.setAnimationListener(new AnimationListener() { public void
		 * onAnimationStart(Animation animation1) { }
		 * 
		 * public void onAnimationRepeat(Animation animation1) { }
		 * 
		 * public void onAnimationEnd(final Animation animation1) {
		 * 
		 * // if (bitmap1 != null) { bitmap1.recycle(); }
		 * 
		 * timehandler.postDelayed(new Runnable() {
		 * 
		 * public void run() {
		 * 
		 * 
		 * //if (bitmap1 != null) { view1.setBackgroundResource(0); bitmap1.recycle(); }
		 * 
		 * int newIndex = _index; newIndex++; if (newIndex >= imgpaths.size()) {
		 * newIndex = 0; } fadeIn(picasso,view1, view2, imgpaths, newIndex, duration,
		 * width, height, animation, templateId); } }, duration); } });
		 */

	}

	/*
	 * private void initPonit(LinearLayout layout, int count) { for (int i = 0; i <
	 * count; i++) { ImageView localImageView = new ImageView(this);
	 * localImageView.setId(i); ImageView.ScaleType localScaleType =
	 * ImageView.ScaleType.FIT_XY; localImageView.setScaleType(localScaleType);
	 * LinearLayout.LayoutParams localLayoutParams = new
	 * LinearLayout.LayoutParams(24, 24);
	 * localImageView.setLayoutParams(localLayoutParams);
	 * localImageView.setPadding(5, 5, 5, 5);
	 * localImageView.setImageResource(R.drawable.ic_focus);
	 * layout.addView(localImageView); } }
	 */
	public UITemplate getTemplateById(List<UITemplate> templateList, String programId) {
		// currentPosition
		// for (UITemplate template : templateList) {
		for (int i = 0; i < templateList.size(); i++) {
			if ((templateList.get(i).getId() + "").equals(programId)) {
				currentPosition = i;
				return templateList.get(i);
			}
		}
		return null;
	}

	private void realseResource() {

		if (player != null) {
			player.releaseMediaPlayer();
			player = null;
		}
		if (imageview_3dgroupList != null && imageview_3dgroupList.size() > 0) {
			for (Image3DSwitchView imageview_3dgroup : imageview_3dgroupList) {
				imageview_3dgroup.endViewAnimation();
			}
		}
		if (gallery != null) {
			gallery.endViewAimation();
			gallery = null;
		}

	}

	private void registerBroadcastReceiver() {
		
		receiver = new CmdReceiver();
		IntentFilter filter = new IntentFilter(ACTION_MQTT);
		filter.addAction(netWorkChangeAction);
		filter.addAction(Intent.ACTION_TIME_TICK);
		registerReceiver(receiver, filter);
	}

	public class CmdReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.i("onReceive", "Dms.CmdReceiver.action=" + action);
			if (Dms.ACTION_MQTT.equals(action)) {
				// System.out.print("ScreenshotReceiver screen shot");
				Bundle bundle = intent.getExtras();
				int cmd = bundle.getInt("cmd");
				Log.i("onReceive", "Dms.CmdReceiver.cmd=" + cmd);
				messageId = bundle.getString("messageId");
				switch (cmd) {
				case 103:
					handler.sendEmptyMessage(11);
					break;
				case 101:
					handler.sendEmptyMessage(5);
					break;
				case 102:
					handler.sendEmptyMessage(6);
					break;
				case 100:
					time = 0;
					currentPosition++;
					getUITemplate();
					break;
				case Config.MQTT_CMD_SCREENSHOT:
					screenshot();
					break;
				case Config.MQTT_CMD_PUBLISH_IM:
					String content = bundle.getString("content");
					int time = bundle.getInt("time");
					int count = bundle.getInt("count");
					String font = bundle.getString("font");
					String fontsize = bundle.getString("fontsize");
					String position = bundle.getString("position");
					// ��ʾ��Ϣ
					showMessage(message_txt, content, time, count, font, fontsize, position);
					break;
				case Config.MQTT_CMD_STOP_MSG:
					if (message_txt != null) {
						message_txt.clearAnimation();
						message_txt.setText("");
						// layout.removeView(txt);
					}
					break;

				case Config.MQTT_CMD_POWER_TIMING:
					String cycle = bundle.getString("cycle");
					String onTime = bundle.getString("onTime");
					String offTime = bundle.getString("offTime");

					if (cycle.equals("a")) {
						if (onTime != null && onTime.matches(Config.matchesTime)) {
							PreferenceManager.getInstance().setTimingPowerOn(onTime);
						} else {
							PreferenceManager.getInstance().setTimingPowerOn("notSelect");
						}
						if (offTime != null && offTime.matches(Config.matchesTime)) {
							PreferenceManager.getInstance().setTimingPowerOff(offTime);
						} else {
							PreferenceManager.getInstance().setTimingPowerOff("notSelect");
						}
					} else {
						selectDay(cb_dayList, cycle);
						if (onTime != null && onTime.matches(Config.matchesTime)) {
							String[] onTimeArr = onTime.split(":");
							sp_onHourTime.setSelection(Integer.parseInt(onTimeArr[0]) + 1);
							sp_onMinuteTime.setSelection((Integer.parseInt(onTimeArr[1])) + 1);
						}
						if (offTime != null && offTime.matches(Config.matchesTime)) {
							String[] offTimeArr = offTime.split(":");
							sp_offHourTime.setSelection(Integer.parseInt(offTimeArr[0]) + 1);
							sp_offMinuteTime.setSelection((Integer.parseInt(offTimeArr[1])) + 1);
						}
						saveSetting();
					}
					Toast.makeText(Dms.this, MainActivity.resources.getString(R.string.tip_powercommandreceive), Toast.LENGTH_SHORT).show();
					break;

				}
			} else if (Intent.ACTION_TIME_TICK.equals(action)) {

				new Thread(new Runnable() {

					public void run() {
						if (stateTime % 3 == 0) {
							String getStateUrl = Config.URL + "android/DMSDevice!checkOnline.action?deviceid="
									+ Config.serialNumber;
							String isOnline = BaseService.connService(getStateUrl, 0);
						}
						stateTime++;

						timeStr = TimeUtil.getTimeStr();
						handler.sendEmptyMessage(3);

						long currentTime = TimeUtil.getCurrentTime();

						if (scheduleoftimes != null) {
							if (mapList == null) {
							} else {

								long differ = 0;
								Scheduleoftime s = null;
								for (Map map : mapList) {

									long stime = (long) map.get("stime");
									// long etime = (long) map.get("etime");

									if (currentTime < stime) {
										if (differ == 0) {
											differ = stime - currentTime;
											s = (Scheduleoftime) map.get("scheduleoftime");
										} else {
											if (differ > (stime - currentTime)) {
												differ = stime - currentTime;
												s = (Scheduleoftime) map.get("scheduleoftime");
											}
										}

									} else if (currentTime == stime) {

										currentPosition = 0;
										time = 0;
										handler.sendEmptyMessage(4);
										return;
									} else {
										if (differ == 0) {
											differ = stime + 24 * 60 - currentTime;
											s = (Scheduleoftime) map.get("scheduleoftime");
										} else {
											if (differ > (stime + 24 * 60 - currentTime)) {
												differ = stime + 24 * 60 - currentTime;
												s = (Scheduleoftime) map.get("scheduleoftime");
											}
										}

									}

								}
								if (s != null) {
									List<UITemplate> UITemplates = s.getTemplates();
									String UITemplatesStr = "";
									if (UITemplates != null && UITemplates.size() > 0) {
										for (int i = 0; i < UITemplates.size(); i++) {
											UITemplate uit = UITemplates.get(i);
											if (i == 0) {
												UITemplatesStr = uit.getName();
											} else {
												UITemplatesStr = UITemplatesStr + "," + uit.getName();
											}
										}
									}
									nextProgram = MainActivity.resources.getString(R.string.nextprogram)+"(" + s.getStime() + ")" + UITemplatesStr;
								}
								if (currentScheduleoftime != null) {
									long etime = TimeUtil.getTimeFromString(currentScheduleoftime.getEtime());
									if (currentTime == etime) {
										currentScheduleoftime = null;
										handler.sendEmptyMessage(4);

										return;
									}
								}

							}

						}

					}

				}).start();

			} else if (netWorkChangeAction.equals(action)) {
				if (intent.getExtras() != null) {
					NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
					if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
						Log.i("onReceive", "����������");
						handler.sendEmptyMessage(8);
					} else {
						Log.i("onReceive", "�����ѶϿ�");
						handler.sendEmptyMessage(7);
					}
				}

			}

		}
	}

	private TextView message_txt;
	private TextView err_txt;
	private TextView netClosed_txt;

	private void showMessage(TextView text, String content, int time, int count, String font, String fontsize,
			String position) {
		final TextView txt = text;
		// ��TextView����txt��ֵ

		txt.setVisibility(View.VISIBLE);
		// txt.clearAnimation();
		// ����txt��������
		txt.setBackgroundColor(Color.WHITE);
		txt.getBackground().setAlpha(0);

		// ����txt����
		txt.setText(content);

		// ���������С
		if (fontsize == null || fontsize.equals("")) {
			txt.setTextSize(20);
		} else {
			try {
				txt.setTextSize(Integer.parseInt(fontsize));
			} catch (NumberFormatException e) {
				txt.setTextSize(20);
			}
		}

		// ��ȡtxt�ؼ��߶�
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		txt.measure(w, h);
		int height = txt.getMeasuredHeight();
		int width = txt.getMeasuredWidth();

		// ����ƽ�ƶ�������
		txt_translateAnimation = new TranslateAnimation(Config.SCREEN_WIDTH, -width, 0, 0);

		// ���ö�������ʱ��
		if (time == 0) {
			return;
		}
		txt_translateAnimation.setDuration(time * 1000);

		// ���ö����ظ�����
		txt_translateAnimation.setRepeatCount(count - 1);

		// ���ö��������¼�
		txt_translateAnimation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation arg0) {
			}

			public void onAnimationRepeat(Animation arg0) {
			}

			public void onAnimationEnd(Animation arg0) {
				txt.setVisibility(View.GONE);
			}
		});

		// ����������ɫ
		int color = -8586240;
		try {
			color = Color.parseColor("#" + font);
		} catch (Exception e) {
			e.printStackTrace();
		}
		txt.setTextColor(color);

		/*
		 * switch (font) { case "white": txt.setTextColor(Color.WHITE); break; case
		 * "black": txt.setTextColor(Color.BLACK); break; case "red":
		 * txt.setTextColor(Color.RED); break; default: txt.setTextColor(Color.RED); }
		 */

		// �������ֲ�������
		RelativeLayout.LayoutParams params02 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// ����txt���ֲ���
		switch (position) {
		case "top":
			params02.setMargins(0, 0, 0, 0);
			break;
		case "middle":
			params02.setMargins(0, Config.SCREEN_HEIGHT / 2 - height / 2, 0, 0);
			break;
		case "bottom":
			params02.setMargins(0, Config.SCREEN_HEIGHT - height, 0, 0);
			break;
		}
		txt.setLayoutParams(params02);

		// �������txt�ؼ�

		/*
		 * if (isAddview) { Log.i("showMessage", "g"); layout.addView(txt); }
		 */
		/*
		 * if (message_txt.getId() == -1) { message_txt.setId(500); }
		 */
		if (main_layout.findViewById(txt.getId()) == null) {
			main_layout.addView(txt);
		} else {
		}

		// txt��������
		txt.startAnimation(txt_translateAnimation);

	}

	private void screenshot() {
		player.pasue();
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			paths = player.path();
			System.out.println(paths);
			retriever.setDataSource(paths);// ��Դ·��
			String timeString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
			long time = Long.parseLong(timeString) * 1000;
			long currentPostion = time * player.getCurrentPosition() / player.getDuration();// ͨ�������������ȡ�Ļ������ڵ�ʱ��
			System.out.println(currentPostion);
			bitmap = retriever.getFrameAtTime(currentPostion);// ����ǰ����λ��ѡ��֡

		} catch (IllegalArgumentException ex) {
		} catch (RuntimeException ex) {
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException ex) {

			}
		}

		Drawable drawable = new BitmapDrawable(bitmap);
		r_layout.setBackgroundDrawable(drawable);
		sfv.setVisibility(sfv.INVISIBLE);
		Bitmap bmp = ScreenUtil.snapShotWithoutStatusBar(Dms.this);
		sfv.setVisibility(sfv.VISIBLE);
		ScreenUtil.savePic(bmp, Config.DIR_CACHE, Config.serialNumber + ".png");
		player.start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = new Message();
				try {
					String savepath = Config.DIR_CACHE + Config.serialNumber + ".png";
					BaseService.uploadFile(Config.URL + "servlet/UploadifySerlet?operate=3&messageId=" + messageId
							+ "&device_id=" + Config.serialNumber, savepath);

				} catch (Exception ex) {
					message.what = 0;
					System.out.print(ex.toString());
				}
			}

		}).start();
	}

	int setVolumeCount = -1;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(MainActivity.resources.getString(R.string.msg_issureexit)).setCancelable(false)
					.setPositiveButton(MainActivity.resources.getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							MainActivity.isApplicationClose = true;
							// realseResource();
							Intent service = new Intent(getApplicationContext(), MQTTservice.class);
							stopService(service);

							Intent intent_orientation = new Intent(Config.ORIENTATION_ACTION);
							int orientation = PreferenceManager.getInstance().getOrientation();
							intent_orientation.putExtra("orientation", orientation);
							sendBroadcast(intent_orientation);

							Intent intent = new Intent(getApplicationContext(), PowerService.class);
							stopService(intent);

							ExitApplication.getInstance().exit();

						}
					}).setNegativeButton(MainActivity.resources.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			builder.create().show();
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_0) {
			test(0);
		}

		if (keyCode == KeyEvent.KEYCODE_1) {
			test(1);

		}

		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			volume--;
			if (volume < 0) {
				volume = 0;
			}
			// CommonUtil.setVolume(Dms.this, volume);
			et_defaultVolume.setText(volume + "");

		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			volume++;
			if (volume > 99) {
				volume = 100;
			}
			// CommonUtil.setVolume(Dms.this, volume);
			et_defaultVolume.setText(volume + "");

		}

		if (keyCode == KeyEvent.KEYCODE_3) {
			// init();
		}
		if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_5) {

			if (program_dialog == null) {

				program_dialog = new AlertDialog.Builder(Dms.this).setView(main_setting).show();

				program_dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
							if (setVolumeCount == 1) {
								setVolumeCount = -1;

								volume--;
								if (volume < 0) {
									volume = 0;
								}
								/*
								 * try { CommandUtil.toCmd((byte) volume); } catch (Exception e) { }
								 * CommonUtil.setVolume(Dms.this, volume);
								 */
								et_defaultVolume.setText(volume + "");

							} else {
								setVolumeCount = 1;
							}

						}
						if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
							if (setVolumeCount == 1) {
								setVolumeCount = -1;
								volume++;
								if (volume > 99) {
									volume = 100;
								}

								/*
								 * try { CommandUtil.toCmd((byte) volume); } catch (Exception e) { }
								 * CommonUtil.setVolume(Dms.this, volume);
								 */

								et_defaultVolume.setText(volume + "");
							} else {
								setVolumeCount = 1;
							}
						}
						return false;
					}
				});

				android.view.WindowManager.LayoutParams p = program_dialog.getWindow().getAttributes(); // ��ȡ�Ի���ǰ�Ĳ���ֵ
				p.gravity = Gravity.CENTER;
				p.height = (int) (Config.SCREEN_HEIGHT * 4 / 5); // �߶�����Ϊ��Ļ��0.5
				p.width = (int) (Config.SCREEN_WIDTH * 2 / 3); // �������Ϊ��Ļ��0.5
				program_dialog.getWindow().setAttributes(p); // ������Ч

			} else {
				program_dialog.show();
			}
			if (adapter1 != null) {
				adapter1.notifyDataSetChanged();
			}

			if (adapter2 != null) {
				adapter2.notifyDataSetChanged();
			}

			if (adapter3 != null) {
				adapter3.notifyDataSetChanged();
			}
		}

		/*
		 * if (keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_6) {
		 * Log.i("connect", "Dms.onKeyDown keyCode == KeyEvent.KEYCODE_6"); if
		 * (setting_layout.getVisibility() == View.VISIBLE) { Log.i("connect",
		 * "Dms.onKeyDown a"); if (!isAnimat) { Log.i("connect", "Dms.onKeyDown b");
		 * isAnimat = true; setting_layout.startAnimation(setting_layout_close); } }
		 * else { Log.i("connect", "Dms.onKeyDown c"); if (!isAnimat) { Log.i("connect",
		 * "Dms.onKeyDown d"); isAnimat = true;
		 * setting_layout.setVisibility(View.VISIBLE);
		 * setting_layout.startAnimation(setting_layout_open); }
		 * 
		 * } }
		 */

		return super.onKeyDown(keyCode, event);
	}

	private void registerProgressReceiver() {
		progressreceiver = new progressReceiver();
		IntentFilter filter = new IntentFilter(DownloadResource.TAG);
		registerReceiver(progressreceiver, filter);
	}

	public class progressReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			/*
			 * String action = intent.getAction(); if (DownloadResource.TAG.equals(action))
			 * { if (intent.getExtras().getInt("A") == 0) { Intent intent_1 = new
			 * Intent(Dms.this, DownloadResource.class); intent.putExtra("messageid", "");
			 * startActivity(intent_1); } else { try {
			 * DownloadResource.textView_percent1.setText("��Դ������������,Loading..." +
			 * (DownloadResource.items.size() + 1) + "/" + DownloadResource.taskCount);
			 * DownloadResource.textView_percent2.setText("��ǰ���ؽ��ȣ�" +
			 * DownloadResource.progress_ss[0] + "%");
			 * DownloadResource.progressBar.setProgress(intent.getExtras().getInt("A")); }
			 * catch (Exception e) { e.printStackTrace(); } } }
			 */
		}
	}

	class MyProgramListBaseAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		TextView tv_programlist;
		Button btn_isuse;
		Button btn_detail;
		Button btn_delete;

		public MyProgramListBaseAdapter(LayoutInflater mInflater) {
			this.mInflater = mInflater;
		}

		public int getCount() {
			if (sType == 0) {
				schedule_layout.setVisibility(View.GONE);
				if (immediatelyProgramPublishList == null) {
					return 0;
				} else {
					return immediatelyProgramPublishList.size();
				}
			} else {
				schedule_layout.setVisibility(View.VISIBLE);
				if (timerProgramPublishList == null) {
					return 0;
				} else {
					return timerProgramPublishList.size();

				}
			}

		}

		public Object getItem(int position) {
			if (sType == 0) {

				return immediatelyProgramPublishList.get(position);
			} else {
				return timerProgramPublishList.get(position);
			}
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.programlist_list, null);
			tv_programlist = (TextView) convertView.findViewById(R.id.tv_programlist);
			btn_isuse = (Button) convertView.findViewById(R.id.btn_isuse);
			btn_detail = (Button) convertView.findViewById(R.id.btn_detail);
			btn_delete = (Button) convertView.findViewById(R.id.btn_delete);
			ProgramPublish p1 = null;
			if (sType == 0) {

				p1 = immediatelyProgramPublishList.get(position);
				if (immediatelyProgramPublish != null) {
					if (immediatelyProgramPublish.getMessageId().equals(p1.getMessageId())) {
						btn_isuse.setText(MainActivity.resources.getString(R.string.btn_played));
						btn_isuse.setFocusable(false);
						btn_isuse.setEnabled(false);
						btn_delete.setFocusable(false);
						btn_delete.setEnabled(false);

					} else {
						btn_isuse.setText(MainActivity.resources.getString(R.string.btn_play));
						btn_isuse.setFocusable(true);
						btn_isuse.setEnabled(true);
						btn_delete.setFocusable(true);
						btn_delete.setEnabled(true);
					}
				} else {
					btn_isuse.setText(MainActivity.resources.getString(R.string.btn_play));
					btn_isuse.setFocusable(true);
					btn_isuse.setEnabled(true);
					btn_delete.setFocusable(true);
					btn_delete.setEnabled(true);
				}
			} else {
				p1 = timerProgramPublishList.get(position);
				if (timerProgramPublish != null) {
					if (timerProgramPublish.getMessageId().equals(p1.getMessageId())) {
						btn_isuse.setText(MainActivity.resources.getString(R.string.btn_used));
						btn_isuse.setFocusable(false);
						btn_isuse.setEnabled(false);
						btn_delete.setFocusable(false);
						btn_delete.setEnabled(false);

					} else {
						btn_isuse.setText(MainActivity.resources.getString(R.string.btn_use));
						btn_isuse.setFocusable(true);
						btn_isuse.setEnabled(true);
						btn_delete.setFocusable(true);
						btn_delete.setEnabled(true);
					}
				} else {
					btn_isuse.setText(MainActivity.resources.getString(R.string.btn_use));
					btn_isuse.setFocusable(true);
					btn_isuse.setEnabled(true);
					btn_delete.setFocusable(true);
					btn_delete.setEnabled(true);
				}
			}

			final ProgramPublish p = p1;

			tv_programlist.setText(p.getRows().get(0).getName() + "-"
					+ PreferenceManager.getInstance().getPublishTime(p.getMessageId()));

			btn_isuse.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					selectProgramPublish = p;
					selectScheduleoftimes = selectProgramPublish.getRows().get(0).getScheduleoftimes();
					selectTemplates = selectScheduleoftimes.get(0).getTemplates();

					adapter2.notifyDataSetChanged();
					adapter3.notifyDataSetChanged();

					getProgramPublish();

				}
			});
			btn_detail.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					selectProgramPublish = p;
					selectScheduleoftimes = selectProgramPublish.getRows().get(0).getScheduleoftimes();
					selectTemplates = selectScheduleoftimes.get(0).getTemplates();

					adapter2.notifyDataSetChanged();
					adapter3.notifyDataSetChanged();
					selectProgramPublish = null;

				}
			});
			btn_delete.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					// FileUtil.deleteFile(new File(Config.DIR_CACHE + p.getMessageId()));

					FileUtil.deleteFile(new File(Config.RESOURCE_INFO + p.getMessageId()));

					programListList = FileUtil.getProgramListList(Dms.this);

					programPublishList = FileUtil.getProgramListList(Dms.this, programListList);// ��ȡ�ų��б�

					immediatelyProgramPublishList = TemplateHelper.getImmediatelyProgramPublishList(programPublishList);

					timerProgramPublishList = TemplateHelper.getTimerProgramPublishList(programPublishList);

					adapter1.notifyDataSetChanged();

				}
			});
			return convertView;
		}

	}

	class MyScheduleBaseAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		TextView tv_scheduleName;

		public MyScheduleBaseAdapter(LayoutInflater mInflater) {
			this.mInflater = mInflater;
		}

		public int getCount() {
			if (selectScheduleoftimes == null) {

				return 0;
			} else {
				return selectScheduleoftimes.size();

			}
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.schedule_layout, null);
			tv_scheduleName = (TextView) convertView.findViewById(R.id.tv_scheduleName);
			Scheduleoftime scheduleoftime = selectScheduleoftimes.get(position);
			if (scheduleoftime.getStype() == 0) {
				tv_scheduleName.setText(MainActivity.resources.getString(R.string.tv_noschedule));
			} else {
				tv_scheduleName.setText(scheduleoftime.getName() + "   (" + scheduleoftime.getStime() + " - "
						+ scheduleoftime.getEtime() + ")");
			}

			return convertView;
		}

	}

	class MyProgramBaseAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		TextView tv_program_name;

		public MyProgramBaseAdapter(LayoutInflater mInflater) {
			this.mInflater = mInflater;
		}

		@Override
		public int getCount() {
			if (selectTemplates == null) {
				return 0;
			}
			return selectTemplates.size();
		}

		@Override
		public Object getItem(int arg0) {
			return selectTemplates.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			arg1 = mInflater.inflate(R.layout.program_list, null);

			tv_program_name = (TextView) arg1.findViewById(R.id.tv_program_name);
			String programName = selectTemplates.get(arg0).getName();
			tv_program_name.setText(programName);

			return arg1;
		}

	}

	public void test(int power) {
		Intent intent_power = new Intent();
		switch (power) {
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
	}

	public String getSelectString(List<CheckBox> cbList) {
		String ret = "";
		for (int i = 0; i < cbList.size(); i++) {
			if (cbList.get(i).isChecked()) {
				ret = ret + i + ",";
			}
		}
		return ret;
	}

	public void allSelect(List<CheckBox> cbList) {
		for (CheckBox cb : cbList) {
			cb.setChecked(true);
		}

	}

	public void allNotSelect(List<CheckBox> cbList) {
		for (CheckBox cb : cbList) {
			cb.setChecked(false);
		}
	}

	public boolean getIsAllSelect(List<CheckBox> cbList) {
		for (CheckBox cb : cbList) {
			if (!cb.isChecked()) {
				return false;
			}
		}
		return true;
	}

	public void selectDay(List<CheckBox> cbList, String selectStr) {
		String[] dayArr = selectStr.split(",");
		allNotSelect(cbList);
		if (dayArr != null && dayArr.length != 0) {
			for (String day : dayArr) {
				switch (day) {
				case "0":
					cbList.get(0).setChecked(true);
					break;
				case "1":
					cbList.get(1).setChecked(true);
					break;
				case "2":
					cbList.get(2).setChecked(true);
					break;
				case "3":
					cbList.get(3).setChecked(true);
					break;
				case "4":
					cbList.get(4).setChecked(true);
					break;
				case "5":
					cbList.get(5).setChecked(true);
					break;
				case "6":
					cbList.get(6).setChecked(true);
					break;
				default:
					break;
				}
			}
		} else {
			for (CheckBox cb : cbList) {
				cb.setChecked(false);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			old_x = event.getX();
			old_y = event.getY();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if ((old_x - event.getX()) < 50&&(old_x - event.getX()) >-50&&(old_y- event.getY())>-50&&(old_y- event.getY())<50) {
			
				if (adapter1 != null) {
					adapter1.notifyDataSetChanged();
				}

				if (adapter2 != null) {
					adapter2.notifyDataSetChanged();
				}

				if (adapter3 != null) {
					adapter3.notifyDataSetChanged();
				}
				if (program_dialog == null) {
					program_dialog = new AlertDialog.Builder(Dms.this).setView(main_setting).show();
					android.view.WindowManager.LayoutParams p = program_dialog.getWindow().getAttributes(); // ��ȡ�Ի���ǰ�Ĳ���ֵ
					p.height = (int) (Config.SCREEN_HEIGHT); // �߶�����Ϊ��Ļ��0.5
					p.width = (int) (Config.SCREEN_WIDTH * 2 / 3); // �������Ϊ��Ļ��0.5
					program_dialog.getWindow().setAttributes(p); // ������Ч

				} else {
					program_dialog.show();
				}
			}
		}

		return super.onTouchEvent(event);
	}

	public void initSettingView() {
		cb_isBootAutoStart = (CheckBox) setting_layout.findViewById(R.id.cb_isBootAutoStart);

		btn_sub = (Button) setting_layout.findViewById(R.id.btn_sub);
		et_defaultVolume = (EditText) setting_layout.findViewById(R.id.et_defaultVolume);
		btn_add = (Button) setting_layout.findViewById(R.id.btn_add);

		cb_isAllDate = (CheckBox) setting_layout.findViewById(R.id.cb_isAllDate);
		cb_sunday = (CheckBox) setting_layout.findViewById(R.id.cb_sunday);
		cb_monday = (CheckBox) setting_layout.findViewById(R.id.cb_monday);
		cb_tuesday = (CheckBox) setting_layout.findViewById(R.id.cb_tuesday);
		cb_wednesday = (CheckBox) setting_layout.findViewById(R.id.cb_wednesday);
		cb_thurday = (CheckBox) setting_layout.findViewById(R.id.cb_thurday);
		cb_friday = (CheckBox) setting_layout.findViewById(R.id.cb_friday);
		cb_saturday = (CheckBox) setting_layout.findViewById(R.id.cb_saturday);

		sp_onHourTime = (Spinner) setting_layout.findViewById(R.id.sp_onHourTime);
		sp_onMinuteTime = (Spinner) setting_layout.findViewById(R.id.sp_onMinuteTime);
		sp_offHourTime = (Spinner) setting_layout.findViewById(R.id.sp_offHourTime);
		sp_offMinuteTime = (Spinner) setting_layout.findViewById(R.id.sp_offMinuteTime);

		btn_isSaveSetting = (Button) setting_layout.findViewById(R.id.btn_isSaveSetting);
		btn_isRegister = (Button) setting_layout.findViewById(R.id.btn_isRegister);

		// main_layout.addView(setting_layout);
		cb_dayList = new ArrayList<CheckBox>();

		cb_dayList.add(cb_sunday);
		cb_dayList.add(cb_monday);
		cb_dayList.add(cb_tuesday);
		cb_dayList.add(cb_wednesday);
		cb_dayList.add(cb_thurday);
		cb_dayList.add(cb_friday);
		cb_dayList.add(cb_saturday);

	}

	private void initSetting() {
		volume = CommonUtil.getVolume(Dms.this);
		et_defaultVolume.setText(volume + "");
		btn_sub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				volume--;
				if (volume < 0) {
					volume = 0;
				}
				try {
					CommandUtil.toCmd((byte) volume);
				} catch (Exception e) {
				}
				CommonUtil.setVolume(Dms.this, volume);
				et_defaultVolume.setText(volume + "");

			}
		});
		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				volume++;
				if (volume > 99) {
					volume = 100;
				}
				try {
					CommandUtil.toCmd((byte) volume);
				} catch (Exception e) {
				}
				CommonUtil.setVolume(Dms.this, volume);
				et_defaultVolume.setText(volume + "");

			}
		});
		if (PreferenceManager.getInstance().getIsBootAutoStart() == Config.BOOT_AUTO_START) {
			cb_isBootAutoStart.setChecked(true);
		} else {
			cb_isBootAutoStart.setChecked(false);
		}

		if (PreferenceManager.getInstance().getIsAllDayOn()) {
			cb_isAllDate.setChecked(true);
			allSelect(cb_dayList);
		} else {
			cb_isAllDate.setChecked(false);
			selectDay(cb_dayList, PreferenceManager.getInstance().getOnDay());
		}

		String onTime = PreferenceManager.getInstance().getOnTime();
		String offTime = PreferenceManager.getInstance().getOffTime();

		if (onTime != null && !onTime.equals("") && !onTime.equals("null")) {
			boolean isOnTimeMatches = onTime.matches(Config.matchesTime);
			if (isOnTimeMatches) {
				String[] onTimeArr = onTime.split(":");
				sp_onHourTime.setSelection(Integer.parseInt(onTimeArr[0]) + 1);
				sp_onMinuteTime.setSelection((Integer.parseInt(onTimeArr[1])) + 1);
			} else {
				sp_onHourTime.setSelection(0);
				sp_onMinuteTime.setSelection(0);
			}

		} else {
			sp_onHourTime.setSelection(0);
			sp_onMinuteTime.setSelection(0);
		}
		if (offTime != null && !offTime.equals("") && !offTime.equals("null")) {
			boolean isOffTimeMatches = offTime.matches(Config.matchesTime);
			if (isOffTimeMatches) {
				String[] offTimeArr = offTime.split(":");
				sp_offHourTime.setSelection(Integer.parseInt(offTimeArr[0]) + 1);
				sp_offMinuteTime.setSelection((Integer.parseInt(offTimeArr[1])) + 1);

			} else {
				sp_offHourTime.setSelection(0);
				sp_offMinuteTime.setSelection(0);
			}
		} else {
			sp_offHourTime.setSelection(0);
			sp_offMinuteTime.setSelection(0);
		}
	}

	private void initSettingListener() {
		cb_isAllDate.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (cmdSource == 0) {
					if (isChecked) {
						allSelect(cb_dayList);
					} else {
						allNotSelect(cb_dayList);
					}
				} else {
					cmdSource = 0;
				}
			}
		});

		for (CheckBox cb : cb_dayList) {
			/*
			 * if(!cb.isChecked()) { Config.isAllDayOn = false;
			 * Config.onDay=getSelectString(cb_dayList); }
			 */
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

					if (isChecked) {
						if (getIsAllSelect(cb_dayList)) {
							cb_isAllDate.setChecked(true);
						}
					} else {
						if (cb_isAllDate.isChecked()) {
							cmdSource = 1;
							cb_isAllDate.setChecked(false);
						}
					}
				}
			});
		}

		btn_isSaveSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveSetting();
			}
		});
		btn_isRegister.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(Dms.this).setTitle(MainActivity.resources.getString(R.string.msg_iscancleregister))
						.setPositiveButton(MainActivity.resources.getString(R.string.btn_ok), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								new Thread(new Runnable() {

									@Override
									public void run() {
										String unregistUrl = Config.URL + "android/DMSDevice!unregist.action?sn="
												+ Config.serialNumber;
										unRegisterResult = BaseService.connService(unregistUrl, 0);

										if (unRegisterResult == null || "".equals(unRegisterResult)) {
											Toast.makeText(Dms.this, MainActivity.resources.getString(R.string.tip_cancelregisterfail), Toast.LENGTH_LONG).show();
										} else {
											handler.sendEmptyMessage(1);

										}
									}

								}).start();

							}
						}).setNegativeButton(MainActivity.resources.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						}).show();
			}
		});
	}

	private void saveSetting() {

		String defaultVolumeStr = et_defaultVolume.getText().toString();
		if (defaultVolumeStr != null && !"".equals(defaultVolumeStr)) {

			byte defaultVolume = Byte.parseByte(defaultVolumeStr);
			volume = defaultVolume;
			try {
				CommandUtil.toCmd((byte) defaultVolume);
			} catch (Exception e) {
			}
			CommonUtil.setVolume(Dms.this, defaultVolume);
			PreferenceManager.getInstance().setDefaultVolume(defaultVolume);

		}

		if (sp_onHourTime.getSelectedItemPosition() == 0 || sp_onMinuteTime.getSelectedItemPosition() == 0) {
			if (sp_onHourTime.getSelectedItemPosition() != sp_onMinuteTime.getSelectedItemPosition()) {
				Toast.makeText(Dms.this, MainActivity.resources.getString(R.string.tip_illegal_powerontime), Toast.LENGTH_SHORT).show();
				return;
			}
		}

		if (sp_offHourTime.getSelectedItemPosition() == 0 || sp_offMinuteTime.getSelectedItemPosition() == 0) {
			if (sp_offHourTime.getSelectedItemPosition() != sp_offMinuteTime.getSelectedItemPosition()) {
				Toast.makeText(Dms.this, MainActivity.resources.getString(R.string.tip_illegal_powerofftime), Toast.LENGTH_SHORT).show();
				return;
			}
		}

		if (cb_isBootAutoStart.isChecked()) {
			PreferenceManager.getInstance().setIsBootAutoStart(Config.BOOT_AUTO_START);
		} else {
			PreferenceManager.getInstance().setIsBootAutoStart(Config.BOOT_NOTAUTO_START);
		}
		if (cb_isAllDate.isChecked()) {
			PreferenceManager.getInstance().setIsAllDayOn(true);
			PreferenceManager.getInstance().setOnDay("0,1,2,3,4,5,6");
		} else {
			PreferenceManager.getInstance().setIsAllDayOn(false);
			PreferenceManager.getInstance().setOnDay(getSelectString(cb_dayList));
		}

		PreferenceManager.getInstance().setOnTime(Config.hourArr[sp_onHourTime.getSelectedItemPosition()] + ":"
				+ Config.MinuteArr[sp_onMinuteTime.getSelectedItemPosition()]);

		PreferenceManager.getInstance().setOffTime(Config.hourArr[sp_offHourTime.getSelectedItemPosition()] + ":"
				+ Config.MinuteArr[sp_offMinuteTime.getSelectedItemPosition()]);

		Toast.makeText(Dms.this, MainActivity.resources.getString(R.string.tip_savesuccess), Toast.LENGTH_SHORT).show();
	}

	private String[] getSelectProgramArr(String selectProgramListStr) {
		if (selectProgramListStr != null && !selectProgramListStr.equals("")) {
			return selectProgramListStr.split(",");
		}

		return null;
	}

	private UITemplate getUITemplate(List<UITemplate> UITemplateList, String[] programIdStrArr) {
		// currentPosition
		// for (UITemplate template : UITemplateList) {
		if (programIdStrArr != null) {

			for (int i = currentPosition; i < currentPosition + UITemplateList.size(); i++) {
				for (String programIdStr : programIdStrArr) {
					if (programIdStr.equals(UITemplateList.get(i % UITemplateList.size()).getId() + "")) {
						currentPosition = i;
						return UITemplateList.get(i % UITemplateList.size());
					}
				}

			}
		}

		return null;
	}

	private boolean getIsContainAt(String arrStr, String str) {
		String[] arr = arrStr.split(",");
		if (arr != null && arr.length != 0) {

			for (String s : arr) {
				if (str.equals(s)) {
					return true;
				}
			}
		}
		return false;
	}

	private String removeAtSelectStr(String arrStr, String str) {

		String[] arr = arrStr.split(",");
		List<String> selectProgramList = new ArrayList<String>();
		String newArrStr = "";
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				if (!arr[i].equals(str) && !arr[i].equals("")) {
					selectProgramList.add(arr[i]);
				}
			}

			if (selectProgramList.size() > 0) {
				for (int j = 0; j < selectProgramList.size(); j++) {
					if (j == 0) {
						newArrStr = selectProgramList.get(j);
					} else {
						newArrStr = newArrStr + "," + selectProgramList.get(j);
					}
				}
			}
		}
		return newArrStr;
	}

	private String getProgramListStr(List<String> programList) {

		String ret = "";
		if (programList != null && programList.size() != 0) {

			for (int i = 0; i < programList.size(); i++) {
				if (i == 0) {
					ret += programList.get(i);
				} else {
					ret += "," + programList.get(i);
				}
			}
		} else {
			ret = null;
		}

		return ret;
	}

	public Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;
		Bitmap bm = null;
		try {
			bm = BitmapFactory.decodeFile(path, options);
		} catch (Exception e) {
			Toast.makeText(Dms.this, MainActivity.resources.getString(R.string.tip_imgloadfail), Toast.LENGTH_SHORT).show();
		}
		return bm;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// final int halfHeight = height / 2;
			// final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((height / inSampleSize) > reqHeight && (width / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	@Override
	protected void onStart() {
		Log.i("cycle", "Dms.onStart");
		super.onStart();
		this.isActive = true;
	}

	@Override
	protected void onStop() {
		Log.i("cycle", "Dms.onStop");
		super.onStop();
	}

	@Override
	protected void onRestart() {
		Log.i("cycle", "Dms.onRestart");
		super.onRestart();
	}

	@Override
	protected void onPause() {
		Log.i("cycle", "Dms.onPause");
		super.onPause();
		/*
		 * if (player != null) { player.stop(); }
		 */
	}

	@Override
	protected void onDestroy() {
		Log.i("cycle", "Dms.onDestroy");

		this.isActive = false;
		realseResource();
		if (program_dialog != null) {
			program_dialog.dismiss();
		}
		if (timer != null) {
			timer.cancel();
		}
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
		if (progressreceiver != null) {
			unregisterReceiver(progressreceiver);
			progressreceiver = null;
		}
		super.onDestroy();
	}
}
