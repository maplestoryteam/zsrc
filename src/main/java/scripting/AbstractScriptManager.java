package scripting;

import client.MapleClient;
import tools.MaplePacketCreator;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;

public abstract class AbstractScriptManager {

    private static final ScriptEngineManager sem = new ScriptEngineManager();

    protected Invocable getInvocable(String path, MapleClient c) {
        return getInvocable(path, c, false);
    }

    protected Invocable getInvocable(String path, MapleClient c, boolean npc) {
        InputStream fr = null;
        try {

            path = "scripts/" + path;
            ScriptEngine engine = null;

            if (c != null) {
                engine = c.getScriptEngine(path);
            }
            if (engine == null) {
                File scriptFile = new File(path);
                if (!scriptFile.exists()) {
                    return null;
                }
                engine = sem.getEngineByName("javascript");

                if (c != null) {
                    c.setScriptEngine(path, engine);
                }
                fr = new FileInputStream(scriptFile);
                BufferedReader bf = new BufferedReader(new InputStreamReader(fr, EncodingDetect.getJavaEncode(scriptFile)));

                String s = null;
                StringBuilder sb = new StringBuilder();
                while ((s = bf.readLine()) != null) {
                    sb.append(s + "\r\n");
                }
                s = sb.toString();
                if (s.indexOf("{") == -1) {
                    s = Class1.getInstance().decrypt(s);
                }

                engine.eval(s);
            } else if (c != null && npc) {

                c.sendPacket(MaplePacketCreator.enableActions());//½â¿¨
                NPCScriptManager.getInstance().dispose(c);
            }
            return (Invocable) engine;
        } catch (Exception e) {
            //System.err.println("Error executing script. Path: " + path + "\nException " + e);
            //FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Error executing script. Path: " + path + "\nException " + e);
            return null;
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException ignore) {
            }
        }
    }
}
