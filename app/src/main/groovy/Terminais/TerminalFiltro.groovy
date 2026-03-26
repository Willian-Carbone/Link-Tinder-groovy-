package Terminais

import Enuns.Especialidades
import Enuns.Estados
import Metodos.GerenciadorBancoDados
import Metodos.Utilidades
import groovy.sql.GroovyRowResult

class TerminalFiltro {

    static filtragem(String identificador, GerenciadorBancoDados gbd, Scanner scan){

        println("Digite 1 para filtar os resultados por estado ou 2 para filtra-los pelas suas habilidades ")
        String decisao= scan.nextLine()

        while(decisao!="1" && decisao!="2"){
            println("Informe uma entrada valida")
            decisao=scan.nextLine()
        }

        if(decisao=="1"){

            println("insira o estado")
            String possivelEstado = scan.nextLine()

            while(Utilidades.normalizador(possivelEstado)==null) {
                println("Insira um estado existente")
                possivelEstado = scan.next()
            }

            Estados estadoConfirmado = Estados.valueOf(Utilidades.normalizador(possivelEstado))

            List<GroovyRowResult> usuariosNoEstado


            if(identificador.length()>11) { usuariosNoEstado=gbd.buscarCandidatosPorEstado(estadoConfirmado)}
            else{  usuariosNoEstado=gbd.buscarEmpresasPorEstado(estadoConfirmado)}



            if(usuariosNoEstado){


                println("Foram encontrados os seguintes perfis")

                usuariosNoEstado.forEach {resultado->
                    println("-------------------------------")
                    println("Numero do perfil: ${resultado.id}")
                    println("Areas atuantes : ${resultado.habilidades}")

                }

            }

            else{
                println("Não foram encontrados perfis para o estado informado")
            }







        }

        else{


            ArrayList<Especialidades> competencias = new ArrayList<>()

            println("informe as habilidades que os perfis obrigatoriamente devem possuir, ao menos uma deve ser informada")

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

            List<GroovyRowResult> usuariosHabilitados

            if(identificador.length()>11){usuariosHabilitados=gbd.buscarCandidatosPorHabilidades(competencias)}
            else{usuariosHabilitados=gbd.buscarEmpresasPorHabilidades(competencias)}




            if(!usuariosHabilitados){println("Não foram enocntrados perfis com as habilidades requeridas")}

            else{
                usuariosHabilitados.forEach {usuario->
                    println("------------------------------------------")
                    println("Numero do perfil: ${usuario.id}")
                    println("Competencias: ${usuario.habilidades}")

                }
            }





        }


    }
}
