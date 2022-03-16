var h2=-1;
var h=-1;
var h1=-1;
var hh=-1;


function start() {
 status=-1;
 action(1, 0, 0);
}


function ask0(selection){
	cm.sendSimple("有甚麼需要服務呢?\r\n#L0##d我想要進入美髮相簿#l#k");
}

function ask1(selection){
	var c=cm.getPlayer();
    var A=c.getHair();
    var B=cm.getSavedHair(0);
    var C=cm.getSavedHair(1);
	var D=cm.getSavedHair(2);
	var E=cm.getSavedHair(3);
	var F=cm.getSavedHair(4);
	var a="#fCharacter/Hair/000"+cm.getPlayer().getHair()+".img/default/hairOverHead#";
	var b="#fCharacter/Hair/000"+cm.getSavedHair(0)+".img/default/hairOverHead#";
	var c="#fCharacter/Hair/000"+cm.getSavedHair(1)+".img/default/hairOverHead#";
	var d="#fCharacter/Hair/000"+cm.getSavedHair(2)+".img/default/hairOverHead#";
	var e="#fCharacter/Hair/000"+cm.getSavedHair(3)+".img/default/hairOverHead#";
	var f="#fCharacter/Hair/000"+cm.getSavedHair(4)+".img/default/hairOverHead#";
	var AA="#r#t"+cm.getPlayer().getHair()+"##k";
	var BB="#r#t"+cm.getSavedHair(0)+"##k";
	var CC="#r#t"+cm.getSavedHair(1)+"##k";
	var DD="#r#t"+cm.getSavedHair(2)+"##k";
	var EE="#r#t"+cm.getSavedHair(3)+"##k";
	var FF="#r#t"+cm.getSavedHair(4)+"##k";
	if(cm.getSavedHair(0)==-1){
	   BB="#d尚未儲存髮型#k";
	}
	if(cm.getSavedHair(1)==-1){
	   CC="#d尚未儲存髮型#k";
	}
	if(cm.getSavedHair(2)==0000){
	   DD="#d尚未儲存髮型#k";
	}
	if(cm.getSavedHair(3)==0000){
	   EE="#d尚未儲存髮型#k";
	}
	if(cm.getSavedHair(4)==0000){
	   FF="#d尚未儲存髮型#k";
	}
	if(cm.getSavedHair(2)==-1){
	   DD="#b尚未開通此儲存格#k";
	}
	if(cm.getSavedHair(3)==-1){
	   EE="#b尚未開通此儲存格#k";
	}
	if(cm.getSavedHair(4)==-1){
	   FF="#b尚未開通此儲存格#k";
	}
	switch(hh){
	case 0:	
	cm.sendSimple("#e歡迎來到咕咕雞美髮相簿,以下是你已儲存的髮型喔:\r\n\r\n目前的髮型:#b#t"+A+"##k\r\n=========================================\r\n#L0#第一儲存位:"+BB+"  #l#L20#預覽#l\r\n\r\n#L1#第二儲存位:"+CC+"  #l#L21#預覽#l\r\n\r\n#L2#第三儲存位:"+DD+"  #l#L22#預覽#l\r\n\r\n#L3#第四儲存位:"+EE+"  #l#L23#預覽#l\r\n\r\n#L4#第五儲存位:"+FF+"  #l#L24#預覽#l");
}
}

function ask2(selection){
var c=cm.getPlayer();
    var A=c.getHair();
    var B=cm.getSavedHair(0);
    var C=cm.getSavedHair(1);
	var D=cm.getSavedHair(2);
	var E=cm.getSavedHair(3);
	var F=cm.getSavedHair(4);
	var a="#fCharacter/Hair/000"+cm.getPlayer().getHair()+".img/default/hairOverHead#";
	var b="#fCharacter/Hair/000"+cm.getSavedHair(0)+".img/default/hairOverHead#";
	var c="#fCharacter/Hair/000"+cm.getSavedHair(1)+".img/default/hairOverHead#";
	var d="#fCharacter/Hair/000"+cm.getSavedHair(2)+".img/default/hairOverHead#";
	var e="#fCharacter/Hair/000"+cm.getSavedHair(3)+".img/default/hairOverHead#";
	var f="#fCharacter/Hair/000"+cm.getSavedHair(4)+".img/default/hairOverHead#";
	if(selection==20&&cm.getSavedHair(0)!=-1){
		cm.sendOk(b);
        cm.dispose();
	}
	if(selection==21&&cm.getSavedHair(1)!=-1){
		cm.sendOk(c);
		cm.dispose();
	}
	if(selection==22&&cm.getSavedHair(2)!=-1&&cm.getSavedHair(2)!=0000){
		cm.sendOk(d);
		cm.dispose();
	}
	if(selection==23&&cm.getSavedHair(3)!=-1&&cm.getSavedHair(3)!=0000){
		cm.sendOk(e);
		cm.dispose();
	}
	if(selection==24&&cm.getSavedHair(4)!=-1&&cm.getSavedHair(4)!=0000){
		cm.sendOk(f);
		cm.dispose();
	}
	if(selection==20&&cm.getSavedHair(0)==-1){
		cm.sendOk("此儲存格尚未儲存髮型");
        cm.dispose();
	}
	if(selection==21&&cm.getSavedHair(1)==-1){
		cm.sendOk("此儲存格尚未儲存髮型");
		cm.dispose();
	}
	if(selection==22&&cm.getSavedHair(2)==-1){
		cm.sendOk("此儲存格尚未開通");
		cm.dispose();
	}
	if(selection==23&&cm.getSavedHair(3)==-1){
		cm.sendOk("此儲存格尚未開通");
		cm.dispose();
	}
	if(selection==24&&cm.getSavedHair(4)==-1){
		cm.sendOk("此儲存格尚未開通");
		cm.dispose();
	}
	if(selection==22&&cm.getSavedHair(2)==0000){
		cm.sendOk("此儲存格尚未儲存髮型");
		cm.dispose();
	}
	if(selection==23&&cm.getSavedHair(3)==0000){
		cm.sendOk("此儲存格尚未儲存髮型");
		cm.dispose();
	}
	if(selection==24&&cm.getSavedHair(4)==0000){
		cm.sendOk("此儲存格尚未儲存髮型");
		cm.dispose();
	}
	if(selection==2&&cm.getSavedHair(2)==-1){
		cm.sendOk("您還沒有開通此欄位喔,需要花費#r500GASH#k開通嗎?\r\n#L3##d好#k\r\n#L4##d不要#k\r\n#L9999##d我要用#i5150038##t5150038開通#");
	}if(selection==3&&cm.getSavedHair(3)==-1){
		cm.sendOk("您還沒有開通此欄位喔,需要花費#r1000GASH#k開通嗎?\r\n#L5##d好#k\r\n#L6##d不要#k\r\n#L10000##d我要用#i5150038##t5150038開通#");
	}if(selection==4&&cm.getSavedHair(4)==-1){
		cm.sendOk("您還沒有開通此欄位喔,需要花費#r1500GASH#k開通嗎?\r\n#L7##d好#k\r\n#L8##d不要#k\r\n#L10001##d我要用#i5150038##t5150038開通#");
	}
    if(selection==1){
	cm.sendOk("請問需要什麼服務呢?\r\n#L0##d換成此髮型#k\r\n#L1##d把目前髮型儲存至此欄位#k\r\n#L2##d清除此儲存格#k");
	}
	if(selection==0){
	cm.sendOk("請問需要什麼服務呢?\r\n#L0##d換成此髮型#k\r\n#L1##d把目前髮型儲存至此欄位#k\r\n#L2##d清除此儲存格#k");
	}
	if(selection==2&&cm.getSavedHair(2)!=-1){
	cm.sendOk("請問需要什麼服務呢?\r\n#L0##d換成此髮型#k\r\n#L1##d把目前髮型儲存至此欄位#k\r\n#L2##d清除此儲存格#k");
}
    if(selection==3&&cm.getSavedHair(3)!=-1){
	cm.sendOk("請問需要什麼服務呢?\r\n#L0##d換成此髮型#k\r\n#L1##d把目前髮型儲存至此欄位#k\r\n#L2##d清除此儲存格#k");
}
    if(selection==4&&cm.getSavedHair(4)!=-1){
	cm.sendOk("請問需要什麼服務呢?\r\n#L0##d換成此髮型#k\r\n#L1##d把目前髮型儲存至此欄位#k\r\n#L2##d清除此儲存格#k");
}
}

function ask3(selection){
    var c=cm.getPlayer();
    var A=c.getHair();
    var B=cm.getSavedHair(0);
    var C=cm.getSavedHair(1);
	var D=cm.getSavedHair(2);
	var E=cm.getSavedHair(3);
	var F=cm.getSavedHair(4);
	var gash=-1;
	var c = cm.getPlayer();
    gash = c.getCSPoints(1);
	var a="#fCharacter/Hair/000"+cm.getPlayer().getHair()+".img/default/hairOverHead#";
	var b="#fCharacter/Hair/000"+cm.getSavedHair(0)+".img/default/hairOverHead#";
	var c="#fCharacter/Hair/000"+cm.getSavedHair(1)+".img/default/hairOverHead#";
	var d="#fCharacter/Hair/000"+cm.getSavedHair(2)+".img/default/hairOverHead#";
	var e="#fCharacter/Hair/000"+cm.getSavedHair(3)+".img/default/hairOverHead#";
	var f="#fCharacter/Hair/000"+cm.getSavedHair(4)+".img/default/hairOverHead#";
	var g1;
	if(h==0&&cm.getSavedHair(0)!=-1){
		g1="#fCharacter/Hair/000"+cm.getSavedHair(0)+".img/default/hairOverHead#";
	}if(h==1&&cm.getSavedHair(1)!=-1){
		g1="#fCharacter/Hair/000"+cm.getSavedHair(1)+".img/default/hairOverHead#";
	}if(h==2&&cm.getSavedHair(2)!=-1){
		g1="#fCharacter/Hair/000"+cm.getSavedHair(2)+".img/default/hairOverHead#";
	}if(h==3&&cm.getSavedHair(3)!=-1){
		g1="#fCharacter/Hair/000"+cm.getSavedHair(3)+".img/default/hairOverHead#";
	}if(h==4&&cm.getSavedHair(4)!=-1){
		g1="#fCharacter/Hair/000"+cm.getSavedHair(4)+".img/default/hairOverHead#";
	}if(h==0&&cm.getSavedHair(0)==-1){
		g1="";
	}if(h==1&&cm.getSavedHair(1)==-1){
		g1="";
	}if(h==2&&cm.getSavedHair(2)==-1){
		g1="";
	}if(h==3&&cm.getSavedHair(3)==-1){
		g1="";
	}if(h==4&&cm.getSavedHair(4)==-1){
		g1="";
	}if(h==2&&cm.getSavedHair(2)==0000){
		g1="";
	}if(h==3&&cm.getSavedHair(3)==0000){
		g1="";
	}if(h==4&&cm.getSavedHair(4)==0000){
		g1="";
	}
	

	var g;
	if(h==0){
		g="#t"+cm.getSavedHair(0)+"#";
	}if(h==1){
		g="#t"+cm.getSavedHair(1)+"#";
	}if(h==2){
		g="#t"+cm.getSavedHair(2)+"#";
	}if(h==3){
		g="#t"+cm.getSavedHair(3)+"#";
	}if(h==4){
		g="#t"+cm.getSavedHair(4)+"#";
	}
	var a1="#t"+cm.getPlayer().getHair()+"#";
	

	    if(h1==0){
		     cm.sendOk("確定要更改成#r"+g+"#k"+g1+"髮型嗎?\r\n換一次需要#r50萬#k楓幣喔!\r\n#L0##b確定#k\r\n#L1##b不要好了#k");
		}
		if(h1==1){
             cm.sendOk("確定要把#r"+a1+"#k"+a+"儲存至此儲存格嗎?\r\n若此儲存格#d#e已有存取髮型將會覆蓋喔#k#n\r\n#L2##b確定#k\r\n#L3##b不要好了#k");		
		}
	    if(h1==2){
		     cm.sendOk("確定要清除#r"+g+"#k"+g1+"嗎?\r\n#e#d若清除了將不會恢復#k#n\r\n#L4##b確定#k\r\n#L5##b不要好了#k");
		}
		if(h1==3&&h==2){
             if(gash<500){
               cm.sendOk("你的錢不夠");
			   cm.dispose();
			 }else if (gash>=500){
				   cm.getPlayer().modifyCSPoints(1,-500,true);
                   cm.setSavedHair(2,0000);	
				   cm.setHair(A);
				   cm.dispose();	 
		}
		}
        if(h1==5&&h==3){
             if(gash<1000){
               cm.sendOk("你的錢不夠");
			   cm.dispose();
			 }else if(gash>=1000){
				   cm.getPlayer().modifyCSPoints(1,-1000,true);
                   cm.setSavedHair(3,0000);	
				   cm.setHair(A);
				   cm.dispose();	 
		}
		}
        if(h1==7&&h==4){
             if(gash<1500){
               cm.sendOk("你的錢不夠");
			   cm.dispose();
			 }else if(gash>=1500){
				   cm.getPlayer().modifyCSPoints(1,-1500,true);
                   cm.setSavedHair(4,0000);	
				   cm.setHair(A);
				   cm.dispose();     		
	    }
		}
		if(h1==9999&&h==2){
			if(cm.haveItem(5150038,1)){
				cm.sendOk("已為您開通欄位");
				cm.gainItem(5150038,-1);
				cm.setSavedHair(2,0000);	
				cm.setHair(A);
				cm.dispose();
			}
            if(!cm.haveItem(5150038)){
                cm.sendOk("您身上並沒有#i5150038##t5150038#");
                cm.dispose();
			}
		}
         if(h1==10000&&h==3){
			if(cm.haveItem(5150038,1)){
				cm.sendOk("已為您開通欄位");
				cm.gainItem(5150038,-1);
				cm.setSavedHair(3,0000);	
				cm.setHair(A);
				cm.dispose();
			}
            if(!cm.haveItem(5150038)){
                cm.sendOk("您身上並沒有#i5150038##t5150038#");
                cm.dispose();
			}
			} 
		 if(h1==10001&&h==4){
			if(cm.haveItem(5150038,1)){
				cm.sendOk("已為您開通欄位");
				cm.gainItem(5150038,-1);
				cm.setSavedHair(4,0000);	
				cm.setHair(A);
				cm.dispose();
			}
            if(!cm.haveItem(5150038)){
                cm.sendOk("您身上並沒有#i5150038##t5150038#");
                cm.dispose();
			}
			}  
	     if(h1==4||h1==6||h1==8){
			 cm.sendOk("再考慮一下吧!美麗是值得保留的");
			 cm.dispose();
		 }
}

	
function setup(mode, type, selection){
	var c=cm.getPlayer();
    var A=c.getHair();
    var B=cm.getSavedHair(0);
    var C=cm.getSavedHair(1);
	var D=cm.getSavedHair(2);
	var E=cm.getSavedHair(3);
	var F=cm.getSavedHair(4);
	var gash=-1;
	var c = cm.getPlayer();
    gash = c.getCSPoints(1);
	var a="#fCharacter/Hair/000"+cm.getPlayer().getHair()+".img/default/hairOverHead#";
	var b="#fCharacter/Hair/000"+cm.getSavedHair(0)+".img/default/hairOverHead#";
	var c="#fCharacter/Hair/000"+cm.getSavedHair(1)+".img/default/hairOverHead#";
	var d="#fCharacter/Hair/000"+cm.getSavedHair(2)+".img/default/hairOverHead#";
	var e="#fCharacter/Hair/000"+cm.getSavedHair(3)+".img/default/hairOverHead#";
	var f="#fCharacter/Hair/000"+cm.getSavedHair(4)+".img/default/hairOverHead#";
	var AA="#t"+cm.getPlayer().getHair()+"#";
	var BB="#t"+cm.getSavedHair(0)+"#";
	var CC="#t"+cm.getSavedHair(1)+"#";
	var DD="#t"+cm.getSavedHair(2)+"#";
	var EE="#t"+cm.getSavedHair(3)+"#";
	var FF="#t"+cm.getSavedHair(4)+"#";


	    if(h==0&&h2==0&&h1==0){
		  if(cm.getSavedHair(0)==-1){
             cm.sendOk("此儲存格尚未儲存髮型");
             cm.dispose();
		  }
		  if(cm.getSavedHair(0)==cm.getPlayer().getHair()){
			cm.sendOk("你目前就是這個頭髮了喔!");
			cm.dispose();
		  }
	      if(cm.getMeso()>=500000&&cm.getSavedHair(0)!=-1&&cm.getSavedHair(0)!=cm.getPlayer().getHair()){
		  cm.setHair(B);
	      cm.dispose();
		  cm.gainMeso(-500000);
		  }else cm.sendOk("您的錢不夠");
	      cm.dispose();
		}

	    if(h==1&&h2==0&&h1==0){
		  if(cm.getSavedHair(1)==-1){
             cm.sendOk("此儲存格尚未儲存髮型");
             cm.dispose();
		  }	
		  if(cm.getSavedHair(1)==cm.getPlayer().getHair()){
			cm.sendOk("你目前就是這個頭髮了喔!");
			cm.dispose();
		  }
		  if(cm.getMeso()>=500000&&cm.getSavedHair(1)!=-1&&cm.getSavedHair(1)!=cm.getPlayer().getHair()){
		  cm.setHair(C);
	      cm.dispose();
		  cm.gainMeso(-500000);
		  }else cm.sendOk("您的錢不夠");
	      cm.dispose();
		}
		if(h==2&&h2==0&&h1==0){
		  if(cm.getSavedHair(2)==-1){
             cm.sendOk("此儲存格尚未開啟");
             cm.dispose();
		  }
		  if(cm.getSavedHair(2)==0000){
             cm.sendOk("此儲存格尚未儲存髮型");
             cm.dispose();
		  }
		  if(cm.getSavedHair(2)==cm.getPlayer().getHair()){
			cm.sendOk("你目前就是這個頭髮了喔!");
			cm.dispose();
		  }
		  if(cm.getMeso()>=500000&&cm.getSavedHair(2)!=-1&&cm.getSavedHair(2)!=cm.getPlayer().getHair()&&cm.getSavedHair(2)!=0000){
		  cm.setHair(D);
	      cm.dispose();
		  cm.gainMeso(-500000);
		  }else cm.sendOk("您的錢不夠");
	      cm.dispose();
		}if(h==3&&h2==0&&h1==0){
		  if(cm.getSavedHair(3)==-1){
             cm.sendOk("此儲存格尚未開啟");
             cm.dispose();
		  }
		  if(cm.getSavedHair(3)==0000){
             cm.sendOk("此儲存格尚未儲存髮型");
             cm.dispose();
		  }
		  if(cm.getSavedHair(3)==cm.getPlayer().getHair()){
			cm.sendOk("你目前就是這個頭髮了喔!");
			cm.dispose();
		  }
          if(cm.getMeso()>=500000&&cm.getSavedHair(3)!=-1&&cm.getSavedHair(3)!=cm.getPlayer().getHair()&&cm.getSavedHair(3)!=0000){
		  cm.setHair(E);
	      cm.dispose();
		  cm.gainMeso(-500000);
		  }else cm.sendOk("您的錢不夠");
	      cm.dispose();
		}if(h==4&&h2==0&&h1==0){
		  if(cm.getSavedHair(4)==-1){
             cm.sendOk("此儲存格尚未開啟");
             cm.dispose();
		  }
		  if(cm.getSavedHair(4)==0000){
             cm.sendOk("此儲存格尚未儲存髮型");
             cm.dispose();
		  }
		  if(cm.getSavedHair(4)==cm.getPlayer().getHair()){
			cm.sendOk("你目前就是這個頭髮了喔!");
			cm.dispose();
		  }
          if(cm.getMeso()>=500000&&cm.getSavedHair(4)!=-1&&cm.getSavedHair(4)!=cm.getPlayer().getHair()&&cm.getSavedHair(4)!=0000){
		  cm.setHair(F);
	      cm.dispose();
		  cm.gainMeso(-500000);
		  }else cm.sendOk("您的錢不夠");
	      cm.dispose();	
		}if(h==0&&h2==2&&h1==1){
			 cm.setSavedHair(0,A);
		     cm.getSavedHair(0);
		     cm.setHair(A);
		     cm.dispose();
		}if(h==1&&h2==2&&h1==1){
			  cm.setSavedHair(1,A);
		      cm.getSavedHair(1);
		      cm.setHair(A);
		      cm.dispose();
		}if(h==2&&h2==2&&h1==1){
			  cm.setSavedHair(2,A);
		      cm.getSavedHair(2);
		      cm.setHair(A);
		      cm.dispose();
		}if(h==3&&h2==2&&h1==1){
			  cm.setSavedHair(3,A);
		      cm.getSavedHair(3);
		      cm.setHair(A);
		      cm.dispose();
		}if(h==4&&h2==2&&h1==1){
			  cm.setSavedHair(4,A);
		      cm.getSavedHair(4);
		      cm.setHair(A);
			  cm.dispose();
		}if(h==0&&h2==4&&h1==2){
			  if(cm.getSavedHair(0)==-1){
				  cm.sendOk("此儲存格無髮型可刪除");
			  }else
			  cm.setSavedHair(0,-1);
              cm.getSavedHair(0);	
              cm.setHair(A);					
              cm.dispose();
		}if(h==1&&h2==4&&h1==2){
			  if(cm.getSavedHair(1)==-1){
			      cm.sendOk("此儲存格無髮型可刪除");
			  }else
			  cm.setSavedHair(1,-1);
              cm.getSavedHair(1);	
              cm.setHair(A);					
              cm.dispose();
		}if(h==2&&h2==4&&h1==2){
			  if(cm.getSavedHair(2)==0000){
			      cm.sendOk("此儲存格無髮型可刪除");
			  }else
			  cm.setSavedHair(2,0000);
              cm.getSavedHair(2);	
              cm.setHair(A);					
              cm.dispose();
		}if(h==3&&h2==4&&h1==2){
			  if(cm.getSavedHair(3)==0000){
			      cm.sendOk("此儲存格無髮型可刪除");
			  }else
			  cm.setSavedHair(3,0000);
              cm.getSavedHair(3);	
              cm.setHair(A);					
              cm.dispose();
		}if(h==4&&h2==4&&h1==2){
			  if(cm.getSavedHair(4)==0000){
			      cm.sendOk("此儲存格無髮型可刪除");
			  }else
			  cm.setSavedHair(4,0000);
              cm.getSavedHair(4);	
              cm.setHair(A);					
              cm.dispose();
		}     
        if(h2==1|h2==3||h2==5){
          cm.sendOk("沒關係!");
		  cm.dispose();
		}
	}


			
	
		

function action(mode, type, selection){
	var c=cm.getPlayer();
    var A=c.getHair();
    var B=cm.getSavedHair(0);
    var C=cm.getSavedHair(1);
	var D=cm.getSavedHair(2);
	var E=cm.getSavedHair(3);
	var F=cm.getSavedHair(4);
    
	if (mode == 1) {
            status++; 
	}else 
            status--; 
	if(status==0){
        ask0(selection);
	}		
	else if(status==1){
		hh=selection;
		ask1(selection);
	}else if(status == 2){
		h=selection;
		ask2(selection);
	}else if(status == 3){
		h1=selection;
		ask3(selection);
	}else if(status == 4){
		h2=selection;
		setup(mode,type,selection);
	}
}