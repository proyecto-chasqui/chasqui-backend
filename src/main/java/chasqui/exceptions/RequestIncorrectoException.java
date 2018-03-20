package chasqui.exceptions;

public class RequestIncorrectoException extends Exception{

	private static final long serialVersionUID = 1L;

	public RequestIncorrectoException(Exception e){
		super(e);
	}
	
	public RequestIncorrectoException(Exception e , String mensaje){
		super(mensaje,e);
	}
	
	
	public RequestIncorrectoException(String msg){
		super(msg);
	}

	public RequestIncorrectoException() {
		// TODO Auto-generated constructor stub
	}

	
}
