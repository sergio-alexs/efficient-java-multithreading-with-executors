package tut7.executors.exceptionHandling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tuts.common.ExceptionLeakingTask;
import tuts.common.ThreadExceptionHandler;

/**
 * <p>
 * <b>TUTORIAL 7: MANEJO DE EXCEPCIONES EN EXECUTORS - TÉCNICA 1 (TAREAS 'RUNNABLE')</b>
 * </p>
 *
 * Esta clase demuestra cómo se manejan las excepciones para tareas `Runnable`
 * que se ejecutan con `executor.execute()`.
 * <br><br>
 * <b>Concepto Clave: Bifurcación del Manejo de Errores en Executors</b>
 * <ul>
 *     <li><b>`submit(Callable)`:</b> El error se captura y se guarda en el objeto `Future`.
 *         Se recupera al llamar a `future.get()` (envuelto en una `ExecutionException`).
 *         <b>El `UncaughtExceptionHandler` NO es invocado.</b></li>
 *     <li><b>`execute(Runnable)`:</b> El error "escapa" del hilo. No hay `Future` para capturarlo.
 *         La excepción es manejada por el `UncaughtExceptionHandler` del hilo, siguiendo
 *         la misma jerarquía que vimos con hilos manuales.</li>
 * </ul>
 * Este ejemplo se centra en el segundo caso: `execute(Runnable)`.
 */
public class HandlingExecutorsUncaughtExceptionsForEveryThread {

	public static void main(String[] args) {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		// Establecemos un manejador por defecto para toda la aplicación.
		// Será nuestra red de seguridad para cualquier hilo que no tenga un manejador propio.
		Thread.setDefaultUncaughtExceptionHandler(new ThreadExceptionHandler("DEFAULT_HANDLER"));
		
		// Creamos un ExecutorService simple, sin una `ThreadFactory` personalizada.
		// Esto significa que los hilos que cree no tendrán un `UncaughtExceptionHandler` específico.
		ExecutorService execService = Executors.newCachedThreadPool();
		
		// Ejecutamos tareas que fallan usando `execute()`.
		execService.execute(new ExceptionLeakingTask());
		execService.execute(new ExceptionLeakingTask());
		execService.execute(new ExceptionLeakingTask());
		
		execService.shutdown();
		
		// Al usar `execute()`, la excepción de la tarea se propagará hacia arriba.
		// Como los hilos del pool no tienen un manejador específico, la JVM usará
		// el manejador por defecto que hemos configurado para todos ellos.
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
