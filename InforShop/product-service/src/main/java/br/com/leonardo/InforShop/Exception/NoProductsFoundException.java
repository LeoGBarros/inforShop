package br.com.leonardo.InforShop.Exception;

public class NoProductsFoundException extends RuntimeException {
    public NoProductsFoundException() {
        super("Nenhum produto encontrado.");
    }
}
