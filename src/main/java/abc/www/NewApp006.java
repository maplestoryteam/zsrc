package abc.www;

import java.io.*;
public class NewApp006 {
	public static void main(String[] args) throws IOException{
	char charArr[]=new char[1024]; //创建可容纳 1024 个字符的数组
        FileReader b=new FileReader("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt");
        int iNum=b.read(charArr); //将数据读入到数组 a 中，并返回字符数
        String str=new String(charArr,0,iNum); //将字符串数组转换成字符串
        System.out.println("读取的字符个数为："+iNum+",内容为：\n");
        System.out.println(str);
	}
}
