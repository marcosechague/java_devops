package aws.mitocode.spring.modelDTO;

import java.io.Serializable;

public class NegocioDTO implements Serializable {
	
	private static final long serialVersionUID = 2445247993956960711L;

	private int id;
	private String nombre;
	private TipoNegocioDTO tipoNegocio;

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

	public TipoNegocioDTO getTipoNegocio() {
		return tipoNegocio;
	}

	public void setTipoNegocio(TipoNegocioDTO tipoNegocio) {
		this.tipoNegocio = tipoNegocio;
	}

}
