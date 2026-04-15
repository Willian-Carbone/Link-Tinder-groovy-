package Modulos.validadoresDeEntradas

import Enuns.Estados

class ValidadorEstado implements ValidadadorI<String>{
    @Override
    boolean validarDado(String possivelEstado) {
        return Estados.any{it.name()==possivelEstado}
    }
}
