package tut5.executors.aliveCheck;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import tuts.common.CalculationTaskA;
import tuts.common.LoopTaskC;
import tuts.common.NamedThreadsFactory;

/**
 * <p>
 * <b>TUTORIAL 5: COMPROBAR SI UNA TAREA ESTÁ TERMINADA - TÉCNICA CON FUTURE.ISDONE()</b>
 * </p>
 *
 * Esta clase demuestra cómo monitorizar el estado de una TAREA (no de un hilo)
 * usando el método `isDone()` del objeto `Future`.
 * <br><br>
 * <b>`isAlive()` (Hilo) vs. `isDone()` (Tarea):</b>
 * <ul>
 *     <li>`thread.isAlive()`: Nos dice si el <b>hilo</b> (el "obrero") está en marcha.</li>
 *     <li>`future.isDone()`: Nos dice si la <b>tarea</b> (el "trabajo") ha finalizado.</li>
 * </ul>
 * En un pool, un hilo puede terminar una tarea (`isDone=true`) y seguir vivo (`isAlive=true`) esperando nuevo trabajo.
 */
public class ThreadsAliveCheckUsingExecutors {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		ExecutorService execService = Executors.newCachedThreadPool(new NamedThreadsFactory());
		
		//********** 1. SUMISIÓN DE TAREAS **********//
		
		// `submit()` devuelve un `Future` que podemos usar para monitorizar la tarea.
		Future<?> f1 = execService.submit(new LoopTaskC());
		Future<Integer> f2 = execService.submit(new CalculationTaskA(3, 4, 700));
		
		// `FutureTask`: un híbrido que es a la vez un `Runnable` y un `Future`.
		// Permite usar `execute()` (que no devuelve Future) y aun así tener un objeto Future para monitorizar.
		FutureTask<Integer> ft3 = new FutureTask<>(new CalculationTaskA(4, 5, 500));
		execService.execute(ft3);

		execService.shutdown();
		
		//********** 2. MONITORIZACIÓN (POLLING - POCO RECOMENDADO) **********//
		
		// Al igual que con `isAlive()`, hacer "polling" con `isDone()` en un bucle no es la forma
		// ideal de esperar. Es útil si el hilo principal necesita hacer otro trabajo mientras
		// comprueba esporádicamente, pero para una espera simple, `future.get()` es mejor.
		for (int i = 1; i <= 5; i++) {
			TimeUnit.MILLISECONDS.sleep(300);
			
			// `isDone()` no es bloqueante. Devuelve `true` si la tarea terminó (normal, con error o cancelada).
			System.out.printf("[%s] ITR-%d -> 'LoopTaskC' terminada? %b%n", currentThreadName, i, f1.isDone());
			System.out.printf("[%s] ITR-%d -> 'CalcTaskA-1' terminada? %b%n", currentThreadName, i, f2.isDone());
			System.out.printf("[%s] ITR-%d -> 'CalcTaskA-2' terminada? %b%n", currentThreadName, i, ft3.isDone());
		}
		
		//********** 3. RECUPERACIÓN DE RESULTADOS (LA FORMA CORRECTA DE ESPERAR) **********//
		
		System.out.println("\n$$$$$ [" + currentThreadName + "] Recuperando resultados... $$$$$");
		
		// La forma correcta de esperar y obtener un resultado es `get()`.
		// Si la tarea no está "done", `get()` bloqueará el hilo actual hasta que lo esté.
		// Si ya está "done", devuelve el resultado inmediatamente.
		System.out.println("[" + currentThreadName + "] 'LoopTaskC' resultado = " + f1.get()); // get() de un Runnable devuelve null
		System.out.println("[" + currentThreadName + "] 'CalcTaskA-1' resultado = " + f2.get());
		System.out.println("[" + currentThreadName + "] 'CalcTaskA-2' (FutureTask) resultado = " + ft3.get());
		
		System.out.println("\n==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
