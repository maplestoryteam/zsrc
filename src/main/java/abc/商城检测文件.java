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

public class 商城检测文件 {

    private static 商城检测文件 instance = null;
    private static boolean CANLOG;
    private Properties itempb_cfg;
    private String id;
    private String idz;
    private static Logger log = LoggerFactory.getLogger(商城检测文件.class);

    public 商城检测文件() {
        itempb_cfg = new Properties();
        try {
            InputStreamReader is = new FileReader("加载文件\\商城检测.ini");
            itempb_cfg.load(is);
            is.close();
            id = itempb_cfg.getProperty("id");
            idz = itempb_cfg.getProperty("id2");
        } catch (Exception e) {
            log.error("Could not configuration", e);
        }
    }

    public String getid() {
        return id;
    }

    public String getidz() {
        return idz;
    }

    public boolean isCANLOG() {
        return CANLOG;
    }

    public void setCANLOG(boolean CANLOG) {
        商城检测文件.CANLOG = CANLOG;
    }

    public static 商城检测文件 getInstance() {
        if (instance == null) {
            instance = new 商城检测文件();
        }
        return instance;
    }
}
