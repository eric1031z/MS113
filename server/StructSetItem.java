package server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StructSetItem {

    public int completeCount, setItemID;
    public Map<Integer, SetItem> items = new LinkedHashMap<>();
    public List<String> itemIDs = new ArrayList<>();

    public static class SetItem {

        public int incPDD, incMDD, incSTR, incDEX, incINT, incLUK, incACC, incPAD, incMAD, incSpeed, incMHP, incMMP;
    }

    public Map<Integer, SetItem> getItems() {
        return new LinkedHashMap<>(items);
    }
}
