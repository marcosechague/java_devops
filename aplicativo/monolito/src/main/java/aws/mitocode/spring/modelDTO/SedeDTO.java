package aws.mitocode.spring.modelDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SedeDTO implements Serializable {

	private static final long serialVersionUID = -8686839700916248089L;

	private int id;
	private String nombre;
	private Double latitud;
	private Double longitud;
	private NegocioDTO negocio;
	private List<TipoServicioDTO> tiposServicios = new ArrayList<TipoServicioDTO>();
	
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
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	public NegocioDTO getNegocio() {
		return negocio;
	}
	public void setNegocio(NegocioDTO negocio) {
		this.negocio = negocio;
	}
	public List<TipoServicioDTO> getTiposServicios() {
		return tiposServicios;
	}
	public void setTiposServicios(List<TipoServicioDTO> tiposServicios) {
		this.tiposServicios = tiposServicios;
	}
}
