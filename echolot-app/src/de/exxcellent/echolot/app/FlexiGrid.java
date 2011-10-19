/*
 * This file (FlexiGrid.java) is part of the Echolot Project (hereinafter "Echolot").
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

package de.exxcellent.echolot.app;

import de.exxcellent.echolot.layout.FlexiCellLayoutData;

import de.exxcellent.echolot.model.flexi.FlexiCell;
import de.exxcellent.echolot.model.flexi.FlexiSortingModel;
import de.exxcellent.echolot.model.flexi.FlexiRowSelection;
import de.exxcellent.echolot.model.flexi.ResultsPerPageOption;
import de.exxcellent.echolot.model.flexi.FlexiColumn;
import de.exxcellent.echolot.model.flexi.FlexiColumnModel;
import de.exxcellent.echolot.model.flexi.FlexiTableModel;
import de.exxcellent.echolot.model.flexi.FlexiPage;
import de.exxcellent.echolot.model.flexi.FlexiRow;
import de.exxcellent.echolot.model.flexi.FlexiColumnVisibility;

import de.exxcellent.echolot.event.FlexTableModelEvent;
import de.exxcellent.echolot.event.ResultsPerPageOptionEvent;
import de.exxcellent.echolot.event.TableColumnToggleEvent;
import de.exxcellent.echolot.event.TableRowSelectionEvent;
import de.exxcellent.echolot.event.TableSortingChangeEvent;

import de.exxcellent.echolot.listener.FlexiTableModelListener;
import de.exxcellent.echolot.listener.ResultsPerPageOptionChangeListener;
import de.exxcellent.echolot.listener.TableColumnToggleListener;
import de.exxcellent.echolot.listener.TableRowSelectionChangeListener;
import de.exxcellent.echolot.listener.TableSortingChangeListener;

import de.exxcellent.echolot.model.flexi.FlexiCellsUpdate;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import nextapp.echo.app.*;

import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * The {@link FlexiGrid} is a component using the <a href="http://www.flexigrid.info/">flexigrid jquery
 * plugin</a> to visualize a data grid. The flexigrid has several features such as:
 * <ul>
 * <li>Resizable columns</li>
 * <li>Resizable height and width</li>
 * <li>Sortable column headers (drag and drop)</li>
 * <li>Paging</li>
 * <li>Show/hide columns</li>
 * </ul>
 * <p/>
 * <pre>
 * +--------------------------+-+
 * | Title                    |+|
 * +-------+------------------+-+
 * | Head  | Head               |
 * +-------+--------------------+
 * | Cell  | Cell               |
 * +-------+--------------------+
 * | Cell  | Cell               |
 * +-------+--------------------+
 * | Footer                     |
 * +----------------------------+
 * </pre>
 * <p/>
 * Use the {@link FlexiGrid} like this:
 * <p/>
 * <pre>
 * final FlexiGrid FlexiGrid = new FlexiGrid();
 * final Column[] columns = new Column[]{
 *       		new Column(0, "First name", 50, true, "left"),
 *       		new Column(1, "Name", 50, false, "right"),
 *       		new Column(2, "Email", 10, true, "center")};
 * final Row[] rows = new Row[]{
 * 				new Row(0, new String[]{"Bob", "Doe","bob.doe@email.com"}),
 * 				new Row(1, new String[]{"Lisa", "Minelli", "lisa.minelli@email.com"}),
 * 				new Row(2, new String[]{"Ronald","McDonald","ronald.mcdonald@email.com"})
 * };
 * final Page page = new Page(pageIdx, 3, rows);
 * final TableModel model = new TableModel(columns, page);
 *
 * FlexiGrid.setTableModel(model);
 * </pre>
 *
 * @author Oliver Pehnke <o.pehnke@exxcellent.de>
 * @see <a href="http://www.flexigrid.info/">Flexigrid Home</a>
 * @see <a href="http://codeigniter.com/forums/viewthread/75326">CodeIgniter Flexigrid Forum</a>
 */
public final class FlexiGrid extends Component implements Pane {
    private static final long serialVersionUID = 7873962246421609162L;

    private static final String CSS_REFERENCE = ResourceHelper.getFileAsString("js/flexigrid/css/flexigrid/flexigrid-template.css");

    /**
     * the sorting model to be displayed in the grid and used as parameter if the sorting changes
     */
    public static final String PROPERTY_SORTINGMODEL = "sortingModel";


    /**
     * the column model to be displayed in the grid
     */
    public static final String PROPERTY_COLUMNMODEL = "columnModel";
    /**
     * the title at the top of the grid
     */
    public static final String PROPERTY_TITLE = "title";
    /**
     * the width of the grid itself
     */
    public static final String PROPERTY_WIDTH = "width";
    /**
     * the height of the grid itself
     */
    public static final String PROPERTY_HEIGHT = "height";
    /**
     * the heightOffset is used to determine the correct maximum height if height is 'auto'.
     */
    public static final String PROPERTY_HEIGHT_OFFSET = "heightOffset";
    
    /**
     * if <code>true</code>, the grid will have a button to hide and show the grid at the top right
     */
    public static final String PROPERTY_SHOW_TABLE_TOGGLE_BUTTON = "showTableToggle";
    /**
     * the css as string injected in the html-head, since echo3 doesn't support any css styling by default
     */
    public static final String PROPERTY_CSS = "css";
    /**
     * if <code>true</code> the grid is resizable horizontally and vertically
     */
    public static final String PROPERTY_RESIZABLE = "resizable";
    /**
     * if <code>true</code> the client side sorting algorithm is enabled.
     */
    public static final String PROPERTY_CLIENT_SORTING = "clientSorting";
    /**
     * if the client side sorting algorithm is enabled you need to specify this delimiter value to sort numbers,e.g.
     * '1000,00' regarding to you locale.
     */
    public static final String PROPERTY_DIGITGROUP_DELIMITER = "digitGroupDelimiter";

    /**
     * To Specify a delimiter for Decimal-Values: e.g. 17,345 or 258845,66
     */
    public static final String PROPERTY_DECIMAL_DELIMITER = "decimalDelimiter";

    /**
     * <code>true</code> if the pager is shown
     */
    public static final String PROPERTY_SHOW_PAGER = "showPager";
    /**
     * <code>true</code> if the page statistics are shown in the footer
     */
    public static final String PROPERTY_SHOW_PAGE_STAT = "showPageStatistics";
    /**
     * <code>true</code> if the results per page are shown
     */
    public static final String PROPERTY_SHOW_RESULTS_PPAGE = "showResultsPerPage";
    
       /**
     * the message displayed if no items were found, e.g. "no items found"
     */
    public static final String PROPERTY_NO_ITEMS_MSG = "messageNoItems";
    /**
     * the message displayed while processing the data
     */
    public static final String PROPERTY_PROCESS_MSG = "messageProcessing";
    /**
     * the message displayed as tooltip on a column
     */
    public static final String PROPERTY_HIDE_COLUMN_MSG = "messageColumnHiding";
    /**
     * the message displayed on the button to hide the table
     */
    public static final String PROPERTY_MIN_TABLE_MSG = "messageTableHiding";
    /**
     * the message displayed as page statistics
     */
    public static final String PROPERTY_PAGE_STATISTICS_MSG = "messagePageStatistics";
    /**
     * the state if the even and odd rows have different colors, i.e. "striped".
     */
    public static final String PROPERTY_STRIPED = "striped";
    /**
     * the minimal width of the grid if the user resizes
     */
    public static final String PROPERTY_COLUMN_MIN_WIDTH = "minColumnWidth";
    /**
     * the minimal height of the grid if the user resizes
     */
    public static final String PROPERTY_MIN_COLUMN_HEIGHT = "minColumnHeight";
    /**
     * <code>true</code> if no wrap is enabled
     */
    public static final String PROPERTY_NO_WRAP = "noWrap";
    /**
     * if <code>true</code> the selection is switched to single select otherwise multiselect
     */
    public static final String PROPERTY_SINGLE_SELECT = "singleSelect";

    // ** Active Page Properties */
    public static final String PROPERTY_ACTIVE_PAGE = "activePage";
    public static final String INPUT_ACTIVE_PAGE_CHANGED = "activePageChanged";
    public static final String TABLE_ACTIVE_PAGE_LISTENERS_CHANGED_PROPERTY = "activePageChangedListeners";

    // ** Table Row Selection Properties */
    public static final String PROPERTY_TABLE_ROW_SELECTION = "tableRowSelection";
    public static final String INPUT_TABLE_ROW_SELECTION_CHANGED = "tableRowSelectionChanged";
    public static final String TABLE_ROW_SELECTION_LISTENERS_CHANGED_PROPERTY = "tableRowSelectionListeners";
  
    // ** Results Per Page Option Properties */
    public static final String PROPERTY_RESULTS_PER_PAGE_OPTION = "resultsPerPageOption";
    public static final String INPUT_PROPERTY_RESULTS_PER_PAGE_OPTION_CHANGED = "resultsPerPageOptionChanged";
    public static final String RESULTS_PER_PAGE_OPTION_LISTENERS_CHANGED_PROPERTY = "resultsPerPageOptionListeners";
    
    public static final String PROPERTY_CELLS_UPDATE = "cellsUpdate";
    
    /**
     * The constant used to track changes to the action listener list.
     */
    public static final String TABLE_COLUMNTOGGLE_LISTENERS_CHANGED_PROPERTY = "tableColumnToggleListeners";
    /**
     * The constant used to track changes to the action listener list.
     */
    public static final String TABLE_SORTCHANGE_LISTENERS_CHANGED_PROPERTY = "tableSortingChangeListeners";

    /**
     * The name of the action event registered in the peer when action listeners are added or removed.
     */
    public static final String INPUT_TABLE_COLUMN_TOGGLE = "tableColumnToggle";
    /**
     * The name of the action event registered in the peer when action listeners are added or removed.
     */
    public static final String INPUT_TABLE_SORTING_CHANGE = "tableSortingChange";

    public static final String PROPERTY_LINE_IMG = "LINE_IMG";
    public static final String PROPERTY_HL_IMG = "HL_IMG";
    public static final String PROPERTY_FHBG_IMG = "FHBG_IMG";
    public static final String PROPERTY_DDN_IMG = "DDN_IMG";
    public static final String PROPERTY_WBG_IMG = "WBG_IMG";
    public static final String PROPERTY_UUP_IMG = "UUP_IMG";
    public static final String PROPERTY_BGROUND_IMG = "BGROUND_IMG";
    public static final String PROPERTY_DOWN_IMG = "DOWN_IMG";
    public static final String PROPERTY_UP_IMG = "UP_IMG";
    public static final String PROPERTY_PREV_IMG = "PREV_IMG";
    public static final String PROPERTY_MAGNIFIER_IMG = "MAGNIFIER_IMG";
    public static final String PROPERTY_FIRST_IMG = "FIRST_IMG";
    public static final String PROPERTY_NEXT_IMG = "NEXT_IMG";
    public static final String PROPERTY_LAST_IMG = "LAST_IMG";
    public static final String PROPERTY_LOAD_IMG = "LOAD_IMG";
    public static final String PROPERTY_LOAD_BTN_IMG = "LOAD_BTN_IMG";

    private static final ImageReference LINE_IMG =
            new ResourceImageReference("js/flexigrid/css/flexigrid/images/line.gif");
    private static final ImageReference HL_IMG = new ResourceImageReference("js/flexigrid/css/flexigrid/images/hl.png");
    private static final ImageReference FHBG_IMG =
            new ResourceImageReference("js/flexigrid/css/flexigrid/images/fhbg.gif");
    private static final ImageReference DDN_IMG =
            new ResourceImageReference("js/flexigrid/css/flexigrid/images/ddn.png");
    private static final ImageReference WBG_IMG =
            new ResourceImageReference("js/flexigrid/css/flexigrid/images/wbg.gif");
    private static final ImageReference UUP_IMG =
            new ResourceImageReference("js/flexigrid/css/flexigrid/images/uup.png");
    private static final ImageReference BGROUND_IMG =
            new ResourceImageReference("js/flexigrid/css/flexigrid/images/bg.gif");
    private static final ImageReference DOWN_IMG =
            new ResourceImageReference("js/flexigrid/css/flexigrid/images/dn.png");
    private static final ImageReference UP_IMG = new ResourceImageReference("js/flexigrid/css/flexigrid/images/up.png");
    private static final ImageReference PREV_IMG =
            new ResourceImageReference("js/flexigrid/css/flexigrid/images/prev.gif");
    private static final ImageReference MAGNIFIER_IMG =
            new ResourceImageReference("js/flexigrid/css/flexigrid/images/magnifier.png");
    private static final ImageReference FIRST_IMG =
            new ResourceImageReference("js/flexigrid/css/flexigrid/images/first.gif");
    private static final ImageReference NEXT_IMG =
            new ResourceImageReference("js/flexigrid/css/flexigrid/images/next.gif");
    private static final ImageReference LAST_IMG =
            new ResourceImageReference("js/flexigrid/css/flexigrid/images/last.gif");
    private static final ImageReference LOAD_IMG =
            new ResourceImageReference("js/flexigrid/css/flexigrid/images/load.png");
    private static final ImageReference LOAD_BTN_IMG =
            new ResourceImageReference("js/flexigrid/css/flexigrid/images/load.gif");
        
    
    private static final String FLEXIGRID_CHILDS_PROPS = "_FLEXIGRID_CHILDS_PROPS";
    
    /**
     * This is due to the lack of knowledge how to force a sync on the client.
     */
    private final TableSortingChangeListener TABLE_SORTING_CHANGE_LISTENER = new TableSortingChangeListener() {
        @Override
        public void sortingChange(TableSortingChangeEvent e) { }
    };
    
    private final TableRowSelectionChangeListener TABLE_RS_CHANGE_LISTENER = new TableRowSelectionChangeListener() {
        @Override
        public void rowSelection(TableRowSelectionEvent e) {
          FlexiGrid.this.set(
                    FlexiGrid.PROPERTY_TABLE_ROW_SELECTION, 
                    e.getRowSelection().getAllSelectedRowsIds(), 
                    false
                  );
        }
    };
    
    private final FlexiTableModelListener FLEX_TABLE_MODEL_LISTENER = new FlexiTableModelListener() {
        @Override
        public void flexTableChanged(FlexTableModelEvent event) {
          final int type = event.getType();
          switch(type) {
            // table has new rows
            // ------------------
            case FlexTableModelEvent.INSERT:
              setLastActivePage();
              break;
            // table has deleted rows
            // ----------------------
            case FlexTableModelEvent.DELETE:
              int[] deletedRowsIds = event.getRowsIds();
              for(int rowId : deletedRowsIds)
                releaseRowResources(rowId);
              
              final int lastPageIdx = FlexiGrid.this.getTotalPageCount();
              int currentPageIdx = activePageIdx;              
              if(currentPageIdx > lastPageIdx) {
                  currentPageIdx = lastPageIdx;
              }
              setActivePage(currentPageIdx);
              break;            
            // table has rows that were updated
            // -----------------------
            case FlexTableModelEvent.UPDATE:
              int[] updatedRowsIds = event.getRowsIds();
              for(int rowId : updatedRowsIds)
                releaseRowResources(rowId);              
              
              set(PROPERTY_ACTIVE_PAGE, new FlexiPage(1, 1, new FlexiRow[0]), false);
              reloadCurrentPage();
              break;
            // column model is updated
            // -----------------------
            case FlexTableModelEvent.STRUCTURE_CHANGED:
              validateColumnModel();
              break;
            default:
              throw new Error("Unsupported FlexTableModelEvent type!");
          }
        }
        
        private void releaseRowResources(int rowId) {
//            HashMap<Component, Integer> rowComponents = childs.remove(rowId);
//            if(rowComponents != null) {
//                FlexiGrid.this.childsForReplace.addAll(rowComponents.values());
//            }
        }
    };
    
    private final PropertyChangeListener CELL_LAYOUT_DATA_LISTENER = new PropertyChangeListener() {
        private boolean inProcess = false;
                
        private void addToCollection(Integer componentID, ArrayList<FlexiCell> collection) {
            Component component = FlexiGrid.this.getComponent(componentID);
            MutableStyle componentStyle = (MutableStyle) component.getLocalStyle();
            FlexiCell componentCell = (FlexiCell) componentStyle.getIndex(FLEXIGRID_CHILDS_PROPS, 0);
            collection.add(componentCell);
        }
        
        @Override
        public void propertyChange(PropertyChangeEvent pce) {          
            if (this.inProcess || activePageIdx == -1) {
                System.out.println("return");
                return;
            } else {
                this.inProcess = true;
                
                Component source = (Component) pce.getSource();

                // prev layoutData ...
                FlexiCellLayoutData oldLayoutData = (FlexiCellLayoutData) pce.getOldValue();
                Extent oldWidth = oldLayoutData.getWidth();
                Extent oldHeight = oldLayoutData.getHeight();

                // new layoutData ...
                FlexiCellLayoutData newLayoutData = (FlexiCellLayoutData) pce.getNewValue();            
                Extent newWidth = newLayoutData.getWidth();
                Extent newHeight = newLayoutData.getHeight();

                // check for changes in height or width
                // ... ignore all the other changes ...
                // ------------------------------------
                int widthResult = newWidth.compareTo(oldWidth);
                int heightResult = newHeight.compareTo(oldHeight);
                if(widthResult == 0 && heightResult == 0) {
                    System.out.println("return");
                    this.inProcess = false;
                    return;
                }
                MutableStyle sourceStyle = (MutableStyle) source.getLocalStyle();
                FlexiCell sourceCell = (FlexiCell) sourceStyle.getIndex(FLEXIGRID_CHILDS_PROPS, 0);
                int colID = sourceCell.getColId();
                int rowID = sourceCell.getRowId();


                // cells in current row !!!
                ArrayList<FlexiCell> rowCells = new ArrayList<FlexiCell>();
                for (Integer componentID : rowsChilds.get(rowID)) {
                    addToCollection(componentID, rowCells);
                }

                // cells in current column !!!
                ArrayList<FlexiCell> columnCells = new ArrayList<FlexiCell>();
                for (Integer componentID : columnsChilds.get(colID)) {
                    addToCollection(componentID, columnCells);
                }

                if (widthResult >= 1) {
                    for (FlexiCell fc : columnCells) {
                        fc.setWidth(newWidth);
                    }
                    maxColumnWidths.put(colID, newWidth);
                } else {
                    sourceCell.setWidth(oldWidth);
                }

                if (heightResult >= 1) {
                    for (FlexiCell fc : rowCells) {
                        fc.setHeight(newHeight);
                    }
                    maxRowHeights.put(rowID, newHeight);
                } else {
                    sourceCell.setHeight(oldHeight);
                }

                // finishing ...
                System.out.println("work ...");
                this.inProcess = false;
            }
        }
    };
    
    private FlexiTableModel tableModel;
    private int activePageIdx = -1;
    private FlexiSortingModel sortingModel;
    
    /* Key: Row's ID -> Value: components' ids */
    private final HashMap<Integer, ArrayList<Integer>> rowsChilds = new HashMap<Integer, ArrayList<Integer>>();
    
    /* Key: Column's ID -> Value: components' ids */
    private final HashMap<Integer, ArrayList<Integer>> columnsChilds = new HashMap<Integer, ArrayList<Integer>>();
    
    /* Contains indeces of childs that are free to replace */
    private final ArrayList<Integer> childsForReplace = new ArrayList<Integer>();
        
    /* Max width for each column | Key: ColumnID; Value: MaxWidth */
    private HashMap<Integer, Extent> maxColumnWidths = new HashMap<Integer, Extent>();
    
    /* Max height for each row | Key: RowID; Value: MaxHeight */
    private HashMap<Integer, Extent> maxRowHeights = new HashMap<Integer, Extent>();
    
    /**
     * Default constructor for a {@link FlexiGrid}. Sets the several default values.
     */
    public FlexiGrid() {
      this("No items", "Processing, please wait ...", "Displaying {from} to {to} of {total} items");
    }
    
    public FlexiGrid(String noItemsMsg, String processingMsg, String pageStatisticsMsg) {
        super();
        setCSS(CSS_REFERENCE);
        setHeight(-1);
        setWidth(-1);
        setTitle("false");
        setShowTableToggleButton(Boolean.TRUE);
        setShowPager(Boolean.FALSE);
        setShowResultsPerPage(Boolean.FALSE);
        setResultsPerPageOption(null);
        setMessageNoItems(noItemsMsg);
        setMessageProcessing(processingMsg);
        setMessagePageStatistics(pageStatisticsMsg);
        setStriped(Boolean.TRUE);
        setMinimalColumnWidth(30);
        setMinimalColumnHeight(80);
        setNoWrap(Boolean.TRUE);
        setSingleSelect(Boolean.TRUE);
        setNewCellsUpdate(new FlexiCellsUpdate(new HashMap<FlexiCell, Integer[]>()));

        /* images */
        set(PROPERTY_LINE_IMG, LINE_IMG);
        set(PROPERTY_HL_IMG, HL_IMG);
        set(PROPERTY_FHBG_IMG, FHBG_IMG);
        set(PROPERTY_DDN_IMG, DDN_IMG);
        set(PROPERTY_WBG_IMG, WBG_IMG);
        set(PROPERTY_UUP_IMG, UUP_IMG);
        set(PROPERTY_BGROUND_IMG, BGROUND_IMG);
        set(PROPERTY_DOWN_IMG, DOWN_IMG);
        set(PROPERTY_UP_IMG, UP_IMG);
        set(PROPERTY_PREV_IMG, PREV_IMG);
        set(PROPERTY_MAGNIFIER_IMG, MAGNIFIER_IMG);
        set(PROPERTY_FIRST_IMG, FIRST_IMG);
        set(PROPERTY_NEXT_IMG, NEXT_IMG);
        set(PROPERTY_LAST_IMG, LAST_IMG);
        set(PROPERTY_LOAD_IMG, LOAD_IMG);
        set(PROPERTY_LOAD_BTN_IMG, LOAD_BTN_IMG);

        // this is due to the lack of knowledge how to force a sync on the client
        addTableSortingChangeListener(TABLE_SORTING_CHANGE_LISTENER);
        addTableRowSelectionListener(TABLE_RS_CHANGE_LISTENER);
    }

    /**
     * Sets a generic property of the <code>Component</code>.
     * The value will be stored in this <code>Component</code>'s local style.
     * 
     * @param propertyName the name of the property
     * @param newValue the value of the property
     * @firePropertyChangeEvent flag for firePropertyChangeEvent
     * @see #get(java.lang.String)
     */
    private void set(String propertyName, Object newValue, boolean firePropertyChangeEvent) {
        MutableStyle ms = (MutableStyle) getLocalStyle();
        Object oldValue = ms.get(propertyName);
        ms.set(propertyName, newValue);
        if (firePropertyChangeEvent) {
            firePropertyChange(propertyName, oldValue, newValue);
        }
    }
    
    
    /**
     * Set current selected rows' ids.
     * 
     * @param rowsIds
     */
    public void selectRows(Integer... rowsIds) {      
        FlexiPage currentPage = (FlexiPage) get(PROPERTY_ACTIVE_PAGE);
        ArrayList<Integer> currentRowsIds = new ArrayList<Integer>(Arrays.asList(currentPage.getRowsIds()));
        
        ArrayList<Integer> validRowsIds = new ArrayList<Integer>();
        for(Integer id : rowsIds) {
          if(currentRowsIds.contains(id))
            validRowsIds.add(id);
        }
        
        set(PROPERTY_TABLE_ROW_SELECTION, validRowsIds.toArray(new Integer[validRowsIds.size()]), true);
    }
    
    /**
     * Unselect rows from current selection.
     * 
     * @param rowsIds 
     */
    public void unselectRows(Integer... rowsIds) {
      HashSet<Integer> selected = new HashSet(Arrays.asList(getSelectedRowsIds()));
      for(Integer id : rowsIds)
        selected.remove(id);
      selectRows(selected.toArray(new Integer[selected.size()]));
    }    
    
    /**
     * Set the tableModel
     *
     * @param tableModel the FlexiTableModel for FlexiGrid
     */
    public void setFlexTableModel(FlexiTableModel tableModel) {        
        this.tableModel = tableModel;
        
        // wenn das flexTableModel null ist, dann wars das hier auch schon
        if(tableModel == null) {
            setColumnModel(new FlexiColumnModel(new FlexiColumn[0]));
            return;
        }
        
        tableModel.addFlexTableModelListener(FLEX_TABLE_MODEL_LISTENER);        
        validateColumnModel();
        setActivePage(1);
        // setzen der RowsPerPage Option
        if (tableModel.getRowsPerPageCount() == FlexiTableModel.SHOW_ALL_ROWS_ON_ONE_PAGE) {
            setResultsPerPageOption(new ResultsPerPageOption(tableModel.getRowCount(), new int[]{tableModel.getRowCount()}));
        } else {
            setResultsPerPageOption(new ResultsPerPageOption(tableModel.getRowsPerPageCount(), new int[]{tableModel.getRowsPerPageCount()}));
        }
    }
    
    /**
     * Create column models by tableModel
     */
     private void validateColumnModel() {
        int columnCount = tableModel.getColumnCount();
        FlexiColumn[] columns = new FlexiColumn[columnCount];
        Extent maxHeight = new Extent(0);
        for (int c = 0; c < columnCount; c++) {
            FlexiColumn currentColumn = tableModel.getColumnAt(c);
            columns[c] = currentColumn;
            
            // add column cell to FlexiGrid
            // ----------------------------
            FlexiCell columnCell = currentColumn.getCell();
            addFlexiCell(columnCell);
            
            // check for max height
            // --------------------
            Extent columnHeight = columnCell.getHeight();
            if (columnHeight.compareTo(maxHeight) > 0) {
                maxHeight = columnHeight;
            }
            
            // set default columns widths
            // --------------------------
            maxColumnWidths.put(currentColumn.getId(), columnCell.getWidth());
        }
        
        // set max height for each column from model
        // -----------------------------------------
        maxRowHeights.put(-1, maxHeight);
        tableModel.getColumnAt(0).getCell().setHeight(maxHeight);
        
        // set new column model
        // --------------------
        setFlexiColumnModel(new FlexiColumnModel(columns));
    }
    

    /**
     * Returns the FlexTableModel
     *
     * @return the flexTableModel
     */
    public FlexiTableModel getFlexTableModel() {
        return tableModel;
    }
        
    public int getTotalPageCount() {
        if(tableModel.getRowsPerPageCount() == FlexiTableModel.SHOW_ALL_ROWS_ON_ONE_PAGE)
          return 1;

        final int totalRowCount = tableModel.getRowCount();
        final int rowsPerPageCount = getResultsPerPageOption().getInitialOption();

        if((totalRowCount % rowsPerPageCount) == 0)
            return totalRowCount / rowsPerPageCount;
        else
            return totalRowCount / rowsPerPageCount + 1;
    }
    
    public void setLastActivePage() {
        setActivePage(getTotalPageCount());
    }
    
    public void reloadCurrentPage() {
        setActivePage(activePageIdx);
    }
          
    /**
     * Set the current activePage of the TableModel
     * The needed Rows for this page will be extracted and transported to client to be visible in flexigrid.
     *
     * @param page
     */
    public void setActivePage(int page) {
        activePageIdx = page;
        FlexiPage requestedPage = null;
        if(tableModel == null) {
            requestedPage = new FlexiPage(1, 1, new FlexiRow[0]);
        }
        else {
            requestedPage = makePage(page);
        }        
        setActivePage(requestedPage);
    }
    
    /**
     * Make new Page object
     * 
     * @param page
     * @return new Page
     */
    private FlexiPage makePage(int page) {        
        int firstRowStart;
        int rowEnd;

        // if all Rows should be displayed on one page ...
        // -----------------------------------------------
        if (tableModel.getRowsPerPageCount() == FlexiTableModel.SHOW_ALL_ROWS_ON_ONE_PAGE) {
            // ... we set rowStart to zero and rowEnd to maximum
            // -------------------------------------------------
            firstRowStart = 0;
            rowEnd = tableModel.getRowCount();
        } else {
            // ... otherwise if there is some paging active we have to calculate the range of rows to display
            // ----------------------------------------------------------------------------------------------
            firstRowStart = (page - 1) * tableModel.getRowsPerPageCount();
            rowEnd = firstRowStart + tableModel.getRowsPerPageCount();
            if (rowEnd > tableModel.getRowCount()) {
                rowEnd = tableModel.getRowCount();
            }
        }

        // The number of rows for this page
        // --------------------------------
        int amountOfRows = rowEnd - firstRowStart;
        
        // The number of columns for this page
        // -----------------------------------
        int amountOfColumns = tableModel.getColumnCount();

        // The structure of the page
        // -------------------------
        FlexiRow[] rows = new FlexiRow[amountOfRows];        
        int rowCounter = 0;
        for (int currentRow = firstRowStart; currentRow < rowEnd; currentRow++) {
            FlexiCell[] rowCells = new FlexiCell[amountOfColumns];
            int rowId = tableModel.getRowAt(currentRow).getId();
            Extent rowMaxHeight = new Extent(0);
            for (int currentColumn = 0; currentColumn < amountOfColumns; currentColumn++) {                
                // process FlexiColumn
                // --------------
                FlexiColumn column = tableModel.getColumnAt(currentColumn);
                int colID = column.getId();
                FlexiCell columnCell = column.getCell();
                
                // process FlexiCell
                // -------------------
                FlexiCell cell = tableModel.getCellAt(currentRow, currentColumn);
                cell.equalizeLayoutDataTo(columnCell);
                
                // check for max width
                // ---------------------
                Extent currentMaxWidth = maxColumnWidths.get(colID);
                Extent cellWidth = cell.getWidth();
                if (cellWidth.compareTo(currentMaxWidth) >= 1) {
                    maxColumnWidths.put(colID, cellWidth);
                }
        
                // check for max height (current row)
                // ... may have rows with different heights
                // ----------------------------------------
                Extent height = cell.getHeight();
                if(height.compareTo(rowMaxHeight) >= 1) {
                    rowMaxHeight = height;
                }
                                
                // add current cell to FlexiGrid
                // -----------------------------------------
                addFlexiCell(cell);
                rowCells[currentColumn] = cell;                
            }
            
            // set the maximum height for each cell of current row
            // ---------------------------------------------------
            maxRowHeights.put(rowId, rowMaxHeight);
            for (int c = 0; c < amountOfColumns; c++) {
                if (rowCells[c].setHeight(rowMaxHeight)) {
                    break;
                }
            }
            
            FlexiRow row = new FlexiRow(rowId, rowCells);
            rows[rowCounter] = row;
            rowCounter++;
        }
        
        // set the maximum width for each column cell
        // ------------------------------------------
        for (int c = 0; c < amountOfColumns; c++) {
            FlexiColumn column = tableModel.getColumnAt(c);
            Extent maxWidth = maxColumnWidths.get(column.getId()); 
            if(column.getCell().setWidth(maxWidth)) {
                continue;
            }
            
            int startRow = firstRowStart;
            FlexiCell cell = null;
            do {
                cell = tableModel.getCellAt(startRow++, c);
            } while (!cell.setWidth(maxWidth) && startRow < rowEnd);
        }        
                
        return new FlexiPage(page, tableModel.getRowCount(), rows);
    }
    
    private void addFlexiCell(FlexiCell cell) {
        int rowID = cell.getRowId();
        int colID = cell.getColId();
        Component component = cell.getComponent();
        String componentID = component.getId();
        
        if (componentID == null) {
            Integer idx = null;
            if (!childsForReplace.isEmpty()) {                          
                idx = childsForReplace.get(0);
                remove(idx);
                add(component, idx);
            } else {
                idx = getComponentCount();
                add(component);
            }
            
            component.setId(idx.toString());            
            component.addPropertyChangeListener(Component.PROPERTY_LAYOUT_DATA, CELL_LAYOUT_DATA_LISTENER);
            MutableStyle componentStyle = (MutableStyle) component.getLocalStyle();
            componentStyle.setIndex(FLEXIGRID_CHILDS_PROPS, 0, cell);
            
            ArrayList<Integer> rowComponentsIds = rowsChilds.get(rowID);
            if (rowComponentsIds == null) {
                rowComponentsIds = new ArrayList<Integer>();
                rowsChilds.put(rowID, rowComponentsIds);
            }
            rowComponentsIds.add(idx);
            
            ArrayList<Integer> columnComponentsIds = columnsChilds.get(colID);
            if (columnComponentsIds == null) {
                columnComponentsIds = new ArrayList<Integer>();
                columnsChilds.put(colID, columnComponentsIds);
            }
            columnComponentsIds.add(idx);
        }
    }
    
       
    
    /**
     * Clear current selected rows' ids.
     * <br />
     * Called when active page is changed!
     */
    public void clearSelection() {
      set(PROPERTY_TABLE_ROW_SELECTION, new Integer[0], true);
    }
    
    /**
     * Sets the message displayed if no items were found, e.g. "no items found".
     *
     * @param newValue the message displayed if no items were found
     */
    public void setMessageNoItems(String newValue) {
        set(PROPERTY_NO_ITEMS_MSG, newValue);
    }

    /**
     * Returns the options of number of shown results per page.
     *
     * @return the options of number of shown results per page
     */
    public ResultsPerPageOption getResultsPerPageOption() {
        return (ResultsPerPageOption) get(PROPERTY_RESULTS_PER_PAGE_OPTION);
    }

    /**
     * Sets the options of number of shown results per page, e.g. "[10,15,20,25]".
     * Don't set this manually if you don't know what you are doing... - will be done by flexigrid for you
     *
     * @param newValue the initial number shown of results per page
     */
    public void setResultsPerPageOption(ResultsPerPageOption newValue) {
        set(PROPERTY_RESULTS_PER_PAGE_OPTION, newValue);
    }
    
    public FlexiCellsUpdate getLastCellsUpdate() {
        return (FlexiCellsUpdate) get(PROPERTY_CELLS_UPDATE);
    }
    
    public void setNewCellsUpdate(FlexiCellsUpdate update) {
        set(PROPERTY_CELLS_UPDATE, update);
    }

    /**
     * Returns <code>true</code> if the results per page are shown.
     *
     * @return <code>true</code> if results per page are shown
     */
    public Boolean getShowResultsPerPage() {
        return (Boolean) get(PROPERTY_SHOW_RESULTS_PPAGE);
    }

    /**
     * Sets the visibility of the results per page.
     *
     * @param newValue <code>true</code> the results per page are visible
     */
    public void setShowResultsPerPage(Boolean newValue) {
        set(PROPERTY_SHOW_RESULTS_PPAGE, newValue);
    }

    /**
     * Returns <code>true</code> if the pager is shown.
     *
     * @return <code>true</code> if the pager is shown
     */
    public boolean getShowPager() {
        return Boolean.getBoolean((String) get(PROPERTY_SHOW_PAGER));
    }

    /**
     * Sets the visibility of the pager.
     *
     * @param newValue <code>true</code> the pager is visible
     */
    public void setShowPager(boolean newValue) {
        set(PROPERTY_SHOW_PAGER, newValue);
    }

    /**
     * Returns <code>true</code> if the page statistics are shown.
     *
     * @return <code>true</code> if the page statistics are shown
     */
    public boolean getShowPageStatistics() {
        return Boolean.getBoolean((String) get(PROPERTY_SHOW_PAGE_STAT));
    }

    /**
     * Sets the visibility of the page statistics.
     *
     * @param newValue <code>true</code> the page statistics are visible
     */
    public void setShowPageStatistics(boolean newValue) {
        set(PROPERTY_SHOW_PAGE_STAT, newValue);
    }

    /**
     * Returns the cascading style sheet for this component.
     *
     * @return the cascading style sheet
     */
    public String getCSS() {
        return (String) get(PROPERTY_CSS);
    }

    /**
     * Sets the cascading style sheet for this component.
     *
     * @param newValue the new css
     */
    public void setCSS(String newValue) {
        set(PROPERTY_CSS, newValue);
    }
    
    /**
     * Returns <code>true</code> the selection is switched to single select otherwise multiple selection is used.
     *
     * @return <code>true</code> single select, otherwise multiple selection of rows
     */
    public Boolean getSingleSelect() {
        return (Boolean) get(PROPERTY_SINGLE_SELECT);
    }

    /**
     * Sets the selection mode. The selection is switched to single select with <code>true</code> otherwise multiple
     * selection is used.
     *
     * @param newValue the state of single selection/ or multiple selection
     */
    public void setSingleSelect(Boolean newValue) {
        set(PROPERTY_SINGLE_SELECT, newValue);
    }

    /**
     * Returns <code>true</code> if no wrap is enabled.
     *
     * @return <code>true</code> if no wrap is enabled
     */
    public Boolean getNoWrap() {
        return (Boolean) get(PROPERTY_NO_WRAP);
    }

    /**
     * Sets the state if no wrap is enabled.
     *
     * @param newValue the state if no wrap is enabled
     */
    public void setNoWrap(Boolean newValue) {
        set(PROPERTY_NO_WRAP, newValue);
    }

    /**
     * Returns the minimal of the columns in the grid if the user resizes.
     *
     * @return the amount of minimal height used by a column if the user resizes
     */
    public int getMinimalColumnHeight() {
        return Integer.parseInt((String) get(PROPERTY_MIN_COLUMN_HEIGHT));
    }

    /**
     * Sets the minimal height of the columns in the grid if the user resizes.
     *
     * @param newValue the minimal height of the grid if the user resizes
     */
    public void setMinimalColumnHeight(int newValue) {
        set(PROPERTY_MIN_COLUMN_HEIGHT, newValue);
    }

    /**
     * Returns the minimal width of the columns in the grid if the user resizes.
     *
     * @return the amount of minimal width used by each column if the user resizes
     */
    public int getMinimalColumnWidth() {
        return Integer.parseInt((String) get(PROPERTY_COLUMN_MIN_WIDTH));
    }

    /**
     * Sets the minimal width of the columns in the grid if the user resizes.
     *
     * @param newValue the minimal width of the columns in the grid if the user resizes
     */
    public void setMinimalColumnWidth(int newValue) {
        set(PROPERTY_COLUMN_MIN_WIDTH, newValue);
    }

    /**
     * Returns the state if the even and odd rows have different colors, i.e. "striped".
     *
     * @return <code>true</code> if the even and odd rows have different colors
     */
    public Boolean getStriped() {
        return (Boolean) get(PROPERTY_STRIPED);
    }

    /**
     * Sets the state if the even and odd rows have different colors.
     *
     * @param newValue the state if the even and odd rows have different colors
     */
    public void setStriped(Boolean newValue) {
        set(PROPERTY_STRIPED, newValue);
    }

    /**
     * Returns the message displayed as page statistics.
     *
     * @return the message displayed as page statistics
     */
    public String getMessagePageStatistics() {
        return (String) get(PROPERTY_PAGE_STATISTICS_MSG);
    }

    /**
     * Sets the message displayed as page statistics. Use may use tokens to be replaced, such as
     * "Displaying {from} to {to} of {total} items".
     *
     * @param newValue the message displayed as page statistics
     */
    public void setMessagePageStatistics(String newValue) {
        set(PROPERTY_PAGE_STATISTICS_MSG, newValue);
    }

    /**
     * Returns the message displayed while processing the data.
     *
     * @return the message while processing the data
     */
    public String getMessageProcessing() {
        return (String) get(PROPERTY_PROCESS_MSG);
    }

    /**
     * Sets the message displayed while processing the data.
     *
     * @param newValue the message while processing the data
     */
    public void setMessageProcessing(String newValue) {
        set(PROPERTY_PROCESS_MSG, newValue);
    }

    /**
     * Returns the message displayed as tooltip on the hide button on the table headline.
     *
     * @return the tooltip on the hide button on the table headline
     */
    public String getMessageTableHiding() {
        return (String) get(PROPERTY_MIN_TABLE_MSG);
    }

    /**
     * Sets the message displayed as tooltip on the hide button on the table headline.
     *
     * @param newValue the message as tooltip on the hide button on the table headline
     */
    public void setMessageTableHiding(String newValue) {
        set(PROPERTY_MIN_TABLE_MSG, newValue);
    }

    /**
     * Returns the message displayed as tooltip on a column.
     *
     * @return the tooltip on a column
     */
    public String getMessageColumnHiding() {
        return (String) get(PROPERTY_HIDE_COLUMN_MSG);
    }

    /**
     * Sets the message displayed as tooltip on a column.
     *
     * @param newValue the message displayed as tooltip on a column
     */
    public void setMessageColumnHiding(String newValue) {
        set(PROPERTY_HIDE_COLUMN_MSG, newValue);
    }

    /**
     * Returns the message displayed if no items were found.
     *
     * @return the message displayed if no items were found
     */
    public String getMessageNoItems() {
        return (String) get(PROPERTY_NO_ITEMS_MSG);
    }
    
    /**
     * Returns current selected rows' ids.
     *
     * @return array of selected rows' ids
     */
    public Integer[] getSelectedRowsIds() {
      return (Integer[]) get(PROPERTY_TABLE_ROW_SELECTION);
    }
    
    
    /**
     * Set the current active Page
     * !! --------------------------------------------- !!
     * !! Be careful by calling this manually
     * !! It's better to use {@link #setActivePage(int)}
     * !! --------------------------------------------- !!
     *
     * @param newPage the active page to be set to current
     */
    private void setActivePage(final FlexiPage newPage) {
        clearSelection();
        set(PROPERTY_ACTIVE_PAGE, newPage);
    }

    /**
     * Return the current active page
     *
     * @return the current active page
     */
    public FlexiPage getActivePage() {
        return (FlexiPage) get(PROPERTY_ACTIVE_PAGE);
    }

    /**
     * Returns the current activePage Index
     *
     * @return the index of the current active page
     */
    public int getActivePageIdx() {
        return activePageIdx;
    }

    /**
     * Return the table model.
     *
     * @return The table model object or {@code null} if no such exists.
     */
    public FlexiColumnModel getColumnModel() {
        return (FlexiColumnModel) get(PROPERTY_COLUMNMODEL);
    }

    /**
     * Set the value of the {@link #PROPERTY_COLUMNMODEL} property.
     *
     * @param newTableModel The table model to be represented in this component.
     */
    public void setColumnModel(final FlexiColumnModel newColumnModel) {
        set(PROPERTY_COLUMNMODEL, newColumnModel);
    }
    
    /**
     * Set the value of the {@link #PROPERTY_COLUMNMODEL} property.
     *
     * @param columnModel The table model to be represented in this component.
     */
    public void setFlexiColumnModel(final FlexiColumnModel columnModel) {
        set(PROPERTY_COLUMNMODEL, columnModel);
    }

    /**
     * Set the value of the {@link #PROPERTY_SORTINGMODEL} property.
     *
     * @param newSortingModel The sorting model to be represented in this component.
     */
    public void setSortingModel(final FlexiSortingModel newSortingModel) {
        final FlexiSortingModel oldSortingModel = sortingModel;
        set(PROPERTY_SORTINGMODEL, newSortingModel);
        firePropertyChange(PROPERTY_SORTINGMODEL, oldSortingModel, newSortingModel);
    }

    /**
     * Return the sorting model of the grid.
     *
     * @return The sorting model object or {@code null} if no such exists.
     */
    public FlexiSortingModel getSortingModel() {
        sortingModel = (FlexiSortingModel) get(PROPERTY_SORTINGMODEL);
        return sortingModel;
    }

    /**
     * Return the table title above the table.
     *
     * @return The title is the name above the table.
     */
    public String getTitle() {
        return (String) get(PROPERTY_TITLE);
    }

    /**
     * Set the title above the table, see {@link #PROPERTY_TITLE} property.
     *
     * @param title The table title to be represented in this component.
     */
    public void setTitle(final String title) {
        set(PROPERTY_TITLE, title);
    }

    /**
     * Return the table height of the table.
     *
     * @return The height of the table.
     */
    public int getHeight() {
        return Integer.parseInt((String) get(PROPERTY_HEIGHT));
    }

    /**
     * Set the height of the table, see {@link #PROPERTY_HEIGHT} property. If the value is -1 its set to "auto" height.
     *
     * @param height The table height of this component.
     */
    public void setHeight(final int height) {
        set(PROPERTY_HEIGHT, height);
    }

    /**
     * Return the heightOffset is used to determine the correct maximum height if height is 'auto'.
     *
     * @return The height of the table.
     */
    public int getHeightOffset() {
        return Integer.parseInt((String) get(PROPERTY_HEIGHT_OFFSET));
    }

    /**
     * Set the heightOffset is used to determine the correct maximum height if height is 'auto'.
     *
     * @param height the heightOffset is used to determine the correct maximum height if height is 'auto'.
     */
    public void setHeightOffset(final int height) {
        set(PROPERTY_HEIGHT_OFFSET, height);
    }

    /**
     * Return the table width of the table.
     *
     * @return The width of the table.
     */
    public int getWidth() {
        return Integer.parseInt((String) get(PROPERTY_WIDTH));
    }

    /**
     * Set the width of the table, see {@link #PROPERTY_WIDTH} property. Can be a value like "400" interpreted as 400px
     * or "auto". If the value is -1 its set to "auto" height.
     *
     * @param width The table width of this component.
     */
    public void setWidth(final int width) {
        set(PROPERTY_WIDTH, width);
    }

    /**
     * Shows the button to hide and show the the table grid.
     *
     * @param showTableToggleButton <code>true</code> the button will be shown
     */
    public void setShowTableToggleButton(boolean showTableToggleButton) {
        set(PROPERTY_SHOW_TABLE_TOGGLE_BUTTON, showTableToggleButton);
    }

    /**
     * Return <code>true</code> if the button to hide and show the the table grid is visible.
     *
     * @return <code>true</code> if the button to hide and show the the table grid is visible.
     */
    public boolean getShowTableToggleButton() {
        return (Boolean) get(PROPERTY_SHOW_TABLE_TOGGLE_BUTTON);
    }

    /**
     * If <code>true</code> the whole grid is resizable vertically and horizontally.
     *
     * @param resizable <code>true</code> the grid is resizable
     */
    public void setResizable(boolean resizable) {
        set(PROPERTY_RESIZABLE, resizable);
    }

    /**
     * Return <code>true</code> if the grid is resizable vertically and horizontally.
     *
     * @return <code>true</code> if the grid is resizable.
     */
    public boolean getResizable() {
        return (Boolean) get(PROPERTY_RESIZABLE);
    }

    /**
     * !!!!!
     * Does no longer work with lazy loading
     * ! Feel free to fix this !
     * !!!!!
     * <p/>
     * If <code>true</code> the client side sorting algorithm is enabled. The client side sorting reduces the bandwidth
     * and supports multicolumn sorting for alpha numeric values. You can also use your own sorting method server side
     * by implementing an {@link TableSortingChangeListener} to sort and setting the updated {@link TableModel}
     * containing all sorted data. However this clientSorting value should then be set to <code>false</code> to avoid
     * double sorting.
     *
     * @param clientSorting <code>true</code> activates the client side sorting.
     * @deprecated
     */
    public void setClientSorting(boolean clientSorting) {
        set(PROPERTY_CLIENT_SORTING, clientSorting);
    }

    /**
     * Return <code>true</code> if the client side sorting algorithm is enabled.
     *
     * @return <code>true</code> if the debug mode is enabled.
     * @deprecated
     */
    public boolean getClientSorting() {
        return (Boolean) get(PROPERTY_CLIENT_SORTING);
    }

    /**
     * If the client side sorting algorithm is enabled you need to specify this delimiter value to sort numbers,e.g.
     * '1000,00' regarding to you locale.
     *
     * @param digitGroupDelimiter the delimiter, such as '.' or ',' or whatever.
     */
    public void setDigitGroupDelimiter(String digitGroupDelimiter) {
        set(PROPERTY_DIGITGROUP_DELIMITER, digitGroupDelimiter);
    }

    /**
     * Return the delimiter used for sorting numbers.
     *
     * @return the delimiter used for sorting numbers.
     */
    public String getDigitGroupDelimiter() {
        return (String) get(PROPERTY_DIGITGROUP_DELIMITER);
    }

    /**
     * If the client side sorting algorithm is enabled you need to specify this delimiter value to sort numbers with decimals,e.g.
     * '1000,345' or '23,66235' regarding to you locale.
     *
     * @param decimalDelimiter the delimiter, such as '.' or ',' or whatever.
     */
    public void setDecimalDelimiter(String decimalDelimiter) {
        set(PROPERTY_DECIMAL_DELIMITER, decimalDelimiter);
    }

    /**
     * Return the decimal-delimiter used for sorting numbers.
     *
     * @return the decimal-delimiter used for sorting numbers.
     */
    public String getDecimalDelimiter() {
        return (String) get(PROPERTY_DECIMAL_DELIMITER);
    }

    /**
     * Adds a {@link TableRowSelectionChangeListener}.
     *
     * @param l will be informed if a row is selected
     */
    public void addTableRowSelectionListener(TableRowSelectionChangeListener l) {
        getEventListenerList().addListener(TableRowSelectionChangeListener.class, l);
        firePropertyChange(TABLE_ROW_SELECTION_LISTENERS_CHANGED_PROPERTY, null, l);
    }

    /**
     * Removes a {@link TableRowSelectionChangeListener}
     *
     * @param l will be removed from listener list.
     */
    public void removeTableRowSelectionListener(TableRowSelectionChangeListener l) {
        getEventListenerList().removeListener(TableRowSelectionChangeListener.class, l);
        firePropertyChange(TABLE_ROW_SELECTION_LISTENERS_CHANGED_PROPERTY, l, null);
    }
    
    /**
     * Adds a {@link TableColumnToggleListener}.
     *
     * @param l will be informed if the state of a column changes from visible to invisible
     */
    public void addTableColumnToggleListener(TableColumnToggleListener l) {
        getEventListenerList().addListener(TableColumnToggleListener.class, l);
        firePropertyChange(TABLE_COLUMNTOGGLE_LISTENERS_CHANGED_PROPERTY, null, l);
    }

    /**
     * Removes a {@link TableColumnToggleListener}
     *
     * @param l will be removed from listener list.
     */
    public void removeTableColumnToggleListener(TableColumnToggleListener l) {
        getEventListenerList().removeListener(TableColumnToggleListener.class, l);
        firePropertyChange(TABLE_COLUMNTOGGLE_LISTENERS_CHANGED_PROPERTY, l, null);
    }

    /**
     * Adds a {@link TableSortingChangeListener}.
     *
     * @param l will be informed if the sorting changes for columns
     */
    public void addTableSortingChangeListener(TableSortingChangeListener l) {
        getEventListenerList().addListener(TableSortingChangeListener.class, l);
        firePropertyChange(TABLE_SORTCHANGE_LISTENERS_CHANGED_PROPERTY, null, l);
    }

    /**
     * Removes a {@link TableSortingChangeListener}
     *
     * @param l will be removed from listener list.
     */
    public void removeTableSortingChangeListener(TableSortingChangeListener l) {
        getEventListenerList().removeListener(TableSortingChangeListener.class, l);
        firePropertyChange(TABLE_SORTCHANGE_LISTENERS_CHANGED_PROPERTY, l, null);
    }
    
        
    /**
     * Adds a {@link ResultsPerPageOptionChangeListener}.
     *
     * @param l will be informed if the results per page options change
     */
    public void addResultsPerPageOptionChangeListener(ResultsPerPageOptionChangeListener l) {
        getEventListenerList().addListener(ResultsPerPageOptionChangeListener.class, l);
        firePropertyChange(RESULTS_PER_PAGE_OPTION_LISTENERS_CHANGED_PROPERTY, null, l);
    }
    
    
    /**
     * Removes a {@link ResultsPerPageOptionChangeListener}
     *
     * @param l will be removed from listener list.
     */
    public void removeResultsPerPageOptionChangeListener(ResultsPerPageOptionChangeListener l) {
        getEventListenerList().removeListener(ResultsPerPageOptionChangeListener.class, l);
        firePropertyChange(RESULTS_PER_PAGE_OPTION_LISTENERS_CHANGED_PROPERTY, l, null);
    }
    
    /**
     * Processes a user request to select a row with the given parameter.
     *
     * @param rowSelection the object containing information about the selection
     */
    public void userTableRowSelection(FlexiRowSelection rowSelection) {
        fireTableRowSelection(rowSelection);
    }

    /**
     * Processes a user request to toggle the visibility state of a column.
     *
     * @param columnVisibility the object containing information about the toggle
     */
    public void userTableColumnToggle(FlexiColumnVisibility columnVisibility) {
        fireTableColumnToggle(columnVisibility);
    }

    /**
     * Processes a user request to change the sorting of columns
     *
     * @param sortingModel the object containing information about the sorting
     */
    public void userTableSortingChange(FlexiSortingModel sortingModel) {
        fireTableSortingChange(sortingModel);
    }
    
    
    // by sieskei
    public void userResultsPerPageOptionChange(ResultsPerPageOption rppo) {
        fireResultsPerPageOptionChange(rppo);
    }

    /**
     * Notifies <code>TableRowSelectionChangeListener</code>s that the user has selected a row.
     */
    protected void fireTableRowSelection(FlexiRowSelection rowSelection) {
        if (!hasEventListenerList()) {
            return;
        }
        EventListener[] listeners = getEventListenerList().getListeners(TableRowSelectionChangeListener.class);
        if (listeners.length == 0) {
            return;
        }
        TableRowSelectionEvent e = new TableRowSelectionEvent(this, rowSelection);
        for (int i = 0; i < listeners.length; ++i) {
            ((TableRowSelectionChangeListener) listeners[i]).rowSelection(e);
        }
    }    

    /**
     * Notifies <code>TableRowSelectionChangeListener</code>s that the user has selected a row.
     */
    protected void fireTableColumnToggle(FlexiColumnVisibility columnVisibility) {
        if (!hasEventListenerList()) {
            return;
        }
        EventListener[] listeners = getEventListenerList().getListeners(TableColumnToggleListener.class);
        if (listeners.length == 0) {
            return;
        }
        TableColumnToggleEvent e = new TableColumnToggleEvent(this, columnVisibility);
        for (int i = 0; i < listeners.length; ++i) {
            ((TableColumnToggleListener) listeners[i]).columnToggle(e);
        }
    }

    /**
     * Notifies <code>TableSortingChangeListener</code>s that the user has changed the sorting.
     */
    protected void fireTableSortingChange(FlexiSortingModel sortingModel) {
        if (!hasEventListenerList()) {
            return;
        }
        EventListener[] listeners = getEventListenerList().getListeners(TableSortingChangeListener.class);
        if (listeners.length == 0) {
            return;
        }
        TableSortingChangeEvent e = new TableSortingChangeEvent(this, sortingModel);
        for (int i = 0; i < listeners.length; ++i) {
            ((TableSortingChangeListener) listeners[i]).sortingChange(e);
        }
    }
    
    
    /**
     * Notifies <code>ResultsPerPageOptionChangeListener</code>s that the user has changed the results per page option.
     */
    protected void fireResultsPerPageOptionChange(ResultsPerPageOption rppo) {
        if (!hasEventListenerList()) {
            return;
        }
        EventListener[] listeners = getEventListenerList().getListeners(ResultsPerPageOptionChangeListener.class);
        if (listeners.length == 0) {
            return;
        }
        ResultsPerPageOptionEvent e = new ResultsPerPageOptionEvent(this, rppo);
        for (int i = 0; i < listeners.length; ++i) {
            ((ResultsPerPageOptionChangeListener) listeners[i]).resultsPerPageChange(e);
        }
    }

    /**
     * Determines the any <code>TableRowSelectionChangeListener</code>s are registered.
     *
     * @return true if any <code>TableRowSelectionChangeListener</code>s are registered
     */
    public boolean hasTableRowSelectionListeners() {
        if (!hasEventListenerList()) {
            return false;
        }
        return getEventListenerList().getListenerCount(TableRowSelectionChangeListener.class) > 0;
    }

    /**
     * Determines the any <code>TableColumnToggleListener</code>s are registered.
     *
     * @return true if any <code>TableColumnToggleListener</code>s are registered
     */
    public boolean hasTableColumnToggleListeners() {
        if (!hasEventListenerList()) {
            return false;
        }
        return getEventListenerList().getListenerCount(TableColumnToggleListener.class) > 0;
    }

    /**
     * Determines the any <code>TableSortingChangeListener</code>s are registered.
     *
     * @return true if any <code>TableSortingChangeListener</code>s are registered
     */
    public boolean hasTableSortingChangeListeners() {
        if (!hasEventListenerList()) {
            return false;
        }
        return getEventListenerList().getListenerCount(TableSortingChangeListener.class) > 0;
    }
    
    /**
     * Determines the any <code>ResultsPerPageOptionChangeListener</code>s are registered.
     *
     * @return true if any <code>ResultsPerPageOptionChangeListener</code>s are registered
     */
    public boolean hasResultPerPageOptionChangeListeners() {
        if (!hasEventListenerList()) {
            return false;
        }
        return getEventListenerList().getListenerCount(ResultsPerPageOptionChangeListener.class) > 0;
    }
}