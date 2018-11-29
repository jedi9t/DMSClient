package tvdms;
import java.util.LinkedList;
import java.util.List;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ExitApplication extends Application {
	
	private List<Activity> activityList = new LinkedList();
	private static ExitApplication instance;

	private ExitApplication() {
		
	}

	// ����ģʽ�л�ȡΨһ��ExitApplicationʵ��
	public static ExitApplication getInstance() {
		if (null == instance) {
			instance = new ExitApplication();
		}
		return instance;
	}

	// ���Activity��������
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish
	@Override
    public void onCreate() {
        super.onCreate();
 
        instance = this;
   //     initTbs();
    }
	public void exit() {
		/*for(int i=activityList.size()-1;i>-1;i--) {
			Log.i("cycle","ExitApplication.getInstance().exit()"+activityList.get(i).toString());	
			activityList.get(i).finish();
		}*/
		
		for (Activity activity : activityList) {
			Log.i("cycle","ExitApplication.getInstance().exit()"+activity.toString());	
			activity.finish();
		}
		//System.exit(0);

	}

	 /* private  void initTbs() {
	        //�Ѽ�����tbs�ں���Ϣ���ϱ������������������ؽ������ʹ���ĸ��ںˡ�
	        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
	 
	            @Override
	            public void onViewInitFinished(boolean arg0) {
	              //  LogUtil.i("onViewInitFinished is " + arg0);
	            }
	 
	            @Override
	            public void onCoreInitFinished() {
	            }
	        };
	 
	        QbSdk.setTbsListener(new TbsListener() {
	            @Override
	            public void onDownloadFinish(int i) {
	              //  LogUtil.i("onDownloadFinish");
	            }
	 
	            @Override
	            public void onInstallFinish(int i) {
	              //  LogUtil.i("onInstallFinish");
	            }
	 
	            @Override
	            public void onDownloadProgress(int i) {
	               // LogUtil.i("onDownloadProgress:" + i);
	            }
	        });
	 
	        QbSdk.initX5Environment(getApplicationContext(), cb);
	    }*/
	 
	
}
