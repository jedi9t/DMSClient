package service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.util.Calendar;

import config.Config;
import helper.PreferenceManager;
import util.PowerUtil;

public class PowerService extends Service {
	PowerManageReceiver powerManageReceiver;
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("cycle", "PowerService.onCreate");
		powerManageReceiver=new PowerManageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction("victgroup.action.POWER_SETTING");
		filter.addAction(Config.ORIENTATION_ACTION);
		registerReceiver(powerManageReceiver, filter);

	}

	class PowerManageReceiver extends BroadcastReceiver {
		
		PowerUtil powerUtil;

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			
			final Intent intent=arg1;
			final Context context=arg0;
			
			new Thread(new Runnable() {

				public void run() {
					if(powerUtil==null) {
						powerUtil = new PowerUtil(context);
					}
					String action = intent.getAction();
					Log.i("onReceive", "PowerManageReceiver.intent.action=" + action);
					if (action.equals("victgroup.action.POWER_SETTING")) {
						int power = intent.getIntExtra("power", -1);
						Log.i("onReceive", "power=" + power);
						switch (power) {
						case 0:
							powerUtil.powerOff();
							break;
						case 1:
							powerUtil.powerOn();
							break;
						default:
							break;
						}
					} else if (action.equals(Intent.ACTION_TIME_TICK)){
						PreferenceManager.init(context);

						Calendar c = Calendar.getInstance();
						int day = c.get(Calendar.DAY_OF_WEEK);
						int hour = 0;
						if (c.get(Calendar.AM_PM) == Calendar.AM) {
							hour = c.get(Calendar.HOUR);
						} else {
							hour = c.get(Calendar.HOUR) + 12;

						}
						int minute = c.get(Calendar.MINUTE);
						
						
						
						
						String powerOnCmd = PreferenceManager.getInstance().getTimingPowerOn();
						String powerOffCmd = PreferenceManager.getInstance().getTimingPowerOff();
						Log.i("onReceive", "powerOnCmd=" + powerOnCmd+",powerOffCmd="+powerOffCmd);
						if (getIsSameTime(powerOnCmd, hour, minute)) {
							powerUtil.powerOn();
							PreferenceManager.getInstance().setTimingPowerOn("notSelect");
							return;
						}
						if (getIsSameTime(powerOffCmd, hour, minute)) {
							powerUtil.powerOff();
							PreferenceManager.getInstance().setTimingPowerOff("notSelect");
							return;
						}

						
						String onDay = PreferenceManager.getInstance().getOnDay();

							if (onDay != null && !onDay.equals("") && !onDay.equals("null")) {

								String onTime = PreferenceManager.getInstance().getOnTime();
								String offTime = PreferenceManager.getInstance().getOffTime();
								boolean isOnTimeNotEmpty = onTime != null && !onTime.equals("") && !onTime.equals("null");
								boolean isOffTimeNotEmpty = offTime != null && !offTime.equals("") && !offTime.equals("null");
								if (isOnTimeNotEmpty || isOffTimeNotEmpty) {

									// Log.i("onReceive", "month=" + month + ",day=" + day + ",time=" + time);
									Log.i("onReceive","onDay=" + onDay + ",����ʱ�䣺" + onTime + ",�ػ�ʱ�䣺"
											+ offTime + ",��ǰʱ�䣺" + hour + ":" + minute);
										if (getIsContain(onDay.split(","), day - 1 + "")) {
											if (getIsSameTime(onTime, hour, minute)) {
												powerUtil.powerOn();
												Log.i("onReceive", "���ڿ���������ʱ�䣺" + onTime);
												return;
											}
											if (getIsSameTime(offTime, hour, minute)) {
												powerUtil.powerOff();
												Log.i("onReceive", "���ڹػ����ػ�ʱ�䣺" + offTime);
												return;
											}

										}

								}

							}

					}else if(action.equals(Config.ORIENTATION_ACTION)){
						
						int orientation = intent.getIntExtra("orientation",0);
						handler.sendEmptyMessage(orientation);
					}
					
				}
				
			}).start();
			

		}

	}

	public boolean getIsContain(String[] strArr, String str) {
		for (String s : strArr) {
			if (Integer.parseInt(s) == Integer.parseInt(str)) {
				return true;
			}
		}
		return false;
	}

	public boolean getIsSameTime(String timing_time, int currentHour, int currentMinute) {
		if(timing_time!=null&&!timing_time.equals("notSelect")){
			
			try {
				String[] timeArr = timing_time.split(":");
				if (Integer.parseInt(timeArr[0]) == currentHour) {
					if (Integer.parseInt(timeArr[1]) == currentMinute) {
						return true;
					}
				}
			} catch (Exception e) {
				
			}
		}
		return false;
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			toChangeOrientation(msg.what);
			
		}
	};
	
	
public void toChangeOrientation(int orientation) {
	Log.i("mytest", "PowerManageReceiver.toChangeOrientation orientation="+orientation);	
	
		WindowManager mWindow = (WindowManager)getSystemService(WINDOW_SERVICE);
		
		CustomLayout mLayout=new CustomLayout();
		
		mLayout.screenOrientation = orientation;
		
		mWindow.addView(new View(PowerService.this), mLayout);
	//	Config.SCREEN_HEIGHT = ScreenUtil.getScreenHeight(getApplicationContext());
	//	Config.SCREEN_WIDTH = ScreenUtil.getScreenWidth(getApplicationContext());
		Log.i("mytest", "PowerManageReceiver   Config.SCREEN_WIDTH="+Config.SCREEN_WIDTH+",Config.SCREEN_HEIGHT="+Config.SCREEN_HEIGHT);	
	}
	
	 static class CustomLayout extends WindowManager.LayoutParams {

		public CustomLayout() {
			super(0, 0, TYPE_SYSTEM_OVERLAY, FLAG_FULLSCREEN | FLAG_NOT_FOCUSABLE, PixelFormat.RGBX_8888);
			// size.(�������Ƶ������Ķ����������Ǹı����С��)
			this.gravity = Gravity.TOP;
		}
	}
	 @Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("cycle", "PowerService.onDestroy");
		unregisterReceiver(powerManageReceiver);
	}
}
