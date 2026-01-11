package tut1.threads.running;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * <b>TUTORIAL 1: TÉCNICA 5 - LA FORMA MODERNA Y CONCISA: EXPRESIONES LAMBDA (Java 8+)</b>
 * </p>
 *
 * Esta clase demuestra una forma muy común y compacta de definir tareas para hilos
 * usando una sintaxis introducida en Java 8: las expresiones lambda.
 * <br><br>
 * <b>Concepto Clave:</b> Una expresión lambda, en este contexto, es un atajo para crear
 * una implementación de la interfaz `Runnable` sobre la marcha.
 */
public class FifthTechnique {

    public static void main(String[] args) {
        System.out.println("==> Hilo Principal INICIADO <==");

        // --- Creación del Hilo con una Tarea Definida por Lambda ---

        // new Thread( ... ) sigue siendo la forma de crear el "cocinero".
        // La novedad es cómo le pasamos la "receta" (la tarea Runnable).

        // La expresión lambda `() -> { ... }` es, funcionalmente, lo mismo que hacer esto:
        // new Runnable() {
        //     @Override
        //     public void run() {
        //         // ... el código del bucle va aquí ...
        //     }
        // }
        //
        // Es una forma que nos ahorra escribir todo ese código repetitivo para tareas sencillas.
        Thread t = new Thread(() -> {
            // Este es el cuerpo del método run() de nuestra tarea anónima.
            for (int i = 10; i > 0; i--) {
                System.out.println(">>> Hilo anónimo: Tick " + i);

                try {
                    TimeUnit.MILLISECONDS.sleep(250);
                } catch (InterruptedException e) {
                    System.out.println("!!! Hilo anónimo interrumpido.");
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("*** Hilo anónimo terminado ***");
        });

        // El inicio del hilo es exactamente igual que en las técnicas anteriores.
        t.start();

        // El hilo principal continúa su camino.
        System.out.println("==> Hilo Principal FINALIZADO <==");
    }
}
