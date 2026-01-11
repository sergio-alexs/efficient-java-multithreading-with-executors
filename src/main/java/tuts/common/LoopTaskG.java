package tuts.common;

import java.util.concurrent.TimeUnit;

/**
 * Esta clase representa una tarea que se ejecuta en un bucle infinito y que puede
 * ser cancelada de forma segura utilizando el mecanismo de interrupción estándar de Java.
 * 
 * Es un ejemplo muy claro de cómo manejar la InterruptedException que se lanza
 * desde métodos bloqueantes como 'sleep()'.
 */
public class LoopTaskG implements Runnable {
	
	private static int count = 0;
	private int instanceNumber;
	private String taskId;
	
	@Override
	public void run() {
		String currentThreadName = Thread.currentThread().getName();
		
		System.out.println("##### [" + currentThreadName + "] <" + taskId + "> STARTING #####");
		
		// Este es un bucle infinito. La única forma de salir de él es con 'break' o 'return'.
		for (int i=1;; i++) {
			System.out.println("[" + currentThreadName + "] <" + taskId + "> TICK TICK - " + i);
			
			try {
				// Ponemos el hilo a "dormir" por un tiempo aleatorio.
				// El método sleep() es sensible a las interrupciones.
				TimeUnit.MILLISECONDS.sleep((long)(Math.random() * 3000));
				
			} catch (InterruptedException e) {
				// --- ¡AQUÍ OCURRE LA CANCELACIÓN! ---
				// Si otro hilo llama a 'hilo.interrupt()' sobre el hilo que ejecuta esta tarea
				// MIENTRAS está en sleep(), el sleep() se detiene inmediatamente y lanza
				// esta excepción, InterruptedException.
				
				System.out.println("***** [" + currentThreadName + "] <" + taskId + "> Sleep Interrupted. Cancelling ...");
				
				// Al atrapar la excepción, sabemos que nos han pedido que paremos.
				// La acción "cooperativa" que tomamos es salir del bucle.
				break;
			}
		}
		
		// Este mensaje solo se imprimirá cuando el bucle haya terminado (es decir, después de la cancelación).
		System.out.println("***** [" + currentThreadName + "] <" + taskId + "> COMPLETED *****");
	}
	
	public LoopTaskG() {
		this.instanceNumber = ++count;
		this.taskId = "LoopTaskG" + instanceNumber;
	}
}
