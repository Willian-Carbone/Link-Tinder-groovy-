import Model.Enuns.Estados
import Utilits.LocalizadorDadoEmEnum.LocalizadorDadoEmEnumI
import Utilits.LocalizadorDadoEmEnum.LocalizadorEstadoEmEnum

import spock.lang.Specification


class LocalizadorDadoEmEnumSpec extends Specification{
    def "localizar e retornar estado valido"(){
        given:

        LocalizadorDadoEmEnumI localizadorEstado = new LocalizadorEstadoEmEnum()
        expect:

        localizadorEstado.capturarDadoEnum(ExemploDePossivelEstado) == ValorEsperado

        where:

        ExemploDePossivelEstado | ValorEsperado
        "rio de janeiro" | Estados.RIODEJANEIRO
        "SaÕpÃuLo" | Estados.SAOPAULO
        "StringAleatoria" | null
        "" | null
        null| null
    }


}
