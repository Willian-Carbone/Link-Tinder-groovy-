package Utilits.LocalizadorDadoEmEnum

import Model.Enuns.Estados

import java.text.Normalizer

class LocalizadorEstadoEmEnum implements LocalizadorDadoEmEnumI {
    @Override
    Estados capturarDadoEnum(String estadoBuscado) {

        if(!estadoBuscado ){ return null}

        String normalizado=Normalizer.normalize(estadoBuscado,Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+","").replaceAll(" ","").toUpperCase()

        return Estados.find { Estados it-> it.name()==normalizado} as Estados





    }
}
