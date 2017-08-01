package fr.jubil.map.bean;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class HeightMap {
	private String name;
	private int size;
	private int[][] map;

	public HeightMap() {
		this("map", 8);
	}

	public HeightMap(String name) {
		this(name, 8);
	}

	public HeightMap(String name, int size) {
		if (size < 0)
			size = 0;
		else if (size > 12) {
			size = 12;
		}
		this.size = ((int) (Math.pow(2.0D, size) + 1.0D));
		this.name = name;
		generate();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return this.size;
	}

	public int[][] getMap() {
		return this.map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public void generate() {
		Random r = new Random();
		this.map = new int[this.size][this.size];
		int i = this.size - 1;

		this.map[0][0] = r.nextInt(256);
		this.map[(this.size - 1)][0] = r.nextInt(256);
		this.map[0][(this.size - 1)] = r.nextInt(256);
		this.map[(this.size - 1)][(this.size - 1)] = r.nextInt(256);

		while (i > 1) {
			int id = i / 2;
			for (int x = id; x <= this.size - 1; x += i) {
				for (int y = id; y <= this.size - 1; y += i) {
					int moyenne = (this.map[(x - id)][(y - id)]
							+ this.map[(x - id)][(y + id)]
							+ this.map[(x + id)][(y + id)] + this.map[(x + id)][(y - id)]) / 4;
					this.map[x][y] = (moyenne + r.nextInt(2 * id) - id);
					if (this.map[x][y] < 0)
						this.map[x][y] = 0;
					else if (this.map[x][y] > 255) {
						this.map[x][y] = 'ÿ';
					}
				}
			}
			for (int x = 0; x <= this.size - 1; x += id) {
				int decalage;
				if (x % i == 0)
					decalage = id;
				else {
					decalage = 0;
				}

				for (int y = decalage; y <= this.size - 1; y += i) {
					int somme = 0;
					int n = 0;
					if (x >= id) {
						somme += this.map[(x - id)][y];
						n++;
					}
					if (x + id < this.size - 1) {
						somme += this.map[(x + id)][y];
						n++;
					}
					if (y >= id) {
						somme += this.map[x][(y - id)];
						n++;
					}
					if (y + id < this.size - 1) {
						somme += this.map[x][(y + id)];
						n++;
					}

					this.map[x][y] = (somme / n + r.nextInt(2 * id) - id);
					if (this.map[x][y] < 0)
						this.map[x][y] = 0;
					else if (this.map[x][y] > 255) {
						this.map[x][y] = 'ÿ';
					}
				}
			}
			i = id;
		}
	}

	public void switchColor() {
		for (int x = 0; x < this.size; x++)
			for (int y = 0; y < this.size; y++)
				this.map[x][y] *= 256;
	}

	public void cutLayers() {
		for (int x = 0; x < this.size; x++)
			for (int y = 0; y < this.size; y++)
				if (this.map[x][y] < 128)
					this.map[x][y] = 0;
				else
					this.map[x][y] = 'ÿ';
	}

	public void exportAsPNG() {
		exportAsPNG(this.name);
	}

	public void exportAsPNG(String string) {
		BufferedImage bi = new BufferedImage(this.size, this.size, 1);
		for (int x = 0; x < bi.getWidth(); x++) {
			for (int y = 0; y < bi.getHeight(); y++) {
				bi.setRGB(x, y, this.map[x][y]);
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