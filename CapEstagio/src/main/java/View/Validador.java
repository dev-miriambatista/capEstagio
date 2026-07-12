package View;

import java.util.regex.Pattern;

public class Validador {

    // 1. Validação de E-mail
    public static boolean isEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    // 2. Validação matemática de CNPJ
    public static boolean isCNPJValido(String cnpj) {
        cnpj = cnpj.replaceAll("[^0-9]", "");
        if (cnpj.length() != 14) return false;
        if (cnpj.matches("(\\d)\\1{13}")) return false;
        try {
            int soma = 0, peso = 2;
            for (int i = 11; i >= 0; i--) {
                soma += (cnpj.charAt(i) - 48) * peso;
                peso = (peso == 9) ? 2 : peso + 1;
            }
            int resto = soma % 11;
            char digito1 = (resto < 2) ? '0' : (char) ((11 - resto) + 48);

            soma = 0; peso = 2;
            for (int i = 12; i >= 0; i--) {
                soma += (cnpj.charAt(i) - 48) * peso;
                peso = (peso == 9) ? 2 : peso + 1;
            }
            resto = soma % 11;
            char digito2 = (resto < 2) ? '0' : (char) ((11 - resto) + 48);

            return (cnpj.charAt(12) == digito1) && (cnpj.charAt(13) == digito2);
        } catch (Exception e) {
            return false;
        }
    }

    // 3. Validação de telefone — aceita (XX) XXXXX-XXXX ou (XX) XXXX-XXXX
    public static boolean isTelefoneValido(String tel) {
        if (tel == null || tel.trim().isEmpty()) return false;
        String apenasDigitos = tel.replaceAll("[^0-9]", "");
        return apenasDigitos.length() == 10 || apenasDigitos.length() == 11;
    }

    // 4. Validação de período — deve ser inteiro entre 1 e 10
    public static boolean isPeriodoValido(String periodo) {
        try {
            int p = Integer.parseInt(periodo.trim());
            return p >= 1 && p <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // 5. Validação de data no formato dd/MM/yyyy
    public static boolean isDataValida(String data) {
        if (data == null || data.trim().isEmpty()) return false;
        try {
            java.time.format.DateTimeFormatter fmt =
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            java.time.LocalDate.parse(data.trim(), fmt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 6. Validação de lista vazia — usada antes de exibir relatórios/listagens
    public static boolean isListaVazia(java.util.List<?> lista) {
        return lista == null || lista.isEmpty();
    }
}