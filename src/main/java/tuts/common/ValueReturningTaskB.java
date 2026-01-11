package tuts.common;

import java.util.concurrent.TimeUnit;

/**
 * Esta clase demuestra una forma muy común y flexible de manejar resultados de tareas asíncronas:
 * el patrón de diseño "Observer" (Observador).
 *
 * En este patrón:
 * - Esta clase (`ValueReturningTaskB`) actúa como el "Sujeto" (Subject) o "Publicador" (Publisher).
 *   Es la que realiza el trabajo y genera un resultado.
 * - El `ResultListener` es la interfaz que define al "Observador" (Observer) o "Suscriptor" (Subscriber).
 *   Es el que está interesado en el resultado cuando esté listo.
 *
 * La tarea no devuelve el valor directamente. En su lugar, cuando termina, "notifica" a su escuchador.
 */
public class ValueReturningTaskB implements Runnable {

	private int a;
	private int b;
	private long sleepTime;
	private int sum;
	
	private static int count = 0;
	private int instanceNumber;
	private final String taskId;
	
	// --- LA CONEXIÓN ENTRE SUJETO Y OBSERVADOR ---
	// Esta variable almacena una referencia al objeto observador que debe ser notificado.
	//
	// Principio de "Bajo Acoplamiento":
	// Fíjate que el tipo es la interfaz `ResultListener`, no una clase concreta. Esto es clave.
	// Significa que esta tarea no sabe ni le importa *quién* es el observador (podría ser un `SumObserver`,
	// un `DataAggregator`, etc.). Lo único que le importa es que el objeto cumple el "contrato"
	// de `ResultListener`, es decir, que tiene un método `notifyResult()`.
	private final ResultListener<Integer> listener;
	
	/**
	 * Constructor de la tarea (el Sujeto).
	 * @param a El primer número a sumar.
	 * @param b El segundo número a sumar.
	 * @param sleepTime El tiempo que simulará estar calculando.
	 * @param listener El objeto "observador" que será notificado con el resultado.
	 */
	public ValueReturningTaskB(int a, int b, long sleepTime, ResultListener<Integer> listener) {
		this.a = a;
		this.b = b;
		this.sleepTime = sleepTime;
		this.listener = listener; // Guardamos la referencia al observador.
		
		this.instanceNumber = ++count;
		this.taskId = "ValueReturningTaskB-" + instanceNumber;
	}
	
	@Override
	public void run() {
		String currentThreadName = Thread.currentThread().getName();
		
		System.out.println("##### [" + currentThreadName + "] <" + taskId + "> [SUJETO] STARTING #####");
		
		try {
			// Simulamos un cálculo que toma tiempo.
			TimeUnit.MILLISECONDS.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Realizamos el cálculo.
		sum = a + b;
		
		System.out.println("***** [" + currentThreadName + "] <" + taskId + "> [SUJETO] CALCULATION COMPLETED *****");
		
		// --- ¡LA PUBLICACIÓN DEL RESULTADO! ---
		// Ahora que el trabajo está hecho, el Sujeto notifica a su Observador.
		// Llama al método `notifyResult` del escuchador que nos pasaron en el constructor
		// y le entrega el resultado (`sum`).
		//
		// En este punto, la responsabilidad de esta tarea termina. No sabe qué hará el
		// observador con el resultado, y no le importa.
		System.out.println("***** [" + currentThreadName + "] <" + taskId + "> [SUJETO] Notifying listener...");
		listener.notifyResult(sum);
	}
	
}
