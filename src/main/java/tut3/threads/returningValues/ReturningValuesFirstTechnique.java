package tut3.threads.returningValues;

import tuts.common.ValueReturningTaskA;

/**
 * <p>
 * <b>TUTORIAL 3: DEVOLVER VALORES - TÉCNICA 1 (MANUAL Y BLOQUEANTE)</b>
 * </p>
 *
 * Esta clase demuestra una técnica manual para obtener resultados de tareas en otros hilos.
 * <br><br>
 * <b>Mecanismo:</b> La clase `ValueReturningTaskA` implementa un sistema de comunicación
 * de bajo nivel usando `wait()` y `notify()`:
 * <ul>
 *     <li>El hilo principal (el "consumidor") llama a `getSum()`. Si el resultado no
 *         está listo, se bloquea llamando a `wait()` sobre el objeto de la tarea.</li>
 *     <li>El hilo de la tarea (el "productor") realiza el cálculo y, al terminar,
 *         llama a `notify()` sobre sí mismo para despertar al hilo principal.</li>
 * </ul>
 * Esta clase demuestra por qué este enfoque, aunque funcional, es <b>muy ineficiente</b>.
 */
public class ReturningValuesFirstTechnique {

	public static void main(String[] args) {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		// Creamos tres tareas con diferentes duraciones.
		ValueReturningTaskA task1 = new ValueReturningTaskA(2, 3, 2000); // La más lenta
		ValueReturningTaskA task2 = new ValueReturningTaskA(3, 4, 1000); // Intermedia
		ValueReturningTaskA task3 = new ValueReturningTaskA(4, 5, 500);  // La más rápida
		
		Thread t1 = new Thread(task1, "Thread-1");
		Thread t2 = new Thread(task2, "Thread-2");
		Thread t3 = new Thread(task3, "Thread-3");
		
		// Iniciamos la ejecución en paralelo. Las tres tareas corren a la vez.
		t1.start();
		t2.start();
		t3.start();
		
		// --- EL PROBLEMA: RECUPERACIÓN SECUENCIAL Y BLOQUEANTE ---
		// A pesar de que las tareas se ejecutan en paralelo, aquí recuperamos
		// sus resultados de forma estrictamente secuencial.

		// 1. Pedimos el resultado de la tarea 1. `getSum()` bloqueará el hilo principal
		//    durante ~2000ms. Aunque `task3` y `task2` terminen mucho antes,
		//    el hilo principal está "atascado" aquí y no puede ir a por sus resultados.
		System.out.println("Pidiendo resultado de Tarea 1...");
		System.out.println("Resultado-1 = " + task1.getSum());
		System.out.println("...Resultado de Tarea 1 OBTENIDO!");
		
		// 2. Cuando `task1.getSum()` por fin devuelve el control, pedimos el resultado de la tarea 2.
		//    Como `task2` ya terminó hace tiempo, `getSum()` devolverá el valor inmediatamente.
		System.out.println("Pidiendo resultado de Tarea 2...");
		System.out.println("Resultado-2 = " + task2.getSum());
		System.out.println("...Resultado de Tarea 2 OBTENIDO!");
		
		// 3. Lo mismo para la tarea 3.
		System.out.println("Pidiendo resultado de Tarea 3...");
		System.out.println("Resultado-3 = " + task3.getSum());
		System.out.println("...Resultado de Tarea 3 OBTENIDO!");
		
		// Este patrón hace que el tiempo total del programa sea, como mínimo, el de la
		// tarea más lenta que se consulta primero, desaprovechando el paralelismo.
		// La solución moderna a esto es usar `Callable` y `Future`, que veremos más adelante.
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}	
}
