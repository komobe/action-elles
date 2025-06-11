package ci.komobe.actionelle.infrastructure.services.attestation;

import ci.komobe.actionelle.application.commons.services.qrcode.QRCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * @author Moro KONÉ 2025-06-11
 */
@Component
@RequiredArgsConstructor
public class GenererAttestationQrCode {

  private final QRCodeGenerator qrCodeGenerator;

  public byte[] generate(String numero) {
    String attestationUrl = getAttestationUrl(numero);
    return qrCodeGenerator.generate(attestationUrl);
  }

  private String getAttestationUrl(String numero) {
    return ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/v1/subscriptions/{id}/attestation")
        .buildAndExpand(numero)
        .toUriString();
  }
}
