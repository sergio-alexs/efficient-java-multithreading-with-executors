package tutexercises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import tuts.common.CalculationTaskC;

/**
 * Solución para el Ejercicio 1.
 * Este programa prueba el rendimiento de un ExecutorService con un pool de hilos de tamaño fijo.
 * Se ejecutan 1000 tareas y se mide el tiempo total variando el tamaño del pool de hilos.
 */
public class Exercise1 {

    public static void main(String[] args) {
        // Tamaños de pool de hilos que se probarán.
        int[] poolSizes = {3, 6, 9, 12, 15, 18, 21, 24, 27, 30}; 
        
        // Imprime la cabecera de la tabla de resultados.
        System.out.println("====================================");
        System.out.println(" # OF THREADS TIME TAKEN");
        System.out.println("====================================");

        // Itera sobre cada tamaño de pool, ejecuta la prueba y muestra el resultado.
        for (int poolSize : poolSizes) {
            long timeTaken = runTest(poolSize);
            // Imprime el resultado en el formato "tamaño <tiempo s>".
            System.out.println(poolSize + " <" + (timeTaken / 1000.0) + " s>");
        }
        
        // Imprime el pie de página para el informe.
        System.out.println("====================================");
        // Se debe rellenar manualmente la información del sistema.
        System.out.println("Laptop Name/Model : <e.g. HP Probook>");
        System.out.println("Processor Make : <e.g. Intel Core i5 vPro>");
        System.out.println("# of cores : <e.g. 2>");
        System.out.println("RAM : <e.g. 8 GB>");
    }

    /**
     * Ejecuta la prueba para un tamaño de pool de hilos dado.
     * @param poolSize El número de hilos en el pool.
     * @return El tiempo total de ejecución en milisegundos.
     */
    private static long runTest(int poolSize) {
        // Crea un ExecutorService con un número fijo de hilos.
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
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
        return endTime - startTime; // Devuelve la duración total.
    }
}
