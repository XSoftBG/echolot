/*
 * This file (ArrayList.java) is part of the Echolot Project (hereinafter "Echolot").
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
