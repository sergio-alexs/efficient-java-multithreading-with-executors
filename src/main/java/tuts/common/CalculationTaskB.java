package tuts.common;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Esta clase es una tarea 'Callable' que realiza un cálculo y devuelve un resultado
 * más estructurado y descriptivo.
 * 
 * Es una mejora sobre CalculationTaskA, ya que el resultado que devuelve no es solo
 * el valor numérico, sino un objeto que contiene tanto el valor como información
 * sobre la tarea que lo produjo.
 */
public class CalculationTaskB implements Callable<TaskResult<String, Integer>> {

	private int a;
	private int b;
	private long sleepTime;
	
	private static int count = 0;
	private int instanceNumber;
	private final String taskId;
	
	public CalculationTaskB(int a, int b, long sleepTime) {
		this.a = a;
		this.b = b;
		this.sleepTime = sleepTime;
		
		this.instanceNumber = ++count;
		this.taskId = "CalculationTaskB-" + instanceNumber;
	}
	
	/**
	 * El método call() es el punto de entrada de la tarea.
	 * 
	 * Fíjate en el tipo de retorno: 'TaskResult<String, Integer>'. Esto significa que
	 * la tarea promete devolver un objeto de tipo TaskResult, que a su vez contendrá
	 * un String (para el ID de la tarea) y un Integer (para el resultado del cálculo).
	 * 
	 * @return Un objeto TaskResult que encapsula el ID de la tarea y el resultado de la suma.
	 */
	@Override
	public TaskResult<String, Integer> call() throws Exception {
		String currentThreadName = Thread.currentThread().getName();
		
		System.out.println("##### [" + currentThreadName + "] <" + taskId + "> STARTING #####");
		
		TimeUnit.MILLISECONDS.sleep(sleepTime);
		
		System.out.println("***** [" + currentThreadName + "] <" + taskId + "> CALCULATION COMPLETED *****");
		
		// --- ¡AQUÍ ESTÁ LA MEJORA! ---
		// En lugar de devolver solo 'a + b', creamos un nuevo objeto 'TaskResult'.
		// Este objeto contiene tanto el ID de esta tarea ('taskId') como el resultado ('a + b').
		// Esto es muy útil porque cuando recibimos el resultado, también sabemos
		// de qué tarea específica proviene, sin tener que adivinarlo.
		return new TaskResult<>(taskId, a + b);
	}
	
}
