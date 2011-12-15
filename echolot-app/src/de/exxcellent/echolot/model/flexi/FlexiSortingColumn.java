/*
 * This file (FlexiSortingColumn.java) is part of the Echolot Project (hereinafter "Echolot").
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

import de.exxcellent.echolot.model.SortDirection;
import java.io.Serializable;

/**
 * A {@link FlexiSortingColumn} class describes a column and its sortOrder order.
 * 
 * @author Oliver Pehnke <o.pehnke@exxcellent.de>
 */
public class FlexiSortingColumn implements Serializable {
    private final int columnId;
    private final String sortOrder;

    /**
     * Default constructor to create a {@link FlexiSortingColumn}. Used by the JSON-Converter.
     * 
     * @param columnId the identifier of the column
     * @param sortOrder the sorting order is either ascending or descending
     */
    protected FlexiSortingColumn(int columnId, String sortOrder) {
        this.columnId = columnId;
        this.sortOrder = sortOrder;
    }

    /**
     * Constructor to create a {@link FlexiSortingColumn}.
     * 
     * @param columnId the ID of the the column to sort with
     * @param sortOrder the sortOrder mode
     * @see Column#getId() 
     */
    public FlexiSortingColumn(int columnId, SortDirection sortOrder) {
        this.columnId = columnId;
        this.sortOrder = sortOrder.getSortingDirectionValue();
    }

    public int getColumnId() {
        return columnId;
    }
    
    public SortDirection getSortingDirection() {
        return SortDirection.toSortingDirectionValue(sortOrder);
    }
    
    @Override
    public String toString() {
        return "Column " + columnId + ", " + getSortingDirection().name();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FlexiSortingColumn other = (FlexiSortingColumn) obj;
        if (this.columnId != other.columnId) {
            return false;
        }
        if ((this.sortOrder == null) ? (other.sortOrder != null) : !this.sortOrder.equals(other.sortOrder)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.columnId;
        hash = 79 * hash + (this.sortOrder != null ? this.sortOrder.hashCode() : 0);
        return hash;
    }
}


