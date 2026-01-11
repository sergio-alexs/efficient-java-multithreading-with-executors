package tuts.common;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Esta clase es una versión mejorada de FactorialTaskA.
 * Calcula el factorial de un número y puede ser cancelada, pero utiliza el
 * mecanismo de INTERRUPCIÓN estándar de Java, que es la forma recomendada.
 */
public class FactorialTaskB implements Callable<Long> {
	
	private static int count = 0;
	private int instanceNumber;
	private String taskId;
	
	private long a;
	private long sleepTime;
	private long factorial;
	
	/**
	 * El método call() contiene la lógica de la tarea.
	 * A diferencia de la versión anterior, no necesita 'throws Exception' porque
	 * la InterruptedException se maneja internamente.
	 * 
	 * @return El factorial calculado, o -1 si la tarea fue cancelada.
	 */
	@Override
	public Long call() {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("##### [" + currentThreadName + "] <" + taskId +	"> STARTING #####");
		
		factorial = 1L;
		
		// Bucle para calcular el factorial.
		for (long i=1; i <= a; i++) {
			factorial *= i;
			
			System.out.println("[" + currentThreadName + "] <" + taskId + "> Iteration - " + i + ". Intermediate " +
					"Result = " + factorial);
			
			// --- ¡AQUÍ ESTÁ LA CLAVE DEL NUEVO MECANISMO! ---
			try {
				// Ponemos el hilo a dormir.
				TimeUnit.MILLISECONDS.sleep(sleepTime);
			} catch (InterruptedException e) {
				// Si otro hilo llama a 'hilo.interrupt()' MIENTRAS este hilo está en sleep(),
				// el sleep() se interrumpe inmediatamente y se lanza una InterruptedException.
				// Esto nos permite reaccionar a la solicitud de cancelación.
				
				System.out.println("***** [" + currentThreadName + "] <" + taskId + "> Sleep Interrupted. Cancelling ...");
				factorial = -1L; // Marcamos el resultado como inválido.
				
				// Es importante limpiar la bandera de interrupción si no queremos que se propague.
				// En este caso, como salimos del bucle, no es estrictamente necesario, pero es buena práctica.
				Thread.currentThread().interrupt(); // Re-establece la bandera de interrupción.
				
				break; // Salimos del bucle para terminar la tarea.
			}
		}
		
		System.out.println("***** [" + currentThreadName + "] <" + taskId + "> COMPLETED *****");
		
		return factorial;
	}
	
	// Fíjate que ya no hay un método cancel(). La cancelación se manejará desde fuera
	// llamando a 'future.cancel(true)', que es lo que causa la interrupción.
	
	public FactorialTaskB(long a, long sleepTime) {
		this.a = a;
		this.sleepTime = sleepTime;
		
		this.instanceNumber = ++count;
		this.taskId = "FactorialTaskB" + instanceNumber;
	}
}
