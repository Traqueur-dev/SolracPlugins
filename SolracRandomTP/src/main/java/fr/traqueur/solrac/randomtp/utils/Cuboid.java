package fr.traqueur.solrac.randomtp.utils;

public class Cuboid {
	private String worldName;
	private int x1;
	private int x2;
	private int y1;
	private int y2;
	private int z1;
	private int z2;
	public Cuboid(String string, int n, int n2, int n3, int n4, int n5, int n6) {
		this.worldName = string;
		this.x1 = Math.min(n, n4);
		this.x2 = Math.max(n, n4);
		this.y1 = Math.min(n2, n5);
		this.y2 = Math.max(n2, n5);
		this.z1 = Math.min(n3, n6);
		this.z2 = Math.max(n3, n6);
	}

	public boolean contains(int n, int n2, int n3) {
		if (n >= this.x1 && n <= this.x2 && n2 >= this.y1 && n2 <= this.y2 && n3 >= this.z1 && n3 <= this.z2) {
			return true;
		}
		return false;
	}
}