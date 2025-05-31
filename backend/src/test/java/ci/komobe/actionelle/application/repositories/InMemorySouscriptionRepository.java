package ci.komobe.actionelle.application.repositories;

import ci.komobe.actionelle.domain.entities.Souscription;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemorySouscriptionRepository implements SouscriptionRepository {

  private final List<Souscription> souscriptions = new ArrayList<>();

  @Override
  public void save(Souscription souscription) {
    souscriptions.add(souscription);
  }

  @Override
  public List<Souscription> findAll() {
    return souscriptions;
  }

  @Override
  public Optional<Souscription> findById(String souscriptionId) {
    return souscriptions.stream()
        .filter(souscription -> souscription.getId().equals(souscriptionId))
        .findFirst();
  }
}
