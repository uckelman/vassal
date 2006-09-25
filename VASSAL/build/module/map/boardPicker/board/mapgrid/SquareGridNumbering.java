/*
 * $Id$
 *
 * Copyright (c) 2000-2003 by Rodney Kinney
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License (LGPL) as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, copies are available
 * at http://www.opensource.org.
 */
/*
 * Created by IntelliJ IDEA.
 * User: rkinney
 * Date: Jul 24, 2002
 * Time: 10:31:09 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package VASSAL.build.module.map.boardPicker.board.mapgrid;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;

import javax.swing.JComponent;
import javax.swing.JPanel;

import VASSAL.build.Buildable;
import VASSAL.build.module.map.boardPicker.board.SquareGrid;
import VASSAL.counters.Labeler;

public class SquareGridNumbering extends RegularGridNumbering {

  private SquareGrid grid;

  public void addTo(Buildable parent) {
    grid = (SquareGrid) parent;
    grid.setGridNumbering(this);
  }

  protected JComponent getGridVisualizer() {
    if (visualizer == null) {
      visualizer = new JPanel() {
        public void paint(Graphics g) {
          g.clearRect(0, 0, getWidth(), getHeight());
          Rectangle bounds = new Rectangle(0, 0, getWidth(), getHeight());
          grid.forceDraw(g, bounds, bounds, 1.0, false);
          forceDraw(g, bounds, bounds, 1.0, false);
        }

        public Dimension getPreferredSize() {
          return new Dimension(3 * (int) grid.getDx(), 3 * (int) grid.getDy());
        }
      };
    }
    return visualizer;
  }

  /** Draw the numbering, if visible */
  public void draw(Graphics g, Rectangle bounds, Rectangle visibleRect, double scale, boolean reversed) {
    if (visible) {
      forceDraw(g, bounds, visibleRect, scale, reversed);
    }
  }

  /** Draw the numbering, even if not visible */
  public void forceDraw(Graphics g, Rectangle bounds, Rectangle visibleRect, double scale, boolean reversed) {
    int size = (int) (scale * fontSize + 0.5);
    if (size < 5 || !bounds.intersects(visibleRect)) {
      return;
    }
    Rectangle region = bounds.intersection(visibleRect);
    Shape oldClip = g.getClip();
    if (oldClip != null) {
      Area clipArea = new Area(oldClip);
      clipArea.intersect(new Area(region));
      g.setClip(clipArea);
    }

    double deltaX = scale * grid.getDx();
    double deltaY = scale * grid.getDy();

    Point centerPoint = null;
    Graphics2D g2d = (Graphics2D) g;
    double radians = 0;
    if (rotateTextDegrees != 0) {
      radians = Math.toRadians(rotateTextDegrees);
      g2d.rotate(radians);
    }

    int minCol = reversed ? (int) Math.ceil((bounds.x - scale * grid.getOrigin().x + bounds.width - region.x) / deltaX)
        : (int) Math.floor((region.x - bounds.x - scale * grid.getOrigin().x) / deltaX);
    double xmin = reversed ? bounds.x - scale * grid.getOrigin().x + bounds.width - deltaX * minCol
        : bounds.x + scale * grid.getOrigin().x + deltaX * minCol;
    double xmax = region.x + region.width + deltaX;
    int minRow = reversed ? (int) Math.ceil((bounds.y - scale * grid.getOrigin().y + bounds.height - region.y) / deltaY)
        : (int) Math.floor((region.y - bounds.y - scale * grid.getOrigin().y) / deltaY);
    double ymin = reversed ? bounds.y - scale * grid.getOrigin().y + bounds.height - deltaY * minRow
        : bounds.y + scale * grid.getOrigin().y + deltaY * minRow;
    double ymax = region.y + region.height + deltaY;

    Font f = new Font("Dialog", Font.PLAIN, size);
    int column = minCol;
    for (double x = xmin; x < xmax; x += deltaX, column += reversed ? -1 : 1) {
      int printRow, printColumn;
      int row = minRow;
      for (double y = ymin; y < ymax; y += deltaY, row += reversed ? -1 : 1) {
        printRow = row;
        printColumn = column;
        if (vDescending) {
          printRow = getMaxRows() - row;
        }
        if (hDescending) {
          printColumn = getMaxColumns() - column;
        }

        // When rotating text, keep basic label position as in center along edge
        int newX = 0, newY = 0;
        switch (rotateTextDegrees) {
          case 90:
            newX = (int) (x + deltaX / 2);
            newY = (int) y;
            break;
          case 180:
            newX = (int) x;
            newY = (int) (y + deltaY / 2);
            break;
          case 270:
            newX = (int) (x - deltaX / 2);
            newY = (int) y;
            break;
          default :
            newX = (int) x;
            newY = (int) (y - deltaY / 2);
            break;
        }

        centerPoint = offsetLabelCenter(newX, newY, scale);
        Labeler.drawLabel(g, getName(printRow, printColumn),
                          centerPoint.x,
                          centerPoint.y,
                          f,
                          Labeler.CENTER,
                          Labeler.TOP, color, null, null);
      }
    }
    if (rotateTextDegrees != 0) {
      g2d.rotate(-radians);
    }
    g.setClip(oldClip);
  }

  public int getColumn(Point p) {
    int col = (int) Math.floor((p.x - grid.getOrigin().x) / grid.getDx() + 0.5);
    if (hDescending) {
      return (getMaxColumns() - col);
    }
    else {
      return col;
    }
  }

  public int getRow(Point p) {
    int row = (int) ((p.y - grid.getOrigin().y) / grid.getDy() + 0.5);
    if (vDescending) {
      return (getMaxRows() - row);
    }
    else {
      return row;
    }
  }


  public void removeFrom(Buildable parent) {
    grid.setGridNumbering(null);
  }

  protected int getMaxRows() {
    return (int) (grid.getContainer().getSize().height / grid.getDy() + 0.5);
  }

  protected int getMaxColumns() {
    return (int) (grid.getContainer().getSize().width / grid.getDx() + 0.5);
  }
}
