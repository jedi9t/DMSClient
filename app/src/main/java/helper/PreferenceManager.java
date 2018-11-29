package helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
	
	public static final String PREFERENCE_NAME = "system_setting";
	private static SharedPreferences mSharedPreferences;
	private static PreferenceManager mPreferencemManager;
	private static SharedPreferences.Editor editor;

	private String RMID = "RMID";
	private String DEVICE_ID = "DEVICE_ID";
	private String IP = "IP";

	private String MESSAGE_ID = "MESSAGE_ID";

	private String ONTIME = "ONTIME";
	private String OFFTIME = "OFFTIME";

	private String PROGRAM_ID = "PROGRAM_ID";

	private String Process = "Process";

	private String ResId = "ResId";
	
	
	
	public String getPublishMessageId(String templatesId) {
		return mSharedPreferences.getString(templatesId+"_publishMessageId", "");
	}

	public void setPublishMessageId(String templatesId,String publishMessageId) {
		editor.putString(templatesId+"_publishMessageId", publishMessageId);
		editor.commit();
	}
	
	
	
	
	
	public String getRecentlyDownloadProgramPublish() {
		return mSharedPreferences.getString("recentlyDownloadProgramPublish", "");
	}

	public void setRecentlyDownloadProgramPublish(String recentlyDownloadProgramPublish) {
		editor.putString("recentlyDownloadProgramPublish", recentlyDownloadProgramPublish);
		editor.commit();
	}
	
	public boolean getIsInstallAPKComplete(String installAPKUrl) {
		return mSharedPreferences.getBoolean(installAPKUrl, false);
	}

	public void setIsInstallAPKComplete(String installAPKUrl,boolean isInstallAPKComplete) {
		editor.putBoolean(installAPKUrl, isInstallAPKComplete);
		editor.commit();
	}
	
	public String getPublishTime(String messageid) {
		return mSharedPreferences.getString("publish_time"+messageid, "");
	}

	public void setPublishTime(String messageid,String timeStr) {
		editor.putString("publish_time"+messageid, timeStr);
		editor.commit();
	}
	
	public String getOnTime() {
		return mSharedPreferences.getString(ONTIME, "");
	}

	public void setOnTime(String str) {
		editor.putString(ONTIME, str);
		editor.commit();
	}
	
	public String getOffTime() {
		return mSharedPreferences.getString(OFFTIME, "");
	}

	public void setOffTime(String str) {
		editor.putString(OFFTIME, str);
		editor.commit();
	}

	public String getProgram_id() {
		return mSharedPreferences.getString(PROGRAM_ID, "");
	}

	public void setProgram_id(String id) {
		editor.putString(PROGRAM_ID, id);
		editor.commit();
	}

	
	
	public String getMessageId() {
		return mSharedPreferences.getString(MESSAGE_ID, "");
	}

	public void setMessageId(String str) {
		editor.putString(MESSAGE_ID, str);
		editor.commit();
	}

	public String getRMID() {
		return mSharedPreferences.getString(RMID, "");
	}

	public void setRMID(String str) {
		editor.putString(RMID, str);
		editor.commit();
	}

	/*public String getDeviceId() {
		return mSharedPreferences.getString(DEVICE_ID, "");
	}

	public void setDeviceId(String str) {
		editor.putString(DEVICE_ID, str);
		editor.commit();
	}*/
	
	public void setProcess(String str) {
		editor.putString(Process, str);
		editor.commit();
	}

	public String getProcess() {
		return mSharedPreferences.getString(Process, "");
	}
	
	public void setResId(String str) {
		editor.putString(ResId, str);
		editor.commit();
	}

	public String getResId() {
		return mSharedPreferences.getString(ResId, "");
	}


	public String getIP() {
		return mSharedPreferences.getString(IP, "");
	}

	public void setIP(String str) {
		editor.putString(IP, str);
		editor.commit();
	}

	public static synchronized void init(Context cxt) {
		if (mPreferencemManager == null) {
			mPreferencemManager = new PreferenceManager(cxt);
		}
	}

	private PreferenceManager(Context cxt) {
		mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();
	}

	public synchronized static PreferenceManager getInstance() {
		if (mPreferencemManager == null) {
			throw new RuntimeException("please init first!");
			
		}
		return mPreferencemManager;
	}
	
	public void setRegisterState(int registerState) {
		editor.putInt("registerState", registerState);
		editor.commit();
	}

	public int getRegisterState() {
		
		return mSharedPreferences.getInt("registerState",0);
	}
	
	
	
	
	public void setDmsUrl(String dms_url) {
		editor.putString("dms_url",dms_url);
		editor.commit();
	}

	public String getDmsUrl() {
		
		return mSharedPreferences.getString("dms_url","");
	}
	public void setDeviceName(String deviceName) {
		editor.putString("deviceName",deviceName);
		editor.commit();
	}

	public String getDeviceName() {
		
		return mSharedPreferences.getString("deviceName","");
	}
	
	
	public void setTopicName(String topicName) {
		editor.putString("topicName",topicName);
		editor.commit();
	}

	public String getTopicName() {
		
		return mSharedPreferences.getString("topicName","");
	}
	
	
	
	
	
	
	
	
	public void setUserName(String userName) {
		editor.putString("userName",userName);
		editor.commit();
	}

	public String getUserName() {
		
		return mSharedPreferences.getString("userName","");
	}
	
	public int getIsBootAutoStart() {
		return mSharedPreferences.getInt("isBootAutoStart",0);
	}

	public void setIsBootAutoStart(int isBootAutoStart) {
		editor.putInt("isBootAutoStart", isBootAutoStart);
		editor.commit();
	}

	public boolean getIsProgramCompleteDownload(String templateListId,String programId) {
		return mSharedPreferences.getBoolean(templateListId+"_"+programId,false);
	}

	public void setIsProgramCompleteDownload(String templateListId,String programId,boolean isCompleteDownload) {
		editor.putBoolean(templateListId+"_"+programId, isCompleteDownload);
		editor.commit();
	}
	public boolean getIsProgramListCompleteDownload(String templateListId) {
		return mSharedPreferences.getBoolean("programList_"+templateListId,false);
	}

	public void setIsProgramListCompleteDownload(String templateListId,boolean isCompleteDownload) {
		editor.putBoolean("programList_"+templateListId, isCompleteDownload);
		editor.commit();
	}
	
/*	public long getPlayCount(String programId) {
		return mSharedPreferences.getLong(programId+"_playcount",0);
	}

	public void setPlayCount(String programId,long playCount) {
		editor.putLong(programId+"_playcount", playCount);
		editor.commit();
	}*/
	
	public String getProgramName(String programId) {
		return mSharedPreferences.getString(programId+"_programname","");
	}

	public void setProgramName(String programId,String programName) {
		editor.putString(programId+"_programname", programName);
		editor.commit();
	}
	
	public long getFileLength(String fileName) {
		return mSharedPreferences.getLong(fileName+"_filelength",0);
	}

	public void setFileLength(String fileName,long fileLength) {
		editor.putLong(fileName+"_filelength", fileLength);
		editor.commit();
	}
	
	/*public long getFileTotal(String fileName) {
		return mSharedPreferences.getLong(fileName+"_filelength",0);
	}

	public void setFileLength(String fileName,long fileLength) {
		editor.putLong(fileName+"_filelength", fileLength);
		editor.commit();
	}*/
	
	
	
	
	public String getOnDay() {
		return mSharedPreferences.getString("onDay","");
	}

	public void setOnDay(String onDay) {
		editor.putString("onDay",onDay);
		editor.commit();
	}
	public boolean getIsAllDayOn() {
		return mSharedPreferences.getBoolean("isAllDayOn",false);
	}

	public void setIsAllDayOn(boolean isAllDayOn) {
		editor.putBoolean("isAllDayOn",isAllDayOn);
		editor.commit();
	}
	
	public int getProgramVersion(String programId) {
		return mSharedPreferences.getInt(programId+"_version",0);
	}

	public void setProgramVersion(String programId,int version) {
		editor.putInt(programId+"_version", version);
		editor.commit();
	}
	
	public String getTimingPowerOn() {
		return mSharedPreferences.getString("timingPowerOn","");
	}

	public void setTimingPowerOn(String timingPowerOn) {
		editor.putString("timingPowerOn",timingPowerOn);
		editor.commit();
	}
	public String getTimingPowerOff() {
		return mSharedPreferences.getString("timingPowerOff","");
	}

	public void setTimingPowerOff(String timingPowerOff) {
		editor.putString("timingPowerOff",timingPowerOff);
		editor.commit();
	}
	
	public String getTemplatesId() {
		return mSharedPreferences.getString("templateListId","");
	}

	public void setTemplatesId(String templateListId) {
		editor.putString("templateListId", templateListId);
		editor.commit();
	}      
	
	public String getImmediatelyTemplatesId() {
		return mSharedPreferences.getString("immediately_templateListId","");
	}

	public void setImmediatelyTemplatesId(String immediately_templateListId) {
		editor.putString("immediately_templateListId", immediately_templateListId);
		editor.commit();
	}    
	
	public String getTimerTemplatesId() {
		return mSharedPreferences.getString("timer_templateListId","");
	}

	public void setTimerTemplatesId(String timer_templateListId) {
		editor.putString("timer_templateListId", timer_templateListId);
		editor.commit();
	}    
	
	public long getRecordScheduleoftimeId() {
		return mSharedPreferences.getLong("record_scheduleoftimeId",0);
	}

	public void setRecordScheduleoftimeId(long record_scheduleoftimeId) {
		editor.putLong("record_scheduleoftimeId", record_scheduleoftimeId);
		editor.commit();
	}   
	
	
	
	
	
	
	public String getSelectProgramList(String templatesId) {
		return mSharedPreferences.getString(templatesId+"selectProgramList","");
	}

	public void setSelectProgramList(String templatesId,String selectProgramList) {
		editor.putString(templatesId+"selectProgramList",selectProgramList);
		editor.commit();
	}
	
	public int getDefaultVolume() {
		return mSharedPreferences.getInt("defaultVolume",0);
	}

	public void setDefaultVolume(int defaultVolume) {
		editor.putInt("defaultVolume", defaultVolume);
		editor.commit();
	}
	
	public boolean getIsFirstBoot() {
		return mSharedPreferences.getBoolean("isfirstboot",true);
	}

	public void setIsFirstBoot(boolean isfirstboot) {
		editor.putBoolean("isfirstboot", isfirstboot);
		editor.commit();
	}
	
	
	
	
	
	public int getOrientation() {
		return mSharedPreferences.getInt("orientation",0);
	}

	public void setOrientation(int orientation) {
		editor.putInt("orientation", orientation);
		editor.commit();
	}
	
	
	
	
	
	
}
