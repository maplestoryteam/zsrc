/*
Ôö¼ÓÉËº¦µÄ×°±¸
 */
package abc;

import constants.*;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Îü¹Ö¼ì²â {

    private static Îü¹Ö¼ì²â instance = null;
    private static boolean CANLOG;
    private Properties itempb_cfg;
    private String jc1[];
    private String jc2[];
    private String jc3[];
    private String jc4[];
    private String jc5[];
    private String jc6[];
    private String jc7[];
    private String jc8[];

    private static Logger log = LoggerFactory.getLogger(Îü¹Ö¼ì²â.class);

    public Îü¹Ö¼ì²â() {
        itempb_cfg = new Properties();
        try {
            InputStreamReader is = new FileReader("Load\\Bin\\ÌìÇ´¼ì²âA.ini");
            itempb_cfg.load(is);
            is.close();
            jc1 = itempb_cfg.getProperty("jc1").split(",");
            jc2 = itempb_cfg.getProperty("jc2").split(",");
            jc3 = itempb_cfg.getProperty("jc3").split(",");
            jc4 = itempb_cfg.getProperty("jc4").split(",");
            jc5 = itempb_cfg.getProperty("jc5").split(",");
            jc6 = itempb_cfg.getProperty("jc6").split(",");
            jc7 = itempb_cfg.getProperty("jc7").split(",");
            jc8 = itempb_cfg.getProperty("jc8").split(",");

        } catch (Exception e) {
            log.error("Could not configuration", e);
        }
    }

    public String[] getjc1() {
        return jc1;
    }

    public String[] getjc2() {
        return jc2;
    }

    public String[] getjc3() {
        return jc3;
    }

    public String[] getjc4() {
        return jc4;
    }

    public String[] getjc5() {
        return jc5;
    }

    public String[] getjc6() {
        return jc6;
    }

    public String[] getjc7() {
        return jc7;
    }

    public String[] getjc8() {
        return jc8;
    }

    public boolean isCANLOG() {
        return CANLOG;
    }

    public void setCANLOG(boolean CANLOG) {
        Îü¹Ö¼ì²â.CANLOG = CANLOG;
    }

    public static Îü¹Ö¼ì²â getInstance() {
        if (instance == null) {
            instance = new Îü¹Ö¼ì²â();
        }
        return instance;
    }
}
