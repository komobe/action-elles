package ci.komobe.actionelle.infrastructure.persistences.jpa.converters;

import ci.komobe.actionelle.domain.valueobjects.Valeur;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

/**
 * Convertisseur JPA pour le type Valeur Permet de convertir une Valeur en deux colonnes dans la
 * base de données
 *
 * @author Moro KONÉ
 */
@Converter(autoApply = true)
public class ValeurConverter implements AttributeConverter<Valeur, BigDecimal> {

  @Override
  public BigDecimal convertToDatabaseColumn(Valeur valeur) {
    if (valeur == null) {
      return null;
    }
    return valeur.getMontant();
  }

  @Override
  public Valeur convertToEntityAttribute(BigDecimal dbData) {
    if (dbData == null) {
      return null;
    }

    return Valeur.of(dbData, "XOF");
  }
}