package tuts.common;

import java.util.concurrent.TimeUnit;

/**
 * Esta clase representa una tarea que puede ser interrumpida en diferentes puntos
 * de su ejecución: tanto cuando está "durmiendo" (en sleep) como cuando está
 * activamente trabajando. Demuestra un patrón de cancelación más robusto.
 */
public class LoopTaskH implements Runnable {
	
	private static int count = 0;
	private int instanceNumber;
	private String taskId;
	
	// Una bandera para recordar si la interrupción ocurrió específicamente durante el sleep.
	private boolean sleepInterrupted = false;
	
	@Override
	public void run() {
		String currentThreadName = Thread.currentThread().getName();
		
		System.out.println("##### [" + currentThreadName + "] <" + taskId + "> STARTING #####");
		
		// Bucle infinito que se detendrá cuando se detecte una interrupción.
		for (int i=1;; i++) {
			System.out.println("[" + currentThreadName + "] <" + taskId + "> TICK TICK - " + i);
			
			try {
				// La tarea duerme por un tiempo.
				TimeUnit.MILLISECONDS.sleep((long)(Math.random() * 3000));
			} catch (InterruptedException e) {
				// --- PUNTO DE CONTROL 1: Interrupción durante el sleep ---
				// Si la interrupción ocurre aquí, el sleep() lanza la excepción.
				System.out.println("***** [" + currentThreadName + "] <" + taskId + "> Sleep Interrupted. SETTING THE FLAG ...");
				// Levantamos nuestra bandera local para recordar que la interrupción ocurrió aquí.
				// La bandera de interrupción del hilo es limpiada por la excepción, así que necesitamos la nuestra.
				sleepInterrupted = true;
			}
			
			// La tarea simula hacer más trabajo después de despertar.
			doSomeMoreWork();
			
			// --- PUNTO DE CONTROL 2: Comprobación explícita de interrupción ---
			// Este es el punto de control para ambos escenarios de interrupción.
			//
			// Escenario A: La interrupción ocurrió durante el sleep().
			// En este caso, 'sleepInterrupted' será 'true' y la condición se cumplirá.
			//
			// Escenario B: La interrupción ocurrió durante doSomeMoreWork().
			// En este caso, 'sleepInterrupted' será 'false', pero 'Thread.interrupted()'
			// devolverá 'true', por lo que la condición también se cumplirá.
			if (sleepInterrupted || Thread.interrupted()) {
				System.out.println("***** [" + currentThreadName + "] <" + taskId + "> INTERRUPTED. Cancelling ...");
				break; // Salimos del bucle y terminamos la tarea.
			}
		}
		
		System.out.println("***** [" + currentThreadName + "] <" + taskId + "> COMPLETED *****");
	}
	
	/**
	 * Simula trabajo adicional que consume CPU.
	 */
	private void doSomeMoreWork() {
		System.out.println("***** [" + Thread.currentThread().getName() + "] <" + taskId + "> DOING SOME MORE WORK ...");
	}
	
	public LoopTaskH() {
		this.instanceNumber = ++count;
		this.taskId = "LoopTaskH" + instanceNumber;
	}
}
