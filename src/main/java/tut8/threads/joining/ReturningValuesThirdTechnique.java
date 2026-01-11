package tut8.threads.joining;

import tuts.common.ValueReturningTaskC;

/**
 * <p>
 * <b>TUTORIAL 8: DEVOLVER VALORES - TÉCNICA 3 (USANDO JOIN())</b>
 * </p>
 *
 * Esta clase demuestra cómo se puede usar `join()` para obtener de forma segura
 * un resultado de una tarea que, por sí misma, no es segura para hilos (`thread-safe`).
 * <br><br>
 * <b>El Problema:</b> `ValueReturningTaskC` es una tarea "rota". Su método `getSum()`
 * simplemente devuelve una variable, sin saber si el hilo de la tarea ya la ha
 * calculado. Esto crea una condición de carrera.
 * <br><br>
 * <b>La Solución con `join()`:</b> El método `join()` establece una garantía de
 * "sucede-antes-de" (`happens-before`). Cuando `t1.join()` retorna, la JVM asegura que
 * todos los cambios hechos por el hilo `t1` son visibles para el hilo actual.
 * Esto "arregla" la tarea rota, pero de una forma ineficiente.
 */
public class ReturningValuesThirdTechnique {

	public static void main(String[] args) throws InterruptedException {
		String currentThreadName = Thread.currentThread().getName();
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		ValueReturningTaskC task1 = new ValueReturningTaskC(2, 3, 500);
		Thread t1 = new Thread(task1, "Thread-1");
		
		ValueReturningTaskC task2 = new ValueReturningTaskC(3, 4, 1000);
		Thread t2 = new Thread(task2, "Thread-2");

		ValueReturningTaskC task3 = new ValueReturningTaskC(4, 5, 2000);
		Thread t3 = new Thread(task3, "Thread-3");
		
		t1.start();
		t2.start();
		t3.start();
		
		// --- SINCRONIZACIÓN SECUENCIAL CON JOIN ---
		
		// Esperamos a que la tarea 1 termine.
		t1.join();
		// Ahora que `join()` ha retornado, es seguro leer el resultado.
		System.out.println("Resultado-1 = " + task1.getSum());
		
		// Esperamos a que la tarea 2 termine.
		t2.join();
		System.out.println("Resultado-2 = " + task2.getSum());
		
		// Esperamos a que la tarea 3 termine.
		t3.join();
		System.out.println("Resultado-3 = " + task3.getSum());
		
		// Si bien `join()` hace que la obtención del resultado sea segura, no resuelve
		// el problema de la ineficiencia. El hilo principal todavía espera los resultados
		// en un orden fijo, y el tiempo total de ejecución está limitado por la suma
		// de las esperas.
		//
		// Las técnicas con `Future` y `CompletionService` del tut3 son muy superiores.
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}	
}
