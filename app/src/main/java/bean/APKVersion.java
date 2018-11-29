package bean;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import util.CommonUtil;

public class APKVersion {
	//private String path;
	private String versionCode;
	private String description;
	private String apkurl;
	private String ip;
	
    public APKVersion(String versionCode,String description,String apkurl){
    	this.versionCode=versionCode;
    	this.description=description;
    	this.apkurl=apkurl;		
	}
    
    public APKVersion(){
		
	}
    
    public static APKVersion getAPKVersion(String str){
    	Log.i("connect", "APKVersion.getAPKVersion");
    	if(!CommonUtil.isNotEmpty(str)){
    		return null;
    	}
    	JSONObject obj;
    	APKVersion entity=null;
		try {
			obj = new JSONObject(str);
			int version=obj.getInt("version");								 
			if(version==1){
				entity=new APKVersion();
				entity.setVersionCode(obj.getString("versionCode"));
                entity.setDescription(obj.getString("description"));
                entity.setApkurl(obj.getString("apkurl"));		                                      
               }		                        		                    							  
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entity;                  
    }

	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getApkurl() {
		return apkurl;
	}
	public void setApkurl(String apkurl) {
		this.apkurl = apkurl;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	
	

}
