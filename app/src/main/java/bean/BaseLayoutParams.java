package bean;

import java.util.List;

public class BaseLayoutParams {

	int id;

	int type;

	int top;
	int left;

	int width;
	int height;
	int playDuration;

	int fontsize;

	int style;

	String position;

	String font;
	
	String htmlContent;

	String htmlUrl;
	
	
	
	public String getHtmlUrl() {
		return htmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	private List<BaseResource> resourceList;

	public BaseLayoutParams() {

	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public BaseLayoutParams(String htmlContent,int id, int type, int left, int top, int width,
			int height, int fontsize, int playDuration,
			List<BaseResource> resourceList) {
		this.htmlContent = htmlContent;
		this.id = id;
		this.type = type;
		this.top = top;
		this.left = left;
		this.width = width;
		this.height = height;
		this.fontsize = fontsize;
		this.playDuration = playDuration;
		this.resourceList = resourceList;
	}
	public BaseLayoutParams(String htmlContent,int id, int type, int left, int top, int width,
			int height, int fontsize, int playDuration,
			List<BaseResource> resourceList,String htmlUrl) {
		this.htmlContent = htmlContent;
		this.id = id;
		this.type = type;
		this.top = top;
		this.left = left;
		this.width = width;
		this.height = height;
		this.fontsize = fontsize;
		this.playDuration = playDuration;
		this.resourceList = resourceList;
		this.htmlUrl = htmlUrl;
	}
	public BaseLayoutParams(int id, int type, int left, int top, int width,
			int height, int fontsize, int playDuration,
			List<BaseResource> resourceList) {
		this.id = id;
		this.type = type;
		this.top = top;
		this.left = left;
		this.width = width;
		this.height = height;
		this.fontsize = fontsize;
		this.playDuration = playDuration;
		this.resourceList = resourceList;
	}

	public List<BaseResource> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<BaseResource> resourceList) {
		this.resourceList = resourceList;
	}

	public int getFontsize() {
		return fontsize;
	}

	public void setFontsize(int fontsize) {
		this.fontsize = fontsize;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getPlayDuration() {
		return playDuration;
	}

	public void setPlayDuration(int playDuration) {
		this.playDuration = playDuration;
	}

}
