package ci.komobe.actionelle.infrastructure.services.qrcode;

import static com.google.zxing.BarcodeFormat.QR_CODE;

import ci.komobe.actionelle.application.commons.services.qrcode.QRCodeGenerator;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.stereotype.Service;

/**
 * Implémentation du générateur de QR code avec ZXing
 *
 * @author Moro KONÉ 2025-06-01
 */
@Service
public class ZXingQRCodeGenerator implements QRCodeGenerator {

  private static final int QR_CODE_SIZE = 200;
  private static final String IMAGE_FORMAT = "PNG";

  @Override
  public byte[] generate(String qrCodeUrl) {
    try {
      QRCodeWriter qrCodeWriter = new QRCodeWriter();
      BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeUrl, QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE);

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(bitMatrix, IMAGE_FORMAT, outputStream);

      return outputStream.toByteArray();

    } catch (WriterException | IOException e) {
      throw new IllegalArgumentException("Erreur lors de la génération du QR Code: ", e);
    }
  }
}