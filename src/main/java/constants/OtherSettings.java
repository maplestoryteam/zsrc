/*
商城屏蔽物品
 */
package constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class OtherSettings {

    private static OtherSettings instance = null;
    private static boolean CANLOG;
    private final Properties itempb_cfg;
    private String[] itempb_id;
    private String[] itemjy_id;
    private String[] itemgy_id;
    //private String 屏蔽文字[];
    private String[] mappb_id;
    private static final Logger log = LoggerFactory.getLogger(OtherSettings.class);

    public OtherSettings() {
        itempb_cfg = new Properties();
        try {
            InputStreamReader is = new FileReader("Load\\物品掉落提示文件.ini");//屏蔽商城物品
            itempb_cfg.load(is);
            is.close();
            itempb_id = itempb_cfg.getProperty("baowu").split(",");
            itemjy_id = itempb_cfg.getProperty("diaoyu").split(",");
            //itemgy_id = itempb_cfg.getProperty("gysj", "0").split(",");
            //屏蔽文字 = itempb_cfg.getProperty("pbsh", "0").split(",");
        } catch (Exception e) {
            log.error("Could not configuration", e);
        }
    }

    public String[] getItempb_id() {
        return itempb_id;
    }

    public String[] getItemgy_id() {
        return itemgy_id;
    }

    /* public String[] 屏蔽文字() {a 
        return 屏蔽文字;
    }*/
    public String[] getItemjy_id() {
        return itemjy_id;
    }

    public String[] getMappb_id() {
        return mappb_id;
    }

    public boolean isCANLOG() {
        return CANLOG;
    }

    public void setCANLOG(boolean CANLOG) {
        OtherSettings.CANLOG = CANLOG;
    }

    public static OtherSettings getInstance() {
        if (instance == null) {
            instance = new OtherSettings();
        }
        return instance;
    }
}

///*
//商城屏蔽物品
//*/
//package constants;
//
//import java.io.FileReader;
//import java.io.InputStreamReader;
//import java.util.Properties;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class OtherSettings {
//
//    private static OtherSettings instance = null;
//    private static boolean CANLOG;
//    private Properties itempb_cfg;
//    private String itempb_id[];
//    /*private String itemjy_id[];
//    private String itemgy_id[];
//    //private String 屏蔽文字[];
//    private String mappb_id[];*/
//    private static Logger log = LoggerFactory.getLogger(OtherSettings.class);
//
//    public OtherSettings() {
//        itempb_cfg = new Properties();
//        try {
//            InputStreamReader is = new FileReader("ZEVMS.ini");
//            itempb_cfg.load(is);
//            is.close();
//            itempb_id = itempb_cfg.getProperty("爆物提示").split(",");
//            /**itemjy_id = itempb_cfg.getProperty("cashjy", "0").split(",");
//            itemgy_id = itempb_cfg.getProperty("爆物提示", "0").split(",");*/
//            //屏蔽文字 = itempb_cfg.getProperty("pbsh", "0").split(",");
//        } catch (Exception e) {
//            log.error("Could not configuration", e);
//        }
//    }
//
//    public String[] getItempb_id() {
//        return itempb_id;
//    }
//
////    public String[] getItemgy_id() {
////        return itemgy_id;
////    }
////
////   /* public String[] 屏蔽文字() {
////        return 屏蔽文字;
////    }*/
////
////    public String[] getItemjy_id() {
////        return itemjy_id;
////    }
////
////    public String[] getMappb_id() {
////        return mappb_id;
////    }
//
//    public boolean isCANLOG() {
//        return CANLOG;
//    }
//
//    public void setCANLOG(boolean CANLOG) {
//        OtherSettings.CANLOG = CANLOG;
//    }
//
//    public static OtherSettings getInstance() {
//        if (instance == null) {
//            instance = new OtherSettings();
//        }
//        return instance;
//    }
//}
