/*
 * This file (FlexiTableModelEvent.java) is part of the Echolot Project (hereinafter "Echolot").
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

package de.exxcellent.echolot.event.flexi;

import de.exxcellent.echolot.model.flexi.FlexiTableModel;
import java.util.Arrays;
import java.util.EventObject;

/**
 *
 * @author sieskei (XSoft Ltd.)
 */
public class FlexiTableModelEvent extends EventObject
{
    /** Serial Version UID. */
    private static final long serialVersionUID = 20110101L;

    /** An event type indicating one or more table rows were inserted. */
    public static final int INSERT = 1;
    
    /** An event type indicating one or more table rows were deleted. */
    public static final int DELETE = 2;   
    
    /** An event type indicating the table structure was modified. */
    public static final int STRUCTURE_CHANGED = 3;
        
    private final int type;
    private final int[] rowsIds;
    
    
    public static FlexiTableModelEvent makeRowsInsertedEvent(FlexiTableModel source) {
        return new FlexiTableModelEvent(source, FlexiTableModelEvent.INSERT);
    }
    
    public static FlexiTableModelEvent makeRowsDeletedEvent(FlexiTableModel source, int... rowsIds) {
        return new FlexiTableModelEvent(source, FlexiTableModelEvent.DELETE, rowsIds);
    }
    
    public static FlexiTableModelEvent makeStructureChangedEvent(FlexiTableModel source) {
        return new FlexiTableModelEvent(source, FlexiTableModelEvent.STRUCTURE_CHANGED);
    }
    
    
    /**
     * Primary constructor for creating <code>FlexiTableModelEvent</code>s.
     * All other constructors are for convenience and must invoke this
     * constructor.
     *
     * @param source the changed <code>TableModel</code>
     * @param rowsIds the changed <code>rows</code>
     * @param type The type of change that occurred, one of the following 
     *        values:
     *        <ul>
     *        <li>DELETE - indicates one or more rows were deleted.</li>
     *        <li>INSERT - indicates one or more rows were inserted.</li>
     *        <li>STRUCTURE_CHANGED - indicates that table structure was modified.</li>
     *        </ul>
     */
    private FlexiTableModelEvent(FlexiTableModel source, int type, int... rowsIds) {
      super(source);      
      this.type = type;
      this.rowsIds = rowsIds;
      Arrays.sort(rowsIds);
    }

    /**
     * Returns the type of update that occurred.
     *
     * @return the type of update that occurred, one of the following values:
     *         <ul>
     *         <li>DELETE - indicates one or more rows were deleted.</li>
     *         <li>INSERT - indicates one or more rows were inserted.</li>
     *         <li>STRUCTURE_CHANGED - indicates that table structure was modified.</li>
     *         </ul>
     */
    public int getType() {
        return type;
    }
    
    /**
     * Return the efected rows' ids
     * @return rows' ids
     */
    public int[] getRowsIds() {
      return rowsIds;
    }
}
