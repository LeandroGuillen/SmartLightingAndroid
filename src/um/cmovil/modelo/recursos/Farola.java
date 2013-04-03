package um.cmovil.modelo.recursos;

public class Farola {
	private boolean encendida;
	private int dim;

	public Farola() {
		this(false, 0);
	}

	public Farola(boolean encendida) {
		this(false, 0);
	}

	public Farola(boolean encendida, int dim) {
		setEncendida(encendida);
		setDim(dim);
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

	public int getDim() {
		return dim;
	}

	public void setDim(int dim) {
		this.dim = dim;
	}
}
