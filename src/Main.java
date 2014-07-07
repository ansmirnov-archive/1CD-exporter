/**
 * Andrey Smirnov (mail@ansmirnov.ru)
 */


import java.io.IOException;
import java.nio.ByteBuffer;

public class Main {

    public static void main(String[] args) throws IOException {
        Base1CD base1cd = new Base1CD("/home/ateladmin/1Cv8.1CD");
        Table tables[] = base1cd.readTableList();
        String str = tables[0].getObjects()[0].asString();
        for (int i = 0; i < tables.length; i++) {
            System.out.printf("%d:\n", i);
            Object[] objects = tables[i].getObjects();
            for (int j = 0; j < objects.length; j++) {
                if (objects[j] != null) {
                    objects[j].asString();
                }
            }
        }
    }
}
