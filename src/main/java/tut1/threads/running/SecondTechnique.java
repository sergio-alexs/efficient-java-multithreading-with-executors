package tut1.threads.running;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * <b>TUTORIAL 1: TÉCNICA 2 - SEPARAR CREACIÓN DE INICIO</b>
 * </p>
 *
 * Esta clase demuestra la forma correcta y segura de iniciar hilos cuando
 * se hereda de la clase `Thread`.
 * <br><br>
 * La diferencia fundamental con `FirstTechnique` es que aquí separamos la
 * <b>creación</b> del objeto (`new SecondTask()`) de su <b>inicio</b> (`task.start()`).
 */
public class SecondTechnique {

    public static void main(String[] args) {
        System.out.println("==> Hilo Principal INICIADO <==");

        // --- Hilo 1 ---
        // Se crea un objeto 'SecondTask' y, justo después, se llama a su método start().
        // El hilo comienza su ejecución en segundo plano. Esto es mucho más seguro.
        SecondTask task1 = new SecondTask();
        task1.start();

        // --- Hilo 2 ---
        // Hacemos lo mismo para un segundo hilo.
        SecondTask task2 = new SecondTask();
        task2.start();

        System.out.println("==> Hilo Principal FINALIZADO <==");
    }
}


/**
 * <h2>SecondTask - Un Hilo con Inicio Controlado</h2>
 *
 * La estructura de la clase es casi idéntica a `FirstTask`, ya que también
 * hereda de `Thread`. La gran diferencia reside en su constructor.
 */
class SecondTask extends Thread {

    private static int count = 0;
    private final int id;

    @Override
    public void run() {
        for (int i = 10; i > 0; i--) {
            System.out.println(">>> Hilo " + id + ": Tick " + i);

            try {
                TimeUnit.MILLISECONDS.sleep(250);
            } catch (InterruptedException e) {
                // Si otro hilo interrumpe a este mientras está "durmiendo",
                // se lanzará esta excepción. Informamos que ha ocurrido.
                System.out.println("!!! Hilo " + id + " interrumpido.");
                // Es una buena práctica restaurar el estado de interrupción para que
                // el código que llamó a este hilo pueda saber que fue interrumpido.
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("*** Hilo " + id + " terminado ***");
    }

    /**
     * --- ¡LA MEJORA CLAVE ESTÁ AQUÍ! ---
     * <p>
     * A diferencia del ejemplo anterior, este constructor sigue el principio de
     * responsabilidad única: solo se encarga de construir e inicializar el objeto.
     * <b>NO inicia el hilo.</b>
     * <p>
     * Esto le da el control total al código que crea el objeto (el "cliente", en este
     * caso, el método `main`) para decidir cuándo es el momento adecuado para llamar
     * a `start()`. Esto evita los problemas de concurrencia que mencionamos en `FirstTechnique`.
     */
    public SecondTask() {
        this.id = ++count;
        // La peligrosa línea 'this.start();' ya no está aquí. ¡Bien!
    }
}