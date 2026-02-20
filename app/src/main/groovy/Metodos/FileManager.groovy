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


    static Map processarAdicao(Map bancoDados, Object o, String tipo){

        switch (tipo) {
            case "Objetos.Empresa":
                bancoDados.Empresa << o
                break
            case "Objetos.Candidato":
                bancoDados.Candidato << o
        }
        return bancoDados

    }

    // partição do metodo adicionar em 2 (adicionar + porcessar adição), para manter a idea de teste unitario

    static adicionar(Object o, String tipo ,String nomeArquivo= "Dados.json") {


        File arq = new File(nomeArquivo)
        // conversao do arquivo para tipo json sluper , classe inteligente, faz conversoes de map e inserçoes de forma automatizada
        Map bancodedados = new JsonSlurper().parse(arq)

        bancodedados = processarAdicao(bancodedados,o,tipo)


        arq.text = JsonOutput.prettyPrint(JsonOutput.toJson(bancodedados))
        // rescreve arquivo realizando conversões (sem buscas/ necessidade de map/ criar array para adicioanr novo objeto)


    }


    // de modo similar , todos os arquivos de checagem foram modificados para separar criação do mapa e checagem

    static boolean checagem(String valor , Map zonadebusca ){

        if(valor ==null){return false}

        boolean noCandidato = zonadebusca.Candidato.any { it.cpf == valor }
        boolean naEmpresa = zonadebusca.Empresa.any { it.cnpj == valor }

        return noCandidato || naEmpresa


    }

    static boolean cpf_em_uso(String cpf , String nomeArquivo= "Dados.json") {


        File arq = new File(nomeArquivo)

        Map bancodedados = new JsonSlurper().parse(arq)

        return checagem(cpf,bancodedados)


    }


    static boolean cnpj_em_uso(String cnpj ,String nomeArquivo= "Dados.json") {



        File arq = new File(nomeArquivo)

        Map bancodedados = new JsonSlurper().parse(arq)

        return checagem(cnpj,bancodedados)


    }


    // metodo listagem passou pelo mesmo processo listagem + ConstrucaoDoObjeto


    static <T> ArrayList<T> ConstrucaoDoObjeto(Map listaDeMapas, String opcao) {

        List dados = listaDeMapas[opcao] ?: []

        if (!dados) return []


        def classeAlvo = (opcao == "Candidato") ? Candidato.class : Empresa.class

        return (ArrayList<T>) dados.collect { mapa ->

            //correção para erro BOOM

            String estadoRaw = mapa.estado?.toString()?.trim() ?: ""
            def estadoEnum = null
            if (estadoRaw) {
                String nomeLimpo = Metodos.normalizador(estadoRaw)
                if (nomeLimpo) estadoEnum = Estados.valueOf(nomeLimpo)
            }
            // define qual classe sera instanciada e seu construtor, necessario poisa automatização(obejto pra mapa) utiliza o costrutor vazio, que nas classes nao existem


            //itera em dados, que é uma lista onde cada item é um mapa de itens da classe atual, retornanando um array list contendo obejtos especiicados

            def args = (opcao == "Candidato") ? [
                    mapa.nome as String, mapa.cpf as String, mapa.idade as Integer,
                    mapa.email as String, mapa.cep as String, estadoEnum,
                    mapa.descricao as String, mapa.competencias as ArrayList<String>
            ] : [
                    mapa.nome as String, mapa.cnpj as String, mapa.pais as String,
                    mapa.cep as String, mapa.email as String, estadoEnum,
                    mapa.descricao as String, mapa.competencias as ArrayList<String>
            ]

            def construtor = classeAlvo.constructors.find { it.parameterCount == args.size() }
            return construtor.newInstance(args.toArray())
        }
    }

    static <T> ArrayList<T> listagem(String opcao, String nomeArquivo = "Dados.json") {
        File arq = new File(nomeArquivo)
        Map bancodedados = new JsonSlurper().parse(arq)

        return ConstrucaoDoObjeto(bancodedados, opcao)
    }








    //bloco referente ao desafio

    // divisão de funçoes (logica-listagem , listagem-vaga)

    static List logica_listagem(String contratante, Map dados){


        def lista = []

        if (contratante.size() == 11) {
            //retorna vagas que o candidato nao curtiu ainda

            String id_criptografado = Metodos.criptografia((contratante))

            lista = dados.vagas.findAll { vaga -> !(vaga.curtidas?.any { curtida -> curtida.identificador == id_criptografado }) }

        } else {
            // retorna somente as vagas do contratante
            lista = dados.vagas.findAll { it.contratante == contratante }

        }

        return lista


    }


    static List listagem_vagas(String contratante , String nomeArquivo= "Vagas.json") {



        File arq = new File( nomeArquivo)


        Map bancodedados = new JsonSlurper().parse(arq)

       def retorno = logica_listagem(contratante , bancodedados)

        return retorno



    }




    // diviso de tarefas (adicionar_vaga, logicaAdicao)

    static Map logicAdicao(Vaga vaga, Map VagasCadastradas){

        VagasCadastradas.vagas << vaga

        return  VagasCadastradas

    }



    static void adicionar_vaga(Vaga vaga ,String nomeArquivo= "Vagas.json") {



        File arq = new File(nomeArquivo)


        Map bancodedados = new JsonSlurper().parse(arq)


        bancodedados = logicAdicao(vaga, bancodedados)

        arq.text = JsonOutput.prettyPrint(JsonOutput.toJson(bancodedados))


    }







    //separação registrar curtida + logicaRegistroCurtida

    static Map logicaRegistroCurtida(Map dadosVagas, Map dadosUsuarios, String id_empresa, String id_candidato){

        def candidato = dadosUsuarios.Candidato.find { it.cpf == id_candidato }

        def infocurtida = [
                identificador: Metodos.criptografia(id_candidato),
                email        : candidato.email,
                competencias : candidato.competencias
        ]


        def vagaAlvo = dadosVagas.vagas.find { it.contratante == id_empresa }
        vagaAlvo.curtidas << infocurtida

        return dadosVagas

    }


    static void registrar_curtida(String id_empresa, String id_candidato , String nomeArquivoDados= "Dados.json", String nomeArquivosVagas = "Vagas.json") {



        File arq_vaga= new File(nomeArquivosVagas)


        Map bancodedados_vaga = new JsonSlurper().parse(arq_vaga)



        File arq_registros = new File(nomeArquivoDados)


        Map bancodedados_registros = new JsonSlurper().parse(arq_registros)

       bancodedados_vaga = logicaRegistroCurtida(bancodedados_vaga,bancodedados_registros,id_empresa,id_candidato)



        arq_vaga.text = JsonOutput.prettyPrint(JsonOutput.toJson(bancodedados_vaga))


    }






    // divisao tarefas salvar_match + logicaCriarMatch

    static Map logicaCriarMatch(Map DadosUsuarios, Map DadosMatchs, String empresa_id, String candidato_id){

        def objCandidato = DadosUsuarios.Candidato.find { it.cpf == candidato_id }


        def objEmpresa = DadosUsuarios.Empresa.find { it.cnpj == empresa_id }

        Curtida curtida = new Curtida(candidato:objCandidato, empresa: objEmpresa)

        DadosMatchs.Match << curtida

        return DadosMatchs
    }


    static void salvar_match(String empresa_id, String candidato_id, String nomeArquivoDados= "Dados.json", String nomeArquivoMatchs= "Matchs_registrados.json") {



        File arq_registros = new File(nomeArquivoDados)

        Map bancodedados = new JsonSlurper().parse(arq_registros)


        File arq_match = new File(nomeArquivoMatchs)
        Map banco_de_matchs = new JsonSlurper().parse(arq_match)


        banco_de_matchs= logicaCriarMatch(bancodedados,banco_de_matchs, empresa_id,candidato_id)



        arq_match.text = JsonOutput.prettyPrint(JsonOutput.toJson(banco_de_matchs))





    }

    //divisao tarefas checar_matchs + logicaChecagemMatchs

    static List logicaChecagemMatchs(Map bancoMatchs, String user_id){

        def lista_de_matchs= bancoMatchs.Match ?: []

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



    static List checar_matches(String user_id , String nomeArquivoMatch = "Matchs_registrados.json") {



        File arq_match = new File(nomeArquivoMatch)


        def banco_de_matchs = new JsonSlurper().parse(arq_match)

        List resultado =logicaChecagemMatchs(banco_de_matchs, user_id)

       return resultado



    }


}
















