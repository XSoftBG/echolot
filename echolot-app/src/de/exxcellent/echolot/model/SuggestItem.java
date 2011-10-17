/*
 * This file (SuggestItem.java) is part of the Echolot Project (hereinafter "Echolot").
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

/**
 * Object that represents a SuggestItem for a SuggestField (it's covered by a list in SuggestModel)
 *
 * @author Ralf Enderle
 * @version 1.0
 */
public class SuggestItem {
    private String label;
    private String description;
    private String category;
    private Integer identifier;

    /**
     * UserObject to identify the PieSector by the user
     */
    private transient Object userObject;

    public SuggestItem() {
        reCalculateIdentifier();
    }

    public SuggestItem(String label) {
        setLabel(label);

        reCalculateIdentifier();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        reCalculateIdentifier();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        reCalculateIdentifier();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        reCalculateIdentifier();
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    /**
     * Get UserObject
     * 
     * @return
     */
    public Object getUserObject() {
        return userObject;
    }

    /**
     * Set specific UserObject
     *
     * @param userObject
     */
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    // private helper

    /**
     * ReCalculate the identifier
     */
    private void reCalculateIdentifier() {
        this.identifier = hashCode();
    }
}
