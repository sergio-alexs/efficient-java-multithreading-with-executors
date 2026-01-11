package tuts.common;

import java.util.concurrent.TimeUnit;

/**
 * Esta clase es un ejemplo avanzado que muestra cómo una tarea en un hilo (el "Productor")
 * puede calcular un valor y entregárselo a otro hilo (el "Consumidor") que lo está esperando.
 * <p>
 * Para lograr esta comunicación, se utiliza un mecanismo de bajo nivel de Java: `wait()`, `notify()` y
 * bloques `synchronized`. Este es el fundamento sobre el que se construyen utilidades más avanzadas.
 * <p>
 * Patrón Productor-Consumidor:
 * - El hilo que ejecuta `run()` es el PRODUCTOR: calcula la `sum`.
 * - El hilo que llama a `getSum()` es el CONSUMIDOR: espera y consume la `sum`.
 */
public class ValueReturningTaskA implements Runnable {

    private int a;
    private int b;
    private long sleepTime;
    private int sum;

    private static int count = 0;
    private int instanceNumber;
    private String taskId;

    // La palabra clave 'volatile' es crucial aquí. Asegura que cualquier cambio en la variable `done`
    // (escrito por el hilo productor) sea visible inmediatamente para el hilo consumidor. Sin `volatile`,
    // el consumidor podría no ver que `done` ha cambiado a `true` y esperar para siempre.
    private volatile boolean done = false;

    public ValueReturningTaskA(int a, int b, long sleepTime) {
        this.a = a;
        this.b = b;
        this.sleepTime = sleepTime;

        this.instanceNumber = ++count;
        this.taskId = "ValueReturningTaskA-" + instanceNumber;
    }

    // --- LÓGICA DEL PRODUCTOR ---

    /**
     * El método `run()` es ejecutado por el hilo "productor".
     * Su trabajo es realizar el cálculo y notificar al consumidor que el resultado está listo.
     */
    @Override
    public void run() {
        String currentThreadName = Thread.currentThread().getName();
        System.out.println("##### [" + currentThreadName + "] <" + taskId + "> [PRODUCTOR] STARTING #####");

        try {
            TimeUnit.MILLISECONDS.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // El productor realiza el cálculo.
        sum = a + b;

        System.out.println("***** [" + currentThreadName + "] <" + taskId + "> [PRODUCTOR] CALCULATION COMPLETED *****");

        // --- La Notificación ---
        // Para llamar a notify(), el hilo DEBE ser el dueño del "monitor" (o "lock") del objeto.
        // `synchronized(this)` es la forma de obtener ese monitor. Es como entrar en una habitación
        // privada donde solo puede haber un hilo a la vez para este objeto (`this`).
        synchronized (this) {
            // El productor marca el trabajo como terminado.
            done = true;

            System.out.println("[" + currentThreadName + "] <" + taskId + "> [PRODUCTOR] NOTIFYING ...");
            // notify() despierta a UN SOLO hilo que esté esperando en `wait()` sobre este mismo objeto.
            // Si hubiera varios hilos esperando, solo uno es elegido. Si no hay ninguno, no pasa nada.
            this.notify();
        }
    }

    // --- LÓGICA DEL CONSUMIDOR ---

    /**
     * Este método es llamado por el hilo "consumidor" para esperar y obtener el resultado.
     *
     * @return La suma calculada por el productor.
     */
    public int getSum() {
        String currentThreadName = Thread.currentThread().getName();

        // El consumidor también debe obtener el monitor del objeto para poder llamar a wait().
        synchronized (this) {
            // IMPORTANTE: La comprobación del estado (`done`) DEBE hacerse en un bucle `while`.
            // ¿Por qué? Por el fenómeno de "despertares espurios" (spurious wakeups), donde un hilo
            // puede despertarse de `wait()` sin haber sido notificado. Si eso pasa, el bucle
            // hace que vuelva a comprobar la condición (`!done`) y se vuelva a dormir si el trabajo
            // aún no ha terminado.
            // (Esto `while` es la forma correcta y robusta de hacerlo).
            while (!done) {
                try {
                    System.out.println("[" + currentThreadName + "] <" + taskId + "> [CONSUMIDOR] WAITING for result...");

                    // --- La Espera ---
                    // `wait()` hace tres cosas en un solo paso atómico:
                    // 1. Pone al hilo consumidor a "dormir".
                    // 2. LIBERA el monitor (el lock) del objeto `this`.
                    // 3. Esto es VITAL: al liberar el lock, permite que el hilo productor
                    //    pueda entrar en su propio bloque `synchronized` para hacer el `notify()`.
                    // El hilo consumidor se quedará aquí hasta que sea "notificado" por el productor.
                    this.wait();

                    System.out.println("[" + currentThreadName + "] <" + taskId + "> [CONSUMIDOR] WOKEN-UP!");

                } catch (InterruptedException e) {
                    // Si el consumidor es interrumpido mientras espera, se lanza esta excepción.
                    e.printStackTrace();
                }
            }
        }

        // Una vez que el consumidor es notificado y la condición `done` es `true`,
        // sale del bucle y del bloque synchronized. Ahora puede devolver de forma segura
        // el resultado, que ya ha sido calculado por el productor.
        return sum;
    }

}
