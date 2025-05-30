package ci.komobe.actionelle.infrastructure.views.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Setter
@Getter
public class ResponseApi<T> {

  private String status;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T data;

  public static <T> ResponseApi<T> success(T data) {
    var responseApi = new ResponseApi<T>();
    responseApi.setStatus("success");
    responseApi.setData(data);
    return responseApi;
  }

  public static <T> ResponseApi<T> error(String message, T data) {
    var responseApi = new ResponseApi<T>();
    responseApi.setStatus("error");
    responseApi.setData(data);
    responseApi.setMessage(message);
    return responseApi;
  }

  public static <T> ResponseApi<T> error(String message) {
    return error(message, null);
  }
}
