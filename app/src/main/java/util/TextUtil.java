package util;

public class TextUtil {
public static String getColorStr(String rgbStr) {
	String subStr=rgbStr.substring(rgbStr.indexOf("(")+1,rgbStr.length()-1);
	String[] strArr=subStr.split(",");

	int r=Integer.parseInt(strArr[0].trim());
	int g=Integer.parseInt(strArr[1].trim());
	int b=Integer.parseInt(strArr[2].trim());
	String hexR;
	String hexG;
	String hexB;
		if(r<16) {
			hexR="0"+Integer.toHexString(r);
		}else {
			hexR=Integer.toHexString(r);
		}
		if(g<16) {
			hexG="0"+Integer.toHexString(g);
		}else {
			hexG=Integer.toHexString(g);
		}
		if(b<16) {
			hexB="0"+Integer.toHexString(b);
		}else {
			hexB=Integer.toHexString(b);
		}
	
	return "#"+hexR+hexG+hexB;
	
}
}
