package tut7.threads.exceptionHandling;

import tuts.common.ExceptionLeakingTask;
import tuts.common.ThreadExceptionHandler;

/**
 * <p>
 * <b>TUTORIAL 7: MANEJO DE EXCEPCIONES - TÉCNICA 1 (EL MANEJADOR POR DEFECTO)</b>
 * </p>
 *
 * <b>El Problema:</b> Cuando una excepción no es capturada dentro del método `run()` de un hilo,
 * ese hilo muere abruptamente. Por defecto, la traza del error se imprime en la consola,
 * pero el resto de la aplicación (incluido el hilo principal) no se entera de nada.
 * Esto puede dejar a la aplicación en un estado inconsistente.
 * <br><br>
 * Esta clase demuestra cómo establecer un "manejador de excepciones por defecto" para
 * toda la aplicación, que actuará como una red de seguridad global.
 * <br><br>
 * <b>Analogía:</b> `Thread.setDefaultUncaughtExceptionHandler()` es como instalar
 * la "caja negra" en toda una aerolínea (la JVM). Si cualquier avión (hilo) se estrella,
 * la caja negra registrará lo que ocurrió.
 */
public class HandlingUncaughtExceptionsForEveryThread {

	public static void main(String[] args) {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		// --- ¡AQUÍ ESTÁ LA CLAVE! ---
		// Registramos una instancia de nuestro manejador personalizado como el
		// manejador por defecto para toda la JVM.
		// A partir de ahora, cualquier excepción no capturada en CUALQUIER hilo
		// que no tenga un manejador propio, será enviada aquí.
		Thread.setDefaultUncaughtExceptionHandler(new ThreadExceptionHandler("DEFAULT_HANDLER"));
		
		// Todas estas tareas lanzarán una RuntimeException y morirán.
		new Thread(new ExceptionLeakingTask(), "MyThread-1").start();
		new Thread(new ExceptionLeakingTask(), "MyThread-2").start();
		new Thread(new ExceptionLeakingTask(), "MyThread-3").start();
		new Thread(new ExceptionLeakingTask(), "MyThread-4").start();
		
		// --- FLUJO DE EVENTOS PARA CADA HILO ---
		// 1. `ExceptionLeakingTask` lanza una `RuntimeException`.
		// 2. El método `run()` termina abruptamente.
		// 3. La JVM busca si el hilo tiene un manejador específico (`setUncaughtExceptionHandler`). No lo tiene.
		// 4. La JVM busca si hay un manejador por defecto (`setDefaultUncaughtExceptionHandler`). ¡Sí lo hay!
		// 5. La JVM invoca el método `uncaughtException()` de nuestro "DEFAULT_HANDLER".
		// 6. El hilo muere.
		
		// Es importante entender que el manejador NO PREVIENE la muerte del hilo.
		// Solo nos permite REACCIONAR a ella (loguear el error, notificar, etc.).
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
