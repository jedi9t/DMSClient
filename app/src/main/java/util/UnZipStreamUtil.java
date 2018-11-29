package util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class UnZipStreamUtil {
	    
	   /* String filename=null;
	    String unzipath=null;
	    public unzip(String filename,String unzipath)
	    {
	        this.filename=filename;
	        this.unzipath=unzipath;
	    }*/
	    
	public static void doUnZip(String zipFileName, String extPlace)
	//    public static void doUnZip(String filename,String unzipath)
	    {
	    String	filename=extPlace+zipFileName;
	    String	unzipath=extPlace+zipFileName.substring(0,zipFileName.lastIndexOf("."))+"/";
		
		
	    	
	    	File unzipathFile=new File(unzipath);
	    	
	    	if(!unzipathFile.exists()) {
	    		unzipathFile.mkdirs();
	    	}
	    	byte doc[]=null;
	        try{
	            //����filename���ļ�������xxx.zip
	 //       	 CheckedIutputStream csum = new CheckedIutputStream
	            ZipInputStream zipis=new ZipInputStream(new FileInputStream(filename));
	            
	            ZipEntry fentry=null;
	            
	            while((fentry=zipis.getNextEntry())!=null)
	            {
	                //fentry�����ȡzip�е���Ŀ����һ����ȡ������Ϊtest��
	                //test��Ŀ���ļ��У���˻ᴴ��һ��File����File������յĲ���Ϊ��ַ
	                //Ȼ��ͻ���exists,�жϸò�����ָ����·�����ļ�����Ŀ¼�Ƿ����
	                //��������ڣ��򹹽�һ���ļ��У������ڣ�����
	                //�������һ��zip��Ҳ��������һ���ļ��У�Ȼ�������zip������ļ�����txt
	                if(fentry.isDirectory()){
	                    File dir = new File(unzipath+fentry.getName());
	                    if(!dir.exists()){
	                        dir.mkdirs();
	                    }
	                } else {
	                    //fname���ļ���,fileoutputstream����ļ�������
	                    String fname=new String(unzipath+fentry.getName());
	                    try{
	                        //�½�һ��out,ָ��fname��fname�������ַ
	                        FileOutputStream out = new FileOutputStream(fname);
	                        doc=new byte[512];
	                        int n;
	                        //��û�ж���������ȡ��ĩβ���򷵻�-1
	                        while((n=zipis.read(doc,0,512))!=-1)
	                        {
	                            //��ͰѶ�ȡ����n���ֽ�ȫ����д�뵽ָ��·����
	                            out.write(doc,0,n);
//	                            System.out.println(n);
	                        }
	                        out.close();
	                        out=null;
	                        doc=null;
	                    }catch (Exception ex) {
	                    	ex.printStackTrace();
	                    }
	                }
	            }
	            zipis.close();
	        }catch (IOException ioex){ioex.printStackTrace();}
	        System.out.println("finished!");
	    }

}
