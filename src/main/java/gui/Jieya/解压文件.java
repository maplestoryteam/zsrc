package gui.Jieya;

import java.io.*;

public class 解压文件 {

    static final int BUFFER = 2048;

    /**
     * 读取ZIP文件，只适合于ZIP文件对于RAR文件无效，因为ZIP文件的压缩算法是公开的，而RAR不是
     *
     * @author 彭建明
     * @version 1.0
     * @param zipfilepath：ZIP文件的路径，unzippath：要解压到的文件路径
     */
    /*public  void main(String[] args) {
        
        ReadZip("C:\\Users\\Administrator\\Desktop\\divine.zip", "C:\\Users\\Administrator\\Desktop\\");
        
    }*/
    public static void 解压文件(String zipfilepath, String unzippath) {
        try {
            BufferedOutputStream bos = null;
            //创建输入字节流
            FileInputStream fis = new FileInputStream(zipfilepath);
            //根据输入字节流创建输入字符流
            BufferedInputStream bis = new BufferedInputStream(fis);
            //根据字符流，创建ZIP文件输入流
            ZipInputStream zis = new ZipInputStream(bis);
            //zip文件条目，表示zip文件
            ZipEntry entry;
            //循环读取文件条目，只要不为空，就进行处理
            while ((entry = zis.getNextEntry()) != null) {
                //System.out.println("====" + entry.getName());
                int count;
                byte[] date = new byte[BUFFER];
                //如果条目是文件目录，则继续执行
                if (entry.isDirectory()) {
                    continue;
                } else {
                    int begin = zipfilepath.lastIndexOf("\\") + 1;
                    int end = zipfilepath.lastIndexOf(".") + 1;
                    String zipRealName = zipfilepath.substring(begin, end);
                    bos = new BufferedOutputStream(new FileOutputStream(getRealFileName(unzippath, entry.getName())));//zipRealName
//                    System.out.println(unzippath);
//                    System.out.println(zipRealName);
//                    System.out.println(entry.getName());
                    while ((count = zis.read(date)) != -1) {
                        bos.write(date, 0, count);
                    }
                    bos.flush();
                    bos.close();
                }
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File getRealFileName(String zippath, String absFileName) {
        String[] dirs = absFileName.split("/", absFileName.length());
        //创建文件对象 
        File file = new File(zippath);
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                //根据file抽象路径和dir路径字符串创建一个新的file对象，路径为文件的上一个目录
                file = new File(file, dirs[i]);
            }
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(file, dirs[dirs.length - 1]);
        return file;
    }
}
