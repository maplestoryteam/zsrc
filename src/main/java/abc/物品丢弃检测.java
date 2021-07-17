/*
增加伤害的装备
 */
package abc;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 物品丢弃检测 {

    private static 物品丢弃检测 instance = null;
    private static boolean CANLOG;
    private Properties itempb_cfg;
    private String id;
    private String idz;
    private static Logger log = LoggerFactory.getLogger(物品丢弃检测.class);

    public 物品丢弃检测() {
        itempb_cfg = new Properties();
        try {
            InputStreamReader is = new FileReader("加载文件\\物品丢弃检测.ini");
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
        物品丢弃检测.CANLOG = CANLOG;
    }

    public static 物品丢弃检测 getInstance() {
        if (instance == null) {
            instance = new 物品丢弃检测();
        }
        return instance;
    }
}
