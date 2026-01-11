package tut9.executors.scheduling;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import tuts.common.CalculationTaskD;
import tuts.common.NamedThreadsFactory;
import tuts.common.ScheduledTaskB;

/**
 * Esta clase demuestra cómo programar tareas para una ejecución única (One-Time Execution)
 * en el futuro utilizando ScheduledExecutorService.
 */
public class SchedulingTasksForOneTimeExecutionUsingExecutors {
	
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss.SSS");

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("[" + currentThreadName + "] Main thread starts here...");
		
		// Crea un pool de hilos programados con 3 hilos.
		ScheduledExecutorService execService = Executors.newScheduledThreadPool(3, new NamedThreadsFactory());
		
		System.out.println("[" + currentThreadName + "] Current time : " + dateFormatter.format(new Date()));
		
		// Programa una tarea Runnable (ScheduledTaskB) para ejecutarse una sola vez después de 4 segundos.
		// ScheduledFuture<?> se usa porque Runnable no devuelve un resultado.
		ScheduledFuture<?> schedFuture1 = execService.schedule(new ScheduledTaskB(3000), 4, TimeUnit.SECONDS);
		
		// Programa una tarea Callable (CalculationTaskD) para ejecutarse una sola vez después de 6 segundos.
		// ScheduledFuture<Integer> permitirá obtener el resultado (Integer) cuando esté listo.
		ScheduledFuture<Integer> schedFuture2 = execService.schedule(new CalculationTaskD(2, 3, 3000), 6, TimeUnit.SECONDS);
		
		// Programa otra tarea Runnable para ejecutarse después de 8 segundos.
		// No guardamos el Future porque no planeamos cancelarla ni esperar su resultado explícitamente aquí.
		execService.schedule(new ScheduledTaskB(0), 8, TimeUnit.SECONDS);
		
		// Programa otra tarea Callable para ejecutarse después de 10 segundos.
		ScheduledFuture<Integer> schedFuture4 = execService.schedule(new CalculationTaskD(3, 4, 0), 10, TimeUnit.SECONDS);
		
		// Inicia el proceso de apagado del executor.
		// IMPORTANTE: shutdown() no detiene las tareas que ya han sido programadas o están en ejecución,
		// simplemente deja de aceptar NUEVAS tareas. Las tareas programadas (como las de arriba) se ejecutarán
		// a menos que se cancelen o el programa termine abruptamente.
		execService.shutdown();
		
		// Cancela la tarea 1 y la tarea 2 antes de que tengan oportunidad de ejecutarse (o completarse).
		// 'true' indica que si ya están corriendo, deben ser interrumpidas.
		schedFuture1.cancel(true);
		schedFuture2.cancel(true);
		
		System.out.println("[" + currentThreadName + "] RETRIEVING THE RESULTS NOW ...\n");
		
		// Intentar obtener el resultado de una tarea cancelada lanzaría una CancellationException.
		// Por eso estas líneas están comentadas.
//		System.out.println("[" + currentThreadName + "] TASK-1 RESULT = " + schedFuture1.get() + "\n");
//		System.out.println("[" + currentThreadName + "] TASK-2 RESULT = " + schedFuture2.get() + "\n");
		
		// Obtiene el resultado de la tarea 4.
		// .get() bloqueará el hilo principal hasta que la tarea 4 se complete (aprox. a los 10 segundos).
		System.out.println("[" + currentThreadName + "] TASK-4 RESULT = " + schedFuture4.get() + "\n");
		
		System.out.println("[" + currentThreadName + "] Main thread ends here...");
	}	
}
