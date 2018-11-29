package bean;

public class MQTTMessage {
	private int syncCmd;
	private String topic;
	private boolean deviceupdate;
	private String messageId;
	private String content;
	private int count;
	private int time;
	private String cycle;
	private String font;
	private String fontsize;
	private String position;
	private String power;
	private String onTime;
	private String offTime;
	
	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	

	public MQTTMessage(int syncCmd, String topic, boolean update,
			String messageId) {
		this.syncCmd = syncCmd;
		this.topic = topic;
		this.deviceupdate = update;
		this.messageId = messageId;
	}
	public MQTTMessage(int syncCmd) {
		this.syncCmd = syncCmd;
	}
	public MQTTMessage(String content,int count,String font,String fontsize,String position,int syncCmd,int time) {
		this.content = content;
		this.count = count;
		this.font = font;
		this.fontsize = fontsize;
		this.position = position;
		this.syncCmd = syncCmd;
		this.time = time;
		
		
	}
	
	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getFontsize() {
		return fontsize;
	}

	public void setFontsize(String fontsize) {
		this.fontsize = fontsize;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	

	public String getOnTime() {
		return onTime;
	}

	public void setOnTime(String onTime) {
		this.onTime = onTime;
	}

	public String getOffTime() {
		return offTime;
	}

	public void setOffTime(String offTime) {
		this.offTime = offTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}
	
	
	
	

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public boolean isDeviceupdate() {
		return deviceupdate;
	}

	public void setDeviceupdate(boolean deviceupdate) {
		this.deviceupdate = deviceupdate;
	}

	public int getSyncCmd() {
		return syncCmd;
	}

	public void setSyncCmd(int syncCmd) {
		this.syncCmd = syncCmd;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
