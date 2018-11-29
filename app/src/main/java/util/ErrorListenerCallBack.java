package util;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

/**
 * ���������쳣�ص�
 * 
 * @author cairuizhi
 * 
 */
public class ErrorListenerCallBack implements ErrorListener {
	private static final String TAG = "�����쳣�ع�����";

	@Override
	public void onErrorResponse(VolleyError error) {
		// LogUtils.e(TAG, error.getMessage());
		error.printStackTrace();
		// ToastManagerUtils.show(error.getMessage(),
		// AppManager.getAppManager().currentActivity());
	}

}
