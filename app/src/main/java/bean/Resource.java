package bean;


import config.Config;

public class Resource {
long id;
String description;
String resUrl;
String refUrl;
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getResUrl() {
	return resUrl;
}
public void setResUrl(String resUrl) {
	this.resUrl = resUrl;
}
public String getRefUrl() {
	return refUrl;
}
public void setRefUrl(String refUrl) {
	this.refUrl = refUrl;
}
public String getFilepath(String currentProgramListPath,long programId) {
	String filePath=Config.DIR_CACHE+currentProgramListPath+"/"+programId+"/"+resUrl.substring(resUrl.lastIndexOf("/")+1);
	
	
	return filePath;
	
}
}
