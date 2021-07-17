package gui.图片.xiazai;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
  
public class StartDownload extends Thread  
{  
	ArrayList<JProgressBar> jpList = null;
	ArrayList<JLabel> jlList = null;
	static String sourceFile;
	static String name1;
	static String path1;
	JLabel label1;
    //定义几个线程去下载多个文件
    final static int FILE_THREAD_NUM = 3;
    //定义几个线程去下载单个文件  
    final static int DOWN_THREAD_NUM = 3;
    
    public static void main(String[] args){
    }
    
    public StartDownload(ArrayList<JProgressBar> jps, ArrayList<JLabel> jls, String path, String name, String path2, JLabel label){
    	jpList = jps;//进度条
    	jlList = jls;//标签
    	sourceFile=path;//链接	
        name1=name;//名字		
        path1=path2;//存储路径
        label1=label;
    }
    
    public void run(){    	
   
    	List<DownloadFile> fileList = new ArrayList<DownloadFile>();
    	
    	buildList(fileList);
    	
        System.out.println("下载开始");  
        label1.setText("下载开始");
        List<FileDownThread> list = new ArrayList<FileDownThread>();
  
        for (int i = 0; i < FILE_THREAD_NUM; i++)  
        {  
        	FileDownThread my = new FileDownThread("线程 " + i, fileList, DOWN_THREAD_NUM, jpList, jlList);  
        	System.out.println(my);
            my.start();
            list.add(my);
        }
  
        try  
        {  
            for (FileDownThread my : list)  
            {  
                my.join();  
            }  
        }  
        catch (InterruptedException e)  
        {  
            e.printStackTrace();  
        }  
  
        System.out.println("下载完成");
        
       label1.setText("下载结束");
    }

    private static void buildList(List<DownloadFile> fileList){

    	DownloadFile downloadfile1 = new DownloadFile();
    	String a=path1+"\\"+name1;
    	downloadfile1.sourceFile=sourceFile;
    	downloadfile1.targetFile=a;
    	downloadfile1.finished=0;
    	fileList.add(downloadfile1);
    	
    }
} 