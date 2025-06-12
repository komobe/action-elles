package ci.komobe.actionelle.application.features.produit.usecases;

import ci.komobe.actionelle.application.features.produit.commands.ModifierProduitCommand;
import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.exceptions.ProduitErreur;
import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.domain.repositories.GarantieRepository;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifierProduitUseCase {
  private final ProduitRepository produitRepository;
  private final GarantieRepository garantieRepository;
  private final CategorieVehiculeRepository categorieVehiculeRepository;
  private final Validator validator;

  public void execute(ModifierProduitCommand command) {
    var violations = validator.validate(command);
    if (!violations.isEmpty()) {
      var messages = violations.stream()
          .map(ConstraintViolation::getMessage)
          .collect(Collectors.joining(", "));
      throw new ProduitErreur("Validation échouée : " + messages);
    }

    var produit = produitRepository.chercherParId(command.getId())
        .orElseThrow(() -> new ProduitErreur("Produit non trouvé"));

    if (!produit.getCode().equals(command.getCode()) && produitRepository.existeParCode(command.getCode())) {
      throw new ProduitErreur("Un produit avec le code " + command.getCode() + " existe déjà");
    }

    // Récupération des garanties
    List<Garantie> garanties = command.getGaranties().stream()
        .map(code -> garantieRepository.chercherParCode(code)
            .orElseThrow(() -> new ProduitErreur("Garantie avec le code " + code + " non trouvée")))
        .toList();

    // Récupération des catégories de véhicules
    List<CategorieVehicule> categoriesVehicules = command.getCategoriesVehicules().stream()
        .map(code -> categorieVehiculeRepository.chercherParCode(code)
            .orElseThrow(() -> new ProduitErreur("Catégorie de véhicule avec le code " + code + " non trouvée")))
        .toList();

    produit.setCode(command.getCode());
    produit.setNom(command.getLibelle());
    produit.setDescription(command.getDescription());
    produit.setGaranties(garanties);
    produit.setCategoriesVehicules(categoriesVehicules);

    produitRepository.enregistrer(produit);
  }
}