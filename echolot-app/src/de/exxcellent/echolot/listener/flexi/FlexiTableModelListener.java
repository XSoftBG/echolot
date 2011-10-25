package de.exxcellent.echolot.listener.flexi;

import de.exxcellent.echolot.event.flexi.FlexiTableModelEvent;
import java.io.Serializable;
import java.util.EventListener;

/**
 *
 * @author sieskei (XSoft Ltd.)
 */
public interface FlexiTableModelListener extends EventListener, Serializable {
    public void flexTableChanged(FlexiTableModelEvent ftme);
}
