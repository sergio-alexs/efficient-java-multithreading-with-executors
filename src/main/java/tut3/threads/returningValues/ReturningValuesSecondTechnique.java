package tut3.threads.returningValues;

import tuts.common.ValueReturningTaskB;

/**
 * <p>
 * <b>TUTORIAL 3: DEVOLVER VALORES - TÉCNICA 2 (ASÍNCRONA CON PATRÓN OBSERVER)</b>
 * </p>
 *
 * Esta clase demuestra una técnica asíncrona y no-bloqueante para obtener
 * resultados, utilizando el <b>Patrón de Diseño Observer (Observador)</b>.
 * <br><br>
 * <b>Analogía:</b>
 * <ul>
 *     <li><b>El Hilo Principal:</b> Es el "Juez de Salida" de una maratón.</li>
 *     <li><b>La Tarea (`ValueReturningTaskB`):</b> Es un "Corredor".</li>
 *     <li><b>El Observador (`SumObserver`):</b> Es un "Familiar" del corredor, esperando en la meta con un cronómetro.</li>
 * </ul>
 * El Juez (hilo principal) da el pistoletazo de salida a todos los corredores (inicia los hilos) y su trabajo termina. No se queda esperando a nadie.
 * Cada vez que un Corredor (tarea) cruza la meta, avisa a su Familiar (observador), quien apunta el resultado. Los resultados se registran a medida que llegan.
 */
public class ReturningValuesSecondTechnique {

	public static void main(String[] args) {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");

		// Creamos una tarea y su observador dedicado.
		// Le "decimos" a la tarea quién es su observador pasándoselo en el constructor.
		ValueReturningTaskB task1 = new ValueReturningTaskB(2, 3, 2000, new SumObserver("task-1"));
		Thread t1 = new Thread(task1, "Thread-1");
		
		ValueReturningTaskB task2 = new ValueReturningTaskB(3, 4, 1000, new SumObserver("task-2"));
		Thread t2 = new Thread(task2, "Thread-2");

		ValueReturningTaskB task3 = new ValueReturningTaskB(4, 5, 500, new SumObserver("task-3"));
		Thread t3 = new Thread(task3, "Thread-3");
		
		// Damos el "pistoletazo de salida".
		t1.start();
		t2.start();
		t3.start();
		
		// --- LA VENTAJA: "FIRE AND FORGET" (DISPARA Y OLVIDA) ---
		// El hilo principal no se bloquea. Su trabajo aquí ha terminado.
		// Ha delegado la responsabilidad de manejar los resultados a los observadores.
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
		
		// Mientras tanto, en segundo plano, a medida que cada tarea termina,
		// su `run()` llama a `observer.notifyResult()`, lo que causa que se imprima el resultado.
		// Verás los resultados aparecer en la consola en el orden en que las tareas terminan
		// (primero task-3, luego task-2, y finalmente task-1), no en el orden en que se iniciaron.
		
		// Aunque este patrón es potente, requiere crear interfaces y clases adicionales.
		// El Executor Framework simplifica esto con `Callable` y `Future`.
	}	
}
