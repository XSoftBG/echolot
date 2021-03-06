/** The column model object describes table columns. */
exxcellent.model.ColumnModel = Core.extend({
    /**
             * An array of exxcellent.model.Column.
             * @type exxcellent.model.Column
             */
    columns : null,

    $construct : function(columns) {
        this.columns = columns;
    },

    getColumns: function() {
        return this.columns;
    },

    /** Return the string representation of this model. */
    toString : function() {
        return this.columns;
    }
});

/** The column object for any grid based component. */
exxcellent.model.Column = Core.extend({
    /** The id of the column */
    name: null,

    /** The name (title) for the column. */
    display : null,

    /**
             * The width for this colum.
             */
    width : null,

    /**
             * Defines if the column is sortable.
             */
    sortable: null,

    /**
             * Defines the alignment: 'left', 'right', 'center'
             * @type String
             */
    align: null,

    /**
             * The tooltip displayed if the user hovers over the column
             * @type String the tooltip
             */
    tooltip: null,

    $construct : function(name, display, width, sortable, align, tooltip) {
        this.name = name;
        this.display = display;
        this.width = width;
        this.sortable = sortable;
        this.align = align;
        this.tooltip = tooltip;
    },

    /** Return the string representation of this column. */
    toString : function() {
        return this.name;
    }
});

/** The data object for any grid based component. */
exxcellent.model.Row = Core.extend({
    /** The id of the row */
    id: null,

    /**
     * The data for this row as index based array,
     * e.g. ["value a", "value b"].
     */
    cell : null,

    $construct : function(id, cell) {
        this.id = id;
        this.cell = cell;
    },

    /** Return the string representation of this row. */
    toString : function() {
        return this.id;
    }
});

/** The page object for any grid based component. */
exxcellent.model.Page = Core.extend({
    /** page number */
    page : 1,
    /** amount if rows in the page*/
    total: 1,
    /** array of exxcellent.model.Row objects */
    rows : null,

    $construct : function(page, total, rows) {
        this.page = page;
        this.total = total;
        this.rows = rows;
    },

    /** Return the string representation of this model. */
    toString : function() {
        return this.page;
    }
});

/** The table model object for any grid based component. */
exxcellent.model.TableModel = Core.extend({
    /**
             * An array of exxcellent.model.Row
             * @type exxcellent.model.Page
             */
    pages : null,

    $construct : function(pages) {
        this.pages = pages;
    },

    /** Return the string representation of this table model. */
    toString : function() {
        return this.pages;
    }
});

/** The results per page model object for any grid based component. */
exxcellent.model.ResultsPerPageOption = Core.extend({
    /**
             * An array of int, e.g. [10,25,50]
             * @type int[]
             */
    pageOptions : null,

    /**
             * An initial value preselected as results per page
             * @type int
             */
    initialOption: null,

    $construct : function(initialOption, pageOptions) {
        this.initialOption = initialOption;
        this.pageOptions = pageOptions;
    },

    /** Return the string representation of this model. */
    toString : function() {
        return this.initialOption + " of " + this.pageOptions;
    }
});

/** The sorting model object contains column names (id) and a sorting order. */
exxcellent.model.SortingModel = Core.extend({
    /**
             * An array of exxcellent.model.SortingColumn.
             * @type exxcellent.model.SortingColumn
             */
    columns : null,

    $construct : function(columns) {
        this.columns = columns;
    },

    getColumns: function() {
        return this.columns;
    },

    /** Return the string representation of this model. */
    toString : function() {
        return this.columns;
    }
});

/** The sorting column describes a colum by its name (id) and a sorting order. */
exxcellent.model.SortingColumn = Core.extend({
    /**
             * An column identifier, e.g. '10'
             * @type int
             */
    columnId : null,

    /**
      * An order value, e.g. 'asc' or 'desc'
      * @type String
      */
    sortOrder: null,

    $construct : function(columnId, sortOrder) {
        this.columnId = columnId;
        this.sortOrder = sortOrder;
    },

    /** Return the string representation of this model. */
    toString : function() {
        return this.columnId + " ordered " + this.sortOrder;
    }
});
        
/** JavaScript representation of RowSelection Java Class  */
exxcellent.model.RowSelection = Core.extend({  
    asr : null, // AllSelectedRowIds (Actual)
    osr : null, // OldSelectedRowsIds
    nsr : null, // NewSelectedRowsIds
    nur : null, // NewUnselectedRowsIds

    $construct : function(asr, osr, nsr, nur) {
        this.asr = asr;
        this.osr = osr;
        this.nsr = nsr;
        this.nur = nur;
    },

    toJsonStr : function() {
        return JSON.stringify( {
            rowSelection : {
                asr: this.asr, 
                osr: this.osr, 
                nsr: this.nsr, 
                nur: this.nur
                }
            } );
    },
            
    /** Return the string representation of this model. */
    toString : function() {
        return "AllSelectedRowsIds: " + this.asr + " " +
        "OldSelectedRowsIds: " + this.osr + " " +
        "NewSelectedRowsIds: " + this.nsr + " " +
        "NewUnselectedRowsIds: " + this.nur;
    }
});

/**
 * Component implementation for a FlexiGrid
 */
exxcellent.FlexiGrid = Core.extend(Echo.Component, {

    $load : function() {
        Echo.ComponentFactory.registerType("exxcellent.FlexiGrid", this);
    },

    /** Properties defined for this component. */
    $static : {
        ACTIVE_PAGE : "activePage",
        ACTIVE_PAGE_CHANGED : "activePageChanged",
                
        TABLE_ROW_SELECTION: "tableRowSelection",
        TABLE_ROW_SELECTION_CHANGED: "tableRowSelectionChanged",
                
        RESULTS_PPAGE_OPTION: "resultsPerPageOption",                
        RESULTS_PPAGE_OPTION_CHANGED: "resultsPerPageOptionChanged",
        
        HEADER_VISIBLE: "headerVisible",
        
        COLUMNS_UPDATE: "columnsUpdate",
        COL_UPDATE_TOOLTIP: "columnTooltip",
        COL_UPDATE_SORTABLE: "columnSortable",
        COL_UPDATE_HIDED: "columnHided",
        COL_UPDATE_VISIBLE: "columnVisible",
              
        HEIGHT_OFFSET: "heightOffset",                
        TABLE_COLUMN_TOGGLE: "tableColumnToggle",
        TABLE_SORTING_CHANGE: "tableSortingChange",
        TABLE_COLUMN_ARRANGED: "tableColumnArrange",
        TABLE_COLUMN_RESIZED: "tableColumnResize",
        TABLE_RESIZED: "tableResize",
        DIGITGROUP_DELIMITER: "digitGroupDelimiter",
        DECIMAL_DELIMITER: "decimalDelimiter",
        CSS : "css",
        CLIENT_SORTING: "clientSorting",
        WIDTH : "width",
        HEIGHT : "height",
        //COLUMN_WIDTH_UNIT : "columnWidthUnit",
        TITLE : "title",
        SHOWTABLE_TOGGLE: "showTableToggle",
        SORTINGMODEL : "sortingModel",
        COLUMNMODEL : "columnModel",
        SHOW_PAGER : "showPager",
        SHOW_PAGE_STAT : "showPageStatistics",
        SHOW_RESULTS_PPAGE: "showResultsPerPage",
        
        NO_ITEMS_MSG : "messageNoItems",
        PROCESS_MSG : "messageProcessing",
        HIDE_COLUMN_MSG : "messageColumnHiding",
        MIN_TABLE_MSG : "messageTableHiding",
        PAGE_STATISTICS_MSG : "messagePageStatistics",
        PAGE_WORD : "wordPage",
        OF_WORD : "wordOf",
        
        RESIZABLE : "resizable",
        STRIPED : "striped",
        MIN_COLUMN_WIDTH : "minColumnWidth",
        MIN_COLUMN_HEIGHT : "minColumnHeight",
        NO_WRAP : "noWrap",
        SELECTION_MODE : "selectionMode",
        LINE_IMG : "LINE_IMG",
        HL_COLOR : "HL_COLOR",
        HLFG_COLOR : "HLFG_COLOR",
        FHBG_IMG : "FHBG_IMG",
        DDN_IMG : "DDN_IMG",
        WBG_IMG : "WBG_IMG",
        UUP_IMG : "UUP_IMG",
        BGROUND_IMG : "BGROUND_IMG",
        DOWN_IMG : "DOWN_IMG",
        UP_IMG : "UP_IMG",
        PREV_IMG : "PREV_IMG",
        MAGNIFIER_IMG : "MAGNIFIER_IMG",
        FIRST_IMG : "FIRST_IMG",
        NEXT_IMG : "NEXT_IMG",
        LAST_IMG : "LAST_IMG",
        LOAD_IMG : "LOAD_IMG",
        LOAD_BTN_IMG : "LOAD_BTN_IMG"
    },

    componentType: "exxcellent.FlexiGrid",
    
    /** @see Echo.Component#focusable */
    focusable: true,
    
    /** @see Echo.Component#pane */
    pane: true,
    
    _selectedRows: [],
    
    /**
     * Return current selected row' ids.
     */
    getRowSelection: function() {
        return this._selectedRows;
    },
    
    /**
     * Return current selected rows' ids that should be rendered.
     */
    renderRowSelection: function() {
        return JSON.parse(this.render(exxcellent.FlexiGrid.TABLE_ROW_SELECTION));
    },
    
    /**
     * Set new rows' ids for current selection.
     * 
     * @param {Array} asr all selected rows
     * @param {Boolean} rendered optional flag indicating whether the update has already been rendered by the containing client; 
     *                  if enabled, the property update will not be sent to the update manager
     */
    setRowSelection: function(asr, rendered) {
        this._selectedRows = asr;
        this.set(exxcellent.FlexiGrid.TABLE_ROW_SELECTION, JSON.stringify(asr), rendered)
    },

    /** Perform when an select row action is triggered in the flexigrid. */
    doSelection : function(rowSelection) {
        // 	Notify table row select listeners.
        this.fireEvent({
            type : exxcellent.FlexiGrid.TABLE_ROW_SELECTION_CHANGED,
            source : this,
            data : rowSelection
        });
    },

    doChangePage : function(pageId) {
        // 	Notify table row select listeners.
        this.fireEvent({
            type : exxcellent.FlexiGrid.ACTIVE_PAGE_CHANGED,
            source : this,
            data : pageId
        });
    },

    /** Performed when a toggle column action is triggered in the flexigrid. */
    doToggleColumn : function(columnVisibility) {
        // 	Notify table column visibility toggle listeners.
        this.fireEvent({
            type : exxcellent.FlexiGrid.TABLE_COLUMN_TOGGLE,
            source : this,
            data : columnVisibility
        });
    },

    /** Performed when the sorting of columns changes. */
    doChangeSorting : function(sortingModel) {
        // 	Notify table column sorting change listeners.
        this.fireEvent({
            type : exxcellent.FlexiGrid.TABLE_SORTING_CHANGE,
            source : this,
            data : sortingModel
        });
    },

    /** Performed when the columns order changes because of user drag and drop. */
    doArrangeColumn : function(arrangeModel) {
        // 	Notify table column arrangement change listeners.
        this.fireEvent({
            type : exxcellent.FlexiGrid.TABLE_COLUMN_ARRANGED,
            source : this,
            data : arrangeModel
        });
    },

    /** Performed when the columns size changes because of user drag and drop. */
    doResizeColumn : function(columnResizeModel) {
        // 	Notify table column size change listeners.
        this.fireEvent({
            type : exxcellent.FlexiGrid.TABLE_COLUMN_RESIZED,
            source : this,
            data : columnResizeModel
        });
    },

    /** Performed when the table size changes because of user drag and drop. */
    doResizeTable : function(tableResizeModel) {
        // 	Notify table table size change listeners.
        this.fireEvent({
            type : exxcellent.FlexiGrid.TABLE_RESIZED,
            source : this,
            data : tableResizeModel
        });
    },
            
    /** Performed when the results per page changes from client */
    doChangeResultsPerPage : function(initialOption) {
        this.fireEvent({
            type : exxcellent.FlexiGrid.RESULTS_PPAGE_OPTION_CHANGED,
            source : this,
            data : initialOption
        });
    }
});
        
/**
 * Component rendering peer: FlexiGrid.
 */
exxcellent.FlexiGridSync = Core.extend(Echo.Render.ComponentSync, {

    $load: function() {
        Echo.Render.registerPeer("exxcellent.FlexiGrid", this);
    },

    /** root div as container for the flexigrid. */
    _div: null,
    _flexigrid: null,

    _table: null,
    _tableModel : null,
    _activePage : null,
    _activePageComponentIdxs : null,
    _sortingModel: null,
    _resultsPerPageOption: null,
    _renderRequired: null,
    _counterSetPropsRequired: null,
    
    _renderedChilds: null,
    
    _waitDialogHandle: null,
            
    /**
     * Describes how a component is initially built.
     */
    renderAdd: function(update, parentElement) {
        // console.log('FG: renderAdd: ' + this.component.renderId);
        
        /**
         * the root div with and table inside.
         * The table will be manipulated by the flexigrid plugin by adding surrounding
         * div elements.
         */
        this._div = document.createElement("div");
        this._div.id = this.component.renderId;
        
        /**
         * set tabindex="0", otherwise its non-focusable component.
         * @see: http://echo.nextapp.com/site/node/5979
         */
        this._div.tabIndex = "0";
        this._div.style.outlineStyle = "none";
        this._div.style.overflow = "hidden";

        /**
         * set grid dimensions
         */
        Echo.Sync.Extent.render(this.component.render(exxcellent.FlexiGrid.WIDTH, "100%"), this._div, "width", true, true);
        Echo.Sync.Extent.render(this.component.render(exxcellent.FlexiGrid.HEIGHT, "100%"), this._div, "height", false, true);
        
        this._table = document.createElement("table");
        this._div.appendChild(this._table);

        //* Register key down/up events for actions */
        Core.Web.Event.add(this._div, "keydown", Core.method(this, this._processKeyPress), false);
        Core.Web.Event.add(this._div, "keyup", Core.method(this, this._processKeyUp), false);
        Core.Web.Event.add(this._div, "blur", Core.method(this, this._processBlur), false);

        Echo.Sync.renderComponentDefaults(this.component, this._div);
        parentElement.appendChild(this._div);

        if (jQuery("#flexigridCss_"+this.component.renderId).length === 0) {
            var stylesheet = this._createStylesheet();
            jQuery("head").append("<style type=\"text/css\" id=\"flexigridCss_\"" + this.component.renderId + ">" + stylesheet + "</style>");
        }

        this._renderRequired = true;
        this._counterSetPropsRequired = true;
        
        this._renderedChilds = { };        
    },

    /**
     * Describes how the component is destroyed.
     */
    renderDispose: function(update) {
        // console.log('FG renderDispose: ' + this.component.renderId);
        // These cleanup things are CRUCICAL to avoid DRASTIC memory leaks.
        //
        // Remove out attached keylisteners from the DIV
        Core.Web.Event.removeAll(this._div);
        // Call internal flexigrid destroy.
        if (this._flexigrid) {
            // only destroy it, when it's not already null see: https://github.com/exxcellent/echolot/issues/6
            this._flexigrid.flexDestroy();
        }

        this._table = null;
        this._flexigrid = null;
        this._tableModel = null;
        this._sortingModel = null;
        this._resultsPerPageOption = null;
        this._div = null;
        
        this._renderedChilds = null;
        
        if (this._waitDialogHandle) {
            this.client.removeInputRestriction(this._waitDialogHandle);
            this._waitDialogHandle = null;
        }
    },

    /**
     * Describes how a component is updated, e.g. destroyed and build again. The
     * FlexiGrid supports partially updates only for the TableModel, SortingModel,
     * but not for any semantic model, such as ColumnModel.
     */
    renderUpdate: function(update) {
        // console.log('FG renderUpdate: ' + this.component.renderId + update.toString());
        if (this._renderRequired) {
            return true;
        }
        
        var key;
        var tmp;
        
        var dataIds = [];
        for (key in this._getActivePage().cells) { dataIds.push(key); }
        
        var headerIds = [];
        for (key in this._getColumnModel().cells) { headerIds.push(key); }
        
        var hasUpdatedProps = update.hasUpdatedProperties();
        var hasAddedChildren = update.hasAddedChildren();
        var hasRemovedChildren = update.hasRemovedChildren();
        var hasUpdatedLayoutDatas = update.hasUpdatedLayoutDataChildren();
        var hashUpdatedChildren = update.hasUpdatedChildren();
        var dataReloaded = false;
        
        if (hasUpdatedProps) {
            var updatedProps = update.getUpdatedPropertyNames();
            if (Core.Arrays.indexOf(updatedProps, exxcellent.FlexiGrid.COLUMNMODEL) >= 0) {
                /* 
                 * destroy the container and add it again
                 */ 
                var element = this._div;
                var containerElement = element.parentNode;
                Echo.Render.renderComponentDispose(update, update.parent);
                containerElement.removeChild(element);
                Echo.Render.renderComponentAdd(update, this.component, containerElement);
                return true;
            } else {
                if (Core.Arrays.indexOf(updatedProps, exxcellent.FlexiGrid.ACTIVE_PAGE) >= 0) {
                    /* 
                     * reload data =>
                     * - ignore row selection property
                     * - ignore rows per page options
                     */
                    var options = this._renderUpdateOptions();
                    this._flexigrid.flexOptions(options);
                    this._flexigrid.flexReload();
                    dataReloaded = true;
                } else if (Core.Arrays.indexOf(updatedProps, exxcellent.FlexiGrid.TABLE_ROW_SELECTION) >= 0) {
                    this._flexigrid.flexMakeSelection(this._mkSelection());
                }
            
                if (Core.Arrays.indexOf(updatedProps, exxcellent.FlexiGrid.COLUMNS_UPDATE) >= 0) {
                    var columnUpdates = update.getUpdatedProperty(exxcellent.FlexiGrid.COLUMNS_UPDATE);
                    this._flexigrid.flexUpdateColumns(this._fromJsonString(columnUpdates.newValue).columnsUpdate.updates);
                }
            }
        }
                
        
        var rendered = []; // ids of components that to be rendered
        if (dataReloaded) { 
            for (key in dataIds) { 
                rendered.push(dataIds[key]); 
            }
        }
        
        if (hashUpdatedChildren) {
            var childrenUpdate = update.getChildrenUpdate();            
            tmp = [];
            for (key in childrenUpdate) {
                var id = childrenUpdate[key].parent.renderId;
                if (dataReloaded) {
                    if (Core.Arrays.indexOf(dataIds, id) == -1) {
                        tmp.push(id);
                    }
                } else {
                    tmp.push(id);
                }
            }
            
            // delete from rendered childs
            for (key in tmp) {
                delete this._renderedChilds[tmp[key]]; 
            }
            
            // render cells
            this._flexigrid.flexRenderChilds(tmp);
            
            // store rendered cells
            for (key in tmp) {
                rendered.push(tmp[key]); 
            }
        }
        
        if (hasAddedChildren && hasRemovedChildren) {
            
            /*
             * is possible to have cells that need to be rerendered (e.g. component in cell is changed)
             */
            
            var addedRenderIds = [];
            var added = update.getAddedChildren();
            for (key in added) { addedRenderIds.push(added[key].renderId); }
            
            var removedRenderIds = [];
            var removed = update.getRemovedChildren();
            for (key in removed) { removedRenderIds.push(removed[key].renderId); }
            
            var toBeRerended = Core.Arrays.getDuplicates(addedRenderIds, removedRenderIds);
            
            tmp = [];
            if (dataReloaded) {
                for (key in toBeRerended) { if (Core.Arrays.indexOf(dataIds, toBeRerended[key]) == -1) { tmp.push(toBeRerended[key]); } } 
            } else {
                for (key in toBeRerended) { tmp.push(toBeRerended[key]); }
            }
            
            
            // delete from rendered childs
            for (key in tmp) {
                delete this._renderedChilds[tmp[key]];
            }
            
            // render cells
            this._flexigrid.flexRenderChilds(tmp);
            
            // store rendered cells
            for (key in tmp) { 
                rendered.push(tmp[key]); 
            }
        }
        
        if (hasUpdatedLayoutDatas) {
            tmp = [];
            var updatedLayoutDatas = update.getUpdatedLayoutDataChildren();
            for (key in updatedLayoutDatas) {
                if (Core.Arrays.indexOf(rendered, updatedLayoutDatas[key].renderId) == -1) {
                    tmp.push(updatedLayoutDatas[key].renderId);
                }
            }
            // render cells layout datas
            // console.log("to be render ld: " + tmp);
            this._flexigrid.flexRenderLayoutChilds(tmp);
        }
        
        return true;
    },

    /**
     * Describes how the component renders itself.
     */
    renderDisplay: function() {
        // console.log('FG: renderDisplay ' + this.component.renderId);
        if (this._renderRequired) {
            this._renderRequired = false;
            var options = this._renderOptions();
            this._flexigrid = $(this._table).flexigrid(options);
        } else {
            this._flexigrid.flexFixHeight();
        }
    },
    
    /** @see Echo.Render.ComponentSync#isChildDisplayed */
    isChildVisible: function(component) {
        return !!this._renderedChilds[component.renderId];
    },
    
    _onRenderCell: function(component) {
        this._renderedChilds[component.renderId] = component;
    },

    _renderOptions: function() {        
        var resultsPerPageOption = this._getResultsPerPageOption();
        var gridWidth = this.component.render(exxcellent.FlexiGrid.WIDTH);
        var gridHeight = this.component.render(exxcellent.FlexiGrid.HEIGHT);
        var rs = this._mkSelection();
                
        return {
            owner: this,
            ownerId: this.component.renderId,
            method:'GET',
            url: this,
            dataType: 'json',
            autoload: true,
            onPopulateCallback: this._onPopulate,
            onRenderCell: this._onRenderCell,
            onRpChange: this._onRpChange,
            onChangeSort: this._onChangeSorting,
            onToggleCol: this._onToggleColumnVisibilty,
            onSelection: this._onSelection,
            onProcessKeyUp: this._processKeyUp,
            onSuccess: this._onPopulateFinish,
            onDragCol: this._onArrangeColumn,
            onChangePage : this._onChangePage,
            onResizeCol: this._onResizeColumn,
            onResizeGrid: this._onResizeGrid,
            sortModel: this._getSortingModel(),
            colModel : this._getColumnModel().columns,
            showPageStat: this.component.render(exxcellent.FlexiGrid.SHOW_PAGE_STAT),
            headerVisible: this.component.render(exxcellent.FlexiGrid.HEADER_VISIBLE, true),
            width: gridWidth ? Echo.Sync.Extent.toCssValue(gridWidth, true, true) : 'auto',
            height: gridHeight ? Echo.Sync.Extent.toCssValue(gridHeight, false, true) : 'auto',
            showTableToggleBtn: this.component.render(exxcellent.FlexiGrid.SHOWTABLE_TOGGLE),
            title: this.component.render(exxcellent.FlexiGrid.TITLE),
            usepager: this.component.render(exxcellent.FlexiGrid.SHOW_PAGER),
            useRp: this.component.render(exxcellent.FlexiGrid.SHOW_RESULTS_PPAGE),
            rp: resultsPerPageOption.initialOption,
            rpOptions: resultsPerPageOption.pageOptions,
            asr: $.makeArray(rs.asr),
            osr: $.makeArray(rs.osr),
            nsr: $.makeArray(rs.nsr),
            nur: $.makeArray(rs.nur),
            nomsg: this.component.render(exxcellent.FlexiGrid.NO_ITEMS_MSG),
            pageWord: this.component.render(exxcellent.FlexiGrid.PAGE_WORD),
            ofWord: this.component.render(exxcellent.FlexiGrid.OF_WORD),
            procmsg: this.component.render(exxcellent.FlexiGrid.PROCESS_MSG),
            hidecolmsg: this.component.render(exxcellent.FlexiGrid.HIDE_COLUMN_MSG),
            mintablemsg: this.component.render(exxcellent.FlexiGrid.MIN_TABLE_MSG),
            pagestat: this.component.render(exxcellent.FlexiGrid.PAGE_STATISTICS_MSG),
            resizable: this.component.render(exxcellent.FlexiGrid.RESIZABLE),
            striped: this.component.render(exxcellent.FlexiGrid.STRIPED),
            minwidth: this.component.render(exxcellent.FlexiGrid.MIN_COLUMN_WIDTH),
            minheight: this.component.render(exxcellent.FlexiGrid.MIN_COLUMN_HEIGHT),
            nowrap: this.component.render(exxcellent.FlexiGrid.NO_WRAP),
            selectionMode: this.component.render(exxcellent.FlexiGrid.SELECTION_MODE),
            clientsort: this.component.render(exxcellent.FlexiGrid.CLIENT_SORTING),
            digitGroupDL: this.component.render(exxcellent.FlexiGrid.DIGITGROUP_DELIMITER),
            decimalDelimiter: this.component.render(exxcellent.FlexiGrid.DECIMAL_DELIMITER),
            heightOffset: this.component.render(exxcellent.FlexiGrid.HEIGHT_OFFSET)
        };
    },
    _renderUpdateOptions: function() {
        var resultsPerPageOption = this._getResultsPerPageOption();
        var rs = this._mkSelection();
        var gridWidth = this.component.render(exxcellent.FlexiGrid.WIDTH);
        var gridHeight = this.component.render(exxcellent.FlexiGrid.HEIGHT);

        return Object({
            showPageStat: this.component.render(exxcellent.FlexiGrid.SHOW_PAGE_STAT),
            width: (!gridWidth || gridWidth === -1) ? 'auto' : gridWidth * 1,
            height: (!gridHeight || gridHeight === -1) ? 'auto' : gridHeight * 1,
            showTableToggleBtn: this.component.render(exxcellent.FlexiGrid.SHOWTABLE_TOGGLE),
            title: this.component.render(exxcellent.FlexiGrid.TITLE),
            usepager: this.component.render(exxcellent.FlexiGrid.SHOW_PAGER),
            useRp: this.component.render(exxcellent.FlexiGrid.SHOW_RESULTS_PPAGE),
            rp: resultsPerPageOption.initialOption,
            rpOptions: resultsPerPageOption.pageOptions,
            asr: $.makeArray(rs.asr),
            osr: $.makeArray(rs.osr),
            nsr: $.makeArray(rs.nsr),
            nur: $.makeArray(rs.nur),
            nomsg: this.component.render(exxcellent.FlexiGrid.NO_ITEMS_MSG),            
            pageWord: this.component.render(exxcellent.FlexiGrid.PAGE_WORD),
            ofWord: this.component.render(exxcellent.FlexiGrid.OF_WORD),
            procmsg: this.component.render(exxcellent.FlexiGrid.PROCESS_MSG),
            hidecolmsg: this.component.render(exxcellent.FlexiGrid.HIDE_COLUMN_MSG),
            mintablemsg: this.component.render(exxcellent.FlexiGrid.MIN_TABLE_MSG),
            pagestat: this.component.render(exxcellent.FlexiGrid.PAGE_STATISTICS_MSG),
            resizable: this.component.render(exxcellent.FlexiGrid.RESIZABLE),
            striped: this.component.render(exxcellent.FlexiGrid.STRIPED),
            minwidth: this.component.render(exxcellent.FlexiGrid.MIN_COLUMN_WIDTH),
            minheight: this.component.render(exxcellent.FlexiGrid.MIN_COLUMN_HEIGHT),
            nowrap: this.component.render(exxcellent.FlexiGrid.NO_WRAP),
            selectionMode: this.component.render(exxcellent.FlexiGrid.SELECTION_MODE),
            clientsort: this.component.render(exxcellent.FlexiGrid.CLIENT_SORTING),
            digitGroupDL: this.component.render(exxcellent.FlexiGrid.DIGITGROUP_DELIMITER),
            decimalDelimiter: this.component.render(exxcellent.FlexiGrid.DECIMAL_DELIMITER),
            heightOffset: this.component.render(exxcellent.FlexiGrid.HEIGHT_OFFSET)
        });
    },

    /**
     * Creates a stylesheet with dynamically replaced images.
     * @return css String
     */
    _createStylesheet : function() {
        var css = this.component.render(exxcellent.FlexiGrid.CSS);
        if (css) {
            css = css.replace(/LINE_IMG/g, this.component.render(exxcellent.FlexiGrid.LINE_IMG));
            css = css.replace(/HL_COLOR/g, this.component.render(exxcellent.FlexiGrid.HL_COLOR));
            css = css.replace(/HLFG_COLOR/g, this.component.render(exxcellent.FlexiGrid.HLFG_COLOR));
            css = css.replace(/FG_COLOR/g, this.component.render("foreground"));
            css = css.replace(/FHBG_IMG/g, this.component.render(exxcellent.FlexiGrid.FHBG_IMG));
            css = css.replace(/DDN_IMG/g, this.component.render(exxcellent.FlexiGrid.DDN_IMG));
            css = css.replace(/WBG_IMG/g, this.component.render(exxcellent.FlexiGrid.WBG_IMG));
            css = css.replace(/UUP_IMG/g, this.component.render(exxcellent.FlexiGrid.UUP_IMG));
            css = css.replace(/BGROUND_IMG/g, this.component.render(exxcellent.FlexiGrid.BGROUND_IMG));
            css = css.replace(/DOWN_IMG/g, this.component.render(exxcellent.FlexiGrid.DOWN_IMG));
            css = css.replace(/UP_IMG/g, this.component.render(exxcellent.FlexiGrid.UP_IMG));
            css = css.replace(/PREV_IMG/g, this.component.render(exxcellent.FlexiGrid.PREV_IMG));
            css = css.replace(/MAGNIFIER_IMG/g, this.component.render(exxcellent.FlexiGrid.MAGNIFIER_IMG));
            css = css.replace(/FIRST_IMG/g, this.component.render(exxcellent.FlexiGrid.FIRST_IMG));
            css = css.replace(/NEXT_IMG/g, this.component.render(exxcellent.FlexiGrid.NEXT_IMG));
            css = css.replace(/LAST_IMG/g, this.component.render(exxcellent.FlexiGrid.LAST_IMG));
            css = css.replace(/LOAD_IMG/g, this.component.render(exxcellent.FlexiGrid.LOAD_IMG));
            css = css.replace(/LOAD_BTN_IMG/g, this.component.render(exxcellent.FlexiGrid.LOAD_BTN_IMG));
        }
        return css;
    },

    _getActivePage: function() {
        var value = this.component.render(exxcellent.FlexiGrid.ACTIVE_PAGE);
        if (!value) {
            return null;
        }
        if (value && value instanceof exxcellent.model.Page) {
            // Client-side usage: property containts page model
            this._activePage = value;
        } else if (value) {
            // Server-side usage: property contains JSON Sting containing the page
            this._activePage = this._fromJsonString(value).activePage;
        }
        
        
        // * Store child's table position (colId, rowId) ...
        // ... the key is component renderId ...
        // ... we need from this when component in table is replaced ...
        // -------------------------------------------------------------        
        if (!this._activePage.cells) {
            this._activePage.cells = [];
            for (var r = 0; r < this._activePage.rows.length; r++) {
                for (var c = 0; c < this._activePage.rows[r].cells.length; c++) {
                    var cell = this._activePage.rows[r].cells[c];
                    var apc = this.component.getComponent(cell.componentIdx);                    
                    this._activePage.cells[apc.renderId] = "CELL." + apc.renderId;
                }
            }
        }
        
        return this._activePage;
    },
            
    /**
     * Method to return the sorting model, even if the provided value was null.
     */
    _getSortingModel: function() {
        var value = this.component.render(exxcellent.FlexiGrid.SORTINGMODEL);
        if (value instanceof exxcellent.model.SortingModel) {
            this._sortingModel = value;
        } else if (value) {
            this._sortingModel = this._fromJsonString(value).sortingModel;
        } else {
            this._sortingModel = {
                columns: []
            };
        }
        return this._sortingModel;
    },

    /**
     * Method to return the results per page object.
     */
    _getResultsPerPageOption: function() {                
        var value = this.component.render(exxcellent.FlexiGrid.RESULTS_PPAGE_OPTION);
        if (value instanceof exxcellent.model.ResultsPerPageOption) {
            this._resultsPerPageOption = value;
        } else if (value) {
            this._resultsPerPageOption = this._fromJsonString(value).resultsPerPageOption;
        }
        return this._resultsPerPageOption;
    },
            
    /**
     * Creates a column model from a given tablemodel
     * @return an columnModel, e.g.
     *         columnModel[
     *                 {display: 'Name', name: 0},
     *                 {display: 'EMail', name: 1}
     *         ]
     */
    _getColumnModel: function () {
        var value = this.component.render(exxcellent.FlexiGrid.COLUMNMODEL);
        if (value instanceof exxcellent.model.ColumnModel) {
            this._columnModel = value;
        } else if (value) {
            this._columnModel = this._fromJsonString(value).columnModel;
        }
        
        // * Store child's table position (colId, rowId) ...
        // ... the key is component renderId ...
        // ... we need from this when component in table is replaced ...
        // -------------------------------------------------------------        
        if (!this._columnModel.cells) {
            this._columnModel.cells = [];
            for (var c = 0; c < this._columnModel.columns.length; c++) {
                var cell = this._columnModel.columns[c].cell;
                var cmc = this.component.getComponent(cell.componentIdx);
                this._columnModel.cells[cmc.renderId] = "CELL." + cmc.renderId;
            }
        }
        
        return this._columnModel;                
    },

    /**
     * Method to populate the grid with new data depending on the given parameter, e.g. sorting, page etc.
     * This method is also triggered by other events such as:
     * - refresh button
     * - _onChangeSorting
     * - _onChangePage
     * - selecting a rows per page option
     */
    _onPopulate: function (param) {
        // if there is already a waitHandle do not create another one so we just create a new if it is null...
        if (this._waitDialogHandle == null) {                    
            this._waitDialogHandle = this.client.createInputRestriction();
        }
        return this._getActivePage();
    },
    
    _onPopulateFinish: function(counterIndexes) {
        this._setCounterProps(counterIndexes);
        this._setCounterNumbers(counterIndexes);        
        this.client.removeInputRestriction(this._waitDialogHandle);
        this._waitDialogHandle = null;
    },
    
    _onChangePage: function(newPageNo) {        
        for (var key in this._getActivePage().cells) {
            delete this._renderedChilds[key];
        }
        // notify listeners
        this.component.doChangePage(newPageNo);
    },
    
        /**
     * Method to process the event of changing the sorting.
     * {"sortingModel": {
     *      "columns": [
     *        {
     *          "columnId": 5,
     *          "sortOrder": 'desc'
     *        },
     *        {
     *          "columnId": 10,
     *            "sortOrder": 'asc'
     *       }
     *    ]
     *   }}
     * @param {} sortingModel containing columns with their name column name and sorting
     */
    _onChangeSorting: function (sortingModel) {
        this._sortingModel = sortingModel;

        var columnsArray = [];
        if (sortingModel && sortingModel.columns) {
            for (var i = 0; i < sortingModel.columns.length; i++) {
                var column = sortingModel.columns[i];
                columnsArray.push({
                    columnId: column.columnId * 1, // convert it to a number
                    sortOrder: column.sortOrder
                }
                );
            }
        }
        var sortingObj = {
            sortingModel : {
                columns: {
                    sortingColumn: columnsArray
                }
            }
        };
                
        var jsonMessage = this._toJsonString(sortingObj);
        if (this.component.render(exxcellent.FlexiGrid.CLIENT_SORTING)) {
            // client-sorting will no longer work - so this will do just nothing :-)
            this._sortClientSide();
        }
        
        if (this._resultsPerPageOption.initialOption == -1) {
          this._setCounterNumbers(this._flexigrid.flexGetCounterIndexes());
        }
        this.component.doChangeSorting(jsonMessage);
    },

    /**
     * Method responsible to sort the current tableModel using the current sortingModel and current columnModel.
     * The sorting is using the flexMultiSort algorithm and starts refreshing the table model after to redraw
     * the table content. Actually we only set a new table model to refresh the table with the new sortModel.
     */
    _sortClientSide: function () {
        return;
        // After refactoring to lazy loading this does not work any more...
        // if you need this feel free to implement a logic to sort a lazy-loaded model on client-side

        /*
         var newTableModel = new exxcellent.model.TableModel(this._tableModel.pages);
         this.component.set(exxcellent.FlexiGrid.TABLEMODEL, newTableModel);
         */
    },
    
    _setCounterProps: function(indexes) {
        if (this._counterSetPropsRequired && indexes.length != 0) {
            var counterComponent = this.component.getComponent(indexes[0]);
            var counterLocalStyleData = counterComponent.getLocalStyleData();
            for (var i = 1; i < indexes.length; i++) {
                var component = this.component.getComponent(indexes[i]);
                for (var propName in counterLocalStyleData) {
                    if (propName !== "text" && propName !== "layoutData") {
                        component.set(propName, counterLocalStyleData[propName], false);
                    }
                }
            }
            this._counterSetPropsRequired = false;
        }
    },
    
    _setCounterNumbers: function(indexes) {
        if (indexes.length != 0 && this._sortingModel && this._sortingModel.columns.length != 0) {
            var startNo = (this._activePage.page - 1) * this._resultsPerPageOption.initialOption + 1;
            for (var i = 1; i < indexes.length; i++) {
                this.component.getComponent(indexes[i]).set("text", startNo++, false);
            }
        }
    },
    
    /**
     * Method to process the event on selecting a row / rows.
     * 
     * Used JSON format :
     * {"rowSelection": {
     *      "allSelected": [1, 3, 4, 5],
     *      "oldSelectedRowsIds": [1, 2, 3],
     *      "newSelectedRowsIds" [4, 5]
     *      "newUnselectedRowsIds": [2]
     * }}
     * 
     * @param {rowSelectionEventData}
     * @param {newRowSelection}
     */
    _onSelection: function (asr, osr, nsr, nur) {
        /** set all selected rows */
        this.component.setRowSelection(asr, true);
        
        /** Store new row selection */
        var newSelection = new exxcellent.model.RowSelection(
            $.makeArray(asr), 
            $.makeArray(osr), 
            $.makeArray(nsr), 
            $.makeArray(nur));

        // ** Fire event for new client selection */
        if(nsr.length != 0 || nur.length != 0) {
            this.component.doSelection(newSelection.toJsonStr());
        }
    },
           
    /**
     * This method is started when the recalculation table.
     */
    _mkSelection: function() {
        /* selected rows' ids that should be rendered  */
        var nasr = this.component.renderRowSelection();
                
        /* all (actual) selected rows' ids */
        var asr = $.makeArray(this.component.getRowSelection());

        /* calculate new selected rows' ids */
        var nsr = $.grep(nasr, function(value) {
            return $.inArray(value, asr) == -1;
        });

        /* calculate new nonselected rows' ids */
        var nur = $.grep(asr, function(value) {
            return $.inArray(value, nasr) == -1;
        });

        return new exxcellent.model.RowSelection(nasr, asr, nsr, nur);
    },
            
    /**
     * Method to process the event ot changing the ResultsPerPageOption.
     */
    _onRpChange: function(initialOption) {
        for (var key in this._getActivePage().cells) {
            delete this._renderedChilds[key];
        }
        this.component.doChangeResultsPerPage(initialOption);
    },

    /**
     * Method to process the event on changing the visibility of a column.
     * 
     * Used JSON format :
     * {"columnVisibility": {
     *      "columnId": 0,
     *      "visible": true
     * }}
     * @param {Number} columnId the column identifier
     * @param {Boolean} visible the state of visibility, either true or false
     */
    _onToggleColumnVisibilty: function (columnId, visible) {
        var toggleObj = {
            columnVisibility : {
                columnId: columnId * 1, // convert it to a number
                visible: !!visible // convert it to boolean
            }
        };
        var jsonMessage = this._toJsonString(toggleObj);
        this.component.doToggleColumn(jsonMessage);
    },
            
    /**
     * Method to process the event on arranging a column,
     * i.e. dragging and dropping a column.
     * Used JSON format :
     * {"columnArrange": {
     *          sourceColumnId: 20,
     *          targetColumnId: 15,
     *      }
     * }
     * @param {String} sourceColumnId the column identifier of the dragged column
     * @param {String} targetColumnId the column identifier of the dropped column
     */
    _onArrangeColumn: function (sourceColumnId, targetColumnId) {
        var columnArrangeObj = {
            columnArrange : {
                sourceColumnId: sourceColumnId, // convert it to a number
                targetColumnId: targetColumnId // convert it to a number
            }
        };
        var jsonMessage = this._toJsonString(columnArrangeObj);
        this.component.doArrangeColumn(jsonMessage);
        return false;
    },

    /**
     * Method to process the event on resizing a column.
     * Used JSON format :
     * {"columnResize": {
     *          columnId: 20,
     *          width: 250
     *      }
     * }
     * @param {String} columnId the column identifier that is resizing
     * @param {Number} newWidth the new width of the resized column
     */
    _onResizeColumn: function (columnId, newWidth) {
        var columnResizeObj = {
            columnResize : {
                columnId: columnId * 1, // convert it to a number
                width: newWidth * 1
            }
        };
        var jsonMessage = this._toJsonString(columnResizeObj);
        this.component.doResizeColumn(jsonMessage);
        return false;
    },

    /**
     * Method to process the event on resizing the table.
     * Used JSON format :
     * {"tableResize": {
     *          width: 200,
     *          height: 400
     *      }
     * }
     * @param {Number} width the new grid width
     * @param {Number} height the new grid height
     */
    _onResizeGrid: function (width, height) {
        var tableResizeObj = {
            tableResize : {
                width: width * 1, // convert it to a number
                height: height * 1 // convert it to a number
            }
        };
        var jsonMessage = this._toJsonString(tableResizeObj);
        this.component.doResizeTable(jsonMessage);
        return false;
    },

    /** Processes a key press event. */
    _processKeyPress: function(e) {
        if (!this.client || !this.client.verifyInput(this.component)) {
            return true;
        }
        if (e.keyCode === 13 || e.keyCode === 40 || e.keyCode === 38
            || e.keyCode === 37 || e.keyCode === 39) {
            this._flexigrid.flexRemoteControl(e);
            return true;
        } else {
            return true;
        }
    },
            
    /** Processes a key up event. */
    _processKeyUp: function(e) {
        if (!this.client || !this.client.verifyInput(this.component)) {
            return true;
        }
        if(e.keyCode == 17) {
            this._flexigrid.flexCheckSelection();
        }                
        return true;
    },

    /** @see Echo.Render.ComponentSync#getFocusFlags */
    getFocusFlags: function() {
        // we doesn't allow any of the keys: arrow up,down,left,right to change the focus
        return false;
    },

    /** Processes a focus blur event. */
    _processBlur: function(e) {
        this._renderFocusStyle(false);
    },

    /** @see Echo.Render.ComponentSync#renderFocus */
    renderFocus: function() {
        this._renderFocusStyle(true);
        Core.Web.DOM.focusElement(this._div);
    },

    /**
     * Enables/disables focused appearance of flexigrid.
     *
     * @param {Boolean} focusState the new focus state.
     */
    _renderFocusStyle: function(focusState) {
        // Render default focus aesthetic.                
        this._flexigrid.flexFocus(focusState);
    },
            
    /**
     * Method to parse a JSON string into an object.
     * @see "http://www.json.org/js.html"
     * @param {} jsonStr the string to be transformed into an object
     * @return {} the object
     */
    _fromJsonString : function(jsonStr) {
        return JSON.parse(jsonStr);
    },

    /**
     * Method to convert an object to a json string.
     * @see "http://www.json.org/js.html"
     * @param {} object the object to be transformed into string
     * @return {} the json string
     */
    _toJsonString : function(object) {
        return JSON.stringify(object);
    }
});