package util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {
	public static Bitmap loadImageFromUrl(String path,int width,int height)
	     {
	 
	         URL url;
	         InputStream is = null;
	         BufferedInputStream bis = null;
	         ByteArrayOutputStream out = null;
	 
	         if (path == null) {
	        	 return null;
	         }
	         
	         try{
	        	 is = new FileInputStream(path);
	             bis = new BufferedInputStream(is, 1024 * 4);
	             out = new ByteArrayOutputStream();
	             
	             int len = 0;
	 byte[] isBuffer=new byte[1024];
	             while ((len = bis.read(isBuffer)) != -1)
	             {
	                 out.write(isBuffer, 0, len);
	             }
	             out.close();
	             bis.close();
	         } catch (MalformedURLException e1){
	             e1.printStackTrace();
	             return null;
	         } catch (IOException e){
	             e.printStackTrace();
	         }
	         
	         if (out == null) {
	        	 return null;
	         }
	         
	         byte[] data = out.toByteArray();
	         BitmapFactory.Options options = new BitmapFactory.Options();
	         options.inJustDecodeBounds = true;
	         BitmapFactory.decodeByteArray(data, 0, data.length, options);
             options.inJustDecodeBounds = false;
	   /*     int be = (int) (options.outHeight / (float) sc);
           if (be <= 0){
	             be = 1;
	         } else if (be > 3){
	             be = 3;
	         }*/
             int sc=calculateInSampleSize(options, width, height);
           
	         options.inSampleSize =sc;
	         Bitmap bmp =null;
	         try
	         {
	             bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options); //��������ͼ
	         } catch (OutOfMemoryError e)
	         {
	             // TODO: handle exception
	         //    MainActivity.print("Tile Loader (241) Out Of Memory Error " + e.getLocalizedMessage()); 
         
	             System.gc();
	             bmp =null;
	         }
	         return bmp;
	     }
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

			while ((height / inSampleSize) > reqHeight || (width / inSampleSize) > reqWidth) {
				if(inSampleSize>8) {
					inSampleSize += 8;
				}else {
					inSampleSize *= 2;
				}
			}
		return inSampleSize;
	}
}
