package Modulos.GerenciadoresTerminal.Impressores


import groovy.sql.GroovyRowResult

class ExibicaoVagasEmpresa implements  Impressor<GroovyRowResult> {
    @Override
    void exibirDado(GroovyRowResult dadosVaga) {

        println("*************************************")
        println("Numero da Vaga:${dadosVaga.id}")
        println("Nome da Vaga: ${dadosVaga.nome}")
        println("Descrição da vaga:${dadosVaga.descricao}")
        println("_________________________________________")

    }
}
