package View.Impressores


import groovy.sql.GroovyRowResult

class ExibicaoMatchCandidato implements Impressor<List<GroovyRowResult>> {

    @Override
    void exibirDado(List<GroovyRowResult> matchs) {

        if (matchs) {
            println("As seguintes empresas curtiram seu perfil")
            println("---------------------------")

            for (match in matchs) {

                println("Nome da empresa:${match.nome}")
                println("Descricao da empresa:${match.descricao}")
                println("Email de contato:${match.email}")
                println("Pais de atuação:${match.pais}")
                println("Estado de atuação:${match.estado}")
                println("Identificador da vaga onde ocorreu o match:${match.vaga}")
                println("---------------------------")

            }
        } else {

            println("Não foram encontrados matchs realizados para seu perfil")

        }

    }
}




