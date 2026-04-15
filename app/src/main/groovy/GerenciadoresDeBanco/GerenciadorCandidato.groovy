package GerenciadoresDeBanco

import Objetos.Candidato
import Objetos.Curtida
import groovy.sql.GroovyRowResult
import groovy.sql.Sql


class GerenciadorCandidato extends GerenciadorBancoTemplate implements CrudPerfilPrincipal<Candidato,Integer, String>,MatchableI,CurtidorI{

    GerenciadorCandidato(Sql sql) {
        super(sql)
    }

    @Override
    Integer criarPerfil(Candidato candidato) {

        try {
            def insercao = sql.executeInsert ("INSERT INTO  candidato (cpf,idade,candidato_id) VALUES (?,?,?)", [candidato.cpf, candidato.idade, candidato.identificador])
            Integer idGerado = insercao[0][2] as Integer

            return  idGerado
        }
        catch (Exception e) {
            throw new RuntimeException("não foi possivel inserir o dado : ${e.message}", e)
        }
    }


    @Override
    void removerPerfil(String cpf) {
        try {

            GroovyRowResult candidato = sql.firstRow("SELECT candidato_id FROM candidato WHERE cpf = ?", [cpf])
            Integer idParaDeletar = candidato.candidato_id as Integer

            sql.withTransaction {


                sql.execute "DELETE FROM matchs WHERE candidato = ?", [cpf]
                sql.execute "DELETE FROM curtida WHERE candidato = ?", [cpf]


                sql.execute "DELETE FROM candidato WHERE candidato_id = ?", [idParaDeletar]
                sql.execute "DELETE FROM especialidade_usuario WHERE usuario = ?", [idParaDeletar]
                sql.execute "DELETE FROM usuario WHERE id = ?", [idParaDeletar]
            }
        }

        catch (Exception e){
            throw new RuntimeException("Nao foi possivel salvar o dado  ${e.message}",e)
        }

    }

    @Override
    void editarPerfil(String cpf, Integer novaIdade) {

        try {

            sql.execute "UPDATE candidato SET idade=? WHERE cpf=?", [novaIdade, cpf]
        }

        catch (Exception e){
            throw  new RuntimeException("Não foi possive editar o dado ${e.message}",e)
        }

    }

    @Override

    GroovyRowResult capturarInformacoesPerfil(String cpf) {

        String busca = """
                SELECT u.id,u.nome,u.email,u.cep,u.estado,u.descricao,c.idade,c.cpf, STRING_AGG(eu.especialidade, ', ')  AS competencias
                FROM usuario as u
                JOIN candidato as c ON u.id=c.candidato_id
                LEFT JOIN especialidade_usuario as eu ON u.id=eu.usuario
                WHERE c.cpf=?
                GROUP BY u.id, u.nome, u.email, u.cep, u.estado,u.descricao, c.idade,c.cpf
        """
        try {
            GroovyRowResult infos = sql.firstRow(busca, [cpf])

            return infos
        }
        catch (Exception e){
            throw new RuntimeException("Nao foi possivel retornar o dado  ${e.message}",e)
        }
    }


    @Override
    Integer capturarId(String cpf) {

        try {

            GroovyRowResult linhaUsuario = sql.firstRow("SELECT candidato_id AS id FROM candidato WHERE cpf=?", [cpf])
            Integer idUsuario = linhaUsuario.id as Integer
            return idUsuario
        }

        catch (Exception e){
            throw new RuntimeException("Nao foi possivel retornar o dado  ${e.message}",e)
        }
    }


    @Override
    List<GroovyRowResult> buscarVagas(String cpf) {
        try {
            String buscaVagas = """
            SELECT v.id AS id_vaga, v.nome, v.descricao,  u.id AS id_contratante,e.cnpj AS cnpj_empresa,STRING_AGG(ev.especialidade, ', ') AS competencias
            FROM vaga v JOIN  empresa e ON v.contratante = e.cnpj
            JOIN  usuario u ON e.empresa_id = u.id
            LEFT JOIN especialidade_vaga ev ON v.id = ev.vaga
            WHERE 
 
            v.id NOT IN (
                SELECT vaga FROM curtida WHERE candidato = ?
            )
           
            AND e.cnpj NOT IN (
                SELECT empresa FROM matchs WHERE candidato = ?
            )
            GROUP BY 
            v.id, v.nome, v.descricao, u.id, e.cnpj
            """


            return sql.rows(buscaVagas, [cpf, cpf])
        }

        catch (Exception e) {
            throw new RuntimeException("Nao foi possivel se conectar ao banco ${e.message}", e)
        }
    }


    @Override
    List<GroovyRowResult> buscarMatchs(String cpf) {

        try {
            String buscaMatchs = """
            SELECT u.nome, u.descricao, u.email, emp.pais, u.estado, m.vaga
            FROM usuario AS u
            JOIN empresa AS emp ON u.id = emp.empresa_id
            JOIN matchs AS m ON m.empresa = emp.cnpj
            WHERE m.candidato = ?
            """

            List<GroovyRowResult> matchsEncontrados = sql.rows(buscaMatchs, [cpf])

            return matchsEncontrados
        }

        catch (Exception e) {
            throw new RuntimeException("Nao foi possivel se conectar ao banco ${e.message}", e)
        }
    }

    @Override
    Integer curtir(Curtida curtida) {

        try {
            def insercao = sql.executeInsert "INSERT INTO curtida (vaga,candidato) VALUES (?,?)", [curtida.idVaga, curtida.cpf]

            Integer idGerado = insercao[0][0] as Integer

            return idGerado
        }

        catch (Exception e){
            throw new RuntimeException("Nao foi possivel salvar o dado  ${e.message}",e)
        }
    }


}
