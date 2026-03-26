package Terminais

import Enuns.Especialidades
import Enuns.Estados
import Metodos.GerenciadorBancoDados
import Metodos.Utilidades
import groovy.sql.GroovyRowResult

class TerminalEdicaoBase {
    static edicao( Integer idUsuario ,GroovyRowResult infosUsuario, GerenciadorBancoDados gbd,Scanner scan) {


        if (Utilidades.confirmacao("Seu nome de usuario atual é ${infosUsuario.nome} , deseja altera-lo?", scan)) {
            println("Digite o novo nome do seu perfil")
            String nome = scan.nextLine()

            while (!Utilidades.validadorNome(nome)) {
                println("Digite um nome de usuario válido")
                nome = scan.nextLine()
            }

            gbd.trocarNomeDoUsuario(idUsuario, nome)
        }

        if (Utilidades.confirmacao("Sua descrição de usuario atual é ${infosUsuario.descricao} , deseja altera-lo?", scan)) {
            println("Digite a nova descrição do seu perfil")
            String descricao = scan.nextLine()
            gbd.trocarDescricaoDoUsuario(idUsuario, descricao)
        }


        if (Utilidades.confirmacao("Seu email cadastrado atualmente é ${infosUsuario.email} , deseja altera-lo?", scan)) {
            println("Digite o novo email do seu perfil")
            String email = scan.nextLine()

            while (!Utilidades.validadorEmail(email) || gbd.emailEmUso(email)) {
                println("Insira um email válido e não utilizado")
                email = scan.nextLine()
            }

            gbd.trocarEmailDoUsuario(idUsuario, email)


        }

        if (Utilidades.confirmacao("Seu CEP cadastrado atualmente é ${infosUsuario.cep} , deseja altera-lo?", scan)) {
            println("Digite o novo cep do seu perfil")
            String cep = scan.nextLine()

            while (!Utilidades.validadorCep(cep)) {
                println("Insira um cep válido")
                cep = scan.nextLine()
            }

            gbd.trocarCepDoUsuario(idUsuario, Utilidades.padronizaEntrada(cep))


        }

        if (Utilidades.confirmacao("Seu estado cadastrado atualmente é ${infosUsuario.estado} , deseja altera-lo?", scan)) {
            println("Digite o novo estado do seu perfil")
            String possivelEstado = scan.nextLine()

            while (Utilidades.normalizador(possivelEstado) == null) {
                println("Insira um estado existente")
                possivelEstado = scan.next()
            }


            Estados estadoConfirmado = Estados.valueOf(Utilidades.normalizador(possivelEstado))

            gbd.trocarEstadoDoUsuario(idUsuario, estadoConfirmado)


        }

        if(Utilidades.confirmacao("As habilidades atualmente no seu perfil sao ${infosUsuario.competencias} deseja altera-las?",scan)){

            ArrayList<Especialidades> competencias = new ArrayList<>()

            println("informe as habilidades que sua empresa busca em um candidato da lista abaixo ao menos uma deve ser selecionada")

            println ("""
                   PT --> phyton
                   JAV --> java
                   Ang --> Angular
                   Spr --> spring
                   HT--> html
                   Cs --> para css
                   Fim --> saida
                                """)

            String especialidade = scan.nextLine().toUpperCase()


            while (especialidade != "FIM" || competencias.size() == 0){


                if (Utilidades.checarCompetencia(especialidade)){

                    Especialidades espec = Especialidades.valueOf(especialidade)
                    if (!competencias.contains(espec)){
                        competencias.add(espec)
                    }
                    especialidade = scan.nextLine().toUpperCase()
                }

                else{

                    println("insira um valor válido")
                    especialidade = scan.nextLine().toUpperCase()

                }

            }
            gbd.trocarEspecialidadesDoUsuario(idUsuario,competencias)
        }
    }
}