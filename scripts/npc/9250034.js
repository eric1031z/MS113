var status;
var CorrectCombo;
var CurrentCombo;
var PreviousGuesses = new Array();
var NumSpaces, NumColors;
var SpaceDiffSelected, PieceDiffSelected;
var Pieces = [4030002, 4030003, 4030004, 4030005, 4030006, 4030007, 4030008];
var SpaceDifficulties = [["小試身手", 3], ["初階", 4], ["中階", 5], ["高階", 6], ["專家", 7]];
var PieceDifficulties = [["小試身手", 3], ["初階", 4], ["中階", 5], ["高階", 6], ["專家", 7]];
var MaxGuesses = 25;
var RewardReq = 5;
//var CorrectPiece = 4031140, CorrectPieceAndSpace = 4005004;
var CorrectPiece = 4001007, CorrectPieceAndSpace = 4001008, Blank = 4031325, DeletePrevious = 3991002;

function Header() {
    return "#d#k 咕咕雞谷 #e#b珠璣妙算#k#n #d#k\r\n\r\n";
}
function SendMainMenu() {
    cm.sendSimple(
        Header() +
        "#L0#開始遊戲#l\r\n" +
        "#L1#遊戲規則#l"
    );
}
function SendGameInstructions() {
    cm.sendNext(Header() +
        "#b#e歡迎來到咕咕雞遊戲世界--珠璣妙算.#k#n\r\n" +
        "#e遊戲規則:#n\r\n" +
        "1) 本遊戲的目的就是正確猜對特定物品種類的排列順序.\r\n" +
        "2) 每次猜完以後,都會給你提示,告訴你跟正確答案的差異點\r\n" +
        "        #v"+CorrectPieceAndSpace+"# - 表示有 #b正確#k 的物品在 #b正確#k 的位置\r\n" +
        "        #v"+CorrectPiece+"# - 表示有 #b正確#k 的物品在 #r錯誤#k 的位置\r\n\r\n" +
        "    例如: " + GetFeedback([1,2]) + " - 代表有1個特定物品已經在正確的位置,但有兩個特定物品在錯誤的位置.\r\n" +
        "3) 你總共有 #r" + MaxGuesses + "#k 機會來猜到正確的排列順序.\r\n"+
		"4) 如果在某次選擇中選錯了物品了想要回上一步,請按遊戲內圖示C\r\n"+
		"5) 若挑戰序列長度7(專家級),物品種類7(專家級),在6回合內結束,即可獲得#i2049100#一張\r\n"
    );
}
function SendNumSpacesSelectionMenu() {
    var SendStr = Header();
    SendStr += "#e請選擇你想要挑戰的排列長度:#n\r\n"
    for (var i = 0;i < SpaceDifficulties.length;i++) {
        SendStr += "#L" + i + "#" + SpaceDifficulties[i][0] + " - 排列長度: " + SpaceDifficulties[i][1] + "\r\n";
    }
    cm.sendSimple(SendStr);
}
function SendNumPiecesSelectionMenu() {
    var SendStr = Header();
    SendStr += "#e請選擇你想要挑戰的物品種類數量:#n\r\n"
    for (var i = 0;i < PieceDifficulties.length;i++) {
        SendStr += "#L" + i + "#" + PieceDifficulties[i][0] + " - 物品種類數量: " + PieceDifficulties[i][1] + "\r\n";
    }
    cm.sendSimple(SendStr);
}
function GetGameSettingsDescription() {
    return "\r\n#d#e遊戲設置:#n#k\r\n" +
        SpaceDifficulties[SpaceDiffSelected][0] + " - 排列長度: " + SpaceDifficulties[SpaceDiffSelected][1] + "\r\n" +
        PieceDifficulties[PieceDiffSelected][0] + " - 物品種類數量: " +  PieceDifficulties[PieceDiffSelected][1] + "\r\n";
}
function InitializeCorrectSequence() {
    Shuffle(Pieces);
    CorrectCombo = new Array(NumSpaces);
    for (var i = 0;i < CorrectCombo.length;i++) {
        CorrectCombo[i] = Math.floor(Math.random() * NumColors);
    }
}
function GetSequenceString(Sequence) {
    var Ret = "";
    for (var i = 0;i < Sequence.length;i++) {
        Ret += "#v" + Pieces[Sequence[i]] + "#";
    }
    return Ret;
}
function GetCurrentCombo() {
    var Ret = "當前的猜測: ";
    for (var i = 0;i < CurrentCombo.length;i++) {
        Ret += "#v" + Pieces[CurrentCombo[i]] + "#";
    }
    for (var j = 0;j < NumSpaces - CurrentCombo.length;j++) {
        Ret += "#v" + Blank + "#";
    }
    return Ret + "\r\n";
}
function GetPieceSelectionString() {
    var Ret = "請選擇一個物品:\r\n";
    for (var i = 0;i < NumColors;i++) {
        Ret += "#L" + i + "##v" + Pieces[i] + "##l ";
    }
    if (CurrentCombo.length > 0) {
        Ret += "#L" + NumColors + "##v" + DeletePrevious + "##l";
    }
    return Ret + "\r\n";

}
function CalculateMatch() {
    var Correct = new Array();
    for (var i = 0;i < NumSpaces;i++) {
        if (CorrectCombo[i] == CurrentCombo[i]) {
            Correct.push(i);
        }
    }
    var Ret1 = Correct.length;
    for (var j = 0;j < NumSpaces;j++) {
        if (!Contains(j, Correct)) {
            for (var k = 0;k < NumSpaces;k++) {
                if (!Contains(k, Correct)) {
                    if (CurrentCombo[j] == CorrectCombo[k]) {
                        Correct.push(k);
                        break;
                    }
                }
            }
        }
    }
    return [Ret1, Correct.length - Ret1];
}
function Contains(i, arr) {
    for (var j = 0;j < arr.length;j++) {
        if (arr[j] == i) {
            return true;
        }
    }
    return false;
}
function GetFeedback(CorrectNumbers) {
    var Ret = "";
    for (var i = 0;i < CorrectNumbers[0];i++) {
        Ret += "#v" + CorrectPieceAndSpace + "#";
    }
    for (var j = 0;j < CorrectNumbers[1];j++) {
        Ret += "#v" + CorrectPiece + "#";
    }
    return Ret;
}
function GetPreviousCombos() {
    if (PreviousGuesses.length == 0) {
        return "";
    }
    var Ret = "\r\n=== #e先前的猜測#n ===\r\n";
    for (var i = PreviousGuesses.length - 1;i >= 0;i--) {
        var PreviousGuess = PreviousGuesses[i];
        Ret += ((i + 1) + ". ") + GetSequenceString(PreviousGuess[0]) + "    " + GetFeedback(PreviousGuess[1]) + "\r\n";
    }
    return Ret + "\r\n";
}
function Shuffle(arr) {
    var i = arr.length;
    if (i == 0)
        return;
    while (--i) {
        var j = Math.floor( Math.random() * ( i + 1 ) );
        var tempi = arr[i];
        var tempj = arr[j];
        arr[i] = tempj;
        arr[j] = tempi;
    }
}
function CheckSelection(s, low, high) {
    return s < low && s > high;
}
function start() {
    status = -1;
    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1 || (mode == 0 && (status != 5 || CurrentCombo == null || CurrentCombo.length != NumSpaces))) {
        cm.dispose();
        return;
    }
    if (status != 5)
        status++;

    if (status == 0) {
        SendMainMenu();
    } else if (status == 1) {
        if (selection == 0) {
            action(1, 0, 0);
        } else if (selection == 1) {
            SendGameInstructions();
        } else {
            cm.dispose();
        }
    } else if (status == 2) {
        SendNumSpacesSelectionMenu();
    } else if (status == 3) {
        if (CheckSelection(selection, 0, SpaceDifficulties.length - 1)) {
            cm.dispose();
            return;
        }
        SpaceDiffSelected = selection;
        NumSpaces = SpaceDifficulties[selection][1];
        SendNumPiecesSelectionMenu();
    } else if (status == 4) {
        if (CheckSelection(selection, 0, PieceDifficulties.length - 1)) {
            cm.dispose();
            return;
        }
        PieceDiffSelected = selection;
        NumColors = PieceDifficulties[selection][1];
        InitializeCorrectSequence();
        CurrentCombo = new Array();
        cm.sendSimple(Header() + GetCurrentCombo() + GetPieceSelectionString()
            + GetPreviousCombos() + GetGameSettingsDescription());
    } else if (status == 5) {
        if (CurrentCombo.length == NumSpaces) {
            if (mode == 0) { //No
                CurrentCombo.pop();
                cm.sendSimple(Header() + GetCurrentCombo() + GetPieceSelectionString()
                    + GetPreviousCombos() + GetGameSettingsDescription());
            } else if (mode == 1) { //Yes
                var Match = CalculateMatch();
                if (Match[0] == NumSpaces) {
                    //Win
                    cm.sendOk(Header() + "#b#e恭喜你在 "+(PreviousGuesses.length + 1)+" 回合就猜對了!跟仙女一樣聰明!#n#k\r\n"
                        + GetSequenceString(CurrentCombo) + "    " + GetFeedback(Match) + "\r\n"
                        + GetPreviousCombos() + GetGameSettingsDescription());
				 if (SpaceDifficulties[SpaceDiffSelected][1]==7&&PieceDifficulties[PieceDiffSelected][1]==7&&PreviousGuesses.length+1<=RewardReq){
					cm.gainItem(2049100,1);
					cm.dispose()
				}
				  else
                    cm.dispose();
                } else if (PreviousGuesses.length + 1 == MaxGuesses) {
                    //Lose
                    PreviousGuesses.push([CurrentCombo, Match]);
                    cm.sendOk(Header() + "#b#e抱歉你花了超過 ("+MaxGuesses+")來猜了QQ!#n#k\r\n"
                        + "正確的物品和其排列方式為: " +  GetSequenceString(CorrectCombo) + "\r\n"
                        + GetPreviousCombos() + GetGameSettingsDescription());
                    cm.dispose();
                } else {
                    //Continue
                    PreviousGuesses.push([CurrentCombo, Match]);
                    CurrentCombo = new Array();
                    cm.sendSimple(Header() +
                        "#r抱歉,這個物品排列方式是錯的喔.#k\r\n"
                        + GetCurrentCombo() + GetPieceSelectionString()
                        + GetPreviousCombos() + GetGameSettingsDescription());
                }
            }
        } else {
            if (CheckSelection(selection, 0, (CurrentCombo.length > 0 ? NumColors : NumColors - 1))) {
                cm.dispose();
                return;
            }
            if (selection == NumColors) {
                CurrentCombo.pop();
            } else {
                CurrentCombo.push(selection);
            }
            if (CurrentCombo.length == NumSpaces) {
                cm.sendYesNo(Header() + "#r請確認你的選項:#k\r\n"
                    + GetCurrentCombo() + GetPreviousCombos() + GetGameSettingsDescription()
                );
            } else {
                cm.sendSimple(Header() + GetCurrentCombo() + GetPieceSelectionString()
                    + GetPreviousCombos() + GetGameSettingsDescription());
            }
        }
    } else {
        cm.dispose();
    }
} 