package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Assure;
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
public class InMemoryAssureRepository implements AssureRepository {

  private final Map<String, Assure> assures = new HashMap<>();

  public void addAssure(Assure assure) {
    this.assures.put(assure.getNumeroCarteIdentite(), assure);
  }

  @Override
  public void enregistrer(Assure assure) {
    addAssure(assure);
  }

  @Override
  public void supprimer(Assure entity) {
    assures.remove(entity.getNumeroCarteIdentite());
  }

  @Override
  public Optional<Assure> chercherParId(String id) {
    return assures.values().stream()
        .filter(assure -> assure.getId().equals(id))
        .findFirst();
  }

  @Override
  public Optional<Assure> chercherParNumeroCarteIdentite(String numero) {
    return assures.containsKey(numero) ? Optional.of(assures.get(numero)) : Optional.empty();
  }

  @Override
  public boolean existeParNumeroCarteIdentite(String numeroCarteIdentite) {
    return assures.containsKey(numeroCarteIdentite);
  }

  @Override
  public boolean existeParId(String id) {
    return assures.values().stream().anyMatch(a -> a.getId().equals(id));
  }

  @Override
  public boolean existeParEmail(String email) {
    return assures.values().stream().anyMatch(a -> a.getEmail().equals(email));
  }

  @Override
  public List<Assure> lister() {
    return new ArrayList<>(assures.values());
  }

  @Override
  public Page<Assure> lister(Pageable pageable) {
    var content = assures.values().stream()
        .skip(pageable.getOffset())
        .limit(pageable.getSize())
        .toList();
    int totalElements = assures.size();
    int totalPages = (int) Math.ceil((double) totalElements / pageable.getSize());
    return Page.<Assure>builder()
        .data(content)
        .number(pageable.getNumber())
        .size(pageable.getSize())
        .totalElements(totalElements)
        .totalPages(totalPages)
        .build();
  }
}
