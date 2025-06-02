package ci.komobe.actionelle.application.features.produit.usecases;

import ci.komobe.actionelle.application.features.produit.commands.CreerProduitCommand;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.exceptions.ProduitErreur;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreerProduitUseCase {
  private final ProduitRepository produitRepository;
  private final Validator validator;

  public void execute(CreerProduitCommand command) {
    var violations = validator.validate(command);
    if (!violations.isEmpty()) {
      var messages = violations.stream()
          .map(ConstraintViolation::getMessage)
          .collect(Collectors.joining(", "));
      throw new ProduitErreur("Validation échouée : " + messages);
    }

    if (produitRepository.existeParCode(command.getCode())) {
      throw new ProduitErreur("Un produit avec le code " + command.getCode() + " existe déjà");
    }

    var produit = Produit.builder()
        .code(command.getCode())
        .nom(command.getLibelle())
        .description(command.getDescription())
        .build();

    produitRepository.enregistrer(produit);
  }
}