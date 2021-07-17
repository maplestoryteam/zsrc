/*
增加伤害的装备
 */
package abc;

import constants.*;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 屏幕关键字 {

    private static 屏幕关键字 instance = null;
    private static boolean CANLOG;
    private Properties itempb_cfg;
    private String PM;

    private static Logger log = LoggerFactory.getLogger(屏幕关键字.class);

    public 屏幕关键字() {
        itempb_cfg = new Properties();
        try {
            InputStreamReader is = new FileReader("加载文件\\加载文件\\屏幕关键字.ini");
            itempb_cfg.load(is);
            is.close();
            PM = itempb_cfg.getProperty("PM");
        } catch (Exception e) {
            log.error("Could not configuration", e);
        }
    }

    public String getPM() {
        return PM;
    }

    public boolean isCANLOG() {
        return CANLOG;
    }

    public void setCANLOG(boolean CANLOG) {
        屏幕关键字.CANLOG = CANLOG;
    }

    public static 屏幕关键字 getInstance() {
        if (instance == null) {
            instance = new 屏幕关键字();
        }
        return instance;
    }
}
