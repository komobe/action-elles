package ci.komobe.actionelle.infrastructure.adapters.application.common;

import ci.komobe.actionelle.application.commons.services.attestation.AttestationFormat;
import ci.komobe.actionelle.application.commons.services.attestation.AttestationGenerator;
import ci.komobe.actionelle.application.commons.services.attestation.AttestationGeneratorService;
import ci.komobe.actionelle.application.features.souscription.dto.AttestationDto;
import ci.komobe.actionelle.infrastructure.services.attestation.ExcelAttestationGenerator;
import ci.komobe.actionelle.infrastructure.services.attestation.PDFAttestationGenerator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Moro KONÉ 2025-06-01
 */
@Configuration
public class AttestationServiceConfiguration {

  @Bean
  public AttestationGeneratorService attestationGeneratorService(
      PDFAttestationGenerator pdfAttestationGenerator,
      ExcelAttestationGenerator excelAttestationGenerator
  ) {
    // Initialisation des générateurs dans une EnumMap
    Map<AttestationFormat, AttestationGenerator> generators =
        new EnumMap<>(AttestationFormat.class);

    generators.put(pdfAttestationGenerator.getFormat(), pdfAttestationGenerator);

    return new AttestationGeneratorService() {

      @Override
      public byte[] generer(AttestationDto attestationDto, AttestationFormat format) {
        AttestationGenerator generator = generators.get(format);
        if (generator == null) {
          throw new IllegalArgumentException("Format d'attestation non supporté pour le moment : " + format);
        }
        return generator.generate(attestationDto);
      }

      @Override
      public List<AttestationFormat> getSupportedFormats() {
        return List.copyOf(generators.keySet());
      }
    };
  }
}
