package tutexercises;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Solución para el Ejercicio 8.
 * Este programa demuestra cómo devolver un valor desde una tarea que se ejecuta en un hilo separado,
 * utilizando dos enfoques diferentes: la API de Threads básica y el Executor Framework.
 */
public class Exercise8 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("--- Parte (a) Usando la API de Threads ---");
        solveWithThreadsAPI();

        System.out.println("\n--- Parte (b) Usando el Executor Framework ---");
        solveWithExecutorsFramework();
    }

    /**
     * Parte (a): Demuestra cómo devolver un valor desde un hilo usando la API de Threads.
     * El método run() de Runnable no devuelve un valor, por lo que se necesita un "contenedor"
     * compartido para almacenar el resultado.
     */
    private static void solveWithThreadsAPI() throws InterruptedException {
        // Se usa AtomicReference como un contenedor seguro para hilos para el resultado.
        // Una simple variable final no funcionaría porque su contenido no podría ser modificado.
        final AtomicReference<String> resultHolder = new AtomicReference<>();

        // La tarea (Runnable) que se ejecutará en el nuevo hilo.
        Runnable task = () -> {
            String result = "Resultado de la tarea con API de Thread";
            // Se establece el resultado en el contenedor compartido.
            resultHolder.set(result);
        };

        Thread thread = new Thread(task);
        thread.start(); // Inicia el hilo.
        thread.join();  // El hilo principal espera a que el hilo de la tarea termine.

        // Una vez que el hilo ha terminado, se puede obtener el resultado del contenedor.
        System.out.println("Resultado obtenido: " + resultHolder.get());
    }

    /**
     * Parte (b): Demuestra cómo devolver un valor usando el Executor Framework.
     * Este enfoque es más moderno y preferido. Utiliza Callable y Future.
     */
    private static void solveWithExecutorsFramework() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Callable es similar a Runnable, pero su método call() puede devolver un valor.
        Callable<String> task = () -> {
            // Simplemente se devuelve el resultado.
            return "Resultado de la tarea con Executor Framework";
        };

        // Se envía la tarea Callable al executor. Devuelve un objeto Future.
        // Future es una promesa de que habrá un resultado en el futuro.
        Future<String> future = executorService.submit(task);
        
        // El método future.get() bloquea el hilo principal hasta que el resultado esté disponible.
        String result = future.get(); 
        
        System.out.println("Resultado obtenido: " + result);

        // Apaga el executor service.
        executorService.shutdown();
    }
}
