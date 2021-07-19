package scripting;

import client.MapleClient;
import server.MaplePortal;
import tools.FileoutputUtil;

import javax.script.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static abc.Game.地图脚本报错;

public class PortalScriptManager {

    private static final PortalScriptManager instance = new PortalScriptManager();
    private final Map<String, PortalScript> scripts = new HashMap<String, PortalScript>();
    private final static ScriptEngineFactory sef = new ScriptEngineManager().getEngineByName("javascript").getFactory();

    public final static PortalScriptManager getInstance() {
        return instance;
    }

    private PortalScript getPortalScript(MapleClient c, final String scriptName) {
        if (scripts.containsKey(scriptName)) {
            scripts.clear();
            return scripts.get(scriptName);
        }

        final File scriptFile = new File("scripts/portal/" + scriptName + ".js");
        if (!scriptFile.exists()) {
            //scripts.put(scriptName, null);
            return null;
        }

        InputStream fr = null;
        final ScriptEngine portal = sef.getScriptEngine();
        try {
            fr = new FileInputStream(scriptFile);
            BufferedReader bf = new BufferedReader(new InputStreamReader(fr, EncodingDetect.getJavaEncode(scriptFile)));
            CompiledScript compiled = ((Compilable) portal).compile(bf);
            compiled.eval();
        } catch (final Exception e) {
            System.err.println("Error executing Portalscript: " + scriptName + ":" + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Error executing Portal script. (" + scriptName + ") " + e);
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (final IOException e) {
                    System.err.println("ERROR CLOSING" + e);
                }
            }
        }
        final PortalScript script = ((Invocable) portal).getInterface(PortalScript.class);
        scripts.put(scriptName, script);
        return script;
    }

    public final void executePortalScript(final MaplePortal portal, final MapleClient c) {
        final PortalScript script = getPortalScript(c, portal.getScriptName());
        if (gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0 && c.getPlayer().isGM()) {
            c.getPlayer().dropMessage(5, "dialogue:[scripts/portal/" + portal.getScriptName() + ".js]" + (script != null ? "" : ""));
        }
        if (script != null) {
            try {
                script.enter(new PortalPlayerInteraction(c, portal));
            } catch (Exception e) {
                System.err.println("Error entering Portalscript: " + portal.getScriptName() + ":" + e);
            }
        } else {
            if (地图脚本报错 == "开") {
                System.out.println("Unhandled portal script " + portal.getScriptName() + " on map " + c.getPlayer().getMapId());//地图脚本报错屏蔽
            }
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Unhandled portal script " + portal.getScriptName() + " on map " + c.getPlayer().getMapId());
        }
    }

    public final void clearScripts() {
        scripts.clear();
    }
}
