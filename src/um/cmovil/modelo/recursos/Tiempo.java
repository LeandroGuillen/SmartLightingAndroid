package um.cmovil.modelo.recursos;


public class Tiempo {
	private int temperatura;
	private int vientoMedio;
	private int vientoRacha;
	private int precipitaciones;
	private int nubes;
	private int humedad;
	private int presion;
	private long fecha;

	public int getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(int temperatura) {
		this.temperatura = temperatura;
	}

	public int getVientoMedio() {
		return vientoMedio;
	}

	public void setVientoMedio(int vientoMedio) {
		this.vientoMedio = vientoMedio;
	}

	public int getVientoRacha() {
		return vientoRacha;
	}

	public void setVientoRacha(int vientoRacha) {
		this.vientoRacha = vientoRacha;
	}

	public int getPrecipitaciones() {
		return precipitaciones;
	}

	public void setPrecipitaciones(int precipitaciones) {
		this.precipitaciones = precipitaciones;
	}

	public int getNubes() {
		return nubes;
	}

	public void setNubes(int nubes) {
		this.nubes = nubes;
	}

	public int getHumedad() {
		return humedad;
	}

	public void setHumedad(int humedad) {
		this.humedad = humedad;
	}

	public int getPresion() {
		return presion;
	}

	public void setPresion(int presion) {
		this.presion = presion;
	}

	public long getFecha() {
		return fecha;
	}

	public void setFecha(long fecha) {
		this.fecha = fecha;
	}
}
