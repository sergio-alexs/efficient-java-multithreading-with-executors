package tut6.executors.terminating;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import tuts.common.FactorialTaskB;
import tuts.common.LoopTaskG;
import tuts.common.NamedThreadsFactory;

/**
 * <p>
 * <b>TUTORIAL 6: TERMINAR TAREAS DE EXECUTOR - TÉCNICA 3 (CANCELANDO TAREAS BLOQUEADAS)</b>
 * </p>
 *
 * Esta clase demuestra cómo `future.cancel(true)` es efectivo para terminar tareas
 * que están bloqueadas en métodos como `Thread.sleep()`.
 * <br><br>
 * <b>Mecanismo:</b> `future.cancel(true)` llama a `thread.interrupt()` sobre el hilo
 * de la tarea. Si el hilo está en `sleep()`, este se despierta inmediatamente y
 * lanza una `InterruptedException`, permitiendo que el código en el `catch`
 * maneje la terminación.
 */
public class TerminatingBlockedExecutorTasks {

	public static void main(String[] args) throws InterruptedException {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		ExecutorService execService = Executors.newCachedThreadPool(new NamedThreadsFactory());
		
		// Tarea que maneja InterruptedException saliendo de su bucle.
		Future<?> f1 = execService.submit(new LoopTaskG());
		// Tarea que también maneja InterruptedException.
		Future<Long> f2 = execService.submit(new FactorialTaskB(30, 500));
		
		execService.shutdown();
		
		TimeUnit.MILLISECONDS.sleep(2000);
		
		System.out.println(">>> [" + currentThreadName + "] Invocando cancel(true) en las tareas...");
		
		// Ambas tareas (LoopTaskG y FactorialTaskB) están bien escritas. Tienen un
		// bloque try-catch alrededor de su sleep(). Al recibir la InterruptedException,
		// limpian lo que sea necesario y salen de su bucle, terminando limpiamente.
		f1.cancel(true);
		f2.cancel(true);
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
