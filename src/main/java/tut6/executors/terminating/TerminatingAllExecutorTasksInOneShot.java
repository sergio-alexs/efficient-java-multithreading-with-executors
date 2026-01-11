package tut6.executors.terminating;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import tuts.common.CalculationTaskB;
import tuts.common.CalculationTaskC;
import tuts.common.FactorialTaskB;
import tuts.common.LoopTaskA;
import tuts.common.LoopTaskF;
import tuts.common.NamedThreadsFactory;
import tuts.common.TaskResult;

/**
 * <p>
 * <b>TUTORIAL 6: TERMINAR TAREAS DE EXECUTOR - TÉCNICA 4 (APAGADO BRUSCO CON SHUTDOWNNOW())</b>
 * </p>
 *
 * Esta clase demuestra el uso de `executor.shutdownNow()` para un apagado inmediato y forzado.
 * <br><br>
 * <b>`shutdown()` vs. `shutdownNow()`</b>
 * <ul>
 *     <li><b>`shutdown()` (Apagado Ordenado):</b> Deja de aceptar nuevas tareas, pero
 *         permite que las tareas en ejecución y las que están en la cola terminen
 *         normalmente. Es como decir: "Terminad lo que estáis haciendo y no empecéis nada nuevo".</li>
 *     <li><b>`shutdownNow()` (Apagado Brusco):</b> Intenta detener todo inmediatamente. Es
 *         como tirar de la palanca de emergencia.</li>
 * </ul>
 */
public class TerminatingAllExecutorTasksInOneShot {

	public static void main(String[] args) throws Exception {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		ExecutorService execService = Executors.newCachedThreadPool(new NamedThreadsFactory());
		
		CalculationTaskC task1 = new CalculationTaskC();
		LoopTaskF task2 = new LoopTaskF();
		FactorialTaskB task3 = new FactorialTaskB(30, 500);
		
		Future<Long> f1 = execService.submit(task1);
		Future<?> f2 = execService.submit(task2);
		Future<Long> f3 = execService.submit(task3);
		
		TimeUnit.MILLISECONDS.sleep(1000);
		
		System.out.println(">>> [" + currentThreadName + "] Invocando shutdownNow()...");
		
		// --- ¿QUÉ HACE `shutdownNow()`? ---
		// 1. **INTERRUMPE TAREAS ACTIVAS:** Llama a `thread.interrupt()` sobre todos los hilos
		//    del pool que estén ejecutando una tarea. Depende de las tareas manejar la interrupción.
		// 2. **DRENA LA COLA:** Descarta todas las tareas que estaban en la cola esperando
		//    ser ejecutadas. Estas tareas nunca llegarán a empezar.
		// 3. **DEVUELVE TAREAS PENDIENTES:** Retorna una `List<Runnable>` con las tareas que
		//    fueron descartadas de la cola.
		List<Runnable> tasksThatNeverRan = execService.shutdownNow();
		System.out.println(">>> [" + currentThreadName + "] Tareas que nunca empezaron: " + tasksThatNeverRan.size());
		
		// Esperamos un poco para que las interrupciones se procesen.
		execService.awaitTermination(7000, TimeUnit.MILLISECONDS);
		
		// --- ¿QUÉ PASA AL LLAMAR A GET() DESPUÉS DE SHUTDOWNNOW()? ---
		// Generalmente, si una tarea fue cancelada (ya sea por interrupción o por ser
		// eliminada de la cola), `future.get()` lanzará una `CancellationException`.
		try {
			System.out.println(">>> [" + currentThreadName + "] 'CalculationTaskC' resultado = " + f1.get());
			System.out.println(">>> [" + currentThreadName + "] 'LoopTaskF' resultado = " + f2.get());
			System.out.println(">>> [" + currentThreadName + "] 'FactorialTaskB' resultado = " + f3.get());
		} catch (CancellationException ce) {
			System.err.println("!!! Tarea cancelada. " + ce);
		}
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
