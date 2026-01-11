package tut7.threads.exceptionHandling;

import tuts.common.ExceptionLeakingTask;
import tuts.common.ThreadExceptionHandler;
import tuts.common.ThreadExceptionNotifier;

/**
 * <p>
 * <b>TUTORIAL 7: MANEJO DE EXCEPCIONES - TÉCNICA 2 (MANEJADOR POR HILO)</b>
 * </p>
 *
 * Esta clase demuestra cómo asignar un manejador de excepciones específico
 * a un hilo concreto, permitiendo un tratamiento de errores granular.
 * <br><br>
 * <b>Analogía:</b> `thread.setUncaughtExceptionHandler()` es como asignar una
 * "ambulancia personal" a un VIP. Si le pasa algo a ese VIP (hilo), su ambulancia
 * personal acude. Si le pasa algo a un ciudadano normal (otro hilo sin manejador
 * específico), se llamará al servicio de emergencias general (el manejador por defecto).
 */
public class HandlingUncaughtExceptionsDifferentlyPerThread {

	public static void main(String[] args) {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		// Asignamos su propio manejador personal al hilo t1.
		Thread t1 = new Thread(new ExceptionLeakingTask(), "MyThread-1");
		t1.setUncaughtExceptionHandler(new ThreadExceptionHandler("HANDLER-1"));
		
		// Asignamos un manejador de un tipo diferente al hilo t2.
		Thread t2 = new Thread(new ExceptionLeakingTask(), "MyThread-2");
		t2.setUncaughtExceptionHandler(new ThreadExceptionNotifier());
		
		t1.start();
		t2.start();
		
		// --- JERARQUÍA DE MANEJADORES ---
		// Cuando un hilo muere por una excepción, la JVM busca un manejador en este orden:
		// 1. Busca un manejador específico asignado al propio hilo (con `setUncaughtExceptionHandler`).
		//    Si lo encuentra, lo usa y se detiene.
		//
		// 2. Si no hay manejador de hilo, busca un manejador por defecto para toda la
		//    aplicación (con `setDefaultUncaughtExceptionHandler`). Si lo encuentra, lo usa.
		//
		// 3. Si no hay ninguno de los dos, usa el comportamiento por defecto: imprimir
		//    la traza de la excepción en la consola.
		
		// CASO DE USO: Podrías tener un manejador por defecto que solo loguea los errores.
		// Pero para un hilo súper-crítico (ej. procesar un pago), podrías asignarle un
		// manejador específico que, además de loguear, envíe una alerta por email o SMS.
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
