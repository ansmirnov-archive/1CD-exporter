/**
 * Andrey Smirnov (mail@ansmirnov.ru)
 */


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        Base1CD base1cd = new Base1CD("/home/ateladmin/1Cv8.1CD");
        Table tables[] = base1cd.readTableList();
        System.out.print(tables[22].getHandle());
        ByteBuffer test = tables[22].getData();
        System.out.print(test);
    }
}
