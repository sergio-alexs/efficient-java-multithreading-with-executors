package tut1.threads.running;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * <b>TUTORIAL 1: TÉCNICA 3 - IMPLEMENTAR RUNNABLE (con inicio automático)</b>
 * </p>
 *
 * Esta clase introduce la interfaz `Runnable`. Esta es la forma más flexible
 * y recomendada de definir una tarea en Java.
 * <br><br>
 * La idea clave es <b>separar la TAREA (el "qué" hacer) del HILO (el "quién" lo hace)</b>.
 * <br>
 * En este ejemplo específico, la propia tarea `ThirdTask` es responsable de crear un
 * `Thread` para ejecutarse a sí misma, volviendo al anti-patrón de `FirstTechnique`.
 */
public class ThirdTechnique {

    public static void main(String[] args) {
        System.out.println("==> Hilo Principal INICIADO <==");

        // Al crear 'new ThirdTask()', su constructor se ejecuta.
        // El constructor, a su vez, crea un nuevo Thread y lo inicia.
        new ThirdTask(); // Tarea 1 inicia Hilo 1
        new ThirdTask(); // Tarea 2 inicia Hilo 2

        System.out.println("==> Hilo Principal FINALIZADO <==");
    }
}


/**
 * <h2>ThirdTask - Una TAREA que se puede ejecutar</h2>
 *
 * Al usar `implements Runnable`, esta clase define una unidad de trabajo.
 * <b>Un objeto `ThirdTask` NO ES un hilo</b>. Es simplemente un objeto que
 * posee un método `run()` con el código que un hilo puede ejecutar.
 * <p>
 * Ventaja principal: Como no heredas de `Thread`, tu clase `ThirdTask` puede
 * heredar de otra clase si lo necesitas, lo cual es imposible en `FirstTask`
 * y `SecondTask` debido a la herencia simple de Java.
 */
class ThirdTask implements Runnable {

    private static int count = 0;
    private final int id;

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
     * --- ¡ADVERTENCIA! MISMO ANTI-PATRÓN QUE EN FIRSTTECHNIQUE ---
     * <p>
     * Este constructor vuelve a mezclar la creación de la tarea con el inicio
     * de su ejecución, lo cual es una mala práctica.
     */
    public ThirdTask() {
        this.id = ++count;

        // 1. `this`: Es la referencia a este objeto `ThirdTask` (la tarea).
        // 2. `new Thread(this)`: Creamos un "trabajador" (`Thread`) y le entregamos
        //    la tarea (`this`) que debe ejecutar. El `Thread` sabe que debe llamar
        //    al método `run()` del objeto `Runnable` que le pasamos.
        // 3. `.start()`: Inicia el hilo, que a su vez llama a nuestro `run()`.
        //
        // Aunque la sintaxis es diferente (`new Thread(this).start()` vs `this.start()`),
        // el problema es el mismo: el hilo se escapa y empieza a ejecutarse antes
        // de que el constructor de `ThirdTask` haya terminado completamente.
        new Thread(this).start();
    }
}