
import spock.lang.Specification
import Metodos.Utilidades

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

    def "Deve remover não digitos de uma string"(){

        expect:

        Utilidades.padronizaInformacoesParaBancoDados(
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

        Utilidades.checarSeCompetenciaExiste(ExemploCompetencia) == ValorEsperado

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

    def "Deve retornar o nome do estado (sem espaços+ sem acentos+ caixa Alta) ou nulo"(){
        expect:

        Utilidades.capturarEstado(ExemploDePossivelEstado) == ValorEsperado

        where:


        ExemploDePossivelEstado | ValorEsperado
        "rio de janeiro" | "RIODEJANEIRO"
        "SaÕpÃuLo" | "SAOPAULO"
        "StringAleatoria" | null
        "" | null
        null| null


    }

    def "Deve verificar maior idade"(){
        expect:
        Utilidades.verificacarMaiorIdade(PossivelIdade) == ValorEsperado

        where:

        PossivelIdade|ValorEsperado
        "17" | false
        "18"| true
        "19" | true
        "String"|false
        null|false
    }

    def "deve retorne true para nome valido"(){
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







}


