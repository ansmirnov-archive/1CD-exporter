/**
 * Andrey Smirnov (mail@ansmirnov.ru)
 */


import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        Base1CD base1cd = new Base1CD("/home/ateladmin/1Cv8.1CD");
        Table tables[] = base1cd.readTableList();
        System.out.print(tables[4].getHandle());
        ByteBuffer test = tables[4].getData();
        tables[4].readValues(1);
        System.out.print(test);
        test.position(0);
        System.out.print("\n");
        byte[] test2 = test.array();
        for (int i = 0; i < test2.length; i++) {
            System.out.printf("%d %s %c \n", i, String.format("%02X", test2[i]), (char)test2[i]);
        }
        System.out.print("test");
    }
}
