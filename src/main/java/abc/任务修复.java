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

public class 任务修复 {

    private static 任务修复 instance = null;
    private static boolean CANLOG;
    private Properties itempb_cfg;
    private String PM;
    private String PMM;

    private static Logger log = LoggerFactory.getLogger(任务修复.class);

    public 任务修复() {
        itempb_cfg = new Properties();
        try {
            InputStreamReader is = new FileReader("scripts\\zevms\\QUEST.ini");
            itempb_cfg.load(is);
            is.close();
            PM = itempb_cfg.getProperty("PM");
            PMM = itempb_cfg.getProperty("PMM");
        } catch (Exception e) {
            log.error("Could not configuration", e);
        }
    }

    public String getPM() {
        return PM;
    }

    public String getPMM() {
        return PMM;
    }

    public boolean isCANLOG() {
        return CANLOG;
    }

    public void setCANLOG(boolean CANLOG) {
        任务修复.CANLOG = CANLOG;
    }

    public static 任务修复 getInstance() {
        if (instance == null) {
            instance = new 任务修复();
        }
        return instance;
    }
}
