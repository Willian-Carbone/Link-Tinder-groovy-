package Model.validadoresDeEntradas

class ValidadorCep implements ValidadadorI<String>{
    @Override
    boolean validarDado(String cep) {
        return cep ==~ /\d{5}-\d{3}/ || cep ==~ /\d{8}/
    }
}
