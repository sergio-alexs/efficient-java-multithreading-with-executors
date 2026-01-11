package tut4.executors.daemonThreads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import tuts.common.LoopTaskD;
import tuts.common.NamedDaemonThreadsFactory;

/**
 * <p>
 * <b>TUTORIAL 4: HILOS DEMONIO CON EXECUTORSERVICE USANDO THREADFACTORY</b>
 * </p>
 *
 * Esta clase demuestra cómo un `ExecutorService` puede gestionar una mezcla
 * de hilos de usuario y demonio gracias a una `ThreadFactory` personalizada.
 * <br><br>
 * <b>Concepto Clave:</b> La `ThreadFactory` actúa como una "política de contratación"
 * para el `ExecutorService`. El Executor simplemente pide hilos y la fábrica
 * se los entrega ya configurados según su lógica interna.
 */
public class DaemonThreadsUsingExecutors {

    public static void main(String[] args) throws InterruptedException {
        String currentThreadName = Thread.currentThread().getName();
        System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");

        // Nuestra `NamedDaemonThreadsFactory` tiene una "política" especial:
        // "Contrata a los empleados impares (1, 3, ...) como fijos (USER threads)
        // y a los pares (2, 4, ...) como temporales (DAEMON threads)".
        ExecutorService execService = Executors.newCachedThreadPool(new NamedDaemonThreadsFactory());

        // Enviamos 4 tareas. El ExecutorService pedirá 4 hilos a la fábrica:
        execService.execute(new LoopTaskD(1000)); // Creado como PoolWorker-1 (USER)
        execService.execute(new LoopTaskD(2000)); // Creado como PoolWorker-2 (DAEMON)
        execService.execute(new LoopTaskD(3000)); // Creado como PoolWorker-3 (USER)
        execService.execute(new LoopTaskD(4000)); // Creado como PoolWorker-4 (DAEMON)

        execService.shutdown();
        
        // --- PREDICCIÓN DEL RESULTADO ---
        // La JVM se mantendrá viva hasta que los hilos de USUARIO terminen.
        // Hilos de usuario: PoolWorker-1 (dura 10s) y PoolWorker-3 (dura 30s).
        // Hilos demonio: PoolWorker-2 (dura 20s) y PoolWorker-4 (dura 40s).
        //
        // La aplicación se cerrará cuando el último hilo de usuario (PoolWorker-3) termine,
        // es decir, después de unos 30 segundos.
        // En ese momento, el PoolWorker-4 (demonio), que necesita 40s, será terminado
        // abruptamente a mitad de su trabajo.
        // ¡Ejecuta el código y comprueba si PoolWorker-4 llega a imprimir "COMPLETED"!

        System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
    }
}
