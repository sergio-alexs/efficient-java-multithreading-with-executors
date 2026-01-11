package tuts.common;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * <h2>La Solución: El Manejador de Excepciones No Capturadas</h2>
 * 
 * Esta clase es la "red de seguridad" para las excepciones que matan a un hilo.
 * Actúa como un "observador" global que es notificado por la JVM justo cuando un hilo
 * va a morir debido a una excepción que no fue atrapada por ningún `try-catch`.
 * 
 * <b>Analogía:</b> Si cada hilo es una habitación insonorizada con una alarma de incendios
 * (ver `ExceptionLeakingTask`), este manejador es como la <b>luz de alarma central</b>
 * en el pasillo. No detiene el fuego (la excepción), pero se enciende para que todos
 * sepan que una de las alarmas internas se ha activado. Hace visible un fallo que,
 * de otro modo, sería silencioso.
 * 
 * <h2>¿Cómo se usa?</h2>
 * No se "llama" directamente. Se "registra" en un hilo o en toda la aplicación:
 * <ul>
 *   <li><b>Modo Global:</b> `Thread.setDefaultUncaughtExceptionHandler(new ThreadExceptionHandler(...))`
 *       Esto establece un manejador por defecto para TODOS los hilos de la aplicación.</li>
 *   <li><b>Modo Por Hilo:</b> `unHilo.setUncaughtExceptionHandler(new ThreadExceptionHandler(...))`
 *       Esto establece un manejador solo para ese hilo específico. Un `ThreadFactory` es el
 *       lugar perfecto para hacer esto de forma consistente.</li>
 * </ul>
 */
public class ThreadExceptionHandler implements UncaughtExceptionHandler {

	private String handlerId;
	
	public ThreadExceptionHandler(String handlerId) {
		this.handlerId = handlerId;
	}
	
	public ThreadExceptionHandler() {
		// Constructor por defecto.
	}
	
	/**
	 * Este es el método que la JVM llamará. Se ejecuta en un hilo de la JVM, NO en el hilo
	 * que ha fallado (que ya está a punto de morir). Esto permite a la aplicación
	 * reaccionar al fallo de forma segura.
	 * 
	 * @param t El hilo que ha fallado y está a punto de terminar.
	 * @param e La excepción (`Throwable`) que causó el fallo.
	 */
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println("####### [MANEJADOR DE EXCEPCIONES: '" + getHandlerId() + "'] #######");
		System.out.println("El hilo '" + t.getName() + "' ha muerto debido a una excepción no capturada.");
		System.out.println("Excepción: " + e.getClass().getSimpleName() + " - " + e.getMessage());
		System.out.println("Traza de la pila:");
		e.printStackTrace(System.out); // Imprime la traza completa.
		System.out.println("#################################################################");
		
		// En una aplicación real, aquí harías algo más útil:
		// - LOGGING: Escribir el error en un archivo de log (usando Log4j, SLF4J, etc.).
		// - ALERTING: Enviar una notificación a un sistema de monitoreo (Sentry, Datadog).
		// - CLEANUP: Intentar limpiar recursos que el hilo fallido podría haber dejado abiertos.
		// - SHUTDOWN: Si el error es crítico, iniciar un apagado ordenado de la aplicación.
	}
	
	private String getHandlerId() {
		return (handlerId == null || "".equals(handlerId)) ? "DEFAULT" : handlerId;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "@" + this.hashCode() + 
				(handlerId == null || "".equals(handlerId) ? "" : "(\"" + handlerId + "\")");
	}
}
