/**
 * Andrey Smirnov (mail@ansmirnov.ru)
 */


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class Object {

    public Base1CD base1cd;
    private Table parent_table;
    private int start_block;
    private ByteBuffer head;
    private int n_blocks;
    private int[] blocks;

    public Object(Table parent_table, int start_block)
            throws IOException {
        this.base1cd = parent_table.base1cd;
        this.parent_table = parent_table;
        this.start_block = start_block;
        this.head = base1cd.readBlock(start_block);
        this.n_blocks = this.head.getInt(0);
        this.blocks = new int[this.n_blocks];
        for (int i = 0; i < this.n_blocks; i++) {
            this.blocks[i] = this.head.getInt(4 + i * 4);
        }
    }

    public int[] getObjects() {
        return this.blocks;
    }

    public String blockAsString(int block_number)
            throws IOException {
        return new String(base1cd.readBlock(this.blocks[block_number]).array(), Charset.forName("UTF-16LE"));
    }

    public String asString()
            throws IOException {
        String res = new String("");
        System.out.printf("!%d \n", this.n_blocks);
        for (int i = 0; i < this.n_blocks; i++) {
            res += this.blockAsString(i);
        }
        return res;
    }
}
