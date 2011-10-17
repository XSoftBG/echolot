/*
 * This file (FlexTableModel.java) is part of the Echolot Project (hereinafter "Echolot").
 * Copyright (C) 2008-2011 eXXcellent Solutions GmbH.
 *
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 */

package de.exxcellent.echolot.model;

import de.exxcellent.echolot.event.FlexTableModelEvent;
import de.exxcellent.echolot.listener.FlexiTableModelListener;
import java.io.Serializable;
import java.util.EventListener;
import nextapp.echo.app.event.EventListenerList;

/**
 * TableModel for FlexiGrid
 *
 * @author Ralf Enderle <r.enderle@exxcellent.de>
 */
public abstract class FlexTableModel implements Serializable {

    /**
     * Static identifier for all Rows on one Page
     */
    public static final int SHOW_ALL_ROWS_ON_ONE_PAGE = -1;
    
    private EventListenerList listenerList = new EventListenerList();
    
    /**
     * @see nextapp.echo.app.table.TableModel#addTableModelListener(nextapp.echo.app.event.TableModelListener)
     */
    public void addFlexTableModelListener(FlexiTableModelListener l) {
        listenerList.addListener(FlexiTableModelListener.class, l);
    }
    
    /**
     * @see nextapp.echo.app.table.TableModel#removeTableModelListener(nextapp.echo.app.event.TableModelListener)
     */
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
        fireTableChanged(new FlexTableModelEvent(this, FlexTableModelEvent.INSERT));
    }
        
    /** Notifies <code>FlexiTableModelListener</code>s that rows were deleted */
    public void fireTableRowsDeleted(int... rowsIds) {
          if(rowsIds.length != 0) {
              fireTableChanged(new FlexTableModelEvent(this, FlexTableModelEvent.DELETE, rowsIds));
          }
    }
    
    /** Notifies <code>FlexiTableModelListener</code>s that rows were updated */
    public void fireTableRowsUpdated(int... rowsIds) {
          if(rowsIds.length != 0) {
              fireTableChanged(new FlexTableModelEvent(this, FlexTableModelEvent.UPDATE, rowsIds));
          }
    }
    
    /** Notifies <code>FlexiTableModelListener</code>s that column model were changed */
    public void fireTableColumnsChanged() {
          fireTableChanged(new FlexTableModelEvent(this, FlexTableModelEvent.STRUCTURE_CHANGED));
    }
    
    /**
     * Notifies <code>FlexiTableModelListener</code>s of the specified event.
     *
     * @param e the event
     */
    public void fireTableChanged(FlexTableModelEvent e) {
        EventListener[] listeners = listenerList.getListeners(FlexiTableModelListener.class);
        
        for (int index = 0; index < listeners.length; ++index) {
            ((FlexiTableModelListener) listeners[index]).flexTableChanged(e);
        }
    }

    /**
     * Returns the RowsPerPageCount
     *
     * @return the amount of Rows per Page
     * @see FlexTableModel.SHOW_ALL_ROWS_ON_ONE_PAGE
     */
    public abstract int getRowsPerPageCount();

    /**
     * Returns the Rowcount of the TableModel
     *
     * @return the total amount of rows
     */
    public abstract int getRowCount();

    /**
     * Returns the ColumnCount of the TableModel
     *
     * @return the total amount of columns
     */
    public abstract int getColumnCount();

    /**
     * Returns the ColumnModel for a specific columnIndex
     *
     * @param columnIndex the requested index
     * @return the suitable columnModel
     */
    public abstract FlexColumnModel getColumnModel(int columnIndex);

    /**
     * Returns the value or component at a specific row/col position of the TableModel
     *
     * @param rowIndex    the rowIndex
     * @param columnIndex the columnIndex
     * @return the value of the requested row/col index
     */
    public abstract Object getValueAt(int rowIndex, int columnIndex);
    
    /**
     * Return the id at a specific row position of the TableModel
     * 
     * @param rowIndex the row index
     * @return the id of the requested row
     */
    public abstract int getRowId(int rowIndex);
}
