package zevms.data;

import javax.swing.*;
import java.io.Serializable;
public class DataPack implements Serializable
{
	public int optype=0; //1是登陆 2是发送内容 3是离开 4是新用户登陆 5是系统广播信息

	public Object con=null;
	public String from=null;      //发送信息用户的名字
	public String to=null;        //发送到的用户的名字
	public ImageIcon expr=null;   //要发送的表情图片
	public String biaoqing=null;  //要发送的表情文字
	public String yanse=null;	  //要发送文字的颜色
	public String txt=null;	      //要发送的信息内容
	public boolean si=false;      //是否是私聊
	public DataPack()
	{
	}
	public void init()
	{
		optype=0;
		con=null;
		from=null;
		to=null;
		expr=null;
		biaoqing=null;
		yanse=null;
		txt=null;
		si=false;
	}
}