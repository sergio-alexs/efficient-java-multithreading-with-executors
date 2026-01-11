package tut9.threads.scheduling;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import tuts.common.ScheduledTaskA;
import tuts.utils.TimeUtils;

/**
 * <p>
 * <b>TUTORIAL 9: PLANIFICACIÓN - TÉCNICA 1 (USANDO EL ANTIGUO `java.util.Timer`)</b>
 * </p>
 *
 * Esta clase demuestra cómo planificar tareas usando la clase `java.util.Timer`.
 * <br><br>
 * <b>*** ADVERTENCIA: `java.util.Timer` es una clase antigua y NO se recomienda
 * su uso en código moderno. El `ScheduledExecutorService` es la alternativa
 * superior en todos los aspectos. ***</b>
 * <br>
 * <b>Debilidades de `Timer`:</b>
 * <ul>
 *     <li><b>Usa un solo hilo:</b> Todas las tareas programadas en un `Timer` se ejecutan
 *         secuencialmente en un único hilo. Si una tarea tarda mucho, retrasa a todas las demás.</li>
 *     <li><b>Es frágil:</b> Si una tarea lanza una excepción no capturada, el hilo del `Timer`
 *         muere, y el `Timer` deja de funcionar por completo. Ninguna otra tarea
 *         planificada se ejecutará.</li>
 *     <li><b>Depende del reloj del sistema:</b> Es sensible a cambios en la hora del SO.</li>
 * </ul>
 */
public class SchedulingTasksForOneTimeExecution {
	
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss.SSS");

	public static void main(String[] args) throws InterruptedException {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		// Un Timer gestiona la planificación. `true` en el constructor lo hace un hilo demonio.
		Timer timer = new Timer("Timer-Thread", true);
		Date currentTime = new Date();
		
		System.out.println("[" + currentThreadName + "] Hora actual: " + dateFormatter.format(currentTime));
		
		// --- Tarea 1: Ejecutar en una fecha específica ---
		Date scheduledTime = TimeUtils.getFutureTime(currentTime, 5000);
		timer.schedule(new ScheduledTaskA(8000), scheduledTime);
		System.out.println(">>> Tarea 1 planificada para: " + dateFormatter.format(scheduledTime));

		// --- Tarea 2: Ejecutar después de un retardo ---
		long delayMillis = 10000;
		ScheduledTaskA task2 = new ScheduledTaskA(4000);
		timer.schedule(task2, delayMillis);
		System.out.println(">>> Tarea 2 planificada para ejecutarse tras 10 segundos.");

		// --- Tarea 3: Cancelación de una tarea ---
		Date scheduledTime2 = TimeUtils.getFutureTime(currentTime,  30000);
		ScheduledTaskA task3 = new ScheduledTaskA(0);
		timer.schedule(task3, scheduledTime2);
		System.out.println(">>> Tarea 3 planificada para dentro de 30 segundos.");
		
		// `task.cancel()` solo previene la ejecución si la tarea aún no ha empezado.
		task3.cancel();
		
		// El hilo principal debe permanecer vivo para que el hilo del Timer (demonio) pueda trabajar.
		TimeUnit.MILLISECONDS.sleep(32000);
		
		// `timer.cancel()` detiene el hilo del Timer y cancela todas las tareas pendientes.
		System.out.println(">>> Cancelando el Timer...");
		timer.cancel();
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}	
}
