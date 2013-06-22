package de.blogspot.wrongtracks.prost.ejb.exception;

/**
 * Exception to indicate that OSGi couldn't provide a server and because of that
 * the EJB cannot do its job.
 * 
 * @author Ronny Bräunlich
 * 
 */
public class ServiceUnavailableException extends RuntimeException {

	private static final long serialVersionUID = -2496808022080999949L;

}
