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
import java.beans.PropertyChangeEvent;
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
import nextapp.echo.app.LayoutData;

/**
 *
 * @author sieskei (XSoft Ltd.)
 */
public class FlexiCell implements Serializable, Comparable<FlexiCell> {    
    private static final long serialVersionUID = 201110101l;
    private static final String PROPERTY_COMPONENT_CHANGE = "PROPERTY_COMPONENT_CHANGE";
    private static final String PROPERTY_LAYOUTDATA_CHANGE = "PROPERTY_LAYOUTDATA_CHANGE";
        
    private abstract class SerializablePropertyChangeListener implements PropertyChangeListener, Serializable { }
    
    /** 
     * The property change event dispatcher.
     * This object is lazily instantiated. 
     */
    private PropertyChangeSupport componentChangeSupport;
    
    private final int rowId;
    private final int colId;
    
    private boolean internalSetLayoutData = false;
    //private boolean valid = true;
    
    private Label EMPTY;
    private Component component;
    private PropertyChangeListener componentVisibleChanged = new SerializablePropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            Boolean visible = (Boolean) pce.getNewValue();            
            if (visible) {
                firePropertyChange(PROPERTY_COMPONENT_CHANGE, EMPTY, component);
            } else {
                firePropertyChange(PROPERTY_COMPONENT_CHANGE, component, EMPTY);
            }
        }
    };
    
    private PropertyChangeListener externalComponentLDChanged = new SerializablePropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            Component c = (Component) pce.getSource();
            if (!internalSetLayoutData && c.isRenderVisible()) {
                try {
                    FlexiCellLayoutData newLayoutData = (FlexiCellLayoutData) pce.getNewValue();
                    EMPTY.setLayoutData(newLayoutData);
                    firePropertyChange(PROPERTY_LAYOUTDATA_CHANGE, pce.getOldValue(), newLayoutData);
                } catch (ClassCastException ex) {
                    throw new Error("Unsupported layoutData for FlexiCell component: " + pce.getSource());
                }
            }
        }
    };
            
    public FlexiCell(final int rowId, final int colId, final Component component) {       
        this.rowId = rowId;
        this.colId = colId;
        this.component = component;
        
        LayoutData orignalLayoutData = component.getLayoutData();
        if (!(orignalLayoutData instanceof FlexiCellLayoutData)) {
            FlexiCellLayoutData layoutData = new FlexiCellLayoutData();
            component.setLayoutData(layoutData);
        }
        
        EMPTY = new Label();
        EMPTY.setLayoutData(component.getLayoutData());
        
        bindComponent();
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
      final Object currentLayoutData = component.getLayoutData();
      if (currentLayoutData == layoutData) {
          return;
      }
        
      try {
        this.internalSetLayoutData = true;        
        this.component.setLayoutData(layoutData);
        this.EMPTY.setLayoutData(layoutData);
        firePropertyChange(PROPERTY_LAYOUTDATA_CHANGE, currentLayoutData, layoutData);
      } finally {
        this.internalSetLayoutData = false;
      }
    }

    /**
     * Get cell component.
     * @return current cell component.
     */
    public Component getComponent() {
        return component;
    }
    
    public Component getValidComponent() { 
        return component != null ? component.isVisible() ? component : EMPTY : EMPTY;
    }
    
    /**
     * Set new cell component.
     * <br />
     * The new component inherits layoutData!
     * @param newComponent new cell component
     */
    public void setComponent(Component newComponent) {
        if (newComponent != null) {
            if (component == newComponent) {
              return;
            }
            
            if (component != null) {
                // replace current
                unbindComponent();
                final Component currentComponent = component;
                FlexiCellLayoutData currentLayoutData = getLayoutData();
                
                final LayoutData compLayoutData = newComponent.getLayoutData();
                if (compLayoutData instanceof FlexiCellLayoutData) {
                    final FlexiCellLayoutData newLayoutData = ((FlexiCellLayoutData) compLayoutData).clone();
                    newLayoutData.setWidth(getWidth());
                    newLayoutData.setHeight(getHeight());
                    currentLayoutData = newLayoutData;
                }
                
                component = newComponent;        
                component.setLayoutData(currentLayoutData);
                EMPTY.setLayoutData(currentLayoutData);

                firePropertyChange(PROPERTY_COMPONENT_CHANGE, currentComponent, component);
                bindComponent();                
            } else {
                // add current              
                LayoutData newLayoutData = newComponent.getLayoutData();
                if (!(newLayoutData instanceof FlexiCellLayoutData)) {
                    FlexiCellLayoutData layoutData = new FlexiCellLayoutData();
                    newComponent.setLayoutData(layoutData);
                }

                component = newComponent;
                EMPTY = new Label();
                EMPTY.setLayoutData(component.getLayoutData());
                
                firePropertyChange(PROPERTY_COMPONENT_CHANGE, null, component);
                bindComponent();
            }            
        } else {            
            // remove current
            unbindComponent();            
            firePropertyChange(PROPERTY_COMPONENT_CHANGE, component, EMPTY);
            component = null;
        }
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
        if ((currentWidth == null && newWidth == null) || (currentWidth != null && currentWidth.compareTo(newWidth) == 0)) {
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
        if ((currentHeight == null && newHeight == null) || (currentHeight != null && currentHeight.compareTo(newHeight) == 0)) {
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
        boolean result = equalsProps(getAlignment(), newAlignment);
        if (!result) {
            FlexiCellLayoutData cloned = getLayoutData().clone();
            cloned.setAlignment(newAlignment);
            setLayoutData(cloned);
        }
        return !result;
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
        boolean result = equalsProps(getInsets(), newInsets);
        if (!result) {
            FlexiCellLayoutData cloned = getLayoutData().clone();
            cloned.setInsets(newInsets);
            setLayoutData(cloned);
        }
        return !result;
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
        boolean result = equalsProps(getBackground(), newBackground);
        if (!result) {
            FlexiCellLayoutData cloned = getLayoutData().clone();
            cloned.setBackground(newBackground);
            setLayoutData(cloned);
        }
        return !result;
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
        boolean result = equalsProps(getBackgroundImage(), newBackgroundImage);
        if (!result) {
            FlexiCellLayoutData cloned = getLayoutData().clone();
            cloned.setBackgroundImage(newBackgroundImage);
            setLayoutData(cloned);
        }
        return !result;
    }
    
    
    private boolean equalsProps(Object oldValue, Object newValue) {
        if ((oldValue == null && newValue == null) || (oldValue != null && oldValue.equals(newValue))) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Inherits the settings from another cell, if no own.
     * @param cell 'base' cell
     */
    public void equalizeLayoutDataTo(FlexiCell cell) {
        equalizeLayoutDataTo(cell.getLayoutData());
    }
    
    public void equalizeLayoutDataTo(FlexiCellLayoutData cellLayoutData) {
        FlexiCellLayoutData newLayoutData = FlexiCellLayoutData.inherit(getLayoutData(), cellLayoutData);
        if (newLayoutData != null) {
            setLayoutData(newLayoutData);
        }
    }

    /**
     * Adds a component change listener to this <code>FlexiCell</code>.
     *
     * @param l the listener to add
     */
    public void addComponentChangeListener(PropertyChangeListener l) {
        addListener(PROPERTY_COMPONENT_CHANGE, l);
    }
    
    /**
     * Removes a component change listener from this <code>FlexiCell</code>.
     *
     * @param l the listener to be removed
     */
    public void removeComponentChangeListener(PropertyChangeListener l) {
        removeListener(PROPERTY_COMPONENT_CHANGE, l);
    }
    
    
    /**
     * Adds a layoutdata change listener to this <code>FlexiCell</code>.
     *
     * @param l the listener to add
     */
    public void addLayoutDataChangeListener(PropertyChangeListener l) {
        addListener(PROPERTY_LAYOUTDATA_CHANGE, l);
    }
    
    /**
     * Removes a layoutdata change listener from this <code>FlexiCell</code>.
     *
     * @param l the listener to be removed
     */
    public void removeLayoutDataChangeListener(PropertyChangeListener l) {
        removeListener(PROPERTY_LAYOUTDATA_CHANGE, l);
    }
    
    /**
     * Base method for add listener.
     * @param propertyName the name of the changed property
     * @param l listener
     */
    private void addListener(String propertyName, PropertyChangeListener l) {
        if (componentChangeSupport == null) {
            componentChangeSupport = new PropertyChangeSupport(this);
        }
        componentChangeSupport.addPropertyChangeListener(propertyName, l);
    }
       
    /**
     * Base method for remove listener.
     * @param propertyName the name of the changed property
     * @param l listener
     */
    private void removeListener(String propertyName, PropertyChangeListener l) {
        if (componentChangeSupport != null) {
            componentChangeSupport.removePropertyChangeListener(propertyName, l);
        }
    }
    
    /**
     * Reports a bound property change to <code>PropertyChangeListener</code>s
     * and to the <code>ApplicationInstance</code>'s update management system.
     *
     * @param propertyName the name of the changed property
     * @param oldValue the previous value of the property
     * @param newValue the present value of the property
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        // Report to PropertyChangeListeners.
        if (componentChangeSupport != null) {
            componentChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }
    
    private void bindComponent() {
        this.component.addPropertyChangeListener(Component.VISIBLE_CHANGED_PROPERTY, componentVisibleChanged);
        this.component.addPropertyChangeListener(Component.PROPERTY_LAYOUT_DATA, externalComponentLDChanged);
    }
        
    private void unbindComponent() {
        this.component.removePropertyChangeListener(Component.VISIBLE_CHANGED_PROPERTY, componentVisibleChanged);
        this.component.removePropertyChangeListener(Component.PROPERTY_LAYOUT_DATA, externalComponentLDChanged);
    }
    
    public void invalidate() {
        final Component currComp;
        final Component newComp;        
        if (component != null) {
            if (component.isVisible()) {
                currComp = component;
                newComp = EMPTY;
            } else {
                currComp = EMPTY;
                EMPTY = new Label();
                EMPTY.setLayoutData(currComp.getLayoutData());           
                newComp = EMPTY;
            }
            unbindComponent();
            component = null;
        } else {
            currComp = EMPTY;
            EMPTY = new Label();
            EMPTY.setLayoutData(currComp.getLayoutData());           
            newComp = EMPTY;
        }        
        firePropertyChange(PROPERTY_COMPONENT_CHANGE, currComp, newComp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ FlexiCell:\n")        
        .append("- ID: ").append(component.getId()).append("\n")
        .append("- RenderID: ").append(component.getRenderId()).append("\n")
        .append("- Component: ").append(component).append("\n")
        .append("- RowID: ").append(rowId).append("\n")
        .append("- ColID: ").append(colId).append("\n")
        .append("- Hash: ").append(hashCode()).append(" ]\n");
        return sb.toString();
    }

    @Override
    public int compareTo(FlexiCell t) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if(this.hashCode() < t.hashCode()) {
            return BEFORE;
        } else if(this.hashCode() > t.hashCode()) {
            return AFTER;
        } else {
            return EQUAL;
        }
    }
}
