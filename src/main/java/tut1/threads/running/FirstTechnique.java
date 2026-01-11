package tut1.threads.running;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * <b>TUTORIAL 1: TÉCNICA 1 - EXTENDER LA CLASE THREAD</b>
 * </p>
 * <p>
 * Esta es la primera y más básica forma de crear un hilo en Java.
 * La clase `FirstTask` define el trabajo a realizar y también ES un hilo,
 * porque hereda directamente de la clase `Thread`.
 * <br><br>
 * El programa principal (`FirstTechnique`) simplemente crea e inicia estas tareas.
 */
public class FirstTechnique {

    public static void main(String[] args) {
        // El programa siempre empieza su ejecución en el "hilo principal" o "main thread".
        System.out.println("==> Hilo Principal INICIADO <==");

        // --- Creación e inicio de hilos ---
        // Al hacer 'new FirstTask()', estamos creando una nueva instancia de nuestra tarea.
        // En este ejemplo particular, el propio constructor de 'FirstTask' inicia el hilo.
        new FirstTask();
        new FirstTask();

        // Aunque guardamos la referencia en la variable 't', no la usamos después.
        // El hilo ya está iniciado y funcionando en segundo plano.
        Thread t = new FirstTask();

        // El hilo principal no espera a que los otros hilos terminen.
        // Simplemente los pone en marcha y continúa con su propio trabajo.
        // Por eso verás este mensaje final casi inmediatamente.
        System.out.println("==> Hilo Principal FINALIZADO <==");
    }
}


/**
 * <h2>FirstTask - Una tarea que ES un Hilo</h2>
 * <p>
 * Esta clase representa una unidad de trabajo que se ejecutará en paralelo.
 * <br>
 * Al hacer `extends Thread`, convertimos esta clase en un tipo de `Thread`.
 * Esto significa que cada objeto `FirstTask` que creemos será un hilo completo,
 * con toda la maquinaria necesaria para ejecutarse en segundo plano.
 */
class FirstTask extends Thread {

    // Una variable 'static' pertenece a la CLASE, no a un objeto individual.
    // Esto significa que 'count' es compartida por TODAS las instancias de FirstTask.
    // La usamos para dar un ID único a cada nuevo hilo que creamos.
    private static int count = 0;
    private final int id;

    /**
     * El método `run()` es el corazón del hilo. Es obligatorio implementarlo.
     * Contiene el código que se ejecutará en un nuevo hilo de ejecución
     * cuando llamemos al método `start()`.
     */
    @Override
    public void run() {
        for (int i = 10; i > 0; i--) {
            System.out.println(">>> Hilo " + id + ": Tick " + i);

            try {
                // Pausa la ejecución de ESTE hilo durante el tiempo especificado.
                // Es una forma de simular una tarea que toma tiempo (ej. una descarga, un cálculo largo).
                // La llamada a sleep() puede lanzar una 'InterruptedException', que es una forma
                // en que otro hilo puede "despertar" a este. Debemos manejarla obligatoriamente.
                TimeUnit.MILLISECONDS.sleep(250);
            } catch (InterruptedException e) {
                // Si otro hilo interrumpe a este, se entrará en este bloque.
                System.out.println("!!! Hilo " + id + " interrumpido.");
                // Es una buena práctica restaurar el estado de interrupción.
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("*** Hilo " + id + " terminado ***");
    }

    /**
     * Constructor de la clase. Se llama automáticamente cada vez que hacemos `new FirstTask()`.
     */
    public FirstTask() {
        // Asignamos un ID único a esta instancia del hilo.
        this.id = ++count;

        // --- ¡ADVERTENCIA! ---
        // Iniciar un hilo dentro de su propio constructor es generalmente una MALA PRÁCTICA.
        // ¿Por qué? Porque permite que el hilo empiece a ejecutarse ANTES de que el objeto
        // esté completamente construido. Si otra clase hereda de FirstTask y añade más
        // inicializaciones en su constructor, este hilo podría intentar usar esas variables
        // antes de que tengan un valor, causando errores difíciles de depurar (NullPointerException).
        //
        // Se muestra aquí con fines educativos, pero la forma correcta es:
        // FirstTask task = new FirstTask();
        // task.start();
        this.start();
    }
}