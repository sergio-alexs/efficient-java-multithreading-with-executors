package tut6.executors.terminating;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import tuts.common.CalculationTaskC;
import tuts.common.LoopTaskF;
import tuts.common.NamedThreadsFactory;

/**
 * <p>
 * <b>TUTORIAL 6: TERMINAR TAREAS DE EXECUTOR - TÉCNICA 2 (USANDO FUTURE.CANCEL())</b>
 * </p>
 *
 * Esta clase demuestra la forma idiomática y correcta de cancelar tareas individuales
 * en un `ExecutorService` usando el objeto `Future` que se nos devuelve al someterlas.
 */
public class TerminatingExecutorTasksSecondTechnique {

	public static void main(String[] args) throws InterruptedException {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		ExecutorService execService = Executors.newFixedThreadPool(2, new NamedThreadsFactory());
		
		// Someter las tareas nos devuelve un "mando a distancia" (Future) para cada una.
		Future<Long> f1 = execService.submit(new CalculationTaskC());
		Future<?> f2 = execService.submit(new LoopTaskF());
		Future<?> f3 = execService.submit(new LoopTaskF());
		
		execService.shutdown();
		
		TimeUnit.MILLISECONDS.sleep(2000);
		
		// --- El método `future.cancel(boolean mayInterruptIfRunning)` ---
		// Este es el "botón de cancelar" en nuestro mando a distancia. El parámetro
		// booleano es muy importante y controla la agresividad de la cancelación.
		
		System.out.println(">>> [" + currentThreadName + "] Cancelando CalculationTaskC-1...");
		// `cancel(true)`:
		// 1. Si la tarea todavía no ha empezado (está en la cola), se elimina y no se ejecuta.
		// 2. Si la tarea YA ESTÁ EN EJECUCIÓN, el executor llamará a `thread.interrupt()` sobre
		//    el hilo que la está ejecutando. Esto activa el mecanismo de interrupción cooperativa
		//    que vimos antes. La tarea debe estar preparada para manejar la interrupción.
		// `cancel()` devuelve `true` si la cancelación fue "exitosa" (en el sentido de que la
		// tarea no se ejecutará o ha sido señalada para interrumpirse).
		System.out.println("[" + currentThreadName + "] Resultado de cancelación de f1 = " + f1.cancel(true));
		
		System.out.println(">>> [" + currentThreadName + "] Cancelando LoopTaskF-1...");
		System.out.println("[" + currentThreadName + "] Resultado de cancelación de f2 = " + f2.cancel(true));
		
		System.out.println(">>> [" + currentThreadName + "] Cancelando LoopTaskF-2...");
		// `cancel(false)`:
		// 1. Si la tarea no ha empezado, la elimina de la cola (igual que antes).
		// 2. Si la tarea YA ESTÁ EN EJECUCIÓN, `cancel(false)` NO LA INTERRUMPE. La deja terminar
		//    su trabajo tranquilamente. Sin embargo, el Future quedará en estado "cancelado".
		// Esto es útil si quieres evitar que empiece pero no quieres interrumpirla si ya lo ha hecho.
		// En este caso, como usamos un FixedThreadPool de 2 hilos, es probable que esta tercera
		// tarea aún esté en la cola, por lo que `cancel(true)` o `cancel(false)` tendrán el mismo efecto.
		System.out.println("[" + currentThreadName + "] Resultado de cancelación de f3 = " + f3.cancel(true));
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
