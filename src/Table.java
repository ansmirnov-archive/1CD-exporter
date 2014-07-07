import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public class Table {

    public Base1CD base1cd;
    private int start_block;
    private ByteBuffer head;
    private Object objects[];

    public Table(Base1CD base1cd, int start_block)
            throws IOException {
        this.base1cd = base1cd;
        this.start_block = start_block;
        this.head = base1cd.readBlock(start_block);
        this.objects = new Object[1018];
        for (int i = 0; i < 1018; i++) {
            int addr = this.head.getInt(24 + i * 4);
            if (addr == 0) {
                this.objects[i] = null;
            }
            else {
                this.objects[i] = new Object(this, addr);
            }
        }
    }

    public Object[] getObjects() {
        return this.objects;
    }
}
