package bean;

import java.util.List;

public class Schedule {
	long id;
String name;
boolean ispublish;
List<Scheduleoftime> scheduleoftimes;
int stype;




public int getStype() {
	return stype;
}
public void setStype(int stype) {
	this.stype = stype;
}
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public boolean isIspublish() {
	return ispublish;
}
public void setIspublish(boolean ispublish) {
	this.ispublish = ispublish;
}
public List<Scheduleoftime> getScheduleoftimes() {
	return scheduleoftimes;
}
public void setScheduleoftimes(List<Scheduleoftime> scheduleoftimes) {
	this.scheduleoftimes = scheduleoftimes;
}


}
