package ci.komobe.actionelle.domain.repositories;


import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.Pageable;
import java.util.List;
import java.util.Optional;

/**
 * Interface de base pour tous les repositories
 *
 * @author Moro KONÉ 2025-06-01
 */
public interface BaseRepository<T, I> {

  void enregistrer(T entity);

  default void modifier(T entity) {
  }

  void supprimer(T entity);

  Optional<T> chercherParId(I id);

  boolean existeParId(I id);

  List<T> lister();

  Page<T> lister(Pageable pageRequest);
}