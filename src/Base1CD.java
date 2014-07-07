/**
 * Andrey Smirnov (mail@ansmirnov.ru)
 */


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public class Base1CD {

    private RandomAccessFile FILE;
    private String FileName;
    private FileChannel inChannel;
    private int BLOCK_SIZE = 4096;

    public Base1CD(String FileName)
            throws IOException {
        this.FileName = FileName;
        this.FILE = new RandomAccessFile(this.FileName, "r");
        this.inChannel = this.FILE.getChannel();
    }

    public ByteBuffer readBuffer(long offset, int size)
            throws IOException {
        ByteBuffer b = ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN);
        this.inChannel.read(b, offset);
        return b;
    }

    public ByteBuffer readBlock(int i)
            throws IOException{
        return this.readBuffer((long)i * BLOCK_SIZE, BLOCK_SIZE);
    }

    public Table[] readTableList()
            throws IOException {
        ByteBuffer block = readBlock(4);
        int size = block.getInt(32);
        Table[] list = new Table[size];
        for (int i = 0; i < size; i++) {
            list[i] = new Table(this, block.getInt(36 + i * 4));
        }
        return list;
    }
}
