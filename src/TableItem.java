/**
 * Andrey Smirnov (mail@ansmirnov.ru), 2014
 */
public class TableItem {
    private int type;
    private String value;
    private TableInfo child;

    public TableItem(String value) {
        this.type = 1;
        this.value = value;
    }

    public TableItem(TableInfo child) {
        this.type = 2;
        this.child = child;
    }
}
