package ci.komobe.actionelle.application.features.vehicule.commands;

import ci.komobe.actionelle.application.commons.Specification;
import ci.komobe.actionelle.domain.entities.Vehicule;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * @author Moro KONÉ 2025-05-28
 */
public abstract class SupprimerVehiculeCommand<T> implements Specification<Vehicule> {

  public abstract T value();

  public abstract String field();

  @Override
  public Predicate toPredicate(Root<Vehicule> root, CriteriaQuery<?> query,
      CriteriaBuilder builder) {
    return builder.equal(root.get(field()), value());
  }
}