package tut5.threads.aliveCheck;

import java.util.concurrent.TimeUnit;

import tuts.common.LoopTaskC;

/**
 * <p>
 * <b>TUTORIAL 5: COMPROBAR SI UN HILO ESTÁ VIVO - TÉCNICA MANUAL CON ISALIVE()</b>
 * </p>
 *
 * Esta clase demuestra el uso del método `isAlive()` para monitorizar el estado de un hilo.
 * <br><br>
 * <b>Definición:</b> Un hilo se considera "vivo" (`isAlive() == true`) si su método
 * `start()` ha sido invocado y todavía no ha terminado su ejecución (su método `run()`
 * no ha finalizado).
 */
public class ThreadsAliveCheck {

	public static void main(String[] args) throws InterruptedException {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		Thread t1 = new Thread(new LoopTaskC(), "MyThread-1");
		Thread t2 = new Thread(new LoopTaskC(), "MyThread-2");
		
		// Antes de llamar a start(), el hilo está en estado NEW y no está "vivo".
		System.out.println(">>> ANTES de start(): " + t1.getName() + " está vivo? = " + t1.isAlive());
		System.out.println(">>> ANTES de start(): " + t2.getName() + " está vivo? = " + t2.isAlive());
		
		System.out.println("\n--- Iniciando Hilos ---\n");
		t1.start();
		t2.start();
		
		// --- ANTI-PATRÓN: BUCLE DE POLLING (ESPERA ACTIVA) ---
		// Este bucle comprueba repetidamente el estado de los hilos. Es una forma de
		// "espera activa" (busy-waiting) que, aunque funciona, es ineficiente.
		// El hilo principal gasta tiempo de CPU despertándose solo para preguntar "¿ya terminaste?".
		while(true) {
			// Pausa para no inundar la consola.
			TimeUnit.MILLISECONDS.sleep(600);
			
			boolean t1IsAlive = t1.isAlive();
			boolean t2IsAlive = t2.isAlive();
			
			System.out.println(">>> POLLING: " + t1.getName() + " está vivo? = " + t1IsAlive);
			System.out.println(">>> POLLING: " + t2.getName() + " está vivo? = " + t2IsAlive);
			
			// Si ambos hilos han terminado, salimos del bucle.
			if (!t1IsAlive && !t2IsAlive) {
				break;
			}
		}

		// ** LA FORMA CORRECTA Y EFICIENTE **
		// En lugar del bucle de polling anterior, la forma idiomática de esperar a que un hilo
		// termine es usar el método `join()`.
		//
		// t1.join(); // Esto le diría al hilo principal: "Páusate aquí hasta que t1 muera".
		// t2.join(); // Y luego: "Ahora, páusate aquí hasta que t2 muera".
		//
		// `join()` es mucho más eficiente porque el sistema operativo "duerme" al hilo
		// que espera, sin consumir CPU. Veremos `join()` en detalle en el `tut8`.

		System.out.println("\n==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}	
}
