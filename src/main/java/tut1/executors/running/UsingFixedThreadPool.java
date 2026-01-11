package tut1.executors.running;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import tuts.common.LoopTaskA;

/**
 * <p>
 * <b>INTRODUCCIÓN AL EXECUTOR FRAMEWORK: `newFixedThreadPool`</b>
 * </p>
 *
 * Esta clase muestra el uso de un `ExecutorService`, que es la forma moderna
 * y preferida de gestionar hilos en Java.
 * <br><br>
 * <b>Concepto Clave:</b> Un `ExecutorService` es un objeto que gestiona un grupo de
 * hilos (un "pool"). En lugar de crear hilos manualmente (`new Thread()`), le
 * entregamos tareas (`Runnable`) y él se encarga de asignarlas a un hilo libre.
 * <br><br>
 * <b>Analogía:</b> Piensa en un `ExecutorService` como el <b>jefe de una cuadrilla de
 * obreros (hilos)</b>.
 * `Executors.newFixedThreadPool(6)` contrata una cuadrilla con <b>exactamente 6 obreros</b>.
 * Ni más, ni menos.
 */
public class UsingFixedThreadPool {

    public static void main(String[] args) {
        System.out.println("==> Hilo Principal INICIADO <==");

        // Creamos nuestro "jefe de cuadrilla" con 6 "obreros" (hilos).
        ExecutorService executorService = Executors.newFixedThreadPool(6);

        // Le entregamos 6 "planos" (tareas `LoopTaskA`) al jefe.
        // Como hay 6 obreros y 6 tareas, cada obrero coge una y empieza a trabajar inmediatamente.
        // Si entregáramos 10 tareas, 6 empezarían y 4 quedarían en una cola de espera.
        for (int i = 0; i < 6; i++) {
            // `execute()` es la forma de decir: "Jefe, aquí tienes una tarea para hacer".
            executorService.execute(new LoopTaskA());
        }

        // `shutdown()` es como decirle al jefe: "No aceptes más tareas nuevas.
        // Cuando tus obreros terminen las que ya tienen, pueden irse a casa".
        // El `ExecutorService` entra en un estado de apagado: no acepta más `execute()`,
        // pero los hilos actuales terminan su trabajo.
        executorService.shutdown();

        // Si intentamos darle una nueva tarea al jefe después de decirle que cierre,
        // nos la rechazará lanzando una excepción.
        try {
            executorService.execute(new LoopTaskA());
        } catch (RejectedExecutionException e) {
            System.err.println("### Tarea rechazada. El servicio ya no acepta trabajo nuevo. ###");
        }

        // El hilo principal no espera a que las tareas del executor terminen.
        // Simplemente da las órdenes y continúa.
        System.out.println("==> Hilo Principal FINALIZADO <==");
    }
}