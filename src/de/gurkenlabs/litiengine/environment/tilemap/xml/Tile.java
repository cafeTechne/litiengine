package de.gurkenlabs.litiengine.environment.tilemap.xml;

import java.awt.Point;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.gurkenlabs.litiengine.environment.tilemap.ITerrain;
import de.gurkenlabs.litiengine.environment.tilemap.ITile;
import de.gurkenlabs.litiengine.environment.tilemap.ITileAnimation;
import de.gurkenlabs.litiengine.util.ArrayUtilities;

@XmlRootElement(name = "tile")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tile extends CustomPropertyProvider implements ITile, Serializable {
  public static final int NONE = 0;
  public static final Tile EMPTY = new Tile(NONE);
  protected static final long FLIPPED_HORIZONTALLY_FLAG = 0xFFFFFFFF80000000L;
  protected static final long FLIPPED_VERTICALLY_FLAG = 0xFFFFFFFF40000000L;
  protected static final long FLIPPED_DIAGONALLY_FLAG = 0xFFFFFFFF20000000L;

  protected static final long FLIPPED_HORIZONTALLY_FLAG_CSV = 0x80000000L;
  protected static final long FLIPPED_VERTICALLY_FLAG_CSV = 0x40000000L;
  protected static final long FLIPPED_DIAGONALLY_FLAG_CSV = 0x20000000L;

  private static final long serialVersionUID = -7597673646108642906L;

  @XmlAttribute
  private Integer gid;

  @XmlAttribute
  private Integer id;

  @XmlAttribute
  private String terrain;

  @XmlElement(required = false)
  private Animation animation;

  private transient Point tileCoordinate;

  private transient ITerrain[] terrains;

  private transient ITile customPropertySource;

  private transient boolean flippedDiagonally;
  private transient boolean flippedHorizontally;
  private transient boolean flippedVertically;
  private transient boolean flipped;

  public Tile() {
  }

  public Tile(int gid) {
    this.gid = gid;
  }

  public Tile(long gidBitmask, boolean csv) {
    // Clear the flags
    long tileId = gidBitmask;
    if (csv) {
      tileId &= ~(FLIPPED_HORIZONTALLY_FLAG_CSV | FLIPPED_VERTICALLY_FLAG_CSV | FLIPPED_DIAGONALLY_FLAG_CSV);
      this.flippedDiagonally = (gidBitmask & FLIPPED_DIAGONALLY_FLAG_CSV) == FLIPPED_DIAGONALLY_FLAG_CSV;
      this.flippedHorizontally = (gidBitmask & FLIPPED_HORIZONTALLY_FLAG_CSV) == FLIPPED_HORIZONTALLY_FLAG_CSV;
      this.flippedVertically = (gidBitmask & FLIPPED_VERTICALLY_FLAG_CSV) == FLIPPED_VERTICALLY_FLAG_CSV;
    } else {
      tileId &= ~(FLIPPED_HORIZONTALLY_FLAG | FLIPPED_VERTICALLY_FLAG | FLIPPED_DIAGONALLY_FLAG);
      this.flippedDiagonally = (gidBitmask & FLIPPED_DIAGONALLY_FLAG) == FLIPPED_DIAGONALLY_FLAG;
      this.flippedHorizontally = (gidBitmask & FLIPPED_HORIZONTALLY_FLAG) == FLIPPED_HORIZONTALLY_FLAG;
      this.flippedVertically = (gidBitmask & FLIPPED_VERTICALLY_FLAG) == FLIPPED_VERTICALLY_FLAG;
    }

    this.flipped = this.isFlippedDiagonally() || this.isFlippedHorizontally() || this.isFlippedVertically();
    this.gid = (int) tileId;
  }

  @Override
  public boolean hasCustomProperty(String name) {
    return customPropertySource == null ? super.hasCustomProperty(name) : customPropertySource.hasCustomProperty(name);
  }

  @Override
  public List<Property> getCustomProperties() {
    return customPropertySource == null ? super.getCustomProperties() : customPropertySource.getCustomProperties();
  }

  @Override
  public void setCustomProperties(List<Property> props) {
    if (customPropertySource == null) {
      super.setCustomProperties(props);
    } else {
      customPropertySource.setCustomProperties(props);
    }
  }

  @Override
  public String getStringProperty(String name, String defaultValue) {
    return customPropertySource == null ? super.getStringProperty(name, defaultValue) : customPropertySource.getStringProperty(name, defaultValue);
  }

  @Override
  public void setProperty(String name, String value) {
    if (customPropertySource == null) {
      super.setProperty(name, value);
    } else {
      customPropertySource.setProperty(name, value);
    }
  }

  @Override
  public boolean isFlippedDiagonally() {
    return this.flippedDiagonally;
  }

  @Override
  public boolean isFlippedHorizontally() {
    return this.flippedHorizontally;
  }

  @Override
  public boolean isFlippedVertically() {
    return this.flippedVertically;
  }

  @Override
  public boolean isFlipped() {
    return this.flipped;
  }

  @Override
  public int getGridId() {
    if (this.gid == null) {
      return 0;
    }

    return this.gid;
  }

  @Override
  public Point getTileCoordinate() {
    return this.tileCoordinate;
  }

  /**
   * Sets the tile coordinate.
   *
   * @param tileCoordinate
   *          the new tile coordinate
   */
  public void setTileCoordinate(final Point tileCoordinate) {
    this.tileCoordinate = tileCoordinate;
  }

  @Override
  public int getId() {
    if (this.id == null) {
      return 0;
    }

    return this.id;
  }

  @Override
  public ITerrain[] getTerrain() {
    return customPropertySource == null ? this.terrains : customPropertySource.getTerrain();
  }

  @Override
  public ITileAnimation getAnimation() {
    return customPropertySource == null ? this.animation : customPropertySource.getAnimation();
  }

  @Override
  public String toString() {
    for (int i = 0; i < this.getTerrainIds().length; i++) {
      if (this.getTerrainIds()[i] != Terrain.NONE) {
        return this.getGridId() + " (" + Arrays.toString(this.getTerrainIds()) + ")";
      }
    }

    return this.getGridId() + "";
  }

  protected int[] getTerrainIds() {
    int[] terrainIds = new int[] { Terrain.NONE, Terrain.NONE, Terrain.NONE, Terrain.NONE };
    if (this.terrain == null || this.terrain.isEmpty()) {
      return terrainIds;
    }

    int[] ids = ArrayUtilities.getIntegerArray(this.terrain);
    if (ids.length != 4) {
      return terrainIds;
    } else {
      terrainIds = ids;
    }

    return terrainIds;
  }

  protected void setTerrains(ITerrain[] terrains) {
    this.terrains = terrains;
  }

  protected void setCustomPropertySource(ITile source) {
    customPropertySource = source;
  }

  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller u, Object parent) {
    if (this.gid != null && this.gid == 0) {
      this.gid = null;
    }

    if (this.id != null && this.id == 0) {
      this.id = null;
    }
  }
}
