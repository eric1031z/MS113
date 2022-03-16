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
package provider;

import java.awt.Point;
import java.awt.image.BufferedImage;

import provider.WzXML.MapleDataType;
import tools.FilePrinter;

public class MapleDataTool {

    public static String getString(MapleData data) {
        return ((String) data.getData());
    }

    public static String getString(MapleData data, String def) {
        if (data == null || data.getData() == null) {
            return def;
        } else {
            try {
                String d = (String) data.getData();
            } catch (Exception e) {
                System.err.println(data.getParent().getName());
            }
            
            return ((String) data.getData());
        }
    }

    public static String getString(String path, MapleData data) {
        return getString(data.getChildByPath(path));
    }

    public static String getString(String path, MapleData data, String def) {
        return getString(data.getChildByPath(path), def);
    }

    public static double getDouble(MapleData data) {
        return ((Double) data.getData());
    }

    public static float getFloat(MapleData data) {
        return ((Float) data.getData()).floatValue();
    }

    public static float getFloat(MapleData data, float def) {
        if (data == null || data.getData() == null) {
            return def;
        } else {
            return ((Float) data.getData()).floatValue();
        }
    }

    public static int getInt(MapleData data) {
        Object get = data.getData();
        try {
            return Integer.parseInt(get.toString());
        } catch (Exception ex) {
            FilePrinter.printError("MapleDataTool.txt", ex, "getInt");
            ex.printStackTrace();
        }
        return 0;
    }

    public static int getInt(MapleData data, int def) {
        if (data == null || data.getData() == null) {
            return def;
        } else {
            if (null == data.getType()) {
                return ((Integer) data.getData()).intValue();
            } else switch (data.getType()) {
                case STRING:
                    return Integer.parseInt(getString(data));
                case SHORT:
                    return Integer.valueOf(((Short) data.getData()).shortValue());
                default:
                    return ((Integer) data.getData()).intValue();
            }
        }
    }

    public static int getInt(String path, MapleData data) {
        return getInt(data.getChildByPath(path));
    }

    public static int getIntConvert(MapleData data) {
        if (data.getType() == MapleDataType.STRING) {
            return Integer.parseInt(getString(data));
        } else {
            return getInt(data);
        }
    }

    public static int getIntConvert(String path, MapleData data) {
        return getIntConvert(data.getChildByPath(path));
    }

    public static int getIntConvert(MapleData d, int def) {
        try {
            return getIntConvert(d);
        } catch (Exception ex) {
            return def;
        }
    }

    public static int getInt(String path, MapleData data, int def) {
        return getInt(data.getChildByPath(path), def);
    }

    public static int getIntConvert(String path, MapleData data, int def) {
        if (data == null) {
            return def;
        }
        MapleData d = data.getChildByPath(path);
        if (d == null) {
            return def;
        }
        if (d.getType() == MapleDataType.STRING) {
            try {
                return Integer.parseInt(getString(d));
            } catch (NumberFormatException nfe) {
                return def;
            }
        } else {
            return getInt(d, def);
        }
    }

    public static long getLong(MapleData data, long def) {
        if (data == null || data.getData() == null) {
            return def;
        } else {
            if (null == data.getType()) {
                return ((Long) data.getData());
            } else switch (data.getType()) {
                case STRING:
                    return Long.parseLong(getString(data));
                case SHORT:
                    return Long.valueOf(((Short) data.getData()));
                default:
                    return ((Long) data.getData());
            }
        }
    }

    public static BufferedImage getImage(MapleData data) {
        return ((MapleCanvas) data.getData()).getImage();
        
    }

    public static Point getPoint(MapleData data) {
        return ((Point) data.getData());
    }

    public static Point getPoint(String path, MapleData data) {
        return getPoint(data.getChildByPath(path));
        
    }

    public static Point getPoint(String path, MapleData data, Point def) {
        final MapleData pointData = data.getChildByPath(path);
        if (pointData == null) {
            return def;
        }
        return getPoint(pointData);
    }

    public static String getFullDataPath(MapleData data) {
        String path = "";
        MapleDataEntity myData = data;
        while (myData != null) {
            path = myData.getName() + "/" + path;
            myData = myData.getParent();
        }
        return path.substring(0, path.length() - 1);
    }
}
