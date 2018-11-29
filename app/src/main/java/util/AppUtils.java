package util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

//import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * app��Ϣ�����
 * 
 * @author Administrator
 * 
 */
public class AppUtils {

	/**
	 * ���apk�汾����
	 * 
	 * @param context
	 * @return
	 */
	public static String getApkVersionName(Context context) {
		String currentVersionCode = "";
		PackageManager manager = context.getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
			// �汾��
			currentVersionCode = info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return currentVersionCode;
	}

	/**
	 * ���apk�汾��
	 * 
	 * @param context
	 * @return
	 */
	public static int getApkVersionCode(Context context) {
		int currentVersionCode = 0;
		PackageManager manager = context.getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
			// �汾��
			currentVersionCode = info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return currentVersionCode;
	}

	/**
	 * ��ȡ������IP��ַ
	 * 
	 * @return
	 */
/*	public static String getLocalHostIp() {
		String ipaddress = "";
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			// �������õ�����ӿ�
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();// �õ�ÿһ������ӿڰ󶨵�����ip
				Enumeration<InetAddress> inet = nif.getInetAddresses();
				// ����ÿһ���ӿڰ󶨵�����ip
				while (inet.hasMoreElements()) {
					InetAddress ip = inet.nextElement();
					if (!ip.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ip
									.getHostAddress())) {
						return ipaddress = "������ip��" + "��" + ip.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			Log.e("feige", "��ȡ����ip��ַʧ��");
			e.printStackTrace();
		}
		return ipaddress;

	}*/

	/**
	 * �ж�app�Ƿ����
	 * 
	 * @param context
	 * @param appPackageName
	 * @return
	 */
	public static boolean isApplicationAvilible(Context context,
			String appPackageName) {
		PackageManager packageManager = context.getPackageManager();// ��ȡpackagemanager
		List pinfo = packageManager.getInstalledPackages(0);// ��ȡ�����Ѱ�װ����İ���Ϣ
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo
						.get(i)
						.toString()
						.substring(
								pinfo.get(i).toString().lastIndexOf("\40") + 1);
				pn = pn.replace("}", "");
				if (appPackageName.equals(pn)) {
					return true;
				}
			}
		}
		return false;
	}

}
