package tut4.threads.daemonThreads;

import tuts.common.LoopTaskD;

/**
 * <p>
 * <b>TUTORIAL 4: HILOS DEMONIO (DAEMON) VS. HILOS DE USUARIO (USER)</b>
 * </p>
 *
 * Esta clase demuestra la diferencia fundamental entre los dos tipos de hilos en Java.
 * <br><br>
 * <b>Analogía de la Obra de Teatro:</b>
 * <ul>
 *     <li><b>Hilos de Usuario:</b> Son los "Actores Principales". La obra (la aplicación JVM)
 *         no puede terminar hasta que todos los actores principales hayan finalizado su actuación.
 *         El hilo `main` es siempre un hilo de usuario.</li>
 *     <li><b>Hilos Demonio:</b> Son el "Personal de Backstage" (luces, sonido, tramoyistas).
 *         Su trabajo es dar soporte a los actores. En cuanto el último actor principal
 *         abandona el escenario, el telón cae INMEDIATAMENTE y la luz se apaga, sin importar
 *         si el personal de backstage ha terminado de guardar las cosas.</li>
 * </ul>
 */
public class DaemonThreads {

	public static void main(String[] args) {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		// --- Hilo 1: El "Personal de Backstage" ---
		Thread t1 = new Thread(new LoopTaskD(500), "DaemonThread-1");
		
		// `setDaemon(true)` marca este hilo como un hilo de soporte.
		// ¡IMPORTANTE! Esta llamada debe hacerse ANTES de llamar a `t1.start()`.
		t1.setDaemon(true);
		
		// --- Hilo 2: El "Actor Principal" ---
		// Por defecto, cualquier hilo que creamos es un hilo de usuario.
		Thread t2 = new Thread(new LoopTaskD(1000), "UserThread-2");
		
		t1.start();
		t2.start();
		
		// El hilo 'main' (otro "actor principal") termina su parte aquí.
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
		
		// --- ¿QUÉ SUCEDE AHORA? ---
		// 1. El hilo `main` (usuario) ha terminado.
		// 2. Quedan dos hilos en ejecución: `t1` (demonio) y `t2` (usuario).
		//
		// REGLA DE ORO: La JVM (la aplicación) se mantendrá viva mientras exista
		// AL MENOS UN HILO DE USUARIO activo.
		//
		// Por lo tanto, la aplicación esperará a que `t2` (el último actor) termine su
		// tarea (que dura 10 * 1000ms = 10 segundos).
		//
		// Cuando `t2` termine, ya no quedará ningún hilo de usuario. En ese instante,
		// la JVM se apagará abruptamente, "matando" al hilo `t1` (demonio) sin piedad,
		// aunque no haya completado su bucle.
		//
		// *** ADVERTENCIA CRÍTICA ***
		// Debido a su terminación abrupta, NUNCA uses hilos demonio para tareas críticas
		// como escribir en archivos, bases de datos o liberar recursos, ya que los bloques
		// `finally` no están garantizados a ejecutarse.
	}	
}
