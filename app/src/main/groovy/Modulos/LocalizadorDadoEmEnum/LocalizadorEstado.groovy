package Modulos.LocalizadorDadoEmEnum
import Enuns.Estados
import java.text.Normalizer

class LocalizadorEstado implements LocalizadorDadoEnumI<Estados> {
    @Override
    Estados capturarDadoEnum(String estadoBuscado) {

        if(!estadoBuscado){return null}

        String normalizado = Normalizer.normalize(estadoBuscado,Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+","").replaceAll(" ","").toUpperCase()

        return Estados.find { Estados it-> it.name()==normalizado} as Estados





    }
}
