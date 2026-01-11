package tuts.common;

import java.util.concurrent.TimeUnit;

/**
 * LoopTaskC es una tarea que implementa la interfaz Runnable, diseñada para ser ejecutada en un hilo.
 * Es similar a LoopTaskA y LoopTaskB, pero en este caso, no modifica el nombre del hilo.
 * Simplemente utiliza el nombre que el ExecutorService le asigna al hilo que la ejecuta.
 */
public class LoopTaskC implements Runnable {

    // Contador estático para asignar un número de instancia único a cada tarea.
    private static int count = 0;
    // El número de instancia de este objeto de tarea específico.
    private int instanceNumber;
    // Un identificador de tarea único, como "LoopTaskC1", "LoopTaskC2", etc.
    private final String taskId;

    /**
     * El método run() contiene la lógica que se ejecutará cuando la tarea sea iniciada por un hilo.
     */
    @Override
    public void run() {
        // Imprime un mensaje de inicio, mostrando el nombre del hilo actual y el ID de la tarea.
        // El nombre del hilo es el que le asigna el ExecutorService (p. ej., "pool-1-thread-1").
        System.out.println("##### [" + Thread.currentThread().getName() +
                "] <" + taskId + "> STARTING #####");

        // Este bucle simula el trabajo de la tarea.
        for (int i = 10; i > 0; i--) {
            // Imprime el progreso de la tarea, mostrando el nombre del hilo para ver cuál está trabajando.
            System.out.println("[" + Thread.currentThread().getName() +
                    "] <" + taskId + "> TICK TICK - " + i);

            try {
                // Pone el hilo a dormir por un tiempo aleatorio (hasta 499 milisegundos).
                // Esto simula que la tarea está realizando alguna operación que consume tiempo.
                TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 500));
            } catch (InterruptedException e) {
                // Se captura si el hilo es interrumpido mientras está en sleep.
                e.printStackTrace();
            }
        }

        // Imprime un mensaje indicando que la tarea ha finalizado.
        System.out.println("***** [" + Thread.currentThread().getName() +
                "] <" + taskId + "> COMPLETED *****");
    }

    /**
     * Constructor para LoopTaskC.
     * Incrementa el contador global y asigna un número de instancia y un ID de tarea únicos.
     */
    public LoopTaskC() {
        this.instanceNumber = ++count;
        this.taskId = "LoopTaskC" + instanceNumber;
    }
}