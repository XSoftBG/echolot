/*
 * This file (Column.java) is part of the Echolot Project (hereinafter "Echolot").
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

package de.exxcellent.echolot.model;

import java.io.Serializable;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;

/**
 * The column model object for use in the {@link de.exxcellent.echolot.app.FlexiGrid} component.
 * 
 * <pre>
 *      columnModel[
 *              {display: 'Name', name: 0},
 *              {display: 'EMail', name: 1}
 * </pre>
 * 
 * @author Oliver Pehnke <o.pehnke@exxcellent.de>
 */
public class Column implements Serializable {
    private static final long serialVersionUID = 1l;

    /** The name for the column, i.e. id */
    private int id;

    /** The display/title for the column (headline). */
    private String display;

    /** The column width */
    private Extent width;
    
    /** The column font */
    private Font font;

    /** The column is sortable */
    private boolean sortable = true;

    /** The alignment of the column, e.g. "right", "left", "center" */
    private Alignment align;

    /** if <code>true</code> the column is hidden by default */
    private boolean hide;

    /** if <code>true</code> the column is hidden by default */
    private boolean visible = true;
    
    /** the column tooltip visible if the user hovers over the column. */
    private String tooltip;

    /** Default constructor. */
    public Column() {
      super();
    }

    /**
     * Create a new {@link Column} with the specified values.
     * 
     * @param id the id to use as unique id in this model.
     * @param columnLabel the name to display as header value.
     * @param width the initial width of the column itself, e.g. "100px" or "auto".
     * @param sortable if <code>true</code> the column is sortable
     * @param align the column alignment, e.g. "right", "left", "center"
     * @param hide if <code>true</code> the column is hidden.
     * @param tooltip the column tooltip visible if the user hovers over the column.
     */
    public Column(final int id, final String columnLabel, final Extent width, final Font font, final boolean sortable, final Alignment align,
            final boolean hide, final boolean visible, final String tooltip) {
        this.id = id;
        this.display = columnLabel;
        this.width = width;
        this.font = font;
        this.sortable = sortable;
        this.align = align;
        this.hide = hide;
        this.visible = visible;
        this.tooltip = tooltip;
    }

    /**
     * Accessor for property 'display'.
     * 
     * @return Value for property 'display'.
     */
    public String getDisplay() {
      return display;
    }

    /**
     * Mutator for property 'display'.
     * 
     * @param display
     *            Value to set for property 'display'.
     */
    public void setDisplay(final String display) {
      this.display = display;
    }

    /**
     * Accessor for property 'width'.
     * 
     * @return Value for property 'width'.
     */
    public Extent getWidth() {
      return this.width;
    }

    /**
     * Mutator for property 'width'.
     * 
     * @param width value to set for property 'width'.
     */
    public void setWidth(final Extent width) {
      this.width = width;
    }
    
    /**
     * Accessor for property 'font'.
     * 
     * @return Value for property 'font'.
     */
    public Font getFont() {
      return this.font;
    }

    /**
     * Mutator for property 'font'.
     * 
     * @param width value to set for property 'font'.
     */
    public void setFont(final Font font) {
      this.font = font;
    }

    /**
     * Accessor for property 'name'.
     * 
     * @return Value for property 'name'.
     */
    public int getId() {
      return id;
    }

    /**
     * Mutator for property 'name'.
     * 
     * @param id
     *            Value to set for property 'name'.
     */
    public void setId(int id) {
      this.id = id;
    }
    /**
       * Accessor for property 'sortable'.
       * 
       * @return Value for property 'sortable'.
       */
    public boolean isSortable() {
      return sortable;
    }
    /**
       * Mutator for property 'sortable'.
       * 
       * @param sortable Value to set for property 'sortable'.
       */
    public void setSortable(boolean sortable) {
      this.sortable = sortable;
    }
    /**
       * Accessor for property 'align'.
       * 
       * @return Value for property 'align'.
       */
    public Alignment getAlign() {
      return align;
    }
    
    /**
     * Mutator for property 'align'.
     * 
     * @param align Value to set for property 'align'.
     */
    public void setAlign(Alignment align) {
      this.align = align;
    }
	
    /**
     * Accessor for property 'hide'.
     * 
     * @return Value for property 'hide'.
     */
    public boolean getHide() {
        return hide;
    }
    
    /**
     * Mutator for property 'hide'.
     * 
     * @param hide Value to set for property 'hide'.
     */
    public void setHide(boolean hide) {
        this.hide = hide;
    }
    
    /**
     * Accessor for property 'visible'.
     * 
     * @return Value for property 'visible'.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Mutator for property 'visible'.
     * 
     * @param hide Value to set for property 'hide'.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Accessor for property 'tooltip'. The column tooltip visible if the user hovers over the column.
     * 
     * @return Value for property 'tooltip'.
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * Mutator for property 'tooltip'. The column tooltip visible if the user hovers over the column.
     * 
     * @param tooltip Value to set for property 'tooltip'.
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
    
    /**
     * Compares the specified object with this instance for equality.
     * 
     * @param object the object to be compared.
     * @return Returns <code>true</code> if the specified object is of the same type and has the same values.
     */
    @Override
    public boolean equals(final Object object) {
      if (this == object) {
        return true;
      }
      
      if (object == null || object.getClass() != this.getClass()) {
        return false;
      }
      
      Column cmp = (Column) object;
      return this.id == cmp.id &&
             this.display.equalsIgnoreCase(cmp.display) &&
             this.width.compareTo(cmp.width) == 0 &&
             this.sortable == cmp.sortable &&
             this.align.equals(cmp.align) &&
             this.visible == cmp.visible &&
             (this.tooltip != null ? cmp.tooltip != null ? this.tooltip.equalsIgnoreCase(cmp.tooltip)
                                                        : false
                                  : cmp.tooltip != null ? false
                                                        : true);
    }

    /**
     * Calculates a hash code for this object using the class fields.
     * 
     * @return The hash code for this instance.
     */
    @Override
    public int hashCode() {
      return id;
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return "Column id: " + id +
                ", width: " + width +
                ", font: " + font +
                ", sortable: " + sortable +
                ", align: " + align +
                ", hide: " + hide +
                ", visible: " + visible +
                ", tooltip: " + tooltip;
    }
}
