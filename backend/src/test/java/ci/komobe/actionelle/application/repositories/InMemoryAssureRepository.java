package ci.komobe.actionelle.application.repositories;

import ci.komobe.actionelle.domain.repositories.AssureRepository;
import java.util.ArrayList;
import java.util.List;

import ci.komobe.actionelle.domain.entities.Assure;

public class InMemoryAssureRepository implements AssureRepository {
  private final List<Assure> assures = new ArrayList<>();

  @Override
  public boolean existsByNumeroCarteIdentite(String numeroCarteIdentite) {
    return assures.stream()
        .anyMatch(assure -> assure.getNumeroCarteIdentite().equals(numeroCarteIdentite));
  }

  @Override
  public void enregistrer(Assure assure) {
    assures.add(assure);
  }

  @Override
  public List<Assure> findAll() {
    return assures;
  }
}
