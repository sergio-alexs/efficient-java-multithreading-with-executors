package tuts.common;

/**
 * Esta fábrica de hilos demuestra una forma robusta y predecible de personalizar hilos.
 * Combina dos conceptos:
 * <ol>
 *   <li><b>Reutilización de código:</b> Hereda de `NamedThreadsFactory` para no tener que
 *       reescribir la lógica que asigna nombres a los hilos.</li>
 *   <li><b>Personalización adicional:</b> Añade una nueva capacidad: asignar un manejador
 *       de excepciones a algunos de los hilos que crea.</li>
 * </ol>
 * 
 * El patrón que utiliza (un contador para alternar la configuración) es simple,
 * <b>determinista</b> y fácil de entender y depurar.
 */
public class NamedExceptionHandlingAlternatingThreadFactory extends NamedThreadsFactory {

	private int count = 0;
	
	/**
	 * Este método es llamado por un ExecutorService cada vez que necesita un nuevo hilo.
	 * La anotación `@Override` confirma que estamos reemplazando intencionadamente
	 * el método `newThread` de la clase padre (`NamedThreadsFactory`).
	 */
	@Override
	public Thread newThread(Runnable r) {
		// --- PASO 1: Reutilizar la lógica del padre (Delegación) ---
		// `super.newThread(r)` ejecuta el código del `newThread` de `NamedThreadsFactory`.
		// El resultado es `t`, un hilo que YA TIENE UN NOMBRE único (ej: "PoolWorker-1").
		Thread t = super.newThread(r);
		
		// --- PASO 2: Añadir nuestra propia lógica de personalización ---
		// Usamos un contador simple y predecible para aplicar la configuración de forma alternada.
		// `++count % 2 == 0` será verdadero para el 2º, 4º, 6º... hilo (los pares).
		if (++count % 2 == 0) {
			System.out.println("Attaching exception handler to thread '" + t.getName() + "'");
			
			// A los hilos pares, les asignamos un manejador de excepciones específico.
			// `ThreadExceptionNotifier` es otra implementación de `UncaughtExceptionHandler`,
			// al igual que `ThreadExceptionHandler`.
			t.setUncaughtExceptionHandler(new ThreadExceptionNotifier());
		}
		
		// Devolvemos el hilo, que ahora está doblemente personalizado:
		// - Tiene un nombre (gracias a la clase padre).
		// - Y, si es un hilo par, tiene un manejador de excepciones (gracias a esta clase).
		return t;
	}
}
