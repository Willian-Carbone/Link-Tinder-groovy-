

import Enuns.Estados
import Modulos.LocalizadorDadoEmEnum.*
import spock.lang.Specification


class LocalizadorDadoEmEnumSpec extends Specification{
    def "localizar e retornar estado valido"(){
        given:

        LocalizadorDadoEnumI localizadorEstado = new LocalizadorEstado()
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
