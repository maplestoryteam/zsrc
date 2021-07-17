package gui.图片.shuchu;


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class AppOutputCapture {

    private static Process process;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("用法：java AppOutputCapture "
                    + "<程序名字> {参数1 参数2 ...}");
            System.exit(0);
        }

        try {
            // 启动命令行指定程序的新进程
            process = Runtime.getRuntime().exec(args);
        } catch (IOException e) {
            System.err.println("创建进程时出错...\n" + e);
            System.exit(1);
        }

        // 获得新进程所写入的流
        InputStream[] inStreams
                = new InputStream[]{
                    process.getInputStream(), process.getErrorStream()};
        ConsoleTextArea cta = new ConsoleTextArea(inStreams);
        cta.setFont(java.awt.Font.decode("monospaced"));

        JFrame frame = new JFrame(args[0]
                + "控制台输出");

        frame.getContentPane().add(new JScrollPane(cta),
                BorderLayout.CENTER);
        frame.setBounds(50, 50, 400, 400);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                process.destroy();
                try {
                    process.waitFor(); // 在Win98下可能被挂起
                } catch (InterruptedException e) {
                }
                System.exit(0);
            }
        });
    } // main()
} // AppOutputCapture
