package bean;

public class BaseResource {
int id;
String resUrl;
String refUrl;
//int type;
String filepath;
String refpath;
String description;

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public String getRefUrl() {
	return refUrl;
}

public void setRefUrl(String refUrl) {
	this.refUrl = refUrl;
}

public String getRefpath() {
	return refpath;
}

/*public void setRefpath(String refpath) {
	this.refpath = refpath;
}*/
int widgetid;

public int getWidgetid() {
	return widgetid;
}

public void setWidgetid(int widgetid) {
	this.widgetid = widgetid;
}

public String getFilepath() {
	return filepath;
}

public void setFilepath(String filepath) {
	this.filepath = filepath;
}

public BaseResource(int id,String resUrl,String refUrl,String description,int widgetid){
	this.id=id;
	this.resUrl=resUrl;	
	this.refUrl=refUrl;
	this.description=description;
	this.widgetid=widgetid;	
}

public BaseResource(int id,String resUrl,String refUrl){
	this.id=id;
	this.resUrl=resUrl;	
	this.refUrl=refUrl;
}

public BaseResource(int id,String resUrl){
	this.id=id;
	this.resUrl=resUrl;	
}

public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getResUrl() {
	return resUrl;
}
public void setResUrl(String resUrl) {
	this.resUrl = resUrl;
}
/*public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
*/

}
