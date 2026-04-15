package Modulos.validadoresDeEntradas

class ValidadorCpf implements ValidadadorI<String>{
    @Override
    boolean validarDado(String cpf) {
        return (cpf ==~ /\d{3}\.\d{3}\.\d{3}-\d{2}/ || cpf ==~ /\d{11}/)
    }
}
