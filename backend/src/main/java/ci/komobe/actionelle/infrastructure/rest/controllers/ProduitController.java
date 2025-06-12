package ci.komobe.actionelle.infrastructure.rest.controllers;

import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des produits
 *
 * @author Moro KONÉ 2025-06-01
 */
@RestController
@RequestMapping("/api/v1/produits")
@RequiredArgsConstructor
@Tag(name = "Produits", description = "API de gestion des produits")
public class ProduitController {

  private final ProduitRepository produitRepository;

  @GetMapping
  public List<Produit> listerProduits() {
    return produitRepository.lister();
  }
}