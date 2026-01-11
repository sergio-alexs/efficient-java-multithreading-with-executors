package tuts.common;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Arrays;
import java.util.List;

/**
 * Esta fábrica de hilos, al igual que las otras, personaliza los hilos antes de
 * que un ExecutorService los utilice.
 * 
 * En este caso, reutiliza la lógica de nombramiento de `NamedThreadsFactory` y, además,
 * asigna un manejador de excepciones a cada hilo que crea, seleccionándolo de una
 * lista de manejadores disponibles.
 *
 * <b>NOTA IMPORTANTE:</b> La forma en que selecciona el manejador (basada en el tiempo)
 * es <b>no determinista</b>, lo que generalmente no es una buena práctica para una fábrica.
 * Se muestra aquí con fines educativos para contrastar con un enfoque determinista
 * (como el de `NamedExceptionHandlingAlternatingThreadFactory`).
 */
public class NamedExceptionHandlingThreadFactory extends NamedThreadsFactory {

	// Una lista que contiene las diferentes "plantillas" de manejadores de excepciones que podemos usar.
	// En este caso, tenemos dos tipos diferentes: ThreadExceptionHandler y ThreadExceptionNotifier.
	private List<UncaughtExceptionHandler> handlers = Arrays.asList(
			new ThreadExceptionHandler(), 
			new ThreadExceptionNotifier()
	);
	
	/**
	 * Este método es llamado por un ExecutorService cada vez que necesita un nuevo hilo.
	 */
	@Override
	public Thread newThread(Runnable r) {
		// 1. Obtenemos un hilo con nombre de la clase padre (reutilización de código).
		Thread t = super.newThread(r);
		
		// 2. --- Lógica de selección del manejador (NO DETERMINISTA) ---
		// `System.currentTimeMillis()` devuelve el número de milisegundos desde 1970.
		// El operador '%' (módulo) calcula el resto de una división.
		// `milisegundos % handlers.size()` (que es 2) dará 0 si el número es par y 1 si es impar.
		//
		// ADVERTENCIA: Como el tiempo cambia constantemente, esta selección es "pseudo-aleatoria".
		// Si ejecutas el mismo programa dos veces, un hilo con el mismo nombre (ej: "PoolWorker-1")
		// podría tener un manejador diferente cada vez. Esto hace que el comportamiento sea
		// impredecible y difícil de depurar. Un enfoque con un contador (`count++ % size`)
		// es casi siempre preferible.
		int handlerIndex = (int) (System.currentTimeMillis() % handlers.size());
		
		UncaughtExceptionHandler handler = handlers.get(handlerIndex);
		System.out.println("Assigning handler '" + handler.getClass().getSimpleName() + "' to thread '" + t.getName() + "'");

		// 3. Asignamos el manejador de excepciones seleccionado al hilo.
		t.setUncaughtExceptionHandler(handler);
		
		// 4. Devolvemos el hilo doblemente personalizado.
		return t;
	}
}
