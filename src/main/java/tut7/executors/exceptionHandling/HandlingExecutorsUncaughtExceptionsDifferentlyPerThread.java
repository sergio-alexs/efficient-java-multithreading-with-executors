package tut7.executors.exceptionHandling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tuts.common.ExceptionLeakingTask;
import tuts.common.NamedExceptionHandlingThreadFactory;

/**
 * <p>
 * <b>TUTORIAL 7: MANEJO DE EXCEPCIONES EN EXECUTORS - TÉCNICA 2 (USANDO THREADFACTORY)</b>
 * </p>
 *
 * Esta clase demuestra la forma idiomática de manejar excepciones para tareas `Runnable`
 * ejecutadas con `execute()`: usar una `ThreadFactory` personalizada que asigne
 * `UncaughtExceptionHandler`s a los hilos que crea.
 * <br><br>
 * <b>Concepto Clave:</b> La `ThreadFactory` es nuestro único punto de control para
 * configurar los hilos (nombre, estado de demonio, manejador de excepciones, etc.)
 * que un `ExecutorService` gestiona.
 */
public class HandlingExecutorsUncaughtExceptionsDifferentlyPerThread {

	public static void main(String[] args) {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		// Creamos un ExecutorService, pero esta vez le pasamos nuestra fábrica de hilos personalizada.
		// `NamedExceptionHandlingThreadFactory` tiene la lógica para asignar un manejador
		// de excepciones a cada nuevo hilo que el executor le pida.
		ExecutorService execService = Executors.newCachedThreadPool(new NamedExceptionHandlingThreadFactory());
		
		// Enviamos tareas que fallan usando `execute()`.
		execService.execute(new ExceptionLeakingTask());
		execService.execute(new ExceptionLeakingTask());
		execService.execute(new ExceptionLeakingTask());
		execService.execute(new ExceptionLeakingTask());
		
		execService.shutdown();
		
		// --- FLUJO DE EVENTOS ---
		// 1. `execute()` envía una tarea.
		// 2. `ExecutorService` necesita un hilo y se lo pide a nuestra `ThreadFactory`.
		// 3. Nuestra `ThreadFactory` crea un nuevo hilo, le pone un nombre y le asigna
		//    un `UncaughtExceptionHandler` específico con `t.setUncaughtExceptionHandler(...)`.
		// 4. El hilo ejecuta la tarea, que lanza una excepción.
		// 5. La JVM ve que el hilo tiene un manejador específico y lo invoca.
		
		// Este patrón nos da control total sobre el manejo de errores para tareas
		// que no devuelven resultado, sin tener que modificar el código de las tareas.
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
