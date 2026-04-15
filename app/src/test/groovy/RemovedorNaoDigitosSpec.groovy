

import Modulos.ConversoresEntrada.ConversorDadoI
import Modulos.ConversoresEntrada.RemovedorNaoDigitos

import spock.lang.Specification

class RemovedorNaoDigitosSpec extends Specification{


    def "Teste de removedor não digitos"(){

        given:

        ConversorDadoI conversorParaBanco = new RemovedorNaoDigitos()

        expect: "deve remover nao digitos da string "

        conversorParaBanco.converterDado(exemploEntrada)==valorEsperado

        where:

        exemploEntrada| valorEsperado
        "asdhs2asdha" | "2"
        "%¨&*()#@!23%#@!&*><" | "23"
        ""|""
        null | null



    }
}
