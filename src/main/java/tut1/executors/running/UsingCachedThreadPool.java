package tut1.executors.running;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import tuts.common.LoopTaskA;

/**
 * <p>
 * <b>INTRODUCCIÓN AL EXECUTOR FRAMEWORK: `newCachedThreadPool`</b>
 * </p>
 *
 * Esta clase demuestra el uso de un `CachedThreadPool`.
 * <br><br>
 * <b>Concepto Clave:</b> Un `CachedThreadPool` es un pool flexible. No tiene un tamaño fijo.
 * Crea nuevos hilos según sea necesario para las tareas entrantes, pero intentará reutilizar
 * hilos que hayan quedado libres anteriormente.
 * <br><br>
 * <b>Comportamiento:</b>
 * <ul>
 *     <li>Si llega una tarea y hay un hilo libre, lo usa.</li>
 *     <li>Si llega una tarea y todos están ocupados, crea un hilo nuevo inmediatamente.</li>
 *     <li>Si un hilo pasa 60 segundos sin hacer nada, se elimina (se despide).</li>
 * </ul>
 * <br>
 * <b>Analogía:</b> Imagina una <b>agencia de trabajo temporal</b> muy ágil.
 * Si hay trabajo, llaman a alguien nuevo al instante. Si alguien termina su trabajo, espera un poco
 * por si sale otra cosa. Si en 60 segundos no hay nada nuevo, se va a casa.
 * Es ideal para muchas tareas cortas y asíncronas.
 */
public class UsingCachedThreadPool {

    public static void main(String[] args) {
        System.out.println("==> Hilo Principal INICIADO <==");

        // Creamos nuestro "jefe de agencia temporal".
        // A diferencia del FixedThreadPool, aquí no le decimos cuántos hilos queremos.
        // El pool crecerá o se encogerá dinámicamente.
        ExecutorService execService = Executors.newCachedThreadPool();

        // Le entregamos 6 tareas.
        // Como las tareas se envían muy rápido en este bucle, es probable que el pool
        // cree 6 hilos casi simultáneamente, ya que al enviar la tarea 2, el hilo de la tarea 1
        // probablemente siga ocupado.
        for (int i = 0; i < 6; i++) {
            execService.execute(new LoopTaskA());
        }

        // Le decimos al jefe que cierre el chiringuito.
        // No acepta más trabajos, pero deja que los temporales terminen lo que están haciendo.
        execService.shutdown();


        // Si intentamos darle una nueva tarea al jefe después de decirle que cierre,
        // nos la rechazará lanzando una excepción.
        try {
            execService.execute(new LoopTaskA());
        } catch (RejectedExecutionException e) {
            System.err.println("### Tarea rechazada. El servicio ya no acepta trabajo nuevo. ###");
        }

        System.out.println("==> Hilo Principal FINALIZADO <==");
    }
}