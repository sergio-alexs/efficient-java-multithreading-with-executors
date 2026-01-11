package tut9.executors.scheduling;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import tuts.common.NamedThreadsFactory;
import tuts.common.ScheduledTaskB;
import tuts.utils.TimeUtils;

/**
 * Esta clase demuestra cómo programar tareas repetitivas con un retraso fijo (Fixed Delay)
 * utilizando ScheduledExecutorService.
 *
 * A diferencia de 'Fixed Rate', 'Fixed Delay' espera a que la tarea termine antes de contar
 * el tiempo de retraso para la siguiente ejecución.
 */
public class SchedulingTasksForFixedDelayRepeatedExecutionsUsingExecutors {
	
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss.SSS");

	public static void main(String[] args) throws InterruptedException {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("[" + currentThreadName + "] Main thread starts here...");
		
		// Crea un pool de hilos programados con 3 hilos.
		// NamedThreadsFactory se usa para dar nombres personalizados a los hilos.
		ScheduledExecutorService execService = Executors.newScheduledThreadPool(3, new NamedThreadsFactory());
		
		System.out.println("[" + currentThreadName + "] Current time : " + dateFormatter.format(new Date()));
		
		// Programa la tarea 1 (ScheduledTaskB) para ejecutarse repetidamente.
		// Parámetros:
		// 1. La tarea a ejecutar (ScheduledTaskB con duración de 1000ms).
		// 2. Retraso inicial (4 segundos) antes de la primera ejecución.
		// 3. Retraso fijo (2 segundos) entre la TERMINACIÓN de una ejecución y el INICIO de la siguiente.
		// 4. Unidad de tiempo (TimeUnit.SECONDS).
		ScheduledFuture<?> schedFuture1 = execService.scheduleWithFixedDelay(new ScheduledTaskB(1000), 4, 2, TimeUnit.SECONDS);
		
		// Programa la tarea 2 de manera similar.
		ScheduledFuture<?> schedFuture2 = execService.scheduleWithFixedDelay(new ScheduledTaskB(3000), 4, 2, TimeUnit.SECONDS);
		
		// Cancela la tarea 2 inmediatamente. El parámetro 'true' indica que si la tarea se está ejecutando, debe ser interrumpida.
		schedFuture2.cancel(true);
		
//		for (int i = 0; i < 5; i++) {
//			System.out.print("[" + currentThreadName + "] Next run of TASK-1 scheduled at approx.: ");
//			Date scheduledTime = TimeUtils.getFutureTime(new Date(), schedFuture1.getDelay(TimeUnit.MILLISECONDS));
//			System.out.println(dateFormatter.format(scheduledTime));
//			
//			TimeUnit.MILLISECONDS.sleep(3000);
//		}
		
		// El hilo principal duerme durante 15 segundos para permitir que las tareas programadas se ejecuten varias veces.
		TimeUnit.MILLISECONDS.sleep(15000);
		
		// Apaga el executor service, liberando los recursos y deteniendo las tareas programadas.
		execService.shutdown();
		
		System.out.println("[" + currentThreadName + "] Main thread ends here...");
	}	
}
