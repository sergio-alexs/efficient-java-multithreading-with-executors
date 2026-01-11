package tut9.threads.scheduling;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import tuts.common.ScheduledTaskA;
import tuts.utils.TimeUtils;

/**
 * Esta clase demuestra cómo programar tareas repetitivas utilizando la clase antigua `java.util.Timer`.
 *
 * NOTA IMPORTANTE: `java.util.Timer` es una API antigua y tiene limitaciones (por ejemplo, si una tarea lanza
 * una excepción no controlada, el hilo del Timer muere y cancela todas las demás tareas).
 * En desarrollo moderno, se prefiere usar `ScheduledExecutorService` (como en los ejemplos anteriores).
 *
 * Sin embargo, es útil entender cómo funciona `Timer` para mantener código legado.
 *
 * A diferencia de `ScheduledExecutorService`, `Timer` NO tiene una forma directa de hacer "Fixed Delay" real
 * (esperar a que termine una para contar el tiempo). `Timer.schedule` se comporta más parecido a "Fixed Rate"
 * o "Fixed Delay" dependiendo de la implementación, pero técnicamente `schedule` con periodo es para ejecuciones repetidas
 * con un intervalo fijo entre *inicios* (si la tarea es rápida) o secuencial (si es lenta).
 */
public class SchedulingTasksForFixedDelayRepeatedExecutions {
	
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss.SSS");

	public static void main(String[] args) throws InterruptedException {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("[" + currentThreadName + "] Main thread starts here...");
		
		// Crea un objeto Timer.
		// El segundo parámetro 'true' indica que el hilo del Timer debe ser un hilo "Daemon".
		// Un hilo Daemon no impide que la JVM se cierre si el hilo principal termina.
		Timer timer = new Timer("Timer-Thread", true);
		Date currentTime = new Date();
		
		System.out.println("[" + currentThreadName + "] Current time : " + dateFormatter.format(currentTime));

		// Configuración para la Tarea 1:
		// Se ejecutará por primera vez en 'scheduledTime' (3 segundos en el futuro).
		Date scheduledTime = TimeUtils.getFutureTime(currentTime, 3000);
		// Se repetirá cada 2000ms (2 segundos).
		long intervalMillis = 2000;
		
		// Programa la Tarea 1 usando una FECHA específica de inicio.
		// timer.schedule(TimerTask task, Date firstTime, long period)
		timer.schedule(new ScheduledTaskA(1000), scheduledTime, intervalMillis);
		
		System.out.println("[" + currentThreadName + "] Task-1 first-run scheduled for " + dateFormatter.format(scheduledTime) +
				" and then repeatedly at an interval of every " + TimeUtils.convertToFractionalSecondsStr(0, intervalMillis));
		
		//**********************************************************************************************************//
		
		// Configuración para la Tarea 2:
		// Se ejecutará por primera vez después de un RETRASO de 4000ms (4 segundos).
		long delayMillis = 4000;
		// Se repetirá cada 2000ms (2 segundos).
		long intervalMillis2 = 2000;
		
		// Programa la Tarea 2 usando un RETRASO (delay) en milisegundos.
		// timer.schedule(TimerTask task, long delay, long period)
		timer.schedule(new ScheduledTaskA(1000), delayMillis, intervalMillis2);
		
		System.out.println("[" + currentThreadName + "] Task-2 first-run scheduled " +
				TimeUtils.convertToFractionalSecondsStr(0, delayMillis) + " after " + dateFormatter.format(currentTime) +
				" and then repeatedly at an interval of every " + TimeUtils.convertToFractionalSecondsStr(0, intervalMillis2));
		
		// El hilo principal duerme 16 segundos para dejar que el Timer trabaje.
		TimeUnit.MILLISECONDS.sleep(16000);
		
		System.out.println("[" + currentThreadName + "] CANCELLING THE TIMER NOW ...");
		// Cancela el Timer. Esto descarta todas las tareas programadas actualmente y libera el hilo del Timer.
		timer.cancel();
		
		System.out.println("[" + currentThreadName + "] Main thread ends here...");
	}	
}
