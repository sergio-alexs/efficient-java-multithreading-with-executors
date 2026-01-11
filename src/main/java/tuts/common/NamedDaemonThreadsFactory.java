package tuts.common;

/**
 * Esta fábrica de hilos es una especialización de `NamedThreadsFactory`.
 * 
 * <h2>Herencia y Reutilización de Código</h2>
 * Esta clase `extends NamedThreadsFactory`. Esto significa que "hereda" toda la funcionalidad
 * de su clase padre, en este caso, la capacidad de crear hilos con nombres como "PoolWorker-1".
 * 
 * Luego, "sobreescribe" (`@Override`) el método `newThread` para añadir una nueva
 * personalización: marcar algunos de los hilos como "hilos demonio".
 * 
 * <h2>¿Qué es un Hilo Demonio (Daemon Thread)?</h2>
 * En Java, hay dos tipos de hilos:
 * <ul>
 *   <li><b>Hilos de Usuario (User Threads):</b> Son los hilos normales. La aplicación (la JVM)
 *       NO se cerrará mientras al menos un hilo de usuario siga en ejecución. El hilo `main` es
 *       un hilo de usuario.</li>
 *   <li><b>Hilos Demonio (Daemon Threads):</b> Son hilos de "segundo plano" o de "servicio".
 *       La JVM SÍ se cerrará aunque haya hilos demonio en ejecución, siempre y cuando ya no quede
 *       ningún hilo de usuario. Los hilos demonio son terminados abruptamente.</li>
 * </ul>
 * <b>Analogía:</b> Imagina que te vas del trabajo. Las "tareas críticas" (hilos de usuario) son
 * las que debes terminar antes de poder irte. La "música de fondo" (hilos demonio) simplemente
 * se apaga cuando te vas, no esperas a que la canción termine.
 */
public class NamedDaemonThreadsFactory extends NamedThreadsFactory {

	private static int count = 0;
	
	/**
	 * Este método es llamado por un ExecutorService cada vez que necesita un nuevo hilo.
	 * La anotación `@Override` indica que estamos reemplazando el método `newThread` de la
	 * clase padre (`NamedThreadsFactory`).
	 */
	@Override
	public Thread newThread(Runnable r) {
		// 1. Reutilizamos la lógica del padre.
		// `super.newThread(r)` llama al método `newThread` de `NamedThreadsFactory`.
		// Esto nos da un hilo ya configurado con un nombre único (ej: "PoolWorker-1").
		Thread t = super.newThread(r);
		
		// 2. Aplicamos una lógica de ejemplo para alternar entre hilos de usuario y demonio.
		// `++count % 2 == 0` será verdadero para el 2º, 4º, 6º... hilo que se cree.
		// No es un patrón común en producción, pero es útil para demostrar ambos tipos de hilos.
		if (++count % 2 == 0) {
			
			// 3. --- ¡LA PERSONALIZACIÓN ADICIONAL! ---
			// Si la condición se cumple, marcamos el hilo como demonio.
			// Esto debe hacerse ANTES de que el hilo se inicie.
			System.out.println("~~~~ " + t.getName() + " is a DAEMON thread ~~~~");
			t.setDaemon(true);
		} else {
			System.out.println("++++ " + t.getName() + " is a USER thread ++++");
		}
		
		// 4. Devolvemos el hilo personalizado al ExecutorService.
		// Dependiendo del contador, el Executor recibirá a veces un hilo de usuario y a veces un demonio.
		return t;
	}
	
}
