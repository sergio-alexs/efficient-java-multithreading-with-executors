package tuts.common;

import java.util.concurrent.TimeUnit;

/**
 * Esta clase, `LoopTaskA`, es un ejemplo de una tarea que puede ser ejecutada por un hilo (Thread).
 * 
 * En Java, una de las formas más comunes de definir el trabajo que un hilo debe realizar es
 * implementando la interfaz `Runnable`. Esta interfaz tiene un único método: `run()`.
 * 
 * Piensa en una clase que implementa `Runnable` como una "unidad de trabajo" o una "tarea" que
 * puede ser entregada a un hilo para que la ejecute de forma asíncrona.
 */
public class LoopTaskA implements Runnable {
	
	// `count` es una variable 'static'. Esto significa que pertenece a la clase `LoopTaskA` en su conjunto,
	// y no a una instancia individual. Todas las instancias de `LoopTaskA` comparten esta misma variable.
	// La usamos para llevar la cuenta de cuántas tareas se han creado.
	private static int count = 0;
	
	// `id` es una variable de instancia. Cada objeto (instancia) de `LoopTaskA` tendrá su propia
	// copia de `id`, con su propio valor. Esto nos permite identificar cada tarea de forma única.
	private final int id;
	
	/**
	 * El método `run()` es el corazón de un `Runnable`. Contiene la lógica que el hilo ejecutará.
	 * Cuando "inicias" un hilo con esta tarea, el hilo llama a este método y comienza a ejecutar
	 * el código que hay aquí dentro. Una vez que este método termina, el hilo también termina su ejecución.
	 */
	@Override
	public void run() {
		// Se imprime un mensaje para saber cuándo ha comenzado esta tarea específica.
		System.out.println("##### <TASK-" + id + "> STARTING #####");
		
		// Este bucle simula una tarea que realiza un trabajo durante un tiempo.
		for (int i=10; i>0; i--) {
			System.out.println("<" + id + ">TICK TICK - " + i);
			
			try {
				// `TimeUnit.MILLISECONDS.sleep()` es una forma de pausar el hilo actual.
				// Esto pone al hilo en un estado de "espera" (TIMED_WAITING) y no consume CPU
				// durante ese tiempo. Es muy útil para simular operaciones que tardan,
				// como descargar un archivo, esperar una respuesta de red, o para no saturar el sistema.
				TimeUnit.MILLISECONDS.sleep((long)(Math.random() * 1000));
			} catch (InterruptedException e) {
				// `InterruptedException` es una excepción especial (checked exception). Se lanza si otro hilo
				// "interrumpe" a este hilo mientras está en `sleep()` (o en otros métodos de espera como `wait()`).
				// La interrupción es un mecanismo cooperativo para pedirle a un hilo que deje de hacer lo que
				// está haciendo. En lugar de ignorarla, una buena práctica es registrarla o volver a establecer
				// el estado de interrupción para que el código superior pueda manejarla.
				e.printStackTrace();
			}
		}
		
		// Se imprime un mensaje para saber que la tarea ha finalizado su trabajo.
		System.out.println("***** <TASK-" + id + "> COMPLETED *****");
	}
	
	/**
	 * Constructor de `LoopTaskA`. Se llama cada vez que se crea una nueva instancia (`new LoopTaskA()`).
	 */
	public LoopTaskA() {
		// Incrementamos el contador estático y asignamos el nuevo valor al `id` de esta instancia.
		// El pre-incremento `++count` asegura que el valor se incrementa *antes* de ser asignado.
		// Esto garantiza que cada tarea creada tenga un ID único y secuencial (1, 2, 3, ...).
		this.id = ++count;
	}
}