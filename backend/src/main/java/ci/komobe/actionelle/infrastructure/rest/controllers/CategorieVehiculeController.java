package ci.komobe.actionelle.infrastructure.rest.controllers;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Moro KONÉ 2025-06-03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories-vehicules")
@Tag(name = "Categories véhicules", description = "API de gestion des categories de véhicules")
public class CategorieVehiculeController {

  private final CategorieVehiculeRepository categorieVehiculeRepository;

  @GetMapping
  public List<CategorieVehicule> lister() {
    return categorieVehiculeRepository.lister();
  }
}
