package tut2.executors.naming;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import tuts.common.LoopTaskC;
import tuts.common.NamedThreadsFactory;

/**
 * <p>
 * <b>TUTORIAL 2: NOMBRAR HILOS EN UN EXECUTORSERVICE USANDO UNA THREADFACTORY</b>
 * </p>
 *
 * Esta clase demuestra la forma estándar y correcta de nombrar los hilos
 * que son gestionados por un `ExecutorService`.
 * <br><br>
 * <b>Concepto Clave:</b> Como no creamos los hilos directamente, no podemos llamar a
 * `thread.setName()`. En su lugar, le proporcionamos al `ExecutorService` una
 * "fábrica de hilos" (`ThreadFactory`) que él usará para crear los hilos que necesite.
 * <br><br>
 * <b>Analogía:</b>
 * <ul>
 *   <li><b>ExecutorService:</b> Es el "jefe de la obra".</li>
 *   <li><b>ThreadFactory:</b> Es el "departamento de RRHH".</li>
 * </ul>
 * El jefe no contrata obreros directamente. Cuando necesita uno, le dice a RRHH:
 * "¡Necesito un obrero!". RRHH lo busca, le pone un nombre (ej: "Obrero-5"), y
 * se lo envía al jefe para que le asigne trabajo.
 */
public class NamingExecutorThreads {

    public static void main(String[] args) {
        String currentThreadName = Thread.currentThread().getName();
        System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");

        // --- ¡Aquí está la magia! ---
        // Al crear el ExecutorService, le pasamos nuestra fábrica personalizada.
        // newCachedThreadPool() es un tipo de Executor que crea hilos bajo demanda y
        // reutiliza los que están inactivos.
        ExecutorService execService = Executors.newCachedThreadPool(new NamedThreadsFactory());

        // `NamedThreadsFactory` es una clase que hemos creado y que implementa `ThreadFactory`.
        // Te animo a que abras el archivo 'tuts/common/NamedThreadsFactory.java' para ver
        // cómo implementa el método `newThread(Runnable r)` para devolver un hilo
        // con un nombre personalizado ("PoolWorker-X").

        // Entregamos 3 tareas al "jefe".
        for (int i = 0; i < 3; i++) {
            // Por cada tarea, si el ExecutorService necesita un nuevo hilo, llamará
            // a nuestra `NamedThreadsFactory` para crearlo y nombrarlo.
            execService.execute(new LoopTaskC());
        }

        // Damos la orden de apagar.
        execService.shutdown();

        System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
    }
}
