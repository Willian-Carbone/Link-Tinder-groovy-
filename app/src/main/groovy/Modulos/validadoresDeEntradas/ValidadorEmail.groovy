package Modulos.validadoresDeEntradas

class ValidadorEmail implements ValidadadorI<String>{
    @Override
    boolean validarDado(String email) {
        return (email ==~ /[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}/)
    }
}
