package gui.图片.xiazai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;  
import java.io.RandomAccessFile;  

import javax.swing.JProgressBar;
  
public class DownThread extends Thread {  
      
    // 定义字节数组（取水的竹筒）的长度  
    private final int BUFF_LEN = 100;  
      
    // 定义下载的原始起始点  
    private long start;  
    
    // 定义下载的实际起始点  
    private long downloadstart;  
      
    // 定义下载的结束点  
    private long end;  
      
    // 下载资源对应的输入流  
    private InputStream inputStream;  
      
    // 将下载到的字节输出到raf中  
    private RandomAccessFile raf;  
    
    // 进度条
    private JProgressBar jpb;
    
    // 断点下载配置文件
    private String cfgFileName;
      
    // 构造器，传入输入流，输出流和下载起始点、结束点  
    public DownThread(String name, long start, long end, InputStream is, RandomAccessFile raf, JProgressBar jp, String fileName) {  
        // 输出该线程负责下载的字节位置  
        System.out.println(start + "---->" + end);  
        this.setName(name);
        this.start = start;  
        this.downloadstart = start;
        this.end = end;  
        this.inputStream = is;  
        this.raf = raf;  
        this.jpb = jp;
        this.cfgFileName = fileName;
    }  
  
    public void run() {  
        try {
        	// 判断是否需要断点下载
            FileReader reader = new FileReader(cfgFileName);
            BufferedReader br = new BufferedReader(reader);
           
            String str = null;
           
            while((str = br.readLine()) != null)
            {                         
            	if (!str.equals("Finish"))
            	{
            		downloadstart = Long.parseLong(str);
            		break;
            	}
            	else
            	{
                    jpb.setMinimum(0);
                    jpb.setMaximum(1);
                    jpb.setValue(1);
            		return;
            	}
            }
            
            br.close();
            reader.close();            
            // 下载开始位置设置
            inputStream.skip(downloadstart);  
            raf.seek(downloadstart);             
            // 在配置文件中写入该线程的起始下载位置
    		writeToFile(cfgFileName, Long.toString(downloadstart));		
            // 定义读取输入流内容的的缓存数组（竹筒）  
            byte[] buff = new byte[BUFF_LEN];  
            // 本线程负责下载资源的大小  
            long contentLen = end - start;  
            // 定义最多需要读取几次就可以完成本线程的下载  
            long times = contentLen / BUFF_LEN + 4;  
            // 实际读取的字节数  
            int hasRead = 0;         
            jpb.setMinimum(0);
            jpb.setMaximum((int) times);     
            
            System.out.println(times+"我来看看这是个啥");
            
            for (int i = 0; i <times; i++) {
            	if (!ThreadController.start)
            		break;
            	
            	if (downloadstart > start + i*BUFF_LEN)
            		continue;           	
                hasRead = inputStream.read(buff);
                downloadstart += hasRead;
                // 如果读取的字节数小于0，则退出循环！  
                if (hasRead < 0) {
                	writeToFile(cfgFileName, "Finish");
                    break;  
                }  
                raf.write(buff, 0, hasRead);
            	// 下载内容写入配置文件
                if (i == times-1)
                	writeToFile(cfgFileName, "Finish");
                else
                	writeToFile(cfgFileName, Long.toString(downloadstart)); 	
            	// 进度条显示             
					jpb.setValue(i+1);             
            }  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
        // 使用finally块来关闭当前线程的输入流、输出流  
        finally {  
            try {  
                if (inputStream != null) {  
                    inputStream.close();  
                }  
                if (raf != null) {  
                    raf.close();  
                }  
            } catch (Exception ex) {  
                ex.printStackTrace();  
            }  
        }  
    }
    
    private static void writeToFile(String fileName, String content)
    {
    	 try {
             // write string to file
             FileWriter writer = new FileWriter(fileName);
             BufferedWriter bw = new BufferedWriter(writer);
             bw.write(content);          
             bw.close();
             writer.close();
    	 }
    	 catch(FileNotFoundException e) {
                   e.printStackTrace();
         }
         catch(IOException e) {
               e.printStackTrace();
         }
	}
}