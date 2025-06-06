package ci.komobe.actionelle.seeders;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.domain.repositories.GarantieRepository;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import ci.komobe.actionelle.domain.utils.IdGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

  @SafeVarargs
  private static <T> List<T> buildGaranties(List<T> base, T... others) {
    List<T> result = new ArrayList<>(base);
    Collections.addAll(result, others);
    return result;
  }

  @Transactional
  public void seed() {
    if (!produitRepository.lister().isEmpty()) {
      return;
    }

    log.info("Seeding des produits...");

    // Récupération des garanties
    List<Garantie> rc = garantieRepository.lister()
        .stream().filter(g -> g.getCode().equals("RC"))
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

    Garantie dommage = garantieRepository.chercherParCode("DOMMAGE").orElseThrow();
    Garantie tierceCollison = garantieRepository.chercherParCode("TIERCE_COLLISION").orElseThrow();
    Garantie tiercePlafonne = garantieRepository.chercherParCode("TIERCE_PLAFONNEE").orElseThrow();
    Garantie vol = garantieRepository.chercherParCode("VOL").orElseThrow();
    Garantie incendie = garantieRepository.chercherParCode("INCENDIE").orElseThrow();

    // Récupération des catégories
    CategorieVehicule categorie201 = categorieVehiculeRepository.chercherParCode("201").orElseThrow();
    CategorieVehicule categorie202 = categorieVehiculeRepository.chercherParCode("202").orElseThrow();
    CategorieVehicule categorie203 = categorieVehiculeRepository.chercherParCode("203").orElseThrow();
    CategorieVehicule categorie204 = categorieVehiculeRepository.chercherParCode("204").orElseThrow();

    List<Produit> produits = List.of(
        // Produit Papillon
        Produit.builder()
            .id(IdGenerator.generateId())
            .code("Papillon")
            .nom("Papillon")
            .description("RC, DOMMAGE, VOL")
            .garanties(buildGaranties(rc, dommage, vol))
            .categorieVehicules(List.of(categorie201, categorie202, categorie203, categorie204))
            .build(),

        // Produit Douby
        Produit.builder()
            .id(IdGenerator.generateId())
            .code("Douby")
            .nom("Douby")
            .description("RC, DOMMAGE, TIERCE COLLISION")
            .garanties(buildGaranties(rc, dommage, tierceCollison))
            .categorieVehicules(List.of(categorie202))
            .build(),

        // Produit Douyou
        Produit.builder()
            .id(IdGenerator.generateId())
            .code("Douyou")
            .nom("Douyou")
            .description("RC, DOMMAGE, COLLISION, INCENDIE")
            .garanties(buildGaranties(rc, dommage, tierceCollison, incendie))
            .categorieVehicules(List.of(categorie201, categorie202))
            .build(),

        // Produit Toutourisquou
        Produit.builder()
            .id(IdGenerator.generateId())
            .code("Toutourisquou")
            .nom("Toutourisquou")
            .description("Toutes garanties")
            .garanties(buildGaranties(rc, dommage, tierceCollison, tiercePlafonne, vol, incendie))
            .categorieVehicules(List.of(categorie201))
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