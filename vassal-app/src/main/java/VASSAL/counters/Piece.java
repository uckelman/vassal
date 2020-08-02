package VASSAL.counters;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import javax.swing.KeyStroke;

import VASSAL.build.module.Map;
import VASSAL.build.module.properties.PropertyNameSource;
import VASSAL.command.Command;
import VASSAL.property.PersistentPropertyContainer;

public class Piece implements GamePiece,
                              StateMergeable,
                              PropertyNameSource,
                              PersistentPropertyContainer,
                              PropertyExporter {

  private final GamePiece inner;

  public Piece(GamePiece inner) {
    this.inner = inner;
    System.out.println("Piece ctor!");
  }

  public GamePiece getInner() {
    return inner;
  }

  //
  // GamePiece
  //

  @Override
  public void setMap(Map map) {
    inner.setMap(map);
  }

  @Override
  public Map getMap() {
    return inner.getMap();
  }

  @Override
  public void draw(Graphics g, int x, int y, Component obs, double zoom) {
    inner.draw(g, x, y, obs, zoom);
  }

  @Override
  public Point getPosition() {
    return inner.getPosition();
  }

  @Override
  public void setPosition(Point p) {
    inner.setPosition(p);
  }

  @Override
  public Rectangle boundingBox() {
    return inner.boundingBox();
  }

  @Override
  public Shape getShape() {
    return inner.getShape();
  }

  @Override
  public Stack getParent() {
    return inner.getParent();
  }

  @Override
  public void setParent(Stack s) {
    inner.setParent(s);
  }

  @Override
  public Command keyEvent(KeyStroke stroke) {
    return inner.keyEvent(stroke);
  }

  @Override
  public String getName() {
    return inner.getName();
  }

  @Override
  public String getLocalizedName() {
    return inner.getLocalizedName();
  }

  @Override
  public String getId() {
    return inner.getId();
  }

  @Override
  public void setId(String id) {
    inner.setId(id);
  }

  @Override
  public String getType() {
    return inner.getType();
  }

  @Override
  public String getState() {
    return inner.getState();
  }

  @Override
  public void setState(String newState) {
    inner.setState(newState);
  }

  @Override
  public void setProperty(Object key, Object val) {
    inner.setProperty(key, val);
  }

  @Override
  public Object getProperty(Object key) {
    return inner.getProperty(key);
  }

  //
  // PropertySource
  //

  @Override
  public Object getLocalizedProperty(Object key) {
    return inner.getLocalizedProperty(key);
  }

  //
  // StateMergeable
  //

  @Override
  public void mergeState(String newState, String oldState) {
    if (inner instanceof StateMergeable) {
      ((StateMergeable) inner).mergeState(newState, oldState);
    }
    else {
      inner.setState(newState);
    }
  }

  //
  // PropertyNameSource
  //
  
  @Override
  public List<String> getPropertyNames() {
    if (inner instanceof PropertyNameSource) {
      return ((PropertyNameSource) inner).getPropertyNames();
    }
    return new ArrayList<>();
  }

  //
  // PersistentPropertyContainer
  //

  @Override
  public Command setPersistentProperty(Object key, Object val) {
    if (inner instanceof PersistentPropertyContainer) {
      return ((PersistentPropertyContainer) inner).setPersistentProperty(key, val);
    }
    return null;
  }

  @Override
  public Object getPersistentProperty(Object key) {
    return inner.getProperty(key);
  }
}
