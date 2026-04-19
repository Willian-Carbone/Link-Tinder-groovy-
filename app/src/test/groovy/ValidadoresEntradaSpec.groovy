import Model.validadoresDeEntradas.ValidadadorI
import Model.validadoresDeEntradas.ValidadorCep
import Model.validadoresDeEntradas.ValidadorCnpj
import Model.validadoresDeEntradas.ValidadorCpf
import Model.validadoresDeEntradas.ValidadorEmail
import Model.validadoresDeEntradas.ValidadorIdade
import Model.validadoresDeEntradas.ValidadorNome
import spock.lang.Specification

class ValidadoresEntradaSpec extends Specification{

    def "Teste para validar formato do Cep" (){
        given:

        ValidadadorI validadorCep = new ValidadorCep()

        expect:

        validadorCep.validarDado(ExemploCep) == ValorEsperado

        where:

        ExemploCep | ValorEsperado
        "12345-678" | true

        "12345678" | true
        "123" | false
        "1234567a" | false
        "" | false
        null| false
    }


    def "Teste para validar formato do Cnpj" (){
        given:

        ValidadadorI validadorCnpj = new ValidadorCnpj()

        expect:

        validadorCnpj.validarDado(ExemploCnpj) == ValorEsperado

        where:

        ExemploCnpj | ValorEsperado
        "12.345.678/9012-34" | true
        "12345678901234" | true
        "123" | false
        "12.345.678/9012-ab" | false
        "" | false
        null| false
    }

    def "Teste para validar  formato do Cpf" (){
        given:

        ValidadadorI validadorCpf = new ValidadorCpf()

        expect:

        validadorCpf.validarDado(ExemploCpf)== ValorEsperado
        where:

        ExemploCpf | ValorEsperado
        "12345678901" | true
        "123.456.789-02" | true
        "123" | false
        "123.123.123-ab" | false
        "" | false
        null| false

    }

    def "Teste para validar formato do Email" () {

        given:

        ValidadadorI validadorEmail = new ValidadorEmail()

        expect:

        validadorEmail.validarDado(ExemploEmail) == ValorEsperado

        where:

        ExemploEmail         | ValorEsperado
        "meuemail@gmail.com" | true
        "asdasd%-75@Az90.Az" | true
        "123@"               | false
        "1234567a@a.a"       | false
        ""                   | false
        null                 | false
    }

    def "Teste verificação idade minima"(){

        given:

        ValidadadorI validadorIdade = new ValidadorIdade()

        expect:
        validadorIdade.validarDado(possivelIdade) == ValorEsperado

        where:

        possivelIdade|ValorEsperado
        "17" | false
        "18"| true
        "19"| true
        null|false
    }

    def "Teste para nome valido"(){
        given:

        ValidadadorI validadorNome = new ValidadorNome()

        expect:

        validadorNome.validarDado(possivelNome)==valorEperado

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
