package ci.komobe.actionelle.application.features.assure.usecases;

import ci.komobe.actionelle.application.features.assure.commands.CreerAssureCommand;
import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.domain.exceptions.AssureErreur;
import ci.komobe.actionelle.domain.repositories.AssureRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreerAssureUseCase {
  private final AssureRepository assureRepository;
  private final Validator validator;

  public void execute(CreerAssureCommand command) {
    var violations = validator.validate(command);
    if (!violations.isEmpty()) {
      var messages = violations.stream()
          .map(ConstraintViolation::getMessage)
          .collect(Collectors.joining(", "));
      throw new AssureErreur("Validation échouée : " + messages);
    }

    if (assureRepository.existeParEmail(command.getEmail())) {
      throw new AssureErreur("Un assuré avec l'email " + command.getEmail() + " existe déjà");
    }

    var assure = Assure.builder()
        .numeroCarteIdentite(command.getNumeroCarteIdentite())
        .nom(command.getNom())
        .prenoms(command.getPrenoms())
        .sexe(command.getSexe())
        .dateNaissance(command.getDateNaissance())
        .lieuNaissance(command.getLieuNaissance())
        .email(command.getEmail())
        .telephone(command.getTelephone())
        .adresse(command.getAdresse())
        .build();

    assureRepository.enregistrer(assure);
  }
}