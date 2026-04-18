package Modulos.GerenciadoresTerminal.Impressores


import groovy.sql.GroovyRowResult

class ExibicaoMatchEmpresa implements Impressor<List<GroovyRowResult>> {
    @Override
    void exibirDado(List<GroovyRowResult> matchs) {

        if (matchs) {
            println("Os seguintes candidatos possuem um match registardo com sua empresa")
            println("---------------------------")

            for (match in matchs) {

                println("Nome do candidato:${match.nome}")
                println("Descricao do candidato:${match.descricao}")
                println("Email de contato:${match.email}")
                println("idade:${match.idade}")
                println("Estado de atuação:${match.estado}")
                println("Identificador da vaga onde ocorreu o match:${match.vaga}")
                println("---------------------------")

            }
        } else {

            println("Não foram encontrados matchs realizados para seu perfil")

        }

    }
}
