package io.uex.listadecontatos.exception;

public class UserNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException() {
        super("Usuário não encontrado.");
    }
	
}
