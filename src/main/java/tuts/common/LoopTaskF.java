package tuts.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Esta clase demuestra el mecanismo de interrupción estándar de Java para detener un hilo.
 * La interrupción es una forma de "pedirle educadamente" a un hilo que se detenga.
 * El hilo no se detiene a la fuerza; debe comprobar si ha recibido esta petición y actuar en consecuencia.
 * Este es un mecanismo de detención "cooperativo".
 */
public class LoopTaskF implements Runnable {

    private static int count = 0;
    private int instanceNumber;
    private final String taskId;

    private final int DATASET_SIZE = 100000;

    @Override
    public void run() {
        String currentThreadName = Thread.currentThread().getName();

        System.out.println("##### [" + currentThreadName + "] <" + taskId + "> STARTING #####");

        // Bucle infinito que se detendrá cuando el hilo sea interrumpido.
        for (int i = 1; ; i++) {
            System.out.println("[" + currentThreadName + "] <" + taskId + "> TICK TICK - " + i);

            // Simula un trabajo de cálculo intensivo.
            doComplexCalculation();

            // --- ¡AQUÍ ESTÁ LA CLAVE DEL MECANISMO DE INTERRUPCIÓN! ---
            // Thread.interrupted() es un método estático que hace DOS cosas:
            // 1. Comprueba si el hilo actual ha sido interrumpido (si alguien ha llamado a .interrupt() en él).
            // 2. ¡MUY IMPORTANTE! Limpia la bandera de interrupción, poniéndola de nuevo en 'false'.
            //
            // Por lo tanto, esta es una comprobación de "un solo uso".
            if (Thread.interrupted()) {
                System.out.println("[" + currentThreadName + "] <" + taskId + "> Interrupted. Cancelling ...");
                // Si se ha solicitado la interrupción, salimos del bucle para terminar la tarea.
                break;
            }
        }

        // Esta línea demostrará que la bandera de interrupción fue limpiada por la llamada anterior.
        // Siempre imprimirá 'false', porque la condición del 'if' ya consumió el estado 'true'.
        System.out.println("[" + currentThreadName + "] <" + taskId + "> Retrieving 'INTERRUPTED' status again : " +
                Thread.interrupted());

        System.out.println("***** [" + currentThreadName + "] <" + taskId + "> COMPLETED *****");
    }

    /**
     * Simula un trabajo que consume tiempo y CPU, como ordenar grandes conjuntos de datos.
     * Es importante notar que este código no lanza InterruptedException, por lo que
     * dependemos de la comprobación manual con Thread.interrupted() en el bucle principal.
     */
    private void doComplexCalculation() {
        for (int i = 0; i < 2; i++) {
            Collections.sort(generateDataSet());
        }
    }

    /**
     * Genera una lista de números aleatorios para simular un conjunto de datos.
     */
    private List<Integer> generateDataSet() {
        List<Integer> intList = new ArrayList<>();
        Random randomGenerator = new Random();

        for (int i = 0; i < DATASET_SIZE; i++) {
            intList.add(randomGenerator.nextInt(DATASET_SIZE));
        }

        return intList;
    }

    public LoopTaskF() {
        this.instanceNumber = ++count;
        this.taskId = "LoopTaskF" + instanceNumber;
    }
}