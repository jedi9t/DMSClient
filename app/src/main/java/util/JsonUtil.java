package util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bean.MQTTMessage;
import bean.Machine;
import bean.RegisterResult;
import bean.UserGroup;
import config.Config;
import helper.PreferenceManager;

public class JsonUtil {

	public String sendMessage(String topic, String messageId) {
		String ret = "";
		JSONObject obj = new JSONObject();
		try {
			obj.put("topic", topic);
			obj.put("deviceId", Config.serialNumber);
			obj.put("messageId", messageId);
			obj.put("status", "1");
			ret = obj.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	public MQTTMessage getMQTTMessage(String str) {
		MQTTMessage ret = null;
		JSONObject jsonObject;
		if (str == null || "".equals(str))
			return null;
		
		try {
			jsonObject = new JSONObject(str);

			int syncCmd = jsonObject.getInt("syncCmd");
			String topic = jsonObject.getString("topic");
			String messageId = jsonObject.getString("messageId");

			boolean update = false;
			JSONArray jsonArray = jsonObject.getJSONArray("devicelist");
			Log.i("mytest", "JsonUtil jsonArray.length()=" + jsonArray.length());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jObject = (JSONObject) jsonArray.opt(i);
				String deviceid = jObject.getString("deviceId");
				Log.i("mytest", "JsonUtil deviceid=" + deviceid+",Config.serialNumber="+Config.serialNumber);
				if (deviceid.equals(Config.serialNumber)) {
					update = true;
					break;
				}
			}
			Log.i("mytest", "JsonUtil update=" + update);
			if (update) {
				String msgid = PreferenceManager.getInstance().getMessageId();
				Log.i("mytest", "oldMessageId=" + msgid+",messageId="+messageId);
				if (msgid.equals(messageId)) {
					return null;
				} else {// ���messageid����ͬ��ִ�и��²�����ͬʱ���浱ǰ��messageid
					PreferenceManager.getInstance().setMessageId(messageId);

					ret = new MQTTMessage(syncCmd, topic, update, messageId);
					Log.i("mytest", "syncCmd=" + syncCmd);
					if (syncCmd == Config.MQTT_CMD_RESOURCE_UPDATE) {

						ret.setMessageId(messageId);
					}
					if (syncCmd == Config.MQTT_CMD_PUBLISH_IM) {
						ret.setContent(jsonObject.getString("content"));
						ret.setTime(jsonObject.getInt("time"));
						ret.setCount(jsonObject.getInt("count"));
						ret.setFont(jsonObject.getString("font"));
						ret.setPosition(jsonObject.getString("position"));
						ret.setFontsize(jsonObject.getString("fontsize"));
					}
					if (syncCmd == Config.MQTT_CMD_INSTALL_APK) {
						ret.setContent(jsonObject.getString("apkUrl"));
					}

					if (syncCmd == Config.MQTT_CMD_POWER_PLAN) {
						// ret.setOnTime(jsonObject.getString("onTime"));
						// ret.setOffTime(jsonObject.getString("offTime"));
						ret.setPower(jsonObject.getString("content"));

						// PreferenceManager.getInstance().setOnTime(ret.getOnTime());
						// PreferenceManager.getInstance().setOffTime(ret.getOffTime());
					}
					if (syncCmd == Config.MQTT_CMD_DELETE_PROGRAM) {
						ret.setContent(jsonObject.getString("content"));

					}
					if (syncCmd == Config.MQTT_CMD_POWER_TIMING) {
						ret.setCycle(jsonObject.getString("cycle"));
						ret.setOnTime(jsonObject.getString("onTime"));
						ret.setOffTime(jsonObject.getString("offTime"));

					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public static Machine getMachine(String str) {
		Machine ret = null;
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(str);
			int total = jsonObject.getInt("total");
			String topicName=jsonObject.getString("topicName");
			if (total == 1) {
				JSONObject obj2 = new JSONObject(jsonObject.get("rows").toString());
				ret = new Machine();
				ret.setId(obj2.getString("id"));
				ret.setMachineId(obj2.getString("device_id"));
				ret.setCapcha(obj2.getString("capcha"));
				ret.setIp(obj2.getString("ip"));
				ret.setOnTime(obj2.getString("onTime"));
				ret.setOffTime(obj2.getString("offTime"));
				ret.setDeviceName(obj2.getString("device_name"));
				ret.setRegisterState(obj2.getString("registerState"));
				JSONObject obj3 = new JSONObject(obj2.getString("usergroup"));
			    int id=obj3.getInt("id");
			    String name=obj3.getString("name");
				ret.setUserGroup(new UserGroup(id,name));
				
				ret.setTopicName(topicName);
				

			//	PreferenceManager.getInstance().setDeviceId(obj2.getString("device_id"));
				PreferenceManager.getInstance().setIP(ret.getIp());
				PreferenceManager.getInstance().setRMID("RM" + ret.getId());
				// PreferenceManager.getInstance().setOnTime(ret.getOnTime());
				// PreferenceManager.getInstance().setOffTime(ret.getOffTime());
				if (ret.getDeviceName() != null || !ret.getDeviceName().equals("") || ret.getDeviceName() != "null") {
					PreferenceManager.getInstance().setDeviceName(ret.getDeviceName());
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public static RegisterResult getRegisterState(String str) {
		RegisterResult registerResult=new RegisterResult();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(str);
			int customerId=jsonObject.getInt("customerId");
			String customerName=jsonObject.getString("customerName");
			registerResult.setCustomerId(customerId);
			registerResult.setCustomerName(customerName);
		}catch (Exception e) {
		}
		return registerResult;
	}
	
	
	
	public static boolean isJsonAvalable(String str) {
		boolean ret = false;
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(str);

			int total = jsonObject.getInt("total");
			if (total == 0) {
				return false;
			}
			JSONArray jsonArray = jsonObject.getJSONArray("rows");
			if (jsonArray == null || jsonArray.length() == 0) {
				return false;
			}
			ret = true;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	/*public List<PlayManifest> getResource(String str) {
		List<PlayManifest> list = null;
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(str);

			JSONArray jsonArray = jsonObject.getJSONArray("rows");
			if (jsonArray == null || jsonArray.length() == 0) {
				return null;
			}
			list = new ArrayList<PlayManifest>();

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jObject = (JSONObject) jsonArray.opt(i);
				int id = jObject.getInt("id");
				String startTime = jObject.getString("startTime");
				String endTime = jObject.getString("endTime");

				JSONArray jArray = jObject.getJSONArray("programManifestList");
				List<BaseResource> rlist = new ArrayList<BaseResource>();
				for (int j = 0; j < jArray.length(); j++) {
					JSONObject obj = (JSONObject) jArray.opt(j);
					int pid = obj.getInt("id");
					int widgetid = 0;
					String s = obj.getString("resource");
					if (s != null && "" != s && "null" != s) {
						JSONObject o = new JSONObject(s);
						String resUrl = o.getString("resUrl");
						String refUrl = null;
						if (o.getString("refUrl") != null && !"".equals(o.getString("refUrl"))
								&& !"null".endsWith(o.getString("refUrl"))) {
							refUrl = o.getString("refUrl");
						}
						String description = o.getString("description");
						s = obj.getString("widget");
						o = new JSONObject(s);
						widgetid = o.getInt("id");
						BaseResource result = new BaseResource(pid, resUrl, refUrl, description, widgetid);
						rlist.add(result);
					}
				}

				JSONObject tobj = new JSONObject(jObject.getString("template"));
				int tid = tobj.getInt("id");
				String background = tobj.getString("background");
				int screenWidth = tobj.getInt("screenWidth");
				int screenHeight = tobj.getInt("scrrenHeight");

				JSONArray tArray = tobj.getJSONArray("widgets");
				List<BaseLayoutParams> wlist = new ArrayList<BaseLayoutParams>();
				for (int j = 0; j < tArray.length(); j++) {
					JSONObject obj = (JSONObject) tArray.opt(j);
					int pid = obj.getInt("id");
					int type = 0;
					double x = obj.getDouble("location_x") * Config.SCREEN_WIDTH / 100;
					double y = obj.getDouble("location_y") * Config.SCREEN_HEIGHT / 100;
					int left = (int) x;
					int top = (int) y;
					double w = obj.getDouble("widthPercent") * Config.SCREEN_WIDTH / 100;
					double h = obj.getDouble("heightPercent") * Config.SCREEN_HEIGHT / 100;
					int width = (int) w;
					int height = (int) h;
					int fontsize = obj.getInt("fontsize");
					int playDuration = obj.getInt("playDuration");
					JSONObject o = new JSONObject(obj.getString("type"));
					type = o.getInt("id");
					BaseLayoutParams result = new BaseLayoutParams(pid, type, left, top, width, height, fontsize,
							playDuration, null);
					wlist.add(result);
				}
				UITemplate template = new UITemplate(tid, background, wlist);

				PlayManifest bean = new PlayManifest(id, startTime, endTime, rlist, template);
				list.add(bean);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}*/
}
