package ci.komobe.actionelle.infrastructure.persistences.jpa.converters;

import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convertisseur JPA pour l'énumération TypeMontantPrime
 * 
 * @author Moro KONÉ 2025-06-01
 */
@Converter(autoApply = true)
public class TypeMontantPrimeConverter implements AttributeConverter<TypeMontantPrime, String> {

  @Override
  public String convertToDatabaseColumn(TypeMontantPrime attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.name();
  }

  @Override
  public TypeMontantPrime convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    return TypeMontantPrime.valueOf(dbData);
  }
}