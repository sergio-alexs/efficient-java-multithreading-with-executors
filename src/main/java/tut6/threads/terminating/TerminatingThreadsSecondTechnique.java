package tut6.threads.terminating;

import java.util.concurrent.TimeUnit;

import tuts.common.LoopTaskF;

/**
 * <p>
 * <b>TUTORIAL 6: TERMINAR HILOS - TÉCNICA 2 (EL MECANISMO DE INTERRUPCIÓN DE JAVA)</b>
 * </p>
 * <p>
 * Esta clase demuestra la forma idiomática y recomendada de terminar hilos en Java.
 * <br><br>
 * <b>Mecanismo de Interrupción Cooperativa:</b>
 * <ol>
 *     <li><b>La señal:</b> El hilo principal llama a `t.interrupt()`. Esto NO detiene el hilo.
 *         Simplemente establece una bandera booleana interna en el hilo `t`, llamada "estado de interrupción".</li>
 *     <li><b>La cooperación:</b> El código dentro del método `run()` de la tarea debe comprobar
 *         periódicamente esta bandera. Si está levantada, debe detener lo que está haciendo y terminar limpiamente.</li>
 * </ol>
 */
public class TerminatingThreadsSecondTechnique {

    public static void main(String[] args) throws InterruptedException {
        String currentThreadName = Thread.currentThread().getName();
        System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");

        Thread t1 = new Thread(new LoopTaskF(), "MyThread-1");
        t1.start();

        Thread t2 = new Thread(new LoopTaskF(), "MyThread-2");
        t2.start();

        Thread t3 = new Thread(new LoopTaskF(), "MyThread-3");
        t3.start();

        // Dejamos que los hilos se ejecuten durante 3 segundos.
        TimeUnit.MILLISECONDS.sleep(3000);

        // Enviamos la "señal de interrupción" a cada hilo.
        System.out.println(">>> [" + currentThreadName + "] Interrumpiendo hilos...");
        t1.interrupt();
        t2.interrupt();
        t3.interrupt();

        // --- ¿CÓMO LA TAREA DETECTA LA SEÑAL? ---
        // El código en `LoopTaskF` usa `Thread.interrupted()` para comprobar la bandera.
        //
        // Hay dos formas de comprobarla:
        // 1. `Thread.interrupted()`: Es un método ESTÁTICO. Comprueba el estado del hilo
        //    actual Y LIMPIA la bandera (la pone de nuevo a `false`). Es como leer y
        //    borrar una notificación. Se usa cuando quieres manejar la interrupción una sola vez.
        //
        // 2. `t.isInterrupted()`: Es un método DE INSTANCIA. Comprueba el estado del hilo `t`
        //    y NO LIMPIA la bandera. Es como mirar la notificación sin borrarla.
        //
        // NOTA: Este ejemplo NO se enfrenta al `InterruptedException` porque la tarea
        // `LoopTaskF` realiza cálculos y no llama a métodos bloqueantes como `sleep()` o `wait()`.
        // El siguiente ejemplo cubrirá ese importante caso.

        System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
    }
}
