package util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.vigroup.tvapi.common.MTvManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

public class CommonUtil {
	static MTvManager mtvManager;
	static AudioManager mAudioManager;
	public static void tvTimerOnOff(Context context, String onTime, String offTime, boolean flag) {

	}

	public static long getTotalInternalMemorySize() {
		// ��ȡ�ڲ��洢��Ŀ¼
		File path = Environment.getDataDirectory();
		// ϵͳ�Ŀռ�������
		StatFs stat = new StatFs(path.getPath());
		// ÿ������ռ�ֽ���
		long blockSize = stat.getBlockSize();
		// ��������
		long totalBlocks = stat.getBlockCount();
		return (totalBlocks * blockSize)/(1024*1024);
	}

	public static long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		// ��ȡ������������
		long availableBlocks = stat.getAvailableBlocks();
		return (availableBlocks * blockSize)/(1024*1024);
	}

	public static Calendar getTimenextDay(int daylater) {
		Calendar c = Calendar.getInstance();
		Date date = new Date();
		c.setTime(date);
		c.add(Calendar.DATE, daylater);
		return c;
	}

	public static String getMacAddress() {
		String result = "";
		String Mac = "";
		result = callCmd("busybox ifconfig", "HWaddr");

		if (result == null) {
			return "���������������";
		}
		// �Ը������ݽ��н���
		// ���磺eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
		if (result.length() > 0 && result.contains("HWaddr") == true) {
			Mac = result.substring(result.indexOf("HWaddr") + 6, result.length() - 1);
			if (Mac.length() > 1) {
				Mac = Mac.replaceAll(" ", "");
				result = "";
				String[] tmp = Mac.split(":");
				for (int i = 0; i < tmp.length; ++i) {
					result += tmp[i];
				}
			}
			Log.i("test", result + " result.length: " + result.length());
		}
		return result;
	}

	public static String callCmd(String cmd, String filter) {
		String result = "";
		String line = "";
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);

			// ִ������cmd��ֻȡ����к���filter����һ��
			while ((line = br.readLine()) != null && line.contains(filter) == false) {
				// result += line;
				Log.i("test", "line: " + line);
			}

			result = line;
			Log.i("test", "result: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean isNotEmpty(String str) {
		if (str != null && !"".equals(str) && !"null".equals(str)) {
			return true;
		}
		return false;
	}

	private static SharedPreferences sharedPrefs;

	public void doStartApplicationWithPackageName(Context context, String packagename) {

		// ͨ��������ȡ��APP��ϸ��Ϣ������Activities��services��versioncode��name�ȵ�
		PackageInfo packageinfo = null;
		try {
			packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageinfo == null) {
			return;
		}

		// ����һ�����ΪCATEGORY_LAUNCHER�ĸð�����Intent
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(packageinfo.packageName);

		// ͨ��getPackageManager()��queryIntentActivities��������
		List<ResolveInfo> resolveinfoList = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

		ResolveInfo resolveinfo = resolveinfoList.iterator().next();
		if (resolveinfo != null) {
			// packagename = ����packname
			String packageName = resolveinfo.activityInfo.packageName;
			// �����������Ҫ�ҵĸ�APP��LAUNCHER��Activity[��֯��ʽ��packagename.mainActivityname]
			String className = resolveinfo.activityInfo.name;
			// LAUNCHER Intent
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			// ����ComponentName����1:packagename����2:MainActivity·��
			ComponentName cn = new ComponentName(packageName, className);

			intent.setComponent(cn);
			context.startActivity(intent);
		}
	}

	public static String getWifiMacId(Context context) {
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
		return m_szWLANMAC;
	}

	public static String getMacId(Context context) {
		String m_szWLANMAC = getMac(context);
		
		String m_szLongID = m_szWLANMAC;
		Log.i("mac", "mac=" + m_szWLANMAC);
		// compute md5
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
		// get md5 bytes
		byte p_md5Data[] = m.digest();
		// create a hex string
		String m_szUniqueID = new String();
		for (int i = 0; i < p_md5Data.length; i++) {

			int b = (0xFF & p_md5Data[i]);
			// int b= p_md5Data[i]
			if (b <= 0xF)
				m_szUniqueID += "0";
			m_szUniqueID += Integer.toHexString(b);
		}
		m_szUniqueID = m_szUniqueID.toUpperCase();
		return m_szUniqueID;
	}

	public static String getSerialNumberRandomStr(String str) {
		Log.i("mac", "serialName=" + str);
		// compute md5
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		m.update(str.getBytes(), 0, str.length());
		// get md5 bytes
		byte p_md5Data[] = m.digest();
		// create a hex string
		String m_szUniqueID = new String();
		for (int i = 0; i < p_md5Data.length; i++) {

			int b = (0xFF & p_md5Data[i]);
			// int b= p_md5Data[i]
			if (b <= 0xF)
				m_szUniqueID += "0";
			m_szUniqueID += Integer.toHexString(b);
		}
		m_szUniqueID = m_szUniqueID.toUpperCase();
		
		return m_szUniqueID.substring(0,6);
	}

	/*public static String getLocalHostIp() {
		String ipaddress = "no_data";
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			// �������õ�����ӿ�
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();// �õ�ÿһ������ӿڰ󶨵�����ip
				Enumeration<InetAddress> inet = nif.getInetAddresses();
				// ����ÿһ���ӿڰ󶨵�����ip
				while (inet.hasMoreElements()) {
					InetAddress ip = inet.nextElement();
					if (!ip.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ip.getHostAddress())) {
						return ipaddress = ip.getHostAddress();
					}
				}

			}
		} catch (SocketException e) {
			System.out.print("��ȡIP ʧ��" + e.toString());
			e.printStackTrace();
		}
		return ipaddress;
	}*/
	public static String getIP(Context context){

	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	             NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
	              {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address))
	                {
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    }
	    catch (SocketException ex){
	        ex.printStackTrace();
	    }
	    return null;
	}

	public static List<String> getTxtList(String str) {
		List<String> subtitleList = null;
		if (str != null && !"".equals(str)) {
			subtitleList = new ArrayList<String>();
			String[] aa = str.split("/n");
			for (int i = 0; i < aa.length; i++) {
				subtitleList.add(aa[i]);
			}
		}
		return subtitleList;
	}

	public static String getMac(Context context) {
		String macSerial = null;
		String str = "";
		InputStreamReader ir = null;
		LineNumberReader input;
		try {
			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/eth0/address ");
			ir = new InputStreamReader(pp.getInputStream());
			input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// ȥ�ո�
					break;
				}
			}
			ir.close();
			input.close();
		} catch (IOException ex) {
			// ����Ĭ��ֵ
			ex.printStackTrace();
		}
		if (macSerial == null || macSerial.equals("")) {
			WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			macSerial = wm.getConnectionInfo().getMacAddress();
		}
		return macSerial;
	}
	
	public static int getVolume(Context context) {
		int volume=-1;
		if(mtvManager==null) {
			mtvManager=new MTvManager();
		}
		if(mAudioManager==null) {
			mAudioManager=(AudioManager) context.getSystemService(context.AUDIO_SERVICE);
		}
		try {
		 volume = mtvManager.getCurrentVolume(context.getApplicationContext());
		}catch (Exception e) {
		}
		if(volume!=-1) {
			return volume;
		}
		try {
		 volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		}catch (Exception e) {
		}
		if(volume!=-1) {
			return volume;
		}
		return 0;
		
	}
	public static int setVolume(Context context,int volume) {
		if(mtvManager==null) {
			mtvManager=new MTvManager();
		}
		if(mAudioManager==null) {
			mAudioManager=(AudioManager) context.getSystemService(context.AUDIO_SERVICE);
		}
		try {
			mtvManager.setDefaultVolume(context, volume);
		}catch (Exception e) {
		}
//		public void setStreamVolume (int streamType, int index, int flags)
		try {
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volume, AudioManager.FLAG_PLAY_SOUND);
		}catch (Exception e) {
		}
		return volume;
		
	}
	/**
	 * ���ص�ǰ����汾��
	 */
	public static String getAppVersionCode(Context context) {
	    int versioncode = 0;
	    try {
	        PackageManager pm = context.getPackageManager();
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
	        // versionName = pi.versionName;
	        versioncode = pi.versionCode;
	    } catch (Exception e) {
	        Log.e("VersionInfo", "Exception", e);
	    }
	    return versioncode + "";
	}

	/**
	 * ���ص�ǰ����汾��
	 */
	public static String getAppVersionName(Context context) {
	    String versionName=null;
	    try {
	        PackageManager pm = context.getPackageManager();
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
	        versionName = pi.versionName;
	    } catch (Exception e) {
	        Log.e("VersionInfo", "Exception", e);
	    }
	    return versionName;
	}

}
