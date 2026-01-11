package tuts.common;

import java.util.concurrent.TimeUnit;

/**
 * Esta clase representa una tarea que se ejecuta en un bucle infinito, pero que
 * puede ser detenida de forma controlada desde el exterior.
 * Esto implementa un mecanismo de "apagado suave" (graceful shutdown).
 */
public class LoopTaskE implements Runnable {

    private static int count = 0;
    private int instanceNumber;
    private String taskId;

    // --- La bandera de apagado ---
    // 'volatile' es una palabra clave importante en concurrencia.
    // Le dice a la JVM que el valor de esta variable puede ser modificado por otros hilos.
    // Esto asegura que cuando un hilo cambia 'shutdown' a 'true', los otros hilos vean
    // ese cambio inmediatamente y no trabajen con una copia obsoleta (en caché).
    private volatile boolean shutdown = false;

    /**
     * El método run() contiene la lógica principal de la tarea.
     */
    @Override
    public void run() {
        String currentThreadName = Thread.currentThread().getName();

        System.out.println("##### [" + currentThreadName + "] <" + taskId +
                "> STARTING #####");

        // Este es un bucle infinito (la condición de parada está vacía).
        // La tarea seguirá ejecutándose una y otra vez hasta que se le indique que pare.
        for (int i = 1; ; i++) {
            System.out.println("[" + currentThreadName + "] <" + taskId +
                    "> TICK TICK - " + i);

            try {
                // Simulamos algo de trabajo.
                TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 3000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // --- Punto de control para el apagado ---
            // En cada iteración, la tarea comprueba si debe detenerse.
            // 'synchronized(this)' se usa para asegurar que la lectura de la variable 'shutdown'
            // sea atómica y consistente, especialmente si la escritura en cancel() también está sincronizada.
            // Evita condiciones de carrera.
            synchronized (this) {
                if (shutdown) {
                    // Si la bandera 'shutdown' es verdadera, salimos del bucle infinito.
                    break;
                }
            }
        }

        // Este mensaje solo se imprimirá cuando el bucle haya terminado.
        System.out.println("***** [" + currentThreadName + "] <" + taskId +
                "> COMPLETED *****");
    }

    /**
     * Este método permite que un hilo externo le pida a esta tarea que se detenga.
     */
    public void cancel() {
        System.out.println("***** [" + Thread.currentThread().getName() + "] <" + taskId + "> Shutting down *****");

        // 'synchronized(this)' asegura que el cambio de la bandera sea visible
        // de forma segura y consistente para el hilo que está ejecutando el método run().
        synchronized (this) {
            this.shutdown = true;
        }
    }

    public LoopTaskE() {
        this.instanceNumber = ++count;
        this.taskId = "LoopTaskE" + instanceNumber;
    }
}