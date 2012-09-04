/*
 * This file (FlexiColumn.java) is part of the Echolot Project (hereinafter "Echolot").
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
import java.util.Map.Entry;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Label;

/**
 *
 * @author sieskei (XSoft Ltd.)
 */
public class FlexiColumn implements Serializable {    
    private static final long serialVersionUID = 201110102l;
    
    public static final String PROPERTY_COL_TOOLTIP   = "columnTooltip";
    public static final String PROPERTY_COL_SORTABLE  = "columnSortable";
    public static final String PROPERTY_COL_HIDED     = "columnHided";
    public static final String PROPERTY_COL_VISIBLE   = "columnVisible";
    public static final String COLUMN_PROPERTY_CHANGE = "propertyColumnChange";
    
    public final class FlexiColumnProperty implements Entry<String, Object>, Cloneable, Serializable {
        private final String propertyName;
        private Object propertyValue;
        
        private FlexiColumnProperty(final String propertyName, final Object propertyValue) {
            this.propertyName = propertyName;
            this.propertyValue = propertyValue;
        }
        
        @Override
        public String getKey() {
            return this.propertyName;
        }

        @Override
        public Object getValue() {
            return this.propertyValue;
        }

        @Override
        public Object setValue(Object v) {
            FlexiColumnProperty oldValue = this.clone();
            this.propertyValue = v;
            FlexiColumn.this.firePropertyChange(oldValue, this);
            return v;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final FlexiColumnProperty other = (FlexiColumnProperty) obj;
            if ((this.propertyName == null) ? (other.propertyName != null) : !this.propertyName.equals(other.propertyName)) {
                return false;
            }
            if (this.propertyValue != other.propertyValue && (this.propertyValue == null || !this.propertyValue.equals(other.propertyValue))) {
                return false;
            }
            return true;
        }

        @Override
        protected FlexiColumnProperty clone() {
          return new FlexiColumnProperty(propertyName, propertyValue);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[ FlexiColumnProperty:\n")
            .append("- ColID: ").append(FlexiColumn.this.id).append("\n")
            .append("- name: ").append(this.propertyName).append("\n")
            .append("- value: ").append(propertyValue).append("]\n");
            return sb.toString();
        }
    }
    
    private final int id;
    private final FlexiCell cell;
        
    /** 
     * The property change event dispatcher.
     * This object is lazily instantiated. 
     */
    private PropertyChangeSupport componentChangeSupport;
    private final FlexiColumnProperty tooltipProp;
    private final FlexiColumnProperty sortableProp;
    private final FlexiColumnProperty hidedProp;
    private final FlexiColumnProperty visibleProp;
        
    public FlexiColumn(int id, String title) {
        this(id, new Label(title), null, true, false, true);
    }
    
    public FlexiColumn(int id, String title, String tooltip, boolean sortable, boolean hided, boolean visible) {
        this(id, new Label(title), tooltip, sortable, hided, visible);
    }
    
    public FlexiColumn(int id, Label title, String tooltip, boolean sortable, boolean hided, boolean visible) {
        this.id = id;
        cell = new FlexiCell(-1, id, title);
        
        // set default props for LayoutData ...
        // ... if necessary, children will inherit
        // ---------------------------------------
        FlexiCellLayoutData layoutData = cell.getLayoutData().clone();
        layoutData.setInsets(new Insets(new Extent(0), new Extent(0), new Extent(0), new Extent(0)));
        layoutData.setAlignment(new Alignment(Alignment.CENTER, Alignment.CENTER));
        layoutData.setWidth(new Extent(135));
        layoutData.setHeight(new Extent(25));
        
        cell.setLayoutData(layoutData);
        
        // * new version * //
        // --------------- //
        this.tooltipProp  = new FlexiColumnProperty(PROPERTY_COL_TOOLTIP,  tooltip == null ? "" : tooltip);
        this.sortableProp = new FlexiColumnProperty(PROPERTY_COL_SORTABLE, sortable);
        this.hidedProp    = new FlexiColumnProperty(PROPERTY_COL_HIDED,    hided);
        this.visibleProp  = new FlexiColumnProperty(PROPERTY_COL_VISIBLE,  visible);
    }

    public FlexiCell getCell() {
        return cell;
    } 
    
    public final int getId() {
        return id;
    }

    public final boolean isSortable() {
        return (Boolean) sortableProp.getValue();
    }

    public final boolean isHided() {
        return (Boolean) hidedProp.getValue();
    }

    public final boolean isVisible() {
        return (Boolean) visibleProp.getValue();
    }

    public final String getTooltip() {
        return (String) tooltipProp.getValue();
    }
    
    public void setSortable(boolean sortable) {
        this.sortableProp.setValue(sortable);
    }
    
    public void hide() {
       this.hidedProp.setValue(true);
    }
    
    public void show() {
        this.hidedProp.setValue(false);
    }

    public void setVisible(boolean visible) {
        this.visibleProp.setValue(visible);
    }
    
    public void setTooltip(String tooltip) {
        this.tooltipProp.setValue(tooltip);
    }
    
    /**
     * Adds a property change listener to this <code>FlexiColumn</code>.
     *
     * @param l the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        if (componentChangeSupport == null) {
            componentChangeSupport = new PropertyChangeSupport(this);
        }
        componentChangeSupport.addPropertyChangeListener(COLUMN_PROPERTY_CHANGE, l);
    }
    
    /**
     * Removes a property change listener from this <code>FlexiColumn</code>.
     *
     * @param l the listener to be removed
     */
    public void removeComponentChangeListener(PropertyChangeListener l) {
        if (componentChangeSupport != null) {
            componentChangeSupport.removePropertyChangeListener(COLUMN_PROPERTY_CHANGE, l);
        }
    }
    
    /**
     * Reports a bound component change to <code>ComponentChangeListener</code>s
     *
     * @param oldValue the previous property
     * @param newValue the present property
     */
    protected void firePropertyChange(FlexiColumnProperty oldValue, FlexiColumnProperty newValue) {
        // Report to PropertyChangeListeners.
        if (componentChangeSupport != null) {
            componentChangeSupport.firePropertyChange(COLUMN_PROPERTY_CHANGE, oldValue, newValue);
        }
    }
        
    @Override
    public int hashCode() {
        return this.id;
    }
    
    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
          return false;
      }
      if (getClass() != obj.getClass()) {
          return false;
      }
      final FlexiColumn other = (FlexiColumn) obj;
      if (this.id != other.id) {
          return false;
      }
      return true;
    }
}
