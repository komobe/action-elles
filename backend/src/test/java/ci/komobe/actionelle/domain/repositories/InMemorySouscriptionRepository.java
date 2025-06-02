package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.Pageable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-28
 */
public class InMemorySouscriptionRepository implements SouscriptionRepository {

  private final Map<String, Souscription> souscriptions = new HashMap<>();

  public void addSouscription(Souscription souscription) {
    this.souscriptions.put(souscription.getId(), souscription);
  }

  @Override
  public void enregistrer(Souscription souscription) {
    addSouscription(souscription);
  }

  @Override
  public void supprimer(Souscription entity) {
    souscriptions.remove(entity.getId());
  }

  @Override
  public Optional<Souscription> chercherParId(String id) {
    return souscriptions.containsKey(id) ? Optional.of(souscriptions.get(id)) : Optional.empty();
  }

  @Override
  public List<Souscription> lister() {
    return new ArrayList<>(souscriptions.values());
  }

  @Override
  public boolean existeParId(String id) {
    return souscriptions.values().stream()
        .anyMatch(souscription -> souscription.getId().equals(id));
  }

  @Override
  public Page<Souscription> lister(Pageable pageRequest) {
    return Page.<Souscription>builder()
        .number(pageRequest.getNumber())
        .size(pageRequest.getSize())
        .totalElements(lister().size())
        .totalPages(1)
        .build();
  }

  @Override
  public Optional<Souscription> chercherParNumero(String numero) {
    return souscriptions.values().stream()
        .filter(souscription -> souscription.getNumero().equals(numero))
        .findFirst();
  }

  @Override
  public boolean existeParNumero(String numero) {
    return souscriptions.values().stream()
        .anyMatch(souscription -> souscription.getNumero().equals(numero));
  }
}
