package tuts.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import tuts.utils.TimeUtils;

/**
 * Una tarea que puede ser ejecutada por un hilo.
 * Implementa la interfaz Runnable, que es la forma más básica de crear una tarea en Java para la ejecución concurrente.
 */
public class ScheduledTaskB implements Runnable {

	// Tiempo que la tarea "dormirá" (simulando trabajo) en milisegundos.
	private long sleepTime;
	
	// Contador estático para llevar la cuenta de cuántas instancias de esta tarea se han creado.
	private static int count = 0;
	// Número de instancia para esta tarea específica.
	private int instanceNumber;
	// Identificador único para esta tarea.
	private String taskId;
	
	// Formateador de fecha para mostrar las horas de ejecución de forma legible.
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss.SSS");
	
	/**
	 * Constructor de la tarea.
	 * @param sleepTime El tiempo en milisegundos que la tarea debe simular trabajo.
	 */
	public ScheduledTaskB(long sleepTime) {
		this.sleepTime = sleepTime;
		
		// Asigna un número de instancia y un ID único a esta tarea.
		this.instanceNumber = ++count;
		this.taskId = "ScheduledTaskB-" + instanceNumber;
	}
	
	/**
	 * El método principal que se ejecuta cuando la tarea es iniciada por un hilo.
	 * Este es el método requerido por la interfaz Runnable.
	 */
	@Override
	public void run() {
		// Registra la hora de inicio de la tarea.
		Date startTime = new Date();
		
		// Obtiene el nombre del hilo que está ejecutando esta tarea.
		String currentThreadName = Thread.currentThread().getName();
		
		// Imprime un mensaje indicando que la tarea ha comenzado.
		System.out.println("##### [" + currentThreadName + "] <" + taskId + 
				"> STARTED AT : " + dateFormatter.format(startTime) + " #####");
		
		try {
			// Simula un trabajo que consume tiempo haciendo que el hilo duerma.
			TimeUnit.MILLISECONDS.sleep(sleepTime);
		} catch (InterruptedException e) {
			// Maneja la excepción si el hilo es interrumpido mientras duerme.
			e.printStackTrace();
		}
		
		// Registra la hora de finalización de la tarea.
		Date endTime = new Date();
		
		// Imprime información sobre la finalización de la tarea y su duración total.
		System.out.println("***** [" + currentThreadName + "] <" + taskId + 
				"> COMPLETED AT : " + dateFormatter.format(endTime) + " *****" +
				"\n\tRUN DURATION     : " + TimeUtils.getTimeDifferenceInSeconds(startTime,  endTime) + "\n");
	}
}
