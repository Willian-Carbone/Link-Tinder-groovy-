
import spock.lang.Specification

import Metodos.Metodos

class MetodosSpec extends Specification {

    def "Teste para validar  formato do Cpf" (){
        expect:

        Metodos.validador_cpf(ExemploCpf) == ValorEsperado

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

        Metodos.validador_cnpj(ExemploCnpj) == ValorEsperado

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

        Metodos.validador_cep(ExemploCep) == ValorEsperado

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

        Metodos.validador_email(ExemploEmail) == ValorEsperado

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

        Metodos.padronizar_entrada(ExemploEntrada) == ValorEsperado

        where:

        ExemploEntrada| ValorEsperado
        "asdhs2asdha" | "2"
        "%¨&*()#@!23%#@!&*><" | "23"
        ""|""
        null | "Dado não informado"



    }

    def "Checar se a competencia existe no arquivo especialidades"(){

        // sem necessidade de mock em enuns (final por natureza)

        expect:

        Metodos.checar_competencia(ExemploCompetencia) == ValorEsperado

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

        Metodos.normalizador(ExemploDePossivelEstado) == ValorEsperado

        where:

        // qualquer estado em qualquer grafia, se existir, deve retornar se valor em caixa alta sem espaço, ignora acentuação

        ExemploDePossivelEstado | ValorEsperado
        "rio de janeiro" | "RIODEJANEIRO"
        "SaÕpÃuLo" | "SAOPAULO"
        "StringAleatoria" | "O estado informado não corresponde a um estado válido"
        "" | "O estado informado não corresponde a um estado válido"
        null| null


    }

    def "Teste verificação idade minima"(){
        expect:
        Metodos.validador_idade(PossivelIdade) == ValorEsperado

        where:

        PossivelIdade|ValorEsperado
        "17" | false
        "18"| true
        "19" | true
        "String"|false
        null|false
    }

    def "Criptografia/Descriptografia são funcionais e reversiveis"(){
        given :"entrada de CPF ja no padrão normalizado"

        String CpfNormalizado = "12345678901"


        when: "Passo meu cpf pela criptografia e sua saida pela descriptografia"
        String CpfCriptografado = Metodos.criptografia(CpfNormalizado)
        String RetornoDaDescriptografia = Metodos.descriptografia(CpfCriptografado)

        then: "Saida da descriptografia deve ser identica ao cpf original"

        RetornoDaDescriptografia == CpfNormalizado


    }


}


