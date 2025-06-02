package ci.komobe.actionelle.infrastructure.services.qrcode;

import ci.komobe.actionelle.application.commons.services.qrcode.QRCodeGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

/**
 * Implémentation du générateur de QR Code avec ZXing
 * 
 * @author Moro KONÉ 2025-06-01
 */
@Service
public class ZXingQRCodeGenerator implements QRCodeGenerator {

  private static final int QR_CODE_SIZE = 200;

  @Override
  public byte[] generate(String data) {
    try {
      QRCodeWriter qrCodeWriter = new QRCodeWriter();
      BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE);

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

      return outputStream.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("Erreur lors de la génération du QR Code", e);
    }
  }
}