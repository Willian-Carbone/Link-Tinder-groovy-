package Modulos.validadoresDeEntradas

class ValidadorIdade implements ValidadadorI<String>{
    @Override
    boolean validarDado(String idade) {
        if(!idade.trim().isInteger()){return false}
        return (idade as Integer)>=18
    }
}
