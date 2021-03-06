package chasqui.view.composer;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.cxf.common.util.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.ClickEvent;

import chasqui.model.Usuario;
import chasqui.model.Vendedor;
import chasqui.services.impl.MailService;
import chasqui.services.impl.SessionListenerService;
import chasqui.services.interfaces.PedidoColectivoService;
import chasqui.services.interfaces.UsuarioService;

@SuppressWarnings({ "serial", "deprecation" })
public class LoginComposer  extends GenericForwardComposer<Component>{

	
	
	private AnnotateDataBinder binder;
	private Textbox usernameLoggin;
	private Textbox passwordLoggin;
	private Label labelError;
	private Button logginButton;
	private Popup emailPopUp;
	private Toolbarbutton olvidoSuPassword;
	private Textbox emailTextbox;
	private Button cerrarPopUpButton;
	private Window loginWindow;
	
	private UsuarioService service;
	private MailService mailService;
	private SessionListenerService sessionListenerService;
	private boolean forceLogin = false;

	@Override
	public void doAfterCompose(Component comp) throws Exception{
		Vendedor u = (Vendedor) Executions.getCurrent().getSession().getAttribute(Constantes.SESSION_USERNAME);
		sessionListenerService = (SessionListenerService) SpringUtil.getBean("sessionListenerService");
		if(u != null){
			Executions.sendRedirect("/administracion.zul");
		}
		super.doAfterCompose(comp);
		binder = new AnnotateDataBinder(comp);
		service = (UsuarioService) SpringUtil.getBean("usuarioService");
		mailService = (MailService) SpringUtil.getBean("mailService");
		comp.addEventListener(Events.ON_NOTIFY, new EnvioEmailListener(this));
	}
	
	
	public void onClick$logginButton() throws Exception{
		String password = passwordLoggin.getValue();
		String usuario = usernameLoggin.getValue();
//		Vendedor v = (Vendedor) SecurityContextHolder.getContext().getAuthentication().getCredentials();
		if (!password.matches("^[a-zA-Z0-9]*$") || password.length() < 8){
			labelError.setVisible(true);
			passwordLoggin.setValue("");
			binder.loadAll();
			return;
		};
		Vendedor user = null;
		try{			
			user =(Vendedor) service.login(usuario,password);
			if(sessionListenerService.existeUnaSesionPara(usuario) && !forceLogin) {
				advertenciaDeIngreso(usuario);
			}else {
				forceLogin=false;
				iniciarPanel(usuario,user);				
			}
		}catch(Exception e){
			labelError.setVisible(true);
			passwordLoggin.setValue("");
		}
		
	}
	
	public void iniciarPanel(String nombreUsuario, Vendedor user) {
		service.inicializarListasDe(user);
		Executions.getCurrent().getSession().setAttribute(Constantes.SESSION_USERNAME, user);
		Executions.sendRedirect("/administracion.zul");
		listenSession(nombreUsuario, (HttpSession) Executions.getCurrent().getSession().getNativeSession());
	}
	
	private void advertenciaDeIngreso(final String nombreUsuario) {
		
		Messagebox.show(
				"Hay una sesion activa para el usuario "+ nombreUsuario +", si ingresa desconectará al usuario actual ¿Desea ingresar de todas maneras?",
				"Advertencia",
	    		new Messagebox.Button[] {Messagebox.Button.YES, Messagebox.Button.ABORT},
	    		new String[] {"Si","No"},
	    		Messagebox.EXCLAMATION, null, new EventListener<ClickEvent>(){
	
			public void onEvent(ClickEvent event) throws Exception {
				String edata = "";
				if (event.getData() != null){
					edata= event.getData().toString();
				}
				switch (edata){
				case "YES":
					try {
						forceLogin = true;
						Clients.showNotification("Ya puede ingresar", "info", logginButton, "after_center", 3000, true);
					}catch (Exception e) {
						e.printStackTrace();						
						Clients.showNotification("Ocurrio un error desconocido", "error", logginButton, "after_center", 3000);
					}
					
				case "ABORT":
				default:
				}
			}
			});

	}
	
	public void listenSession(String user, HttpSession session) {
		sessionListenerService.addOrReplaceSession(user, session);
	}
	
	public void onOK$enter() throws Exception {
		this.onClick$logginButton();  
	}
	
	public void onOlvidoPassword(){
		emailPopUp.open(olvidoSuPassword);
	}
	
	
	public void bloquearPantalla(String msg){
		Clients.showBusy(msg);
	}
	
	public void desbloquearPantalla(){
		Clients.clearBusy();
	}
	
	public void onClick$emailButton(){
		bloquearPantalla("Procesando...");
		Events.echoEvent(Events.ON_NOTIFY, loginWindow, null);
	}
	
	public void onEnviarEmail() {
		try{
			String email = emailTextbox.getValue();
			if(StringUtils.isEmpty(email) || !EmailValidator.getInstance().isValid(email)){
				throw new WrongValueException(emailTextbox,"Por favor ingrese un email valido.");
			}
			Usuario u = service.obtenerUsuarioPorEmail(email);
			if(u != null && u instanceof Vendedor){
				mailService.enviarEmailRecuperoContraseña(email, u.getUsername());				
				Messagebox.show("El Email ha sido enviado con exito!","Información",Messagebox.OK,Messagebox.INFORMATION);
				desbloquearPantalla();
				emailPopUp.close();
			}else{
				desbloquearPantalla();
				throw new WrongValueException(emailTextbox,"Por favor ingrese un email valido.");
			}
		}catch(Exception e){
			desbloquearPantalla();
			throw new WrongValueException(emailTextbox,e.getMessage());
		}		
	}
	
	public void onClick$cerrarPopUpButton(){
		emailPopUp.close();
	}
	
	public Textbox getEmailTextbox() {
		return emailTextbox;
	}


	public void setEmailTextbox(Textbox emailTextbox) {
		this.emailTextbox = emailTextbox;
	}


	public Button getLogginButton() {
		return logginButton;
	}


	public void setLogginButton(Button logginButton) {
		this.logginButton = logginButton;
	
	}
	public Label getLabelError() {
		return labelError;
	}


	public void setLabelError(Label labelError) {
		this.labelError = labelError;
	}

	
	public Button getCerrarPopUpButton() {
		return cerrarPopUpButton;
	}


	public void setCerrarPopUpButton(Button cerrarPopUpButton) {
		this.cerrarPopUpButton = cerrarPopUpButton;
	}


	public Toolbarbutton getOlvidoSuPassword() {
		return olvidoSuPassword;
	}


	public void setOlvidoSuPassword(Toolbarbutton olvidoSuPassword) {
		this.olvidoSuPassword = olvidoSuPassword;
	}


	public AnnotateDataBinder getBinder() {
		return binder;
	}

	public void setBinder(AnnotateDataBinder binder) {
		this.binder = binder;
	}

	public Textbox getUsernameLoggin() {
		return usernameLoggin;
	}

	public void setUsernameLoggin(Textbox usernameLoggin) {
		this.usernameLoggin = usernameLoggin;
	}

	public Textbox getPasswordLoggin() {
		return passwordLoggin;
	}

	public void setPasswordLoggin(Textbox passwordLoggin) {
		this.passwordLoggin = passwordLoggin;
	}


	public Popup getEmailPopUp() {
		return emailPopUp;
	}


	public void setEmailPopUp(Popup emailPopUp) {
		this.emailPopUp = emailPopUp;
	}
	
}

class EnvioEmailListener implements EventListener<Event>{
	
	LoginComposer composer;
	
	public EnvioEmailListener (LoginComposer l){
		composer = l;
	}
	
	public void onEvent(Event event) throws Exception {
		composer.onEnviarEmail();
		
	}
	
}
