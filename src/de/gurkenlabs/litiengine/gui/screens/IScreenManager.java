package de.gurkenlabs.litiengine.gui.screens;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.function.Consumer;

import de.gurkenlabs.litiengine.graphics.RenderComponent;

/**
 * The screen manager manages all screens of a game. The method
 * renderCurrentScreen is called from the render loop of the game and renders
 * the current screen to the getRenderComponent() of this manager.
 */
public interface IScreenManager {
  public void addScreen(final Screen screen);

  public void displayScreen(Screen screen);

  public void displayScreen(String screenName);

  public Rectangle getBounds();

  public Screen getCurrentScreen();

  public RenderComponent getRenderComponent();

  public Dimension getResolution();
  
  public Point2D getCenter();

  public float getResolutionScale();

  public Point getScreenLocation();

  public String getTitle();

  public void init(int width, int height, boolean fullscreen);

  public boolean isFocusOwner();

  public void onResolutionChanged(Consumer<Dimension> resolutionConsumer);

  public void onScreenChanged(Consumer<Screen> screenConsumer);

  public void setIconImage(Image image);

  public void setTitle(String string);

  public void setResolution(Resolution res);
}
