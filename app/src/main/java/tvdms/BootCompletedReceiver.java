package tvdms;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import config.Config;
import helper.PreferenceManager;

public class BootCompletedReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent arg1) {
		Log.i("mytest", "BootCompletedReceiver.onReceive   arg1.getAction()="+arg1.getAction());
		PreferenceManager.init(context);
		if (arg1.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			if(PreferenceManager.getInstance().getIsBootAutoStart()==Config.BOOT_AUTO_START) {
				Log.i("mytest", "BOOT_AUTO_START");
				Intent intent=new Intent(context,MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		}
		
	}

}
