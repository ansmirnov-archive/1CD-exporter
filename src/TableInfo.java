/**
 * Andrey Smirnov (mail@ansmirnov.ru), 2014
 */

import java.util.*;

public class TableInfo {
    private int ITER;
    private String StrInfo;

    public TableInfo(String StrInfo) {
        this.StrInfo = StrInfo;
    }

    public TableInfo(List<String> StrInfoList) {
        this.StrInfo = "";
        for (int i = 0; i < StrInfoList.size(); i++) {
            this.StrInfo += StrInfoList.get(i);
        }
    }

    public char getChar() {
        return this.StrInfo.charAt(this.ITER);
    }

    public void nextChar() {
        this.ITER++;
    }

    public void clearIter() {
        this.ITER = 0;
    }

    public String getLiter() {
        Character[] arr_delimiters = {'{', '}', ','}, arr_ignored = {'\n', ' ', '"'};
        HashSet<Character> delimiters = new HashSet<Character>(Arrays.asList(arr_delimiters));
        HashSet<Character> ignored = new HashSet<Character>(Arrays.asList(arr_ignored));
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

    public List<String> getValue(String index) {
        this.clearIter();
        String s = getLiter();
        List<String> res = new ArrayList<String>();
        if (!s.equals("{"))
            return null;
        while (s != null) {
            if (!s.equals("{")) {
                s = this.getLiter();
                continue;
            }
            s = this.getLiter();
            if (!s.equals(index)) {
                s = this.getLiter();
                continue;
            }
            s = this.getLiter();
            int r = 0;
            while (true) {
                s = this.getLiter();
                if (s.equals("{")) {
                    r++;
                }
                if (s.equals("}")) {
                    r--;
                    if (r < 0) {
                        break;
                    }
                }
                res.add(s);
            }
            return res;
        }
        return null;
    }
}
