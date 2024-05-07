package io.uex.listadecontatos.exception;

public class InvalidCPFException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCPFException() {
        super("CPF inválido.");
    }
	
}
