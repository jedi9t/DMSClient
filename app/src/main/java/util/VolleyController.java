package util;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

/**
 * Volley������������(���Volley�����ԣ���volley��Application�󶨣�ͨ��Application����Volley����������)
 * 
 * @author cairuizhi
 * 
 */
public class VolleyController extends Application {
	/*
	 * ��������TAG��ǩ
	 */
	public static final String TAG = "VolleyPatterns";

	/*
	 * �����������
	 */
	private RequestQueue requestQueue;

	/*
	 * ��������ģʽ����
	 */
	private static VolleyController volleyController;

	private Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		volleyController = this;
	}

	/**
	 * ���VolleyConroller�ĵ�������
	 */
	public static synchronized VolleyController getInstance() {
		if (volleyController != null) {

			return volleyController;
		} else {
			return volleyController = new VolleyController();
		}
	}

	/**
	 * �����Ϣ���ж���
	 * 
	 * @return
	 */
	public RequestQueue getRequestQueue(Context context) {
		this.context = context;
		if (requestQueue == null) {
			synchronized (VolleyController.class) {
				if (requestQueue == null) {
					// LogUtils.i(TAG, "------getApplicationContext------" +
					// getApplicationContext());
					requestQueue = Volley.newRequestQueue(context);
				}
			}

		}
		return requestQueue;

	}

	/**
	 * �����������Ϣ������,tag��ÿ����������Ϣ���еı�ǩ��������侭�п���
	 * 
	 * @param request
	 * @param tag
	 */
	public <T> void addToRequestQuere(Request<T> request, String tag) {
		request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		VolleyLog.d("Adding request to queue: %s", request.getUrl());
		getRequestQueue(context).add(request);
	}

	/**
	 * �����������Ϣ������,tag��ʹ�õ���Ĭ�ϱ�ǩ
	 * 
	 * @param request
	 */
	public <T> void addToRequestQuere(Request<T> request) {
		request.setTag(TAG);
		getRequestQueue(context).add(request);

	}

	/**
	 * ͨ��tagȡ�������������
	 * 
	 * @param tag
	 */

	public void canclePendingRequest(Object tag) {
		if (requestQueue != null) {
			requestQueue.cancelAll(tag);
		}
	}
}