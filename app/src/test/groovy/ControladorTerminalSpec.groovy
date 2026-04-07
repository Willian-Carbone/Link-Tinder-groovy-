import Metodos.ControladorTerminal
import spock.lang.Specification

class ControladorTerminalSpec extends  Specification{

    def "Teste de confirmação em terminal"() {
        given:

        def scan = new Scanner("${entrada}\n")

        expect:
        ControladorTerminal.confirmarAcaoEmTerminal("Deseja continuar?", scan) == esperado

        where: "os cenários de teste são:"
        entrada   | esperado
        "S"       | true
        "s"       | true
        "N"       | false
        "n"       | false

    }
}
