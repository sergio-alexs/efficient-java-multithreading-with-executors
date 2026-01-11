package tuts.common;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Esta clase es otra implementación de `UncaughtExceptionHandler`.
 * 
 * Mientras que `ThreadExceptionHandler` era un manejador más genérico, este tiene un
 * propósito muy específico: simular el envío de una notificación (por ejemplo, un email)
 * cuando un hilo falla.
 * 
 * Esto demuestra el patrón "Strategy": puedes tener diferentes manejadores de excepciones
 * (diferentes estrategias) y asignarlos a diferentes tipos de hilos según tus necesidades.
 * Por ejemplo, los hilos de un pool que realizan tareas críticas podrían notificar por email,
 * mientras que los de tareas menos importantes solo registran el error en un archivo.
 */
public class ThreadExceptionNotifier implements UncaughtExceptionHandler {

	/**
	 * Este es el método que la JVM llama cuando un hilo muere por una excepción no capturada.
	 * 
	 * La única responsabilidad de este método es delegar la acción a un método más específico.
	 * 
	 * @param t El hilo que falló.
	 * @param e La excepción (`Throwable`) que causó el fallo.
	 */
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		// Llama al método privado que contiene la lógica de notificación.
		sendNotificationEmail(t, e);
	}
	
	/**
	 * Un método privado que simula la lógica para enviar un correo electrónico de notificación.
	 * 
	 * @param t El hilo que falló.
	 * @param e La excepción que ocurrió.
	 */
	private void sendNotificationEmail(Thread t, Throwable e) {
		// En una aplicación real, aquí iría el código para conectarse a un servidor de correo
		// y enviar un email con detalles útiles para el diagnóstico.
		
		String subject = "ERROR CRÍTICO: El hilo '" + t.getName() + "' ha fallado";
		
		// Un buen cuerpo de email incluiría:
		// - El nombre del hilo.
		// - La fecha y hora del error.
		// - El tipo de excepción.
		// - El mensaje de la excepción.
		// - La traza de la pila completa (stack trace) para poder depurar.
		StringBuilder body = new StringBuilder();
		body.append("Un error crítico ha ocurrido en la aplicación.\n\n");
		body.append("Detalles del Hilo:\n");
		body.append("  Nombre: ").append(t.getName()).append("\n");
		body.append("  ID: ").append(t.getId()).append("\n\n");
		body.append("Detalles del Error:\n");
		body.append("  Tipo: ").append(e.getClass().getName()).append("\n");
		body.append("  Mensaje: ").append(e.getMessage()).append("\n\n");
		body.append("Stack Trace:\n");
		// Aquí se adjuntaría la traza de la pila.
		// e.printStackTrace(printWriter);
		
		// Para este ejemplo, simplemente imprimimos un mensaje en la consola que simula
		// el envío de la notificación.
		System.out.println("--- SIMULACIÓN DE ENVÍO DE EMAIL ---");
		System.out.println("Destinatario: admin@my-app.com");
		System.out.println("Asunto: " + subject);
		System.out.println("Cuerpo del mensaje (resumen): \n" + body.toString());
		System.out.println("------------------------------------");
	}

}
