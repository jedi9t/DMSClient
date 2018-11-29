package util;

import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class CommandUtil {
	// String[][] s=new String[8][8];
	static List<byte[]> cmdList;
	static byte[] volumeCmd=new byte[] { 7, 1, 0, 68, 0,0,102};
	public static void init() {
		cmdList = new ArrayList<byte[]>();
		cmdList.add(new byte[] { 6, 0, 0, 24, 2, 28 });
		cmdList.add(new byte[] { 6, 0, 0, 24, 1, 31 });
		cmdList.add(new byte[] { 9, 0, 0, -84, 5, 0, 1, 0, -95 });
		cmdList.add(new byte[] { 9, 1, 0, -84, 14, 9, 1, 0, -94 });
		cmdList.add(new byte[] { 9, 0, 0, -84, 13, 1, 1, 0, -88 });
		cmdList.add(new byte[] { 9, 0, 0, -84, 6, 1, 1, 0, -93 });
		cmdList.add(new byte[] { 9, 1, 0, -84, 17, 9, 1, 0, -67 });
		cmdList.add(new byte[] { 9, 1, 0, -84, 24, 9, 1, 0, -76 });
		
	//	cmdList.add(new byte[] { 7, 1, 0, 68, 35,35,102});
	}

	public static String ip = "127.0.0.1";

	public static void toCmd(int cmdIndex) {
		Log.i("hhhhh", "��ip:" + ip + "����ָ��:" + cmdIndex);
		if (ip.equals("")) {
			return;
		}
		final byte[] numArr = cmdList.get(cmdIndex);
		new Thread(new Runnable() {
			public void run() {
				Socket socket = null;
				try {

					socket = new Socket(ip, 5000);
		//			socket.setSoTimeout(20000);
					OutputStream os = socket.getOutputStream();
					os.write(numArr);
					os.close();
					Log.i("hhhhh", "ip:" + ip);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
	public static void toCmd(byte b) {
		Log.i("hhhhh", "��ip:" + ip + "������������ָ��:" + b);
		volumeCmd=new byte[] { 7, 1, 0, 68, b,b,102};
		if (ip.equals("")) {
			return;
		}
		//final byte[] numArr = cmdList.get(cmdIndex);
		new Thread(new Runnable() {
			public void run() {
				Socket socket = null;
				try {

					socket = new Socket(ip, 5000);
		//			socket.setSoTimeout(20000);
					OutputStream os = socket.getOutputStream();
					os.write(volumeCmd);
					os.close();
					Log.i("hhhhh", "ip:" + ip);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
}
