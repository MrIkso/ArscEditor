package com.mrikso.arsceditor.gui.dialogs;
// Copyright Keith D Gregory
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;


/**
 *  A layout manager designed for building input forms. It provides the
 *  following features:
 *  <UL>
 *  <LI> Divides the container into a grid of X rows and Y columns. Number of
 *       columns is set at construction time, number of rows is determined from
 *       number of components to be laid out.
 *  <LI> Components are assigned to grid locations based on the order that they
 *       are added to the container, going across and then down (ie, row 0
 *       column 0, followed by row 0 column 1) . You do not need to specify an
 *       explicit constraints object for a component.
 *  <LI> The preferred width of a column is found by taking the maximum preferred
 *       width of all components in the column. The preferred height of a column
 *       is found by taking the maximum preferred height of all components in
 *       the column.
 *  <LI> The minimum width/height are calculated the same way, using the minimum
 *       dimensions of the components.
 *  <LI> Each component is laid out at its preferred height and width, if there
 *       is room in the cell; components are not stretched to fit the height and
 *       width of their cell. This layout manager is used for forms, and stretching
 *       all components to same size gives a very unnatural appearance.
 *  <LI> If the layout manager does not have room for all rows and columns, it
 *       will reduce each to its minimum height or width. It will not attempt
 *       to reduce components below their minimum width, which means that
 *       components may be laid out outside of the bounds of the container.
 *  <LI> By default, components will be aligned according to their preferred
 *       horizontal and vertical alignment. The layout manager will provide a
 *       way to override for all components, since forms typically use a left/
 *       center alignment.
 *  </UL>
 ******************************************************************************
 * @version    1.0
 * @copyright  2004, Keith D. Gregory
 * @cvs        {$Id: CompactGridLayout.java,v 1.3 2009-04-11 16:44:37 kgregory Exp $}
 **/

public class CompactGridLayout
        implements LayoutManager2
{
//----------------------------------------------------------------------------
//  Instance data and constructors
//----------------------------------------------------------------------------

    private int     _cols;                  // # columns, set by ctor
    private int     _rows;                  // # rows, set by recalculate()

    private int     _vGap;                  // gap between rows
    private int     _hGap;                  // gap between cols

    private float   _xAlignment;            // horizontal alignment within cell
    private float   _yAlignment;            // vertical alignment within cell

    private int[]   _minRowHeights;         // minimum height of each row
    private int[]   _prfRowHeights;         // preferred height of each row
    private int[]   _minColWidths;          // minimum width of each column
    private int[]   _prfColWidths;          // preferred width of each column

    private int     _preferredHeight;       // these four include container insets
    private int     _preferredWidth;
    private int     _minimumHeight;
    private int     _minimumWidth;


    /** Basic constructor, which allows user to specify the number of columns
     *  and nothing else. Vertical and horizontal gaps both default to 0, and
     *  component alignment defaults to left-horizontal and center-vertical.
     *
     *  @param  cols    Number of columns in the layout.
     */
    public CompactGridLayout( int cols )
    {
        this(cols, 0, 0, 0.0f, 0.5f);
    }


    /** Constructor that allows specification of horizontal and vertical gaps
     *  between components. Alignment defaults to left-horizontal and center-
     *  vertical.
     *
     *  @param  cols    Number of columns in the layout.
     *  @param  hGap    Horizontal gap between components, in pixels.
     *  @param  vGap    Vertical gap between components, in pixels.
     */
    public CompactGridLayout( int cols, int hGap, int vGap )
    {
        this(cols, hGap, vGap, 0.0f, 0.5f);
    }


    /** Constructor that allows specification of horizontal and vertical gaps
     *  between components, as well as component alignment within cells.
     *
     *  @param  cols    Number of columns in the layout.
     *  @param  hGap    Horizontal gap between components, in pixels.
     *  @param  vGap    Vertical gap between components, in pixels.
     *  @param  hAlign  Horizontal alignment of components within cell.
     *  @param  vAlign  Vertical alignment of components within cell.
     */
    public CompactGridLayout( int cols, int hGap, int vGap, float hAlign, float vAlign )
    {
        _cols = cols;
        _hGap = hGap;
        _vGap = vGap;
        _xAlignment = hAlign;
        _yAlignment = vAlign;
    }


//----------------------------------------------------------------------------
//  Public methods
//----------------------------------------------------------------------------

    /** Adds a component to the layout, associating it with the specified
     *  constraints object.
     *
     *  @param  comp    The component to add.
     *  @param  cons    A constraints object to go with this component. Since
     *                  this layout manager does not use constraints, this
     *                  parameter is ignored, and may be <CODE>null</CODE>
     */
    public void addLayoutComponent( Component comp, Object cons )
    {
        // in this revision, we don't keep track of constraints, so don't
        // need to do anything in this method
    }


    /** Adds a component to the layout, associating it with the specified
     *  constraints object.
     *
     *  @deprecated     use <CODE>addLayoutComponent(Component,Object)</CODE>
     *
     *  @param  comp    The component to add.
     *  @param  cons    A constraints object to go with this component. Since
     *                  this layout manager does not use constraints, this
     *                  parameter is ignored, and may be <CODE>null</CODE>
     */
    public void addLayoutComponent( String name, Component comp )
    {
    }


    /** Removes a component from the layout.
     */
    public void removeLayoutComponent(Component comp)
    {
        // we don't need to do anything since we just get the list of
        // components from the container
    }


    /** Invalidates the current cached information for this layout, and
     *  recalculates the row heights and column widths.
     */
    public void invalidateLayout(Container target)
    {
        synchronized (target.getTreeLock())
        {
            _minRowHeights = null;
            _prfRowHeights = null;
            _minColWidths  = null;
            _prfColWidths  = null;
        }
    }


    /** Returns the preferred size of this layout, based on its components.
     */
    public Dimension preferredLayoutSize(Container target)
    {
        synchronized (target.getTreeLock())
        {
            recalculate(target);
            return new Dimension(_preferredWidth, _preferredHeight);
        }
    }


    /** Returns the minimum size of this layout, based on its components.
     */
    public Dimension minimumLayoutSize(Container target)
    {
        synchronized (target.getTreeLock())
        {
            recalculate(target);
            return new Dimension(_minimumWidth, _minimumHeight);
        }
    }


    /** Returns the maximum size of this layout, which is the same as its
     *  preferred size.
     */
    public Dimension maximumLayoutSize(Container target)
    {
        return preferredLayoutSize(target);
    }


    /** Lays out the container.
     */
    public void layoutContainer(Container target)
    {
        synchronized (target.getTreeLock())
        {
            recalculate(target);

            int row = 0;
            int col = 0;
            int x = target.getInsets().left;
            int y = target.getInsets().top;

            int rowHeight = calculateRowHeight(row);

            for (int ii = 0 ; ii < target.getComponentCount() ; ii++)
            {
                Component comp = target.getComponent(ii);
                int compWidth = comp.getPreferredSize().width;
                int compHeight = comp.getPreferredSize().height;

                int colWidth = calculateColWidth(col);

                int xOffset = (int)((colWidth - compWidth) * _xAlignment);
                int yOffset = (int)((rowHeight - compHeight) * _yAlignment);

                comp.setBounds(x, y, compWidth, compHeight);

                x += colWidth + _hGap;;

                if (++col == _cols)
                {
                    col = 0;
                    row++;
                    x = target.getInsets().left;
                    y += rowHeight + _vGap;
                    if (row < _rows)
                        rowHeight = calculateRowHeight(row);
                }
            }
        }
    }


    /** Returns a default horizontal alignment value for this container.
     */
    public float getLayoutAlignmentX(Container target)
    {
        return 0.0f;
    }


    /** Returns a default vertical alignment value for this container.
     */
    public float getLayoutAlignmentY(Container target)
    {
        return 0.0f;
    }


//----------------------------------------------------------------------------
//  Internal methods
//----------------------------------------------------------------------------

    /** Recalculates the row heights and column widths for this layout, based
     *  on the current components. This information is cached, and this method
     *  doesn't actually do the calculation unless the cache is invalid.
     */
    private void recalculate( Container target )
    {
        if (_minRowHeights != null)
            return;

        Component[] comps = target.getComponents();

        int _rows = (comps.length / _cols)
                + (((comps.length % _cols) == 0) ? 0 : 1);

        _minRowHeights   = new int[_rows];
        _prfRowHeights   = new int[_rows];
        _minColWidths    = new int[_cols];
        _prfColWidths    = new int[_cols];

        int row = 0;
        int col = 0;
        for (int ii = 0 ; ii < comps.length ; ii++)
        {
            _minRowHeights[row] = Math.max(_minRowHeights[row],
                    comps[ii].getMinimumSize().height);
            _minColWidths[col]  = Math.max(_minColWidths[col],
                    comps[ii].getMinimumSize().width);
            _prfRowHeights[row] = Math.max(_prfRowHeights[row],
                    comps[ii].getPreferredSize().height);
            _prfColWidths[col]  = Math.max(_prfColWidths[col],
                    comps[ii].getPreferredSize().width);

            if (++col == _cols)
            {
                col = 0;
                row++;
            }
        }

        _minimumHeight = _preferredHeight
                = target.getInsets().top + target.getInsets().bottom;
        _minimumWidth  = _preferredWidth
                = target.getInsets().left + target.getInsets().right;

        for (row = 0 ; row < _rows ; row++)
        {
            _preferredHeight += _prfRowHeights[row];
            _minimumHeight += _minRowHeights[row];
        }

        for (col = 0 ; col < _cols ; col++)
        {
            _preferredWidth += _prfColWidths[col];
            _minimumWidth += _minColWidths[col];
        }

        _preferredHeight += (_rows - 1) * _vGap;
        _preferredWidth  += (_cols - 1) * _hGap;
        _minimumHeight   += (_rows - 1) * _vGap;
        _minimumWidth    += (_cols - 1) * _hGap;
    }


    /** Calculates the height of a row, scaling if necessary for an undersized
     *  layout. This method is only called from <CODE>layoutContainer()</CODE>,
     *  but I want to minimize code clutter in there.
     */
    private int calculateRowHeight( int row )
    {
        return _prfRowHeights[row];
    }


    /** Calculates the width of a column, scaling if necessary for an undersized
     *  layout. As with <CODE>calculateRowHeight()</CODE>, this method is only
     *  called from <CODE>layoutContainer()</CODE>.
     */
    private int calculateColWidth( int col )
    {
        return _prfColWidths[col];
    }
}
