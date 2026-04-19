package Model.validadoresDeEntradas

class ValidadorIdade implements ValidadadorI<String>{
    @Override
    boolean validarDado(String idade) {
        if(!idade || !idade.trim().isInteger() ) {return false}
        return (idade as Integer)>=18
    }
}
