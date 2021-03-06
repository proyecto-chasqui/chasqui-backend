package chasqui.view.composer;

import chasqui.utils.ErrorCodes;

public class Constantes {

	
	public final static String SESSION_USERNAME = "USERNAME";
	//public final static String EMAIL_REGEX_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$;";
	public final static Integer VENTANA_MODO_EDICION = 1;
	public final static Integer VENTANA_MODO_LECTURA = 2;
	
	//Cantidad de estados de pedido posibles.
	public final static Integer CANTIDAD_ESTADOS= 7;
	public final static String ESTADO_PEDIDO_INEXISTENTE="INEXISTENTE";
	public final static String ESTADO_PEDIDO_ABIERTO="ABIERTO";
	public final static String ESTADO_PEDIDO_VENCIDO="VENCIDO";
	public final static String ESTADO_PEDIDO_CANCELADO="CANCELADO";
	public final static String ESTADO_PEDIDO_CONFIRMADO="CONFIRMADO";
	public final static String ESTADO_PEDIDO_ENTREGADO="ENTREGADO";
	public final static String ESTADO_PEDIDO_PREPARADO="PREPARADO";
	
	public final static String VENTANA_PRODUCTOR = "VENTANA_PRODUCTOR";
	public final static String VENTANA_PRODUCTO = "VENTANA_PRODUCTO";
	
	public final static String ESTADO_NODO_SOLICITADO_SIN_ADMIN = "SOLICITADO_SIN_ADMIN";
	public final static String ESTADO_NODO_SOLICITADO = "SOLICITADO";
	public final static String ESTADO_NODO_APROBADO = "APROBADO";
	public final static String ESTADO_NODO_DESHABILITADO = "DESHABILITADO";
	public final static String NODO_ABIERTO = "NODO_ABIERTO";
	public final static String NODO_CERRADO = "NODO_CERRADO";
	
	public static final String DOMICILIO_NO_ESPECIFICADO = "No especificado";
	
	//USUARIO
	
	public static final String MAIL_CONFIRMADO = "MAIL_CONFIRMADO";
	public static final String MAIL_SIN_CONFIRMAR = "MAIL_SIN_CONFIRMAR";
	
	//NOTIFICACIONES
	public final static String ESTADO_NOTIFICACION_NO_LEIDA = "NOTIFICACION_NO_LEIDA";
//	public final static String ESTADO_NOTIFICACION_LEIDA = "Leído";
	public final static String ESTADO_NOTIFICACION_LEIDA_ACEPTADA = "NOTIFICACION_ACEPTADA";
	public final static String ESTADO_NOTIFICACION_LEIDA_RECHAZADA = "NOTIFICACION_RECHAZADA";
	public static final String ZONA_NO_DEFINIDA = "No definida";
	
	// ENVIO MAILS
	public static final String VENCIMIENTO_DE_PEDIDO_SUBJECT = "Vencimiento automatico";
	public static final String NUEVO_PEDIDO_EN_GCC_SUBJECT = "Un miembro de tu grupo de compras ha iniciado su pedido";
	public static final String AGRADECIMIENTO = "Muchas gracias por utilizar el sistema Chasqui";
	public static final String SUGERENCIA = "Si tenés alguna duda con respecto a días y lugares de entrega, consultá la información relacionada en la sección <bienvenida>. ";
	public static final String TEMPLATE_ACEPTAR_INVITACION_GCC ="emailInvitacionAGCCAceptada.ftl";
	public static final String TEMPLATE_ACEPTAR_INVITACION_NODO = "emailInvitacionANODOAceptada.ftl";
	public static final String TEMPLATE_BIENVENIDA_VENDEDOR = "emailBienvenida.ftl";
	public static final String TEMPLATE_BIENVENIDA_CLIENTE = "emailBienvenidaCliente.ftl";
	public static final String PEDIDOS_COLECTIVOS_CONFIRMADOS_TEMPLATE = "emailPedidosColectivosConfirmados.ftl";
	public static final String TEMPLATE_NOTIFICACION_VENCIMIENTO_PROXIMO = "emailNotificacionGenerica.ftl"; //TODO cambiar template, ahora en desuso
	public static final String TEMPLATE_NOTIFICACION_PEDIDO = "emailNotificacionGenerica.ftl";
	public static final String TEMPLATE_NOTIFICACION = "emailNotificacionGenerica.ftl";
	public static final String TEMPLATE_INVITAR_NODO_NO_REGISTRADO = "emailInvitadoSinRegistrarNodo.ftl";
	public static final String TEMPLATE_INVITAR_NODO_REGISTRADO = "emailInvitadoRegistradoNodo.ftl";
	public static final String TEMPLATE_INVITAR_GCC_NO_REGISTRADO = "emailInvitadoSinRegistrar.ftl";
	public static final String TEMPLATE_INVITAR_GCC_REGISTRADO = "emailInvitadoRegistrado.ftl";//TODO Definir, porque no existe, AUN.
	public static final String TEMPLATE_INVITACION_CHASQUI = "emailInvitacion.ftl";
	public static final String TEMPLATE_RECUPERO = "emailRecupero.ftl";
	public static final String SUBJECT_BIENVENIDO = "Bienvenido a Chasqui";
	public static final String SUBJECT_ALERT_VENCIMIENTO = "Tu Pedido esta a punto de vencer";
	public static final String SUBJECT_INVITACION_NO_REGISTRADO = "Te han invitado a un grupo de compras colectivas, registrate para seguir!";
	public static final String SUBJECT_INVITACION_NODO_NO_REGISTRADO = "Te han invitado a un nodo de compras colectivas, registrate para seguir!";
	public static final String SUBJECT_INVITACION_REGISTRADO = "Te han invitado a un grupo de compras colectivas";
	public static final String SUBJECT_INVITACION_NODO_REGISTRADO = "Te han invitado a un nodo de compras colectivas";
	public static final String SUBJECT_CONOCES_CHASQUI = "¿Conocés Chasqui?";
	public static final String SUBJECT_INVITACION_GCC_ACEPTADA = "<usuario> acepto tu invitacion";
	public static final String NUEVO_ADMINISTRADOR_SUBJECT = "Usted es el nuevo Administrador";
	public static final String PEDIDO_PREPARADO_SUBJECT = "Tu pedido ha sido preparado";
	public static final String PEDIDOS_PREPARADOS_SUBJECT = "Los pedidos han sido preparados";
	public static final String PEDIDO_COLECTIVO_CONFIRMADO = "Tu pedido colectivo ha sido confirmado";
	public static final String PEDIDO_COLECTIVO_PREPARADO = "Tu pedido colectivo esta preparado";
	
	public static final String AVISO_DE_RECUPERO_DE_CONTRASEÑA = "Aviso de Recupero de contraseña";
	public static final String CONFIRMACION_COMPRA_TEMPLATE_URL = "emailConfirmacionPedido.ftl";
	public static final String CONFIRMACIÓN_DE_COMPRA_SUBJECT = "Confirmación de Compra";
	public static final int CANT_MAX_IMAGENES_VARIEDAD = 3;
	public static final int MAX_SIZE_DESC_LARGA_PRODUCTOR = 8200;
	public static final String CONFIRMACION_COMPRA_NOTIFICACION ="Tu pedido se ha confirmado con éxito, recibirás un correo con los detalles de tu compra.";
	public static final String CONFIRMACION_COMPRA_NOTIFICACION_OTROMIEMBRO = "El usuario <usuario> ha confirmado su pedido en tu grupo de compras <grupo>.";
	public static final String CONFIRMACION_COMPRA_NOTIFICACION_OTROMIEMBRO_NODO = "El usuario <usuario> ha confirmado su pedido en tu nodo de compras <alias>.";
	public static final String NUEVO_PEDIDO_NOTIFICACION_OTROMIEMBRO =  "El usuario <usuario> ha iniciado su pedido en el <colectivo> <grupo> del catálogo de <vendedor> ¡No te pierdas esta compra!";
	public static final String CONFIRMACION_PEDIDO_COLECTIVO = "El administrador del <colectivo> <grupo> en el catálogo de <vendedor> ha confirmado el pedido colectivo.";
	public static final String VENCIMIENTO_PEDIDO_TEMPLATE = "emailVencimientoAutomatico.ftl";
	public static final String TEMPLATE_NUEVO_ADMINISTRADOR = "emailNuevoAdministrador.ftl";
	public static final String PEDIDO_PREPARADO_TEMPLATE = "emailPedidoPreparado.ftl";
	public static final String PEDIDOS_PREPARADOS_TEMPLATE = "emailPedidosPreparados.ftl";
	
	public static final String TXT_INVITACION_GCC = "El usuario <usuario> te ha invitado al <colectivo> de compras colectivas <alias> para el catálogo de <vendedor>";
	public static final String TXT_INVITACION_GCC_ACEPTADA = "El usuario <usuario> ha aceptado tu invitacion al <colectivo> de compras colectivas <alias> para el catálogo de <vendedor>.";
	public static final String TXT_NUEVO_ADMINISTRADOR = "El usuario <administradorAnterior> le ha cedido la administracion del <colectivo> de compras colectivas <alias>.";
	public static final String TXT_ANTERIOR_ADMINISTRADOR = "Se ha realizado el traspaso de la administracion del <colectivo> de compras colectivas <alias> al usuario <nuevoAdministrador>.";
	public static final String PEDIDO_VENCIDO_NOTIFICACION = "Tu pedido abierto el dia <timestamp> del catálogo del vendedor <vendedor> ha expirado por falta de actividad.";
	
	public static final String SOLICITUD_INGRESO_NODO = "El usuario <usuario> envio una solicitud para ingresar a tu nodo <nodo>";
	public static final String ACCION_GESTION_SOLICITUD_INGRESO_NODO = "El coordinador del nodo <nodo> <accion> tu solicitud de ingreso.";
	public static final String ACCION_GESTION_SOLICITUD_CREACION_NODO = "El vendedor <vendedor> <accion> tu solicitud de creación de nodo";
	public static final String ACCION_CANCELACION_POR_USUARIO_SOLICITUD_INGRESO_NODO = "El usuario <usuario> cancelo su solicitud de ingreso a su nodo <nodo>";
	
	//Mensajes de error
	public static final String ERROR_USUARIO_MAIL_SIN_CONFIRMAR = "Para acceder debe validar la cuenta via e-mail";
	public static final String ERROR_CREDENCIALES_INVALIDAS = "Usuario o Password incorrectos!";
	public static final String ERROR_INVITACION_NO_ACEPTADA = "Debe aceptar la invitacion para poder realizar esta accion";
	public static final String ERROR_CREDENCIALES_INVALIDAS_EN_MODIFICACION = "La password anterior debe ser correcta para realizar el cambio";
	public static final String ERROR_DE_DESCENCRIPTACION = "El proceso de desencriptacion ha sido interrumpido";
	public static final String PASSWORD_CORTO = "El password debe tener entre 10 y 26 caracteres";
	public static final String PASSWORD_ANTERIOR_INCORRECTO = "El password anterior no coincide";
	public static final String ERROR_USUARIO_NO_ES_MIEMBRO = "El usuario no es miembro del grupo de compras";
	public static final String ERROR_PUNTO_DE_RETIRO_INEXISTENTE = "El punto de retiro solicitado no existe";
	public static final String ZONA_NO_NECESARIA = "---";
	public static final String AVISO_PEDIDO_VENCIDO = "notificar_vencimiento";
	
	//estados de las solicitudes de creacion de nodos
	public static final String SOLICITUD_NODO_EN_GESTION = "solicitud_nodo_en_gestion";
	public static final String SOLICITUD_NODO_APROBADO = "solicitud_nodo_aprobado";
	public static final String SOLICITUD_NODO_RECHAZADO = "solicitud_nodo_rechazado";
	public static final String SOLICITUD_NODO_CANCELADO = "solicitud_nodo_cancelado";
	
	//estados de las solicitudes de pertenencia a nodo
	public static final String SOLICITUD_PERTENENCIA_NODO_ENVIADO = "solicitud_pertenencia_nodo_enviado";
	public static final String SOLICITUD_PERTENENCIA_NODO_ACEPTADO = "solicitud_pertenencia_nodo_aceptado";
	public static final String SOLICITUD_PERTENENCIA_NODO_RECHAZADO = "solicitud_pertenencia_nodo_rechazado";
	public static final String SOLICITUD_PERTENENCIA_NODO_CANCELADO = "solicitud_pertenencia_nodo_cancelado";
	public static final String NODO_ACTIVO = "nodo_activo";
	public static final String NODO_INACTIVO = "nodo_inactivo";
	
}
