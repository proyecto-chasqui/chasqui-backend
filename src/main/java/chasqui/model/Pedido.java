package chasqui.model;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

import chasqui.exceptions.EstadoPedidoIncorrectoException;
import chasqui.view.composer.Constantes;

public class Pedido implements IPedido {

	private Integer id;
	private Integer idVendedor;
	private String estado;
	private String nombreVendedor;
	private Cliente cliente;
	private Boolean alterable;
	@Temporal(TemporalType.TIMESTAMP)
	private DateTime fechaCreacion;
	@Temporal(TemporalType.TIMESTAMP)
	private DateTime fechaDeVencimiento;
	@Temporal(TemporalType.TIMESTAMP)
	private DateTime fechaModificacion;
	private Direccion direccionEntrega;
	private PuntoDeRetiro puntoDeRetiro;
	private Double montoMinimo;
	private Double montoActual;
	private Boolean perteneceAPedidoGrupal;
	private Set<ProductoPedido> productosEnPedido;
	private Zona zona;
	private PedidoColectivo pedidoColectivo;// puede ser null
	private String comentario;
	private String tipoDeAjuste;
	private Map<String,String> respuestasAPreguntas;
	DecimalFormat df = new DecimalFormat("#.##");

	public Pedido(Vendedor v, Cliente c, Boolean esPedidoGrupal, DateTime vencimiento) {
		idVendedor = v.getId();
		nombreVendedor = v.getNombre();
		estado = Constantes.ESTADO_PEDIDO_ABIERTO;
		cliente = c;
		fechaCreacion = new DateTime();
		alterable = true;
		perteneceAPedidoGrupal = esPedidoGrupal;
		montoMinimo = new Double(v.getMontoMinimoPedido());
		montoActual = new Double(0.0);
		setFechaDeVencimiento(vencimiento);
		productosEnPedido = new HashSet<ProductoPedido>();
		respuestasAPreguntas = new HashMap<String,String>();
	}

	public Pedido() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getNombreVendedor() {
		return nombreVendedor;
	}

	public void setNombreVendedor(String nombreVendedor) {
		this.nombreVendedor = nombreVendedor;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public DateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(DateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Direccion getDireccionEntrega() {
		return direccionEntrega;
	}

	public void setDireccionEntrega(Direccion direccionEntrega) {
		this.direccionEntrega = direccionEntrega;
	}

	public Boolean getPerteneceAPedidoGrupal() {
		return perteneceAPedidoGrupal;
	}

	public void setPerteneceAPedidoGrupal(Boolean perteneceAPedidoGrupal) {
		this.perteneceAPedidoGrupal = perteneceAPedidoGrupal;
	}

	public Double getMontoMinimo() {
		return trim2decimals(montoMinimo);
	}

	public void setMontoMinimo(Double montoMinimo) {
		this.montoMinimo = montoMinimo;
	}
	//patch para error en monto actual de pedidos revividos, remover cuando se detecte el error superior.
	public Double getMontoActual() {
		double monto = 0.0; 
		for (ProductoPedido p : productosEnPedido) {
			monto = monto + (p.getCantidad() * p.getPrecio());
		}
		//return trim2decimals(montoActual);
		return trim2decimals(monto);
	}
	
	public Double getMontoTotalIncentivo() {
		double monto = 0.0; 
		for (ProductoPedido p : productosEnPedido) {
			monto = monto + (p.getCantidad() * p.getIncentivo());
		}
		return trim2decimals(monto);
	}
	
	private Double trim2decimals(Double d) {
		String trim = df.format(d); 
		Double value = Double.parseDouble(trim.replace(",","."));
		return value;
	}

	public void setMontoActual(Double montoActual) {
		this.montoActual = montoActual;
	}

	public Set<ProductoPedido> getProductosEnPedido() {
		return productosEnPedido;
	}

	public void setProductosEnPedido(Set<ProductoPedido> productosEnPedido) {
		this.productosEnPedido = productosEnPedido;
	}

	public Integer getIdVendedor() {
		return idVendedor;
	}

	public void setIdVendedor(Integer idVendedor) {
		this.idVendedor = idVendedor;
	}

	public Boolean getAlterable() {
		return alterable;
	}

	public void setAlterable(Boolean alterable) {
		this.alterable = alterable;
	}

	public DateTime getFechaDeVencimiento() {
		return fechaDeVencimiento;
	}

	public void setFechaDeVencimiento(DateTime fechaDeVencimiento) {
		this.fechaDeVencimiento = fechaDeVencimiento;
	}
	
	@Override
	public void preparado() throws EstadoPedidoIncorrectoException {
		if (this.getEstado().equals(Constantes.ESTADO_PEDIDO_CONFIRMADO)) {
			this.estado = Constantes.ESTADO_PEDIDO_PREPARADO;
			this.alterable = false;
			this.grabarPuntoDeRetiro();
		} else {
			throw new EstadoPedidoIncorrectoException("El pedido no puede ser preparado pues su estado es: "+ this.getEstado());
		}

	}
	
	private void grabarPuntoDeRetiro(){
		if(this.puntoDeRetiro != null) {
			this.puntoDeRetiro = new PuntoDeRetiro(this.puntoDeRetiro);		
		}
	}

	@Override
	public void entregarte() throws EstadoPedidoIncorrectoException {
		if (this.getEstado().equals(Constantes.ESTADO_PEDIDO_PREPARADO) /*&& alterable*/) {
			this.estado = Constantes.ESTADO_PEDIDO_ENTREGADO;
			this.alterable = false; //TODO: Este campo se usa para algo?
		} else {
			throw new EstadoPedidoIncorrectoException("El pedido no puede ser entregado pues su estado es: "+ this.getEstado());
		}

	}

	public boolean estaVigente() {
		return this.getEstado().equals(Constantes.ESTADO_PEDIDO_ABIERTO) ;
	}
	
	public void reabrirPedido(DateTime nuevoVencimiento) throws EstadoPedidoIncorrectoException {
		if(this.getEstado().equals(Constantes.ESTADO_PEDIDO_CANCELADO)||this.getEstado().equals(Constantes.ESTADO_PEDIDO_VENCIDO)){
			this.setEstado(Constantes.ESTADO_PEDIDO_ABIERTO);
			this.setProductosEnPedido(new HashSet<ProductoPedido>());
			this.fechaDeVencimiento = nuevoVencimiento;
			this.setMontoActual(new Double(0.0));
			this.alterable = true;
			this.setFechaCreacion(new DateTime());
		}else {
			throw new EstadoPedidoIncorrectoException("El pedido no está abierto, su estado es: " + this.getEstado());
		}
	}

	public void agregarProductoPedido(ProductoPedido pp, DateTime nuevoVencimiento)
			throws EstadoPedidoIncorrectoException {
		if(this.getEstado().equals(Constantes.ESTADO_PEDIDO_CANCELADO)||this.getEstado().equals(Constantes.ESTADO_PEDIDO_VENCIDO)){
			this.setEstado(Constantes.ESTADO_PEDIDO_ABIERTO);
			this.setProductosEnPedido(new HashSet<ProductoPedido>());
			this.fechaDeVencimiento = nuevoVencimiento;
			this.setMontoActual(new Double(0.0));
			this.alterable = true;
			this.setFechaCreacion(new DateTime());
		}
		
		if (this.getEstado().equals(Constantes.ESTADO_PEDIDO_ABIERTO)) {
			this.fechaDeVencimiento = nuevoVencimiento;
			if (existeProductoEnPedido(pp)) {
				sumarCantidadAProductoExistente(pp);
			} else {
				productosEnPedido.add(pp);
			}
		} else {
			throw new EstadoPedidoIncorrectoException("El pedido no está abierto, su estado es: " + this.getEstado());
		}
	}
	
	/**
	 * Usado para el exporter
	 */
	public void agregarProductoPedidoConValidaciones(ProductoPedido pp, DateTime nuevoVencimiento) throws EstadoPedidoIncorrectoException {
		if(this.getEstado().equals(Constantes.ESTADO_PEDIDO_CANCELADO)||this.getEstado().equals(Constantes.ESTADO_PEDIDO_VENCIDO)){
			this.setEstado(Constantes.ESTADO_PEDIDO_ABIERTO);
			this.setProductosEnPedido(new HashSet<ProductoPedido>());
			this.fechaDeVencimiento = nuevoVencimiento;
			this.setMontoActual(new Double(0.0));
			this.alterable = true;
			this.setFechaCreacion(new DateTime());
		}
		
		if (this.getEstado().equals(Constantes.ESTADO_PEDIDO_ABIERTO)) {
			this.fechaDeVencimiento = nuevoVencimiento;
			this.agregarProductoPedidoApedido(pp);
		} else {
			throw new EstadoPedidoIncorrectoException("El pedido no está abierto, su estado es: " + this.getEstado());
		}
	}

	private void agregarProductoPedidoApedido(ProductoPedido pp) {
		if (existeProductoEnPedido(pp)) {
			if(existeProductoEnPedidoConMismoPrecio(pp)) {
				sumarCantidadAProductoExistente(pp);
			}else{
				productosEnPedido.add(pp);
			}
		} else {
			productosEnPedido.add(pp);
		}
		
	}

	private boolean existeProductoEnPedidoConMismoPrecio(ProductoPedido pp) {
		ProductoPedido p = encontrarProductoEnPedido(pp);
		return p.getPrecio().equals(pp.getPrecio());
	}

	private void sumarCantidadAProductoExistente(ProductoPedido pp) {
		ProductoPedido p = encontrarProductoEnPedido(pp);
		p.sumarCantidad(pp.getCantidad());
	}

	private ProductoPedido encontrarProductoEnPedido(ProductoPedido pp) {
		for (ProductoPedido p : productosEnPedido) {
			if (p.getIdVariante().equals(pp.getIdVariante())) {
				return p;
			}
		}
		return null;
	}

	private boolean existeProductoEnPedido(ProductoPedido p) {
		return encontrarProductoEnPedido(p) != null;
	}

	public ProductoPedido encontrarProductoPedido(Integer idVariante) {
		for (ProductoPedido pp : this.getProductosEnPedido()) {
			if (pp.getIdVariante().equals(idVariante)) {
				return pp;
			}
		}
		return null;
	}

	public void eliminar(ProductoPedido pp) {
		productosEnPedido.remove(pp);
	}

	public void sumarAlMontoActual(Double precio, Integer cantidad) {
		this.montoActual += precio * cantidad;
	}

	public void restarAlMontoActual(Double precio, Integer cantidad) {
		this.montoActual -= precio * cantidad;
	}

	@Override
	public Double getMontoTotal() {
		return this.getMontoActual();
	}

	@Override
	public void cancelar() throws EstadoPedidoIncorrectoException {
		this.setEstado(Constantes.ESTADO_PEDIDO_CANCELADO);
		this.alterable = false;
	}

	public boolean estaAbierto() {
		return this.getEstado().equals(Constantes.ESTADO_PEDIDO_ABIERTO);
	}
	
	public boolean estaCancelado() {
		return this.getEstado().equals(Constantes.ESTADO_PEDIDO_CANCELADO);
	}

	@Override
	public void confirmarte() throws EstadoPedidoIncorrectoException {
		if (this.getPerteneceAPedidoGrupal() || this.getMontoActual() >= this.getMontoMinimo()) {
			this.setEstado(Constantes.ESTADO_PEDIDO_CONFIRMADO);
			this.alterable = false;
			this.fechaModificacion = new DateTime();
		} else
			throw new EstadoPedidoIncorrectoException(
					"El monto de compra no supera el mínimo (" + this.getMontoMinimo().toString() + ")");
	}
	
	@Override
	public void confirmarteSinMontoMinimo() throws EstadoPedidoIncorrectoException {
		this.setEstado(Constantes.ESTADO_PEDIDO_CONFIRMADO);
		this.alterable = false;
		this.fechaModificacion = new DateTime();
	}

	@Override
	public void setZona(Zona zona) {
		this.zona = zona;
	}

	@Override
	public Zona getZona() {
		return this.zona;
	}

	public void vencerte() throws EstadoPedidoIncorrectoException {
		if (this.getEstado().equals(Constantes.ESTADO_PEDIDO_ABIERTO) /*&& !this.getPerteneceAPedidoGrupal()*/) {
			this.estado = Constantes.ESTADO_PEDIDO_VENCIDO;
		} else
			throw new EstadoPedidoIncorrectoException("El pedido no estaba abierto o pertenece a un pedido colectivo");
	}

	public PedidoColectivo getPedidoColectivo() {
		return pedidoColectivo;
	}

	public void setPedidoColectivo(PedidoColectivo pedidoColectivo) {
		this.pedidoColectivo = pedidoColectivo;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getTipoDeAjuste() {
		return tipoDeAjuste;
	}

	public void setTipoDeAjuste(String tipoDeAjuste) {
		this.tipoDeAjuste = tipoDeAjuste;
	}

	public DateTime getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(DateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public PuntoDeRetiro getPuntoDeRetiro() {
		return puntoDeRetiro;
	}

	public void setPuntoDeRetiro(PuntoDeRetiro puntoDeRetiro) {
		this.puntoDeRetiro = puntoDeRetiro;
	}

	public Map<String,String> getRespuestasAPreguntas() {
		return respuestasAPreguntas;
	}

	public void setRespuestasAPreguntas(Map<String,String> respuestasAPreguntas) {
		this.respuestasAPreguntas = respuestasAPreguntas;
	}

	@Override
	public boolean esParaRetirar() {
		return (this.puntoDeRetiro != null);
	}

	@Override
	public boolean esParaDomicilio() {
		return (this.direccionEntrega != null);
	}

}
