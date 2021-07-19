/*
增加伤害的装备
 */
package abc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class 注册白名单 {

    private static 注册白名单 instance = null;
    private static boolean CANLOG;
    private final Properties itempb_cfg;
    private String QQ;

    private static final Logger log = LoggerFactory.getLogger(注册白名单.class);

    public 注册白名单() {
        itempb_cfg = new Properties();
        try {
            InputStreamReader is = new FileReader("加载文件\\账号白名单.ini");
            itempb_cfg.load(is);
            is.close();
            QQ = itempb_cfg.getProperty("QQ");
        } catch (Exception e) {
            //String QQ = "71447500";
            log.error("Could not configuration", e);
        }
    }

    public String getQQ() {
        return QQ;
    }

    public boolean isCANLOG() {
        return CANLOG;
    }

    public void setCANLOG(boolean CANLOG) {
        注册白名单.CANLOG = CANLOG;
    }

    public static 注册白名单 getInstance() {
        if (instance == null) {
            instance = new 注册白名单();
        }
        return instance;
    }
}
