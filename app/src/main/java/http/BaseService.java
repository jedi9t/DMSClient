package http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import config.Config;
import util.ErrorListenerCallBack;
import util.FileUtil;
import util.VolleyController;


public class BaseService {
	static int connServiceCount;
	static String result = "";

	public static String connService(String urlStr, int methodType) {
		Log.i("FFFFFFFF","urlStr="+urlStr);
		String str = "";

				try {
				//	String newUrlStr=URLEncoder.encode(urlStr, ""); //��������
					URL url = new URL(urlStr);
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					InputStream is = con.getInputStream();
					Reader r=new InputStreamReader(is) ;
					 BufferedReader br=new BufferedReader(r);
					 String len="";
					 
					 while((len=br.readLine())!=null) {
						 str+=len;
					 }
					 br.close();
				} catch (Exception e) {
					System.out.println(e.toString());
					e.printStackTrace();
				}
				Log.i("FFFFFFFF","str="+str);
		return str;
	}
	// 在下载完成后上传
		public static void sendfinishTask(Context context,final String status, final String errorinfo,final String messageId,final String resId) {
			// Log.i("mytest","status="+status+)
			try {
				StringRequest stringRequest = new StringRequest(Request.Method.POST,
						Config.URL + "android/DMSDevice!completed.action", new Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.i("mytest", "sendfinishTask status=" + status);

					}
				}, new ErrorListenerCallBack()) {

					@Override
					protected Map<String, String> getParams() throws AuthFailureError {
						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("deviceid", Config.serialNumber);
						hashMap.put("status", status);
						hashMap.put("errorinfo", errorinfo);
						hashMap.put("msgId", messageId);
						hashMap.put("resId", resId);
						Log.i("mytest_hl02", "sendfinishTask  deviceid=" + Config.serialNumber + ",status=" + status+ ",errorinfo=" + errorinfo+",messageId="+messageId+",resId="+resId);
						return hashMap;
					}
				};

				VolleyController.getInstance().getRequestQueue(context).add(stringRequest);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	/*public static String connService(String url, String str) {
		String ret = "false";
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		try {

			HttpPost httpPost = new HttpPost(url);
			HttpParams httpParams = new BasicHttpParams();
			StringEntity se = new StringEntity(str, "UTF-8");
			se.setContentEncoding("UTF-8");
			se.setContentType("application/json");

			httpPost.setEntity(se);
			httpPost.setParams(httpParams);

			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity != null && response.getStatusLine().getStatusCode() == 200) {// ȡ�÷�������
				ret = EntityUtils.toString(response.getEntity());// ����ת��Ϊ�ı�
			}
		} catch (Exception e) {
			Log.i("sendJsonToServer", e.toString());
			throw new RuntimeException(e);
		}
		return ret;
	}*/

	public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		System.out.print(url);
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * �����ļ�
	 */
	public static void download(String _dirName, String _urlStr) {
		String newFilename = _urlStr.substring(_urlStr.lastIndexOf("/") + 1);

		newFilename = _dirName + newFilename;
		Log.d("BaseService", "newFilename--->" + newFilename);
		File file = new File(newFilename);
		// ���Ŀ���ļ��Ѿ����ڣ���ɾ�����������Ǿ��ļ���Ч��
		if (file.exists()) {
			file.delete();
		}
		try {
			// ����URL
			URL url = new URL(_urlStr);
			// ������
			URLConnection con = url.openConnection();
			// ����ļ��ĳ���
			int contentLength = con.getContentLength();
			// ������
			InputStream is = con.getInputStream();
			// 1K�����ݻ���
			byte[] bs = new byte[1024];
			// ��ȡ�������ݳ���
			int len;
			// ������ļ���
			OutputStream os = new FileOutputStream(newFilename);
			// ��ʼ��ȡ
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// ��ϣ��ر���������
			os.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * sga
	 * 
	 * @param path
	 * @param uploadFile
	 * @param screenshortTime
	 * @param sender
	 */
	public static void uploadFile(String path, String uploadFile, String screenshortTime, String sender) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String newName = FileUtil.getFileNameByUrl(uploadFile);
		try {
			URL url = new URL(path);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* ����Input��Output����ʹ��Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* ���ô��͵�method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			/* ����DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			String str = "Content-Disposition: form-data; name=\"file1\";filename=\"" + newName + "\"" + end;
			ds.writeBytes(str);
			ds.writeBytes(end);
			/* ȡ���ļ���FileInputStream */
			FileInputStream fStream = new FileInputStream(uploadFile);
			/* ����ÿ��д��1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* ���ļ���ȡ������������ */
			while ((length = fStream.read(buffer)) != -1) {
				/* ������д��DataOutputStream�� */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			/* ȡ��Response���� */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			/* �ر�DataOutputStream */
			ds.close();
		} catch (Exception e) {
			System.out.print("�ϴ�ʧ�ܣ�");
		}
	}

	public static void uploadFile(String path, String uploadFile) {
		Log.i("screenshot", "BaseService.uploadFile\npath=" + path + "\nuploadFile=" + uploadFile);
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String newName = FileUtil.getFileNameByUrl(uploadFile);
		try {
			URL url = new URL(path);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* ����Input��Output����ʹ��Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* ���ô��͵�method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			/* ����DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; " + "name=\"file1\";filename=\"" + newName + "\"" + end);
			ds.writeBytes(end);
			/* ȡ���ļ���FileInputStream */
			FileInputStream fStream = new FileInputStream(uploadFile);
			/* ����ÿ��д��1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* ���ļ���ȡ������������ */
			while ((length = fStream.read(buffer)) != -1) {
				/* ������д��DataOutputStream�� */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			/* ȡ��Response���� */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			/* �ر�DataOutputStream */
			ds.close();
		} catch (Exception e) {
			System.out.print("�ϴ�ʧ�ܣ�");
		}
	}
}
