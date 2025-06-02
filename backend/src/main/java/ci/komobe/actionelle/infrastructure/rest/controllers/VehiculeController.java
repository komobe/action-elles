package ci.komobe.actionelle.infrastructure.rest.controllers;

import ci.komobe.actionelle.domain.repositories.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des véhicules
 *
 * @author Moro KONÉ 2025-06-01
 */
@RestController
@RequestMapping("/api/v1/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

  private final VehiculeRepository vehiculeRepository;
}