package gui.图片.xiazai;  

import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JProgressBar;  
public class FileDownThread extends Thread  
{  
	List<DownloadFile> downloadFileList;
	int downThreadNum;
	ArrayList<JProgressBar> jprogressbarlist = null;//进度条
	ArrayList<JLabel> jlabel = null;//标签
	
    public FileDownThread(String name, List<DownloadFile> dfList, int downThreadNum,
    		ArrayList<JProgressBar> jps, ArrayList<JLabel> jls)  
    {  
        this.setName(name);
        this.setDfList(dfList);
        this.setDownThreadNum(downThreadNum);
        jprogressbarlist = jps;
        jlabel = jls;
    }
  
    @Override  
    public void run()  
    {  
        System.out.println(this.getName() + " 开始 ");  
  
        for(int i=0; i<downloadFileList.size(); i++){

        	if (!ThreadController.start)
        		break;
        	
        	if(downloadFileList.get(i).finished == 0){
        		
        		downloadFileList.get(i).finished = 1;
        		
        		int intMain = Integer.parseInt(this.getName().substring(this.getName().length()-1));
        		System.out.println(intMain);
        		try {
					jlabel.get(intMain).setText(downloadFileList.get(i).sourceFile);
				} catch (Exception e) {
					// TODO: handle exception
				}       
        		
        		MultiDown md = new MultiDown();
        		md.MultiDownload(this.getName(), downloadFileList.get(i).sourceFile,downloadFileList.get(i).targetFile, downThreadNum, intMain, jprogressbarlist);       		
        		downloadFileList.get(i).finished = 2;
        	}
        }
        
        System.out.println(this.getName() + " 结束"); 
        
        
    }

	public List<DownloadFile> getDfList() {
		return downloadFileList;
	}

	public void setDfList(List<DownloadFile> dfList) {
		this.downloadFileList = dfList;
	}

	public int getDownThreadNum() {
		return downThreadNum;
	}

	public void setDownThreadNum(int downThreadNum) {
		this.downThreadNum = downThreadNum;
	}
}  