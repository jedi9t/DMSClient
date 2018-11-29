package widget;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class MyGallery extends Gallery{

	/*@Override
	public void setAnimationDuration(int animationDurationMillis) {
		// TODO Auto-generated method stub
		super.setAnimationDuration(animationDurationMillis);
	}*/
	
	private static final int timerAnimation = 1;
	private Timer timer = new Timer();
	private final TimerTask task = new TimerTask()
	{
		public void run()
		{
			mHandler.sendEmptyMessage(1);
		}
	};

	private final Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
		};
	};
	
	public MyGallery(Context paramContext)
	{
		super(paramContext);
	
	}


	public void startViewAimation(int duration)
	{
		timer.schedule(task, duration*1000, duration*1000);	
	}

	public void endViewAimation()
	{
		if(timer!=null){
			timer.cancel();
			timer=null;
		}		
	}


}
