package Modelo;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Coleccion implements Serializable {
	int id;
	String nombre;
	BufferedImage img;
	
	public Coleccion() {
		super();
	}

	public Coleccion(int id, String nombre, BufferedImage img) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.img = img;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return "Coleccion [id=" + id + ", nombre=" + nombre + ", img=" + img + "]";
	}
	
}
