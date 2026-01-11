package tut6.threads.terminating;

import java.util.concurrent.TimeUnit;

import tuts.common.LoopTaskE;

/**
 * <p>
 * <b>TUTORIAL 6: TERMINAR HILOS - TÉCNICA 1 (BANDERA VOLÁTIL MANUAL)</b>
 * </p>
 * <p>
 * Esta clase demuestra una forma manual de terminar un hilo usando una bandera
 * booleana compartida.
 * <br><br>
 * <b>Mecanismo:</b>
 * <ul>
 *     <li>La tarea (`LoopTaskE`) tiene una variable `volatile boolean shutdown`.</li>
 *     <li>El hilo principal, desde fuera, llama a un método `cancel()` que pone esa bandera a `true`.</li>
 *     <li>El bucle interno de la tarea comprueba la bandera en cada iteración y, si es `true`, sale del bucle.</li>
 * </ul>
 * Esto es un ejemplo de <b>interrupción cooperativa</b>: el hilo debe colaborar para ser terminado.
 */
public class TerminatingThreadsFirstTechnique {

    public static void main(String[] args) throws InterruptedException {
        String currentThreadName = Thread.currentThread().getName();
        System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");

        LoopTaskE task1 = new LoopTaskE();
        LoopTaskE task2 = new LoopTaskE();
        LoopTaskE task3 = new LoopTaskE();

        new Thread(task1, "MyThread-1").start();
        new Thread(task2, "MyThread-2").start();
        new Thread(task3, "MyThread-3").start();

        // Dejamos que los hilos se ejecuten durante 5 segundos.
        TimeUnit.MILLISECONDS.sleep(5000);

        // "Solicitamos" a las tareas que se detengan.
        System.out.println(">>> [" + currentThreadName + "] Solicitando terminación de tareas...");
        task1.cancel();
        task2.cancel();
        task3.cancel();

        // --- DESVENTAJAS DE ESTE ENFOQUE ---
        // 1. **No es estándar:** Es un mecanismo "casero". El framework de Java y las
        //    librerías de concurrencia están construidos alrededor del mecanismo de
        //    interrupción nativo (`thread.interrupt()`), no de banderas personalizadas.
        //
        // 2. **NO INTERRUMPE HILOS BLOQUEADOS:** Si una tarea está "dormida" en `sleep()` o
        //    `wait()`, no puede comprobar la bandera. Llamar a `cancel()` no la despertará.
        //    La tarea solo se detendrá cuando se despierte y vuelva a comprobar la bandera.
        //    Esto es un problema grave que el mecanismo de interrupción de Java sí resuelve.

        System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
    }
}
