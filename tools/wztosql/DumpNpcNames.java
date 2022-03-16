/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.wztosql;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.swing.Progressbar;
import tools.StringUtil;

/**
 *
 * @author Itzik
 */
public class DumpNpcNames {

    private final Connection con = DatabaseConnection.getConnection();
    private static final Map<Integer, String> npcNames = new HashMap<>();

    public static void start(String[] args) {
        try {
            Progressbar.setValue(0);
            Progressbar.setText("轉存NPC名稱...");
            System.out.println("Dumping npc name data.");
            DumpNpcNames dump = new DumpNpcNames();
            dump.dumpNpcNameData();
            System.out.println("Dump complete.");
            Progressbar.setValue(100);
        } catch (SQLException ex) {
            Logger.getLogger(DumpNpcNames.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dumpNpcNameData() throws SQLException {
        MapleDataProvider npcData = MapleDataProviderFactory.getDataProvider("Npc.wz");
        MapleDataProvider stringDataWZ = MapleDataProviderFactory.getDataProvider("String.wz");
        MapleData npcStringData = stringDataWZ.getData("Npc.img");
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM `wz_npcnamedata`")) {
            ps.execute();
        }
        for (MapleData c : npcStringData) {
            if (Progressbar.getValue() < 50) {
                Progressbar.addValue();
            }
            int nid = Integer.parseInt(c.getName());
            String n = StringUtil.getLeftPaddedStr(nid + ".img", '0', 11);
            try {
                if (npcData.getData(n) != null) {//only thing we really have to do is check if it exists. if we wanted to, we could get the script as well :3
                    String name = MapleDataTool.getString("name", c, "MISSINGNO");
                    if (name.contains("Maple TV") || name.contains("Baby Moon Bunny")) {
                        continue;
                    }
                    npcNames.put(nid, name);
                }
            } catch (NullPointerException e) {
            } catch (RuntimeException e) { //swallow, don't add if 
            }
        }
        for (int key : npcNames.keySet()) {
            if (Progressbar.getValue() < 90) {
                Progressbar.addValue();
            }
            try {
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO `wz_npcnamedata` (`npc`, `name`) VALUES (?, ?)")) {
                    ps.setInt(1, key);
                    ps.setString(2, npcNames.get(key));
                    ps.execute();
                }
                System.out.println("key: " + key + " name: " + npcNames.get(key));
            } catch (Exception ex) {
                System.out.println("Failed to save key " + key);
            }
        }
    }
}
