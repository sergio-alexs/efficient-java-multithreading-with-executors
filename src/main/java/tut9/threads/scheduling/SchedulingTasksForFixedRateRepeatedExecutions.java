package tut9.threads.scheduling;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import tuts.common.ScheduledTaskA;
import tuts.utils.TimeUtils;

/**
 * <p>
 * <b>TUTORIAL 9: PLANIFICACIÓN - TÉCNICA 2 (TIMER CON TASA FIJA)</b>
 * </p>
 *
 * Esta clase demuestra cómo planificar tareas repetitivas con `scheduleAtFixedRate`.
 * <br><br>
 * <b>*** ADVERTENCIA: `java.util.Timer` sigue siendo obsoleto y no recomendado. ***</b>
 * <br>
 * <b>`scheduleAtFixedRate` (Tasa Fija):</b>
 * Se enfoca en la puntualidad. Intenta ejecutar la tarea a intervalos fijos
 * respecto a la hora de inicio original.
 * <br><br>
 * <b>Analogía:</b> Un autobús que DEBE salir de la parada a las 10:00, 10:10, y 10:20.
 * Si el viaje de las 10:00 dura 12 min por el tráfico (termina a las 10:12), la salida
 * de las 10:10 ya se ha perdido. Para "ponerse al día", el `Timer` lanzará la
 * siguiente ejecución inmediatamente. Esto puede causar ráfagas de ejecuciones si
 * la duración de la tarea es mayor que el periodo.
 */
public class SchedulingTasksForFixedRateRepeatedExecutions {
	
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss.SSS");

	public static void main(String[] args) throws InterruptedException {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		Timer timer = new Timer("Timer-Thread", true);
		Date currentTime = new Date();
		
		System.out.println("[" + currentThreadName + "] Hora actual: " + dateFormatter.format(currentTime));

		// --- Tarea 1: Planificada con tasa fija ---
		Date scheduledTime = TimeUtils.getFutureTime(currentTime,3000);
		long intervalMillis = 2000;
		
		// `scheduleAtFixedRate(task, startTime, period)`
		// Inicia en `startTime`, y luego intenta iniciar de nuevo en
		// `startTime + period`, `startTime + 2*period`, etc., sin importar
		// cuánto dure la ejecución anterior.
		timer.scheduleAtFixedRate(new ScheduledTaskA(1000), scheduledTime, intervalMillis);
		
		System.out.println(">>> Tarea 1 planificada para " + dateFormatter.format(scheduledTime) +
				" y repetir cada " + intervalMillis/1000 + " segundos.");
		
		// El hilo principal debe permanecer vivo.
		TimeUnit.MILLISECONDS.sleep(16000);
		
		timer.cancel();
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}	
}
