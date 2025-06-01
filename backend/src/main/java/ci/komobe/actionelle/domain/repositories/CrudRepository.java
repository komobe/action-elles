package ci.komobe.actionelle.domain.repositories;

/**
 * @author Moro KONÉ 2025-06-01
 */
public interface CrudRepository<T> {

  void enregistrer(T entity);

  default void supprimer(T entity) {}
}
