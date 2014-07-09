/**
 * Andrey Smirnov (mail@ansmirnov.ru)
 */


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Table {

    public Base1CD base1cd;
    private int start_block;
    private ByteBuffer head;
    private List<TableItem> TableItems;

    public Table(Base1CD base1cd, int start_block)
            throws IOException {
        this.base1cd = base1cd;
        this.start_block = start_block;
        this.head = base1cd.readBlock(start_block);
        this.TableItems = this.getTableInfo().getItems();
    }

    public List<Object> getObjects(ByteBuffer block)
            throws IOException {
        List<Object> objects = new ArrayList<Object>();
        for (int i = 0; i < 1018; i++) {
            long addr = block.getInt(24 + i * 4);
            if (addr != 0) {
                objects.add(new Object(this, addr));
            }
        }
        return objects;
    }

    public List<Object> getHeadObjects()
            throws IOException {
        return this.getObjects(this.head);
    }

    public List<Object> getDataObjects()
            throws IOException {
        ByteBuffer data = base1cd.readBlock(this.getFiles()[0]);
        return this.getObjects(data);
    }

    public ByteBuffer getData()
            throws IOException {
        List<Object> objects = this.getDataObjects();
        if (objects.size() > 1) return null;
        return objects.get(0).asByteBuffer();
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

    public List<Field> getFields()
            throws IOException {
        List<TableItem> fields = this.TableItems.get(2).getChild().getItems();
        List<Field> res = new ArrayList<Field>();
        for (int i = 1; i < fields.size(); i++) {
            res.add(new Field(this, fields.get(i).getChild().getItems()));
        }
        return res;
    }

    public int[] getFiles() {
        int[] res = new int[3];
        List<TableItem> items = this.TableItems.get(5).getChild().getItems();
        for (int i = 0; i < 3; i++) {
            res[i] = Integer.parseInt(items.get(i + 1).getValue());
        }
        return res;
    }

    public TableInfo getTableInfo()
            throws IOException {
        return new TableInfo(this.getHandle());
    }

    public List<TableItem> getTableItems()
            throws IOException{
        return this.TableItems;
    }

}
