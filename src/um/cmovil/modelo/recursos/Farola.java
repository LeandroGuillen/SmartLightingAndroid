package um.cmovil.modelo.recursos;

import com.google.android.maps.GeoPoint;

public class Farola {
	private boolean encendida;
	private Integer dim;
	private Integer distancia;
	private String nombre;
	private static int contador = 0;
	private GeoPoint geoPoint;

	public Farola() {
		this("farola__" + (contador++), false, 0);
	}

	public Farola(String nombre) {
		this(nombre, false, 0);
	}

	public Farola(String nombre, boolean encendida, int dim) {
		setEncendida(encendida);
		setDim(dim);
		setNombre(nombre);
		setDistancia(0);
	}

	public boolean isEncendida() {
		return encendida;
	}

	public void apagar() {
		setEncendida(false);
	}

	public void encender() {
		setEncendida(true);
	}

	public void toggle() {
		if (isEncendida())
			apagar();
		else
			encender();
	}

	public void setEncendida(boolean e) {
		encendida = e;
	}

	public Integer getDim() {
		return dim;
	}

	public void setDim(int dim) {
		this.dim = dim;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public Integer getDistancia() {
		return distancia;
	}

	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}

	@Override
	public String toString() {
		return getNombre() + " - encendida:" + isEncendida();
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}
}
