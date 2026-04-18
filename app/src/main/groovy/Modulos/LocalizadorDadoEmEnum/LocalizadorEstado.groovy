package Modulos.LocalizadorDadoEmEnum
import Enuns.Estados
import java.text.Normalizer

class LocalizadorEstado implements LocalizadorDadoEnumI<Estados> {
    @Override
    Estados capturarDadoEnum(String estadoBuscado) {

        if(!estadoBuscado){return null}


        return Estados.find { Estados it-> it.name()==estadoBuscado} as Estados





    }
}
