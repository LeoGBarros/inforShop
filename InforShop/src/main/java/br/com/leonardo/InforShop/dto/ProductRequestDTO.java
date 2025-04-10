package br.com.leonardo.InforShop.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = false) // Rejeita campos não declarados no DTO
public class ProductRequestDTO {

    @NotBlank(message = "Nome do produto é obrigatório")
    private String name;

    @NotNull(message = "Preço é obrigatório")
    @Min(value = 0, message = "Preço inválido, deve ser maior ou igual a zero")
    private Double price;

    @NotNull(message = "Quantidade em estoque é obrigatória")
    @Min(value = 0, message = "Quantidade no estoque não pode ser menor que zero")
    private Integer stockQuantity;

    @NotBlank(message = "Descrição do produto é obrigatório")
    private String description;
}
