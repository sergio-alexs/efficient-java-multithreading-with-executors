package tut8.threads.joining;

import tuts.common.LoopTaskD;

/**
 * <p>
 * <b>TUTORIAL 8: UNIR HILOS (JOINING) - LA FORMA CORRECTA DE ESPERAR</b>
 * </p>
 *
 * Esta clase demuestra el uso del método `Thread.join()` para hacer que un hilo
 * espere a que otro termine. Es la forma correcta y eficiente de sincronizar
 * la finalización de hilos.
 * <br><br>
 * <b>Analogía de la Carrera de Relevos:</b>
 * El hilo `main` es el primer corredor. Cuando llama a `t1.start()`, le pasa el
 * testigo a `t1`. Pero en lugar de seguir corriendo, `main` se sienta a esperar
 * en la línea de meta (`t1.join()`). Solo cuando `t1` completa su vuelta, `main`
 * se levanta y puede continuar con su siguiente acción.
 */
public class JoiningThreads {

	public static void main(String[] args) throws InterruptedException {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		Thread t1 = new Thread(new LoopTaskD(100), "MyThread-1");
		Thread t2 = new Thread(new LoopTaskD(200), "MyThread-2");
		Thread t3 = new Thread(new LoopTaskD(500), "MyThread-3");
		Thread t4 = new Thread(new LoopTaskD(400), "MyThread-4");
		
		// Los 4 hilos empiezan a correr en paralelo.
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		// --- SINCRONIZACIÓN CON JOIN ---
		
		// `t1.join()`: El hilo `main` se bloquea aquí. El sistema operativo lo pone
		// a "dormir" de forma muy eficiente (sin consumir CPU) hasta que `t1` muera.
		// Esto es muy superior al "polling" con `isAlive()` que vimos en el tut5.
		t1.join();
		System.out.println("[" + currentThreadName + "] ==> 'main' se ha unido a '" + t1.getName() + "'");
		
		// El hilo `main` se despierta y ahora espera a `t2`. Si `t2` ya había
		// terminado, `join()` retorna inmediatamente.
		t2.join();
		System.out.println("[" + currentThreadName + "] ==> 'main' se ha unido a '" + t2.getName() + "'");

		t3.join();
		System.out.println("[" + currentThreadName + "] ==> 'main' se ha unido a '" + t3.getName() + "'");

		t4.join();
		System.out.println("[" + currentThreadName + "] ==> 'main' se ha unido a '" + t4.getName() + "'");

		// NOTA: También existen `join(long millis)` y `join(long millis, int nanos)`,
		// que esperan solo un tiempo máximo. Son cruciales para evitar que la aplicación
		// se quede colgada para siempre si un hilo se atasca.
		
		System.out.println("\n==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
