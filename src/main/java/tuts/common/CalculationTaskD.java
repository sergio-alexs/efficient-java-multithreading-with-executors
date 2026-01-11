package tuts.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import tuts.utils.TimeUtils;

/**
 * Esta clase representa una tarea 'Callable' que realiza un cálculo simple (una suma)
 * después de una pausa simulada. También registra y muestra información detallada
 * sobre su tiempo de ejecución.
 */
public class CalculationTaskD implements Callable<Integer> {

	private int a;
	private int b;
	private long sleepTime;
	
	private static int count = 0;
	private int instanceNumber;
	private String taskId;
	
	// Un formateador de fecha para mostrar las horas de inicio y fin de forma legible.
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss.SSS");
	
	/**
	 * Constructor de la tarea.
	 * @param a El primer número a sumar.
	 * @param b El segundo número a sumar.
	 * @param sleepTime El tiempo en milisegundos que la tarea "dormirá" para simular trabajo.
	 */
	public CalculationTaskD(int a, int b, long sleepTime) {
		this.a = a;
		this.b = b;
		this.sleepTime = sleepTime;
		
		this.instanceNumber = ++count;
		this.taskId = "CalculationTaskD-" + instanceNumber;
	}
	
	/**
	 * El método call() es el punto de entrada de la tarea.
	 * Promete devolver un 'Integer' como resultado.
	 */
	@Override
	public Integer call() throws Exception {
		// Registra la hora de inicio.
		Date startTime = new Date();
		String currentThreadName = Thread.currentThread().getName();
		
		System.out.println("##### [" + currentThreadName + "] <" + taskId + "> STARTED at " + 
				dateFormatter.format(startTime) + " #####");
		
		try {
			// Pone el hilo a dormir para simular un cálculo que consume tiempo.
			// A diferencia de la interrupción manual, si este hilo es interrumpido
			// mientras está en sleep(), lanzará una InterruptedException que hará
			// que el método call() falle, lo cual es el comportamiento por defecto.
			TimeUnit.MILLISECONDS.sleep(sleepTime);
		} catch (InterruptedException e) {
			System.out.println("***** [" + currentThreadName + "] <" + taskId + "> Interrupted while sleeping *****");
			// Si la tarea es interrumpida, la excepción se propaga.
			// El ExecutorService la capturará y la envolverá en una ExecutionException
			// cuando se llame a Future.get().
			throw e;
		}
		
		// Registra la hora de finalización.
		Date endTime = new Date();
		
		// Imprime un resumen de la ejecución, incluyendo la duración total.
		System.out.println("***** [" + currentThreadName + "] <" + taskId + "> CALCULATION COMPLETED at " + 
				dateFormatter.format(endTime) + " *****" +
				"\n\tRUN DURATION     : " + TimeUtils.getTimeDifferenceInSeconds(startTime, endTime) + "\n");
		
		// Devuelve el resultado de la suma.
		return a + b;
	}
	
}
