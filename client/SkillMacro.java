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
package client;

import java.io.Serializable;
//import static java.lang.reflect.Array.getChar;

public class SkillMacro implements Serializable {

    private static final long serialVersionUID = -63413738569L;
    private int macroId;
    private int skill1;
    private int skill2;
    private int skill3;
    private String name;
    private int shout;
    private int position;


    public SkillMacro(int skill1, int skill2, int skill3, String name, int shout, int position) {
        this.skill1 = skill1;
        this.skill2 = skill2;
        this.skill3 = skill3;
        this.name = fliters(name);
        this.shout = shout;
        this.position = position;
    }
    
    
    public String fliters (String s){
        StringBuilder fliter = new StringBuilder();
        for (int j = 0, len = s.length(); j < len; j++) {
            char outword = s.charAt(j);
            if ((outword == 0x0)
                || (outword == 0x9)
                || (outword == 0xA)
                || (outword == 0xD)
                || ((outword >= 0x20) && (outword <= 0xD7FF))
                || ((outword >= 0xE000) && (outword <= 0xFFFD))
            ) {
                fliter.append(outword);
            }
        }
        return s = fliter.toString();
    }
    
    
    public int getMacroId() {
        return macroId;
    }

    public int getSkill1() {
        return skill1;
    }

    public int getSkill2() {
        return skill2;
    }

    public int getSkill3() {
        return skill3;
    }

    public String getName() {
        return name;
    }

    public int getShout() {
        return shout;
    }

    public int getPosition() {
        return position;
    }

    public void setMacroId(int macroId) {
        this.macroId = macroId;
    }

    public void setSkill1(int skill1) {
        this.skill1 = skill1;
    }

    public void setSkill2(int skill2) {
        this.skill2 = skill2;
    }

    public void setSkill3(int skill3) {
        this.skill3 = skill3;
    }

    public void setName(String s) {
        fliters(s);
        this. name = s;
    }

    public void setShout(int shout) {
        this.shout = shout;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
