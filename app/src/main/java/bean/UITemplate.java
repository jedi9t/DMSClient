package bean;

import java.util.ArrayList;
import java.util.List;

public class UITemplate {

	long id;
	
	String name;
	
	String background;
	
	int screenWidth;
	
	int screenHeight;
	
	String version;
	
	int fid;
	
	
	List<Widget> widgets;

	String background_path;
	String background_url;
	
	List<String> urlList;
	List<String> wholeUrlList;
	List<LoadItem> loadItemList;
	
	int playtype;
	
	int duration;
	
	int times;
	
	
	
	public List<String> getUrlList() {
		return urlList;
	}

	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}

	public List<String> getWholeUrlList() {
		return wholeUrlList;
	}

	public void setWholeUrlList(List<String> wholeUrlList) {
		this.wholeUrlList = wholeUrlList;
	}

	public int getPlaytype() {
		return playtype;
	}

	public void setPlaytype(int playtype) {
		this.playtype = playtype;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	

	public List<LoadItem> getLoadItemList() {
		return loadItemList;
	}

	public void setLoadItemList(List<LoadItem> loadItemList) {
		this.loadItemList = loadItemList;
	}


	public String getBackground_url() {
		return background_url;
	}

	public void setBackground_url(String background_url) {
		this.background_url = background_url;
	}

	public String getBackground_path() {
		return background_path;
	}

	public void setBackground_path(String background_path) {
		this.background_path = background_path;
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

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public List<Widget> getWidgets() {
		return widgets;
	}

	public void setWidgets(List<Widget> widgets) {
		this.widgets = widgets;
	}
	
	
}
