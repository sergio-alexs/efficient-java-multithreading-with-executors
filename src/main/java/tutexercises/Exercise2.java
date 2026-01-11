package tutexercises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import tuts.common.CalculationTaskC;

/**
 * Solución para el Ejercicio 2.
 * Este programa prueba el rendimiento de un ExecutorService con un pool de hilos en caché.
 * Se ejecutan 1000 tareas y se mide el tiempo total, así como el número máximo de hilos creados.
 */
public class Exercise2 {

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println(" CACHED THREAD POOL TEST");
        System.out.println("====================================");

        // Crea un ExecutorService que crea nuevos hilos según sea necesario.
        ExecutorService executorService = Executors.newCachedThreadPool();
        // Se hace un cast a ThreadPoolExecutor para poder obtener estadísticas del pool.
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
        
        long startTime = System.currentTimeMillis(); // Marca el tiempo de inicio.

        // Envía 1000 tareas de CalculationTaskC al pool de hilos.
        for (int i = 0; i < 1000; i++) {
            executorService.submit(new CalculationTaskC());
        }

        // Inicia el apagado del ExecutorService. No se aceptan nuevas tareas.
        executorService.shutdown();
        try {
            // Espera a que todas las tareas enviadas terminen su ejecución.
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis(); // Marca el tiempo de finalización.
        long timeTaken = endTime - startTime; // Calcula la duración total.
        
        // Obtiene el mayor número de hilos que existieron en el pool simultáneamente.
        int largestPoolSize = threadPoolExecutor.getLargestPoolSize();

        // Imprime las estadísticas.
        System.out.println("Max # of threads created: " + largestPoolSize);
        System.out.println("Time taken: <" + (timeTaken / 1000.0) + " s>");
        
        // Imprime el pie de página para el informe.
        System.out.println("====================================");
        System.out.println("Laptop Name/Model : <e.g. HP Probook>");
        System.out.println("Processor Make : <e.g. Intel Core i5 vPro>");
        System.out.println("# of cores : <e.g. 2>");
        System.out.println("RAM : <e.g. 8 GB>");
    }
}
