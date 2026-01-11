package tut3.threads.returningValues;

import tuts.common.ResultListener;

/**
 * Esta clase es un "Observador" o "Listener" (un escuchador).
 * Su único propósito es esperar a que le notifiquen un resultado y luego hacer algo con él.
 * 
 * Este es un componente del patrón de diseño Observer. En este patrón, hay:
 * 1. Un "Sujeto" (o "Productor"): La tarea que realiza el cálculo.
 * 2. Un "Observador" (o "Consumidor"): Esta clase, que se suscribe al sujeto para
 *    ser notificada cuando el resultado esté listo.
 * 
 * Esta clase no sabe cómo se realiza el cálculo, solo sabe qué hacer cuando recibe el resultado.
 */
public class SumObserver implements ResultListener<Integer> {
	
	private final String taskId;
	
	/**
	 * Constructor que le da un nombre o identificador a este observador,
	 * para que sepamos a qué tarea está asociado.
	 * @param taskId El identificador de la tarea que este observador está "escuchando".
	 */
	public SumObserver(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * Este es el método que será llamado por la tarea de cálculo cuando haya terminado.
	 * La interfaz 'ResultListener<Integer>' nos obliga a implementar este método.
	 * 
	 * Es un "callback": nosotros no lo llamamos directamente. Se lo pasamos a la tarea
	 * y la tarea lo llamará por nosotros cuando tenga el resultado.
	 * 
	 * @param result El resultado del cálculo (un Integer, como se especifica en 'ResultListener<Integer>').
	 */
	@Override
	public void notifyResult(Integer result) {
		// Cuando se nos notifica, simplemente imprimimos el resultado en la consola.
		System.out.println("Result for " + taskId + " = " + result);
	}

}
