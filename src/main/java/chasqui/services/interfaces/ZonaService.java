package chasqui.services.interfaces;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Point;

import chasqui.model.Zona;

public interface ZonaService {
	
	@Transactional
	public void guardar(Zona z);
	@Transactional
	public List<Zona>buscarZonasBy(Integer idUsuario);
	@Transactional
	public void borrar(Zona z);
	@Transactional
	public List<Zona> obtenerZonas(Integer IdVendedor);
	@Transactional
	public Zona buscarZonaProxima(Integer idVendedor);
	@Transactional
	public Zona obtenerZonaPorId(Integer zonaID);
	@Transactional
	public Zona obtenerZonaDePertenenciaDeDireccion(final Point punto, final Integer idVendedor);
	@Transactional
	public void eliminarZonas(List<Zona> obtenerZonas);

}
