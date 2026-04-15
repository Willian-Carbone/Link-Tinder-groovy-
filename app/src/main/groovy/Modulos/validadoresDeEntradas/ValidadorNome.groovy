package Modulos.validadoresDeEntradas

 class ValidadorNome implements ValidadadorI<String>{

    @Override
    boolean validarDado(String nome) {
        return (nome ==~ /(?U)^\p{L}{2,} [\p{L} ]+$/)
    }
}
