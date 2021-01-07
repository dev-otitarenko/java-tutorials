package ua.maestro.lib.xmlcore.xml.data;

import java.util.Objects;

/**
 * Created by maestro on 19.05.17.
 */
public class XmlBodyRec {
    private String value;
    private long rownum;

    public XmlBodyRec(long rn, String val) {
        this.value = val;
        this.rownum = rn;
    }

    public String getValue() {
        return value;
    }

    public long getRownum() {
        return rownum;
    }

    @Override
    public boolean equals(Object c) {
        if (this == c)
            return true;
        if (c == null || getClass() != c.getClass())
            return false;
        XmlBodyRec t  = (XmlBodyRec) c;
        return rownum == t.rownum && value.equals(t.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(rownum, value);
    }
}
