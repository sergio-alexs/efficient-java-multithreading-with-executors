package tut3.executors.returningValues;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import tuts.common.CalculationTaskA;
import tuts.common.LoopTaskA;
import tuts.common.NamedThreadsFactory;

/**
 * <p>
 * <b>TUTORIAL 3: DEVOLVER VALORES CON EXECUTORS - TÉCNICA 1 (USANDO FUTURE.GET())</b>
 * </p>
 *
 * Esta clase introduce `Callable` y `Future`, la forma estándar del Executor Framework
 * para manejar tareas que devuelven resultados.
 * <br><br>
 * <b>Conceptos Clave:</b>
 * <ul>
 *     <li><b>`Callable<V>`:</b> Es el "hermano" de `Runnable`. Su método `call()` puede
 *         devolver un valor de tipo `V` y lanzar excepciones. Es una tarea que calcula algo.</li>
 *     <li><b>`Future<V>`:</b> Es una "promesa" o un "recibo" de un resultado que se
 *         está calculando. El `ExecutorService` te da un `Future` inmediatamente
 *         cuando le envías un `Callable`.</li>
 * </ul>
 */
public class ReturningValuesUsingExecutors_FirstTechnique {

	public static void main(String[] args) {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		ExecutorService execService = Executors.newCachedThreadPool(new NamedThreadsFactory());
		
		// `submit()` es el método para enviar tareas que devuelven valor.
		// Acepta un `Callable` y devuelve un `Future` instantáneamente.
		Future<Integer> future1 = execService.submit(new CalculationTaskA(2, 3, 2000));
		Future<Integer> future2 = execService.submit(new CalculationTaskA(3, 4, 1000));
		Future<Integer> future3 = execService.submit(new CalculationTaskA(4, 5, 500));
		
		// `submit()` también puede recibir un `Runnable`. En ese caso, `future.get()` devolverá
		// `null` cuando termine, pero sirve para saber que ha finalizado.
		Future<?> future4 = execService.submit(new LoopTaskA());
		
		execService.shutdown();
		
		// --- EL PROBLEMA: RECUPERACIÓN SECUENCIAL Y BLOQUEANTE (OTRA VEZ) ---
		// Aunque la sumisión de tareas fue asíncrona, este código recupera los
		// resultados de forma secuencial, cayendo en el mismo problema que la Técnica 1 manual.
		try {
			// `future.get()` es el método para "canjear el recibo". Es bloqueante.
			// Si el resultado no está listo, el hilo principal se queda aquí esperando.
			
			// 1. Pedimos el resultado de future1. El hilo principal se bloquea ~2000ms.
			//    Aunque `future3` y `future2` terminen mucho antes, no podemos ir a por ellos.
			System.out.println("Pidiendo resultado de Tarea 1...");
			System.out.println("Resultado-1 = " + future1.get());
			System.out.println("...Resultado de Tarea 1 OBTENIDO!");

			// 2. Cuando `future1.get()` termina, pedimos el de `future2`, que ya estará listo.
			System.out.println("Pidiendo resultado de Tarea 2...");
			System.out.println("Resultado-2 = " + future2.get());
			System.out.println("...Resultado de Tarea 2 OBTENIDO!");

			// 3. Y finalmente `future3`.
			System.out.println("Pidiendo resultado de Tarea 3...");
			System.out.println("Resultado-3 = " + future3.get());
			System.out.println("...Resultado de Tarea 3 OBTENIDO!");
			
			System.out.println("Pidiendo resultado de Tarea 4 (Runnable)...");
			System.out.println("Resultado-4 = " + future4.get()); // get() devolverá null
			System.out.println("...Resultado de Tarea 4 OBTENIDO!");
			
		} catch (InterruptedException e) {
			// Se lanza si este hilo es interrumpido mientras está bloqueado en `get()`.
			e.printStackTrace();
		} catch (ExecutionException e) {
			// ¡MUY IMPORTANTE! Si el método `call()` de la tarea lanza una excepción,
			// esa excepción es "capturada" por el Executor y envuelta en una `ExecutionException`.
			// `future.get()` la vuelve a lanzar aquí para que podamos manejarla.
			System.err.println("La tarea lanzó una excepción: " + e.getCause());
		}
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
