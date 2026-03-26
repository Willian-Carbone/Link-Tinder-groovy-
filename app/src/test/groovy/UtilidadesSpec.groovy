
import spock.lang.Specification

import Metodos.Utilidades

import java.awt.image.AbstractMultiResolutionImage

class UtilidadesSpec extends Specification {

    def "Teste para validar  formato do Cpf" (){
        expect:

        Utilidades.validadorCpf(ExemploCpf) == ValorEsperado

        where:

        ExemploCpf | ValorEsperado
        "12345678901" | true
        "123.456.789-02" | true
        "123" | false
        "123.123.123-ab" | false
        "" | false
        null| false

    }


    def "Teste para validar formato do Cnpj" (){
        expect:

        Utilidades.validadorCnpj(ExemploCnpj) == ValorEsperado

        where:

        ExemploCnpj | ValorEsperado
        "12.345.678/9012-34" | true
        "12345678901234" | true
        "123" | false
        "12.345.678/9012-ab" | false
        "" | false
        null| false
    }

    def "Teste para validar formato do Cep" (){
        expect:

        Utilidades.validadorCep(ExemploCep) == ValorEsperado

        where:

        ExemploCep | ValorEsperado
        "12345-678" | true
        "12345678" | true
        "123" | false
        "1234567a" | false
        "" | false
        null| false
    }

    def "Teste para validar formato do Email" (){
        expect:

        Utilidades.validadorEmail(ExemploEmail) == ValorEsperado

        where:

        ExemploEmail | ValorEsperado
        "meuemail@gmail.com" | true
        "asdasd%-75@Az90.Az" | true
        "123@" | false
        "1234567a@a.a" | false
        "" | false
        null| false
    }

    def "Teste de metodo que remove nao digitos"(){

        expect:

        Utilidades.padronizaEntrada(
                ExemploEntrada) == ValorEsperado

        where:

        ExemploEntrada| ValorEsperado
        "asdhs2asdha" | "2"
        "%¨&*()#@!23%#@!&*><" | "23"
        ""|""
        null | "Dado não informado"



    }

    def "Checar se a competencia existe no arquivo especialidades"(){



        expect:

        Utilidades.checarCompetencia(ExemploCompetencia) == ValorEsperado

        where:

        ExemploCompetencia | ValorEsperado
        "PT" | true
        "JAV" | true
        "ANG" | true
        "SPR" | true
        "HT" | true
        "CS" | true
        "qualquer outro valor" | false
        "" | false
        null| false

    }

    def " Teste de existencia de estado + retorno de entrada normalizada(Sem Acentos , sem espaçamentos e em caixa alta)"(){
        expect:

        Utilidades.normalizador(ExemploDePossivelEstado) == ValorEsperado

        where:


        ExemploDePossivelEstado | ValorEsperado
        "rio de janeiro" | "RIODEJANEIRO"
        "SaÕpÃuLo" | "SAOPAULO"
        "StringAleatoria" | null
        "" | null
        null| null


    }

    def "Teste verificação idade minima"(){
        expect:
        Utilidades.validadorIdade(PossivelIdade) == ValorEsperado

        where:

        PossivelIdade|ValorEsperado
        "17" | false
        "18"| true
        "19" | true
        "String"|false
        null|false
    }

    def "Teste nome valido"(){
        expect:

        Utilidades.validadorNome(possivelNome)==valorEperado

        where:
        possivelNome|valorEperado
        "João da silva" | true
        "j0ao"| false
        "12345"| false
        "a"|false
        "jo"| false
        "jo p"|true
        "  "|false
        null|false
    }

    def "Teste de confirmação"() {
        given:

        def scan = new Scanner("${entrada}\n")

        expect:
        Utilidades.confirmacao("Deseja continuar?", scan) == esperado

        where: "os cenários de teste são:"
        entrada   | esperado
        "S"       | true
        "s"       | true
        "N"       | false
        "n"       | false

    }



}


