package util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import bean.ProgramPublish;
import bean.UITemplate;
import config.Config;
import helper.PreferenceManager;
import helper.TemplateHelper;
import sql.DatabaseHelper;
import sql.SQLiteHelper;

public class FileUtil {
	// static File FILE_CACHE = new File(Config.DIR_CACHE);
	// static File FILE_RESOURCE = new File(Config.RESOURCE_INFO);
	// static String[] cacheFileNameArr, resourceFileNameArr;
	// static List<String> cacheFileNameList, resourceFileNameList;

	/*
	 * public static String getFilePath(String path) { return Config.DIR_CACHE +
	 * getFileNameByUrl(path); }
	 */

	public static String getPath(Context context, Uri uri) {

		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it
			}
		}

		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static void deleteFile(File file) { // ɾ���ļ��Լ��������ļ�
		if (!file.exists()) {
			return;
		} else {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0) {
					file.delete();
					return;
				}
				for (File f : childFile) {
					deleteFile(f);
				}
				file.delete();
			}
		}
	}

	// ����Ŀ¼
	public static void createDirtory(String dirName) {
		File f = new File(dirName);
		if (!f.exists()) {
			String[] dirArray = dirName.split("/");
			String temp = "";
			for (int i = 0; i < dirArray.length; i++) {
				temp += dirArray[i].trim() + "/";
				f = new File(temp);
				if (!f.exists()) {
					f.mkdir();
				} else if (f.isFile()) {
					f.delete();
					f.mkdir();
				}
			}
		}
	}

	public static String getFileNameByUrl(String _urlStr) {
		// Log.i("FileUtil.getFileNameByUrl",_urlStr);
		return _urlStr.substring(_urlStr.lastIndexOf("/") + 1);
	}

	public static void writeFileSdcard(String fileName, String message) {
		File file = new File(fileName);
		/*
		 * if (file.isDirectory()) { deleteAll(file); }
		 * 
		 * if (!file.getParentFile().exists()) { try { file.getParentFile().mkdirs(); //
		 * ��ָ�����ļ����д����ļ� file.createNewFile(); } catch (Exception e) { } } else if
		 * (file.getParentFile().isFile()) { file.getParentFile().delete();
		 * file.getParentFile().mkdir(); }
		 */
		createFile(file);

		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = message.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String readFileSdcard(String fileName) {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);

			res = new String(buffer);

			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public static String convertCodeAndGetText(String str_filepath) {// ת��

		File file = new File(str_filepath);
		BufferedReader reader;
		String text = "";
		try {
			// FileReader f_reader = new FileReader(file);
			// BufferedReader reader = new BufferedReader(f_reader);
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream in = new BufferedInputStream(fis);
			in.mark(4);
			byte[] first3bytes = new byte[3];
			in.read(first3bytes);// �ҵ��ĵ���ǰ�����ֽڲ��Զ��ж��ĵ����͡�
			in.reset();
			if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB && first3bytes[2] == (byte) 0xBF) {// utf-8

				reader = new BufferedReader(new InputStreamReader(in, "utf-8"));

			} else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFE) {

				reader = new BufferedReader(new InputStreamReader(in, "unicode"));
			} else if (first3bytes[0] == (byte) 0xFE && first3bytes[1] == (byte) 0xFF) {

				reader = new BufferedReader(new InputStreamReader(in, "utf-16be"));
			} else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFF) {

				reader = new BufferedReader(new InputStreamReader(in, "utf-16le"));
			} else {

				reader = new BufferedReader(new InputStreamReader(in, "GBK"));
			}
			String str = reader.readLine();

			while (str != null) {
				text = text + str + "/n";
				str = reader.readLine();

			}
			reader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	/**
	 * �ж��ļ��Ƿ����
	 * 
	 * @return
	 */
	public static boolean fileIsExists(String FileName) {
		try {

			File f = new File(FileName);

			if (!f.exists()) {

				return false;

			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String getFilePath(String programId) {
		File file = new File(Config.DIR_CACHE);
		if (file.exists()) {
			if (file.isDirectory()) {
				if (file.list().length > 0) {
					for (String fileName : file.list()) {

						if (fileName.substring(fileName.indexOf("_") + 1).equals(programId)) {
							return fileName;
						}
					}
				}
			}
		}
		return null;

	}

	/*
	 * public static String getRecentlyProgram() { File FILE_CACHE = new
	 * File(Config.DIR_CACHE); long l = -1; String recentlyProgram = null; if
	 * (FILE_CACHE.exists()) { if (FILE_CACHE.isDirectory()) { if
	 * (FILE_CACHE.list().length > 0) { for (String fileName : FILE_CACHE.list()) {
	 * if (l < Long.parseLong(fileName.substring(0, fileName.indexOf("_")))) { l =
	 * Long.parseLong(fileName.substring(0, fileName.indexOf("_"))); recentlyProgram
	 * = fileName; } } } } } return recentlyProgram;
	 * 
	 * }
	 */

	public static boolean getIsProgramContain(String programId) {
		File FILE_CACHE = new File(Config.DIR_CACHE);
		if (programId != null && !programId.equals("")) {

			if (FILE_CACHE.exists()) {
				if (FILE_CACHE.isDirectory()) {
					if (FILE_CACHE.list().length > 0) {
						for (String cache_fileName : FILE_CACHE.list()) {
							String str = cache_fileName.substring(cache_fileName.indexOf("_") + 1);
							if (programId.equals(str)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public static int deleteDatabase(Context context) {
		DatabaseHelper.init(context);
		DatabaseHelper.getInstance().delete(SQLiteHelper.RESOURCE_TB_NAME, null, null);

		return 0;

	}

	public static int deleteUnuseDatabase(Context context) {
		DatabaseHelper.init(context);
		ContentValues values = new ContentValues();
		values.put("state", 0);
		DatabaseHelper.getInstance().update(SQLiteHelper.RESOURCE_TB_NAME, values, null, null);
		

		List<String> programListPathList = getProgramListList(context);
		File resourceFile=new File(Config.RESOURCE_INFO);
				File downloadFile=new File(Config.DIR_CACHE);
		
		if (programListPathList == null) {
			
			deleteFile(resourceFile);
			deleteFile(downloadFile);
			return DatabaseHelper.getInstance().delete(SQLiteHelper.RESOURCE_TB_NAME, null, null);
			
		}
		File[] resourceFileArr = resourceFile.listFiles();
		for (File resourceChildFile : resourceFileArr) {
			boolean isContain = false;
			for (String resourceChildFileName : programListPathList) {
				if (resourceChildFile.getName().equals(resourceChildFileName)) {
					isContain = true;
				}
			}
			if(!isContain) {
				deleteFile(resourceChildFile);
			}
		}


		List<ProgramPublish> ProgramPublishList = getProgramListList(context, programListPathList);
		if (ProgramPublishList == null) {

			return DatabaseHelper.getInstance().delete(SQLiteHelper.RESOURCE_TB_NAME, null, null);
		}
		for (ProgramPublish programPublish : ProgramPublishList) {
			List<UITemplate> uITemplateList = programPublish.getuITemplateList();
			for (UITemplate uITemplate : uITemplateList) {
				List<String> urlList = uITemplate.getUrlList();
				for (String url : urlList) {
					values.clear();
					values.put("state", 1);
					DatabaseHelper.getInstance().update(SQLiteHelper.RESOURCE_TB_NAME, values, "url=?",
							new String[] { url });
				}

			}
		}

		DatabaseHelper.getInstance().delete(SQLiteHelper.RESOURCE_TB_NAME, "state=?", new String[] { "0" });
		
		Cursor c = DatabaseHelper.getInstance().query(SQLiteHelper.RESOURCE_TB_NAME, new String[] { "name" }, "state=?",
				new String[] { "1" }, null, null, null);
		File[] downloadFileArr = downloadFile.listFiles();
		List<String> downloadFileNameList=new ArrayList<String>();
		while (c.moveToNext()) {
			String name = c.getString(c.getColumnIndex("name"));
			downloadFileNameList.add(name);
		}
		int count = 0;
		for(File downloadChildFile:downloadFileArr) {
			boolean isContain=false;
			for(String downloadChildFileName:downloadFileNameList) {
				if(downloadChildFile.getName().equals(downloadChildFileName)) {
					isContain=true;
				}else {
					if(downloadChildFileName.endsWith(".zip")&&downloadChildFile.isDirectory()) {
						String downloadChildFileNameStart=downloadChildFileName.substring(0,downloadChildFileName.lastIndexOf("."));
						if(downloadChildFile.getName().equals(downloadChildFileNameStart)) {
							isContain=true;
						}
					}
					
				}
				
			}
			if(!isContain) {
				deleteFile(downloadChildFile);
				count++;
			}
			
		}
		
		
		
	/*	int count = 0;
		
		while (c.moveToNext()) {
			
			String name = c.getString(c.getColumnIndex("name"));
			
			if (name.endsWith(".zip")) {
				String unZipName = name.substring(0, name.lastIndexOf("."));
				deleteFile(new File(Config.DIR_CACHE + unZipName));
			}
			deleteFile(new File(Config.DIR_CACHE + name));
			count++;
		}*/
		
		
		
		
		
		
		
		
		c.close();
		return count;

	}

	public static String getRecentlyProgramListPath(Context context) {

		File FILE_CACHE = new File(Config.DIR_CACHE);
		File FILE_RESOURCE = new File(Config.RESOURCE_INFO);
		PreferenceManager.init(context);

		String recentlyProgramListPath = PreferenceManager.getInstance().getTemplatesId();

		if (recentlyProgramListPath == null || "".equals(recentlyProgramListPath)) {
			return null;
		} else {

			if (new File(Config.RESOURCE_INFO + recentlyProgramListPath).exists()
					&& new File(Config.DIR_CACHE).exists()) {
				return recentlyProgramListPath;
			} else {

				deleteFile(new File(Config.RESOURCE_INFO + recentlyProgramListPath));

				PreferenceManager.getInstance().setIsProgramListCompleteDownload(recentlyProgramListPath, false);
				deleteUnuseDatabase(context);
			}

		}

		if (FILE_RESOURCE.exists()) {
			if (FILE_CACHE.exists()) {
				if (FILE_CACHE.isDirectory()) {
					String[] fileNameArr = FILE_RESOURCE.list();
					int size = fileNameArr.length;
					for (int m = 0; m < size; m++) {
						long l = 0;
						try {
							l = Long.parseLong(fileNameArr[m]);
						} catch (Exception e) {

						}
						if (l == 0) {
							String temp = fileNameArr[m];
							fileNameArr[m] = fileNameArr[size - 1];
							fileNameArr[size - 1] = temp;
							size--;
						}
					}

					for (int i = 0; i < size - 1; i++) {
						for (int j = size - 1; j > i; j--) {
							int compare = compareTo(fileNameArr[j - 1], fileNameArr[j]);
							String temp = "";
							switch (compare) {
							case 1:
								break;
							case 2:
								temp = fileNameArr[j - 1];
								fileNameArr[j - 1] = fileNameArr[j];
								fileNameArr[j] = temp;
								break;
							case 3:
								temp = fileNameArr[j - 1];
								fileNameArr[j - 1] = fileNameArr[size - 1];
								fileNameArr[size - 1] = temp;
								size--;
								break;
							case 4:
								temp = fileNameArr[j];
								fileNameArr[j] = fileNameArr[size - 1];
								fileNameArr[size - 1] = temp;
								size--;
								break;
							case 5:
								temp = fileNameArr[j - 1];
								fileNameArr[j - 1] = fileNameArr[size - 2];
								fileNameArr[size - 2] = temp;

								temp = fileNameArr[j];
								fileNameArr[j] = fileNameArr[size - 1];
								fileNameArr[size - 1] = temp;

								size--;
								size--;
								break;

							}
						}
					}
					for (String fileName : fileNameArr) {
						if (PreferenceManager.getInstance().getIsProgramListCompleteDownload(fileName)) {
							return fileName;
						} else {
						}
					}

				}
			}
		}

		return null;

	}

	public static int compareTo(String str1, String str2) {
		long l1 = 0;
		long l2 = 0;
		try {
			l1 = Long.parseLong(str1);
		} catch (Exception e) {
		}
		try {
			l2 = Long.parseLong(str2);
		} catch (Exception e) {
		}

		if (l1 == 0 && l2 == 0) {
			return 5;
		}

		if (l1 == 0) {
			return 3;
		}
		if (l2 == 0) {
			return 4;
		}
		if (l1 > l2) {
			return 1;
		} else {
			return 2;
		}

	}

	public static boolean deleteAll(File file) {

		if (file.exists()) {
			while (!file.delete()) {
				if (file.isDirectory()) {
					for (File f : file.listFiles()) {
						if (!deleteAll(f)) {

							return false;
						}
					}
					try {
						new Thread().sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return true;
		} else {
			return true;
		}

	}

	public static String getEarliestProgramPath() {
		File FILE_CACHE = new File(Config.DIR_CACHE);
		String earliestProgramPath = "";
		long earliestTime = Long.MAX_VALUE;
		for (String fileName : FILE_CACHE.list()) {
			if (earliestTime > Long.parseLong(fileName.substring(0, fileName.indexOf("_")))) {
				earliestTime = Long.parseLong(fileName.substring(0, fileName.indexOf("_")));
				earliestProgramPath = fileName;
			}
		}

		if (earliestProgramPath == null || earliestProgramPath.equals("")) {
			return null;
		}

		return Config.DIR_CACHE + earliestProgramPath;

	}

	/*
	 * public static String getLowestPlayCountProgramPath(Context context) { File
	 * FILE_RESOURCE = new File(Config.RESOURCE_INFO);
	 * PreferenceManager.init(context); long playCount = -1; List<String>
	 * resourceAllFileNameList = new ArrayList<String>(); List<String>
	 * resourceFileNameList = new ArrayList<String>(); for (String resourcefileName
	 * : FILE_RESOURCE.list()) { if
	 * (!resourcefileName.equals(Config.currentProgram.substring(Config.
	 * currentProgram.indexOf("_") + 1))) {
	 * resourceAllFileNameList.add(resourcefileName); } }
	 * 
	 * for (String fileName : resourceAllFileNameList) { if (playCount <
	 * PreferenceManager.getInstance().getPlayCount(fileName)) {
	 * resourceFileNameList.clear(); playCount =
	 * PreferenceManager.getInstance().getPlayCount(fileName);
	 * resourceFileNameList.add(fileName); } else if (playCount ==
	 * PreferenceManager.getInstance().getPlayCount(fileName)) {
	 * resourceFileNameList.add(fileName); } } if (resourceFileNameList.size() > 0)
	 * {
	 * 
	 * return getEarliestProgramPath(resourceFileNameList);
	 * 
	 * }
	 * 
	 * return null;
	 * 
	 * }
	 */

	public static String getEarliestProgramPath(List<String> resourceFileList) {
		File FILE_CACHE = new File(Config.DIR_CACHE);
		List<String> cacheFileNameList = new ArrayList<String>();
		for (String resourceFileName : resourceFileList) {
			for (String cacheFileName : FILE_CACHE.list()) {
				if (cacheFileName.substring(cacheFileName.indexOf("_") + 1).equals(resourceFileName)) {
					cacheFileNameList.add(cacheFileName);
					break;
				}
			}
		}
		return Config.DIR_CACHE + getEarliestProgram(cacheFileNameList);

	}

	public static String getEarliestProgram(List<String> cacheFileNameList) {
		long l = Long.MAX_VALUE;
		String EarliestProgram = null;
		if (cacheFileNameList.size() > 0) {
			for (String fileName : cacheFileNameList) {
				if (l > Long.parseLong(fileName.substring(0, fileName.indexOf("_")))) {
					l = Long.parseLong(fileName.substring(0, fileName.indexOf("_")));
					EarliestProgram = fileName;
				}
			}
		}
		return EarliestProgram;

	}

	/*
	 * public static List<String> getProgramList(String recentlyProgramListPath) {
	 * File programListFile = new File(Config.DIR_CACHE+recentlyProgramListPath);
	 * List<String> cacheFileNameList = new ArrayList<String>(); String[]
	 * cacheFileNameArr = programListFile.list(); for (String cacheFileName :
	 * cacheFileNameArr) { cacheFileNameList.add(cacheFileName); } return
	 * cacheFileNameList; }
	 */

	public static boolean createFile(File file) {
		String filePath = file.getAbsolutePath();
		String[] filePathArr = filePath.split("/");
		String newFilePath = "";
		File f;
		for (String str : filePathArr) {
			newFilePath = newFilePath + "/" + str;
			f = new File(newFilePath);
			if (f.getAbsolutePath().equals(file.getAbsolutePath())) {
				if (f.exists()) {
					if (f.isDirectory()) {
						deleteAll(f);
						try {
							if (f.createNewFile()) {
								return true;
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						if (f.delete()) {
							try {
								if (f.createNewFile()) {
									return true;
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				} else {
					try {
						if (f.createNewFile()) {
							return true;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} else {
				if (f.exists()) {
					if (f.isDirectory()) {

					} else {
						if (f.delete()) {
							if (file.getParentFile().mkdirs()) {
								try {
									if (file.createNewFile()) {
										return true;
									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				} else {
					if (file.getParentFile().mkdirs()) {
						try {
							if (file.createNewFile()) {
								return true;
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		return false;

	}

	public static boolean createDirectory(File file) {
		String filePath = file.getAbsolutePath();
		String[] filePathArr = filePath.split("/");
		String newFilePath = "";
		File f;
		for (String str : filePathArr) {
			newFilePath = newFilePath + "/" + str;
			f = new File(newFilePath);
			if (f.getAbsolutePath().equals(file.getAbsolutePath())) {
				if (f.exists()) {
					if (f.isDirectory()) {
						return true;

					} else {
						if (f.delete()) {
							if (f.mkdirs()) {
								return true;
							}
						}
					}
				} else {
					if (f.mkdirs()) {
						return true;
					}
				}

			} else {
				if (f.exists()) {
					if (f.isDirectory()) {

					} else {
						if (f.delete()) {
							if (file.mkdirs()) {
								return true;
							}
						}
					}
				} else {
					if (file.mkdirs()) {
						return true;
					}
				}
			}
		}
		return false;

	}

	/*
	 * public static boolean deleteProgramKeepDirectory(String[] programIdArr) { for
	 * (String resrourceName : programIdArr) { String cacheName =
	 * getFilePath(resrourceName);
	 * Log.i("connect","FileUtil.deleteProgram.cacheName="+cacheName);
	 * if(cacheName!=null) { if (!cacheName.equals(Dms.currentProgramPath)) {
	 * Log.i("connect","(!cacheName.equals(Dms.currentProgramPath)  cacheName="
	 * +cacheName+",Dms.currentProgramPath="+Dms.currentProgramPath); File
	 * file_resource = new File(Config.RESOURCE_INFO + resrourceName); if
	 * (file_resource.exists()) {
	 * Log.i("connect",file_resource.getAbsolutePath()+"�Ѵ���");
	 * if(!deleteAll(file_resource)) { Log.i("connect","a"); return false; } }
	 * 
	 * File file_cache = new File(Config.DIR_CACHE + cacheName); if
	 * (file_cache.exists()) { if(!deleteAll(file_cache)) { Log.i("connect","c");
	 * return false; }else { Log.i("connect","d"); } }
	 * 
	 * } } } return true;
	 * 
	 * }
	 */
	/*
	 * public static boolean deleteProgram(String[] programIdArr) { for (String
	 * resrourceName : programIdArr) { String cacheName =
	 * getFilePath(resrourceName); Log.i("connect",
	 * "FileUtil.deleteProgram.cacheName=" + cacheName); if (cacheName != null) { if
	 * (!cacheName.equals("")) { Log.i("connect",
	 * "(!cacheName.equals(Dms.currentProgramPath)  cacheName=" + cacheName +
	 * ",Dms.currentProgramPath=" + ""); File file_resource = new
	 * File(Config.RESOURCE_INFO + resrourceName); if (file_resource.exists()) {
	 * Log.i("connect", file_resource.getAbsolutePath() + "�Ѵ���"); if
	 * (!deleteAll(file_resource)) { Log.i("connect", "a"); return false; } }
	 * 
	 * File file_cache = new File(Config.DIR_CACHE + cacheName); if
	 * (file_cache.exists()) { if (!deleteAll(file_cache)) { Log.i("connect", "c");
	 * return false; } else { Log.i("connect", "d"); } }
	 * 
	 * } } } return true;
	 * 
	 * }
	 */

	public static String getOtherProgramPath(String program) {
		File FILE_CACHE = new File(Config.DIR_CACHE);

		String[] cacheFileNameArr = FILE_CACHE.list();
		for (String programFileName : cacheFileNameArr) {
			String otherProgramId = programFileName.substring(programFileName.indexOf("_") + 1);
			String currentProgramId = program.substring(program.indexOf("_") + 1);
			if (otherProgramId.equals(currentProgramId) && !programFileName.equals(program)) {
				return FILE_CACHE.getAbsolutePath() + "/" + programFileName;
			}
		}

		return null;

	}

	public static void deleteOtherProgram(String fileName) {

		File FILE_CACHE = new File(Config.DIR_CACHE);
		File FILE_RESOURCE = new File(Config.RESOURCE_INFO);

		if (FILE_CACHE.exists()) {

			String[] cacheFileNameArr = FILE_CACHE.list();
			for (String cache_childName : cacheFileNameArr) {
				if (!cache_childName.equals(fileName)) {
					deleteAll(new File(Config.DIR_CACHE + cache_childName));
				}
			}
		}

		/*
		 * if (FILE_RESOURCE.exists()) {
		 * 
		 * String[] resourceFileNameArr = FILE_RESOURCE.list(); String
		 * resource_fileName=fileName.substring(fileName.indexOf("_")+1); for (String
		 * resource_childName : resourceFileNameArr) { if
		 * (!resource_childName.equals(resource_fileName)) { deleteAll(new
		 * File(Config.RESOURCE_INFO + resource_childName)); } }
		 * 
		 * }
		 */

	}

	public static void getOtherProgramListFile() {
		// List<File> fileList = new ArrayList<File>();

		File FILE_CACHE = new File(Config.DIR_CACHE);
		File[] fileArr = FILE_CACHE.listFiles();
		long l1 = -1;
		long l2 = -1;
		for (int i = 0; i < fileArr.length - 1; i++) {
			try {
				l1 = Long.parseLong(fileArr[i].getName().substring(0, fileArr[i].getName().indexOf("_")));
			} catch (Exception e) {
				deleteAll(fileArr[i]);
				l1 = -1;
			}
			try {
				l2 = Long.parseLong(fileArr[i + 1].getName().substring(0, fileArr[i + 1].getName().indexOf("_")));
			} catch (Exception e) {
				deleteAll(fileArr[i + 1]);
				l2 = -1;
			}
			if (l1 < l2) {
				deleteAll(fileArr[i]);
			} else if (l1 > l2) {
				deleteAll(fileArr[i + 1]);
			}

		}

	}

	public static String getFilePathById(String programId) {
		String targetName = null;
		if (programId != null) {
			File FILE_CACHE = new File(Config.DIR_CACHE);
			String[] FileNameArr = FILE_CACHE.list();

			for (String fileName : FileNameArr) {
				String file_containId = fileName.substring(fileName.indexOf("_") + 1);
				if (programId.equals(file_containId)) {
					if (targetName == null) {
						targetName = fileName;
					} else {
						long l1 = Long.parseLong(targetName.substring(0, targetName.indexOf("_")));
						long l2 = Long.parseLong(fileName.substring(0, fileName.indexOf("_")));
						if (l1 < l2) {
							targetName = fileName;
						}
					}

				}
			}

		}

		return targetName;

	}

	public static String getRecentlyTemplateListPath() {
		File FILE_CACHE = new File(Config.DIR_CACHE);
		String[] fileNameArr = FILE_CACHE.list();
		long l = 0;
		for (String fileName : fileNameArr) {
			long newL = 0;
			try {
				newL = Long.parseLong(fileName);
			} catch (Exception e) {
				deleteAll(new File(Config.DIR_CACHE + fileName));
			}
			if (l < newL) {
				l = newL;
			}
		}
		if (l == 0) {
			return null;
		}
		return l + "";
	}

	public static boolean getIsExists(String path) {
		File file = new File(path);
		return file.exists();
	}

	public static String[] getPlayArr() {
		String path = getRecentlyTemplateListPath();
		String[] playArr = null;
		if (path != null) {
			File file = new File(Config.DIR_CACHE + path);
			playArr = file.list();
			if (playArr != null && playArr.length != 0) {
				for (int i = 0; i < playArr.length; i++) {
					playArr[i] = path + "/" + playArr[i];
				}
			}

		}

		return playArr;
	}

	public static String deleteOtherTemplateList(String path) {
		File FILE_CACHE = new File(Config.DIR_CACHE);
		// File FILE_RESOURCE = new File(Config.RESOURCE_INFO);
		// String path = getRecentlyTemplateListPath();
		String[] fileNameArr = FILE_CACHE.list();
		if (fileNameArr != null) {
			for (String fileName : fileNameArr) {
				if (!fileName.equals(path)) {
					deleteAll(new File(Config.DIR_CACHE + fileName));
					deleteAll(new File(Config.RESOURCE_INFO + fileName));
				}
			}
		}

		return null;

	}

	public static List<String> getProgramListList(Context context) {
		List<String> programListList = null;
		File FILE_CACHE = new File(Config.DIR_CACHE);
		File FILE_RESOURCE = new File(Config.RESOURCE_INFO);

		PreferenceManager.init(context);
		if (FILE_RESOURCE.exists()) {
			if (FILE_CACHE.exists()) {
				if (FILE_CACHE.isDirectory()) {

					programListList = new ArrayList<String>();

					String[] fileNameArr = FILE_RESOURCE.list();
					int size = fileNameArr.length;
					for (int m = 0; m < size; m++) {
						long l = -1;
						try {
							l = Long.parseLong(fileNameArr[m]);
						} catch (Exception e) {
						}
						if (l == -1) {
							String temp = fileNameArr[m];
							fileNameArr[m] = fileNameArr[size - 1];
							fileNameArr[size - 1] = temp;
							size--;
						}
					}
					for (int i = 0; i < size - 1; i++) {
						for (int j = size - 1; j > i; j--) {
							int compare = compareTo(fileNameArr[j - 1], fileNameArr[j]);
							String temp = "";
							switch (compare) {
							case 1:
								break;
							case 2:
								temp = fileNameArr[j - 1];
								fileNameArr[j - 1] = fileNameArr[j];
								fileNameArr[j] = temp;
								break;
							case 3:
								temp = fileNameArr[j - 1];
								fileNameArr[j - 1] = fileNameArr[size - 1];
								fileNameArr[size - 1] = temp;
								size--;
								break;
							case 4:
								temp = fileNameArr[j];
								fileNameArr[j] = fileNameArr[size - 1];
								fileNameArr[size - 1] = temp;
								size--;
								break;
							case 5:
								temp = fileNameArr[j - 1];
								fileNameArr[j - 1] = fileNameArr[size - 2];
								fileNameArr[size - 2] = temp;

								temp = fileNameArr[j];
								fileNameArr[j] = fileNameArr[size - 1];
								fileNameArr[size - 1] = temp;
								size--;
								size--;
								break;
							}
						}
					}
					for (String fileName : fileNameArr) {
						if (PreferenceManager.getInstance().getIsProgramListCompleteDownload(fileName)) {
							Log.i("mytest_hl", "FileUtil fileName = "+fileName);
							programListList.add(fileName);
						} else {
							Log.i("mytest_hl", "FileUtil δ������� fileName = "+fileName);
						}
					}
					if (programListList.size() == 0) {
						return null;
					}
				}
			}
		}
		return programListList;
	}

	public static List<String> getProgramList(String programListList) {
		List<String> programList = null;
		String[] fileNameArr;
		File FILE_CACHE = new File(Config.DIR_CACHE + programListList);
		File FILE_RESOURCE = new File(Config.RESOURCE_INFO);
		if (FILE_RESOURCE.exists()) {
			if (FILE_CACHE.exists()) {
				if (FILE_CACHE.isDirectory()) {
					programList = new ArrayList<String>();
					fileNameArr = FILE_CACHE.list();
					if (fileNameArr != null && fileNameArr.length > 0) {
						for (String fileName : fileNameArr) {
							programList.add(fileName);
						}
					} else {
						return null;
					}
				}
			}
		}

		return programList;

	}

	public static List<ProgramPublish> getProgramListList(Context context, List<String> programListList) {

		List<ProgramPublish> programPublishList = new ArrayList<ProgramPublish>();
		if (programListList == null) {

		} else {

			for (String fileName : programListList) {

				String content = FileUtil.readFileSdcard(Config.RESOURCE_INFO + fileName);
				// Log.i("mytest", "FileUtil fileName="+fileName+",content="+content);
				ProgramPublish programPublish = TemplateHelper.getTemplate(context, content, 1);
				if (programPublish != null) {
					// Log.i("mytest", "FileUtil
					// programPublish.getMessageId()="+programPublish.getMessageId());
					programPublishList.add(programPublish);
				}

			}
		}
		if (programPublishList.size() == 0) {
			return null;
		}
		return programPublishList;
	}

	public static boolean checkUpdateResource(Context context) {
		PreferenceManager.init(context);
		String recentlyDownloadProgramPublish = PreferenceManager.getInstance().getRecentlyDownloadProgramPublish();
		Log.i("connect","FileUtil.recentlyDownloadProgramPublish="+recentlyDownloadProgramPublish);
		if (recentlyDownloadProgramPublish == null || "".equals(recentlyDownloadProgramPublish)) {
			return false;

		} else {
			if (new File(Config.RESOURCE_INFO + recentlyDownloadProgramPublish).exists()) {
				if (PreferenceManager.getInstance().getIsProgramListCompleteDownload(recentlyDownloadProgramPublish)) {
					return false;
				} else {
					return true;
				}

			} else {
				return false;
			}
		}
	}
}
