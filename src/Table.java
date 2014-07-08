/**
 * Andrey Smirnov (mail@ansmirnov.ru)
 */


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public class Table {

    public Base1CD base1cd;
    private int start_block;
    private ByteBuffer head;

    public Table(Base1CD base1cd, int start_block)
            throws IOException {
        this.base1cd = base1cd;
        this.start_block = start_block;
        this.head = base1cd.readBlock(start_block);
    }

    public Object[] getObjects()
            throws IOException {
        Object objects[] = new Object[1018];
        for (int i = 0; i < 1018; i++) {
            int addr = this.head.getInt(24 + i * 4);
            if (addr == 0) {
                objects[i] = null;
            } else {
                objects[i] = new Object(this, addr);
            }
        }
        return objects;
    }

    public Object getObject(int i)
            throws IOException {
        long addr = this.head.getInt(24 + i * 4);
        if (addr == 0) {
            return null;
        }
        return new Object(this, addr);
    }

    public String getHandle()
            throws IOException {
        return this.getObject(0).asString();
    }

    public Field[] getFields()
            throws IOException {
        return null;
    }

    public TableInfo getTableInfo()
            throws IOException {
        return new TableInfo(this.getHandle());
    }

}
