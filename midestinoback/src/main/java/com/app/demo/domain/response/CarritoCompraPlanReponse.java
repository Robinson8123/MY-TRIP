package com.app.demo.domain.response;

import com.app.demo.domain.dto.CarritoCompraPlanDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoCompraPlanReponse {
    private String message;
    private boolean valid;
    private CarritoCompraPlanDTO carrito;
    @JsonProperty("data")
    private List<CarritoCompraPlanDTO> carritosList;
}
