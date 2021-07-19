import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class JComboBoxDemo1 {

    public static void main(String[] args) {
        JFrame f = new JFrame("JComboBoxDemo1");// 创建一个JFrame对象
        Container contentPane = f.getContentPane();	// 定义一个容器
        contentPane.setLayout(new GridLayout(1, 2));
        String[] s = {"中国", "日本", "美国", "英国", "法国", "台湾", "澳洲", "韩国"};// 定义一个字符串数组，并将其初始化
        Vector v = new Vector();
        v.addElement("Nokia N80");
        v.addElement("Nokia 8250");
        v.addElement("Motorola v8088");
        v.addElement("Motorola v3");
        v.addElement("Panasonic 8850");
        v.addElement("其它");
        JComboBox combo1 = new JComboBox(s);// 定义一个JComboBox对象
        combo1.addItem("中国");// 利用JComboBox类所提供的addItem()方法，加入一个项目到此JComboBox中。
        combo1.setBorder(BorderFactory.createTitledBorder("你最喜欢到哪个国家玩呢?"));
        JComboBox combo2 = new JComboBox(v);
        combo2.setBorder(BorderFactory.createTitledBorder("你最喜欢哪一种手机呢？"));
        contentPane.add(combo1);
        contentPane.add(combo2);
        f.pack();
        f.show();
        f.addWindowListener(new WindowAdapter() {// 添加窗口监听器
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
