package tut8.executors.joining;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tuts.common.LoopTaskI;
import tuts.common.NamedThreadsFactory;

/**
 * <p>
 * <b>TUTORIAL 8: "UNIR" TAREAS DE EXECUTOR - TÉCNICA CON COUNTDOWNLATCH</b>
 * </p>
 *
 * Como no tenemos acceso directo a los hilos de un `ExecutorService`, no podemos
 * usar `thread.join()`. Esta clase demuestra una forma de esperar a que un grupo
 * de tareas termine usando una potente herramienta de sincronización: `CountDownLatch`.
 * <br><br>
 * <b>Analogía del `CountDownLatch`:</b>
 * Imagina un "Controlador de Eventos".
 * <ul>
 *     <li><b>`new CountDownLatch(4)`:</b> El Controlador sabe que hay 4 ponentes.</li>
 *     <li><b>`latch.await()`:</b> El Anfitrión (hilo `main`) le dice al Controlador:
 *         "Avísame cuando los 4 ponentes hayan terminado". Y se pone a esperar.</li>
 *     <li><b>`latch.countDown()`:</b> Cada vez que un Ponente (una tarea `LoopTaskI`)
 *         termina su charla, le devuelve el micrófono al Controlador, que descuenta uno.</li>
 * </ul>
 * Cuando el 4º ponente devuelve su micrófono, el contador llega a 0 y el Controlador
 * le da la señal al Anfitrión para que continúe.
 */
public class JoiningExecutorTasks {

	public static void main(String[] args) {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		ExecutorService execService = Executors.newCachedThreadPool(new NamedThreadsFactory());
		
		// 1. Se crea el `latch` con un contador inicial de 4.
		CountDownLatch doneSignal = new CountDownLatch(4);
		
		// 2. Se lanzan 4 tareas. A CADA tarea se le pasa la MISMA instancia del `latch`.
		execService.execute(new LoopTaskI(doneSignal));
		execService.execute(new LoopTaskI(doneSignal));
		execService.execute(new LoopTaskI(doneSignal));
		execService.execute(new LoopTaskI(doneSignal));
		
		execService.shutdown();
		
		try {
			// 3. El hilo `main` se bloquea aquí hasta que el contador del `latch` llegue a cero.
			System.out.println(">>> [" + currentThreadName + "] Esperando a que las 4 tareas terminen...");
			doneSignal.await();
			System.out.println(">>> [" + currentThreadName + "] Todas las tareas han terminado. Continuando.");
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// NOTA: Para el caso específico de esperar a que TODAS las tareas de un Executor
		// terminen, el patrón `executor.shutdown(); executor.awaitTermination(timeout, unit);`
		// es a menudo más simple porque no requiere que modifiques tus tareas para
		// que conozcan el `latch`. `CountDownLatch` es una herramienta más genérica
		// y flexible, útil para sincronizaciones más complejas.
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
