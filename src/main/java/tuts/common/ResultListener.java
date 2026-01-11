package tuts.common;

/**
 * Esta es una interfaz funcional. En Java, una interfaz es como un "contrato"
 * que una clase puede prometer cumplir. No contiene la implementación (el "cómo"),
 * solo define el "qué".
 * 
 * El propósito de esta interfaz es definir la forma estándar para que cualquier
 * objeto pueda "escuchar" o ser notificado cuando un resultado esté disponible.
 * 
 * @param <T> Esto se llama un "genérico" (Generic). Significa que la interfaz
 *            puede trabajar con cualquier tipo de dato. Cuando una clase implemente
 *            esta interfaz, especificará con qué tipo de resultado concreto va a trabajar
 *            (por ejemplo, 'ResultListener<Integer>' para resultados numéricos,
 *            'ResultListener<String>' para texto, etc.).
 */
public interface ResultListener<T> {
	
	/**
	 * Este es el único método definido en el contrato.
	 * 
	 * Cualquier clase que quiera ser un "escuchador de resultados" (que implemente
	 * esta interfaz) DEBE proporcionar una implementación para este método.
	 * 
	 * Es el método que será llamado por el productor de resultados (la tarea)
	 * para entregar el resultado al escuchador.
	 * 
	 * @param result El resultado que está siendo notificado. El tipo de este
	 *               resultado será el que se especificó al implementar la interfaz (la 'T').
	 */
	void notifyResult(T result);

}
