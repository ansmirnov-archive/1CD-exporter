/**
 * Andrey Smirnov (mail@ansmirnov.ru), 2014
 */

import java.util.*;

public class TableInfo {
    private Character[] arr_delimiters = {'{', '}', ','}, arr_ignored = {'\n', ' ', '"'};
    private HashSet<Character> delimiters = new HashSet<Character>(Arrays.asList(arr_delimiters));
    private HashSet<Character> ignored = new HashSet<Character>(Arrays.asList(arr_ignored));
    private int Iter;
    private String StrInfo;
    private List<TableItem> Items;

    public TableInfo(String StrInfo) {
        this.StrInfo = StrInfo;
        genItems();
    }

    public TableInfo(List<String> StrInfoList) {
        this.StrInfo = "";
        for (int i = 0; i < StrInfoList.size(); i++) {
            this.StrInfo += StrInfoList.get(i);
        }
        genItems();
    }

    public char getChar() {
        if (!canRead()) return 0;
        return this.StrInfo.charAt(this.Iter);
    }

    public void nextChar() {
        this.Iter++;
    }

    public void clearIter() {
        this.Iter = 0;
    }

    public boolean canRead() {
        return (this.Iter < this.StrInfo.length());
    }

    public String getLiter() {
        String s = new String();
        char c;
        do {
            c = this.getChar();
            if (ignored.contains(c)) {
                this.nextChar();
                continue;
            }
            if (delimiters.contains(c)) {
                if (s.length() == 0) {
                    s += c;
                    this.nextChar();
                }
                return s;
            }
            if (c == 0) {
                return null;
            }
            s += c;
            this.nextChar();
        } while (true);
    }

    private void genItems() {
        this.Items = new ArrayList<TableItem>();
        this.clearIter();
        //this.getLiter();
        int r = 0;
        String s = getLiter(), sub = new String();
        while (true) {
            s = getLiter();
            if (s == null) return;
            if (r == 0) {
                if (!delimiters.contains(s.charAt(0)))
                    this.Items.add(new TableItem(s));
            }
            else {
                sub += s;
            }
            if (s.equals("{")) {
                if (r == 0) {
                    sub += s;
                }
                r++;
            }
            if (s.equals("}")) {
                r--;
                if (r == 0) {
                    this.Items.add(new TableItem(new TableInfo(sub)));
                    sub = new String();
                }
            }
        }
    }

    public List<String> cleanValue(List<String> value) {
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < value.size(); i++) {
            if (!value.get(i).equals(","))
                res.add(value.get(i));
        }
        return res;
    }

    public List<TableItem> getItems() {
        return this.Items;
    }
}
