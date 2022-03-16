/*
    This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc>
                       Matthias Butz <matze@odinms.de>
                       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation. You may not use, modify
    or distribute this program under any other version of the
    GNU Affero General Public License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package tools;

import java.io.FileOutputStream;
import java.io.IOException;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataFileEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
/*
 * Author: Xerdox
*/
public class HairAndEyeCreator {
    public static void main(String args[]) throws IOException {
    MapleDataProvider hairSource = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Character.wz/Hair"));
        MapleDataProvider faceSource = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Character.wz/Face"));
        final MapleDataDirectoryEntry root = hairSource.getRoot();
        StringBuilder sb = new StringBuilder();
        FileOutputStream out = new FileOutputStream("hairAndFacesID.txt", true);
        System.out.println("讀取頭髮 :");
        sb.append("頭髮:\r\n");
        for (MapleDataFileEntry topDir : root.getFiles()) {
            int id = Integer.parseInt(topDir.getName().substring(0, 8));
            if ((id / 10000 == 3 || id / 10000 == 4) && id % 10 == 0) {
                sb.append(id).append(", ");
            }
        }
        System.out.println("讀取眼睛 :");
        sb.append("\r\n\r\n");       
        sb.append("男眼:\r\n");
        final MapleDataDirectoryEntry root2 = faceSource.getRoot();
        for (MapleDataFileEntry topDir2 : root2.getFiles()) {
            int id = Integer.parseInt(topDir2.getName().substring(0, 8));
            if (id / 10000 == 2) {
                sb.append(id).append(", ");
            }
        }
       
        sb.append("\r\n\r\n");
        sb.append("已完成讀取\r\n");
        out.write(sb.toString().getBytes());
    }
} 
