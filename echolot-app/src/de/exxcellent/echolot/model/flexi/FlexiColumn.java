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
import java.io.Serializable;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;

/**
 *
 * @author sieskei (XSoft Ltd.)
 */
public class FlexiColumn implements Serializable {    
    private static final long serialVersionUID = 201110102l;
    
    private final FlexiCell cell;
    
    private final int id;    
    private boolean sortable;
    private boolean hided ;
    private boolean visible;
    private String tooltip;
        
    public FlexiColumn(int id, String title) {
        this(id, title, null, true, false, true);
    }
    
    public FlexiColumn(int id, String title, String tooltip, boolean sortable, boolean hided, boolean visible) {
        this.id = id;
        cell = new FlexiCell(-1, id, title);
        
        this.tooltip = tooltip;
        this.sortable = sortable;
        this.hided = hided;
        this.visible = visible;
        
        // set default props for LayoutData ...
        // ... if necessary, children will inherit
        // ---------------------------------------
        FlexiCellLayoutData layoutData = cell.getLayoutData().clone();
        layoutData.setInsets(new Insets(new Extent(0), new Extent(0), new Extent(0), new Extent(0)));
        layoutData.setAlignment(new Alignment(Alignment.CENTER, Alignment.CENTER));
        layoutData.setWidth(new Extent(135));
        layoutData.setHeight(new Extent(25));
        
        cell.setLayoutData(layoutData);
    }

    public FlexiCell getCell() {
        return cell;
    } 
    
    public final int getId() {
        return id;
    }

    public final boolean isSortable() {
        return sortable;
    }

    public final boolean isHided() {
        return hided;
    }

    public final boolean isVisible() {
        return visible;
    }

    public final String getTooltip() {
        return tooltip;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }
    
    public void hide() {
        this.hided = true;
    }
    
    public void show() {
        this.hided = false;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (obj == null) {
          return false;
      }
      if (getClass() != obj.getClass()) {
          return false;
      }
      final FlexiColumn other = (FlexiColumn) obj;
      if (this.cell != other.cell && (this.cell == null || !this.cell.equals(other.cell))) {
          return false;
      }
      if (this.id != other.id) {
          return false;
      }
      if (this.sortable != other.sortable) {
          return false;
      }
      if (this.hided != other.hided) {
          return false;
      }
      if (this.visible != other.visible) {
          return false;
      }
      if ((this.tooltip == null) ? (other.tooltip != null) : !this.tooltip.equals(other.tooltip)) {
          return false;
      }
      return true;
    }
}
