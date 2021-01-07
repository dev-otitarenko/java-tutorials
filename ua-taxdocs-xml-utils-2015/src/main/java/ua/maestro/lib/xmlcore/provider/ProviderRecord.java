package ua.maestro.lib.xmlcore.provider;

/**
 *
 * @author maestro, 2015
 */
public class ProviderRecord {
    private int _type;
    private String _libName;

    public ProviderRecord(int type, String nm) {
        this._type = type;
        this._libName = nm;
    }

    public int getType() {
        return _type;
    }

    public String getLibraryName() {
        return _libName;
    }
}
