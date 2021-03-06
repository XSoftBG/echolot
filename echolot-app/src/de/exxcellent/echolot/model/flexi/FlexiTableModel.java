/*
 * This file (FlexiTableModel.java) is part of the Echolot Project (hereinafter "Echolot").
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

import de.exxcellent.echolot.listener.flexi.FlexiTableModelListener;
import java.io.Serializable;

/**
 *
 * @author sieskei (XSoft Ltd.)
 */
public interface FlexiTableModel extends Serializable {    
    public static final int SHOW_ALL_ROWS_ON_ONE_PAGE = -1;
    
    /**
     * <b>
     * <i>Method is invoked only when PROPERTY_RESULTS_PER_PAGE_OPTION == null !!!</i>
     * <br />
     * <br />
     * The table is initialized with PROPERTY_RESULTS_PER_PAGE_OPTION == null so that the method will be called at least once!
     * </b>
     * @return maximum number of rows per page
     */
    public int getDefaultResultsPerPage();
    
    /**
     * <b>
     * <i>Method is invoked only when PROPERTY_RESULTS_PER_PAGE_OPTION == null !!!</i>
     * <br />
     * <br />
     * The table is initialized with PROPERTY_RESULTS_PER_PAGE_OPTION == null so that the method will be called at least once!
     * </b>
     * @return choice for the maximum number of rows per page
     */
    public int[] getDefaultResultsPerPageOption();
    
    // * Rows methods
    // --------------
    public int getRowCount();
    public FlexiRow getRowAt(int rowIndex);
    public int getRowIdAt(int rowIndex);
    
    // * Columns methods
    // -----------------
    public int getColumnCount();
    public FlexiColumn getColumnAt(int columnIndex);
    public int getColumnIdAt(int columnIndex);
    
    // * Cells methods
    // ---------------
    public FlexiCell getCellAt(int rowIndex, int columnIndex);
    
    /**
     * Server-side sorting.
     * <br />
     * <b>Call only when pagable mode is turn ON;</b>
     * @param model - new sorting model;
     */
    public void onSort(FlexiSortingModel model);
    
    public void onActivePageChange(int pageNo);
    
    // * Listeners
    // -----------
    public void addFlexTableModelListener(FlexiTableModelListener l);
    public void removeFlexTableModelListener(FlexiTableModelListener l);
}
