package bean;

import java.util.List;

public class PlayManifest {
  int id;
  String startTime;
  String endType;
  List<BaseResource> baseResource;
  UITemplate template;
  
  public PlayManifest(int id,String startTime,String endTime,List<BaseResource> baseResource,UITemplate template){
	  this.id=id;
	  this.startTime=startTime;
	  this.endType=endTime;
	  this.baseResource=baseResource;
	  this.template=template;
  }
  
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getStartTime() {
	return startTime;
}
public void setStartTime(String startTime) {
	this.startTime = startTime;
}
public String getEndType() {
	return endType;
}
public void setEndType(String endType) {
	this.endType = endType;
}
public List<BaseResource> getBaseResource() {
	return baseResource;
}
public void setBaseResource(List<BaseResource> baseResource) {
	this.baseResource = baseResource;
}

public UITemplate getTemplate() {
	return template;
}

public void setTemplate(UITemplate template) {
	this.template = template;
}
}
