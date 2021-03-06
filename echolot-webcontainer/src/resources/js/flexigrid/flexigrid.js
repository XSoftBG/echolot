/*
 * Flexigrid for jQuery - New Wave Grid
 *
 * Copyright (c) 2008 Paulo P. Marinas (webplicity.net/flexigrid)
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * $Date: 2008-07-14 00:09:43 +0800 (Tue, 14 Jul 2008) $
 */

(function($){

    $.addFlex = function(t,p)
    {

        if (t.grid) return false; //return if already exist

        // apply default properties
        p = $.extend({
            height: 'auto', //auto height
            width: 'auto', //auto width
            striped: true, //apply odd even stripes
            novstripe: false,
            minwidth: 30, //min width of columns
            minheight: 80, //min height of columns
            resizable: false, //resizable table
            url: false, //ajax url
            method: 'POST', // data sending method
            dataType: 'xml', // type of data loaded
            errormsg: 'Connection Error',
            usepager: false, //
            nowrap: true, //
            page: 1, //current page
            total: 1, //total pages
            useRp: true, //use the results per page select box
            rp: 15, // results per page
            rpOptions: [10,15,20,25,40],
            title: false,
            colModel: false, // the column model.
            pagestat: 'Displaying {from} to {to} of {total} items',
            procmsg: 'Processing, please wait ...',
            hidecolmsg: 'Hide/Show Columns',
            mintablemsg: 'Minimize/Maximize Table',
            query: '',
            qtype: '',
            nomsg: 'No items',
            pageWord: 'Page',
            ofWord: 'of',
            minColToggle: 1, //minimum allowed column to be hidden
            showToggleBtn: true, //show or hide column toggle popup
            showPageStat: true,// show or hide the page statistics in the footer
            hideOnSubmit: true,
            autoload: true,
            blockOpacity: 0.5,
            onToggleCol: false,// using custom change visibility of column function
            onChangeSort: false, // using custom change sort function
            onSuccess: false, // using custom validate after addData function
            onChangePage: false, // using custom change page function
            onSubmit: false, // using a custom populate function
            onPopulateCallback: false, // using a custom populate callback function with parsed params
            onDragCol: false, // using a custom on column dragdrop callback function
            onResizeCol: false, // using a custom on column resizing callback function
            onResizeGrid: false, // using a custom on grid resizing callback function
            owner: false, // the owner of the flexigrid component is used as 'this' for all callbacks
            debug: false, // if true, you may see dub messages in the console (e.g. firebug)
            sortorder: 'asc', // the initial sorting method for the pre-sorted column
            sortModel: {
                columns: []
            }, // the sorting model contains columns with sort information
            clientsort: true, // if true, the table is sorted on every processing request client side.
            digitGroupDL: '.',// the delimiter to separate a group of digits (this is extracted during the search)
            decimalDelimiter: ',', // the delimiter to separate Decimal-Values
            heightOffset: 100, // the offset used to correctly auto height the flexigrid table. Just play with the value.
            searchitems: false
        }, p);


        $(t)
        .show() //show if hidden
        .attr({
            cellPadding: 0, 
            cellSpacing: 0, 
            border: 0
        })  //remove padding and spacing
        .removeAttr('width') //remove width properties
        ;

        // -----------------------------------------------------------------------------------------------------------

        //create grid class
        var g = {      
            hset : {},
           
            rePosDrag: function () {
                var cdleft = 0 - this.hDiv.scrollLeft;
                if (this.hDiv.scrollLeft > 0) cdleft -= Math.floor(p.cgwidth/2);
                g.cDrag.style.top = (g.hDiv.offsetTop + 1) + 'px';
                var cdpad = this.cdpad;
                // Select all possible drags and hide it. The selection is stored to a variable because
                // we will reuse it later while iterate through the header cells.
                var qdrags = $('div', g.cDrag);                
                qdrags.hide();
                // We do not use the regular each method of jQuery because we do need the index of the
                // header cell for other operation with the drags. (each is usually also slower than for)                
                var qheaders = $('thead tr:first th:visible', this.hTable);
                for (var n = 0; n < qheaders.length; n++) {                    
                    //var cdpos = parseInt($('div', qheaders[n]).width());
                    var cdpos = parseInt($(qheaders[n]).width());
                    if (cdleft == 0) {
                        cdleft -= Math.floor(p.cgwidth / 2);
                    }
                    cdpos = cdpos + cdleft + cdpad;
                    // Select the drag which is equals to the index of the current header cell.
                    qdrags[n].style.left = cdpos + 'px';
                    cdleft = cdpos;
                }
                qdrags.show();
            },
            
            /**
             * Method used to fix the height of the column drag-lines and the
             * column visibility menu height (nDiv).
             */
            fixHeight: function (newH) {
                // newH = false;
                if (!newH) {
                    newH = $(g.bDiv).height();
                }
                   
                var hdHeight = $(this.hDiv).height();
                
                for (var c = 0; c < this.cDrag.childNodes.length; ++c) {
                    this.cDrag.childNodes[c].style.height = (newH + hdHeight) + 'px';
                }
               
                g.block.style.height = newH + 'px';
                g.block.style.marginBottom = (newH * -1) + 'px';

                var hrH = g.bDiv.offsetTop + newH;
                if (p.height != 'auto' && p.resizable) { 
                    hrH = g.vDiv.offsetTop;
                }
                g.rDiv.style.height = hrH + 'px';
            },
            dragStart: function (dragtype,e,obj) { //default drag function start

                if (dragtype == 'colresize') //column resize
                {
                    $(g.nDiv).hide();
                    $(g.nBtn).hide();
                    var n = $('div',this.cDrag).index(obj);
                    //var ow = $('th:visible div:eq('+n+')',this.hDiv).width();                    
                    var ow = $('th:visible:eq('+n+')',this.hDiv).width();                    
                    $(obj).addClass('dragging').siblings().hide();
                    $(obj).prev().addClass('dragging').show();

                    this.colresize = $.extend(true, { }, {
                        startX: e.pageX, 
                        ol: parseInt(obj.style.left), 
                        ow: ow, 
                        n : n
                    });
                    $('body').css('cursor','col-resize');
                }
                else if (dragtype == 'vresize') //table resize
                {
                    var hgo = false;
                    $('body').css('cursor','row-resize');
                    if (obj)
                    {
                        hgo = true;
                        $('body').css('cursor','col-resize');
                    }
                    this.vresize = {
                        h: p.height, 
                        sy: e.pageY, 
                        w: p.width, 
                        sx: e.pageX, 
                        hgo: hgo
                    };

                }

                else if (dragtype == 'colMove') //column header drag
                {
                    $(g.nDiv).hide();
                    $(g.nBtn).hide();
                    this.hset = $(this.hDiv).offset();
                    this.hset.right = this.hset.left + $('table',this.hDiv).width();
                    this.hset.bottom = this.hset.top + $('table',this.hDiv).height();
                    this.dcol = obj;
                    this.dcoln = $('th',this.hDiv).index(obj);

                    this.colCopy = document.createElement("div");
                    this.colCopy.className = "colCopy";
                    this.colCopy.innerHTML = obj.innerHTML;
                    if ($.browser.msie)
                    {
                        this.colCopy.className = "colCopy ie";
                    }


                    $(this.colCopy).css({
                        position:'absolute',
                        "float":'left',
                        display:'none', 
                        textAlign: obj.align
                    });

                    $('body').append(this.colCopy);
                    $(this.cDrag).hide();

                }

                // ECHO3: Die entsprechende Eigeneschaft wird wohl nicht  inherited, weil vermutlich
                // umliegende Echo-Komponenten diese woanders definierem:
                // Daher auf dem flexigrid-DIV selber arbeiten statt dem globalen BODY-Tag
                //$('body').noSelect();
                $(g.gDiv).noSelect();

            },
            
            dragMove: function (e) {

                if (this.colresize) //column resize
                {
                    var n = this.colresize.n;
                    var diff = e.pageX-this.colresize.startX;
                    var nleft = this.colresize.ol + diff;
                    var nw = this.colresize.ow + diff;
                    if (nw > p.minwidth)
                    {
                        $('div:eq('+n+')',this.cDrag).css('left',nleft);
                        this.colresize.nw = nw;
                    }
                }
                else if (this.vresize) //table resize
                {
                    var v = this.vresize;
                    var y = e.pageY;
                    var diff = y-v.sy;

                    if (!p.defwidth) {
                        p.defwidth = p.width;
                    }

                    if (p.width != 'auto' && !p.nohresize && v.hgo)
                    {
                        var x = e.pageX;
                        var xdiff = x - v.sx;
                        var newW = v.w + xdiff;
                        if (newW > p.defwidth)
                        {
                            this.gDiv.style.width = newW + 'px';
                            p.width = newW;
                        }
                    }

                    var newH = v.h + diff;
                    if ((newH > p.minheight || p.height < p.minheight) && !v.hgo)
                    {
                        this.bDiv.style.height = newH + 'px';
                        p.height = newH;
                        this.fixHeight(newH);
                    }
                    v = null;
                }
                else if (this.colCopy) {
                    $(this.dcol).addClass('thMove').removeClass('thOver');
                    if (e.pageX > this.hset.right || e.pageX < this.hset.left
                        || e.pageY > this.hset.bottom || e.pageY < this.hset.top)
                        {
                        //this.dragEnd();
                        $('body').css('cursor','move');
                    } else {
                        $('body').css('cursor', 'pointer');
                    }
                    $(this.colCopy).css({
                        top:e.pageY + 10,
                        left:e.pageX + 20, 
                        display: 'block'
                    });
                }

            },
            
            dragEnd: function () {
                if (this.colresize)
                {
                    g.setBusy(true);
                    var n = this.colresize.n;// index of column
                    var nw = this.colresize.nw;// new width of column

                    var columnSel = $('th:visible:eq('+n+')',this.hDiv);
                    columnSel.css('width', nw);
                    columnSel.data('isUserSized', true);
                    
                    var rows = $('#' + $.fn.fixID(p.ownerId + '.DATA')).children('tr');
                    var rc = 0;
                    var runnable = Core.Web.Scheduler.run(Core.method(this, function() {
                        var rowsPerBatch = 50;
                        while(rowsPerBatch-- > 0 && rc < rows.length) {
                            var qtd = $(rows[rc++]).children('td:visible:eq(' + n + ')');
                            qtd.css('width', nw);
                            
                            var qdiv = $(':first-child', qtd[0]);
                            var title = qtd.attr('title');
                            if (nw < qdiv.width() && !title) {
                                var component = p.owner.component.getComponent(/(\d*)$/.exec(qtd.attr('id'))[0] * 1);
                                qtd.attr('title', component.get('text'));
                            } else if (nw >= qdiv.width() && title) {
                                qtd.removeAttr('title');
                            }
                        }                         
                        if (rc == rows.length) {
                            Core.Web.Scheduler.remove(runnable);
                            g.setBusy(false);
                        }
                    }), 1, true);
                    
                    // synchronize the header and the body while scrolling
                    this.hDiv.scrollLeft = this.bDiv.scrollLeft;

                    $('div:eq('+n+')',this.cDrag).siblings().show();
                    $('.dragging',this.cDrag).removeClass('dragging');
                    this.rePosDrag();
                    this.fixHeight();
                    this.colresize = false;

                    if (p.onResizeCol && p.colModel) {
                        var columnId = p.colModel[n].id;
                        p.onResizeCol.call(p.owner, columnId, nw);
                    }
                }
                else if (this.vresize)
                {
                    this.vresize = false;
                    if (p.onResizeGrid) {
                        p.onResizeGrid.call(p.owner, p.width, p.height);
                    }
                }
                else if (this.colCopy)
                {
                    $(this.colCopy).remove();
                    if (this.dcolt != null)
                    {
                        if (this.dcoln>this.dcolt) {
                            $('th:eq('+this.dcolt+')', this.hDiv).before(this.dcol);
                        }                            
                        else {
                            $('th:eq('+this.dcolt+')', this.hDiv).after(this.dcol);
                        }
                          
                        this.switchCol(this.dcoln, this.dcolt);
                        
                        $(this.cdropleft).remove();
                        $(this.cdropright).remove();
                        this.rePosDrag();

                        if (p.onDragCol && p.colModel)
                            var sourceColumnId = p.colModel[this.dcoln].id;
                        var targetColumnId = p.colModel[this.dcolt].id;
                        p.onDragCol.call(p.owner, sourceColumnId, targetColumnId);

                    }

                    this.dcol = null;
                    this.hset = null;
                    this.dcoln = null;
                    this.dcolt = null;
                    this.colCopy = null;

                    $('.thMove',this.hDiv).removeClass('thMove');
                    $(this.cDrag).show();
                }
                $('body').css('cursor','default');
                // $('body').noSelect(false);
                // ECHO3 : siehe comment in dragStart
                $(g.gDiv).noSelect(false);
            },
            
            toggleCol: function(cid,visible) {
                var ncol = $("th[axis='col"+cid+"']",this.hDiv)[0];
                var n = $('thead th',g.hDiv).index(ncol);
                var cb = $('input[value='+cid+']',g.nDiv)[0];

                if (visible == null) {
                    visible = ncol.hide;
                }

                if ($('input:checked', g.nDiv).length < p.minColToggle && !visible) {
                    return false;
                }

                if (visible) {
                    ncol.hide = false;
                    $(ncol).show();
                    cb.checked = true;
                } else {
                    ncol.hide = true;
                    $(ncol).hide();
                    cb.checked = false;
                }

                var qbody = $('#' + $.fn.fixID(p.ownerId + '.DATA'));
                var rows = qbody.children("tr");
                rows.each(function() {
                    $(this).children('td:eq(' + n + ')').toggle();
                });

                this.rePosDrag();

                /*
                 * ECHO3 we need the owner of the object as 'this'.
                 * Event if column visibility is changed.
                 */
                if (p.onToggleCol && p.colModel) {
                    var columnId = p.colModel[cid].id;
                    p.onToggleCol.call(p.owner, columnId, visible);
                }
                
                return visible;
            },

            // After columns are dragged and dropped the data has to be adjusted.
            switchCol: function(cdrag,cdrop) { //switch columns

                var qbody = $('#' + $.fn.fixID(p.ownerId + '.DATA'));
                var rows = qbody.children("tr");
                rows.each(function() {
                    var qthis = $(this);
                    var dropTD = qthis.children('td:eq(' + cdrop + ')');
                    var dragTD = qthis.children('td:eq(' + cdrag + ')');
                    
                    if (cdrag > cdrop) {
                        dropTD.before(dragTD);
                    } else {
                        dropTD.after(dragTD);
                    }
                });

                //switch order in nDiv
                if (cdrag > cdrop)
                    $('tr:eq('+cdrop+')', this.nDiv).before($('tr:eq('+cdrag+')', this.nDiv));
                else
                    $('tr:eq('+cdrop+')', this.nDiv).after($('tr:eq('+cdrag+')', this.nDiv));

                if ($.browser.msie && $.browser.version < 7.0){
                    $('tr:eq('+cdrop+') input', this.nDiv)[0].attr('checked', true);
                }

                this.hDiv.scrollLeft = this.bDiv.scrollLeft;
            },

            // the action triggered by the scroll event in the body div (bDiv)
            scroll: function() {
                this.hDiv.scrollLeft = this.bDiv.scrollLeft;
                this.rePosDrag();
            },

            addData: function (data) {
                this.data = data;
                if (!data) {
                    // There is no data after loading. Interrupt the loading here,
                    // set busy to to false and display an error message.
                    finalizeRendering();
                    return false;
                }

                if (p.dataType != 'xml') {
                    p.total = data.total;
                    p.page = data.page;                    
                } else {
                    p.total = +$('rows total',data).text();
                    p.page = +$('rows page',data).text();
                }
                
                p.pages = p.rp == -1 ? 1 : Math.ceil(p.total/p.rp);

                if (p.total == 0) {
                    $('tr, a, td, div',t).unbind();
                    $(t).empty();
                    p.pages = 1;
                    p.page = 1;
                    finalizeRendering();
                    $('.pPageStat', this.pDiv).html(p.nomsg);                    
                    return false;
                }
                
                // if sorting model exists anr resultsperpage == -1 then sort data ...
                if (p.rp == -1 && this.data && p.clientsort && p.sortModel && p.sortModel.columns.length > 0) {                
                    this.multiSort(
                        p.sortModel, 
                        new exxcellent.model.ColumnModel(p.colModel), 
                        new exxcellent.model.TableModel(new Array(this.data))
                    );
                }
                                
                // set the heights before rendering finished
                if (p.height == 'auto') {
                    var globalDiv = $(g.gDiv);
                  
                  /*
                	 * can not be used... its more complicated.
                	 * the idea was to measure all prev siblings (divs)
                	 *
                      var componentHeight = globalDiv.attr('offsetHeight');
                      globalDiv.prevAll().each(function () {
                          componentHeight += $(this).attr('offsetHeight');
                      });
                   */
                  
                    var bHeight = globalDiv.offsetParent().attr('offsetHeight') - p.heightOffset;
                    // adjust the flexigrid body (table) height
                    $(g.bDiv).css({
                        height: bHeight + 'px'
                    });
                }
                
                // We will need the header cell at this point more times.
                // So we do better to store it not for further usages.
                var headers = $('thead tr:first th', g.hDiv);


               if (!this.dataDOM) {                    
                    var tbody = document.createElement('tbody');                    
                    var protoTr = document.createElement('tr');
                    var protoTd = document.createElement('td');
                    protoTd.appendChild(document.createElement('div'));
                    if (!p.nowrap) {
                        protoTd.style.whiteSpace = 'normal';
                    }
                    for (var c = 0; c < headers.length; c++) {
                        protoTr.appendChild(protoTd.cloneNode(true));
                    }
                    for (var r = 0; r < data.rows.length; r++) {
                        tbody.appendChild(protoTr.cloneNode(true));
                    }
                    this.dataDOM = tbody;
                } else {                    
                    var rowsDiff = data.rows.length - this.dataDOM.childNodes.length;
                    if (rowsDiff > 0) {
                        while (rowsDiff-- > 0) {
                            this.dataDOM.appendChild(this.dataDOM.childNodes[0].cloneNode(true));
                        }
                    } else if (rowsDiff < 0) {
                        while (rowsDiff++ < 0) {
                            this.dataDOM.removeChild(this.dataDOM.childNodes[0]);
                        }
                    }
                }
                
                // Clone prototype data body ...
                var body = this.dataDOM.cloneNode(true);
                body.id = p.ownerId + '.DATA';
                var qtbody = $(body);

                /**
                 * This method is used to finalize the rendering of the data to the body if the grid list.
                 * @return (void)
                 */
                function finalizeRendering() {
                    var qt = $(t);
                    // Clean the current body complete and add the new generated body.
                    $('tr', qt).unbind();
                    qt.empty().append(qtbody);

                    if (data.rows.length != 0) {
                        g.rePosDrag();
                        g.fixHeight();
                    }

                    // This is paranoid but set the variables back to null. It is better for debugging.
                    body = null;
                    qtbody = null;
                    data = null;
                    
                    // Call the onSuccess hook (if present).
                    if (p.onSuccess) {
                        p.onSuccess.call(p.owner, g.getCounterIndexes());
                    }
                    
                    // Deactivate the busy mode.
                    g.setBusy(false);
                    
                    if (g.lazyFocus) {
                        g.lazyFocus.call(this, true);
                    }
                    
                    // build pager.
                    g.buildpager();
                    
                    // COMMENT-ECHO3: Notify for selection */
                    g.notifyForSelection();
                }

                // What is going on here? Because of many rows we have to render, we do not
                // iterate with a regular foreach method. We make a pseudo asynchron process with
                // the setTimeout method. We do better to do this because in other way we will
                // force a lagging of the whole browser. In the worst case the user will get a
                // dialog box of an "endless looping javaScript".

                if (p.dataType == 'json') {                  
                    // Prepare the looping parameters.
                    var ji = 0;
                    var row = null;
                    var cell = null;
                    var tr = null;
                    var qth = null;
                    
                    /**                     
                     * Start the pseudo asynchron iteration.
                     * Processing the JSON input may take some time esp. on crappy MSIEs.
                     * Using this timeout mechanism we avoid "unresponsible script" warn dialogs.
                     * 
                     * Processes a data row in the JSON data stream
                     */
                    // var start = new Date();
                    var runnable = Core.Web.Scheduler.run(Core.method(this, function() {
                        
                        var clickHandler = function(e) {
                            var obj = (e.target || e.srcElement);
                            if (obj.href || obj.type) {
                                return true;
                            }
                            g.processSelection($(this), e);
                            e.stopPropagation();
                        };                        
                      
                        var rowsPerBatch = 20;
                        while (data && data.rows.length > ji && data.rows[ji] && rowsPerBatch > 0) {
                            row = data.rows[ji];                            
                            tr = body.childNodes[ji];
                            
                            if (row.id !== null) {
                                tr.id = p.ownerId + '.ROW.' + row.id;
                            }                            
                            if (ji % 2 && p.striped) {
                                tr.className += ' erow';
                            }
                            if ($.inArray(row.id, p.asr) != -1) {
                                tr.className += ' trSelected';
                            }
                            
                            tr.onclick = clickHandler;
                            
                            /*
                             * if ($.browser.msie && $.browser.version < 7.0) {
                             *    qtr.hover(function () {$(this).addClass('trOver');}, function () {$(this).removeClass('trOver');});
                             * }
                             */
                           
                            // Add each cell for each header column (rowDataIndex)
                            for (var ci = 0; ci < headers.length; ci++) {
                                qth = $(headers[ci]);
                                cell = body.childNodes[ji].childNodes[ci];
                                
                                if (qth.hasClass('sorted')) {
                                    cell.className += ' sorted';
                                }
                                if (headers[ci].hide) {
                                    cell.style.display = 'none';
                                }
                                
                                g.renderCell(row.cells[qth.data('rowDataIndex')].componentIdx, cell.childNodes[0], cell, qth);
                            }
                            
                            // Prepare the next step.
                            ji++;
                            rowsPerBatch--;
                        }
                        
                        if (ji == data.rows.length) {
                            Core.Web.Scheduler.remove(runnable);                            
                            finalizeRendering();
                            // console.log('render time: ' + (new Date() - start) + 'ms');
                        }
                    }), 1, true);
                } else {
                    throw new Error('DataType "' + p.dataType + '" could not be handled.');
                }
            },

            /**
             * On change sort.
             */
            changeSort: function(th, multiSelect) {
                if (this.loading) return true;
               
                $(g.nDiv).hide();
                $(g.nBtn).hide();

                if (!multiSelect) {
                    // remove all sorted columns from the model
                    p.sortModel.columns = [];
                    // remove all classes from the other header columns.
                    var thDiv = $('div', th);
                    $('thead tr:first th div', this.hDiv).not(thDiv).removeClass('sdesc').removeClass('sasc');
                    $(th).siblings().removeClass('sorted');
                }

                // set or add the sorting order in the model
                var thdiv = $('div', th);
                var isSorted = $(th).hasClass('sorted');

                var sortColumn = new Object();
                var abbrSelector = $(th).attr('abbr');
                var columnIdx = $(th, th.parentNode).index();
                // if already sorted column, toggle sorting
                if (isSorted){
                    var no = '';
                    if (p.sortModel.columns.length > 0) {
                        for (var idx = 0; idx < p.sortModel.columns.length; idx++) {
                            var column = p.sortModel.columns[idx];
                            if (column.columnId == abbrSelector) {
                                column.sortOrder = ($(thdiv).hasClass('sasc')? 'asc':'desc');
                                sortColumn = column;
                                break;
                            }
                        }
                    }else {
                        sortColumn = new Object({
                            columnId: abbrSelector,
                            columnIdx: columnIdx,
                            sortOrder: ($(thdiv).hasClass('sasc')? 'asc':'desc')
                        });
                        p.sortModel.columns.push(sortColumn);
                    }
                }
                // not sorted column, activate default sorting
                else if (!isSorted) {
                    $(th).addClass('sorted');
                    thdiv.addClass('s' + p.sortorder);
                    sortColumn = new Object({
                        columnId: abbrSelector,
                        columnIdx: columnIdx,
                        sortOrder: p.sortorder
                    });
                    p.sortModel.columns.push(sortColumn);
                }
                
                if (p.rp != -1) {
                  if (p.onChangeSort) {
                      p.onChangeSort.call(p.owner, p.sortModel);
                  }
                } else {
                    // we are sorting, so visualize the processing
                    this.setBusy(true);
                    
                    var clonedData = JSON.parse(JSON.stringify(this.data));                
                    this.multiSort(p.sortModel, new exxcellent.model.ColumnModel(p.colModel), new exxcellent.model.TableModel(new Array(clonedData)));
                    this.data = clonedData;

                    var sortedColumns = [];
                    $(p.sortModel.columns).each(function(i, col) {
                        sortedColumns.push(col.columnIdx);
                    });

                    var addRowChildProp = function(i, child) {
                        if (Core.Arrays.indexOf(sortedColumns, i) >= 0) {
                            $(child).addClass('sorted');
                        } else {
                            $(child).removeClass('sorted');
                        }
                    } 

                    var qData = $('#' + $.fn.fixID(p.ownerId + '.DATA'));
                    var rc = 0;
                    var runnable = Core.Web.Scheduler.run(Core.method(this, function() {
                        var rowsPerBatch = 50;
                        while(rowsPerBatch > 0 && clonedData.rows.length > rc) {
                            var qrow = $('#' + $.fn.fixID(p.ownerId + '.ROW.' + clonedData.rows[rc].id));
                            qrow.children().each(addRowChildProp);
                            if (rc % 2 == 0 && p.striped) {
                                qrow.removeClass('erow');
                            } else {                            
                                qrow.addClass('erow');
                            }
                            qData.append(qrow);
                            rowsPerBatch--;
                            rc++;
                        }

                        if (rc == clonedData.rows.length) {
                            Core.Web.Scheduler.remove(runnable);
                            g.setBusy(false);
                            if (p.onChangeSort){
                                p.onChangeSort.call(p.owner, p.sortModel);
                            }
                        }
                    }), 1, true);
                }
            },

            // * rebuild pager based on new properties ...
            // -------------------------------------------
            buildpager: function() {
                $('.pcontrol input', this.pDiv).val(p.page);
                $('.pcontrol span', this.pDiv).html(p.pages);

                var r1 = (p.page-1) * p.rp + 1;
                var r2 = p.rp == -1 ? p.total : r1 + p.rp - 1;
                if (p.total < r2) {
                    r2 = p.total;
                }
                
                var stat = p.pagestat;

                stat = stat.replace(/{from}/,r1);
                stat = stat.replace(/{to}/,r2);
                stat = stat.replace(/{total}/,p.total);

                $('.pPageStat', this.pDiv).html(stat);
            },

            /**
             * This method is used to control the grid busy state.
             *
             * @param busy if set to true the grid list will get a semi transparent layer, a loading message will be displayed and a spinner.
             * If set to false this layer, spinner and message will be removed.
             * @return (boolean) true if the state is changed.
             */
            setBusy: function (busy) {
                var result = false;
                var pstat = $('.pPageStat', this.pDiv);                
                if (busy) {
                    if (!this.loading) {
                        this.loading = true;
                        pstat.data('content', pstat.html());
                        pstat.html(p.procmsg);
                        $('.pReload', this.pDiv).addClass('loading');
                        $(g.block).css({
                            top:g.bDiv.offsetTop,
                            position: 'absolute',
                            width: g.bDiv.offsetWidth,
                            height: g.bDiv.offsetHeight
                        });
                        if (p.hideOnSubmit) {
                            $(this.gDiv).prepend(g.block); //$(t).hide();
                        }
                        if ($.browser.opera) {
                            $(t).css('visibility','hidden');
                        }
                        result = true;
                    }
                } else {
                    if (this.loading) {
                        var qstatus = $('.pPageStat', this.pDiv);
                        if (qstatus.html() == p.procmsg) {
                            pstat.html(pstat.data('content'));                            
                        //$('.pPageStat',this.pDiv).text('');
                        }
                        $('.pReload',this.pDiv).removeClass('loading');
                        if (p.hideOnSubmit) {
                            $(g.block).remove(); //$(t).show();
                        }
                        g.hDiv.scrollLeft = g.bDiv.scrollLeft;
                        if ($.browser.opera) {
                            $(t).css('visibility','visible');
                        }
                        this.loading = false;
                        result = true;
                    }
                }
                return result;
            },

            //* Get latest data */
            populate: function () {
                if (this.loading) return true;

                if (p.onSubmit) {
                    var gh = p.onSubmit();
                    if (!gh) return false;
                }

                if (!p.url) return false;

                // Make this grid list busy for the user.
                this.setBusy(true);

                if (!p.newp) p.newp = 1;

                if (p.page>p.pages) p.page = p.pages;
                var params = [
                {
                    name : 'page', 
                    value : p.newp
                }
                ,{
                    name : 'rp', 
                    value : p.rp
                }
                ,{
                    name : 'query', 
                    value : p.query
                }
                ,{
                    name : 'qtype', 
                    value : p.qtype
                }
                ];
              
                var data = [];
                // Only add parameters to request data which are not null.
                for (i in params) {
                    var param = params[i];
                    if (param && param.name && param.value) {
                        data.push(param);
                    }
                }
                /* COMMENT-ECHO3: We need to use echo3 calls instead of ajax URL based approach. */
                data = p.onPopulateCallback.call(p.owner, data);                
                g.addData(data);
                
//                this.data = data;
//                if (data && p.clientsort && p.sortModel && p.sortModel.columns.length > 0) {                
//                    this.multiSort(p.sortModel, new exxcellent.model.ColumnModel(p.colModel), new exxcellent.model.TableModel(new Array(data)));
//                }
                
//                /* COMMENT-ECHO3: Notify for selection */
//                g.notifyForSelection();
            },

            doSearch: function () {
                p.query = $('input[name=q]',g.sDiv).val();
                p.qtype = $('select[name=qtype]',g.sDiv).val();
                p.newp = 1;

                this.populate();
            },

            changePage: function (ctype){ //change page

                if (this.loading) return true;

                switch(ctype)
                {
                    case 'first':
                        p.newp = 1;
                        break;
                    case 'prev':
                        if (p.page>1) p.newp = parseInt(p.page) - 1;
                        break;
                    case 'next':
                        if (p.page<p.pages) p.newp = parseInt(p.page) + 1;
                        break;
                    case 'last':
                        p.newp = p.pages;
                        break;
                    case 'input':
                        var nv = parseInt($('.pcontrol input',this.pDiv).val());
                        if (isNaN(nv)) nv = 1;
                        if (nv<1) nv = 1;
                        else if (nv > p.pages) nv = p.pages;
                        $('.pcontrol input',this.pDiv).val(nv);
                        p.newp =nv;
                        break;
                }

                if (p.newp == p.page) {
                    return false;
                }

                /*  ECHO3 we need the owner of the object as 'this'. */
                if (p.onChangePage) {
                    p.onChangePage.call(p.owner, p.newp);
                }
                else {
                    this.populate();
                }
                    
            },

            getCellDim: function (obj) // get cell prop for editable event
            {
                var ht = parseInt($(obj).height());
                var pht = parseInt($(obj).parent().height());
                var wt = parseInt(obj.style.width);
                var pwt = parseInt($(obj).parent().width());
                var top = obj.offsetParent.offsetTop;
                var left = obj.offsetParent.offsetLeft;
                var pdl = parseInt($(obj).css('paddingLeft'));
                var pdt = parseInt($(obj).css('paddingTop'));
                return {
                    ht:ht,
                    wt:wt,
                    top:top,
                    left:left,
                    pdl:pdl, 
                    pdt:pdt, 
                    pht:pht, 
                    pwt: pwt
                };
            },
     
            // ** Call from Echo3 for clear current selected rows *//
            clearSelection: function() {
                this.selectedRow = null;
                $(this.gDiv).noSelect();        
            },
  
            processSelection: function(qrow, e) {
                // if selection mode is disabled ...
                if (p.selectionMode == 0) {
                    return;
                }
                
                var rowId = /(\d*)$/.exec(qrow.attr('id'))[0] * 1;
                if(p.selectionMode == 1) {
                    if(e.ctrlKey) {
                        if(qrow.is('.trSelected')) {
                            p.nur.push(rowId);
                            qrow.toggleClass('trSelected');
                        } else {
                            p.nsr.push(rowId);
                            qrow.toggleClass('trSelected');
                            if(p.osr.length != 0) {
                                p.nur.push(p.osr[0]);
                                $('#row' + p.osr[0]).toggleClass('trSelected');
                            }
                        }
                    } else {
                        if(qrow.is('.trSelected')) {
                            return;
                        } else {
                            p.nsr.push(rowId);
                            qrow.toggleClass('trSelected');
                            if(p.osr.length != 0) {
                                p.nur.push(p.osr[0]);
                                $('#row' + p.osr[0]).toggleClass('trSelected');
                            }
                        }
                    }
                    qrow.siblings().removeClass('trSelected');
                    g.notifyForSelection();
                }
                else if(p.selectionMode == 2) {
                    if(e.ctrlKey) {
                        var idx = null;            
                        if(qrow.is('.trSelected')) {
                            idx = $.inArray(rowId, p.nsr);
                            if(idx != -1) {
                                p.nsr.splice(idx, 1);
                            }
                            else {
                                p.nur.push(rowId);
                            }
                        }
                        else {
                            idx = $.inArray(rowId, p.nur);
                            if(idx != -1) {
                                p.nur.splice(idx, 1);
                            }
                            else {
                                p.nsr.push(rowId);
                            }
                        }
                        qrow.toggleClass('trSelected');            
                    }
                    else {
                        if(qrow.is('.trSelected')) {
                            p.nur = $.makeArray(p.osr);
                            p.nur.splice($.inArray(rowId, p.nur), 1);
                        }
                        else {
                            p.nsr.push(rowId);
                            p.nur = $.makeArray(p.osr);
                        }
                        qrow.addClass('trSelected');
                        qrow.siblings().removeClass('trSelected');
                        g.notifyForSelection();
                    }
                }        
                g.selectedRow = qrow;
            },
      
            // ** Notify Echo3 on selection and unselection *//
            notifyForSelection: function() {
                if(p.onSelection) {
                    p.asr = $.merge($.merge([], p.nsr), p.osr);
                    $(p.nur).each(function(i, val) {
                        var idx = $.inArray(val, p.asr);
                        if(idx != -1) {
                            p.asr.splice(idx, 1);
                        }
                    });
                    
                    
                    p.onSelection.call(p.owner, p.asr, p.osr, p.nsr, p.nur);
                    
                    p.osr = $.makeArray(p.asr);
                    p.nsr = $.makeArray();
                    p.nur = $.makeArray();
                }
            },
      
            // ** Echo3 request for selection * //
            makeSelection: function(rs) {              
                /** row selection */
                p.asr = rs.asr;
                p.osr = rs.osr;
                p.nsr = rs.nsr;
                p.nur = rs.nur;
                
                var rows = $('#' + $.fn.fixID(p.ownerId + '.DATA')).children('tr');
                rows.each(function() {
                    var qr = $(this);
                    if ($.inArray(this.id, p.asr) != -1) {
                        qr.addClass('trSelected');
                    } else {
                        qr.removeClass('trSelected');
                    }
                });
                                
                this.notifyForSelection();
            },
            
            pager: 0,
            
            getCellHeader: function(cell) {
                if (cell.is('th')) {
                    return cell;
                } else {
                    return $('tr:eq(0) > th:eq(' + cell.index() + ')', this.hTable);
                }
            },
            
            renderCell: function(componentOrIndex, div, td, qth) {
                if (Echo.Render._disposedComponents == null) {
                    Echo.Render._disposedComponents = {};
                }
                
                var component = null;
                if (typeof componentOrIndex == "number") {
                    component = p.owner.component.getComponent(componentOrIndex);
                } else {
                    component = componentOrIndex;
                }
                
                td.id = 'CELL.' + component.renderId;                
                g.renderCellLayoutData(component, div, td, qth);
                Echo.Render.renderComponentAdd(new Echo.Update.ComponentUpdate(), component, div);
                
                if (p.onRenderCell) {
                    p.onRenderCell.call(p.owner, component);
                }
                
//                var autoResizeMethod = Core.method( {td: td, div: div, component: component}, function(event) {
//                    var componentHeight = component.render("height");
//                    if (Echo.Sync.Extent.isPercent(componentHeight)) {
//                        div.style.height = '100%';
//                    }
//                    
//                    var componentWidth = component.render("width");
//                    if (Echo.Sync.Extent.isPercent(componentWidth)) {
//                        div.style.width = '100%';
//                    } else {
//                        var defFloat = div.style.cssFloat;
//                        div.style.cssFloat = 'left';
//                        div.style.width = '';
//                        
//                        var bounds = new Core.Web.Measure.Bounds(div);
//                        div.style.width = bounds.width + 'px';
//                        div.style.cssFloat = defFloat;
//                    }
//                    
//                    var td_width = $(td).width();
//                    var div_width = $(div).width();
//                    
//                    if (div_width > td_width) {
//                        $(td).attr('title', component.get("text"));
//                    }
//                });
//                
//                autoResizeMethod();
//                
//                component.removeAllListeners('updated', true);
//                component.addListener('updated', autoResizeMethod, true);
            },
                        
            renderCellLayoutData: function(componentOrIndex, div, td, qth) {
                var component = null;
                if (typeof componentOrIndex == "number") {
                    component = p.owner.component.getComponent(componentOrIndex);
                } else {
                    component = componentOrIndex;
                }
                
                var layoutData = component.render("layoutData");
                if (layoutData) {
                  
                    if (layoutData.width) {                      
                        if (!qth) {
                            qth = g.getCellHeader($(td));
                        }
                        
                        var userSized = qth.data('isUserSized');
                        if (td != qth[0]) {
                            if (!userSized) {
                                td.style.width = layoutData.width;
                            } else {
                                td.style.width = qth[0].style.width;
                            }
                        } else if (!userSized) {
                            td.style.width = layoutData.width;
                        }
                    }
                    
                    if (layoutData.height) {
                        td.style.height = layoutData.height;
                    }
                    
                    if (layoutData.alignment) {
                        var horizontal = Echo.Sync.Alignment.getRenderedHorizontal(layoutData.alignment);
                        var vertical = typeof(alignment) == "object" ? layoutData.alignment.vertical : layoutData.alignment;

                        switch (horizontal) {
                            case "center":
                                div.style.margin = "0px auto";
                                div.style.cssFloat = 'none';
                                break;
                            default:
                                div.style.cssFloat = horizontal;
                                break;
                        }

                        var verticalValue;
                        switch (vertical) {
                            case "top":
                                verticalValue = "top";
                                break;
                            case "middle":
                                verticalValue = "middle";
                                break;
                            case "bottom":
                                verticalValue = "bottom";
                                break;
                            default:
                                verticalValue = "";
                                break;
                        }
                        td.style.verticalAlign = verticalValue;
                        td.style.textAlign = 'center';
                    }
                    
                    Echo.Sync.Insets.render(layoutData.insets, div, "padding");                    
                    Echo.Sync.Color.render(layoutData.background, td, "backgroundColor");
                    Echo.Sync.FillImage.render(layoutData.backgroundImage, td);
                }
                
                if (Echo.Sync.Extent.isPercent(component.render("height"))) {
                    div.style.height = '100%';
                }

                if (Echo.Sync.Extent.isPercent(component.render("width"))) {
                    div.style.width = '100%';
                }
            },
            
            reloadPositions: function() {
                g.hDiv.style.top = g.mDiv.offsetHeight + 'px';                
                g.bDiv.style.top = p.headerVisible ? (g.hDiv.offsetTop + g.hDiv.offsetHeight) + 'px' : g.hDiv.style.top;                
                g.bDiv.style.bottom = g.pDiv.offsetHeight + 'px';
            },
            
            getCounterIndexes: function() {
                var result = [];
                var qth = $("th[cmid='-1']", g.hDiv);
                if (qth[0]) {
                    result[0] = /(\d*)$/.exec(qth.attr('id'))[0] * 1;
                    var position = qth.index();
                    var rows = $('#' + $.fn.fixID(p.ownerId + '.DATA')).children('tr');
                    rows.each(function(index, row) {
                        result[index + 1] = /(\d*)$/.exec(row.childNodes[position].id)[0] * 1;
                    })
                }
                return result;
            }
        }; // --- EOF Grid Declaration (g)

        // -----------------------------------------------------------------------------------------------------------

        //analyze column model if any
        if (p.colModel) {
            var thead = document.createElement('thead');
            var tr = document.createElement('tr');
            var protoTh = document.createElement('th');
            
            for (var i = 0; i < p.colModel.length; i++) {
                var columnModel = p.colModel[i];
                var th = protoTh.cloneNode(false);

                if (columnModel.id !== null) {
                    th.setAttribute('cmid', columnModel.id);
                    if (columnModel.sortable) {
                        th.setAttribute('abbr', columnModel.id);
                    }
                }
                
                th.setAttribute('title', columnModel.tooltip);
                th.hide = columnModel.hide || !columnModel.visible;
                th.process = columnModel.process;
                
                $(th).data({'rowDataIndex': i, 'componentIdx': columnModel.cell.componentIdx, 'visible': columnModel.visible});                
                tr.appendChild(th);
            }
            thead.appendChild(tr);
            $(t).prepend(thead);
        }

        //init divs
        var pDiv = document.createElement('div'); //create prototype div        
        g.gDiv = pDiv.cloneNode(false); //create global container
        g.mDiv = pDiv.cloneNode(false); //create title container
        g.hDiv = pDiv.cloneNode(false); //create header container
        g.bDiv = pDiv.cloneNode(false); //create body container
        g.vDiv = pDiv.cloneNode(false); //create grip
        g.rDiv = pDiv.cloneNode(false); //create horizontal resizer
        g.cDrag = pDiv.cloneNode(false); //create column drag
        g.block = pDiv.cloneNode(false); //creat blocker
        g.nDiv = pDiv.cloneNode(false); //create column show/hide popup
        g.nBtn = pDiv.cloneNode(false); //create column show/hide button
        g.iDiv = pDiv.cloneNode(false); //create editable layer
        g.tDiv = pDiv.cloneNode(false); //create toolbar
        g.sDiv = pDiv.cloneNode(false);

        if (p.usepager || p.showPageStat) {
            g.pDiv = pDiv.cloneNode(false); //create pager container
        }
        g.hTable = document.createElement('table');
        g.hTable.id = 'HEADER.' + p.ownerId;

        //set gDiv
        g.gDiv.className = 'flexigrid';
        //$(g.gDiv).css({ height: '100%', width: '100%'});
        
        //if (p.width!='auto')
        //    g.gDiv.style.width = p.width;

        //add conditional classes
        if ($.browser.msie) {
            g.gDiv.className += ' ie';
        }

        if (p.novstripe) {
            g.gDiv.className += ' novstripe';
        }

        $(t).before(g.gDiv);
        g.gDiv.appendChild(t);

        //set toolbar
        if (p.buttons) {
            g.tDiv.className = 'tDiv';
            var tDiv2 = document.createElement('div');
            tDiv2.className = 'tDiv2';

            for (i = 0; i < p.buttons.length; i++) {
                var btn = p.buttons[i];
                if (!btn.separator)
                {
                    var btnDiv = document.createElement('div');
                    btnDiv.className = 'fbutton';
                    btnDiv.innerHTML = "<div><span>"+btn.name+"</span></div>";
                    if (btn.bclass)
                        $('span',btnDiv)
                        .addClass(btn.bclass)
                        .css({
                            paddingLeft:20
                        })
                    ;
                    btnDiv.onpress = btn.onpress;
                    btnDiv.name = btn.name;
                    if (btn.onpress)
                    {
                        $(btnDiv).click
                        (
                            function ()
                            {
                                this.onpress(this.name,g.gDiv);
                            }
                            );
                    }
                    $(tDiv2).append(btnDiv);
                    if ($.browser.msie&&$.browser.version<7.0)
                    {
                        $(btnDiv).hover(function(){
                            $(this).addClass('fbOver');
                        },function(){
                            $(this).removeClass('fbOver');
                        });
                    }

                } else {
                    $(tDiv2).append("<div class='btnseparator'></div>");
                }
            }
            $(g.tDiv).append(tDiv2);
            $(g.tDiv).append("<div style='clear:both'></div>");
            $(g.gDiv).prepend(g.tDiv);
        }

        //set hDiv
        g.hDiv.className = 'hDiv';
        if (!p.headerVisible) {
            g.hDiv.style.visibility = 'hidden';
            g.hDiv.style.height = '0px';          
        }        
        
        $(t).before(g.hDiv);

        //set hTable
        g.hTable.cellPadding = 0;
        g.hTable.cellSpacing = 0;
        
        var hDivBox = pDiv.cloneNode(false);
        hDivBox.className = 'hDivBox';
        g.hDiv.appendChild(hDivBox);
        hDivBox.appendChild(g.hTable);
        
        var thead = $("thead:first", t)[0];
        if (thead) {
            g.hTable.appendChild(thead);
        }
        thead = null;

        if (!p.colmodel) var ci = 0;

        //setup table header (thead)
        $('thead tr:first th', g.hDiv).each(function () {
          
                var qth = $(this);
                
                var thdiv = document.createElement('div');
                g.renderCell(qth.data('componentIdx'), thdiv, this, $(this));

                var columnNameSelector = qth.attr('abbr');
                if (columnNameSelector){
                    // click on a column (change sorting)
                    qth.click(
                        function (e) {
                            if (!$(this).hasClass('thOver')) return false;
                            var obj = (e.target || e.srcElement);
                            if (obj.href || obj.type) return true;

                            var thDiv = $('div', this);
                            if (thDiv.hasClass('active')){
                                thDiv.toggleClass('sasc');
                                thDiv.toggleClass('sdesc');
                            } else {
                                thDiv.addClass('active');
                            }
                            g.changeSort(this, e.ctrlKey);
                        }
                        )
                    ;
                    // setup initial sorting
                    if (p.sortModel && p.sortModel.columns) {
                        for (i=0; i<p.sortModel.columns.length; i++) {
                            var sortColumn = p.sortModel.columns[i];
                            if (columnNameSelector == sortColumn.columnId) {
                                qth.addClass('sorted');
                                thdiv.className = 's'+ sortColumn.sortOrder;
                            }
                        }
                    }
                }
                // setup initial hiding
                if (this.hide) qth.hide();

                if (!p.colmodel) {
                    qth.attr('axis','col' + ci++);
                }
                
                qth.empty().append(thdiv).removeAttr('width')
                .mousedown(function (e)
                {
                    g.dragStart('colMove',e,this);
                })
                .hover(
                    // hover in function
                    function(){
                        if (!g.colresize&&!$(this).hasClass('thMove')&&!g.colCopy) $(this).addClass('thOver');
                        // check if sortable column
                        if ($(this).attr('abbr')) {
                            var thDiv = $('div',this);
                            var isSorted = $(this).hasClass('sorted');
                            if (!isSorted && !g.colCopy && !g.colresize) {
                                thDiv.addClass('s' + p.sortorder);
                            }
                            else if (isSorted && !g.colCopy && !g.colresize){
                                thDiv.toggleClass('sasc');
                                thDiv.toggleClass('sdesc');
                            }
                        }
                        // drop the dragged column on another column (hover-in)
                        if (g.colCopy) {
                            var n = $('th', g.hDiv).index(this);
                            if (n == g.dcoln) {
                                return false;
                            }
                            
                            var offset = qth.offset();
                            var arrow = null;
                            if (n < g.dcoln) {
                                arrow = $(g.cdropleft);
                                arrow.css('left', offset.left);
                            } else {
                                arrow = $(g.cdropright);
                                arrow.css('left', offset.left + qth.width() - 16);
                            }
                            
                            arrow.css('top', offset.top + (qth.height() / 2) - 8);
                            qth.append(arrow);
                            
                            g.dcolt = n;
                        } else if (!g.colresize) {
                            var nv = $('th:visible',g.hDiv).index(this);
                            var onl = parseInt($('div:eq('+nv+')',g.cDrag).css('left'));
                            var nw = parseInt($(g.nBtn).width()) + parseInt($(g.nBtn).css('borderLeftWidth'));
                            var nl = onl - nw + Math.floor(p.cgwidth/2);

                            $(g.nDiv).hide();
                            $(g.nBtn).hide();

                            $(g.nBtn).css({
                                'left': nl,
                                'top': g.hDiv.offsetTop,
                                'height': this.offsetHeight                                
                            }).show();

                            var ndw = parseInt($(g.nDiv).width());

                            $(g.nDiv).css({
                                top:g.bDiv.offsetTop
                            });

                            if ((nl+ndw)>$(g.gDiv).width())
                                $(g.nDiv).css('left',onl-ndw+1);
                            else
                                $(g.nDiv).css('left',nl);

                            if ($(this).hasClass('sorted'))
                                $(g.nBtn).addClass('srtd');
                            else
                                $(g.nBtn).removeClass('srtd');

                        }

                    },
                    // hover out function
                    function(){
                        $(this).removeClass('thOver');
                        var thDiv = $('div', this);
                        if(!$(thDiv).hasClass('active')) {
                            var thDiv = $('div',this);
                            if (!$(this).hasClass('sorted')){
                                thDiv.removeClass('s' + p.sortorder);
                            } else {
                                thDiv.toggleClass('sasc');
                                thDiv.toggleClass('sdesc');
                            }
                        } else {
                            $(thDiv).removeClass('active');
                        }
                        if (g.colCopy) {
                            $(g.cdropleft).remove();
                            $(g.cdropright).remove();
                            g.dcolt = null;
                        }
                    })
            ; //wrap content
            }
            );

        //set bDiv (body div)
        g.bDiv.className = 'bDiv';
        $(t).before(g.bDiv);

        //        $(g.bDiv).css({
        //            height: (p.height=='auto') ? '50 px' : p.height
        //        })
        
        $(g.bDiv).scroll(function (e) {
            g.scroll();
            return true;
        }).append(t);

        if (p.height == 'auto')
        {
            $('table',g.bDiv).addClass('autoht');
        }


        $('tbody', g.bDiv).hide();

        //set cDrag
        var cdcol = $('thead tr:first th:first', g.hDiv)[0];

        if (cdcol != null) {
            g.cDrag.className = 'cDrag';
            g.cdpad = 0;
            
            var qcdcol = $(cdcol);
            var qcdcolDiv = $('div', cdcol);
            
            g.cdpad += (isNaN(parseInt(qcdcolDiv.css('borderLeftWidth'))) ? 0 : parseInt(qcdcolDiv.css('borderLeftWidth')));
            g.cdpad += (isNaN(parseInt(qcdcolDiv.css('borderRightWidth'))) ? 0 : parseInt(qcdcolDiv.css('borderRightWidth')));
            g.cdpad += (isNaN(parseInt(qcdcolDiv.css('paddingLeft'))) ? 0 : parseInt(qcdcolDiv.css('paddingLeft')));
            g.cdpad += (isNaN(parseInt(qcdcolDiv.css('paddingRight'))) ? 0 : parseInt(qcdcolDiv.css('paddingRight')));
            
            g.cdpad += (isNaN(parseInt(qcdcol.css('borderLeftWidth'))) ? 0 : parseInt(qcdcol.css('borderLeftWidth')));
            g.cdpad += (isNaN(parseInt(qcdcol.css('borderRightWidth'))) ? 0 : parseInt(qcdcol.css('borderRightWidth')));
            g.cdpad += (isNaN(parseInt(qcdcol.css('paddingLeft'))) ? 0 : parseInt(qcdcol.css('paddingLeft')));
            g.cdpad += (isNaN(parseInt(qcdcol.css('paddingRight'))) ? 0 : parseInt(qcdcol.css('paddingRight')));

            $(g.bDiv).before(g.cDrag);

            var cdheight = $(g.bDiv).height();
            var hdheight = $(g.hDiv).height();
            
            g.cDrag.style.top = -hdheight + 'px';

            var cgDivProto = document.createElement('div');
            cgDivProto.style.height = (cdheight + hdheight) + 'px';
            
            $('thead tr:first th', g.hDiv).each(function() {
                var cgDiv = cgDivProto.cloneNode(false);
                g.cDrag.appendChild(cgDiv);
                
                var qcgDiv = $(cgDiv);                
                if (!p.cgwidth) {
                    p.cgwidth = qcgDiv.width();
                }

                qcgDiv.mousedown(function(e) {g.dragStart('colresize', e, this);});

                /*
                if ($.browser.msie && $.browser.version < 7.0) {
                    g.fixHeight($(g.gDiv).height());
                    qcgDiv.hover(function() { g.fixHeight(); $(this).addClass('dragging'); },
                                    function () { if (!g.colresize) $(this).removeClass('dragging'); });
                }
                */
            });
        }

        if (p.resizable && p.height !='auto')
        {
            g.vDiv.className = 'vGrip';
            $(g.vDiv)
            .mousedown(function (e) {
                g.dragStart('vresize',e);
            })
            .html('<span></span>');
            $(g.bDiv).after(g.vDiv);
        }

        if (p.resizable && p.width !='auto' && !p.nohresize)
        {
            g.rDiv.className = 'hGrip';
            $(g.rDiv)
            .mousedown(function (e) {
                g.dragStart('vresize',e,true);
            })
            .html('<span></span>')
            .css('height',$(g.gDiv).height())
            ;
            if ($.browser.msie&&$.browser.version<7.0)
            {
                $(g.rDiv).hover(function(){
                    $(this).addClass('hgOver');
                },function(){
                    $(this).removeClass('hgOver');
                });
            }
            $(g.gDiv).append(g.rDiv);
        }

        // add pager
        if (p.usepager || p.showPageStat)
        {
            g.pDiv.className = 'pDiv';
            g.pDiv.innerHTML = '<div class="pDiv2"></div>';
            $(g.bDiv).after(g.pDiv);
            if (p.usepager) {
                var pagerHtml = ' <div class="pGroup"> <div class="pFirst pButton"><span></span></div>' +
                '<div class="pPrev pButton"><span></span></div> </div> <div class="btnseparator"></div> ' +
                '<div class="pGroup"><span class="pcontrol">{page} <input type="text" size="4" value="1" /> {of} <span> 1 </span>' +
                '</span></div> <div class="btnseparator"></div> <div class="pGroup"> <div class="pNext pButton">' +
                '<span></span></div><div class="pLast pButton"><span></span></div> </div>';
              
                pagerHtml = pagerHtml.replace(/{page}/, p.pageWord);
                pagerHtml = pagerHtml.replace(/{of}/, p.ofWord);              
              
              
                $('div',g.pDiv).html(pagerHtml);
                // register events for pager
                $('.pReload',g.pDiv).click(function(){
                    g.populate()
                });
                $('.pFirst',g.pDiv).click(function(){
                    g.changePage('first')
                });
                $('.pPrev',g.pDiv).click(function(){
                    g.changePage('prev')
                });
                $('.pNext',g.pDiv).click(function(){
                    g.changePage('next')
                });
                $('.pLast',g.pDiv).click(function(){
                    g.changePage('last')
                });
                $('.pcontrol input',g.pDiv).keydown(function(e){
                    if(e.keyCode==13) g.changePage('input')
                });
                if ($.browser.msie&&$.browser.version<7) $('.pButton',g.pDiv).hover(function(){
                    $(this).addClass('pBtnOver');
                },function(){
                    $(this).removeClass('pBtnOver');
                });

                // add 'rows per page' combobox
                if (p.useRp)
                {
                    var opt = "";
                    for (var nx=0;nx<p.rpOptions.length;nx++)
                    {
                        if (p.rp == p.rpOptions[nx]) sel = 'selected="selected"'; else sel = '';
                        opt += "<option value='" + p.rpOptions[nx] + "' " + sel + " >" + p.rpOptions[nx] + "&nbsp;&nbsp;</option>";
                    };
                    $('.pDiv2',g.pDiv).prepend("<div class='pGroup'><select name='rp'>"+opt+"</select></div> <div class='btnseparator'></div>");
                    $('select',g.pDiv).change(
                        function ()
                        {
                            p.newp = 1;
                            p.rp = +this.value;
                            if (p.onRpChange) {
                                p.onRpChange.call(p.owner, +this.value);
                            }                
                            else {
                                g.populate();
                            }
                        }
                        );
                }
            }
            // add page statistics
            if (p.showPageStat) {
                var pageStatHtml = ' <div class="btnseparator"></div> <div class="pGroup"> <div class="pReload pButton"><span></span></div> </div> <div class="btnseparator"></div> <div class="pGroup"><span class="pPageStat"></span></div>';
                $('.pDiv2',g.pDiv).append(pageStatHtml);
                $('.pReload',g.pDiv).click(function(){
                    g.populate()
                });
            }
            //add search button
            if (p.searchitems)
            {
                $('.pDiv2',g.pDiv).prepend("<div class='pGroup'> <div class='pSearch pButton'><span></span></div> </div>  <div class='btnseparator'></div>");
                $('.pSearch',g.pDiv).click(function(){
                    $(g.sDiv).slideToggle('fast',function(){
                        $('.sDiv:visible input:first',g.gDiv).trigger('focus');
                    });
                });
                //add search box
                g.sDiv.className = 'sDiv';

                var sitems = p.searchitems;

                var sopt = "";
                for (var s = 0; s < sitems.length; s++)
                {
                    if (p.qtype=='' && sitems[s].isdefault==true)
                    {
                        p.qtype = sitems[s].name;
                        var sel = 'selected="selected"';
                    } else sel = '';
                    sopt += "<option value='" + sitems[s].name + "' " + sel + " >" + sitems[s].display + "&nbsp;&nbsp;</option>";
                }

                if (p.qtype=='') p.qtype = sitems[0].name;

                $(g.sDiv).append("<div class='sDiv2'>Quick Search <input type='text' size='30' name='q' class='qsbox' /> <select name='qtype'>"+sopt+"</select> <input type='button' value='Clear' /></div>");

                $('input[name=q],select[name=qtype]',g.sDiv).keydown(function(e){
                    if(e.keyCode==13) g.doSearch()
                });
                $('input[value=Clear]',g.sDiv).click(function(){
                    $('input[name=q]',g.sDiv).val('');
                    p.query = '';
                    g.doSearch();
                });
                $(g.bDiv).after(g.sDiv);

            }

        }
        $(g.pDiv,g.sDiv).append("<div style='clear:both'></div>");

        // add title
        if (p.title)
        {
            g.mDiv.className = 'mDiv';
            g.mDiv.innerHTML = '<div class="ftitle">'+p.title+'</div>';
            $(g.gDiv).prepend(g.mDiv);
            if (p.showTableToggleBtn)
            {
                $(g.mDiv).append('<div class="ptogtitle" title="' + p.mintablemsg + '"><span></span></div>');
                $('div.ptogtitle',g.mDiv).click
                (
                    function ()
                    {
                        $(g.hDiv).toggle();
                        $(g.bDiv).toggle();
                        $(g.pDiv).toggle();
                      
                        $(g.gDiv).toggleClass('hideBody');
                        $(this).toggleClass('vsble');
                    }
                    );
            }
        //g.rePosDrag();
        }

        //setup cdrops
        g.cdropleft = document.createElement('span');
        g.cdropleft.className = 'cdropleft';
        g.cdropright = document.createElement('span');
        g.cdropright.className = 'cdropright';

        //add block
        g.block.className = 'gBlock';
        var gh = $(g.bDiv).height();
        var gtop = g.bDiv.offsetTop;
        $(g.block).css(
        {
            width: g.bDiv.style.width,
            height: gh,
            background: 'white',
            position: 'relative',
            marginBottom: (gh * -1),
            zIndex: 1,
            top: gtop,
            left: '0px'
        }
        );
        $(g.block).fadeTo(0,p.blockOpacity);

        // add column control
        if ($('th', g.hDiv).length)
        {

            g.nDiv.className = 'nDiv';
            g.nDiv.innerHTML = "<table cellpadding='0' cellspacing='0'><tbody></tbody></table>";
            $(g.nDiv).css(
            {
                marginBottom: (gh * -1),
                display: 'none',
                top: gtop
            }
            ).noSelect()
            ;

            $('th div', g.hDiv).each(function (index) {
                var qth = $("th[axis='col" + index + "']", g.hDiv);
                var chk = 'checked = "checked"';
                if (qth.css('display') == 'none') {
                    chk = '';
                }
                                
                var tr = document.createElement('tr');
                if (!qth.data('visible')) {
                    $(tr).css('display', 'none');
                }
                
                $('tbody', g.nDiv).append(tr);
                $(tr).append('<td class="ndcol1"><input type="checkbox" '+ chk +' class="togCol" value="'+ index +'" /></td><td class="ndcol2">'+ qth.html() + '</td>');
                
            });

            if ($.browser.msie&&$.browser.version<7.0)
                $('tr',g.nDiv).hover
                (
                    function () {
                        $(this).addClass('ndcolover');
                    },
                    function () {
                        $(this).removeClass('ndcolover');
                    }
                    );

            $('td.ndcol2', g.nDiv).click
            (
                function ()
                {
                    if ($('input:checked',g.nDiv).length<=p.minColToggle&&$(this).prev().find('input')[0].checked) return false;
                    return g.toggleCol($(this).prev().find('input').val());
                }
                );

            $('input.togCol', g.nDiv).click
            (
                function ()
                {

                    if ($('input:checked',g.nDiv).length<p.minColToggle&&!this.checked) {
                        return false;
                    }
                    $(this).parent().next().trigger('click');
                //return false;
                }
                );


            $(g.gDiv).prepend(g.nDiv);

            $(g.nBtn).addClass('nBtn')
            .html('<div></div>')
            .attr('title',p.hidecolmsg)
            .click
            (
                function ()
                {
                    $(g.nDiv).fadeToggle('fast');
                    return true;
                }
                );

            if (p.showToggleBtn) $(g.gDiv).prepend(g.nBtn);

        }

        // add date edit layer
        $(g.iDiv)
        .addClass('iDiv')
        .css({
            display:'none'
        })
        ;
        $(g.bDiv).append(g.iDiv);

        // add flexigrid events
        $(g.bDiv)
        .hover(function(){
            $(g.nDiv).hide();
            $(g.nBtn).hide();
        },
        function(){
            if (g.multisel) 
                g.multisel = false;
        })
        ;
        
        $(g.gDiv)
        .hover(function(){},function(){
            $(g.nDiv).hide();
            $(g.nBtn).hide();
        })
        ;

        // pinkhominid (2008.09.20): ie leak fix start
        //add document events
        // we need the document here, otherwise the dragging is restricted
        // to the selected div, e.g $(g.gDiv)
        $(document)
        .mousemove(mousemove)
        .mouseup(mouseup)
        .mouseenter(hoverover)
        .mouseleave(hoverout);

        function mousemove(e) {
            g.dragMove(e);
        }
        function mouseup() {
            g.dragEnd();
        }
        function hoverover(){}
        function hoverout() {
            g.dragEnd();
        }

        g.cleanup = function () {
            // Unbind events listeners attached outside flexigrid gDiv
            $(document)
            .unbind('mousemove', mousemove)
            .unbind('mouseup', mouseup)
            .unbind('mouseenter', hoverover)
            .unbind('mouseleave', hoverout);
            // Unbind all event listeners inside flexigrid gDiv
            $(t.grid.gDiv).remove();

            // Help GC
            p.onToggleCol = null;
            p.onChangeSort =  null; // using custom change sort function
            p.preProcess = null; // using custom pre processing before addData function
            p.onSuccess = null; // using custom validate after addData function
            p.onChangePage = null; // using custom change page function
            p.onSubmit = null; // using a custom populate function
            p.onPopulateCallback = null; // using a custom populate callback function with parsed params
            p.onDragCol =  null; // using a custom on column dragdrop callback function
            p.onResizeCol = null; // using a custom on column resizing callback function
            p.onResizeGrid = null; // using a custom on grid resizing callback function
            p = null;
            g = null;
            t.grid = null;
            t.p = null;
            t = null;
        };
        // pinkhominid (2008.09.20): ie leak fix end

        /**
         * Method to focus and blur the flexigrid table.
         */
        g.focus = function (focusState) {
            // FOCUS on first row
            // if flexigrid is busy we will trigger the focus after its finished.
            if (this.loading) {
                g.lazyFocus = g.focus;
            } else {
                g.lazyFocus = null;
                if (!$("table tbody tr").hasClass('trSelected')) {
                    //g.selectedRow = $("table tbody tr:first-child", g.bDiv);
                    if (focusState) {
                    //g.selectedRow.addClass('trSelected');
                    }else {
                        $("table tbody tr").removeClass('trSelected');
                    }
                }
            }
        };

        /**
         * Method to remote control the flexigrid via keycodes.
         */
        g.remoteControl = function (e) {
            if(g.selectedRow && g.selectedRow != null ) {
                var keycode = e.keyCode;
                if (e.keyCode == 13) {
                    // press enter to trigger the same as mouse click (up)
                    g.selectedRow.trigger("click");
                } else if (e.keyCode == 40) {
                    // press arrow down
                    var nextselectedRow = g.selectedRow.next();
                    if (nextselectedRow.is('tr')) {
                        g.processSelection(nextselectedRow, e);
                        var rowsHeight = 0;
                        nextselectedRow.prevAll().each(function () {
                            rowsHeight += $(this).height();
                        });
                        if ($(g.bDiv).height()/2.3 < rowsHeight)
                            g.bDiv.scrollTop = g.bDiv.scrollTop + nextselectedRow.height();                
                    }
                } else if (e.keyCode == 38) {
                    // press arrow up
                    var prevselectedRow = g.selectedRow.prev();
                    if (prevselectedRow.is('tr')) {
                        g.processSelection(prevselectedRow, e);
                        var rowsHeight = 0;
                        prevselectedRow.nextAll().each(function () {
                            rowsHeight += $(this).height();
                        });
                        if ($(g.bDiv).height()/2.3 < rowsHeight)
                            g.bDiv.scrollTop = g.bDiv.scrollTop - prevselectedRow.height();
                    }
                } else if (e.keyCode == 39) {
                    // press arrow right
                    g.bDiv.scrollLeft = g.bDiv.scrollLeft + 50;
                    g.scroll();
                } else if (e.keyCode == 37) {
                    // press arrow left
                    g.bDiv.scrollLeft = g.bDiv.scrollLeft - 50;
                    g.scroll();
                }
            }
            return true;
        };

        /**
         * Method to multisort the flexigrid on demand.
         */
        g.multiSort = function (sortModel, columnModel, tableModel) {
            var sortingColumns = sortModel.columns;

            var columnsToSort = new Array();
            for (idx = 0; idx < sortingColumns.length; idx++) {
                var sortingColumn = sortingColumns[idx];
                for (var adx = 0; adx < columnModel.columns.length; adx++) {
                    if(columnModel.columns[adx].id == sortingColumn.columnId){
                        columnsToSort.push(new Object({
                            index: adx,
                            order: sortingColumn.sortOrder
                        }));
                    }
                }
            }
            columnsToSort.reverse();
            var allRows = new Array();
            for (var idx = 0; idx < tableModel.pages.length; idx++) {
                allRows = allRows.concat(tableModel.pages[idx].rows);
            }
            var delimiterRegExp = new RegExp('[\\' + p.digitGroupDL + ']', 'g');
            var decimalDelimiterRegExp = new RegExp('[\\' + p.decimalDelimiter + ']', 'g');
            allRows = multiSorter(columnsToSort, allRows);
            // implement paging here...
            var firstPage = tableModel.pages[0];
            firstPage.rows = allRows;
            return tableModel;

            /**
             * A method to sort rows using multiple columns.
             */
            function multiSorter (columns, rows) {
                for (idx = 0; idx < columns.length; idx++) {
                    var columnIdx = columns[idx].index;
                    var sortOrder = columns[idx].order;
                    rows.sort(alphaNumericSorter);
                }
                return rows;

                function alphaNumericSorter(row1, row2) {
                    var row1Cell = p.owner.component.getComponent(row1.cells[columnIdx].componentIdx).get('text');
                    var row2Cell = p.owner.component.getComponent(row2.cells[columnIdx].componentIdx).get('text');
                    
                    // undefined rows
                    if (!row1Cell && !row2Cell) {
                        return 0;// test for undefined rows
                    } else if (!row1Cell && row2Cell) {
                        return -1;// test for undefined row1
                    } else if (row1Cell && !row2Cell) {
                        return 1;// test for undefined row2
                    }
                    
                    var result;
                    if (isDigit(row1Cell) && isDigit(row2Cell)) {
                        // convert into num value the fastest way,
                        // see http://www.jibbering.com/faq/faq_notes/type_convert.html
                        if (typeof row1Cell != 'number')
                            var row1Num = row1Cell.replace(delimiterRegExp,'');
                        // after replacing the delimiter, we make sure to have a '.' as delimiter for Decimal-Values
                        if(typeof(row1Num) != 'undefined') // can happen...
                        {
                            row1Num = (+row1Num.replace(decimalDelimiterRegExp, '.'));
                        }

                        if (typeof row2Cell != 'number')
                            var row2Num = row2Cell.replace(delimiterRegExp,'');
                        // after replacing the delimiter, we make sure to have a '.' as delimiter for Decimal-Values
                        if(typeof(row2Num) != 'undefined') // can happen...
                            row2Num = row2Num.replace(decimalDelimiterRegExp, '.');
                        result = sortOrder == 'asc' ? row1Num - row2Num : row2Num - row1Num;
                    }
                    // string rows
                    else {
                        if (row1Cell == row2Cell) {
                            result = 0;
                        }
                        if (sortOrder == 'asc') {
                            result = (row1Cell < row2Cell) ? -1 : 1;
                        } else {
                            result = (row1Cell > row2Cell) ? -1 : 1;
                        }
                    }

                    return result;
                }
                
                function isDigit(s) {
                    if (typeof s == 'number') return true;
                    var DECIMAL = '\\' + p.digitGroupDL;
                    var DECIMAL_DELIMITER = '\\' + p.decimalDelimiter;
                    var exp = '(^([-+]?[\\d'+ DECIMAL + DECIMAL_DELIMITER + ']*)$)';
                    return RegExp(exp).test($.trim(s));
                }
            }
        };
        /**
         * Method to force populating the flexigrid again by setting the loading mode
         * to false. The loading mode is normally used while rendering.
         */
        g.reload = function () {
            g.loading = false;
            g.populate()
        };

        //browser adjustments
        if ($.browser.msie&&$.browser.version < 7.0) {
            $('.hDiv,.bDiv,.mDiv,.pDiv,.vGrip,.tDiv, .sDiv',g.gDiv).css({width: '100%'});
            $(g.gDiv).addClass('ie6');
            if (p.width!='auto') $(g.gDiv).addClass('ie6fullwidthbug');
        }

        // g.rePosDrag(); // don't need
        // g.fixHeight(); // don't need

        //make grid functions accessible
        t.p = p;
        t.grid = g;

        // Load data if possible and enabled.
        if (p.url && p.autoload) {
            g.populate();
        } else {
            // Make this grid list busy for the user.
            g.setBusy(true);

            /**
             * This method is used to finalize the rendering of the data to the body if the grid list.
             * @return (void)
             */
            function finalizeRendering() {
                g.setBusy(false);
                $('tbody', g.bDiv).show();
            }

            // Add tr and td properties

            // What is going on here? Because of many rows we have to render, we do not
            // iterate with a regular foreach method. We make a pseudo asynchron process with
            // the setTimeout method. We do better to do this because in other way we will
            // force a lagging of the whole browser. In the worst case the user will get a
            // dialog box of an "endless looping javaScript".

            // Set initial properties for rendering the data.
            var qth = $('thead tr:first th',g.hDiv);
            var rows = $('tbody tr', g.bDiv);
            var rowIndex = 0;
            function doRow() {
                // Only if there are more rows we will render a next row.
                if (rowIndex < rows.length) {
                    var tr = rows[rowIndex];
                    // Paranoid I know but it possible that there is an array selected with
                    // null entries.
                    if (tr) {
                        var qtr = $(tr);
                        var i = 0;
                        $('td', tr).each(function() {
                            var header = false;
                            if (qth.length > i) {
                                header = qth[i] || false;
                            }
                            g.addCellProp(this, tr, this.innerHTML, header);
                            i++;
                        });
                        g.addRowProp(qtr);
                        // Prepare the next step.
                        rowIndex++;
                        setTimeout(doRow, 1);
                    } else {
                        finalizeRendering();
                    }
                } else {
                    finalizeRendering();
                }
            }
            // Start the pseudo asynchron iteration.
            setTimeout(doRow, 1);
        }
        
        g.reloadPositions();
        
        return t;
    };

    $.fn.flexigrid = function(p) {
        return this.each( function() {
            $.addFlex(this,p);
        });
    };

    $.fn.flexReload = function(p) { // function to reload grid

        return this.each( function() {
            if (this.grid){
                return this.grid.reload();
            }
        });
    };

    $.fn.flexOptions = function(p) { //function to update general options

        return this.each( function() {
            if (this.grid) $.extend(this.p,p);
        });

    };

    $.fn.flexToggleCol = function(cid,visible) { // function to reload grid

        return this.each( function() {
            if (this.grid) this.grid.toggleCol(cid,visible);
        });

    };

    $.fn.flexAddData = function(data) { // function to add data to grid
        return this.each( function() {
            if (this.grid) {
                this.grid.setBusy(true);
                this.grid.addData(data);
            }
        });
    };

    $.fn.noSelect = function(p) {
        if (p == null)
            var prevent = true;
        else
            prevent = p;
        if (prevent) {
            return this.each(function () {
                if ($.browser.msie||$.browser.safari) $(this).bind('selectstart',function(){
                    return false;
                });
                else if ($.browser.mozilla)
                {
                    $(this).css('MozUserSelect','none');
                    $('body').trigger('focus');
                }
                else if ($.browser.opera) $(this).bind('mousedown',function(){
                    return false;
                });
                else $(this).attr('unselectable','on');
            });
        } else {
            return this.each(function () {
                if ($.browser.msie||$.browser.safari) $(this).unbind('selectstart');
                else if ($.browser.mozilla) $(this).css('MozUserSelect','inherit');
                else if ($.browser.opera) $(this).unbind('mousedown');
                else $(this).removeAttr('unselectable','on');
            });
        }
    };

    $.fn.flexDestroy = function() {
        return this.each( function() {
            if (this.grid) this.grid.cleanup();
        });
    };

    $.fn.flexMultiSort = function(sortModel, colModel, tableModel) {
        return this.each( function() {
            if (this.grid) this.grid.multiSort(sortModel, colModel, tableModel);
        });
    };

    $.fn.flexFocus = function(focusState) {
        return this.each( function() {
            if (this.grid) this.grid.focus(focusState);
        });
    };
    
    $.fn.flexFixHeight = function() {
        return this.each( function() {
            if (this.grid) this.grid.fixHeight();
        });
    };

    $.fn.flexRemoteControl = function(e) {
        return this.each( function() {
            if (this.grid) this.grid.remoteControl(e);
        });
    };    
    
    $.fn.flexCheckSelection = function() {
        return this.each( function() {
            if (this.grid) this.grid.notifyForSelection();
        });
    };
    
    $.fn.flexMakeSelection = function(rowSelection) {
        return this.each( function() {
            if (this.grid) this.grid.makeSelection(rowSelection);
        });
    };
    
    $.fn.flexGetCounterIndexes = function() {
        if (this[0].grid) {return this[0].grid.getCounterIndexes();};
    };
    
    $.fn.flexRenderChilds = function(childs) {
        return this.each( function() {
            if (this.grid) {
                var pDiv = document.createElement("div");
                var g = this.grid;
                var frenderer = function(renderId) {
                    var ID = "CELL." + renderId;
                    var cell = document.getElementById(ID);
                    if (cell) {                        
                        var childDiv = pDiv.cloneNode(false);
                        g.renderCell(/(\d*)$/.exec(ID)[0] * 1, childDiv, cell);                        
                        cell.removeChild(cell.firstChild);
                        cell.appendChild(childDiv);
                    }
                }
                               
                if (childs.length <= 50) {
                    for (var i = 0; i < childs.length; ++i) {
                        frenderer(childs[i]);
                    }
                } else {
                    this.grid.setBusy(true);                
                    var cc = 0;
                    var runnable = Core.Web.Scheduler.run(Core.method(this, function() {
                        var cellsPerBatch = 50;
                        while (cellsPerBatch > 0 && cc < childs.length) {
                            frenderer(childs[cc]);
                            cc++;
                            cellsPerBatch--;
                        }

                        if (cc == childs.length) {
                            this.grid.reloadPositions();
                            this.grid.setBusy(false);
                            Core.Web.Scheduler.remove(runnable);
                        }
                    }), 1, true);
                }
            };
        });
    };
    
    $.fn.flexRenderLayoutChilds = function(childs) {
        return this.each( function() {
            if (this.grid) {
                this.grid.setBusy(true);    
                var cc = 0;                
                var runnable = Core.Web.Scheduler.run(Core.method(this, function() {
                    var cellsPerBatch = 200;
                    while (cellsPerBatch > 0 && cc < childs.length) {
                      var ID = "CELL." + childs[cc];                      
                      var cell = document.getElementById(ID);                      
                      cc++;
                      cellsPerBatch--;                      
                      if (cell) {this.grid.renderCellLayoutData(/(\d*)$/.exec(ID)[0] * 1, cell.firstChild, cell);}
                    }                    
                    if (cc == childs.length) {
                        this.grid.reloadPositions();
                        this.grid.rePosDrag();
                        this.grid.setBusy(false);                        
                        Core.Web.Scheduler.remove(runnable);
                    }
                }), 1, true);
            };
        });
    };
    
    $.fn.flexUpdateColumns = function(updates) {
        return this.each( function() {
            if (this.grid) {
                var g = this.grid;
                
                var sc = function(qth, visible) {
                    var qndcol = $($('td.ndcol2', g.nDiv)[qth.index()]);
                    var currentState = qndcol.prev().find('input')[0].checked;
                    if (currentState == visible) {
                        qndcol.trigger('click');
                    }
                }
                
                for (u = 0; u < updates.length; u++) {
                    var qth = $("th[cmid='" + updates[u].ID + "']", g.hDiv);
                    for (var p = 0; p < updates[u].props.length; p++) {
                        var propName = updates[u].props[p][0];
                        var propValue = updates[u].props[p][1];                        
                        switch(propName) {
                            case exxcellent.FlexiGrid.COL_UPDATE_TOOLTIP:
                                qth.attr('title', propValue);
                                break;
                            case exxcellent.FlexiGrid.COL_UPDATE_SORTABLE:
                                // not supported yet ...
                                break;
                            case exxcellent.FlexiGrid.COL_UPDATE_HIDED:
                                sc(qth, propValue);
                                break;
                            case exxcellent.FlexiGrid.COL_UPDATE_VISIBLE:
                                sc(qth, !propValue);
                                var ntr = $($('td.ndcol2', g.nDiv)[qth.index()]).parent();
                                if (propValue) {
                                    ntr.css('display', '');
                                } else {
                                    ntr.css('display', 'none');
                                }
                                break;
                            default:
                                throw new Error("Unsupported column update property: " + propName);
                                break;                                
                        }
                    }
                }
                
                this.grid.reloadPositions();
            };
        });
    };

    $.fn.fixID = function(ID) {
        return ID.replace(/(:|\.)/g,'\\$1');
    };

    //* Plugin for unslectable elements */
    $.fn.unselectable = function() {
        return this.each(function() {
            $(this)
            .css('-moz-user-select', 'none')	// FF
            .css('-khtml-user-select', 'none')	// Safari, Google Chrome
            .css('user-select', 'none');	// CSS 3

            if ($.browser.msie) {		// IE
                $(this).each(function() {
                    this.ondrag = function() {
                        return false;
                    };
                });
                $(this).each(function() {
                    this.onselectstart = function() {
                        return (false);
                    };
                });
            }else if($.browser.opera) {
                $(this).attr('unselectable', 'on');
            }
        });
    };    
})(jQuery);