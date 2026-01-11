package tuts.common;

/**
 * Esta es una clase "contenedora" o "de datos" genérica. Su único propósito es
 * agrupar dos piezas de información relacionadas: el identificador de una tarea y
 * el resultado producido por esa tarea.
 * 
 * @param <S> El tipo de dato para el identificador de la tarea (ej. String, Integer).
 * @param <R> El tipo de dato para el resultado de la tarea (ej. Integer, Double, String).
 */
public class TaskResult<S, R> {

	// 'final' significa que una vez que se asigna un valor a estas variables
	// (en el constructor), no se puede cambiar. Esto hace que el objeto sea "inmutable"
	// y más seguro para usar en entornos multihilo.
	public final S taskId;
	public final R result;
	
	/**
	 * Constructor para crear una nueva instancia de TaskResult.
	 * @param taskId El identificador de la tarea.
	 * @param result El resultado de la tarea.
	 */
	public TaskResult(S taskId, R result) {
		this.taskId = taskId;
		this.result = result;
	}

	/**
	 * El método hashCode() es utilizado por estructuras de datos como HashMap o HashSet
	 * para organizar los objetos de manera eficiente.
	 * Es una buena práctica implementarlo siempre que se implementa equals().
	 * La regla es: si dos objetos son 'equals', deben tener el mismo 'hashCode'.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
		return result;
	}

	/**
	 * El método equals() define lo que significa que dos objetos TaskResult sean
	 * "iguales". En este caso, dos TaskResult son iguales si tienen el mismo
	 * taskId y el mismo result.
	 * Es útil para comparar objetos, por ejemplo, en pruebas unitarias.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskResult other = (TaskResult) obj;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		return true;
	}

	/**
	 * El método toString() proporciona una representación en texto del objeto.
	 * Es muy útil para la depuración, ya que permite imprimir el contenido del
	 * objeto de una manera legible, por ejemplo: System.out.println(myTaskResult);
	 */
	@Override
	public String toString() {
		return "~~~~~TaskResult [taskId=" + taskId + ", result=" + result + "]~~~~~";
	}
}
