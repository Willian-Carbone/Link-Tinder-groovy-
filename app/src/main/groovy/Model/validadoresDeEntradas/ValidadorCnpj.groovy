package Model.validadoresDeEntradas

class ValidadorCnpj implements ValidadadorI<String>{
    @Override
    boolean validarDado(String cnpj) {
        return (cnpj ==~ /\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}/ || cnpj ==~ /\d{14}/)
    }
}
