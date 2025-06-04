package ci.komobe.actionelle.seeders;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.domain.repositories.GarantieRepository;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Seeder pour les produits
 * 
 * @author Moro KONÉ 2025-06-03
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProduitsSeeder {

  private final ProduitRepository produitRepository;
  private final GarantieRepository garantieRepository;
  private final CategorieVehiculeRepository categorieVehiculeRepository;

  @Transactional
  public void seed() {
    log.info("Seeding des produits...");

    // Récupération des garanties
    Garantie rc = garantieRepository.chercherParCode("RC").orElseThrow();
    Garantie vol = garantieRepository.chercherParCode("VOL").orElseThrow();
    Garantie dom = garantieRepository.chercherParCode("DOM").orElseThrow();
    Garantie inc = garantieRepository.chercherParCode("INC").orElseThrow();
    Garantie bdg = garantieRepository.chercherParCode("BDG").orElseThrow();

    // Récupération des catégories
    CategorieVehicule vp = categorieVehiculeRepository.chercherParCode("VP").orElseThrow();
    CategorieVehicule tpm = categorieVehiculeRepository.chercherParCode("TPM").orElseThrow();
    CategorieVehicule tpma = categorieVehiculeRepository.chercherParCode("TPMA").orElseThrow();
    CategorieVehicule deuxRoues = categorieVehiculeRepository.chercherParCode("2R").orElseThrow();

    List<Produit> produits = List.of(
        // Produit Tiers Simple
        Produit.builder()
            .code("TIERS")
            .nom("Assurance au Tiers")
            .description("Assurance minimale obligatoire")
            .garanties(List.of(rc))
            .categorieVehicules(List.of(vp, tpm, tpma, deuxRoues))
            .build(),

        // Produit Tiers Étendu
        Produit.builder()
            .code("TIERS_PLUS")
            .nom("Assurance au Tiers Étendue")
            .description("Assurance au tiers avec garanties supplémentaires")
            .garanties(List.of(rc, vol, inc))
            .categorieVehicules(List.of(vp, tpm, tpma))
            .build(),

        // Produit Tous Risques
        Produit.builder()
            .code("TOUS_RISQUES")
            .nom("Assurance Tous Risques")
            .description("Protection complète pour votre véhicule")
            .garanties(List.of(rc, vol, dom, inc, bdg))
            .categorieVehicules(List.of(vp, tpm))
            .build(),

        // Produit Deux Roues
        Produit.builder()
            .code("2ROUES")
            .nom("Assurance Deux Roues")
            .description("Spécial motos et scooters")
            .garanties(List.of(rc, vol, inc))
            .categorieVehicules(List.of(deuxRoues))
            .build());

    produits.forEach(produit -> {
      if (!produitRepository.existeParCode(produit.getCode())) {
        produitRepository.enregistrer(produit);
        log.info("Produit créé : {}", produit.getCode());
      }
    });

    log.info("{} produits traités", produits.size());
  }
}