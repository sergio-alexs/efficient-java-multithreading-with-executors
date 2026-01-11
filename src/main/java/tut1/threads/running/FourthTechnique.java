package tut1.threads.running;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * <b>TUTORIAL 1: TÉCNICA 4 - LA FORMA RECOMENDADA: RUNNABLE + THREAD</b>
 * </p>
 *
 * Esta clase demuestra la forma canónica, más flexible y recomendada de crear
 * y ejecutar tareas en hilos separados en Java.
 * <br><br>
 * Se basa en el principio de <b>separación de responsabilidades</b>:
 * <ul>
 *     <li><b>La Tarea (el "qué"):</b> Un objeto que implementa `Runnable`. Es como una <b>receta</b>.</li>
 *     <li><b>El Hilo (el "quién"):</b> Un objeto `Thread`. Es como el <b>cocinero</b> que sigue la receta.</li>
 * </ul>
 */
public class FourthTechnique {

    public static void main(String[] args) {
        System.out.println("==> Hilo Principal INICIADO <==");

        // --- Hilo 1 ---
        // 1. `new FourthTask()`: Creamos una instancia de nuestra "receta" (la tarea).
        // 2. `new Thread(...)`: Contratamos a un "cocinero" (hilo) y le entregamos la receta.
        // 3. `.start()`: Le decimos al cocinero que empiece a trabajar.
        new Thread(new FourthTask()).start();


        // --- Hilo 2 (mostrado en pasos para mayor claridad) ---
        // 1. Creamos la tarea (la receta).
        FourthTask task2 = new FourthTask();
        // 2. Creamos el hilo y le asignamos la tarea.
        Thread thread2 = new Thread(task2);
        // 3. Iniciamos el hilo.
        thread2.start();


        System.out.println("==> Hilo Principal FINALIZADO <==");
    }
}


/**
 * <h2>FourthTask - La "Receta" (Tarea)</h2>
 *
 * Esta clase implementa `Runnable`, definiendo el trabajo a realizar.
 * Es completamente independiente del mecanismo que la ejecutará. La misma tarea
 * podría ser ejecutada por un hilo simple, un pool de hilos, un servicio planificado, etc.
 * <p>
 * Esto hace que nuestro código sea mucho más limpio, flexible y reutilizable.
 */
class FourthTask implements Runnable {

    private static int count = 0;
    private final int id;

    /**
     * El método `run()` es la implementación de la "receta". Contiene los pasos
     * que el "cocinero" (hilo) deberá seguir.
     */
    @Override
    public void run() {
        for (int i = 10; i > 0; i--) {
            System.out.println(">>> Tarea " + id + ": Tick " + i);

            try {
                TimeUnit.MILLISECONDS.sleep(250);
            } catch (InterruptedException e) {
                System.out.println("!!! Tarea " + id + " interrumpida.");
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("*** Tarea " + id + " terminada ***");
    }

    /**
     * El constructor solo inicializa los datos de la tarea.
     * No sabe nada sobre hilos ni sobre cuándo o cómo se ejecutará.
     */
    public FourthTask() {
        this.id = ++count;
    }
}