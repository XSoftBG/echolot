package de.exxcellent.echolot.event;

import de.exxcellent.echolot.model.FlexTableModel;
import java.util.Arrays;
import java.util.EventObject;

/**
 *
 * @author sieskei (XSoft Ltd.)
 */
public class FlexTableModelEvent extends EventObject
{
    /** Serial Version UID. */
    private static final long serialVersionUID = 20110101L;

    /** An event type indicating one or more table rows were inserted. */
    public static final int INSERT = 1;
    
    /** An event type indicating one or more table rows were deleted. */
    public static final int DELETE = 2;   
        
    /** An event type indicating one or more table rows were updated. */
    public static final int UPDATE = 3;
    
    /** An event type indicating the table structure was modified. */
    public static final int STRUCTURE_CHANGED = 4;
        
    private final int type;
    private final int[] rowsIds;
        
    /**
     * Primary constructor for creating <code>TableModelEvent</code>s.
     * All other constructors are for convenience and must invoke this
     * constructor.
     *
     * @param source the changed <code>TableModel</code>
     * @param firstRow the first table row affected by the update
     * @param lastRow the last table row affected by the update
     * @param type The type of change that occurred, one of the following 
     *        values:
     *        <ul>
     *        <li>DELETE - indicates one or more rows were deleted.</li>
     *        <li>INSERT - indicates one or more rows were inserted.</li>
     *        <li>UPDATE - indicates one or more rows were updated.</li>
     *        </ul>
     */
    public FlexTableModelEvent(FlexTableModel source, int type, int... rowsIds) {
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
     *         <li>UPDATE - indicates one or more rows were updated.</li>
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
