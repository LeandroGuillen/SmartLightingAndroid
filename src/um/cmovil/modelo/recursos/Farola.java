package um.cmovil.modelo.recursos;

public class Farola {
	private boolean encendida;
	private Integer dim;
	private Integer distancia;
	private String nombre;
	private static int contador = 0;

	public Farola() {
		this(false, 0);
	}

	public Farola(boolean encendida) {
		this(false, 0);
	}

	public Farola(boolean encendida, int dim) {
		setEncendida(encendida);
		setDim(dim);
		setNombre("farola" + (contador++));
		setDistancia(0);
	}

	public boolean isEncendida() {
		return encendida;
	}

	private void setEncendida(boolean encendida) {
		this.encendida = encendida;
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
}
