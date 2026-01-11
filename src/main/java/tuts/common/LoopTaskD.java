package tuts.common;

import java.util.concurrent.TimeUnit;

/**
 * LoopTaskD es una tarea Runnable que introduce el concepto de hilos "demonio" (daemon) vs. "usuario" (user).
 * También permite configurar el tiempo de pausa (sleep) a través de su constructor.
 * Esto permite observar cómo se comportan los diferentes tipos de hilos.
 */
public class LoopTaskD implements Runnable {

    private static int count = 0;
    private int instanceNumber;
    private final String taskId;

    // Tiempo que el hilo "dormirá" en cada iteración del bucle, en milisegundos.
    private long sleepTime;

    /**
     * El método run() contiene la lógica principal de la tarea.
     */
    @Override
    public void run() {
        // Comprueba si el hilo que ejecuta esta tarea es un hilo demonio.
        // Los hilos demonio son hilos de baja prioridad que se ejecutan en segundo plano (p. ej., el recolector de basura).
        // La JVM no espera a que los hilos demonio terminen su ejecución para apagarse. Si todos los hilos de usuario han terminado, la JVM se cierra, terminando abruptamente cualquier hilo demonio en ejecución.
        boolean isRunningInDaemonThread = Thread.currentThread().isDaemon();
        String threadType = isRunningInDaemonThread ? "DAEMON" : "USER"; // Se usa "USER" para alinear la salida.

        String currentThreadName = Thread.currentThread().getName();

        // Imprime un mensaje de inicio, indicando el nombre del hilo, su tipo (DAEMON o USER) y el ID de la tarea.
        System.out.println("##### [" + currentThreadName + ", " + threadType + "] <" + taskId + "> STARTING #####");

        // Bucle que simula el trabajo de la tarea.
        for (int i = 10; i > 0; i--) {
            System.out.println("[" + currentThreadName + ", " + threadType + "] <" + taskId + "> TICK TICK - " + i);

            try {
                // Pausa la ejecución del hilo por el tiempo especificado en 'sleepTime'.
                TimeUnit.MILLISECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Imprime un mensaje de finalización.
        // IMPORTANTE: Si esta tarea se ejecuta en un hilo demonio, es posible que este mensaje nunca se imprima
        // si el hilo principal (que es un hilo de usuario) termina antes.
        System.out.println("***** [" + currentThreadName + ", " + threadType + "] <" + taskId + "> COMPLETED *****");
    }

    /**
     * Constructor para LoopTaskD.
     *
     * @param sleepTime El tiempo en milisegundos que el hilo debe pausar en cada iteración.
     */
    public LoopTaskD(long sleepTime) {
        this.sleepTime = sleepTime;

        this.instanceNumber = ++count;
        this.taskId = "LoopTaskD" + instanceNumber;
    }
}