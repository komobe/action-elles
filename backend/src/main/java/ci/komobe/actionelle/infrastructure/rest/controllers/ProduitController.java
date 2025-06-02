package ci.komobe.actionelle.infrastructure.rest.controllers;

import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import lombok.RequiredArgsConstructor;
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
public class ProduitController {

  private final ProduitRepository produitRepository;
}