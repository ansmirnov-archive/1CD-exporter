/**
 * Andrey Smirnov (mail@ansmirnov.ru), 2014
 */

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class Field {

    public Table parent_table;
    public String FieldName;
    public String FieldType;
    public String NullExists;
    public String FieldLength;
    public String FieldPrecision;
    public String FieldCaseSensitive;
    public ByteBuffer Value;

    public Field(Table parent_table, String FieldName, String FieldType, String NullExists, String FieldLength, String FieldPrecision, String FieldCaseSensitive)
            throws IOException {
        this.parent_table = parent_table;
        this.FieldName = FieldName;
        this.FieldType = FieldType;
        this.NullExists = NullExists;
        this.FieldLength = FieldLength;
        this.FieldPrecision = FieldPrecision;
        this.FieldCaseSensitive = FieldCaseSensitive;
    }

    public Field(Table parent_table, List<TableItem> tableItems)
            throws IOException {
        this.parent_table = parent_table;
        this.FieldName = tableItems.get(0).getValue();
        this.FieldType = tableItems.get(1).getValue();
        this.NullExists = tableItems.get(2).getValue();
        this.FieldLength = tableItems.get(3).getValue();
        this.FieldPrecision = tableItems.get(4).getValue();
        this.FieldCaseSensitive = tableItems.get(5).getValue();
    }

    public int size() {
        if (this.FieldType.equals("B"))
            return Integer.parseInt(this.FieldLength);
        if (this.FieldType.equals("L"))
            return 1;
        if (this.FieldType.equals("N"))
            return (int)((Integer.parseInt(this.FieldLength) + 2.0) / 2);
        if (this.FieldType.equals("NC"))
            return Integer.parseInt(this.FieldLength) * 2;
        if (this.FieldType.equals("NVC"))
            return Integer.parseInt(this.FieldLength) * 2 + 2;
        if (this.FieldType.equals("RV"))
            return 16;
        if (this.FieldType.equals("NT"))
            return 8;
        if (this.FieldType.equals("I"))
            return 8;
        if (this.FieldType.equals("DT"))
            return 7;
        if (this.FieldType.equals("FAKE_VERSION"))
            return 16;
        if (this.FieldType.equals("MARK"))
            return 16;
        return Integer.parseInt(this.FieldLength);
    }

    public void readValue(ByteBuffer data, int offset) {
        int size = this.size();
        byte[] res = new byte[size];
        data.get(res, offset, size);
        this.Value = ByteBuffer.allocate(size).put(res);
    }
}
