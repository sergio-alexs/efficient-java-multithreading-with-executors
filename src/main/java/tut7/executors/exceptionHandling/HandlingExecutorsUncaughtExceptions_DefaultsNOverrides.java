package tut7.executors.exceptionHandling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tuts.common.ExceptionLeakingTask;
import tuts.common.NamedExceptionHandlingAlternatingThreadFactory;
import tuts.common.ThreadExceptionHandler;

/**
 * <p>
 * <b>TUTORIAL 7: MANEJO DE EXCEPCIONES EN EXECUTORS - TÉCNICA 3 (JERARQUÍA COMPLETA)</b>
 * </p>
 *
 * Esta clase es el resumen final de cómo funciona la jerarquía de manejadores de
 * excepciones para tareas `Runnable` ejecutadas con `execute()`.
 * <br><br>
 * Demuestra cómo un manejador por defecto (global) puede coexistir con manejadores
 * específicos asignados por una `ThreadFactory`.
 */
public class HandlingExecutorsUncaughtExceptions_DefaultsNOverrides {

	public static void main(String[] args) {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		// 1. Establecemos el manejador por defecto para toda la JVM. Actuará como red de seguridad.
		Thread.setDefaultUncaughtExceptionHandler(new ThreadExceptionHandler("THE_DEFAULT_ONE"));
		
		// 2. Creamos un ExecutorService con una fábrica que asigna manejadores específicos
		//    SOLO a los hilos pares que crea.
		ExecutorService execService = Executors.newCachedThreadPool(
				new NamedExceptionHandlingAlternatingThreadFactory()
		);
		
		// 3. Enviamos 4 tareas que fallarán.
		execService.execute(new ExceptionLeakingTask()); // Hilo 1 (impar) -> sin manejador específico
		execService.execute(new ExceptionLeakingTask()); // Hilo 2 (par)    -> CON manejador específico
		execService.execute(new ExceptionLeakingTask()); // Hilo 3 (impar) -> sin manejador específico
		execService.execute(new ExceptionLeakingTask()); // Hilo 4 (par)    -> CON manejador específico
		
		execService.shutdown();
		
		// --- CONCLUSIÓN: LA REGLA DE ORO DEL MANEJO DE EXCEPCIONES (PARA `execute`) ---
		// Cuando un hilo muere por una excepción, la JVM busca un manejador en este orden:
		// 1. ¿Tiene el hilo un manejador específico asignado (vía `setUncaughtExceptionHandler`,
		//    normalmente por una `ThreadFactory`)? -> SI: lo usa. FIN.
		// 2. ¿No? ¿Existe un manejador por defecto para la JVM (`setDefaultUncaughtExceptionHandler`)?
		//    -> SI: lo usa. FIN.
		// 3. ¿No? -> Usa el comportamiento por defecto (imprimir en consola).
		
		// NOTA FINAL: Todo este mecanismo de `UncaughtExceptionHandler` es el camino a seguir
		// para tareas `Runnable` enviadas con `execute()`. Para tareas `Callable` enviadas
		// con `submit()`, el camino idiomático es el `try-catch` alrededor de `future.get()`.
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
