package bean;

public class Machine {

	private boolean resource;
	
	private String id;
	private String deviceName;
	

	private String rmid;
	private String machineId;
	private String ip;
	
	private String createTime;

	private String registerState;
	private String capcha;
	private String control;//��ͼ�������
	
	private String userId;
	private String controlTime;

	private String license;
	private String onTime;
	private String offTime;
	private UserGroup userGroup;
	private String topicName;
	
	
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public UserGroup getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
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
	public Machine(){
		
	}
	public Machine(String rmid,String machineId,String ip){
		this.rmid=rmid;
		this.machineId=machineId;
		this.ip=ip;		
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}	
	public String getRmid() {
		return rmid;
	}
	public void setRmid(String rmid) {
		this.rmid = rmid;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getControlTime() {
		return controlTime;
	}

	public void setControlTime(String controlTime) {
		this.controlTime = controlTime;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public String getCapcha() {
		return capcha;
	}

	public void setCapcha(String capcha) {
		this.capcha = capcha;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getRegisterState() {
		return registerState;
	}

	public void setRegisterState(String registerState) {
		this.registerState = registerState;
	}

	public boolean isResource() {
		return resource;
	}

	public void setResource(boolean resource) {
		this.resource = resource;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
}
