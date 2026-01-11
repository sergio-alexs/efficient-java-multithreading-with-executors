package tutexercises;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Solución para el Ejercicio 7.
 * Este archivo contiene una implementación simplificada de un ExecutorService personalizado.
 */

/**
 * Excepción personalizada que se lanza cuando se intenta enviar una tarea a un
 * MyExecutorService que ya ha sido apagado.
 */
class ServiceClosedException extends RuntimeException {
    public ServiceClosedException(String message) {
        super(message);
    }
}

/**
 * Una implementación básica de un servicio de ejecución que gestiona un pool de hilos
 * y una cola de tareas.
 */
class MyExecutorService {
    private final int poolSize;
    private final WorkerThread[] threads;
    private final Queue<Runnable> taskQueue;
    private volatile boolean isShutdown = false;

    /**
     * Constructor que inicializa el pool de hilos.
     * @param poolSize El número de hilos en el pool.
     */
    public MyExecutorService(int poolSize) {
        this.poolSize = poolSize;
        this.threads = new WorkerThread[poolSize];
        this.taskQueue = new LinkedList<>();

        // Crea e inicia los hilos de trabajo.
        for (int i = 0; i < poolSize; i++) {
            threads[i] = new WorkerThread();
            threads[i].start();
        }
    }

    /**
     * Envía una tarea para su ejecución.
     * @param task La tarea (Runnable) a ejecutar.
     * @throws ServiceClosedException si el servicio ha sido apagado.
     */
    public void submit(Runnable task) {
        synchronized (taskQueue) {
            if (isShutdown) {
                throw new ServiceClosedException("El servicio está apagado. No se aceptan nuevas tareas.");
            }
            taskQueue.add(task);
            taskQueue.notify(); // Notifica a un hilo en espera que hay una nueva tarea.
        }
    }

    /**
     * Inicia el apagado del servicio. Las tareas en cola se completarán,
     * pero no se aceptarán nuevas tareas.
     */
    public void shutdown() {
        synchronized (taskQueue) {
            isShutdown = true;
            taskQueue.notifyAll(); // Despierta a todos los hilos para que verifiquen el estado de apagado.
        }
    }

    /**
     * Hilo de trabajo que toma tareas de la cola y las ejecuta.
     */
    private class WorkerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                Runnable task;
                synchronized (taskQueue) {
                    // Espera mientras la cola esté vacía y el servicio no esté apagado.
                    while (taskQueue.isEmpty() && !isShutdown) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            // Manejar la interrupción si es necesario.
                        }
                    }

                    // Si la cola está vacía y el servicio está apagado, el hilo termina.
                    if (taskQueue.isEmpty() && isShutdown) {
                        break;
                    }

                    task = taskQueue.poll(); // Obtiene la siguiente tarea de la cola.
                }

                if (task != null) {
                    try {
                        task.run();
                    } catch (RuntimeException e) {
                        // Captura excepciones en tiempo de ejecución de la tarea para que el hilo no muera.
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

/**
 * Clase principal para demostrar el uso de MyExecutorService.
 */
public class Exercise7 {
    public static void main(String[] args) {
        // Crea un servicio con 3 hilos.
        MyExecutorService service = new MyExecutorService(3);

        // Envía 10 tareas al servicio.
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            service.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " ejecutando tarea " + taskId);
                try {
                    Thread.sleep(500); // Simula trabajo.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        System.out.println("Apagando el servicio...");
        service.shutdown();
        
        // Intenta enviar una tarea después de apagar. Debería fallar.
        try {
             service.submit(() -> System.out.println("Esto debería fallar"));
        } catch (ServiceClosedException e) {
            System.out.println("Se capturó la excepción esperada: " + e.getMessage());
        }
    }
}
