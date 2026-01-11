package tuts.common;

import java.util.concurrent.TimeUnit;

/**
 * LoopTaskB es otra tarea que implementa la interfaz Runnable.
 * Al igual que LoopTaskA, su lógica principal se encuentra en el método run().
 * Una característica distintiva de esta clase es que establece un nombre personalizado
 * para el hilo que la ejecuta, lo que facilita la depuración y el seguimiento de hilos.
 */
public class LoopTaskB implements Runnable {
	
	// Variable estática para llevar la cuenta de cuántas instancias de LoopTaskB se han creado.
	private static int count = 0;
	// Número de instancia para este objeto específico.
	private final int instanceNumber;
	// Identificador único para esta tarea, generado a partir del número de instancia.
	private final String taskId;
	
	/**
	 * El método run() contiene el código que se ejecutará en un hilo separado.
	 */
	@Override
	public void run() {
		// Establece el nombre del hilo actual. Esto es muy útil para la depuración,
		// ya que los nombres de hilo por defecto (ej. "pool-1-thread-1") no son muy descriptivos.
		Thread.currentThread().setName("Worker-" + instanceNumber);
		// Obtiene el nombre del hilo actual para usarlo en los mensajes de registro.
		String currentThreadName = Thread.currentThread().getName();
		
		// Imprime un mensaje de inicio, incluyendo el nombre del hilo y el ID de la tarea.
		System.out.println("##### [" + currentThreadName + "] <" + taskId + 
				"> STARTING #####");
		
		// Bucle de cuenta regresiva que simula una tarea en ejecución.
		for (int i=10; i>0; i--) {
			// Imprime el progreso, mostrando qué hilo y qué tarea están trabajando.
			System.out.println("[" + currentThreadName + "] <" + taskId + 
					"> TICK TICK - " + i);
			
			try {
				// Pausa la ejecución del hilo por un tiempo aleatorio para simular trabajo.
				TimeUnit.MILLISECONDS.sleep((long)(Math.random() * 1000));
			} catch (InterruptedException e) {
				// Maneja la excepción que ocurre si el hilo es interrumpido mientras duerme.
				e.printStackTrace();
			}
		}
		
		// Imprime un mensaje de finalización.
		System.out.println("***** [" + currentThreadName + "] <" + taskId + 
				"> COMPLETED *****");
	}
	
	/**
	 * Constructor de LoopTaskB.
	 * Asigna un número de instancia único y genera un taskId para cada nueva tarea.
	 */
	public LoopTaskB() {
		this.instanceNumber = ++count;
		this.taskId = "LoopTaskB" + instanceNumber;
	}
}