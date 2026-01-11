package tuts.common;

/**
 * <h2>El Problema de las Excepciones en Hilos</h2>
 * 
 * Esta clase es una tarea diseñada para fallar. Su propósito es demostrar uno de los
 * conceptos más confusos para los principiantes en concurrencia: <b>las excepciones
 * no se pueden capturar "a través" de los límites de un hilo</b>.
 * 
 * Si haces algo como esto, NO FUNCIONARÁ:
 * <pre>
 * try {
 *     new Thread(new ExceptionLeakingTask()).start();
 * } catch (RuntimeException e) {
 *     // ESTE BLOQUE NUNCA SE EJECUTARÁ
 *     System.out.println("Excepción capturada!");
 * }
 * </pre>
 * 
 * <b>Analogía:</b> Piensa que cada hilo es una habitación insonorizada. Si una alarma de
 * incendios (una excepción) suena dentro de una habitación, la gente que está fuera
 * (el hilo `main`) no la oirá. La alarma suena, pero solo dentro de esa habitación.
 */
public class ExceptionLeakingTask implements Runnable {

	/**
	 * El método run() es el punto de entrada de la tarea.
	 */
	@Override
	public void run() {
		// --- ¡LA EXCEPCIÓN SE LANZA AQUÍ, DENTRO DEL HILO! ---
		
		// `throw new RuntimeException()` crea y lanza una excepción "no verificada" (unchecked).
		// No hay ningún bloque `try-catch` *dentro* de este método `run()` para capturarla.
		
		// --- CONSECUENCIAS ---
		// 1. La excepción "escapa" del método `run()`.
		// 2. El hilo que está ejecutando esta tarea MUERE INMEDIATAMENTE.
		// 3. Por defecto, Java imprimirá la traza de la excepción en la consola de error.
		// 4. El hilo que inició esta tarea (ej: `main`) CONTINÚA su ejecución como si nada
		//    hubiera pasado. No se entera del fallo. Esto puede causar "fallos silenciosos"
		//    muy difíciles de depurar en una aplicación.
		
		// La solución para "escuchar la alarma" fuera de la habitación es usar un
		// `Thread.UncaughtExceptionHandler`, como se demuestra en `ThreadExceptionHandler.java`.
		
		throw new RuntimeException();
	}

}
