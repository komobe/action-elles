package ci.komobe.actionelle.domain.utils.paginate;

import ci.komobe.actionelle.domain.utils.ClientResponse;
import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Réponse paginée pour l'API
 *
 * @param <T> type des éléments de la page
 * @author Moro KONÉ 2025-05-31
 */
@Value
@Builder
public class PageResponse<T> implements ClientResponse {

  /**
   * Données de la page
   */
  List<T> data;

  /**
   * Métadonnées de la page
   */
  PageMetadata metadata;

  /**
   * Liens de navigation
   */
  Links links;

  /**
   * Crée une réponse paginée à partir d'une page
   *
   * @param <T>     type des éléments
   * @param page    la page source
   * @param baseUrl URL de base pour les liens
   * @return la réponse paginée
   */
  public static <T> PageResponse<T> from(Page<T> page, String baseUrl) {
    // Convertir les numéros de page 0-based en 1-based pour l'API
    int currentPage = page.getNumber() + 1;
    int lastPage = page.getTotalPages() > 0 ? page.getTotalPages() : 1;

    return PageResponse.<T>builder()
        .data(page.getData())
        .metadata(PageMetadata.from(page))
        .links(Links.builder()
            .current(buildPageUrl(baseUrl, currentPage))
            .first(buildPageUrl(baseUrl, 1))
            .last(buildPageUrl(baseUrl, lastPage))
            .next(page.hasNext() ? buildPageUrl(baseUrl, currentPage + 1) : null)
            .prev(page.hasPrevious() ? buildPageUrl(baseUrl, currentPage - 1) : null)
            .build())
        .build();
  }

  private static String buildPageUrl(String baseUrl, int page) {
    return String.format("%s?page=%d", baseUrl, page);
  }

  /**
   * Crée une réponse paginée vide
   *
   * @param <T>     type des éléments
   * @param baseUrl URL de base pour les liens
   * @return une réponse paginée vide
   */
  public static <T> PageResponse<T> empty(String baseUrl) {
    Page<T> emptyPage = Page.<T>builder()
        .data(List.of())
        .number(0)
        .size(PaginationConstants.DEFAULT_PAGE_SIZE)
        .totalElements(0)
        .totalPages(1)
        .build();
    return from(emptyPage, baseUrl);
  }

  /**
   * Vérifie si la page contient des données
   *
   * @return true si la page contient des données
   */
  public boolean hasContent() {
    return data != null && !data.isEmpty();
  }

  /**
   * Obtient le nombre d'éléments dans la page
   *
   * @return nombre d'éléments
   */
  public int getSize() {
    return data == null ? 0 : data.size();
  }

  /**
   * Liens de navigation HATEOAS
   */
  @Value
  @Builder
  public static class Links {

    /**
     * Lien vers la page courante
     */
    String current;

    /**
     * Lien vers la première page
     */
    String first;

    /**
     * Lien vers la dernière page
     */
    String last;

    /**
     * Lien vers la page suivante
     */
    String next;

    /**
     * Lien vers la page précédente
     */
    String prev;
  }
}