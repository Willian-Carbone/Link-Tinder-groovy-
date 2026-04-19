package Model.validadoresDeEntradas

import Model.Enuns.Estados

class ValidadorEstado implements ValidadadorI<String>{
    @Override
    boolean validarDado(String possivelEstado) {
        return Estados.any{it.name()==possivelEstado}
    }
}
