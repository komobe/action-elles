package ci.komobe.actionelle.application.features.produit.usecases;

import ci.komobe.actionelle.domain.exceptions.ProduitErreur;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupprimerProduitUseCase {

  private final ProduitRepository produitRepository;

  public void execute(String id) {
    var produit = produitRepository.chercherParId(id)
        .orElseThrow(() -> new ProduitErreur("Produit non trouvé"));

    produitRepository.supprimer(produit);
  }
}