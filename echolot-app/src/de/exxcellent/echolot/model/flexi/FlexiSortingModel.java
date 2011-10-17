/*
 * This file (FlexiSortingModel.java) is part of the Echolot Project (hereinafter "Echolot").
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
 * Sort on the first column and third column, order asc = 0. sortList: [[0,0],[2,0]].
 * 
 * <pre>
 * {"sortingModel": {
 *      "columns": [
 *          {sortingColumn: {
 *              "columnId": 0,
 *              "sortOrder": "desc"
 *          }}
 *    ]
 *   }}
 * </pre>
 * 
 * @author Oliver Pehnke <o.pehnke@exxcellent.de>
 */
public class FlexiSortingModel implements Serializable {
    private FlexiSortingColumn[] columns;

    /**
     * Creates a {@link FlexiSortingModel} with a set of columns defining the sorting order per column.
     * 
     * @param columns the sorting information about columns.
     */
    public FlexiSortingModel(FlexiSortingColumn[] columns) {
        super();
        this.columns = columns;
    }

    public FlexiSortingColumn[] getColumns() {
        return columns;
    }

    public void setColumns(FlexiSortingColumn[] columns) {
        this.columns = columns;
    }
    
    @Override
    public String toString() {
        return Arrays.toString(columns);
    }
}
