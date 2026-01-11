package tut6.threads.terminating;

import java.util.concurrent.TimeUnit;

import tuts.common.LoopTaskG;
import tuts.common.LoopTaskH;

/**
 * <p>
 * <b>TUTORIAL 6: TERMINAR HILOS - TÉCNICA 3 (INTERRUMPIR HILOS BLOQUEADOS)</b>
 * </p>
 * <p>
 * Esta clase demuestra qué sucede cuando se interrumpe un hilo que está "bloqueado",
 * es decir, pausado en un método como `Thread.sleep()`, `wait()` o `join()`.
 * <br><br>
 * <b>Concepto Clave: `InterruptedException`</b>
 * <ul>
 *     <li>Cuando se llama a `t.interrupt()` sobre un hilo `t` que está bloqueado,
 *         el método bloqueante termina inmediatamente y lanza una `InterruptedException`.</li>
 *     <li><b>¡MUY IMPORTANTE!</b> Al lanzar esta excepción, el estado de interrupción
 *         del hilo ("interruption flag") es <u>limpiado</u> (se pone a `false`).</li>
 * </ul>
 */
public class TerminatingBlockedThreads {

    public static void main(String[] args) throws InterruptedException {
        String currentThreadName = Thread.currentThread().getName();
        System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");

        // `LoopTaskG` muestra un manejo simple de la interrupción
        Thread t1 = new Thread(new LoopTaskG(), "MyThread-1");
        t1.start();

        // `LoopTaskH` muestra un manejo más complejo (y mejorable)
        Thread t2 = new Thread(new LoopTaskH(), "MyThread-2");
        t2.start();

        TimeUnit.MILLISECONDS.sleep(3000);

        System.out.println(">>> [" + currentThreadName + "] Interrumpiendo hilos...");
        t1.interrupt();
        t2.interrupt();

        // --- ANÁLISIS DE LAS TAREAS ---
        //
        // ** LoopTaskG: Patrón simple y directo **
        // El bloque `try-catch` para `InterruptedException` está dentro del bucle.
        // Al recibir la excepción, simplemente ejecuta `break` para salir del bucle y terminar.
        // Esto es efectivo para tareas donde `sleep` es la única operación principal.
        //
        // ** LoopTaskH: Patrón complejo y una lección importante **
        // `LoopTaskH` intenta manejar la interrupción tanto durante `sleep` como durante
        // el "trabajo" posterior. Usa una bandera booleana `sleepInterrupted` para
        // recordar si la interrupción ocurrió en el `catch`.
        //
        // **UNA MEJOR FORMA para LoopTaskH:**
        // En lugar de usar una bandera booleana, la práctica estándar es "reafirmar" la interrupción
        // dentro del bloque `catch` para que el código posterior pueda saber de ella:
        //
        // catch (InterruptedException e) {
        //     System.out.println("... Sleep Interrupted, RESTORING interrupt status ...");
        //     Thread.currentThread().interrupt(); // ¡RESTAURA LA BANDERA DE INTERRUPCIÓN!
        // }
        //
        // Si se hiciera esto, el `if (Thread.interrupted())` posterior funcionaría para
        // ambos escenarios sin necesidad de la bandera `sleepInterrupted`, haciendo el
        // código más limpio y robusto.

        System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
    }
}
