package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Devis;
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
public class InMemoryDevisRepository implements DevisRepository {

  private final Map<String, Devis> devis = new HashMap<>();

  public void addDevis(Devis devis) {
    this.devis.put(devis.getReference(), devis);
  }

  @Override
  public void enregistrer(Devis devis) {
    addDevis(devis);
  }

  @Override
  public void supprimer(Devis entity) {
    devis.remove(entity.getReference());
  }

  @Override
  public Optional<Devis> chercherParId(String id) {
    return devis.values().stream()
        .filter(d -> d.getId().equals(id))
        .findFirst();
  }

  @Override
  public Optional<Devis> chercherParReference(String reference) {
    return devis.containsKey(reference) ? Optional.of(devis.get(reference)) : Optional.empty();
  }

  @Override
  public boolean existeParReference(String reference) {
    return devis.containsKey(reference);
  }

  @Override
  public boolean existeParId(String id) {
    return devis.values().stream().anyMatch(d -> d.getId().equals(id));
  }

  @Override
  public List<Devis> lister() {
    return new ArrayList<>(devis.values());
  }

  @Override
  public Page<Devis> lister(Pageable pageable) {
    var content = devis.values().stream()
        .skip(pageable.getOffset())
        .limit(pageable.getSize())
        .toList();
    int totalElements = devis.size();
    int totalPages = (int) Math.ceil((double) totalElements / pageable.getSize());
    return Page.<Devis>builder()
        .data(content)
        .number(pageable.getNumber())
        .size(pageable.getSize())
        .totalElements(totalElements)
        .totalPages(totalPages)
        .build();
  }
}