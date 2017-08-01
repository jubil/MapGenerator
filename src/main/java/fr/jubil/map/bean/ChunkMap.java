package fr.jubil.map.bean;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ChunkMap
{
  private String name;
  private int size;
  private HeightMap humidite;
  private HeightMap altitude;
  private HeightMap temperature;

  public ChunkMap()
  {
    this("chunk");
  }

  public ChunkMap(String name) {
    this(name, 8);
  }

  public ChunkMap(String name, int size) {
    this(name, new HeightMap("humidite", size), new HeightMap("altitude", size), new HeightMap("temperature", size));
  }

  public ChunkMap(String name, HeightMap humidite, HeightMap altitude, HeightMap temperature) {
    this.name = name;

    this.humidite = humidite;
    this.altitude = altitude;
    this.temperature = temperature;

    this.size = humidite.getSize();
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public HeightMap getHumidite() {
    return this.humidite;
  }

  public void setHumidite(HeightMap humidite) {
    this.humidite = humidite;
  }

  public HeightMap getAltitude() {
    return this.altitude;
  }

  public void setAltitude(HeightMap altitude) {
    this.altitude = altitude;
  }

  public HeightMap getTemperature() {
    return this.temperature;
  }

  public void setTemperature(HeightMap temperature) {
    this.temperature = temperature;
  }

  public int getSize() {
    return this.size;
  }

  public int[][] toMap() {
    int[][] map = new int[this.size][this.size];

    for (int x = 0; x < this.size; x++) {
      for (int y = 0; y < this.size; y++) {
        map[x][y] = (this.humidite.getMap()[x][y] + this.altitude.getMap()[x][y] * 256 + this.temperature.getMap()[x][y] * 65536);
      }
    }
    return map;
  }

  public void exportAsPNG() {
    exportAsPNG(this.name);
  }

  public void exportAsPNG(String string) {
    BufferedImage bi = new BufferedImage(this.size, this.size, 
      1);
    for (int x = 0; x < bi.getWidth(); x++) {
      for (int y = 0; y < bi.getHeight(); y++) {
        bi.setRGB(x, y, toMap()[x][y]);
      }
    }
    new File("maps").mkdirs();
    File outputfile = new File("maps/" + string + ".png");
    try {
      ImageIO.write(bi, "png", outputfile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}