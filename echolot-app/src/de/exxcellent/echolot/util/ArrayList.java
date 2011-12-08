/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.exxcellent.echolot.util;

import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author sieskei (XSoft Ltd.)
 */
public class ArrayList<T> extends java.util.ArrayList<T>
{
  public ArrayList() { super(); }
  
  public ArrayList(Collection<? extends T> col) { super(col); }

  public ArrayList( int size, T def_val ) { super(  Collections.nCopies( size, def_val ) ); }

  public ArrayList( int size ) { this(  size, null ); }
  
  @Override
  public void removeRange( int first_index, int last_index ) { super.removeRange( first_index, last_index ); }

  public void setSize( int size, T def_val )
  {
    if( size > size() )
    {
      final int elems_cnt = size - size();
      if( elems_cnt == 1 )
        this.add( def_val );
      else
        this.addAll( Collections.nCopies( elems_cnt, def_val ) );
    }
    else
    if( size < this.size() )
      removeRange( size, size() );
  }

  public void setSize( int size ) { this.setSize( size, null ); }
}
