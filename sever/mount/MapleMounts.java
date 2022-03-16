/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sever.mount;

import database.DatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author timis
 */
public class MapleMounts {
    private static final List<MapleMountControl> mount = new LinkedList();

    public static void loadMount() {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM mountinfo");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               mount.add(new MapleMountControl(
                        rs.getInt("cid"),
                        rs.getInt("mountid"))
               );
            }
            ps.close();
        } catch (SQLException ex) {
        }
    }
    
    public static void reloadMount(){
        mount.clear();
        loadMount();
    }
}
