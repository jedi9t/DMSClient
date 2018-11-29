package util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import android.util.Log;

public class UnZipUtil {

	public static synchronized boolean unzip(String zipFileName, String extPlace)

			throws Exception {

		return unZipFiles(zipFileName, extPlace);

	}

	/**
	 * ��ѹzip��ʽ��ѹ���ļ���ָ��λ��
	 * @param zipFileName
	 *            ѹ���ļ�
	 * @param extPlace
	 *            ��ѹĿ¼
	 * @throws Exception
	 */

	public static boolean unZipFiles(String zipFileName, String extPlace)

			throws Exception {

		// System.setProperty("sun.zip.encoding",

		// System.getProperty("sun.jnu.encoding"));

		try {

			(new File(extPlace)).mkdirs();

			
			File f = new File(extPlace + zipFileName);
			
			String outputName=null;
			if(zipFileName.contains(".")) {
				outputName=zipFileName.substring(0,zipFileName.indexOf("."));
			}

			@SuppressWarnings("resource")

	//		ZipFile zipFile = new ZipFile(extPlace + zipFileName,Charset.forName("GBK")); // ע��һ��Ҫ�������·�������ѹ����
	//		ZipFile zipFile = new ZipFile(extPlace + zipFileName,"GBK");
			ZipFile zipFile = new ZipFile(extPlace + zipFileName,"GBK");
			if ((!f.exists()) && (f.length() <= 0)) {

				throw new Exception("Ҫ��ѹ���ļ�������!");

			}

			String strPath, gbkPath, strtemp;

			File tempFile = new File(extPlace+outputName);

		

			strPath = tempFile.getAbsolutePath();
			if(tempFile.exists()) {
				Log.i("connect","tempFile is Exist �½���ѹ�ļ�" + strPath);
			}else {
				tempFile.mkdirs();
			}

			Enumeration<?> e = zipFile.getEntries();

			while (e.hasMoreElements()) {

				ZipEntry zipEnt = (ZipEntry) e.nextElement();

				gbkPath = zipEnt.getName();

				if (zipEnt.isDirectory()) {

					strtemp = strPath + File.separator + gbkPath;

					File dir = new File(strtemp);

					dir.mkdirs();

					continue;

				} else {

					// ��д�ļ�

					InputStream is = zipFile.getInputStream(zipEnt);

					BufferedInputStream bis = new BufferedInputStream(is);

					gbkPath = zipEnt.getName();

					strtemp = strPath + File.separator + gbkPath;

					// ��Ŀ¼

					String strsubdir = gbkPath;

					for (int i = 0; i < strsubdir.length(); i++) {

						if (strsubdir.substring(i, i + 1)

								.equalsIgnoreCase("/")) {

							String temp = strPath + File.separator

									+ strsubdir.substring(0, i);

							File subdir = new File(temp);

							if (!subdir.exists())

								subdir.mkdir();

						}

					}

					FileOutputStream fos = new FileOutputStream(strtemp);

					BufferedOutputStream bos = new BufferedOutputStream(fos);

					int c;

					while ((c = bis.read()) != -1) {

						bos.write((byte) c);

					}

					bos.close();

					fos.close();

				}

			}

			System.out.println("tempFile is Exist unzip success !");

			return true;

		} catch (Exception e) {

			e.printStackTrace();

			System.out.println("tempFile is Exist unzip fail !");

			return false;

		}

	}

}
