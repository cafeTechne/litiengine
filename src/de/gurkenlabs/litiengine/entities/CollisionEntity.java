package de.gurkenlabs.litiengine.entities;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import de.gurkenlabs.litiengine.annotation.CollisionInfo;

@CollisionInfo(collision = true)
public abstract class CollisionEntity extends Entity implements ICollisionEntity {
  private boolean collision;
  private final float collisionBoxHeightFactor;

  private final float collisionBoxWidthFactor;

  protected CollisionEntity() {
    super();
    final CollisionInfo info = this.getClass().getAnnotation(CollisionInfo.class);
    this.collisionBoxWidthFactor = info.collisionBoxWidthFactor();
    this.collisionBoxHeightFactor = info.collisionBoxHeightFactor();
    this.collision = info.collision();
  }

  /**
   * Gets the collision box.
   *
   * @return the collision box
   */
  @Override
  public Rectangle2D getCollisionBox() {
    return this.getCollisionBox(this.getLocation());
  }

  /**
   * Gets the collision box.
   *
   * @param location
   *          the location
   * @return the collision box
   */
  @Override
  public Rectangle2D getCollisionBox(final Point2D location) {
    final float collisionBoxWidth = this.getWidth() * this.collisionBoxWidthFactor;
    final float collisionBoxHeight = this.getHeight() * this.collisionBoxHeightFactor;
    return new Rectangle2D.Double(location.getX() + this.getWidth() / 2 - collisionBoxWidth / 2, location.getY() + this.getHeight() - collisionBoxHeight, collisionBoxWidth, collisionBoxHeight);
  }

  /**
   * Checks for collision.
   *
   * @return true, if successful
   */
  @Override
  public boolean hasCollision() {
    return this.collision;
  }

  /**
   * Sets the collision.
   *
   * @param collision
   *          the new collision
   */
  @Override
  public void setCollision(final boolean collision) {
    this.collision = collision;
  }
}
