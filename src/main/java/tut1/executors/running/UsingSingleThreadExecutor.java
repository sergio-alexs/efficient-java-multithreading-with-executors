package tut1.executors.running;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import tuts.common.LoopTaskA;

/**
 * <p>
 * <b>INTRODUCCIÓN AL EXECUTOR FRAMEWORK: `newSingleThreadExecutor`</b>
 * </p>
 *
 * Esta clase demuestra el uso de un `SingleThreadExecutor`.
 * <br><br>
 * <b>Concepto Clave:</b> Como su nombre indica, este executor tiene un pool de <b>un solo hilo</b>.
 * <br><br>
 * <b>Comportamiento:</b>
 * <ul>
 *     <li>Garantiza que las tareas se ejecuten <b>secuencialmente</b> (una detrás de otra).</li>
 *     <li>El orden de ejecución será el orden en que se enviaron las tareas (FIFO - First In, First Out).</li>
 *     <li>Si el único hilo muere por una excepción, se crea uno nuevo para reemplazarlo y seguir con la cola.</li>
 * </ul>
 * <br>
 * <b>Analogía:</b> Imagina una <b>ventanilla única</b> en un banco con un solo cajero.
 * Los clientes (tareas) hacen una fila. El cajero atiende al primero, termina, y luego pasa al siguiente.
 * Nunca atenderá a dos a la vez.
 */
public class UsingSingleThreadExecutor {

    public static void main(String[] args) {
        System.out.println("==> Hilo Principal INICIADO <==");

        // Creamos nuestro "cajero único".
        ExecutorService execService = Executors.newSingleThreadExecutor();

        // Le entregamos 3 tareas.
        // A diferencia de los ejemplos anteriores, aquí NO veremos ejecución paralela.
        // Veremos que la Tarea 1 empieza y termina, LUEGO la Tarea 2 empieza y termina, etc.
        for (int i = 0; i < 3; i++) {
            execService.execute(new LoopTaskA());
        }

        // Cerramos la ventanilla. El cajero terminará la cola actual y luego se irá.
        execService.shutdown();

        // nos la rechazará lanzando una excepción.
        try {
            execService.execute(new LoopTaskA());
        } catch (RejectedExecutionException e) {
            System.err.println("### Tarea rechazada. El servicio ya no acepta trabajo nuevo. ###");
        }


        System.out.println("==> Hilo Principal FINALIZADO <==");
    }
}