package tuts.common;

import java.util.concurrent.TimeUnit;

/**
 * <h2>ANTI-PATRÓN: ¡NO HAGAS ESTO!</h2>
 * 
 * Esta clase es un ejemplo deliberadamente INCORRECTO y PELIGROSO. Su propósito es
 * demostrar los problemas que surgen cuando se intenta devolver un valor desde un
 * hilo sin la debida SINCRONIZACIÓN.
 * 
 * El código de esta clase sufre de dos problemas graves de concurrencia:
 * 1.  <b>Condición de Carrera (Race Condition)</b>: El resultado depende de la "suerte"
 *     de quién llega primero, el hilo que calcula o el hilo que lee.
 * 2.  <b>Problema de Visibilidad de Memoria (Memory Visibility)</b>: No hay garantía
 *     de que el resultado escrito por un hilo sea visible para el otro.
 */
public class ValueReturningTaskC implements Runnable {

	private int a;
	private int b;
	private long sleepTime;
	
	// La variable donde se guardará el resultado.
	// PROBLEMA: No es 'volatile' y no se accede a ella en un bloque 'synchronized'.
	private int sum;
	
	private static int count = 0;
	private int instanceNumber;
	private String taskId;
	
	public ValueReturningTaskC(int a, int b, long sleepTime) {
		this.a = a;
		this.b = b;
		this.sleepTime = sleepTime;
		
		this.instanceNumber = ++count;
		this.taskId = "ValueReturningTaskC-" + instanceNumber;
	}
	
	/**
	 * El método `run()` es ejecutado por el hilo "Productor".
	 */
	@Override
	public void run() {
		String currentThreadName = Thread.currentThread().getName();
		
		System.out.println("##### [" + currentThreadName + "] <" + taskId + "> [PRODUCTOR] STARTING #####");
		
		try {
			TimeUnit.MILLISECONDS.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// El hilo Productor calcula la suma y la escribe en su copia de la variable 'sum'.
		sum = a + b;
		
		System.out.println("***** [" + currentThreadName + "] <" + taskId + "> [PRODUCTOR] CALCULATION COMPLETED *****");
	}
	
	/**
	 * Este método es llamado por el hilo "Consumidor" (por ejemplo, el hilo `main`).
	 * @return El valor (probablemente incorrecto) de 'sum'.
	 */
	public int getSum() {
		// --- ¡AQUÍ ESTÁN LOS ERRORES FUNDAMENTALES! ---
		
		// 1. CONDICIÓN DE CARRERA (Race Condition)
		//    Este método no tiene forma de saber si el hilo Productor ya ha terminado.
		//    Es como preguntar a un chef por un pastel. Si preguntas demasiado pronto,
		//    ¡puede que te dé un bol con harina y huevos! Si preguntas más tarde,
		//    puede que te dé el pastel ya hecho. El resultado es impredecible.
		//    Aquí, si `getSum()` se llama antes de que `run()` termine, devolverá `0` (el valor por defecto).
		
		// 2. PROBLEMA DE VISIBILIDAD DE MEMORIA (Java Memory Model)
		//    Aún peor, incluso si `getSum()` se llama *después* de que `run()` termine,
		//    no hay garantía de que el hilo Consumidor "vea" el valor de `sum` que escribió el Productor.
		//    Por optimización, cada hilo puede tener su propia "copia cacheada" de la variable.
		//    Sin `synchronized` o `volatile`, Java no garantiza que la copia del Consumidor
		//    se actualice con el valor escrito por el Productor. El Consumidor podría ver `0` para siempre.

		System.out.println("***** [" + Thread.currentThread().getName() + "] <" + taskId + "> [CONSUMIDOR] RETURNING SUM...");
		return sum;
		
		// SOLUCIONES CORRECTAS:
		// - `ValueReturningTaskA` usa `wait/notify` para asegurar que el consumidor espera al productor.
		// - `ValueReturningTaskB` usa el patrón Observer para que el productor notifique al consumidor.
		// - La mejor solución moderna es usar `Callable` con un `ExecutorService`, que gestiona todo esto
		//   automáticamente a través de un objeto `Future`.
	}

}
