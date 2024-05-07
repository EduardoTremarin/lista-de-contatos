package io.uex.listadecontatos.exception;

public class ContactAlreadyExistsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContactAlreadyExistsException() {
        super("CPF de contato já cadastrado no sistema.");
    }
	
}
