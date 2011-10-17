package de.exxcellent.echolot.listener;

import de.exxcellent.echolot.event.FlexTableModelEvent;
import java.io.Serializable;
import java.util.EventListener;

/**
 *
 * @author sieskei (XSoft Ltd.)
 */
public interface FlexiTableModelListener extends EventListener, Serializable {
    public void flexTableChanged(FlexTableModelEvent ftme);
}
