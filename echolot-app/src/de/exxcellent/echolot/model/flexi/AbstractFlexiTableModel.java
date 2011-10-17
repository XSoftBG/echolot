/*
 * This file (AbstractFlexiTableModel.java) is part of the Echolot Project (hereinafter "Echolot").
 * Copyright (C) 2008-2010 eXXcellent Solutions GmbH.
 *
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 */

package de.exxcellent.echolot.model.flexi;

import de.exxcellent.echolot.event.FlexTableModelEvent;
import de.exxcellent.echolot.listener.FlexiTableModelListener;
import java.util.EventListener;
import nextapp.echo.app.event.EventListenerList;

/**
 *
 * @author sieskei (XSoft Ltd.)
 */
public abstract class AbstractFlexiTableModel implements FlexiTableModel {    
    private EventListenerList listenerList = new EventListenerList();
    
    /**
     * @see nextapp.echo.app.table.TableModel#addTableModelListener(nextapp.echo.app.event.TableModelListener)
     */
    @Override
    public void addFlexTableModelListener(FlexiTableModelListener l) {
        listenerList.addListener(FlexiTableModelListener.class, l);
    }
    
    /**
     * @see nextapp.echo.app.table.TableModel#removeTableModelListener(nextapp.echo.app.event.TableModelListener)
     */
    @Override
    public void removeFlexTableModelListener(FlexiTableModelListener l) {
        listenerList.removeListener(FlexiTableModelListener.class, l);
    }
        
    /**
     * Returns the <code>EventListenerList</code> used to register listeners.
     * 
     * @return the <code>EventListenerList</code>
     */
    public EventListenerList getEventListenerList() {
        return listenerList;
    }
    
    /** Notifies <code>FlexiTableModelListener</code>s that the rows were inserted */
    public void fireTableRowsInserted() {
     //   fireTableChanged(new FlexTableModelEvent(this, FlexTableModelEvent.INSERT));
    }
        
    /** Notifies <code>FlexiTableModelListener</code>s that rows were deleted */
    public void fireTableRowsDeleted(int... rowsIds) {
          if(rowsIds.length != 0) {
       //       fireTableChanged(new FlexTableModelEvent(this, FlexTableModelEvent.DELETE, rowsIds));
          }
    }
    
    /** Notifies <code>FlexiTableModelListener</code>s that rows were updated */
    public void fireTableRowsUpdated(int... rowsIds) {
          if(rowsIds.length != 0) {
         //     fireTableChanged(new FlexTableModelEvent(this, FlexTableModelEvent.UPDATE, rowsIds));
          }
    }
    
    /** Notifies <code>FlexiTableModelListener</code>s that column model were changed */
    public void fireTableColumnsChanged() {
//          fireTableChanged(new FlexTableModelEvent(this, FlexTableModelEvent.STRUCTURE_CHANGED));
    }
    
    /**
     * Notifies <code>FlexiTableModelListener</code>s of the specified event.
     *
     * @param e the event
     */
    private void fireTableChanged(FlexTableModelEvent e) {
        EventListener[] listeners = listenerList.getListeners(FlexiTableModelListener.class);        
        for (int index = 0; index < listeners.length; ++index) {
            ((FlexiTableModelListener) listeners[index]).flexTableChanged(e);
        }
    }
}
