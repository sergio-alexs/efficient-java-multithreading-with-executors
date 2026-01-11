package tuts.common;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Esta clase es una tarea 'Callable' que calcula el factorial de un número.
 * Implementa un mecanismo de cancelación personalizado usando una bandera 'volatile',
 * similar al que vimos en LoopTaskE.
 */
public class FactorialTaskA implements Callable<Long> {
	
	private static int count = 0;
	private int instanceNumber;
	private String taskId;
	
	// Bandera 'volatile' para señalar la solicitud de cancelación desde otro hilo.
	private volatile boolean shutdown = false;
	
	private long a; // El número del que se calculará el factorial.
	private long sleepTime; // Pausa entre cada paso de la multiplicación.
	private long factorial; // Almacenará el resultado intermedio y final.
	
	/**
	 * El método call() contiene la lógica principal de la tarea.
	 * @return El factorial calculado, o -1 si la tarea fue cancelada.
	 */
	@Override
	public Long call() throws Exception {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("##### [" + currentThreadName + "] <" + taskId +	"> STARTING #####");
		
		factorial = 1L; // Se inicializa en 1 para el cálculo del factorial.
		
		// Bucle para calcular el factorial (n! = 1 * 2 * 3 * ... * n)
		for (long i=1; i <= a; i++) {
			factorial *= i;
			
			System.out.println("[" + currentThreadName + "] <" + taskId + "> Iteration - " + i + ". Intermediate " +
					"Result = " + factorial);
			
			// Pausa para simular un cálculo largo y dar tiempo a que pueda ser cancelado.
			TimeUnit.MILLISECONDS.sleep(sleepTime);
			
			// --- Punto de control para la cancelación ---
			// En cada iteración, se comprueba si se ha solicitado la detención.
			synchronized(this) {
				if (shutdown) {
					factorial = -1L; // Si se cancela, se establece un resultado de error.
					break;           // Se sale del bucle inmediatamente.
				}
			}
		}
		
		System.out.println("***** [" + currentThreadName + "] <" + taskId + "> COMPLETED *****");
		
		return factorial;
	}
	
	/**
	 * Método público que puede ser llamado por otro hilo para solicitar la cancelación de la tarea.
	 */
	public void cancel() {
		System.out.println("***** [" + Thread.currentThread().getName() + "] <" + taskId + "> Shutting down *****");
		
		// Se modifica la bandera de cancelación de forma segura.
		synchronized(this) {
			this.shutdown = true;
		}
	}
	
	public FactorialTaskA(long a, long sleepTime) {
		this.a = a;
		this.sleepTime = sleepTime;
		
		this.instanceNumber = ++count;
		this.taskId = "FactorialTaskA" + instanceNumber;
	}
}
