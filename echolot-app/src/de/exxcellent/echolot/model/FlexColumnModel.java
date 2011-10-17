/*
 * This file (FlexColumnModel.java) is part of the Echolot Project (hereinafter "Echolot").
 * Copyright (C) 2008-2011 eXXcellent Solutions GmbH.
 *
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 */

package de.exxcellent.echolot.model;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;

/**
 * ColumnModel for a column in a flexigrid
 *
 * @author Ralf Enderle <r.enderle@exxcellent.de>
 */
public interface FlexColumnModel {

    /**
     * Returns the ID of a column
     * @return the id
     */
    public int getId();

    /**
     * Returns the Title of a Column
     * @return the title
     */
    public String getTitle();

    /**
     * Returns the Tooltip of a column
     * @return the tooltip
     */
    public String getTooltip();

    /**
     * Returns the default-width of the column
     * @return
     */
    public Extent getWidth();
    
    /**
     * Returns the default-font of the column
     * @return
     */
    public Font getFont();

    /**
     * Returns true, if the column is sortable
     * @return true if the column is sortable
     */
    public boolean isSortable();

    /**
     * Returns the allignment of a column
     * Possible values are: "right", "left", "center"
     * @return the alignment
     */
    public Alignment getAlign();

    /**
     * Returns true if this column is hidden by default
     * @return true, if the column is hidden by default
     */
    public boolean isHiddenByDefault();

    /**
     * Returns true if this column is visible
     * @return true, if the column is visible
     */
    public boolean isVisible();
}
