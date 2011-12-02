/*
 * This file (FlexiRowSelection.java) is part of the Echolot Project (hereinafter "Echolot").
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

import java.io.Serializable;
import java.util.Arrays;

/**
 * JSON Message:
 * 
 * <pre>
 *{"rowSelection": {
 *  "allSelectedRowsIds": [0, 2, 3 ...] 
 *  "oldSelectedRowsIds": [0, 2, 3 ...] 
 *  "newSelectedRowsIds": [0, 2, 3 ...] 
 *  "newUnselectedRowsIds": [0, 2, 3 ...] 
 * }}
 * </pre>
 * 
 * @author oliver
 */
public class FlexiRowSelection implements Serializable {  
    private Integer[] allSelectedRowsIds;
    private Integer[] oldSelectedRowsIds;
    private Integer[] newSelectedRowsIds;
    private Integer[] newUnselectedRowsIds;

    
    /**
     * Constructor with initial rows identifiers.
     * 
     */    
    public FlexiRowSelection() {
      this.allSelectedRowsIds = new Integer[0];
      this.oldSelectedRowsIds = new Integer[0];
      this.newSelectedRowsIds = new Integer[0];
      this.newUnselectedRowsIds = new Integer[0];
    }
    
    /**
     * Constructor with initial rows identifiers.
     * 
     * @param allSelectedRowsIds contains all selected rows AFTER new selection
     * @param oldSelectedRowsIds contains all selected rows BEFORE new selection
     * @param newSelectedRowsIds contains all selected rows not contained in _oldSelectedRows AFTER new selection
     * @param newUnselectedRowsIds contains all non-selected rows contained in _oldSelectedRows AFTER new selection
     */
    public FlexiRowSelection(Integer[] allSelectedRowsIds, Integer[] oldSelectedRowsIds, Integer[] newSelectedRowsIds, Integer[] newUnselectedRowsIds)
    {
        this.allSelectedRowsIds = allSelectedRowsIds;
        this.oldSelectedRowsIds = oldSelectedRowsIds;
        this.newSelectedRowsIds = newSelectedRowsIds;
        this.newUnselectedRowsIds = newUnselectedRowsIds;
    }
    
    /**
     * @return array that contains all selected rows AFTER new selection
     */
    public Integer[] getAllSelectedRowsIds() {
        return allSelectedRowsIds;
    }
    
    public boolean hasSelected() {
      return allSelectedRowsIds.length != 0;
    }
        
    /**
     * @return array that contains all selected rows BEFORE new selection
     */
    public Integer[] getOldSelectedRowsIds() {
        return oldSelectedRowsIds;
    }    
        
    /**
     * @return array that contains all selected rows not contained in _oldSelectedRows AFTER new selection
     */
    public Integer[] getNewSelectedRowsIds() {
        return newSelectedRowsIds;
    }
        
    public boolean hasNewSelected() {
      return newSelectedRowsIds.length != 0;
    }
    
    /**
     * @return array that contains all non-selected rows contained in _oldSelectedRows AFTER new selection
     */
    public Integer[] getNewUnselectedRowsIds() {
        return newUnselectedRowsIds;
    }
    
    public boolean hasNewUnselected() {
      return newUnselectedRowsIds.length != 0;
    }
        
    /**
     * Prevents from null arrays.
     * 
     * Null arrays are created when transform JSON object to JAVA object !!!
     */
    public void repair() {      
      if (allSelectedRowsIds == null) {
          allSelectedRowsIds = new Integer[0];
      }
      
      if (oldSelectedRowsIds == null) {
          oldSelectedRowsIds = new Integer[0];
      }
      
      if (newSelectedRowsIds == null) {
          newSelectedRowsIds = new Integer[0];
      }
      
      if (newUnselectedRowsIds == null) {
          newUnselectedRowsIds = new Integer[0];      
      }
    }

  @Override
  public int hashCode()
  {
      int hash = 7;
      hash = 11 * hash + Arrays.deepHashCode(this.allSelectedRowsIds);
      hash = 11 * hash + Arrays.deepHashCode(this.oldSelectedRowsIds);
      hash = 11 * hash + Arrays.deepHashCode(this.newSelectedRowsIds);
      hash = 11 * hash + Arrays.deepHashCode(this.newUnselectedRowsIds);
      return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
    {
      return false;
    }
    if (getClass() != obj.getClass())
    {
      return false;
    }
    final FlexiRowSelection other = (FlexiRowSelection) obj;
    if (!Arrays.deepEquals(this.allSelectedRowsIds, other.allSelectedRowsIds))
    {
      return false;
    }
    if (!Arrays.deepEquals(this.oldSelectedRowsIds, other.oldSelectedRowsIds))
    {
      return false;
    }
    if (!Arrays.deepEquals(this.newSelectedRowsIds, other.newSelectedRowsIds))
    {
      return false;
    }
    if (!Arrays.deepEquals(this.newUnselectedRowsIds, other.newUnselectedRowsIds))
    {
      return false;
    }
    return true;
  }
}
