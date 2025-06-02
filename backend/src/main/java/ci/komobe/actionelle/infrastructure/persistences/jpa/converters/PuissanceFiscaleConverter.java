package ci.komobe.actionelle.infrastructure.persistences.jpa.converters;


import ci.komobe.actionelle.domain.valueobjects.PuissanceFiscale;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Converter(autoApply = true)
public class PuissanceFiscaleConverter implements AttributeConverter<PuissanceFiscale, String> {

  @Override
  public String convertToDatabaseColumn(PuissanceFiscale puissanceFiscale) {
    if (puissanceFiscale == null) {
      return null;
    }
    return puissanceFiscale.toString();
  }

  @Override
  public PuissanceFiscale convertToEntityAttribute(String dbData) {
    return PuissanceFiscale.fromString(dbData);
  }
}
