package gui.图片.xiazai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;  
import java.io.RandomAccessFile;  
import java.net.URL;  
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JProgressBar;
  
public class MultiDown {
	
    public void MultiDownload(String name, String sourceFile, String targetFile, int threadNum, int intMain, 
    		ArrayList<JProgressBar> jpList/*进度条*/) {
    	File tf = new File(targetFile);
    	if (tf.exists())
    		return;
    	
        //定义几个线程去下载  
        int DOWN_THREAD_NUM = threadNum;
        String DL_FILE_NAME = targetFile + ".dl";//临时文件
        
        String CFG_FILE_NAME = targetFile + ".cfg";//记录下载进度
        
        InputStream[] isArr = new InputStream[DOWN_THREAD_NUM];  
        RandomAccessFile[] outArr = new RandomAccessFile[DOWN_THREAD_NUM];  
        try {
            // 创建一个URL对象  
            URL url = new URL(sourceFile);  
            // 以此URL对象打开第一个输入流  
            isArr[0] = url.openStream();  
            //获取文件大小
            long fileLen = getFileLength(url);  
            
            System.out.println(sourceFile + "的大小" + fileLen);  
            
            // 以输出文件名创建第一个RandomAccessFile输出流  
            //创建从中读取和向其中写入（可选）的随机存取文件流，第一个参数：文件名，第二个参数是：参数指定用以打开文件的访问模式  
            //"rw"可能是可读可写，  
            outArr[0] = new RandomAccessFile(DL_FILE_NAME, "rw");

            outArr[0].setLength(fileLen);
            
            // 每线程应该下载的字节数  
            long numPerThred = fileLen / DOWN_THREAD_NUM;
            // 整个下载资源整除后剩下的余数取模  
            long left = fileLen % DOWN_THREAD_NUM;
            
            List<DownThread> list = new ArrayList<DownThread>();
            
            for (int i = 0; i < DOWN_THREAD_NUM; i++) {
                // 断点下载的配置文件
                File fileConfig = new File(CFG_FILE_NAME + i);
                if(!fileConfig.exists())
                	fileConfig.createNewFile();
                
                // 为每个线程打开一个输入流、一个RandomAccessFile对象，  
                // 让每个线程分别负责下载资源的不同部分。  
                //isArr[0]和outArr[0]已经使用，从不为0开始  
                if (i != 0) {  
                    // 以URL打开多个输入流  
                    isArr[i] = url.openStream();  
                    // 以指定输出文件创建多个RandomAccessFile对象  
                    outArr[i] = new RandomAccessFile(DL_FILE_NAME, "rw");  
                }  
                // 分别启动多个线程来下载网络资源  
                if (i == DOWN_THREAD_NUM - 1) {  
                    // 最后一个线程下载指定numPerThred+left个字节  
                	DownThread dt = new DownThread(name + "-" + i, i * numPerThred, (i + 1) * numPerThred  
                            + left, isArr[i], outArr[i], jpList.get((intMain*3)+i), CFG_FILE_NAME+i);
                	dt.start();
                	list.add(dt);
                } else {  
                    // 每个线程负责下载一定的numPerThred个字节  
                	DownThread dt = new DownThread(name + "-" + i, i * numPerThred, (i + 1) * numPerThred,  
                            isArr[i], outArr[i], jpList.get((intMain*3)+i), CFG_FILE_NAME+i);
                	dt.start();
                	list.add(dt);
                } 
            }             
            try  
            {
            	// 等待下载进程结束
                for (DownThread my : list)  
                {  
                    my.join();  
                }
                
                // 判断下载是否全部结束
                boolean finishFlg = true;
                for (int i = 0; i < DOWN_THREAD_NUM; i++) {
                    FileReader reader = new FileReader(CFG_FILE_NAME+i);
                    BufferedReader br = new BufferedReader(reader);
                   
                    String str = null;
                   
                    while((str = br.readLine()) != null)
                    {                         
                    	if (!str.equals("Finish"))
                    	{
                            finishFlg = false;
                    		break;
                    	}
                    }
                    
                    br.close();
                    reader.close();
                }
                
                // 如果下载全部结束，则把文件名字的后缀.dl去掉，并删除配置文件。cfg
                if (finishFlg)
                {
	                File f1 = new File(DL_FILE_NAME);
	                File f2 = new File(targetFile);
	                if (f2.exists())
	                	f2.delete();
	                if (f1.exists())
	                	f1.renameTo(f2);
	                for (int i = 0; i < DOWN_THREAD_NUM; i++) {
	                	File fileConfig = new File(CFG_FILE_NAME+i);
	                	fileConfig.delete();
	                }
                }
            }  
            catch (InterruptedException e)  
            {  
                e.printStackTrace();  
            }
            
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
    }  
  
    // 定义获取指定网络资源的长度的方法  
    public static long getFileLength(URL url) throws Exception {  
        long length = 0;  
        // 打开该URL对应的URLConnection  
        URLConnection con = url.openConnection(); 
        con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        // 获取连接URL资源的长度  
        long size = con.getContentLength();  
        length = size;  
        return length;  
    }
}  