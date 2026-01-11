package tuts.common;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Esta clase representa una tarea que, además de hacer su trabajo, notifica
 * a un 'CountDownLatch' cuando ha terminado.
 * 
 * Un CountDownLatch es una herramienta de sincronización que permite a uno o más
 * hilos esperar hasta que un conjunto de operaciones que se realizan en otros
 * hilos se completen.
 */
public class LoopTaskI implements Runnable {
	
	private static int count = 0;
	private int instanceNumber;
	private String taskId;
	
	// Una referencia a un CountDownLatch. Este objeto será compartido entre
	// varias tareas y el hilo que las espera.
	private CountDownLatch doneCountLatch;
	
	@Override
	public void run() {
		boolean isRunningInDaemonThread = Thread.currentThread().isDaemon();
		String threadType = isRunningInDaemonThread ? "DAEMON" : "USER  ";
		
		String currentThreadName = Thread.currentThread().getName();
		
		System.out.println("##### [" + currentThreadName + ", " + threadType + "] <" + taskId + "> STARTING #####");
		
		// Bucle de trabajo de la tarea.
		for (int i=10; i>0; i--) {
			System.out.println("[" + currentThreadName + ", " + threadType + "] <" + taskId + "> TICK TICK - " + i);
			
			try {
				TimeUnit.MILLISECONDS.sleep((long)(Math.random() * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("***** [" + currentThreadName + ", " + threadType + "] <" + taskId +	"> COMPLETED *****");
		
		// --- ¡AQUÍ ESTÁ LA NOTIFICACIÓN! ---
		if (doneCountLatch != null) {
			// countDown() decrementa el contador del latch en uno.
			// Cada tarea que termina llama a este método.
			doneCountLatch.countDown();
			
			System.out.println("[" + currentThreadName + ", " + threadType + "] <" + taskId +	"> LATCH COUNT = " +
					doneCountLatch.getCount());
		}
	}
	
	/**
	 * Constructor de la tarea.
	 * @param doneCountLatch El latch que esta tarea debe notificar al terminar.
	 */
	public LoopTaskI(CountDownLatch doneCountLatch) {
		this.doneCountLatch = doneCountLatch;
		
		this.instanceNumber = ++count;
		this.taskId = "LoopTaskI" + instanceNumber;
	}
}
