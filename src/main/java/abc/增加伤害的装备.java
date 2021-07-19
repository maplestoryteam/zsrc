/*
增加伤害的装备
 */
package abc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class 增加伤害的装备 {

    private static 增加伤害的装备 instance = null;
    private static boolean CANLOG;
    private final Properties itempb_cfg;
    private String[] ite;
    private String[] ite5;
    private String[] fte5;
    private String[] fte2;
    private String[] ftex;
    private String[] ite10;
    private String[] ite20;
    private String[] boss5;
    private String[] boss10;
    private String[] Xx3;
    private String[] Xx5;
    private String[] Xm3;
    private String[] Xm5;
    private String[] JX1;
    private static final Logger log = LoggerFactory.getLogger(增加伤害的装备.class);

    public 增加伤害的装备() {
        itempb_cfg = new Properties();
        try {
            InputStreamReader is = new FileReader("Load\\装备附属加载文件.ini");
            itempb_cfg.load(is);
            is.close();
            ite5 = itempb_cfg.getProperty("iet5").split(",");
            ite10 = itempb_cfg.getProperty("iet10").split(",");
            ite20 = itempb_cfg.getProperty("iet20").split(",");
            fte5 = itempb_cfg.getProperty("fet5").split(",");
            fte2 = itempb_cfg.getProperty("fet2").split(",");
            ftex = itempb_cfg.getProperty("fetx").split(",");
            Xx3 = itempb_cfg.getProperty("Xx3").split(",");
            Xx5 = itempb_cfg.getProperty("Xx5").split(",");
            Xm3 = itempb_cfg.getProperty("Xm3").split(",");
            Xm5 = itempb_cfg.getProperty("Xm5").split(",");
            JX1 = itempb_cfg.getProperty("JX1").split(",");
            boss5 = itempb_cfg.getProperty("boss5").split(",");
            boss10 = itempb_cfg.getProperty("boss10").split(",");
        } catch (Exception e) {
            log.error("Could not configuration", e);
        }
    }

    public String[] getboss5() {
        return boss5;
    }

    public String[] getboss10() {
        return boss10;
    }

    public String[] getJX1() {
        return JX1;
    }

    public String[] getXx3() {
        return Xx3;
    }

    public String[] getXx5() {
        return Xx5;
    }

    public String[] getXm3() {
        return Xm3;
    }

    public String[] getXm5() {
        return Xm5;
    }

    public String[] getIte() {
        return ite;
    }

    public String[] getIte5() {
        return ite5;
    }

    public String[] getfte5() {
        return fte5;
    }

    public String[] getfte2() {
        return fte2;
    }

    public String[] getftex() {
        return ftex;
    }

    public String[] getIte10() {
        return ite10;
    }

    public String[] getIte20() {
        return ite20;
    }

    public boolean isCANLOG() {
        return CANLOG;
    }

    public void setCANLOG(boolean CANLOG) {
        增加伤害的装备.CANLOG = CANLOG;
    }

    public static 增加伤害的装备 getInstance() {
        if (instance == null) {
            instance = new 增加伤害的装备();
        }
        return instance;
    }
}
