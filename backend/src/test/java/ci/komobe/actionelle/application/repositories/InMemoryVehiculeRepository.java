package ci.komobe.actionelle.application.repositories;

import ci.komobe.actionelle.application.commons.Specification;
import ci.komobe.actionelle.application.utils.FakeGenerator;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;
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
    return findRandomVehicule().getNumeroImmatriculation();
  }

  public void addVehicule(Vehicule vehicule) {
    this.vehicules.put(vehicule.getNumeroImmatriculation(), vehicule);
  }

  @Override
  public void enregistrer(Vehicule vehicule) {
    addVehicule(vehicule);
    saveCallCount++;
  }

  @Override
  public boolean existsByImmatriculation(String numero) {
    return vehicules.containsKey(numero);
  }

  @Override
  public Optional<Vehicule> recupererParImmatriculation(String numero) {
    return vehicules.containsKey(numero) ? Optional.of(vehicules.get(numero)) : Optional.empty();
  }

  @Override
  public void supprimer(Specification<Vehicule> specification) {
    List<String> keysToRemove = new ArrayList<>();
    for (Map.Entry<String, Vehicule> entry : vehicules.entrySet()) {
      if (specification.isSatisfiedBy(entry.getValue())) {
        keysToRemove.add(entry.getKey());
      }
    }
    for (String key : keysToRemove) {
      vehicules.remove(key);
    }
  }

  @Override
  public Optional<Vehicule> recupererParSpec(Specification<Vehicule> specification) {
    return vehicules.values().stream()
        .filter(specification::isSatisfiedBy)
        .findFirst();
  }

  @Override
  public List<Vehicule> findAll() {
    return new ArrayList<>(vehicules.values());
  }
}
