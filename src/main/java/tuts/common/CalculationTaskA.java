package tuts.common;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Esta clase representa una tarea que realiza un cálculo y DEVUELVE UN RESULTADO.
 * 
 * --- ¡LA DIFERENCIA MÁS IMPORTANTE! ---
 * A diferencia de 'Runnable', esta clase implementa 'Callable<Integer>'.
 * 
 * - 'Runnable': Su método run() es 'void', no devuelve nada. Es para tareas de "haz esto y ya".
 * - 'Callable<T>': Su método call() devuelve un valor del tipo especificado (aquí, 'Integer').
 *   Es para tareas de "calcula esto y devuélveme el resultado".
 */
public class CalculationTaskA implements Callable<Integer> {

	// Los valores de entrada para la suma.
	private final int a;
	private final int b;
	// Tiempo de simulación para que el cálculo parezca que tarda.
	private long sleepTime;
	
	// Identificadores para la tarea, como hemos visto en ejemplos anteriores.
	private static int count = 0;
	private int instanceNumber;
	private final String taskId;
	
	/**
	 * Constructor que inicializa la tarea con los datos necesarios para el cálculo.
	 * @param a El primer número a sumar.
	 * @param b El segundo número a sumar.
	 * @param sleepTime El tiempo que la tarea simulará estar "pensando".
	 */
	public CalculationTaskA(int a, int b, long sleepTime) {
		this.a = a;
		this.b = b;
		this.sleepTime = sleepTime;
		
		this.instanceNumber = ++count;
		this.taskId = "CalculationTaskA-" + instanceNumber;
	}
	
	/**
	 * El método call() es el corazón de una tarea Callable. Es el equivalente al run() de Runnable.
	 * La ejecución del hilo comenzará aquí.
	 * 
	 * Fíjate en dos diferencias clave con run():
	 * 1. Devuelve un valor: 'public Integer call()' en lugar de 'public void run()'.
	 * 2. Puede lanzar excepciones: 'throws Exception', lo que simplifica el manejo de errores.
	 * 
	 * @return El resultado del cálculo (la suma de a + b).
	 */
	@Override
	public Integer call() throws Exception {
		String currentThreadName = Thread.currentThread().getName();
		
		System.out.println("##### [" + currentThreadName + "] <" + taskId + "> STARTING #####");
		
		// Simulamos un cálculo que consume tiempo.
		TimeUnit.MILLISECONDS.sleep(sleepTime);
		
		System.out.println("***** [" + currentThreadName + "] <" + taskId + "> CALCULATION COMPLETED *****");
		
		// --- ¡AQUÍ ESTÁ LA DEVOLUCIÓN DEL RESULTADO! ---
		// Una vez terminado el cálculo, la tarea devuelve el valor.
		// El ExecutorService que ejecutó esta tarea capturará este valor y nos lo
		// entregará a través de un objeto especial llamado 'Future'.
		return a + b;
	}
	
}
