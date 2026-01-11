package tut2.threads.naming;

import tuts.common.LoopTaskB;

/**
 * <p>
 * <b>TUTORIAL 2: TÉCNICA 1 - NOMBRAR HILOS (ENFOQUE NO RECOMENDADO)</b>
 * </p>
 *
 * <b>¿Por qué nombrar los hilos?</b>
 * Cuando depuras una aplicación (ej. analizando un 'thread dump') o la monitoreas,
 * ver hilos llamados "Thread-0", "Thread-1", etc., no da ninguna información.
 * En cambio, ver "ProcesadorDePagos-1", "NotificadorEmail-3" o "Worker-2"
 * te dice exactamente qué estaba haciendo cada hilo.
 * <br><br>
 * Esta clase demuestra una forma de nombrar hilos, pero de una manera que <b>no es ideal</b>.
 * Aquí, la propia tarea (`LoopTaskB`) es responsable de ponerle nombre al hilo que la ejecuta.
 */
public class NamingThreadsFirstTechnique {

    public static void main(String[] args) {
        // Obtenemos y mostramos el nombre del hilo actual, que por defecto es "main".
        String currentThreadName = Thread.currentThread().getName();
        System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");

        // Creamos y ejecutamos una tarea.
        // IMPORTANTE: La lógica que nombra al hilo NO está aquí. Está oculta dentro de LoopTaskB.
        // Te animo a que abras el archivo 'tuts/common/LoopTaskB.java' para ver cómo
        // usa `Thread.currentThread().setName()` en su método `run()`.
        new Thread(new LoopTaskB()).start();

        // Creamos una segunda tarea.
        Thread t2 = new Thread(new LoopTaskB());
        t2.start();

        // Este enfoque rompe la separación de responsabilidades:
        // - La TAREA (el "qué") debería solo contener la lógica de su trabajo.
        // - El CREADOR del hilo (el "quién", en este caso el método `main`) debería ser
        //   responsable de configurar el hilo, incluyendo su nombre.
        //
        // En la siguiente técnica veremos una forma mucho mejor de hacer esto.

        System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
    }
}
