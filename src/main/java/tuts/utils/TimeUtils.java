package tuts.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Una 'clase de utilidad' (utility class) que agrupa métodos estáticos relacionados con el tiempo.
 * Las clases de utilidad no están pensadas para ser instanciadas (es decir, no se crea un `new TimeUtils()`).
 * Su único propósito es servir como un 'contenedor' para funciones que pueden ser llamadas desde cualquier
 * parte del código.
 * 
 * @author Arun Mehra
 */
public class TimeUtils {

	/**
	 * El constructor se declara como 'private' para prevenir que se creen instancias de esta clase.
	 * Si intentaras hacer `new TimeUtils()` desde otra clase, el código no compilaría.
	 * Esto refuerza el patrón de 'clase de utilidad', que solo contiene métodos estáticos.
	 */
	private TimeUtils(){
		// Este constructor está vacío a propósito. Su única función es ser privado.
	}
	
	/**
	 * Calcula una fecha futura sumando una cantidad de milisegundos a la fecha actual.
	 * 
	 * La palabra clave 'static' significa que este método pertenece a la clase `TimeUtils` en sí,
	 * no a un objeto particular de `TimeUtils`. Por eso se puede invocar directamente usando el nombre
	 * de la clase: `TimeUtils.getFutureTime(...)`.
	 * 
	 * @param initialTime La fecha y hora de inicio desde la cual se calculará el futuro.
	 * @param millisToAdd La cantidad de milisegundos que se van a sumar.
	 * @return Un nuevo objeto `Date` que representa la fecha y hora futuras.
	 */
	public static Date getFutureTime(Date initialTime, long millisToAdd) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(initialTime.getTime() + millisToAdd);
		
		return cal.getTime();
	}

	/**
	 * Convierte una duración (dada en segundos y milisegundos) a una cadena de texto formateada.
	 * Por ejemplo, si segundos = 5 y millis = 300, el resultado será "5.300 SECONDS".
	 * 
	 * @param seconds La parte de los segundos de la duración.
	 * @param millis La parte de los milisegundos de la duración.
	 * @return Una cadena de texto (`String`) que representa la duración en segundos fraccionarios.
	 */	public static String convertToFractionalSecondsStr(long seconds, long millis) {
		// Crea una instancia de la clase interna 'SecondsAndMillis' y llama a su método toString()
		// para obtener la representación en texto.
		return new SecondsAndMillis(seconds, millis).toString();
	}
	
	/**
	 * Calcula la diferencia de tiempo entre dos instantes (dos objetos `Date`).
	 * 
	 * @param start La fecha y hora de inicio del intervalo.
	 * @param end La fecha y hora de fin del intervalo.
	 * @return Un objeto `SecondsAndMillis` que encapsula la duración transcurrida.
	 *         Si la fecha 'end' es anterior a 'start', la duración será negativa.
	 */	public static SecondsAndMillis getTimeDifferenceInSeconds(Date start, Date end) {
		// La resta de `getTime()` de dos fechas da como resultado la diferencia en milisegundos.
		long diffInMillis = end.getTime() - start.getTime();
		return new SecondsAndMillis(diffInMillis);
	}
	
	
	/**
	 * Esta es una 'clase interna estática' (static nested class). Es una clase definida dentro de otra.
	 * Se usa aquí por conveniencia y para mantener el código organizado, ya que `SecondsAndMillis`
	 * está fuertemente acoplada a `TimeUtils`.
	 * 
	 * Su propósito es simple: servir como un 'contenedor de datos' para representar una duración
	 * de tiempo de una manera más estructurada que simplemente un número de milisegundos.
	 * 
	 * @author Arun Mehra
	 */	public static class SecondsAndMillis {
		
		// La palabra clave 'final' significa que una vez que se asigna un valor a estas variables,
		// no puede ser cambiado. Esto hace que los objetos de esta clase sean 'inmutables'.
		public final String sign;    // Signo: "-" si es negativo, "" si es positivo.
		public final long seconds;   // Parte entera de los segundos.
		public final long millis;    // Parte restante de los milisegundos.
		
		/**
		 * Constructor que recibe los segundos y milisegundos por separado.
		 */
		public SecondsAndMillis(long seconds, long millis) {
			this.sign    = "";
			this.seconds = seconds;
			this.millis  = millis;
		}
		
		/**
		 * Constructor que recibe una duración total en milisegundos y la descompone en
		 * segundos y milisegundos.
		 * 
		 * @param millisDuration La duración total en milisegundos.
		 */
		public SecondsAndMillis(long millisDuration) {
			// Operador ternario: si millisDuration es negativo, sign = "-", si no, sign = "".
			this.sign = (millisDuration < 0) ? "-" : "";
			
			long absoluteMillis = Math.abs(millisDuration);
			
			// Hay 1000 milisegundos en 1 segundo.
			this.seconds = absoluteMillis / 1000; // División entera para obtener los segundos completos.
			this.millis  = absoluteMillis % 1000;  // El resto de la división nos da los milisegundos sobrantes.
		}
		
		/**
		 * Sobrescribe el método `toString()` de la clase `Object`.
		 * La anotación `@Override` le indica al compilador que estamos reemplazando intencionadamente
		 * un método de la superclase. Ayuda a prevenir errores (por ejemplo, si escribiéramos mal el nombre).
		 * 
		 * Java llama a este método automáticamente cuando se necesita convertir el objeto a una cadena
		 * de texto, como al imprimirlo con `System.out.println()`.
		 */
		@Override
		public String toString() {
			// Combina segundos y milisegundos en un único número decimal.
			// (millis/1000.0) -> La división por un double (1000.0) asegura que el resultado sea decimal.
			double secsNMillis = seconds + (millis / 1000.0);
			
			// Formatea el número para que siempre muestre 3 dígitos decimales, rellenando con ceros si es necesario.
			// Ejemplo: 5.3 se convierte en "5.300".
			String secsStr = String.format("%.3f", secsNMillis);
			
			return sign + secsStr + " SECONDS";
		}
	}
}
