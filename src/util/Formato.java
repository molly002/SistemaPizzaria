package util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Métodos auxiliares de formatação e conversão. */
public class Formato {
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Formato() { }

    public static String moeda(BigDecimal v) { return v == null ? "" : MOEDA.format(v); }
    public static String dataHora(LocalDateTime d) { return d == null ? "" : d.format(DATA_HORA); }
    public static String data(LocalDate d) { return d == null ? "" : d.format(DATA); }

    /** Converte texto (aceita vírgula) em BigDecimal; lança IllegalArgumentException se inválido. */
    public static BigDecimal parseValor(String texto) {
        try {
            return new BigDecimal(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido. Use apenas números (ex.: 35,90).");
        }
    }
}
