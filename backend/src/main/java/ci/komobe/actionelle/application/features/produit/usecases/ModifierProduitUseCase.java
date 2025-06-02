package ci.komobe.actionelle.application.features.produit.usecases;

import ci.komobe.actionelle.application.features.produit.commands.ModifierProduitCommand;
import ci.komobe.actionelle.domain.exceptions.ProduitErreur;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifierProduitUseCase {
  private final ProduitRepository produitRepository;
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

    produit.setCode(command.getCode());
    produit.setNom(command.getLibelle());
    produit.setDescription(command.getDescription());

    produitRepository.enregistrer(produit);
  }
}