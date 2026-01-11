package tut7.threads.exceptionHandling;

import java.lang.Thread.UncaughtExceptionHandler;

import tuts.common.ExceptionLeakingTask;
import tuts.common.ThreadExceptionHandler;
import tuts.common.ThreadExceptionNotifier;

/**
 * <p>
 * <b>TUTORIAL 7: MANEJO DE EXCEPCIONES - TÉCNICA 3 (JERARQUÍA Y PRECEDENCIA)</b>
 * </p>
 *
 * Esta clase demuestra la jerarquía entre los manejadores de excepciones por defecto
 * y los manejadores específicos de cada hilo.
 * <br><br>
 * <b>Analogía de la Ambulancia:</b>
 * <ul>
 *     <li><b>Manejador por Defecto:</b> Es la "ambulancia del hospital del distrito".
 *         Atiende a cualquier persona (hilo) que tenga una emergencia.</li>
 *     <li><b>Manejador Específico:</b> Es la "ambulancia privada de un VIP". Si el VIP
 *         (un hilo específico) tiene una emergencia, su ambulancia privada tiene
 *         prioridad absoluta y la del distrito no interviene.</li>
 * </ul>
 */
public class HandlingUncaughtExceptions_DefaultsNOverrides {

	public static void main(String[] args) {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		// 1. Establecemos la "ambulancia del distrito" para toda la JVM.
		Thread.setDefaultUncaughtExceptionHandler(new ThreadExceptionHandler("THE_DEFAULT_ONE"));
		
		// --- CREACIÓN DE HILOS ---
		
		// Hilo 1: Un ciudadano normal. Si falla, será atendido por el manejador por defecto.
		Thread t1 = new Thread(new ExceptionLeakingTask(), "MyThread-1");
		
		// Hilo 2: Un VIP con su propia ambulancia privada.
		Thread t2 = new Thread(new ExceptionLeakingTask(), "MyThread-2");
		t2.setUncaughtExceptionHandler(new ThreadExceptionNotifier()); // Asignamos su manejador específico.
		
		// Hilo 3: Otro ciudadano normal. También usará el manejador por defecto.
		Thread t3 = new Thread(new ExceptionLeakingTask(), "MyThread-3");
		
		// Hilo 4: Otro VIP.
		Thread t4 = new Thread(new ExceptionLeakingTask(), "MyThread-4");
		t4.setUncaughtExceptionHandler(new ThreadExceptionNotifier());
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		// --- REGLA DE PRECEDENCIA ---
		// El manejador específico de un hilo SIEMPRE tiene prioridad sobre el manejador por defecto.
		//
		// SALIDA ESPERADA:
		// - Las excepciones de t1 y t3 serán atrapadas por "THE_DEFAULT_ONE".
		// - Las excepciones de t2 y t4 serán atrapadas por "ThreadExceptionNotifier".

		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
