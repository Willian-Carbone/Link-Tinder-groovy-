package GerenciadoresDeBanco

import Objetos.Vaga
import groovy.sql.GroovyRowResult
import groovy.sql.Sql

class GerenciadorVaga extends GerenciadorBancoTemplate{

    GerenciadorVaga(Sql sql) {
        super(sql)
    }

    Integer gravarVaga(Vaga vaga) {
        try{
            def insercao = sql.executeInsert "INSERT INTO vaga (nome,descricao,contratante) VALUES (?,?,?)", [vaga.getNome(), vaga.getDescricao(), vaga.getContratante()]
            Integer idVaga = insercao[0][0] as Integer

            vaga.requisitos.forEach {
                sql.executeInsert("INSERT INTO especialidade_vaga (vaga,especialidade) VALUES (?,?)",[idVaga,it.name()])
            }

            return idVaga

        }
        catch (Exception e){
            throw new RuntimeException("não foi possivel inserir o dado : ${e.message}",e)
        }
    }


    void trocarNomeDaVaga(Integer idVaga, String novonome) {

        sql.execute "UPDATE vaga SET nome=? WHERE id=?", [novonome, idVaga]

    }


    void trocarDescricaoDaVaga(Integer idVaga, String novadescricao) {

        sql.execute "UPDATE vaga SET descricao=? WHERE id=?", [novadescricao, idVaga]

    }

    void removerVaga(Integer idVaga){
        sql.execute("DELETE FROM especialidade_vaga  WHERE vaga=?",[idVaga])
        sql.execute("DELETE FROM vaga WHERE id=?",[idVaga])

    }



    List<GroovyRowResult> capturarInteressadosNaVaga(Integer vagaId, String cnpj , CrudPerfilPrincipal perfilCandidato) {

        List<GroovyRowResult> curtidas = sql.rows("SELECT c.candidato FROM curtida AS c WHERE c.vaga = ? AND NOT EXISTS (SELECT 1 FROM matchs AS m WHERE m.candidato = c.candidato AND m.empresa = ?)", [vagaId, cnpj])

        List<GroovyRowResult> listaCompleta = []


        curtidas.each { linha ->
            String cpfCandidato = linha.candidato
            GroovyRowResult infos = perfilCandidato.capturarInformacoesPerfil(cpfCandidato)

            listaCompleta << infos

        }

        return listaCompleta
    }



     ArrayList<Integer> capturarIdentificadoresDasVagas(List<GroovyRowResult> vagas) {

        ArrayList<Integer> identificadoresVagas = new ArrayList<>()

        for (vaga in vagas) {
            identificadoresVagas.add(vaga.id_vaga as Integer)
        }

        return identificadoresVagas
    }







}
