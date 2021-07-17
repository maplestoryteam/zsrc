package gui.图片.gui1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.*;

public class TestCmd {

    public boolean getProcess() {
        boolean flag = false;
        ByteArrayOutputStream baos = null;
        InputStream os = null;
        String s = "";
        try {
            Process p = Runtime.getRuntime().exec("cmd /c tasklist");
            baos = new ByteArrayOutputStream();
            os = p.getInputStream();
            byte b[] = new byte[256];
            while (os.read(b) > 0) {
                baos.write(b);
            }
            s = baos.toString();
            if (s.indexOf("YY.exe") >= 0) {
                System.out.println("已经运行YY");
                flag = true;
            } else {
                System.out.println("未运行YY");
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
          /*  try {
                //System.out.println(s);
                os.close();
                baos.close();
                System.out.println("no" + s);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
        return flag;
    }

    public static void main(String[] args) {
        new TestCmd().getProcess();
        System.out.println("Hello World!");
    }
}
