package util;

import com.victgroup.signup.dmsclient.R;

import java.util.Calendar;

import tvdms.MainActivity;

public class TimeUtil {
	public static long getTimeFromString(String timeStr) {
		String[] timeArr = timeStr.split(":");
		try {
			int hour = Integer.parseInt(timeArr[0]);
			int minute = Integer.parseInt(timeArr[1]);
			long time = hour * 60 + minute;
			return time;

		} catch (Exception e) {
			// TODO: handle exception
		}

		return 0;

	}

	public static long getCurrentTime() {
		Calendar c = Calendar.getInstance();
		int hour = 0;
		if (c.get(Calendar.AM_PM) == Calendar.AM) {
			hour = c.get(Calendar.HOUR);
		} else {
			hour = c.get(Calendar.HOUR) + 12;

		}
		int minute = c.get(Calendar.MINUTE);
		return hour * 60 + minute;
	}

	public static String getTimeStr() {
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH) + 1;

		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		String weekStr=MainActivity.resources.getString(R.string.monday);
		switch(dayOfWeek-1) {
		case 0:
			weekStr=MainActivity.resources.getString(R.string.sunday);
			break;
		case 1:
			weekStr=MainActivity.resources.getString(R.string.monday);
			break;
		case 2:
			weekStr=MainActivity.resources.getString(R.string.tuesday);
			break;
		case 3:
			weekStr=MainActivity.resources.getString(R.string.wednesday);
			break;
		case 4:
			weekStr=MainActivity.resources.getString(R.string.thursday);
			break;
		case 5:
			weekStr=MainActivity.resources.getString(R.string.friday);
			break;
		case 6:
			weekStr=MainActivity.resources.getString(R.string.saturday);
			break;
		}
				
		
		

		String monthStr = null;
		String dayStr = null;
		if (month < 10) {
			monthStr = "0" + month;
		} else {
			monthStr = "" + month;
		}
		if (dayOfMonth < 10) {
			dayStr = "0" + dayOfMonth;
		} else {
			dayStr = "" + dayOfMonth;
		}

		int hour = 0;
		if (c.get(Calendar.AM_PM) == Calendar.AM) {
			hour = c.get(Calendar.HOUR);
		} else {
			hour = c.get(Calendar.HOUR) + 12;

		}
		int minute = c.get(Calendar.MINUTE);
		String hourStr = null;
		String minuteStr = null;
		if (hour < 10) {
			hourStr = "0" + hour;
		} else {
			hourStr = "" + hour;
		}
		if (minute < 10) {
			minuteStr = "0" + minute;
		} else {
			minuteStr = "" + minute;
		}
		return monthStr + MainActivity.resources.getString(R.string.month) + dayStr + MainActivity.resources.getString(R.string.day) + hourStr + MainActivity.resources.getString(R.string.hour) + minuteStr + MainActivity.resources.getString(R.string.minute)+weekStr;
	}

	public static String getSimpleTimeStr() {
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH) + 1;

		int day = c.get(Calendar.DAY_OF_MONTH);

		String monthStr = null;
		String dayStr = null;
		if (month < 10) {
			monthStr = "0" + month;
		} else {
			monthStr = "" + month;
		}
		if (day < 10) {
			dayStr = "0" + day;
		} else {
			dayStr = "" + day;
		}

		int hour = 0;
		if (c.get(Calendar.AM_PM) == Calendar.AM) {
			hour = c.get(Calendar.HOUR);
		} else {
			hour = c.get(Calendar.HOUR) + 12;

		}
		int minute = c.get(Calendar.MINUTE);
		String hourStr = null;
		String minuteStr = null;
		if (hour < 10) {
			hourStr = "0" + hour;
		} else {
			hourStr = "" + hour;
		}
		if (minute < 10) {
			minuteStr = "0" + minute;
		} else {
			minuteStr = "" + minute;
		}
		return monthStr + "��" + dayStr + "�� " + hourStr + "ʱ " + minuteStr + "�� ";
	}
	
	public static String getSampleTimeStr() {
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH) + 1;

		int day = c.get(Calendar.DAY_OF_MONTH);

		String monthStr = null;
		String dayStr = null;
		if (month < 10) {
			monthStr = "0" + month;
		} else {
			monthStr = "" + month;
		}
		if (day < 10) {
			dayStr = "0" + day;
		} else {
			dayStr = "" + day;
		}

		int hour = 0;
		if (c.get(Calendar.AM_PM) == Calendar.AM) {
			hour = c.get(Calendar.HOUR);
		} else {
			hour = c.get(Calendar.HOUR) + 12;

		}
		int minute = c.get(Calendar.MINUTE);
		String hourStr = null;
		String minuteStr = null;
		if (hour < 10) {
			hourStr = "0" + hour;
		} else {
			hourStr = "" + hour;
		}
		if (minute < 10) {
			minuteStr = "0" + minute;
		} else {
			minuteStr = "" + minute;
		}
		return monthStr + dayStr + hourStr + minuteStr;
	}

}
