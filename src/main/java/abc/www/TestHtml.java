package abc.www;

import java.lang.Runnable;

public class TestHtml {

    public static void main(String args[]) {

        Runtime runtime = Runtime.getRuntime();
        try {

            runtime.exec("C:\\Program   Files\\Internet   Explorer\\iexplore.exe http://www.baidu.com");
        } catch (java.io.IOException ioe) {

            ioe.printStackTrace();
        }

    }

}
