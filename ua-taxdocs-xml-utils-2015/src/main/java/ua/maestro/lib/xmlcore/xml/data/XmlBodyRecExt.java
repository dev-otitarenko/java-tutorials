package ua.maestro.lib.xmlcore.xml.data;

import java.util.Objects;

/**
 * Created by maestro on 19.05.17.
 */
public class XmlBodyRecExt {
    private String name;
    private String value;
    private long rownum;

    public XmlBodyRecExt(String nm, long rn, String val) {
        this.name = nm;
        this.value = val;
        this.rownum = rn;
    }

    public String getName() {
        return name;
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
        XmlBodyRecExt t  = (XmlBodyRecExt) c;
        return rownum == t.rownum && name.equals(t.getName()) && value.equals(t.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rownum, value);
    }
}
