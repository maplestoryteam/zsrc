package gui.图片.shuchu;

import java.io.*;
public class Listing2 {
	static PipedInputStream pipedIS = new PipedInputStream();
	static PipedOutputStream pipedOS = 
		new PipedOutputStream();

	public static void main(String[] a){
		try {
			pipedIS.connect(pipedOS);
		}
		catch(IOException e) {
			System.err.println("连接失败");
				System.exit(1);
			}

		byte[] inArray	= new byte[10];
		byte[] outArray = new byte[20];
		int bytesRead = 0;

		try {
            // 向pipedOS发送20字节数据
			pipedOS.write(outArray, 0, 20);
			System.out.println("	 已发送20字节...");

           // 在每一次循环迭代中，读入10字节
           // 发送20字节
			bytesRead = pipedIS.read(inArray, 0, 10);
            int i=0;
			while(bytesRead != -1) {
				pipedOS.write(outArray, 0, 20);
				System.out.println("	 已发送20字节..."+i);
				i++;
				bytesRead = pipedIS.read(inArray, 0, 10);
			}
		}
		catch(IOException e) {
				System.err.println("读取pipedIS时出现错误: " + e);
				System.exit(1);
		}
	} // main()
}
