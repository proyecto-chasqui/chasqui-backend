package chasqui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Vendedor extends Usuario{

	
	private Integer montoMinimoPedido;
	private String nombre;
	private String nombreCorto;
	private String msjCierrePedido;
	private Integer distanciaCompraColectiva;
	private String mapaZonas;
	private List<Categoria> categorias;
	private List<Zona> zonas;
	private List<Fabricante> fabricantes;
	private EstrategiasDeComercializacion estrategiasUtilizadas;
	@Deprecated
	private List<IEstrategiaComercializacion> estrategiasPermitidas;
	private List<PuntoDeRetiro> puntosDeRetiro;
	private String url;
	private List<PreguntaDeConsumo> preguntasDePedidosIndividuales;
	private List<PreguntaDeConsumo> preguntasDePedidosColectivos;
	private Integer tiempoVencimientoPedidos;
	private DataMultimedia dataMultimedia;
	private List<TagZonaDeCobertura> tagsZonaCobertura;
	private List<TagTipoProducto> tagsTipoProducto;
	private List<TagTipoOrganizacion> tagsTipoOrganizacion;
	private List<TagEvento> tagsEvento;
	private boolean visibleEnMulticatalogo;
	private boolean ventasHabilitadas;
	private String mensajeVentasDeshabilitadas;
	
	//GETs & SETs	

	public Vendedor(String nombre,String nombreCorto, String username, String email, String pwd, String urlBase) {
		this.setEstrategiasUtilizadas(new EstrategiasDeComercializacion());
		this.setUsername(username);
		this.setEmail(email);
		this.setNombre(nombre);
		this.setNombreCorto(nombreCorto);
		this.setPassword(pwd);
		this.setIsRoot(false);
		this.setUrl(urlBase);
		this.setMontoMinimoPedido(0);
		this.setTiempoVencimientoPedidos(0);
		this.setVisibleEnMulticatalogo(false);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreCorto() {
		return nombreCorto;
	}

	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	public Vendedor() {
		categorias = new ArrayList<Categoria>();
		fabricantes = new ArrayList<Fabricante>();
	}

	public Integer getMontoMinimoPedido() {
		return montoMinimoPedido;
	}

	public void setMontoMinimoPedido(Integer montoMinimoPedido) {
		this.montoMinimoPedido = montoMinimoPedido;
	}

//	public DateTime getFechaCierrePedido() {
//		return fechaCierrePedido;
//	}
//
//	public void setFechaCierrePedido(DateTime fechaCierrePedido) {
//		this.fechaCierrePedido = fechaCierrePedido;
//	}

	public String getMsjCierrePedido() {
		return msjCierrePedido;
	}

	public void setMsjCierrePedido(String msjCierrePedido) {
		this.msjCierrePedido = msjCierrePedido;
	}

	public Integer getDistanciaCompraColectiva() {
		return distanciaCompraColectiva;
	}

	public void setDistanciaCompraColectiva(Integer distanciaCompraColectiva) {
		this.distanciaCompraColectiva = distanciaCompraColectiva;
	}

	public String getMapaZonas() {
		return mapaZonas;
	}

	public void setMapaZonas(String mapaZonas) {
		this.mapaZonas = mapaZonas;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public List<Fabricante> getFabricantes() {
		return fabricantes;
	}

	public void setFabricantes(List<Fabricante> fabricantes) {
		this.fabricantes = fabricantes;
	}

	public List<IEstrategiaComercializacion> getEstrategiasPermitidas() {
		return estrategiasPermitidas;
	}

	public void setEstrategiasPermitidas(List<IEstrategiaComercializacion> estrategiasPermitidas) {
		this.estrategiasPermitidas = estrategiasPermitidas;
	}


	//METHODS

	public boolean contieneProductor(String nombreProductor){
		for(Fabricante f : fabricantes ){
			if( f.getNombre().equals(nombreProductor)){
				return true;
			}
		}
		return false;
	}

	public void agregarProductor(Fabricante f) {
		fabricantes.add(f);
	}
	
	public void eliminarProductor (Fabricante f) {
		fabricantes.remove(f);
	}
	
	
	public void agregarCategoria (Categoria c){
		categorias.add(c);
	}
	
	public void eliminarCategoria (Categoria c){
		categorias.remove(c);
	}
	
	public List<Producto> obtenerProductos(){
		List<Producto>p = new ArrayList<Producto>();
		for(Categoria c :categorias){
			p.addAll(c.getProductos());
		}
		return p;
	}
	
	
	public List<Producto> getProductos(){
		List<Producto>p = new ArrayList<Producto>();
		for(Categoria c :categorias){
			p.addAll(c.getProductos());
		}
		return p;
	}

	public boolean contieneCategoria(String value) {
		for(Categoria c : categorias){
			if(c.getNombre().equalsIgnoreCase(value)){
				return true;
			}
		}
		return false;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String uRL) {
		url = uRL;
  }
	
	private Variante obtenerVarianteConId(Integer id){
		Variante v = null;
		for(Producto p : this.getProductos()){
			for(Variante va : p.getVariantes()){
				if(va.getId().equals(id)){
					v = va;
				}
			}
		}
		return v;
	}
	
	public void descontarStockYReserva(Pedido p) {
		for(ProductoPedido pp : p.getProductosEnPedido()){
			Variante v = this.obtenerVarianteConId(pp.getIdVariante());
			v.setStock(v.getStock() - pp.getCantidad());
			if((v.getCantidadReservada() - pp.getCantidad()) < 0) {
				v.setCantidadReservada(0);
			}else {
				v.setCantidadReservada(v.getCantidadReservada() - pp.getCantidad());
			}
		}
	}

	public List<Zona> getZonas() {
		return zonas;
	}

	public void setZonas(List<Zona> zonas) {
		this.zonas = zonas;
	}

	public Collection<? extends Producto> obtenerProductosDelFabricante(Integer fabricanteSeleccionadoId, String codigoProducto, Boolean destacado, Boolean visibilidad, Integer stock, String nombreDeProducto) {
		List<Producto>p = new ArrayList<Producto>();
		for(Fabricante f :fabricantes){
			p.addAll(f.getProductos());
		}
		p = (List<Producto>) this.obtenerProductosConCodigo(p,codigoProducto);
		p = (List<Producto>) this.obtenerProductosConIdFabricante(p, fabricanteSeleccionadoId);
		p = (List<Producto>) this.obtenerProductosDestacados(p, destacado);
		p = (List<Producto>) this.obtenerProductosHabilitadosDeshabilitados(p, visibilidad);
		p = (List<Producto>) this.obtenerStock(p, stock);
		p = (List<Producto>) this.obtenerProductosPorNombre(p, nombreDeProducto);
		
		return p;
	}

	private Collection<? extends Producto> obtenerProductosConCodigo(Collection <? extends Producto> lista, String codigoProducto) {
		List<Producto> productosResultado = new ArrayList<Producto>();
		if(codigoProducto != null) {
			for(Producto p: lista) {
				if(p.getVariantes().get(0).getCodigo().matches("(?i).*" + codigoProducto+ ".*")) {
					productosResultado.add(p);
				}
			}
		}else {
			productosResultado.addAll(lista);
		}
		return productosResultado;
	}
	
	private Collection<? extends Producto> obtenerProductosConIdFabricante(Collection <? extends Producto> lista, Integer idFabricante) {
		List<Producto> productosResultado = new ArrayList<Producto>();
		if(idFabricante != null) {
			for(Producto p: lista) {
				if(p.getFabricante().getId().equals(idFabricante)) {
					productosResultado.add(p);
				}
			}
		}else {
			productosResultado.addAll(lista);
		}
		return productosResultado;
	}
	
	private Collection<? extends Producto> obtenerProductosHabilitadosDeshabilitados(Collection <? extends Producto> lista, Boolean visibilidad) {
		List<Producto> productosResultado = new ArrayList<Producto>();
		if(visibilidad != null) {
			for(Producto p: lista) {
				if(p.isOcultado() == !visibilidad) {
					productosResultado.add(p);
				}
			}
		}else {
			productosResultado.addAll(lista);
		}
		return productosResultado;
	}
	
	private Collection<? extends Producto> obtenerProductosDestacados(Collection <? extends Producto> lista, Boolean destacado) {
		List<Producto> productosResultado = new ArrayList<Producto>();
		if(destacado != null) {
			for(Producto p: lista) {
				if(p.getVariantes().get(0).getDestacado() == destacado) {
					productosResultado.add(p);
				}
			}
		}else {
			productosResultado.addAll(lista);
		}
		return productosResultado;
	}
	
	private List<Producto> obtenerStock(List<Producto> lista, Integer stock) {
		List<Producto> productosResultado = new ArrayList<Producto>();
		if(stock != null && stock >= 0) {
			for(Producto p: lista) {
				if(p.getVariantes().get(0).getStock() < stock) {
					productosResultado.add(p);
				}
			}
		}else {
			productosResultado.addAll(lista);
		}
		return productosResultado;
	}
	
	private Collection<? extends Producto> obtenerProductosPorNombre(Collection <? extends Producto> lista, String nombreProducto) {
		List<Producto> productosResultado = new ArrayList<Producto>();
		if(nombreProducto != null) {
			for(Producto p: lista) {
				if(p.getVariantes().get(0).getProducto().getNombre().matches("(?i).*" + nombreProducto+ ".*")) {
					productosResultado.add(p);
				}
			}
		}else {
			productosResultado.addAll(lista);
		}
		return productosResultado;
	}
	
	public EstrategiasDeComercializacion getEstrategiasUtilizadas() {
		if(this.estrategiasUtilizadas == null){
		   this.estrategiasUtilizadas = new EstrategiasDeComercializacion();
		   this.estrategiasUtilizadas.Inicializar();
		}
		return estrategiasUtilizadas;
	}

	public void setEstrategiasUtilizadas(EstrategiasDeComercializacion estrategias) {
		this.estrategiasUtilizadas = estrategias;
	}

	public List<PuntoDeRetiro> getPuntosDeRetiro() {
		return puntosDeRetiro;
	}

	public void setPuntosDeRetiro(List<PuntoDeRetiro> puntosDeRetiro) {
		this.puntosDeRetiro = puntosDeRetiro;
	}
	
	public void agregarPuntoDeRetiro(PuntoDeRetiro puntoderetiro) {
		//escanear que no haya repetidos
		this.puntosDeRetiro.add(puntoderetiro);
	}
	
	public boolean existePuntoDeRetiro(PuntoDeRetiro puntoderetiro){
		boolean ret = false;
		for(PuntoDeRetiro pr: puntosDeRetiro){
			if(!ret){
				ret = pr.getId() == puntoderetiro.getId(); 
			}
		}
		return ret;
	}
	
	public PuntoDeRetiro obtenerPuntoDeRetiro(Integer id){
		for(PuntoDeRetiro pr: puntosDeRetiro){
			if( pr.getId().equals(id)) {
				return pr;
			} 
		}
		return null;
	}

	public void eliminarPuntoDeRetiro(PuntoDeRetiro puntoDeRetiroSeleccionado) {
		ArrayList<PuntoDeRetiro> refill = new ArrayList<PuntoDeRetiro>();
		for(int i=0; i<puntosDeRetiro.size() ;i++){
			PuntoDeRetiro pr = puntosDeRetiro.get(i);
			if(pr!=null) {
			if (pr.getId().equals(puntoDeRetiroSeleccionado.getId())){
				puntosDeRetiro.remove(i);
				i=puntosDeRetiro.size();
			}
			}
		}
		insertAllNotNullElements(refill,puntosDeRetiro);
		puntosDeRetiro.clear();
		puntosDeRetiro = refill;
	}

	private void insertAllNotNullElements(ArrayList<PuntoDeRetiro> refill, List<PuntoDeRetiro> puntosDeRetiro2) {
		for(PuntoDeRetiro pr: puntosDeRetiro2) {
			if(pr!=null) {
				refill.add(pr);
			}
		}
	}

	public void eliminarZona(Zona zonaSeleccionada) {
		ArrayList<Zona> refill = new ArrayList<Zona>();
		for(int i = 0; i<zonas.size() ;i++){
			Zona zs = zonas.get(i);
			if(zs !=null)
			if(zonas.get(i).getId().equals(zonaSeleccionada.getId())){
				this.zonas.remove(i);
				i = zonas.size();
			}
		}
		
		insertNotNullElements(refill,zonas);
		zonas.clear();
		zonas = refill;
		
	}
	
	private void insertNotNullElements(ArrayList<Zona> refill, List<Zona> zonas) {
		for(Zona zs: zonas) {
			if(zs!=null) {
				refill.add(zs);
			}
		}
	}
	
	public void agregarZona(Zona zona){
		this.zonas.add(zona);
	}

	public List<PreguntaDeConsumo> getPreguntasDePedidosIndividuales() {
		return preguntasDePedidosIndividuales;
	}

	public void setPreguntasDePedidosIndividuales(List<PreguntaDeConsumo> preguntasDePedidosIndividuales) {
		this.preguntasDePedidosIndividuales = preguntasDePedidosIndividuales;
	}

	public List<PreguntaDeConsumo> getPreguntasDePedidosColectivos() {
		return preguntasDePedidosColectivos;
	}

	public void setPreguntasDePedidosColectivos(List<PreguntaDeConsumo> preguntasDePedidosColectivos) {
		this.preguntasDePedidosColectivos = preguntasDePedidosColectivos;
	}

	public List<PuntoDeRetiro> getPuntosDeRetiroHabilitados() {
		List<PuntoDeRetiro> list = new ArrayList<PuntoDeRetiro>();
		for(PuntoDeRetiro p : this.getPuntosDeRetiro()){
			if(p.getDisponible()){
				list.add(p);
			}
		}
		return list;
	}

	public List<PreguntaDeConsumo> getPreguntasDePedidosIndividualesHabilitadas() {
		List<PreguntaDeConsumo> list = new ArrayList<PreguntaDeConsumo>();
		for(PreguntaDeConsumo p : this.getPreguntasDePedidosIndividuales()){
			if(p.getHabilitada()){
				list.add(p);
			}
		}
		return list;
	}
	
	public List<PreguntaDeConsumo> getPreguntasDePedidosColectivosHabilitados() {
		List<PreguntaDeConsumo> list = new ArrayList<PreguntaDeConsumo>();
		for(PreguntaDeConsumo p : this.getPreguntasDePedidosColectivos()){
			if(p.getHabilitada()){
				list.add(p);
			}
		}
		return list;
	}

	public Integer getTiempoVencimientoPedidos() {
		return tiempoVencimientoPedidos;
	}

	public void setTiempoVencimientoPedidos(Integer tiempoVencimientoPedidos) {
		this.tiempoVencimientoPedidos = tiempoVencimientoPedidos;
	}

	public DataMultimedia getDataMultimedia() {
		return dataMultimedia;
	}

	public void setDataMultimedia(DataMultimedia dataMultimedia) {
		this.dataMultimedia = dataMultimedia;
	}
	
	public Producto getProductoConCodigo(String codigo) {
		Producto productoRes= null;
		for(Producto pr: this.getProductos()) {
			if(pr.getVariantes().get(0).getCodigo().equals(codigo)) {
				return pr;
			} 
		}
		return productoRes;
	}
	
	public List<Producto> getProductosConCodigo(String codigo) {
		List<Producto> productosRes= new ArrayList<Producto>();
		for(Producto pr: this.getProductos()) {
			if(pr.getVariantes().get(0).getCodigo().equals(codigo)) {
				productosRes.add(pr);
			} 
		}
		return productosRes;
	}
	
	public Fabricante getFabricante(String nombre) {
		Fabricante fabricanteRes= null;
		for(Fabricante fb: this.getFabricantes()) {
			if(fb.getNombre().equals(nombre)) {
				return fb;
			} 
		}
		return fabricanteRes;
	}

	public List<TagZonaDeCobertura> getTagsZonaCobertura() {
		return tagsZonaCobertura;
	}

	public List<TagTipoProducto> getTagsTipoProducto() {
		return tagsTipoProducto;
	}

	public List<TagTipoOrganizacion> getTagsTipoOrganizacion() {
		return tagsTipoOrganizacion;
	}

	public List<TagEvento> getTagsEvento() {
		return tagsEvento;
	}

	public void setTagsZonaCobertura(List<TagZonaDeCobertura> tagsZonaCobertura) {
		this.tagsZonaCobertura = tagsZonaCobertura;
	}

	public void setTagsTipoProducto(List<TagTipoProducto> tagsTipoProducto) {
		this.tagsTipoProducto = tagsTipoProducto;
	}

	public void setTagsTipoOrganizacion(List<TagTipoOrganizacion> tagsTipoOrganizacion) {
		this.tagsTipoOrganizacion = tagsTipoOrganizacion;
	}

	public void setTagsEvento(List<TagEvento> tagsEvento) {
		this.tagsEvento = tagsEvento;
	}

	public boolean isVisibleEnMulticatalogo() {
		return visibleEnMulticatalogo;
	}

	public void setVisibleEnMulticatalogo(boolean visibleEnMulticatalogo) {
		this.visibleEnMulticatalogo = visibleEnMulticatalogo;
	}

	public boolean isVentasHabilitadas() {
		return ventasHabilitadas;
	}

	public void setVentasHabilitadas(boolean ventasHabilitadas) {
		this.ventasHabilitadas = ventasHabilitadas;
	}

	public String getMensajeVentasDeshabilitadas() {
		return mensajeVentasDeshabilitadas;
	}

	public void setMensajeVentasDeshabilitadas(String mensajeVentasDeshabilitadas) {
		this.mensajeVentasDeshabilitadas = mensajeVentasDeshabilitadas;
	}

}
