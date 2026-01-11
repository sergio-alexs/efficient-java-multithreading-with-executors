package tuts.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Esta clase representa una tarea de larga duración que devuelve un resultado (es un 'Callable'),
 * y que además puede ser cancelada a través del mecanismo de interrupción de Java.
 */
public class CalculationTaskC implements Callable<Long> {
	
	private static int count = 0;
	private int instanceNumber;
	private String taskId;
	
	private final int DATASET_SIZE = 100000;
	
	// Una bandera para recordar si la tarea fue interrumpida.
	private boolean isThreadInterrupted = false;
	
	/**
	 * El método call() es el punto de entrada de la tarea.
	 * Promete devolver un 'Long' como resultado.
	 */
	@Override
	public Long call() throws Exception {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("##### [" + currentThreadName + "] <" + taskId + "> STARTING #####");
		
		long totalTimeTakenInMillis = 0;
		
		// El bucle se ejecutará 10 veces, a menos que sea interrumpido antes.
		for (int i=0; i < 10; i++) {
			System.out.println("[" + currentThreadName + "] <" + taskId + "> CURRENT RUNNING AVERAGE = " + 
					(i == 0 ? 0 : totalTimeTakenInMillis / (2 * i)));
			
			// Realiza un cálculo que consume tiempo y mide cuánto tardó.
			long timeTakenInMillis = doComplexCalculation();
			totalTimeTakenInMillis += timeTakenInMillis;
			
			// --- Punto de control para la cancelación ---
			// En cada iteración, comprueba si se ha solicitado la interrupción.
			if (Thread.interrupted()) {
				System.out.println("[" + currentThreadName + "] <" + taskId + "> Interrupted. Cancelling ...");
				isThreadInterrupted = true; // Guarda el hecho de que fue interrumpida.
				break; // Sale del bucle prematuramente.
			}
		}
		
		// Esta línea siempre imprimirá 'false' porque Thread.interrupted() limpia la bandera de interrupción.
		System.out.println("[" + currentThreadName + "] <" + taskId + "> Retrieving 'INTERRUPTED' status again : " +
				Thread.interrupted());
		
		System.out.println("***** [" + currentThreadName + "] <" + taskId +	"> COMPLETED *****");
		
		// --- Devolución del resultado ---
		// Si la tarea fue interrumpida, devuelve -1 como código de error.
		// Si completó todo el bucle, devuelve el tiempo promedio de cálculo.
		return isThreadInterrupted ? -1 : totalTimeTakenInMillis / (2 * 10);
	}
	
	/**
	 * Simula un trabajo de cálculo intensivo y devuelve el tiempo que tardó en milisegundos.
	 */
	private long doComplexCalculation() {
		long startTime = System.currentTimeMillis();
		
		for (int i = 0; i < 2; i++) {
			Collections.sort(generateDataSet());
		}
		
		return System.currentTimeMillis() - startTime;
	}
	
	/**
	 * Genera una lista de números aleatorios.
	 */
	private List<Integer> generateDataSet() {
		List<Integer> intList = new ArrayList<>();
		Random randomGenerator = new Random();
		
		for (int i = 0; i < DATASET_SIZE; i++) {
			intList.add(randomGenerator.nextInt(DATASET_SIZE));
		}
		
		return intList;
	}
	
	public CalculationTaskC() {
		this.instanceNumber = ++count;
		this.taskId = "CalculationTaskC" + instanceNumber;
	}
}
