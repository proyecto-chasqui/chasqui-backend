package chasqui.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import chasqui.aspect.Auditada;
import chasqui.dao.VendedorDAO;
import chasqui.exceptions.VendedorInexistenteException;
import chasqui.model.PreguntaDeConsumo;
import chasqui.model.PuntoDeRetiro;
import chasqui.model.Vendedor;
import chasqui.services.interfaces.VendedorService;

@Auditada
public class VendedorServiceImpl implements VendedorService{

	@Autowired
	VendedorDAO vendedorDAO;
	
	
	@Override
	public List<Vendedor> obtenerVendedores() {
		return vendedorDAO.obtenerVendedores();
	}


	@Override
	public Vendedor obtenerVendedor(String nombreVendedor) throws VendedorInexistenteException {
		Vendedor v = vendedorDAO.obtenerVendedor(nombreVendedor);
		if(v == null){
			throw new VendedorInexistenteException("No existe el vendedor: "+nombreVendedor );
		}
		return v;
	}
	
	@Override
	public Vendedor obtenerVendedorPorId(Integer idVendedor) throws VendedorInexistenteException {
		Vendedor v = vendedorDAO.obtenerVendedorPorId(idVendedor);
		if(v == null){
			throw new VendedorInexistenteException("No existe el vendedor" );
		}
		return v;
	}
	
	@Override
	public List<PuntoDeRetiro> obtenerPuntosDeRetiroDeVendedor(Integer idVendedor){
		return vendedorDAO.obtenerPuntosDeRetiroDeVendedor(idVendedor);
	}
	
	@Override
	public Vendedor obtenerVendedorPorNombreCorto(String nombreCorto) throws VendedorInexistenteException {
		Vendedor v = vendedorDAO.obtenerVendedorPorNombreCorto(nombreCorto);
		if(v == null){
			throw new VendedorInexistenteException("No existe el vendedor con el nombre corto: "+ nombreCorto );
		}
		return v;
	}
	
	
	public VendedorDAO getVendedorDAO() {
		return vendedorDAO;
	}
	public void setVendedorDAO(VendedorDAO vendedorDAO) {
		this.vendedorDAO = vendedorDAO;
	}	
	@Override
	public List<PreguntaDeConsumo> obtenerPreguntasIndividuales(Integer idVendedor){
		return this.vendedorDAO.obtenerPreguntasDeConsumoIndividuales(idVendedor);
	}
	
	@Override
	public List<PreguntaDeConsumo> obtenerPreguntasColectivas(Integer idVendedor){
		return this.vendedorDAO.obtenerPreguntasDeConsumoColectivas(idVendedor);
	}


	@Override
	public List<Vendedor> obtenerVendedoresConTags(String nombre, List<Integer> idsTagsTipoOrganizacion,
			List<Integer> idsTagsTipoProducto, List<Integer> idsTagsZonaDeCobertura, boolean entregaADomicilio, boolean usaPuntoDeRetiro, boolean usaEstrategiaGrupos, boolean usaEstrategiaIndividual, boolean usaEstrategiaNodos) {
		
		return this.vendedorDAO.obtenerVendedoresConTags(nombre,idsTagsTipoOrganizacion,
				idsTagsTipoProducto, idsTagsZonaDeCobertura, entregaADomicilio, usaPuntoDeRetiro, usaEstrategiaGrupos, usaEstrategiaIndividual, usaEstrategiaNodos);
	}

}
