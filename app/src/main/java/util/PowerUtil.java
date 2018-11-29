package util;

import java.io.OutputStream;
import java.net.Socket;

import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.vigroup.tvapi.common.MTvManager;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class PowerUtil {
	Context context;
	MTvManager mtvManager;
	AudioManager am;
	static int volume = -1;
	byte[] bArr1 = new byte[] { 6, 0, 0, 24, 2, 28 };
	byte[] bArr2 = new byte[] { 6, 0, 0, 24, 1, 31 };

	public PowerUtil(Context context) {
		this.context = context;
		mtvManager = new MTvManager();
		am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

	}

	public void powerOff() {
		try {
			toCmd(bArr2);

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			TvManager.getInstance().setTvosCommonCommand("SetBacklightOff");

			volume = mtvManager.getCurrentVolume(context.getApplicationContext());
			Log.i("volume", "powerOff() volume=" + volume);
			mtvManager.setVolume(context.getApplicationContext(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void powerOn() {

		try {
			toCmd(bArr1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			TvManager.getInstance().setTvosCommonCommand("SetBacklightOn");

			Log.i("volume", "powerOn() volume=" + volume);
			if (volume != -1) {
				mtvManager.setVolume(context.getApplicationContext(), volume);
			} else {
				mtvManager.setVolume(context.getApplicationContext(),
						mtvManager.getCurrentVolume(context.getApplicationContext()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void toCmd(final byte[] bArr) {

		new Thread(new Runnable() {
			public void run() {
				Socket socket = null;
				try {

					socket = new Socket("127.0.0.1", 5000);
					socket.setSoTimeout(5000);
					OutputStream os = socket.getOutputStream();
					os.write(bArr);
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

}
