var fee;

var dealerCards = [];
var playerCards = [];

var CARDS = ["2","3","4","5","6","7","8","9","10","J","Q","K","A"];

var status = 0;

function showHand(cards) {
    var ret = "#b[";
    for (var i = 0;i < cards.length;i++) {
        ret += cards[i];
        if (i != cards.length - 1)
            ret += " ";
    }
    ret += "] [總價值: "+calcHandValue(cards)+"]#k";
    return ret;
}

function calcHandValue(cards) {
    if (cards.length == 2) { //Figure out BlackJack first =.=;;
        if (cards[0] == "A" && (cards[1] == "10" || cards[1] == "J" || cards[1] == "Q" || cards[1] == "K"))
            return "21點";
        if (cards[1] == "A" && (cards[0] == "10" || cards[0] == "J" || cards[0] == "Q" || cards[0] == "K"))
            return "21點";
    }
    var ret = 0;
    var numAces = 0; //Stupid variable aces....
    for (var i = 0;i < cards.length;i++)
        if (!isNaN(cards[i]))
            ret += Number(cards[i]);
        else {
            if (cards[i] == "A") {
                numAces++;
                ret += 11;
            } else
                ret += 10;
        }
    while (numAces > 0 && ret > 21) {
        numAces --;
        ret -= 10;
    }
    return ret;
}

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1)
        cm.dispose();
    else {
        if (mode == 0) {
            cm.sendOk("小孬孬?");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0)
            cm.sendNext("想要玩 #b21點?#k 如果你贏了你就能獲得1.78倍楓幣!莊家從17點開始.");
        else if (status == 1)
            cm.sendGetText("你想要賭多少楓幣?");
        else if (status == 2) {
            fee = cm.getText();
            if (isNaN(fee) || fee == "" || fee < 0) {
                cm.sendOk("你這樣母湯喔");
                cm.dispose();
			}
			if (fee>=100000000){
                cm.sendOk("你想賭#r#e"+fee+"#k#n楓幣???\r\n賭太多會傷身體喔");
                 cm.dispose();				
            } else
                cm.sendYesNo("你確定你要賭 #r" + fee + "#k 楓幣嗎?? ");
        } else if (status == 3) {
            if (cm.getMeso() < fee) {
                cm.sendOk("你他媽根本沒那麼多錢.");
                cm.dispose();
            } else {
                cm.gainMeso(-fee); //We take it away now! RAWR!
                dealerCards.push(CARDS[Math.floor(Math.random() * CARDS.length)]);
                dealerCards.push(CARDS[Math.floor(Math.random() * CARDS.length)]);
                playerCards.push(CARDS[Math.floor(Math.random() * CARDS.length)]);
                playerCards.push(CARDS[Math.floor(Math.random() * CARDS.length)]);
                cm.sendNext("莊家秀出了 " + dealerCards[1] + "該你嚕");
            }
        } else if (status == 4) {
            var sendStr = "你手中有 "+showHand(playerCards)+".";
            if (calcHandValue(playerCards) == "21點") {
                sendStr += "\r\n水喔,你有21點,來看看你贏了沒";
                status ++; //Skip to score calc.
                cm.sendNext(sendStr);
            } else {
                sendStr += "\r\n\r\n你接下來想要幹嘛勒? \r\n#L0#加牌.#l\r\n#L1#停牌.#l";
                cm.sendSimple(sendStr);
            }
        } else if (status == 5) {
            if (selection == 0) {
                status--;
                playerCards.push(CARDS[Math.floor(Math.random() * CARDS.length)]);
                sendStr = "你手中有 "+showHand(playerCards)+".";
                var val = calcHandValue(playerCards);
                if (val > 21) {
                    sendStr += "\r\n哈哈哈哈哈你點數爆了!你看看你.";
                    cm.sendOk(sendStr);
                    cm.dispose();
                } else if (val == 21) {
                    sendStr += "\r\n恭喜你,來看看你贏了嗎";
                    status++; //Go straight to 6.
                    cm.sendNext(sendStr);
                } else {
                    sendStr += "\r\n\r\n你接下來想要幹嘛勒? \r\n#L0#加牌.#l\r\n#L1#停牌.#l";
                    cm.sendSimple(sendStr);
                }
            } else {
                status++;
            }
        } else if (status != 6) { //If we get here but the status isn't 6, something messed up.
            cm.dispose();
            return;
        }
        //This is purposefully not part of the else-if chain.
        if (status == 6) {
            sendStr = "莊家手中是: ";
            if (calcHandValue(dealerCards) == "21點") {
                sendStr += showHand(dealerCards);
                cm.sendOk(sendStr+".\r\n#r抱歉你輸啦!哭哭喔!");
                cm.dispose();
                return;
                //We really should penalize the player for 50% more.. but that'll be mean xD
            }
            while (calcHandValue(dealerCards) < 17)
                dealerCards.push(CARDS[Math.floor(Math.random() * CARDS.length)]);
            sendStr += showHand(dealerCards);
            if (calcHandValue(playerCards) == "21點") {
                cm.sendOk(sendStr+".\r\n#r太強了吧= = 你爆贏了1.5倍的獎金.");
                cm.gainMeso(2 * fee);
                cm.dispose();
                return;
            }
            var fDTot = calcHandValue(dealerCards);
            var fPTot = calcHandValue(playerCards);
            if (fDTot > 21) {
                cm.sendOk(sendStr+".\r\n#r莊家超過21點了! 你贏了!爽拿賭金.");
                cm.gainMeso(1.78 * fee);
            } else if (fDTot > fPTot)
                cm.sendOk(sendStr+".\r\n#r抱歉你輸囉^^哭哭喔!");
            else if (fDTot == fPTot) {
                cm.sendOk(sendStr+".\r\n#r平手!拿回你的賭金吧.");
                cm.gainMeso(fee);
            } else {
                cm.sendOk(sendStr+".\r\n#r你贏了!恭喜你!");
                cm.gainMeso(1.78 * fee);
            }
            cm.dispose();
        }
    }
} 
	