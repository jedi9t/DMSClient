package bean;

import java.util.List;

public class Scheduleoftime {
	
long id;

String stime;

String etime;

String name;

List<UITemplate> templates;

int stype;


public int getStype() {
	return stype;
}
public void setStype(int stype) {
	this.stype = stype;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getStime() {
	return stime;
}
public void setStime(String stime) {
	this.stime = stime;
}
public String getEtime() {
	return etime;
}
public void setEtime(String etime) {
	this.etime = etime;
}
public List<UITemplate> getTemplates() {
	return templates;
}
public void setTemplates(List<UITemplate> templates) {
	this.templates = templates;
}


}
