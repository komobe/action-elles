package ci.komobe.actionelle.application.features.vehicule.commands;

import ci.komobe.actionelle.domain.entities.Vehicule;
import lombok.EqualsAndHashCode;

/**
 * @author Moro KONÉ 2025-05-28
 */
@EqualsAndHashCode(callSuper = false)
public class SupprimerVehiculeByImmatriculation extends SupprimerVehiculeCommand<String> {

  private final String immatriculation;

  public SupprimerVehiculeByImmatriculation(String immatriculation) {
    this.immatriculation = immatriculation;
  }

  @Override
  public String value() {
    return immatriculation;
  }

  @Override
  public String field() {
    return "numeroImmatriculation";
  }

  @Override
  public boolean isSatisfiedBy(Vehicule vehicule) {
    return vehicule.getNumeroImmatriculation().equals(value());
  }
}
