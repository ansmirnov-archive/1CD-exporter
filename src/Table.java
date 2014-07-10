/**
 * Andrey Smirnov (mail@ansmirnov.ru)
 */


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Table {

    public Base1CD base1cd;
    private int start_block;
    private ByteBuffer head;
    private List<TableItem> TableItems;
    private int ValuesPosition;
    private ByteBuffer Data;
    private List<Field> Fields;

    public Table(Base1CD base1cd, int start_block)
            throws IOException {
        this.base1cd = base1cd;
        this.start_block = start_block;
        this.head = base1cd.readBlock(start_block);
        this.TableItems = this.getTableInfo().getItems();
        this.Data = null;
        this.Fields = null;
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
        if (this.Data != null)
            return this.Data;
        List<Object> objects = this.getDataObjects();
        if (objects.size() > 1) return null;
        this.Data = objects.get(0).asByteBuffer();
        return this.Data;
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
        if (this.Fields != null)
            return this.Fields;
        List<TableItem> fields = this.TableItems.get(2).getChild().getItems();
        List<Field> res = new ArrayList<Field>();
        for (int i = 1; i < fields.size(); i++) {
            Field field = new Field(this, fields.get(i).getChild().getItems());
            if (field.FieldType.equals("RV"))
                res.add(0, field);
            else
                res.add(field);
        }
        if (!res.get(0).FieldType.equals("RV"))
            res.add(0, new Field(this, "FAKE_VERSION", "FAKE_VERSION", "", "16", "", ""));
        res.add(0, new Field(this, "MARK", "MARK", "", "1", "", ""));
        this.Fields = res;
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

    public int recordSize()
            throws IOException{
        this.getFields();
        int res = 0;
        for (int i = 0; i < this.Fields.size(); i++) {
            res += this.Fields.get(i).size();
        }
        return res;
    }

    public List<Field> readValues(int number)
            throws IOException {
        this.getData();
        this.getFields();
        int record_size = this.recordSize();
        int start_record = number * record_size + 1;
        int offset = 0;
        for (int i = 0; i < this.Fields.size(); i++) {
            Field field = this.Fields.get(i);
            field.readValue(this.Data, start_record + offset);
            offset += field.size();
        }
        return this.Fields;
    }

}
