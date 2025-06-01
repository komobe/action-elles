package ci.komobe.actionelle.application.repositories;

import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemorySouscriptionRepository implements SouscriptionRepository {

  private final List<Souscription> souscriptions = new ArrayList<>();

  @Override
  public void enregistrer(Souscription souscription) {
    souscriptions.add(souscription);
  }

  @Override
  public List<Souscription> findAll() {
    return souscriptions;
  }

  @Override
  public Optional<Souscription> recupererParId(String souscriptionId) {
    return souscriptions.stream()
        .filter(souscription -> souscription.getId().equals(souscriptionId))
        .findFirst();
  }
}
