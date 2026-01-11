package tut2.executors.naming;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import tuts.common.LoopTaskC;
import tuts.common.NamedThreadsFactory;

/**
 * <p>
 * <b>TUTORIAL 2: DEMOSTRACIÓN DE REUTILIZACIÓN DE HILOS CON `CachedThreadPool`</b>
 * </p>
 *
 * Esta clase demuestra una característica clave del `CachedThreadPool`: la <b>reutilización
 * de hilos</b>. Un `CachedThreadPool` crea hilos bajo demanda, pero si uno termina
 * su tarea, lo mantiene "en caché" (generalmente por 60s) para dárselo a una nueva
 * tarea que llegue, ahorrando así el coste de crear un hilo desde cero.
 * <br><br>
 * Gracias a nuestra `NamedThreadsFactory`, podemos <b>ver</b> este comportamiento en la consola.
 */
public class UsingCachedThreadPool_Part2 {

    public static void main(String[] args) throws InterruptedException {
        String currentThreadName = Thread.currentThread().getName();
        System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");

        ExecutorService execService = Executors.newCachedThreadPool(new NamedThreadsFactory());

        // --- ACTO 1: Creación inicial de hilos ---
        // Enviamos 3 tareas. Como el pool está vacío, creará 3 hilos nuevos.
        // ***** QUÉ OBSERVAR EN LA CONSOLA: *****
        // Verás mensajes de "PoolWorker-1", "PoolWorker-2" y "PoolWorker-3".
        System.out.println(">>> LANZANDO PRIMER LOTE DE 3 TAREAS...");
        for (int i = 0; i < 3; i++) {
            execService.execute(new LoopTaskC());
        }

        // Hacemos una pausa de 15 segundos. Las tareas LoopTaskC son cortas, por lo que
        // para cuando esta pausa termine, los 3 hilos habrán completado su trabajo
        // y estarán inactivos, esperando en la caché del Executor.
        System.out.println(">>> ESPERANDO 15 SEGUNDOS PARA QUE LOS HILOS QUEDEN INACTIVOS...");
        TimeUnit.SECONDS.sleep(15);

        // --- ACTO 2: Reutilización y creación de nuevos hilos ---
        // Ahora enviamos 5 tareas más.
        // ***** QUÉ OBSERVAR EN LA CONSOLA: *****
        // 1. Para las primeras 3 tareas, el `CachedThreadPool` REUTILIZARÁ los hilos
        //    existentes. Verás mensajes de "PoolWorker-1", "PoolWorker-2" y "PoolWorker-3" OTRA VEZ.
        // 2. Para la 4ª y 5ª tarea, al no haber más hilos en caché, creará dos hilos NUEVOS.
        //    Verás aparecer por primera vez "PoolWorker-4" y "PoolWorker-5".
        System.out.println(">>> LANZANDO SEGUNDO LOTE DE 5 TAREAS...");
        for (int i = 0; i < 5; i++) {
            execService.execute(new LoopTaskC());
        }

        execService.shutdown();
        System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
    }
}
