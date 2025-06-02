package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.application.commons.Specification;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.utils.FakeGenerator;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.Pageable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;

/**
 * @author Moro KONÉ 2025-05-28
 */
public class InMemoryVehiculeRepository implements VehiculeRepository {

  private final Map<String, Vehicule> vehicules = new HashMap<>();
  @Getter
  private int saveCallCount = 0;

  public InMemoryVehiculeRepository() {
    this(0);
  }

  public InMemoryVehiculeRepository(int count) {
    FakeGenerator.generateVehicules(count).forEach(this::addVehicule);
  }

  public Vehicule findRandomVehicule() {
    return vehicules.values().stream().findAny().orElse(null);
  }

  public String findAnyImmatriculation() {
    return findRandomVehicule().getImmatriculation();
  }

  public void addVehicule(Vehicule vehicule) {
    this.vehicules.put(vehicule.getImmatriculation(), vehicule);
  }

  @Override
  public void enregistrer(Vehicule vehicule) {
    addVehicule(vehicule);
    saveCallCount++;
  }

  @Override
  public void supprimer(Vehicule entity) {
    vehicules.remove(entity.getImmatriculation());
  }

  @Override
  public Optional<Vehicule> chercherParId(String id) {
    return vehicules.values().stream()
        .filter(vehicule -> vehicule.getId().equals(id))
        .findFirst();
  }

  @Override
  public boolean existParImmatriculation(String numero) {
    return vehicules.containsKey(numero);
  }

  @Override
  public Optional<Vehicule> chercherParImmatriculation(String numero) {
    return vehicules.containsKey(numero) ? Optional.of(vehicules.get(numero)) : Optional.empty();
  }

  @Override
  public Optional<Vehicule> chercherParSpec(Specification<Vehicule> specification) {
    return vehicules.values().stream()
        .filter(specification::isSatisfiedBy)
        .findFirst();
  }

  @Override
  public List<Vehicule> lister() {
    return new ArrayList<>(vehicules.values());
  }

  @Override
  public boolean existeParId(String id) {
    return vehicules.values().stream()
        .anyMatch(vehicule -> vehicule.getId().equals(id));
  }

  @Override
  public Page<Vehicule> lister(Pageable pageable) {
    return Page.<Vehicule>builder()
        .number(pageable.getNumber())
        .size(pageable.getSize())
        .totalElements(lister().size())
        .totalPages(1)
        .build();
  }
}
