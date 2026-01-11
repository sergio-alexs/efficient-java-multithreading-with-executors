package tuts.common;

import java.util.concurrent.ThreadFactory;

/**
 * <h2>¿Qué es una Fábrica de Hilos (ThreadFactory)?</h2>
 * 
 * Un `ThreadFactory` es un objeto que crea nuevos hilos bajo demanda. Es un patrón de diseño "Factory".
 * 
 * <h2>¿Por qué es útil?</h2>
 * 
 * Cuando usas un `ExecutorService` (un gestor de pool de hilos), a menudo no creas los hilos tú mismo.
 * El `ExecutorService` los crea por ti cuando los necesita.
 * 
 * Pero, ¿qué pasa si quieres personalizar esos hilos? Por ejemplo:
 * <ul>
 *   <li>Ponerles nombres descriptivos para facilitar la depuración (como hacemos aquí).</li>
 *   <li>Marcar los hilos como "hilos demonio" (daemon threads).</li>
 *   <li>Establecer una prioridad específica para los hilos.</li>
 *   <li>Asignar un manejador de excepciones no capturadas.</li>
 * </ul>
 * 
 * Un `ThreadFactory` te da un control centralizado sobre todas estas propiedades. En lugar de que el
 * `ExecutorService` cree hilos por defecto, le dices: "Oye, cuando necesites un hilo nuevo, usa
 * esta fábrica para construirlo según mis especificaciones".
 */
public class NamedThreadsFactory implements ThreadFactory {

	// Un contador estático. 'static' significa que esta variable es compartida por TODAS las instancias
	// de esta fábrica. Si varios Executors usaran esta misma fábrica, el contador seguiría
	// siendo único globalmente (PoolWorker-1, PoolWorker-2, PoolWorker-3, etc.).
	private static int count = 0;
	
	// El prefijo que usaremos para nombrar a todos los hilos creados por esta fábrica.
	private static final String NAME_PREFIX = "PoolWorker-";
	
	/**
	 * Este es el único método que debemos implementar de la interfaz `ThreadFactory`.
	 * 
	 * <b>¿Quién lo llama?</b> El `ExecutorService` llama a este método automáticamente
	 * cada vez que necesita añadir un nuevo hilo a su pool. NUNCA lo llamas tú directamente.
	 * 
	 * @param r Es el objeto `Runnable` (la tarea) que el nuevo hilo deberá ejecutar.
	 *          El `ExecutorService` se encarga de pasarlo.
	 * 
	 * @return Un nuevo objeto `Thread`, configurado según nuestras reglas.
	 */
	@Override
	public Thread newThread(Runnable r) {
		// Construimos el nombre completo del hilo.
		String threadName = NAME_PREFIX + ++count;
		
		System.out.println("CREATING new thread: '" + threadName + "'");
		
		// Creamos un nuevo hilo, pasándole la tarea `r` que debe ejecutar y el nombre que acabamos de crear.
		Thread t = new Thread(r, threadName);
		
		// El `ExecutorService` recibirá este hilo y se encargará de llamar a `t.start()` internamente
		// cuando le asigne una tarea.
		return t;
	}

}
