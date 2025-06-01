package ci.komobe.actionelle.domain.utils.paginate;

import ci.komobe.actionelle.domain.utils.sorting.Sort;
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
public class PageResponse<T> {
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
    return PageResponse.<T>builder()
        .data(page.getData())
        .metadata(PageMetadata.from(page))
        .links(Links.builder()
            .self(buildPageUrl(baseUrl, page.getNumber()))
            .first(buildPageUrl(baseUrl, 0))
            .last(buildPageUrl(baseUrl, page.getTotalPages() - 1))
            .next(page.hasNext() ? buildPageUrl(baseUrl, page.getNumber() + 1) : null)
            .prev(page.hasPrevious() ? buildPageUrl(baseUrl, page.getNumber() - 1) : null)
            .build())
        .build();
  }

  private static String buildPageUrl(String baseUrl, int page) {
    return String.format("%s?page=%d", baseUrl, page);
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
    String self;

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
        .totalPages(0)
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
}