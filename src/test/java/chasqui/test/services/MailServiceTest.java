package chasqui.test.services;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.mail.MessagingException;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import chasqui.exceptions.EstadoPedidoIncorrectoException;
import chasqui.model.GrupoCC;
import chasqui.model.Pedido;
import chasqui.model.ProductoPedido;
import chasqui.services.impl.MailService;
import chasqui.services.interfaces.GrupoService;
import chasqui.services.interfaces.NotificacionService;
import chasqui.services.interfaces.UsuarioService;
import freemarker.template.TemplateException;

@ContextConfiguration(locations = { "file:src/test/java/dataSource-Test.xml",
"file:src/main/resources/beans/service-beans.xml" })
@RunWith(SpringJUnit4ClassRunner.class)

public class MailServiceTest extends GenericSetUp {

	@Autowired
	public MailService mailService;
	@Autowired UsuarioService usuarioService;
	@Autowired GrupoService grupoService;
	@Autowired NotificacionService notificacionService;
	
	/*
	 * Destinataria debe reemplazarce con la direccion de
	 * correo electrico en la que se desea recibir todos los templates.
	 */
	public String destinatario = "destinatario@dominio.com";
	public String nombreDeUsuario = "User93";
	public String passwordFalsa = "passw0rd1234";
	
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Test
	public void testEnviarEmailDeBienvenidaDeVendedor() throws IOException, MessagingException, TemplateException {
		//Se envia el email del template emailBienvenida.ftl
		
		mailService.enviarEmailBienvenidaVendedor(this.destinatario, this.nombreDeUsuario, this.passwordFalsa);
		assertEquals(true , true);
	}
	
	@Test
	public void testEnviarEmailDeBienvenidaAlCliente() throws IOException, MessagingException, TemplateException {
		//Se envia el email del template emailBienvenidaCliente.ftl
		
		mailService.enviarEmailBienvenidaCliente(this.destinatario, "Rocio", "Otel");;
		assertEquals(true , true);
	}
	
	@Test
	public void testEnviarEmailDeConfirmacionDePedido() throws IOException, MessagingException, TemplateException, EstadoPedidoIncorrectoException {
		//Se envia el email del template emailConfirmacionPedido.ftl
		
		DateTime fechaVencimiento = new DateTime();
		Pedido pedido= new Pedido(vendedor, clienteFulano, false, fechaVencimiento.plusHours(24));
		ProductoPedido prodPed = new ProductoPedido(variante, 5);
		pedido.agregarProductoPedido(prodPed, fechaVencimiento.plusHours(48));
		pedido.sumarAlMontoActual(prodPed.getPrecio(), prodPed.getCantidad());
		pedido.setDireccionEntrega(direccionCasa);
		mailService.enviarEmailConfirmacionPedido(this.destinatario, this.destinatario, pedido);;
		assertEquals(true , true);
	}
	
	@Test
	public void testEnviarEmailDeInvitacionAGCCAceptada() throws IOException, MessagingException, TemplateException {
		//Se envia el email del template emailInvitacionAGCCAceptada.ftl
		
		clienteFulano.setEmail(destinatario);
		vendedor.setEmail(destinatario);
		GrupoCC grupoDeCompras = new GrupoCC(clienteFulano, "La casita de beltran", "El equipo de desarrollo tiene hambre y se organiza!");
		grupoDeCompras.setVendedor(vendedor);
		mailService.enviarEmailDeInvitacionAGCCAceptada(grupoDeCompras, this.clienteJuanPerez);;
		assertEquals(true , true);
	}
	
	@Test
	public void testEnviarEmailDeInvitacionAChasqui() throws Exception {
		//Se envia el email del template emailInvitacion.ftl
		
		clienteFulano.setEmail(destinatario);
		
		mailService.enviarEmailDeInvitacionChasqui(this.clienteFulano, this.destinatario);
		assertEquals(true , true);
	}
	
	@Test
	public void testEnviarEmailDeInvitacionAGrupoAUsuarioRegistrado() throws IOException, MessagingException, TemplateException {
		//Se envia el email del template emailInvitadoRegistrado.ftl 
		
		this.vendedor.setNombre("nombre del vendedor");
		this.vendedor.setUrl("urlVendedor");
		
		mailService.enviarEmailInvitadoRegistrado(this.clienteFulano, this.destinatario, this.vendedor.getUrl(), this.vendedor.getNombre());
		assertEquals(true , true);
	}

	@Test
	public void testEnviarEmailNotificandoElPedido() throws IOException, MessagingException, TemplateException {
		//Se envia el email del template emailNotificacionPedido.ftl 
		
		String cuerpoEmail = "Hola, este es el cuerpo del email, el original se obtiene en Chasqui.Properties";
		mailService.enviarEmailNotificacionPedido(this.destinatario, cuerpoEmail, "nombreDelUsuario", "apellidoDelUsuario");;
		assertEquals(true , true);
	}
	
	@Test
	public void testEnviarEmailDeNuevoAdministrador() throws IOException, MessagingException, TemplateException {
		//Se envia el email del template emailNuevoAdministrador.ftl 
		
		clienteFulano.setEmail(this.destinatario);
		clienteJuanPerez.setEmail(this.destinatario);
		vendedor.setEmail(this.destinatario);
		GrupoCC grupoDeCompras = new GrupoCC(clienteFulano, "La casita de beltran", "El equipo de desarrollo tiene hambre y se organiza!");
		grupoDeCompras.setVendedor(vendedor);
		
		mailService.enviarEmailNuevoAdministrador(this.clienteFulano, this.clienteJuanPerez, grupoDeCompras);;
		assertEquals(true , true);
	}
	
	@Test
	public void testEnviarEmailDePreparacionDePedido() throws IOException, MessagingException, TemplateException, EstadoPedidoIncorrectoException {
		//Se envia el email del template emailPedidoPreparado.ftl 
		
		this.vendedor.setEmail(this.destinatario);
		this.clienteFulano.setEmail(this.destinatario);
		
		DateTime fechaVencimiento = new DateTime().plusHours(24);
		Pedido pedido = new Pedido(this.vendedor, this.clienteFulano, false, fechaVencimiento);
		ProductoPedido prodPed = new ProductoPedido(variante, 5);
		pedido.setDireccionEntrega(direccionCasa);
		pedido.agregarProductoPedido(prodPed, fechaVencimiento.plusHours(48));
		pedido.sumarAlMontoActual(prodPed.getPrecio(), prodPed.getCantidad());
		
		
		mailService.enviarEmailPreparacionDePedido(pedido);;
		assertEquals(true , true);
	}
	
	@Test
	public void testEnviarEmailDeRecuperoDeContraseñaVendedor() throws Exception {
		//Se envia el email del template emailRecupero.ftl 
		
		this.vendedor.setEmail(this.destinatario);
		usuarioService.guardarUsuario(vendedor);
		
		mailService.enviarEmailRecuperoContraseña(this.destinatario, "NombreDeUsuario");;
		assertEquals(true , true);
	}
	
	@Test
	public void testEnviarEmailDeRecuperoDeContraseñaCliente() throws Exception {
		//Se envia el email del template emailRecupero.ftl
		
		this.clienteFulano.setEmail(this.destinatario);
		usuarioService.guardarUsuario(clienteFulano);
		mailService.enviarEmailRecuperoContraseñaCliente(this.destinatario);;
		assertEquals(true , true);
	}
	
	@Test
	public void testEnviarEmailDeVencimientoDePedido() throws IOException, MessagingException, TemplateException {
		//Se envia el email del template emailVencimientoAutomatico.ftl
		
		this.clienteFulano.setEmail(this.destinatario);
		DateTime fechaCreacionPedido = new DateTime();
		fechaCreacionPedido.minusHours(24);

		
		mailService.enviarEmailVencimientoPedido("nombreVendedor", this.clienteFulano, this.dateTimeToString(fechaCreacionPedido), "15");;
		assertEquals(true , true);
	}
	
	private String dateTimeToString(DateTime fecha){
		//"29/10/2012 a las 14:44 Hs "
		Integer horas = fecha.getHourOfDay();
		String horasResultantes;
		Integer minutos = fecha.getMinuteOfHour();
		String minutosResultantes;
		
		if(horas < 10){
			horasResultantes = "0" + horas.toString() + ":";
		}else{
			horasResultantes = horas.toString() + ":";
		}
		if(minutos < 10){
			minutosResultantes = "0" + minutos.toString() + " Hs ";
		}else{
			minutosResultantes = minutos.toString() + " Hs ";
		}
		
		
		String fechaResultante =				
		Integer.toString(fecha.getDayOfMonth())   + "/"       +
		Integer.toString(fecha.getMonthOfYear())  + "/"       +
		Integer.toString(fecha.getYear())         + " a las " +
		horasResultantes +
		minutosResultantes;
		
		return fechaResultante;
	}
	
	@Test
	public void testEnviarEmailDeInvitadoSinRegistrar() throws Exception {
		//Se envia el email del template emailInvitadoSinRegistrar.ftl
		
		Integer idGrupo = null;
		GrupoCC grupo = null;
		String emailCliente = "emailCliente@gmail.com";
		GrupoCC grupoDeCompras = new GrupoCC(clienteFulano, "La casita de beltran", "El equipo de desarrollo tiene hambre y se organiza!");
		grupoDeCompras.setVendedor(vendedor);
		grupoDeCompras.getCache().get(0).setEmail(emailCliente);;
		this.vendedor.setEmail(this.destinatario);
		this.vendedor.setUrl("urlVendedor");
		this.clienteFulano.setEmail(emailCliente);

		usuarioService.guardarUsuario(clienteFulano);
		usuarioService.guardarUsuario(vendedor);
		grupoService.guardarGrupo(grupoDeCompras);
		
		java.util.List<GrupoCC> grupos = grupoService.obtenerGruposDe(vendedor.getId());
		
		for (GrupoCC grupoCC : grupos) {
			if(grupoCC.getAlias().equals("La casita de beltran")){
				idGrupo = grupoCC.getId();
				grupo = grupoCC;
			}
		}
		
		grupo.invitarAlGrupo(this.destinatario);
		notificacionService.notificarInvitacionAGCCClienteNoRegistrado(clienteFulano, this.destinatario, grupo, null);
		
		mailService.enviarmailInvitadoSinRegistrar(this.clienteFulano, this.destinatario, this.vendedor.getUrl(), this.vendedor.getNombre(), idGrupo);;
		assertEquals(true , true);
	}
}