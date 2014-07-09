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

    public List<Field> getFields()
            throws IOException {
        List<TableItem> fields = this.TableItems.get(2).getChild().getItems();
        List<Field> res = new ArrayList<Field>();
        for (int i = 1; i < fields.size(); i++) {
            res.add(new Field(this, fields.get(i).getChild().getItems()));
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
