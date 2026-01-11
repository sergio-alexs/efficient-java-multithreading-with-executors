package tutexercises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Solución para el Ejercicio 6.
 * Este programa demuestra la mejor manera de nombrar hilos en un ExecutorService
 * cuando cada hilo está dedicado a una tarea específica de larga duración.
 * La solución utiliza una ThreadFactory personalizada para asignar nombres significativos.
 */
public class Exercise6 {

    public static void main(String[] args) {
        // Crea una ThreadFactory personalizada para nombrar los hilos de manera descriptiva.
        ThreadFactory namedThreadFactory = new ThreadFactory() {
            private int count = 1; // Contador para asignar nombres secuencialmente.

            @Override
            public Thread newThread(Runnable r) {
                String threadName = "";
                // Asigna un nombre basado en el orden de creación del hilo.
                switch (count) {
                    case 1:
                        threadName = "FileMonitorThread"; // Hilo para monitorear archivos.
                        break;
                    case 2:
                        threadName = "PortReaderThread";  // Hilo para leer un puerto.
                        break;
                    case 3:
                        threadName = "AnimationThread";   // Hilo para animaciones.
                        break;
                    default:
                        threadName = "WorkerThread-" + count; // Nombre por defecto.
                }
                count++;
                // Crea y devuelve el nuevo hilo con el nombre asignado.
                return new Thread(r, threadName);
            }
        };

        // Crea un ExecutorService con 3 hilos, utilizando la fábrica de hilos personalizada.
        ExecutorService executorService = Executors.newFixedThreadPool(3, namedThreadFactory);

        // Envía las tareas (aquí son placeholders/simulaciones).
        // Cada tarea será ejecutada por uno de los hilos con nombre personalizado.
        executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " está monitoreando un archivo...");
            // Aquí iría la lógica real para monitorear un archivo.
        });

        executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " está esperando entrada de un puerto...");
            // Aquí iría la lógica real para leer de un puerto.
        });

        executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " está animando un objeto...");
            // Aquí iría la lógica real para la animación.
        });
        
        // Inicia el apagado del ExecutorService.
        executorService.shutdown();
    }
}
