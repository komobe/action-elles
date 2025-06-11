package ci.komobe.actionelle.infrastructure.services.attestation;

import ci.komobe.actionelle.application.commons.services.attestation.AttestationFormat;
import ci.komobe.actionelle.application.commons.services.attestation.AttestationGenerator;
import ci.komobe.actionelle.application.features.souscription.dto.AttestationDto;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.valueobjects.Valeur;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implémentation du générateur d'attestation au format PDF
 *
 * @author Moro KONÉ 2025-06-01
 */
@Service
@RequiredArgsConstructor
public class PDFAttestationGenerator implements AttestationGenerator {

  private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
  private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private final GenererAttestationQrCode genererAttestationQrCode;

  @Override
  public byte[] generate(AttestationDto data) {
    try {
      var attestationQrCode = genererAttestationQrCode.generate(data.getNumero());

      Document document = new Document();
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PdfWriter.getInstance(document, outputStream);

      document.open();

      // Titre
      Paragraph title = new Paragraph("ATTESTATION D'ASSURANCE", TITLE_FONT);
      title.setAlignment(Element.ALIGN_CENTER);
      document.add(title);
      document.add(new Paragraph(" ")); // Espace

      // Informations du véhicule
      document.add(new Paragraph("Référence: " + data.getNumero(), NORMAL_FONT));
      Vehicule vehicule = data.getVehicule();
      document.add(new Paragraph(
          "Date de mise en circulation: " + vehicule.getDateMiseEnCirculation()
              .format(DATE_FORMATTER),
          NORMAL_FONT));
      Valeur valeurNeuf = vehicule.getValeurNeuf();
      document.add(new Paragraph("Valeur à neuf: " + valeurNeuf.toString(), NORMAL_FONT));
      document.add(
          new Paragraph(
              "Valeur vénale: " + data.getVehiculeValeurVenale(), NORMAL_FONT));
      document.add(
          new Paragraph("Puissance fiscale: " + vehicule.getPuissanceFiscale() + " CV",
              NORMAL_FONT));
      document.add(new Paragraph("Produit souscrit: " + data.getProduit().getNom(), NORMAL_FONT));

      // QR Code
      if (attestationQrCode != null) {
        document.add(new Paragraph(" ")); // Espace
        Image qrCodeImage = Image.getInstance(attestationQrCode);
        qrCodeImage.setAlignment(Element.ALIGN_CENTER);
        document.add(qrCodeImage);
      }

      document.close();

      return outputStream.toByteArray();
    } catch (Exception e) {
      throw new IllegalArgumentException("Erreur lors de la génération du PDF", e);
    }
  }

  @Override
  public AttestationFormat getFormat() {
    return AttestationFormat.PDF;
  }
}