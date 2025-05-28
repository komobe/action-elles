package ci.komobe.actionelle.application.repositories;

import java.util.ArrayList;
import java.util.List;

import ci.komobe.actionelle.domain.entities.Souscription;

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
}
