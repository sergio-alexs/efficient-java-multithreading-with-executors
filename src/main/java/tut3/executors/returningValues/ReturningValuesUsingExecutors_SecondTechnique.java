package tut3.executors.returningValues;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import tuts.common.CalculationTaskB;
import tuts.common.LoopTaskA;
import tuts.common.NamedThreadsFactory;
import tuts.common.TaskResult;

/**
 * <p>
 * <b>TUTORIAL 3: DEVOLVER VALORES - TÉCNICA 2 (LA FORMA EFICIENTE CON COMPLETIONSERVICE)</b>
 * </p>
 *
 * Esta clase demuestra la forma más eficiente de procesar resultados de múltiples tareas:
 * el `ExecutorCompletionService`.
 * <br><br>
 * <b>Concepto Clave:</b> Un `CompletionService` desacopla la sumisión de tareas de la
 * recuperación de resultados. Actúa como una capa intermedia que nos entrega los
 * resultados en el orden en que se completan, no en el que se lanzaron.
 * <br><br>
 * <b>Analogía:</b> Imagina un "buzón de recogida".
 * <ul>
 *   <li><b>ExecutorService:</b> Es el "servicio de mensajería" con sus repartidores (hilos).</li>
 *   <li><b>CompletionService:</b> Es tu "buzón inteligente".</li>
 * </ul>
 * Tú (hilo principal) envías muchas peticiones de paquetes (`Callable`s) al servicio de mensajería.
 * En lugar de esperar en la puerta a cada repartidor en un orden fijo, te vas. Cada vez que
 * un repartidor entrega un paquete, lo deja en tu buzón. Tú simplemente vas al buzón
 * (`completionService.take()`) y recoges el primer paquete que haya llegado.
 */
public class ReturningValuesUsingExecutors_SecondTechnique {

	public static void main(String[] args) {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		ExecutorService execService = Executors.newCachedThreadPool(new NamedThreadsFactory());
		
		// "Envolvemos" nuestro ExecutorService con un CompletionService.
		// A partir de ahora, nos comunicaremos con el CompletionService.
		CompletionService<TaskResult<String, Integer>> completionService = 
				new ExecutorCompletionService<>(execService);
		
		// --- BUCLE DE SUMISIÓN ---
		// Enviamos todas las tareas. La más rápida (500ms) es la última en ser enviada.
		completionService.submit(new CalculationTaskB(2, 3, 2000)); // La más lenta
		completionService.submit(new CalculationTaskB(3, 4, 1000)); // Intermedia
		completionService.submit(new CalculationTaskB(4, 5, 500));  // La más rápida
		
		// Apagamos el ExecutorService subyacente. El CompletionService no se apaga,
		// sino el servicio de "repartidores" que usa por debajo.
		execService.shutdown();
		
		// --- BUCLE DE RECUPERACIÓN ---
		// Este bucle se ejecuta para recoger los resultados de las 3 tareas.
		for (int i = 0; i < 3; i++) {
			try {
				// `completionService.take()`:
				// - Es BLOQUEANTE: espera hasta que AL MENOS UNA tarea haya terminado.
				// - Devuelve el `Future` de la primera tarea que se completó.
				//
				// En la 1ª iteración, aunque `CalculationTaskB(2,3)` se lanzó primero,
				// `take()` devolverá el `Future` de `CalculationTaskB(4,5)` porque es la que
				// termina antes.
				Future<TaskResult<String, Integer>> resultFuture = completionService.take();
				
				// `resultFuture.get()`:
				// - Como `take()` nos asegura que la tarea ya está completa, esta llamada
				//   a `get()` es (casi siempre) instantánea. No hay bloqueo aquí.
				TaskResult<String, Integer> result = resultFuture.get();
				
				System.out.printf("Resultado para '%s' = %d%n", result.taskId, result.result);
				
			} catch (InterruptedException | ExecutionException e) {
				System.err.println("Error procesando el resultado: " + e.getCause());
			}
		}
		// Nota: Existe también `completionService.poll()`, que es no-bloqueante y
		// devuelve `null` si ningún resultado está listo todavía.
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
