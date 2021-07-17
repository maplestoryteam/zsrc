/*
Ôö¼ÓÉËº¦µÄ×°±¸
 */
package abc;

import abc.*;
import constants.*;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ¹Ø¼ü×ÖÆÁ±Î {

    private static ¹Ø¼ü×ÖÆÁ±Î instance = null;
    private static boolean CANLOG;
    private Properties itempb_cfg;
    private String PM;
    private String PMM;

    private static Logger log = LoggerFactory.getLogger(¹Ø¼ü×ÖÆÁ±Î.class);

    public ¹Ø¼ü×ÖÆÁ±Î() {
        itempb_cfg = new Properties();
        try {
            InputStreamReader is = new FileReader("¼ÓÔØÎÄ¼þ\\¹Ø¼ü×ÖÆÁ±Î.ini");
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
        ¹Ø¼ü×ÖÆÁ±Î.CANLOG = CANLOG;
    }

    public static ¹Ø¼ü×ÖÆÁ±Î getInstance() {
        if (instance == null) {
            instance = new ¹Ø¼ü×ÖÆÁ±Î();
        }
        return instance;
    }
}
