package tut2.threads.naming;

import java.util.concurrent.TimeUnit;

import tuts.common.LoopTaskC;

/**
 * <p>
 * <b>TUTORIAL 2: TÉCNICA 2 - NOMBRAR HILOS (ENFOQUE RECOMENDADO)</b>
 * </p>
 * <p>
 * Esta clase demuestra la forma correcta y limpia de nombrar hilos.
 * <br><br>
 * <b>Principio Clave:</b> El código que crea y gestiona el hilo (el "cliente",
 * en este caso el método `main`) es el responsable de darle un nombre. La tarea
 * (`LoopTaskC`) no sabe ni le importa cómo se llama el hilo que la ejecuta.
 * Esto es una correcta <b>separación de responsabilidades</b>.
 */
public class NamingThreadsSecondTechnique {

    public static void main(String[] args) {
        String currentThreadName = Thread.currentThread().getName();
        System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");

        // Usamos LoopTaskC, una tarea "limpia" que no intenta nombrar al hilo por sí misma.
        // (Puedes inspeccionar 'tuts/common/LoopTaskC.java' para comprobarlo).

        // --- MÉTODO 1: Nombrar el hilo en el constructor (el más común) ---
        // El constructor de `Thread` está sobrecargado: una de sus versiones acepta
        // la tarea (`Runnable`) y un `String` para el nombre del hilo.
        Thread t1 = new Thread(new LoopTaskC(), "MiHilo-1");
        t1.start();


        // --- MÉTODO 2: Nombrar el hilo después de crearlo usando setName() ---
        // 1. Creamos un hilo de la forma habitual. Java le asignará un nombre por defecto (p.ej., "Thread-0").
        Thread t2 = new Thread(new LoopTaskC());

        // 2. Iniciamos el hilo. Comienza a ejecutarse con su nombre por defecto.
        t2.start();

        // --- Bloque solo para fines demostrativos ---
        try {
            // Pausamos el hilo principal. El único propósito de esto es dar tiempo
            // a que el hilo `t2` se ejecute un poco y muestre algunos mensajes
            // con su nombre original ("Thread-0") antes de que se lo cambiemos.
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // --- Fin del bloque de demostración ---

        // 3. Cambiamos el nombre de un hilo que ya está en ejecución.
        System.out.println(">>> [" + currentThreadName + "] Renombrando hilo 't2' a 'MiHilo-2'...");
        t2.setName("MiHilo-2");


        System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
    }
}
