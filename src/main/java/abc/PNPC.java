package abc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class PNPC {

    private static PNPC instance = null;
    private static boolean CANLOG;
    private final Properties itempb_cfg;
    private String PM;

    private static final Logger log = LoggerFactory.getLogger(PNPC.class);

    public PNPC() {
        itempb_cfg = new Properties();
        try {
            InputStreamReader is = new FileReader("scripts\\pnpc\\pnpc.ini");
            itempb_cfg.load(is);
            is.close();
            PM = itempb_cfg.getProperty("pnpc");
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
        PNPC.CANLOG = CANLOG;
    }

    public static PNPC getInstance() {
        if (instance == null) {
            instance = new PNPC();
        }
        return instance;
    }
}
