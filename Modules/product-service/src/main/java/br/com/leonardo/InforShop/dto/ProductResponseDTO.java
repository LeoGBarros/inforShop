package br.com.leonardo.InforShop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer stockQuantity;
    private String description;
    private String createdAtFormatted;
}
