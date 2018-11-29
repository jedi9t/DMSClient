package tvdms;

import android.app.Application;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

/**
 * @author yh
 * @date 2018-8-21 ����3:12:42
 *
 */
public class BaseApplication extends Application {
	 
    private static BaseApplication instance;
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        instance = this;
        initTbs();
    }
 
    private void initTbs() {
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
    }
 
    public static BaseApplication getInstance() {
        return instance;
    }
}
