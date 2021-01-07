package ua.maestro.lib.xmlcore.provider;

import ua.maestro.lib.xmlcore.provider.exceptions.ProviderException;
import ua.maestro.lib.xmlcore.provider.interfaces.IProvider;
import ua.maestro.lib.xmlcore.domains.PZDocumentData;
import java.util.ArrayList;

/**
 *
 * @author maestro, 2015
 */
public abstract class AppProvider {
    protected ArrayList<ProviderRecord> plugins = new ArrayList<>();
    
    public AppProvider() {
        initPlugins();
    }

    protected abstract ArrayList<ProviderRecord> initPlugins();
    
    public IProvider getProvider(PZDocumentData doc) throws ProviderException {
        IProvider ret = null;
        
        try {
            for (ProviderRecord plugin : plugins) {
                Class c = Class.forName(plugin.getLibraryName()); 
                Object obj = c.newInstance();
                if (obj instanceof IProvider) {
                    if (((IProvider)obj).getType(doc))
                        return (IProvider)obj;
                }
            }
        } catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.err.println("Error of initialization.");
            throw new ProviderException("Помилка ініціалізації провайдера");
        }
        
        return ret;
    }
}