package tuts.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import tuts.utils.TimeUtils;

/**
 * Una tarea que puede ser programada para ejecutarse en un momento determinado.
 * Extiende TimerTask, lo que permite que sea ejecutada por un Timer.
 */
public class ScheduledTaskA extends TimerTask {

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
	public ScheduledTaskA(long sleepTime) {
		this.sleepTime = sleepTime;
		
		// Asigna un número de instancia y un ID único a esta tarea.
		this.instanceNumber = ++count;
		this.taskId = "ScheduledTaskA-" + instanceNumber;
	}
	
	/**
	 * El método principal que se ejecuta cuando la tarea es iniciada por el planificador.
	 */
	@Override
	public void run() {
		// Registra la hora de inicio real de la tarea.
		Date startTime = new Date();
		// Obtiene la hora en que la tarea estaba programada para ejecutarse.
		Date scheduledTime = new Date(super.scheduledExecutionTime());
		
		// Obtiene el nombre del hilo que está ejecutando esta tarea.
		String currentThreadName = Thread.currentThread().getName();
		
		// Imprime información sobre cuándo estaba programada la tarea y cuándo comenzó realmente.
		System.out.println("##### [" + currentThreadName + "] <" + taskId + 
				"> SCHEDULED TO RUN AT : " + dateFormatter.format(scheduledTime) + 
				", ACTUALLY STARTED AT : " + dateFormatter.format(startTime) + " #####");
		
		try {
			// Simula un trabajo que consume tiempo haciendo que el hilo duerma.
			TimeUnit.MILLISECONDS.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// Maneja la excepción si el hilo es interrumpido mientras duerme.
			e.printStackTrace();
		}
		
		// Registra la hora de finalización de la tarea.
		Date endTime = new Date();
		
		// Imprime información sobre la finalización de la tarea, incluyendo el retraso y la duración.
		System.out.println("***** [" + currentThreadName + "] <" + taskId + 
				"> COMPLETED AT : " + dateFormatter.format(endTime) + " *****" +
				"\n\tDELAYED START BY : " + TimeUtils.getTimeDifferenceInSeconds(scheduledTime,  startTime) +
				"\n\tRUN DURATION     : " + TimeUtils.getTimeDifferenceInSeconds(startTime,  endTime) + "\n");
	}
}
