/*
 * This file (FlexiCell.java) is part of the Echolot Project (hereinafter "Echolot").
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

import de.exxcellent.echolot.layout.FlexiCellLayoutData;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Color;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.FillImage;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Label;

/**
 *
 * @author sieskei (XSoft Ltd.)
 */
public class FlexiCell implements Serializable, Cloneable {    
    private static final long serialVersionUID = 201110101l;
    private static final String PROPERTY_COMPONENT_CHANGE = "PROPERTY_COMPONENT_CHANGE";
    
    /** 
     * The property change event dispatcher.
     * This object is lazily instantiated. 
     */
    private PropertyChangeSupport componentChangeSupport;
    
    private final int rowId;
    private final int colId;
    
    private Component component;
    
    public FlexiCell(final int rowId, final int colId, final Component component) {
        this.rowId = rowId;
        this.colId = colId;
        this.component = component;
        this.component.setLayoutData(new FlexiCellLayoutData());
    }
    
    public FlexiCell(final int rowId, final int colId, final String content) {
        this(rowId, colId, new Label(content));
    }
    
    /**
     * Row's ID in which the cell is located.
     * @return row's ID.
     */
    public int getRowId() {
        return rowId;
    }

    /**
     * Column's ID in which the cell is located.
     * @return column's ID.
     */
    public int getColId() {
        return colId;
    }
    
    FlexiCellLayoutData getLayoutData() {
        return (FlexiCellLayoutData) component.getLayoutData();
    }

    void setLayoutData(FlexiCellLayoutData layoutData) {
      this.component.setLayoutData(layoutData);
    }

    /**
     * Get cell component.
     * @return current cell component.
     */
    public Component getComponent() {
        return component;
    }

    /**
     * Set new cell component.
     * <br />
     * The new component inherits layoutData!
     * @param newComponent new cell component
     */
    public void setComponent(Component newComponent) {
        FlexiCellLayoutData layoutData = getLayoutData();
        Component oldComponent = this.component;
        this.component = newComponent;
        this.component.setLayoutData(layoutData);
        fireComponentChange(oldComponent, this.component);
    }
    
    /**
     * Get cell width.
     * @return current cell width.
     */    
    public Extent getWidth() {
        return getLayoutData().getWidth();
    }
    
    /**
     * Set new cell width.
     * @param newWidth new width.
     * @return success of the operation.
     */
    public boolean setWidth(Extent newWidth) {
        Extent currentWidth = getWidth();        
        if (currentWidth != null && currentWidth.compareTo(newWidth) == 0) {
            return false;
        }
        
        FlexiCellLayoutData cloned = getLayoutData().clone();
        cloned.setWidth(newWidth);
        setLayoutData(cloned);
        return true;
    }
    
    /**
     * Get cell height.
     * @return current cell height.
     */
    public Extent getHeight() {
        return getLayoutData().getHeight();
    }
    
    /**
     * Set new cell height.
     * @param newHeight new height.
     * @return success of the operation.
     */
    public boolean setHeight(Extent newHeight) {
        Extent currentHeight = getHeight();
        if (currentHeight != null && currentHeight.compareTo(newHeight) == 0) {
            return false;
        }
      
        FlexiCellLayoutData cloned = getLayoutData().clone();
        cloned.setHeight(newHeight);
        setLayoutData(cloned);
        return true;
    }
    
    /**
     * Get cell alignment.
     * @return current cell alignment.
     */
    public Alignment getAlignment() {
        return getLayoutData().getAlignment();
    }
    
    /**
     * Set new cell alignment.
     * @param newAlignment new alignment.
     * @return success of the operation.
     */
    public boolean setAlignment(Alignment newAlignment) {      
        if (newAlignment.equals(getAlignment())) {
            return false;
        }
        
        FlexiCellLayoutData cloned = getLayoutData().clone();
        cloned.setAlignment(newAlignment);
        setLayoutData(cloned);
        return true;
    }
    
    /**
     * Get cell insets.
     * @return current cell insets.
     */
    public Insets getInsets() {
        return getLayoutData().getInsets();
    }
        
    /**
     * Set new cell insets.
     * @param newInsets new insets.
     * @return success of the operation.
     */
    public boolean setInsets(Insets newInsets) {
        if (newInsets.equals(getInsets())) {
            return false;
        }
        
        FlexiCellLayoutData cloned = getLayoutData().clone();
        cloned.setInsets(newInsets);
        setLayoutData(cloned);
        return true;
    }
    
    /**
     * Get cell background color.
     * @return current cell background color.
     */
    public Color getBackground() {
        return getLayoutData().getBackground();
    }
   
    /**
     * Set cell background color.
     * @param newBackground new background color.
     * @return success of the operation.
     */
    public boolean setBackground(Color newBackground) {
        if (newBackground.equals(getBackground())) {
            return false;
        }
      
        FlexiCellLayoutData cloned = getLayoutData().clone();
        cloned.setBackground(newBackground);
        setLayoutData(cloned);
        return true;
    }
    
    /**
     * Get cell background image.
     * @return current cell background image.
     */
    public FillImage getBackgroundImage() {
        return getLayoutData().getBackgroundImage();
    }
    
    /**
     * Set cell background image.
     * @param newBackgroundImage new background image.
     * @return success of the operation.
     */        
    public boolean setBackgroundImage(FillImage newBackgroundImage) {
        if(newBackgroundImage.equals(getBackgroundImage())) {
            return false;
        }
        
        FlexiCellLayoutData cloned = getLayoutData().clone();
        cloned.setBackgroundImage(newBackgroundImage);
        setLayoutData(cloned);
        return true;
    }
    
    /**
     * Inherits the settings from another cell, if no own.
     * @param cell 'parental' cell
     */
    public void equalizeLayoutDataTo(FlexiCell cell) {
        FlexiCellLayoutData cellLayoutData = cell.getLayoutData();
        FlexiCellLayoutData oldLayoutData = getLayoutData();
        FlexiCellLayoutData newLayoutData = getLayoutData().clone();
        
        if(oldLayoutData.getAlignment() == null) {
            newLayoutData.setAlignment(cellLayoutData.getAlignment());
        }        
        if(oldLayoutData.getBackground() == null) {
            newLayoutData.setBackground(cellLayoutData.getBackground());
        }
        if(oldLayoutData.getBackgroundImage() == null) {
            newLayoutData.setBackgroundImage(cellLayoutData.getBackgroundImage());
        }
        if(oldLayoutData.getHeight() == null) {
            newLayoutData.setHeight(cellLayoutData.getHeight());
        }
        if(oldLayoutData.getInsets() == null) {
            newLayoutData.setInsets(cellLayoutData.getInsets());
        }
        if(oldLayoutData.getWidth() == null) {
            newLayoutData.setWidth(cellLayoutData.getWidth());
        }
        
        setLayoutData(newLayoutData);
    }

    /**
     * Adds a component change listener to this <code>FlexiCell</code>.
     *
     * @param l the listener to add
     */
    public void addComponentChangeListener(PropertyChangeListener l) {
        if (componentChangeSupport == null) {
            componentChangeSupport = new PropertyChangeSupport(this);
        }
        componentChangeSupport.addPropertyChangeListener(PROPERTY_COMPONENT_CHANGE, l);
    }
    
    /**
     * Removes a component change listener from this <code>FlexiCell</code>.
     *
     * @param l the listener to be removed
     */
    public void removeComponentChangeListener(PropertyChangeListener l) {
        if (componentChangeSupport != null) {
            componentChangeSupport.removePropertyChangeListener(PROPERTY_COMPONENT_CHANGE, l);
        }
    }
    
    /**
     * Reports a bound component change to <code>ComponentChangeListener</code>s
     *
     * @param oldValue the previous component
     * @param newValue the present component
     */
    protected void fireComponentChange(Component oldValue, Component newValue) {
        // Report to PropertyChangeListeners.
        if (componentChangeSupport != null) {
            componentChangeSupport.firePropertyChange(PROPERTY_COMPONENT_CHANGE, oldValue, newValue);
        }
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (this.rowId << 16) | this.colId;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FlexiCell other = (FlexiCell) obj;
        if (this.rowId != other.rowId) {
            return false;
        }
        if (this.colId != other.colId) {
            return false;
        }
        if (this.component != other.component && (this.component == null || !this.component.equals(other.component))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ FlexiCell:\n")
        .append("- ID: ").append(component.getId()).append("\n")
        .append("- RenderID: ").append(component.getRenderId()).append("\n")
        .append("- Component: ").append(component).append("\n")
        .append("- RowID: ").append(rowId).append("\n")
        .append("- ColID: ").append(colId).append(" ]\n");
        return sb.toString();
    }
}
