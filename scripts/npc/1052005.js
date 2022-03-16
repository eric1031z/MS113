var gash=-1;

function start() { 
    status = -1; 
    action(1, 0, 0); 
} 

function action(mode, type, selection) { 
    var c = cm.getPlayer();
    gash = c.getCSPoints(1);
    if (mode == -1) { 
        cm.dispose(); 
    } else { 
        if (status >= 0 && mode == 0) { 
            cm.dispose(); 
            return; 
        } 
        if (mode == 1) 
            status++; 
        else 
            status--; 
        if (status == 0) { 
            cm.sendNext("#e你好!我是 #d咕咕雞谷#k 變性手術醫生嘿嘿\r\n動完手術記得換頻檢查一下呦"); 
        } else if (status == 1) { 
            cm.sendSimple("#e你想轉換成啥性別呢? 換一次要1萬GASH喔! 新人類性別讓你可以穿 #r男#k + #r女#k 兩種裝備, 還有一切特別的裝備. 所以新人類會比較貴一點,所以你要支付15000GASH!\r\n\r\n#b#L0#男(1萬GASH)#l\r\n#L1#女(1萬GASH)#l\r\n#L2#新人類(15000GASH)#l"); 
        } else if (selection == 0) { 
            if (gash>=10000) { 
                if (cm.getPlayer().getGender() == 0) { 
                    cm.sendOk("#e你已經是 #r男生#k 了!"); 
                    cm.dispose(); 
                } else { 
                    cm.getPlayer().setGender(0); 
                    cm.getPlayer().modifyCSPoints(1, -10000, true); 
					cm.sendOk("你已經變得跟咕咕雞一樣醜了");
                    cm.dispose(); 
                } 
            } else { 
                cm.sendOk("#e你錢不夠還想變性?"); 
                cm.dispose(); 
            } 
        } else if (selection == 1) { 
            if (gash>=10000) { 
                if (cm.getPlayer().getGender() == 1) { 
                    cm.sendOk("#e你已經是 #r女生#k 了!"); 
                    cm.dispose(); 
                } else { 
                    cm.getPlayer().setGender(1); 
                    cm.getPlayer().modifyCSPoints(1, -10000, true); 
					cm.sendOk("妳已經變得跟仙女一樣美了!");
                    cm.dispose(); 
                } 
            } else { 
                cm.sendOk("#e你錢不夠還想變性?"); 
                cm.dispose(); 
            } 
        } else if (selection == 2) { 
            if (gash>=15000) { 
                if (cm.getPlayer().getGender() == 2) { 
                    cm.sendOk("#e你已經是 #r新人類#k 了!"); 
                    cm.dispose(); 
                } else { 
                    cm.getPlayer().setGender(2); 
                    cm.getPlayer().modifyCSPoints(1, -15000, true); 
					cm.sendOk("媽的人妖= =");
                    cm.dispose(); 
                } 
            } else { 
                cm.sendOk("#e你錢不夠還想變新人類?"); 
                cm.dispose(); 
            } 
        } 
    } 
}  