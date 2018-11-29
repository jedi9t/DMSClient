package helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.victgroup.signup.dmsclient.R;

import bean.BaseLayoutParams;
import bean.BaseResource;
import bean.LoadItem;
import bean.ProgramItem;
import bean.ProgramPublish;
import bean.Resource;
import bean.Schedule;
import bean.Scheduleoftime;
import bean.Type;
import bean.UITemplate;
import bean.Widget;
import config.Config;
import http.BaseService;
import sql.DatabaseHelper;
import sql.SQLiteHelper;
import tvdms.DownloadResource;
import tvdms.MainActivity;
import util.CommonUtil;
import util.FileUtil;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class TemplateHelper {

	public static int getTemplatesId(String str) {
		if (!CommonUtil.isNotEmpty(str)) {
			return 0;
		}
		try {
			JSONObject jsonObject = new JSONObject(str);
			int total = jsonObject.getInt("total");
			if (total <= 0) {
				return 0;
			}
			JSONArray tArray = new JSONArray(jsonObject.getString("rows"));

			for (int i = 0; i < tArray.length(); i++) {
				JSONObject tobject = (JSONObject) tArray.opt(i);

				return tobject.getInt("id");

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static ProgramPublish getDownloadTemplate(Context context, String str, int source) {
		DatabaseHelper.init(context);
		ProgramPublish programPublish = null;
		try {

			programPublish = new Gson().fromJson(str, new TypeToken<ProgramPublish>() {
			}.getType());

			if (programPublish != null) {

				List<UITemplate> uITemplateList = new ArrayList<UITemplate>();
				List<Schedule> rows = programPublish.getRows();
				if (rows == null) {
					return null;
				}
				Schedule schedule = rows.get(0);
				if (schedule == null) {
					return null;
				}

				List<Scheduleoftime> scheduleoftimes = schedule.getScheduleoftimes();
				ContentValues values = new ContentValues();
				Cursor c = null;
				for (Scheduleoftime scheduleoftime : scheduleoftimes) {
					if (schedule.getStype() == 0) {
						scheduleoftime.setStype(0);
					} else {
						scheduleoftime.setStype(1);
					}

					List<UITemplate> templates = scheduleoftime.getTemplates();
					for (UITemplate uITemplate : templates) {

						List<String> wholeUrlList = new ArrayList<String>();
						List<String> urlList = new ArrayList<String>();

						List<LoadItem> loadItemList = new ArrayList<LoadItem>();
						String background = uITemplate.getBackground();

						if (CommonUtil.isNotEmpty(background)) {
							urlList.add(background);


							c = DatabaseHelper.getInstance().query(SQLiteHelper.RESOURCE_TB_NAME,
									new String[] { "wholeurl", "name", "complete" }, "url=?",
									new String[] { background }, null, null, null);

							String background_path;
							String background_url;
							if (c.moveToNext()) {
								Log.i("mytest", "background c.moveToNext()");
								background_path = Config.DIR_CACHE + c.getString(c.getColumnIndex("name"));
								background_url = c.getString(c.getColumnIndex("wholeurl"));
								// Log.i("connect", "TemplateHelper.getTemplate
								// background_path="+background_path);
								if (FileUtil.getIsExists(background_path)) {
									int complete = c.getInt(c.getColumnIndex("complete"));
									if (complete == 0) {
										loadItemList.add(new LoadItem(background_path, background_url));
									}
								} else {
									loadItemList.add(new LoadItem(background_path, background_url));
								}
								uITemplate.setBackground_path(background_path);
								uITemplate.setBackground_url(background_url);
							} else {
								Log.i("mytest", "background !c.moveToNext()");
								String endWith = background.substring(background.lastIndexOf("/") + 1);
								String fileName = null;
								String timeStr = Calendar.getInstance().getTimeInMillis() + "";
								if (endWith.contains(".")) {
									fileName = timeStr + endWith.substring(endWith.lastIndexOf("."));
								} else {
									fileName = timeStr;
								}

								background_path = Config.DIR_CACHE + fileName;

								if (background.startsWith("http")) {
									background_url = background;
								} else {
									background_url = Config.URL + background;
								}

								values.clear();
								values.put("date", timeStr);
								values.put("url", background);
								values.put("wholeurl", background_url);
								values.put("name", fileName);
								values.put("complete", 0);
								DatabaseHelper.getInstance().insert(SQLiteHelper.RESOURCE_TB_NAME, null, values);
								uITemplate.setBackground_path(background_path);
								uITemplate.setBackground_url(background_url);

								loadItemList.add(new LoadItem(Config.DIR_CACHE + fileName, background_url));
							}
							wholeUrlList.add(background_url);
							c.close();
						}

						List<Widget> widgets = uITemplate.getWidgets();
						for (Widget widget : widgets) {

							double location_x = widget.getLocation_x();
							double location_y = widget.getLocation_y();
							double widthPercent = widget.getWidthPercent();
							double heightPercent = widget.getHeightPercent();
							int left = (int) (location_x * Config.SCREEN_WIDTH / 100);
							int top = (int) (location_y * Config.SCREEN_HEIGHT / 100);
							int width = (int) (widthPercent * Config.SCREEN_WIDTH / 100);
							int height = (int) (heightPercent * Config.SCREEN_HEIGHT / 100);

							widget.setLeft(left);
							widget.setTop(top);
							widget.setWidth(width);
							widget.setHeight(height);

							List<Resource> resources = widget.getResources();
							List<String> resourcePathList = new ArrayList<String>();
							if (widget.getType().getId() == 5) {
								String htmlurl = widget.getHtmlurl();
								if (htmlurl != null && !"".equals(htmlurl) && !"null".equals(htmlurl)) {
									urlList.add(htmlurl);

									/*
									 * if (htmlurl.startsWith("http")) { wholeHtmlUrl = htmlurl + ".zip"; } else {
									 * wholeHtmlUrl = Config.URL + htmlurl + ".zip"; }
									 * 
									 * wholeUrlList.add(wholeHtmlUrl); widget.setHtmlurl(wholeHtmlUrl);
									 */

									c = DatabaseHelper.getInstance().query(SQLiteHelper.RESOURCE_TB_NAME,
											new String[] { "date", "wholeurl", "name", "complete" }, "url=?",
											new String[] { htmlurl }, null, null, null);
									String zip_path;
									String html_path;
									String wholeHtmlUrl;
									if (c.moveToNext()) {
										Log.i("mytest", "h5 c.moveToNext()");
										zip_path = Config.DIR_CACHE + c.getString(c.getColumnIndex("name"));
										html_path = Config.DIR_CACHE + c.getString(c.getColumnIndex("date"))
												+ "/index.html";
										wholeHtmlUrl = c.getString(c.getColumnIndex("wholeurl"));
										if (FileUtil.getIsExists(zip_path)) {
											int complete = c.getInt(c.getColumnIndex("complete"));
											if (complete == 0) {
												loadItemList.add(new LoadItem(zip_path, wholeHtmlUrl));
											}
										} else {
											loadItemList.add(new LoadItem(zip_path, wholeHtmlUrl));
										}
										widget.setZipPath(zip_path);
										widget.setHtmlPath(html_path);
										resourcePathList.add(html_path);
									} else {
										Log.i("mytest", "h5 !c.moveToNext()");
										values.clear();
										Log.i("mytest", "htmlurl=" + htmlurl);
										String endWith = htmlurl.substring(htmlurl.lastIndexOf("/") + 1);
										String fileName = null;
										String timeStr = Calendar.getInstance().getTimeInMillis() + "";

										if (endWith.contains(".")) {
											fileName = timeStr + endWith.substring(endWith.lastIndexOf("."));
										} else {
											fileName = timeStr + ".zip";
										}
										zip_path = Config.DIR_CACHE + fileName;
										html_path = Config.DIR_CACHE + timeStr + "/" + "index.html";
										if (htmlurl.startsWith("http")) {
											wholeHtmlUrl = htmlurl + ".zip";
										} else {
											wholeHtmlUrl = Config.URL + htmlurl + ".zip";
										}

										values.put("date", timeStr);
										values.put("url", htmlurl);
										values.put("wholeurl", wholeHtmlUrl);
										values.put("name", fileName);
										values.put("complete", 0);
										DatabaseHelper.getInstance().insert(SQLiteHelper.RESOURCE_TB_NAME, null,
												values);
										widget.setZipPath(zip_path);
										widget.setHtmlPath(html_path);
										resourcePathList.add(html_path);
										loadItemList.add(new LoadItem(zip_path, wholeHtmlUrl));
									}
									wholeUrlList.add(wholeHtmlUrl);
									widget.setHtmlurl(wholeHtmlUrl);
									c.close();
								}
							}

							for (Resource resource : resources) {
								String resUrl = resource.getResUrl();
								urlList.add(resUrl);

								/*
								 * if (resUrl.startsWith("http")) { wholeResUrl = resUrl; } else { wholeResUrl =
								 * Config.URL + resUrl;
								 * 
								 * } wholeUrlList.add(wholeResUrl);
								 */

								c = DatabaseHelper.getInstance().query(SQLiteHelper.RESOURCE_TB_NAME,
										new String[] { "wholeurl", "name", "complete" }, "url=?",
										new String[] { resUrl }, null, null, null);
								String path;
								String wholeResUrl;
								if (c.moveToNext()) {
									Log.i("mytest", "resource c.moveToNext()");
									path = Config.DIR_CACHE + c.getString(c.getColumnIndex("name"));
									wholeResUrl = c.getString(c.getColumnIndex("wholeurl"));
									// Log.i("connect", "TemplateHelper.getTemplate path="+path);
									if (FileUtil.getIsExists(path)) {

										int complete = c.getInt(c.getColumnIndex("complete"));

										if (complete == 0) {
											loadItemList.add(new LoadItem(path, wholeResUrl));
										}
									} else {
										loadItemList.add(new LoadItem(path, wholeResUrl));
									}
									resourcePathList.add(path);
								} else {
									Log.i("mytest", "resource !c.moveToNext()");
									values.clear();

									String endWith = resUrl.substring(resUrl.lastIndexOf("/") + 1);

									String fileName = null;
									String timeStr = Calendar.getInstance().getTimeInMillis() + "";
									if (endWith.contains(".")) {
										fileName = timeStr + endWith.substring(endWith.lastIndexOf("."));
									} else {
										switch (widget.getType().getId()) {

										case 1:
											fileName = timeStr + ".mp4";
											break;
										case 2:
											fileName = timeStr + ".jpg";
											break;
										case 3:
											fileName = timeStr + ".txt";
											break;
										case 4:
											fileName = timeStr;
											break;
										case 5:
											fileName = timeStr + ".html";
											break;
										case 6:
											fileName = timeStr + ".html";
											break;
										case 7:
											fileName = timeStr + ".ppt";
											break;
										}

									}

									path = Config.DIR_CACHE + fileName;
									if (resUrl.startsWith("http")) {
										wholeResUrl = resUrl;
									} else {
										wholeResUrl = Config.URL + resUrl;

									}
									values.put("date", timeStr);
									values.put("url", resUrl);
									values.put("wholeurl", wholeResUrl);
									values.put("name", fileName);
									values.put("complete", 0);
									DatabaseHelper.getInstance().insert(SQLiteHelper.RESOURCE_TB_NAME, null, values);

									resourcePathList.add(path);
									loadItemList.add(new LoadItem(path, wholeResUrl));
								}
								wholeUrlList.add(wholeResUrl);
								c.close();
							}
							widget.setResourcePathList(resourcePathList);
						}
						uITemplate.setLoadItemList(loadItemList);
						uITemplate.setWholeUrlList(wholeUrlList);
						uITemplate.setUrlList(urlList);
						if (!getIsContain(uITemplateList, uITemplate)) {
							// Log.i()
							uITemplateList.add(uITemplate);
						}
					}
				}
				programPublish.setuITemplateList(uITemplateList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return programPublish;

	}

	public static ProgramPublish getTemplate(Context context, String str, int source) {
		DatabaseHelper.init(context);
		ProgramPublish programPublish = null;
		try {
			programPublish = new Gson().fromJson(str, new TypeToken<ProgramPublish>() {
			}.getType());
			if (programPublish != null) {

				List<UITemplate> uITemplateList = new ArrayList<UITemplate>();
				List<Schedule> rows = programPublish.getRows();
				if (rows == null) {
					return null;
				}
				Schedule schedule = rows.get(0);
				if (schedule == null) {
					return null;
				}

				List<Scheduleoftime> scheduleoftimes = schedule.getScheduleoftimes();
				Cursor c = null;
				for (Scheduleoftime scheduleoftime : scheduleoftimes) {
					if (schedule.getStype() == 0) {
						scheduleoftime.setStype(0);
					} else {
						scheduleoftime.setStype(1);
					}

					List<UITemplate> templates = scheduleoftime.getTemplates();
					for (UITemplate uITemplate : templates) {

						List<String> wholeUrlList = new ArrayList<String>();
						List<String> urlList = new ArrayList<String>();

						String background = uITemplate.getBackground();

						if (CommonUtil.isNotEmpty(background)) {
							String background_url;
							if (background.startsWith("http")) {
								background_url = background;
							} else {
								background_url = Config.URL + background;
							}
							wholeUrlList.add(background_url);
							urlList.add(background);
							c = DatabaseHelper.getInstance().query(SQLiteHelper.RESOURCE_TB_NAME,
									new String[] { "name", "complete" }, "url=?", new String[] { background }, null,
									null, null);
							String background_path;
							if (c.moveToNext()) {
								background_path = Config.DIR_CACHE + c.getString(c.getColumnIndex("name"));
								uITemplate.setBackground_path(background_path);
								uITemplate.setBackground_url(background_url);
							}
							c.close();
						}

						List<Widget> widgets = uITemplate.getWidgets();
						for (Widget widget : widgets) {

							double location_x = widget.getLocation_x();
							double location_y = widget.getLocation_y();
							double widthPercent = widget.getWidthPercent();
							double heightPercent = widget.getHeightPercent();
							int left = (int) (location_x * Config.SCREEN_WIDTH / 100);
							int top = (int) (location_y * Config.SCREEN_HEIGHT / 100);
							int width = (int) (widthPercent * Config.SCREEN_WIDTH / 100);
							int height = (int) (heightPercent * Config.SCREEN_HEIGHT / 100);

							widget.setLeft(left);
							widget.setTop(top);
							widget.setWidth(width);
							widget.setHeight(height);

							List<Resource> resources = widget.getResources();
							List<String> resourcePathList = new ArrayList<String>();
							if (widget.getType().getId() == 5) {
								String htmlurl = widget.getHtmlurl();
								String wholeHtmlUrl = null;
								if (htmlurl != null && !"".equals(htmlurl) && !"null".equals(htmlurl)) {
									if (htmlurl.startsWith("http")) {
										wholeHtmlUrl = htmlurl + ".zip";
									} else {
										wholeHtmlUrl = Config.URL + htmlurl + ".zip";
									}

									wholeUrlList.add(wholeHtmlUrl);
									urlList.add(htmlurl);
									widget.setHtmlurl(wholeHtmlUrl);

									c = DatabaseHelper.getInstance().query(SQLiteHelper.RESOURCE_TB_NAME,
											new String[] { "date", "name", "complete" }, "url=?",
											new String[] { htmlurl }, null, null, null);
									String zip_path;
									String html_path;
									if (c.moveToNext()) {
										zip_path = Config.DIR_CACHE + c.getString(c.getColumnIndex("name"));
										html_path = Config.DIR_CACHE + c.getString(c.getColumnIndex("date"))
												+ "/index.html";

										widget.setZipPath(zip_path);
										widget.setHtmlPath(html_path);
										resourcePathList.add(html_path);
									}
									c.close();
								}
							}

							for (Resource resource : resources) {
								String resUrl = resource.getResUrl();
								String wholeResUrl = null;

								if (resUrl.startsWith("http")) {
									wholeResUrl = resUrl;
								} else {
									wholeResUrl = Config.URL + resUrl;

								}
								wholeUrlList.add(wholeResUrl);
								urlList.add(resUrl);
								c = DatabaseHelper.getInstance().query(SQLiteHelper.RESOURCE_TB_NAME,
										new String[] { "name", "complete" }, "url=?", new String[] { resUrl }, null,
										null, null);
								String path;
								if (c.moveToNext()) {
									path = Config.DIR_CACHE + c.getString(c.getColumnIndex("name"));
									resourcePathList.add(path);
								}
								c.close();
							}
							widget.setResourcePathList(resourcePathList);
						}
						uITemplate.setWholeUrlList(wholeUrlList);
						uITemplate.setUrlList(urlList);
						if (!getIsContain(uITemplateList, uITemplate)) {
							// Log.i()
							uITemplateList.add(uITemplate);
						}
					}
				}
				programPublish.setuITemplateList(uITemplateList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return programPublish;

	}

	public static UITemplate getDefaultUITemplate() {
		UITemplate uITemplate = new UITemplate();
		uITemplate.setId(0);
		List<Widget> widgets = new ArrayList<Widget>();
		Widget widget = new Widget();
		Type type = new Type();
		type.setId(0);
		widget.setType(type);
		widget.setWidth(-2);
		widget.setHeight(-2);

		widgets.add(widget);
		uITemplate.setWidgets(widgets);
		return uITemplate;
	}

	public static String removeSpecialCharacter(String oldName) {
		String newName;
		// oldName.rem

		return oldName;

	}

	public static List<ProgramPublish> getImmediatelyProgramPublishList(List<ProgramPublish> programPublishList) {
		List<ProgramPublish> immediatelyProgramPublishList = new ArrayList<ProgramPublish>();
		if (programPublishList == null) {
			return null;
		}
		for (ProgramPublish programPublish : programPublishList) {
			if (programPublish.getRows().get(0).getStype() == 0) {
				immediatelyProgramPublishList.add(programPublish);
			}
		}
		if (immediatelyProgramPublishList.size() == 0) {
			return null;
		}

		return immediatelyProgramPublishList;

	}

	public static String getTheSamePublishMessageId(ProgramPublish programPublish,
			List<ProgramPublish> programPublishList) {
		for (ProgramPublish p : programPublishList) {
			if (p != null) {
				if(getIsSamePublish(programPublish, p)) {
					return p.getMessageId();
				}
			}

		}
		return null;

	}

	public static boolean getIsSamePublish(ProgramPublish p1, ProgramPublish p2) {
		Schedule schedule1 = p1.getRows().get(0);
		Schedule schedule2 = p2.getRows().get(0);

		if (getIsSameSchedule(schedule1, schedule2)) {
			return true;
		}
		return false;
	}

	public static boolean getIsSameSchedule(Schedule schedule1, Schedule schedule2) {
		if (schedule1.getId() == schedule2.getId() && schedule1.getStype() == schedule2.getStype()) {
			List<Scheduleoftime> scheduleoftimeList1 = schedule1.getScheduleoftimes();
			List<Scheduleoftime> scheduleoftimeList2 = schedule2.getScheduleoftimes();
			if (scheduleoftimeList1.size() == scheduleoftimeList2.size()) {
				for (int i = 0; i < scheduleoftimeList1.size(); i++) {
					if(!getIsSameScheduleoftime(scheduleoftimeList1.get(i),scheduleoftimeList2.get(i))) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean getIsSameScheduleoftime(Scheduleoftime scheduleoftime1, Scheduleoftime scheduleoftime2) {
		if(scheduleoftime1.getId()==scheduleoftime2.getId()&&scheduleoftime1.getStime().equals(scheduleoftime2.getStime())&&scheduleoftime1.getEtime().equals(scheduleoftime2.getEtime())) {
			
			List<UITemplate> templates1=scheduleoftime1.getTemplates();
			List<UITemplate> templates2=scheduleoftime2.getTemplates();
			if (templates1.size() == templates2.size()) {
				for (int i = 0; i < templates1.size(); i++) {
					if(!getIsSameUITemplate(templates1.get(i),templates2.get(i))) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean getIsSameUITemplate(UITemplate template1, UITemplate template2) {
		if(template1.getId()==template2.getId()) {
			if(template1.getVersion()==template2.getVersion()) {
			List<Widget> widgets1=template1.getWidgets();
			List<Widget> widgets2=template2.getWidgets();
			if (widgets1.size() == widgets2.size()) {
				for (int i = 0; i < widgets1.size(); i++) {
					if(!getIsSameWidget(widgets1.get(i),widgets2.get(i))) {
						return false;
					}
				}
				return true;
			}
		}
		}
		return false;
	}
	
	public static boolean getIsSameWidget(Widget widget1, Widget widget2) {
		if(widget1.getId()==widget2.getId()) {
			if(widget1.getStyle()==widget2.getStyle()) {
			List<Resource> resourceList1=widget1.getResources();
			List<Resource> resourceList2=widget2.getResources();
			if (resourceList1.size() == resourceList2.size()) {
				for (int i = 0; i < resourceList1.size(); i++) {
					if(!getIsSameResource(resourceList1.get(i),resourceList2.get(i))) {
						return false;
					}
				}
				return true;
			}
			
		}
		}
		return false;
		
	}
	
	
	public static boolean getIsSameResource(Resource resource1, Resource resource2) {
		if(resource1.getResUrl().equals(resource2.getResUrl())) {
			return true;
		}
		return false;
		
	}


	public static List<ProgramItem> getProgramItemList(Context context,ProgramPublish programPublish,String temStr) {
		PreferenceManager.init(context);
		if (programPublish == null) {
			return null;
		}
		List<ProgramItem> programItemList=null;
		String templatesId = programPublish.getMessageId();
		List<UITemplate> templateList = programPublish.getuITemplateList();
if(templateList!=null&&templateList.size()>0){

		if (FileUtil.getIsExists(Config.RESOURCE_INFO + templatesId)) {

			Log.i("connect", "�ý�Ŀ�б�:" + templatesId + "�Ѵ���");
			if (PreferenceManager.getInstance().getIsProgramListCompleteDownload(templatesId)) {
				Log.i("connect", "�ý�Ŀ�б�:" + templatesId + "���������");

				return null;
			} else {
				Log.i("connect", "�ý�Ŀ�б�:" + templatesId + "δ�������");

				programItemList = new ArrayList<ProgramItem>();

				FileUtil.createDirtory(Config.DIR_CACHE);
				Log.i("connect", "�������ص�ַ��" + Config.DIR_CACHE);


				for (int i = 0; i < templateList.size(); i++) {
					UITemplate template = templateList.get(i);
					if (PreferenceManager.getInstance().getIsProgramCompleteDownload(templatesId,
							template.getId() + "")) {
						Log.i("connect", "�ý�Ŀ��������ɣ�" + template.getId());
					} else {
						Log.i("connect", "�ý�Ŀδ������ɣ�" + template.getId());

						PreferenceManager.getInstance().setProgramName(template.getId() + "",
								template.getName());

						programItemList
								.add(new ProgramItem(template.getId(), template.getLoadItemList()));

					}

				}
			}
		} else {
			Log.i("connect", "�ý�Ŀ�б�:" + templatesId + "������");
			PreferenceManager.getInstance().setIsProgramListCompleteDownload(templatesId + "",
					false);

			programItemList = new ArrayList<ProgramItem>();

			FileUtil.createDirtory(Config.DIR_CACHE);
			FileUtil.writeFileSdcard(Config.RESOURCE_INFO + templatesId, temStr);// д�뱾���ļ����Ա�û��ʱʹ��

			for (int i = 0; i < templateList.size(); i++) {
				PreferenceManager.getInstance().setIsProgramCompleteDownload(templatesId + "",
						templateList.get(i).getId() + "", false);
				UITemplate template = templateList.get(i);
				PreferenceManager.getInstance().setProgramName(template.getId() + "",
						template.getName());

				List<LoadItem> loadItemList = template.getLoadItemList();
				for (int j = 0; j < loadItemList.size(); j++) {
					PreferenceManager.getInstance().setFileLength(loadItemList.get(j).getPath(), 0);
				}
				programItemList.add(new ProgramItem(template.getId(), loadItemList));
			}
		}
	}
		return programItemList;

	}




























	
	
	

	public static List<ProgramPublish> getTimerProgramPublishList(List<ProgramPublish> programPublishList) {
		List<ProgramPublish> timerProgramPublishList = new ArrayList<ProgramPublish>();
		if (programPublishList == null) {
			return null;
		}
		for (ProgramPublish programPublish : programPublishList) {
			if (programPublish.getRows().get(0).getStype() == 1) {
				timerProgramPublishList.add(programPublish);
			}
		}
		if (timerProgramPublishList.size() == 0) {
			return null;
		}
		return timerProgramPublishList;

	}

	public static boolean getIsContain(List<UITemplate> uITemplates, UITemplate uITemplate) {
		for (UITemplate template : uITemplates) {
			if (template.getId() == uITemplate.getId()) {
				return true;
			}
		}
		return false;
	}

	public static String getFilePath(String path) {
		return Config.DIR_CACHE + FileUtil.getFileNameByUrl(path);
	}

	public static String getFilePath(String path, String playIndex) {
		return Config.DIR_CACHE + playIndex + "/" + FileUtil.getFileNameByUrl(path);
	}

}
