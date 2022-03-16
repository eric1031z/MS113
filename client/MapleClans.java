package client;

/**
*
* @author LYZ
*/

public enum MapleClans {
None(0, "無職業", 0, "null"),
奇才(100,"奇才",0,""),
書生(110,"書生",0,""),
武人(120,"武人",0,""),
權威(130,"權威",0,""),
狂人(140,"狂人",0,""),
異類(200,"異類",0,""),
流氓(210,"流氓",0,""),
商人(220,"商人",0,""),
組頭(230,"組頭",0,""),
匠人(240,"匠人",0,""),
天才(300,"天才",2301004,""),
咒師(310,"咒師",3121008,""),
法師(320,"法師",2321004,""),
魔導(330,"魔導",3121002,""),
賢者(340,"賢者",5121009,""),
網紅(400,"網紅",0,""),
造型師(410,"造型師",0,""),
藝術家(420,"藝術家",0,""),
設計師(430,"設計師",0,""),
鑑賞家(440,"鑑賞家",0,"");

final int clanid;
final String clanname;
final int skillBuff;
final String skillName;

private MapleClans(int id, String name, int skillB, String skillN){
clanid = id;
clanname = name;
skillBuff = skillB;
skillName = skillN;
}

public static MapleClans getById(int id) {
for (MapleClans mc : MapleClans.values()) {
if (mc.getId() == id) {
return mc;
}
}
return null;
}

public int getId(){
return clanid;
}

public String getName(){
return clanname;
}

public int getSkillBuff(){
return skillBuff;
}

public String getSkillName(){
return skillName;
}
}  