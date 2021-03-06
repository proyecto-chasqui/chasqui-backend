package chasqui.dao;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

import chasqui.model.Zona;

public interface ZonaDAO {
	
	public void guardar(Zona z);
	public void eliminar(Zona z);
	public List<Zona> buscarZonasBy(Integer idUsuario);
	public Zona ObtenerZonaPorID(final Integer idUsuario, final Integer idZona);
	public List<Zona> obtenerZonas(Integer idVendedor);
	public Zona buscarZonaProxima(Integer idVendedor);
	public Zona obtenerZonaPorId(Integer zonaID);
	public Zona obtenerZonaDePertenenciaDeDireccion(Point punto, Integer idVendedor);
	public Zona obtenerZonaPorNombre(String nombre, Integer idVendedor);
}
