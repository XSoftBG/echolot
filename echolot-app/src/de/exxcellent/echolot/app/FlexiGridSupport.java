/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.exxcellent.echolot.app;

/**
 *
 * @author Miro Yozov
 */
public abstract class FlexiGridSupport {
    private FlexiGrid owner = null;
    
    public final FlexiGrid getOwner() { 
        return owner; 
    }
    
    final void setOwner(FlexiGrid owner) { 
        if (this.owner != null) {
            throw new Error("FlexiCell already has owner!");
        }
        this.owner = owner;
    }
    
    final void free() {
        this.owner = null;
    }
}
