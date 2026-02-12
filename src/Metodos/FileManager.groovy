package Metodos

import Enuns.Estados
import Objetos.Candidato
import Objetos.Curtida
import Objetos.Empresa
import Objetos.Vaga
import groovy.json.JsonSlurper
import groovy.json.JsonOutput


abstract class FileManager {
    // Bloco referente a tarefa principal

    static adicionar(Object o, String tipo) {

        File arq = new File("../Dados.json")
        def bancodedados = new JsonSlurper().parse(arq)
        // conversao do arquivo para tipo json sluper , classe inteligente, faz conversoes de map e inserçoes de forma automatizada


        switch (tipo) {
            case "Objetos.Empresa":
                bancodedados.Empresa << o
                break

            case "Objetos.Candidato":
                bancodedados.Candidato << o

        }

        arq.text = JsonOutput.prettyPrint(JsonOutput.toJson(bancodedados))
        // rescreve arquivo realizando conversões (sem buscas/ necessidade de map/ criar array para adicioanr novo objeto)


    }


    static boolean cpf_em_uso(String cpf) {

        File arq = new File("../Dados.json")
        def bancodedados = new JsonSlurper().parse(arq)
        return bancodedados.Candidato.any { it.cpf == cpf }


    }

    static boolean cnpj_em_uso(String cnpj) {

        File arq = new File("../Dados.json")
        def bancodedados = new JsonSlurper().parse(arq)
        return bancodedados.Empresa.any { it.cnpj == cnpj }


    }


    static <T> ArrayList<T> listagem(String opcao) {
        File arq = new File("../Dados.json")
        def bancodedados = new JsonSlurper().parse(arq)
        def dados = bancodedados[opcao] ?: []

        // define qual classe sera  instanciada e seu construtor, necessario poisa  automatização(obejto pra mapa)  utiliza o costrutor vazio, que nas classes nao existem

        def classeAlvo = (opcao == "Objetos.Candidato") ? Candidato.class : Empresa.class
        def construtor = classeAlvo.constructors[0]

        //itera em dados, que é uma lista onde cada item é um mapa de itens da classe atual, retornanando um array list contendo obejtos especiicados

        return (ArrayList<T>) dados.collect { mapa ->


            String estadoRaw = mapa.estado?.toString()?.trim() ?: "" // garante queo estado nunca sera nulo, e sera convertido para tring e tera espaços removidos, remove inconsistencias entre enum e json


            def estadoEnum = null
            if (estadoRaw) {
                String nomeLimpo = Metodos.normalizador(estadoRaw)
                // traduz o estadoraw (arquvio contendo o exato moodo de como esta salvo o dado estado no json, no formato salvo no enum
                if (nomeLimpo) {
                    estadoEnum = Estados.valueOf(nomeLimpo) // cria o objeto estato enum
                }
            }

            def args = []

            // manualmente , constroi cada objeto do tipo especificado

            if (opcao == "Objetos.Candidato") {
                args = [
                        mapa.nome as String,
                        mapa.cpf as String,
                        mapa.idade as Integer,
                        mapa.email as String,
                        mapa.cep as String,
                        estadoEnum,
                        mapa.descricao as String,
                        mapa.competencias as ArrayList<String>
                ]
            } else {
                args = [
                        mapa.nome as String,
                        mapa.cnpj as String,
                        mapa.pais as String,
                        mapa.cep as String,
                        mapa.email as String,
                        estadoEnum,
                        mapa.descricao as String,
                        mapa.competencias as ArrayList<String>
                ]
            }

            return construtor.newInstance(args.toArray())
        }
    }


    //bloco referente ao desafio


    static List listagem_vagas(String contratante) {
        File arq = new File("../Vagas.json")
        def bancodedados = new JsonSlurper().parse(arq)

        def lista = []

        if (contratante.size() == 11) {
            //retorna vagas que o candidato nao curtiu ainda

            String id_criptografado = Metodos.criptografia((contratante))

            lista = bancodedados.vagas.findAll { vaga -> !(vaga.curtidas?.any { curtida -> curtida.identificador == id_criptografado }) }

        } else {
            // retorna somente as vagas do contratante
            lista = bancodedados.vagas.findAll { it.contratante == contratante }

        }

        return lista


    }


    static void adicionar_vaga(Vaga vaga) {
        File arq = new File("../Vagas.json")
        def bancodedados = new JsonSlurper().parse(arq)

        bancodedados.vagas << vaga
        arq.text = JsonOutput.prettyPrint(JsonOutput.toJson(bancodedados))


    }

    static void registrar_curtida(String id_empresa, String id_candidato) {
        File arq_vaga = new File("../Vagas.json")
        def bancodedados_vaga = new JsonSlurper().parse(arq_vaga)

        File arq_registros = new File("../Dados.json")
        def bancodedados_registros = new JsonSlurper().parse(arq_registros)


        def candidato = bancodedados_registros.Candidato.find { it.cpf == id_candidato }

        def infocurtida = [
                identificador: Metodos.criptografia(id_candidato),
                email        : candidato.email,
                competencias : candidato.competencias
        ]


        def vagaAlvo = bancodedados_vaga.vagas.find { it.contratante == id_empresa }
        vagaAlvo.curtidas << infocurtida


        arq_vaga.text = JsonOutput.prettyPrint(JsonOutput.toJson(bancodedados_vaga))


    }

    static void salvar_match(String empresa_id, String candidato_id) {

        File arq = new File("../Dados.json")
        def bancodedados = new JsonSlurper().parse(arq)

        File arq_match = new File ("../Matchs_registrados.json")
        def banco_de_matchs = new JsonSlurper().parse(arq_match)


        def objCandidato = bancodedados.Candidato.find { it.cpf == candidato_id }


        def objEmpresa = bancodedados.Empresa.find { it.cnpj == empresa_id }

        Curtida curtida = new Curtida(candidato:objCandidato, empresa: objEmpresa)

        banco_de_matchs.Match << curtida

        arq_match.text = JsonOutput.prettyPrint(JsonOutput.toJson(banco_de_matchs))





    }

    static List checar_matches(String user_id) {

        File arq_match = new File ("../Matchs_registrados.json")
        def banco_de_matchs = new JsonSlurper().parse(arq_match)

        def lista_de_matchs= banco_de_matchs.Match ?: []

        List resultado=[]

        if (user_id.size()==11) {

            def matches_candidato = lista_de_matchs.findAll { it.candidato.cpf == user_id }

            if (matches_candidato) {
                matches_candidato.each { m ->
                    resultado.add("EMPRESA: ${m.empresa.nome} | CNPJ: ${m.empresa.cnpj} | EMAIL: ${m.empresa.email} | DESCRIÇÃO: ${m.empresa.descricao}")
                }
            }

            else {
                resultado.add("Nenhum match feito até o momento.")
            }

        }

        else{

            def matches_empresa = lista_de_matchs.findAll { it.empresa.cnpj == user_id }

            if (matches_empresa) {
                matches_empresa.each { m ->

                    String cpfCripto = Metodos.criptografia(m.candidato.cpf)
                    resultado.add("CANDIDATO: ${cpfCripto} | EMAIL: ${m.candidato.email} | ESPECIALIDADES: ${m.candidato.competencias}")
                }
            } else {
                resultado.add("Nenhum match feito até o momento.")
            }

        }

        return resultado




    }


}
















