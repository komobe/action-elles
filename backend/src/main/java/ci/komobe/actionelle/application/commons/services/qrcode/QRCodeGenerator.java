package ci.komobe.actionelle.application.commons.services.qrcode;

/**
 * Interface pour la génération de QR Code
 * 
 * @author Moro KONÉ 2025-06-01
 */
public interface QRCodeGenerator {
  byte[] generate(String data);
}