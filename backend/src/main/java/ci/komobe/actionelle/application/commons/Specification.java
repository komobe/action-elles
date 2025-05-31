package ci.komobe.actionelle.application.commons;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * @author Moro KONÉ 2025-05-28
 */
@FunctionalInterface
public interface Specification<T> {

  Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder);

  default boolean isSatisfiedBy(T t) {
    return false;
  }
}
