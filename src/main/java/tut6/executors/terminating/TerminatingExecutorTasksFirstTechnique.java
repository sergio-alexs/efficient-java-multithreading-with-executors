package tut6.executors.terminating;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import tuts.common.FactorialTaskA;
import tuts.common.LoopTaskE;
import tuts.common.NamedThreadsFactory;

/**
 * <p>
 * <b>TUTORIAL 6: TERMINAR TAREAS DE EXECUTOR - TÉCNICA 1 (MANUAL, NO RECOMENDADA)</b>
 * </p>
 *
 * Esta clase demuestra cómo se pueden terminar tareas en un `ExecutorService`
 * utilizando un mecanismo de cancelación <b>manual</b>, basado en banderas volátiles.
 * <br><br>
 * Este enfoque <u>ignora</u> las herramientas de cancelación que el propio `ExecutorService`
 * proporciona a través del objeto `Future` y, por lo tanto, <b>no es la forma recomendada</b>.
 */
public class TerminatingExecutorTasksFirstTechnique {

	public static void main(String[] args) throws InterruptedException {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("==> [" + currentThreadName + "] Hilo Principal INICIADO <==");
		
		ExecutorService execService = Executors.newCachedThreadPool(new NamedThreadsFactory());
		
		// Creamos instancias de nuestras tareas.
		LoopTaskE task1 = new LoopTaskE();
		FactorialTaskA task2 = new FactorialTaskA(30, 1000);
		
		// Las enviamos al executor.
		execService.execute(task1);
		execService.submit(task2); // submit() devuelve un Future, pero aquí lo estamos ignorando.
		
		execService.shutdown();
		
		TimeUnit.MILLISECONDS.sleep(3000);
		
		// --- EL PROBLEMA DE ESTE ENFOQUE ---
		// Para cancelar las tareas, necesitamos llamar a un método `cancel()` personalizado
		// directamente sobre los objetos de las tareas.
		task1.cancel();
		task2.cancel();
		
		// Esto tiene una gran desventaja: **acoplamiento fuerte**. El código que somete
		// la tarea necesita conocer la implementación específica de la tarea y tener una
		// referencia a ella. Esto no funciona bien con lambdas o clases anónimas,
		// y rompe la abstracción que el `ExecutorService` intenta proporcionar.
		//
		// La forma correcta es usar el "contrato" que nos da el executor: el objeto `Future`.
		// Veremos eso en la siguiente técnica.
		
		System.out.println("==> [" + currentThreadName + "] Hilo Principal FINALIZADO <==");
	}
}
