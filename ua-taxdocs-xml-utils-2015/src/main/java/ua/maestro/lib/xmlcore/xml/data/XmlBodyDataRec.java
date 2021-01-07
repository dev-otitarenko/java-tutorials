package ua.maestro.lib.xmlcore.xml.data;

import java.util.Objects;

/**
 * Created by maestro on 19.05.17.
 */
public class XmlBodyDataRec {
    private Object value;
    private long rownum;

    public XmlBodyDataRec(long rn, Object val) {
        this.value = val;
        this.rownum = rn;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object v) {
        this.value = v;
    }

    public long getRownum() {
        return rownum;
    }

    public void setRownum(long v) {
        this.rownum = v;
    }

    @Override
    public boolean equals(Object c) {
        if (this == c)
            return true;
        if (c == null || getClass() != c.getClass())
            return false;
        XmlBodyDataRec t  = (XmlBodyDataRec) c;
        return rownum == t.rownum && value.equals(t.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(rownum, value);
    }
}
