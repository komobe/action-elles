package ci.komobe.actionelle.application.features.assure.usecases;

import ci.komobe.actionelle.application.features.assure.commands.ModifierAssureCommand;
import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.domain.exceptions.AssureErreur;
import ci.komobe.actionelle.domain.repositories.AssureRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifierAssureUseCase {
  private final AssureRepository assureRepository;
  private final Validator validator;

  public void execute(ModifierAssureCommand command) {
    var violations = validator.validate(command);
    if (!violations.isEmpty()) {
      var messages = violations.stream()
          .map(ConstraintViolation::getMessage)
          .collect(Collectors.joining(", "));
      throw new AssureErreur("Validation échouée : " + messages);
    }

    Assure assure = assureRepository.chercherParId(command.getId())
        .orElseThrow(() -> new AssureErreur("Assuré non trouvé"));

    var assureData = command.getAssureData();
    if (!assure.getEmail().equals(assureData.getEmail()) && assureRepository.existeParEmail(assureData.getEmail())) {
      throw new AssureErreur("Un assuré avec l'email " + assureData.getEmail() + " existe déjà");
    }

    assure.setNom(assureData.getNom());
    assure.setPrenoms(assureData.getPrenoms());
    assure.setEmail(assureData.getEmail());
    assure.setTelephone(assureData.getTelephone());
    assure.setDateNaissance(assureData.getDateNaissance());
    assure.setLieuNaissance(assureData.getLieuNaissance());
    assure.setNumeroCarteIdentite(assureData.getNumeroCarteIdentite());
    assure.setAdresse(assureData.getAdresse());
    assure.setSexe(assureData.getSexe());

    assureRepository.enregistrer(assure);
  }
}