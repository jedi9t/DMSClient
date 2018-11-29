package config;


import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import com.victgroup.signup.dmsclient.R;

import tvdms.MainActivity;

public class Config {

	public static String DIR_NAME;
	public static String installAPKPath;
	
	public static String versionName;
	
	public static String topicName="MQTT_COMMAND";

	public static String RESOURCE_INFO ="/data/data/com.tv.subw_tvdms/dms_resource/";
	// ��������
	public static String DIR_CACHE = "/data/data/com.tv.subw_tvdms/dms_download/";
	
	public static String USER_GROUP_NAME="";

//	public final static int USER_ID=0;
	
	
	public static String DEVICE_MODEL=android.os.Build.MODEL.replaceAll(" ", "").trim();
	
	public static String serialNumber=android.os.Build.SERIAL.replaceAll("[^0-9a-zA-Z.��,��������+_-]","");
	//	public static String serialNumber="test";
	
	
	
	public static String DIR_SCREENSHOT ="/data/data/com.tv.subw_tvdms/screenshot/";

	public static void init(Context context) {
		DIR_NAME = context.getApplicationContext().getFilesDir()
				.getAbsolutePath()
				+ "/";
	}

	public static boolean WIFI_CONNECT;
	public static final int REGISTER_NO = 0;// 电视机未注册
	public static final int REGISTER_YES = 1;// 电视机已注册
	public static String CONTROL_SCREENSHOT = "1";// 截图命令
	public static final int BOOT_AUTO_START = 0;// 开机自启
	public static final int BOOT_NOTAUTO_START = 1;// 开机不自启
	//		public static St-ring URL ="http://www.shopivot.com/tvdms2/";
	//	public static String URL ="http://192.168.1.196:8080/tvdms2/";
				public static String URL ="http://signup.victgroup.com/tvdms2/";

//	public static String MACHINE_ID = "111";
//	public static String Device_ID;
	public static String DEVICE_NAME;
	
	public static String ID;
//	public static int program_id = -1;
	
	/*
	 * android widget ����
	 */
	public final static int WIDGET_MEDIAPLAYER = 1;// 视频组件
	public final static int WIDGET_IMAGEVIEWS = 2;// 图片组件
	public final static int WIDGET_TEXT = 3;// 文本组件
	public final static int WIDGET_STREAM = 4;
	public final static int WIDGET_WEB = 5;// WEB组件
	public final static int WIDGET_WEATHER = 6;// 天气组件
	public final static int WIDGET_PPT = 7;// PPT组件
	public final static int WIDGET_CLOCK = 8;// 时钟组件

	/*
	 * mqtt ָ������
	 */
	
	public final static int MQTT_CMD_RESOURCE_UPDATE_AYN = 0;
	public final static int MQTT_CMD_RESOURCE_UPDATE = 1;// 资源更新
	public final static int MQTT_CMD_SCREENSHOT = 2;// 截图
	public final static int MQTT_CMD_INSTALL_APK = 3;// 安装新应用
	public final static int MQTT_CMD_PUBLISH_IM = 4;// 发布即时消息
	public final static int MQTT_CMD_STOP_MSG = 5;//终止即时消息

	public final static int MQTT_CMD_REBOOT = 6;// 远程重启
	public final static int MQTT_CMD_POWER_PLAN = 7;// 远程开机
	public final static int MQTT_CMD_UPGRADE = 8;// 升级客户端
	public final static int MQTT_CMD_GET_CLENT_STATE = 9;// 获取客户端播放节目ID
	public final static int MQTT_CMD_GET_CLENT_PROGRAM = 10;// 获取客户端节目列表
	public final static int MQTT_CMD_LOOK_DOWNLOAD_STATUS = 11;// 查看下载进度
	public final static int MQTT_CMD_DELETE_PROGRAM = 12;// 删除节目
	public final static int MQTT_CMD_POWER_TIMING = 13;// 定时开关机
	
	
	
	
	
	
	
	
	public static final String SHARED_PREFERENCE_NAME = "tv_preferences";
	public static final String ORIENTATION_ACTION = "victgroup.action.orientation";

	public static String IMEI = "IMEI";
	public static String Dev = "DEVICE_ID";
	public static String WLAN_MAC = "WLAN_MAC";
	public static String BUILD_DEV_SHORT = "BUILD_DEV_SHORT";
	public static String IP = "IP";
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;

	public static String DEVICE_ID_PREFIX = "RM";

	public static Activity SCREENSHOT_ACTIVITY;

	public final static int STYLE_ALPHA = 0;
	public final static int STYLE_GALLERY = 1;
	public final static int STYLE_SCALE_BIGGER = 2;
	public final static int STYLE_TOP = 3;
	public final static int STYLE_RIGHT = 4;
	public final static int STYLE_RIGHT_BOTTOM = 5;
	public final static int STYLE_ROTATE = 6;
	public final static int STYLE_SCALE_SMALLER = 7;
	public final static int STYLE_IMAGES_3D = 8;

	public static int STYLE_VIDEO_LOCAL = 0;
	public static int STYLE_VIDEO_NETWORK = 1;
	public static int STYLE_VIDEO_THIRDPART = 2;

	

//	public static String TEMPLATE_NAME = "";

	public final static int STATE_ONLINE = 1;
	public final static int STATE_IDLE = 2;
	public final static int STATE_OFFLINE = 3;
	public final static int STATE_SHUTDOWN = 4;

	public static long programId  =-1;
	
	
	
//	public static String onTime="";
//	public static String offTime="";
	public static String[] hourArr= {MainActivity.resources.getString(R.string.spinner_no_select),"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
	public static String[] MinuteArr= {MainActivity.resources.getString(R.string.spinner_no_select),"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"};
	public static String matchesTime="[0-2][0-9]:[0-5][0-9]";
	
	
	
}
