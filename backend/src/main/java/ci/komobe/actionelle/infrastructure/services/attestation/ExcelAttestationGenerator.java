package ci.komobe.actionelle.infrastructure.services.attestation;

import ci.komobe.actionelle.application.commons.services.attestation.AttestationFormat;
import ci.komobe.actionelle.application.commons.services.attestation.AttestationGenerator;
import ci.komobe.actionelle.application.commons.services.qrcode.QRCodeGenerator;
import ci.komobe.actionelle.application.features.souscription.dto.AttestationDto;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.valueobjects.Valeur;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Implémentation Excel de l'AttestationGenerator
 *
 * @author Moro KONÉ 2025-06-01
 */
@Service
@RequiredArgsConstructor
public class ExcelAttestationGenerator implements AttestationGenerator {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private final QRCodeGenerator qrCodeGenerator;

  @Override
  public byte[] generate(AttestationDto data) {
    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet("Attestation");

      // Styles
      CellStyle headerStyle = createHeaderStyle(workbook);
      CellStyle normalStyle = createNormalStyle(workbook);

      // Titre
      Row titleRow = sheet.createRow(0);
      Cell titleCell = titleRow.createCell(0);
      titleCell.setCellValue("ATTESTATION D'ASSURANCE");
      titleCell.setCellStyle(headerStyle);
      sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

      // Informations
      int rowNum = 2;
      createDataRow(sheet, rowNum++, "Référence", data.getNumero(), normalStyle);
      Vehicule vehicule = data.getVehicule();
      createDataRow(sheet, rowNum++, "Date de mise en circulation",
          vehicule.getDateMiseEnCirculation().format(DATE_FORMATTER), normalStyle);
      Valeur valeurNeuf = vehicule.getValeurNeuf();
      createDataRow(sheet, rowNum++, "Valeur à neuf",
          String.format("%s", valeurNeuf.toString()), normalStyle);
      createDataRow(sheet, rowNum++, "Valeur vénale",
          String.format("%s", data.getVehiculeValeurVenale()), normalStyle);
      createDataRow(sheet, rowNum++, "Puissance fiscale",
          String.format("%d CV", vehicule.getPuissanceFiscale()), normalStyle);
      createDataRow(sheet, rowNum++, "Produit souscrit",
          data.getProduit().getNom(), normalStyle);

      // QR Code avec l'URL absolue
      String qrCodeUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
          .path("/api/v1/subscriptions/{id}/attestation")
          .buildAndExpand(data.getNumero())
          .toUriString();

      var qrCode = qrCodeGenerator.generate(qrCodeUrl);
      if (qrCode != null) {
        int pictureIdx = workbook.addPicture(qrCode, Workbook.PICTURE_TYPE_PNG);
        CreationHelper helper = workbook.getCreationHelper();
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setRow1(rowNum + 1);
        anchor.setCol1(0);
        anchor.setRow2(rowNum + 8);
        anchor.setCol2(2);
        drawing.createPicture(anchor, pictureIdx);
      }

      // Ajuster la largeur des colonnes
      sheet.autoSizeColumn(0);
      sheet.autoSizeColumn(1);

      // Générer le fichier
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      workbook.write(outputStream);
      return outputStream.toByteArray();

    } catch (Exception e) {
      throw new RuntimeException("Erreur lors de la génération du fichier Excel", e);
    }
  }

  private void createDataRow(Sheet sheet, int rowNum, String label, String value, CellStyle style) {
    Row row = sheet.createRow(rowNum);
    Cell labelCell = row.createCell(0);
    Cell valueCell = row.createCell(1);

    labelCell.setCellValue(label);
    valueCell.setCellValue(value);

    labelCell.setCellStyle(style);
    valueCell.setCellStyle(style);
  }

  private CellStyle createHeaderStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    font.setFontHeightInPoints((short) 14);
    style.setFont(font);
    style.setAlignment(HorizontalAlignment.CENTER);
    return style;
  }

  private CellStyle createNormalStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setFontHeightInPoints((short) 11);
    style.setFont(font);
    return style;
  }

  @Override
  public AttestationFormat getFormat() {
    return AttestationFormat.EXCEL;
  }
}