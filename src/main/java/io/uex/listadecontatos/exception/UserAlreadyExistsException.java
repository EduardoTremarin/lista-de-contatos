package io.uex.listadecontatos.exception;

public class UserAlreadyExistsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAlreadyExistsException() {
        super("Email de usuário já cadastrado no sistema.");
    }
	
}
