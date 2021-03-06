/*
 * This file (FlexiCellLayoutData.java) is part of the Echolot Project (hereinafter "Echolot").
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

package de.exxcellent.echolot.layout;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Color;
import nextapp.echo.app.Extent;
import nextapp.echo.app.FillImage;
import nextapp.echo.app.Insets;
import nextapp.echo.app.layout.CellLayoutData;

/**
 *
 * @author sieskei (XSoft Ltd.)
 */
public class FlexiCellLayoutData extends CellLayoutData implements Cloneable {
  
    /** Serial Version UID. */
    private static final long serialVersionUID = 20111010L;
  
    public static FlexiCellLayoutData inherit(FlexiCellLayoutData inheritor, FlexiCellLayoutData base) {
        boolean diff = false;
        FlexiCellLayoutData newLayoutData = inheritor.clone();
        
        if(inheritor.getAlignment() == null && base.getAlignment() != null) {
            newLayoutData.setAlignment(base.getAlignment());
            diff = true;
        }

        if(inheritor.getBackground() == null && base.getBackground() != null) {
            newLayoutData.setBackground(base.getBackground());
            diff = true;
        }

        if(inheritor.getBackgroundImage() == null && base.getBackgroundImage() != null) {
            newLayoutData.setBackgroundImage(base.getBackgroundImage());
            diff = true;
        }

        if(inheritor.getWidth() == null && base.getWidth() != null) {
            newLayoutData.setWidth(base.getWidth());
            diff = true;
        }

        if(inheritor.getHeight() == null && base.getHeight() != null) {
            newLayoutData.setHeight(base.getHeight());
            diff = true;
        }

        if(inheritor.getInsets() == null && base.getInsets() != null) {
            newLayoutData.setInsets(base.getInsets());
            diff = true;
        }

        if (diff) {
            return newLayoutData;
        } else {
            return null;
        }
    }
  
    
    
    private Extent width = null;
    private Extent height = null;

    public FlexiCellLayoutData() {
        super();
    }
    
    public FlexiCellLayoutData(Extent width, Extent height, Alignment align, Insets insets, Color bgColor, FillImage bgImage) {
        this.width = width;
        this.height = height;
        setAlignment(align);
        setInsets(insets);
        setBackground(bgColor);
        setBackgroundImage(bgImage);
    }
    
    public Extent getHeight() {
        return height;
    }

    public void setHeight(Extent height) {
        this.height = height;
    }
    
    public Extent getWidth() {
        return width;
    }
    
    public void setWidth(Extent width) {
        this.width = width;
    }
    
    @Override
    public FlexiCellLayoutData clone() {
        return new FlexiCellLayoutData(width, height, getAlignment(), getInsets(), getBackground(), getBackgroundImage());
    }
}
