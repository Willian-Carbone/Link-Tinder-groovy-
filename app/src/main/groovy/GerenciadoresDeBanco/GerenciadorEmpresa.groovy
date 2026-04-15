package GerenciadoresDeBanco

import Objetos.Empresa
import Objetos.Match
import groovy.sql.GroovyRowResult
import groovy.sql.Sql

class GerenciadorEmpresa extends GerenciadorBancoTemplate implements CrudPerfilPrincipal<Empresa,String,Object> ,MatchableI,CriadorMatchI{
    GerenciadorEmpresa(Sql sql) {
        super(sql)
    }

    @Override
    Integer criarPerfil(Empresa empresa) {

        try {
            def insercao = sql.executeInsert "INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", [empresa.getCnpj(), empresa.getPais(), empresa.getIdentificador()]
            Integer idRegistrado = insercao[0][2] as Integer

            return idRegistrado
        }
        catch (Exception e) {
            throw new RuntimeException("Não foi possivel inserir o dado : ${e.message}", e)
        }
    }

    @Override
    void removerPerfil(String cnpj) {
        try {

            GroovyRowResult empresa = sql.firstRow("SELECT empresa_id FROM empresa WHERE cnpj = ?", [cnpj])

            if (empresa) {
                Integer idParaDeletar = empresa.empresa_id as Integer

                sql.withTransaction {

                    sql.execute "DELETE FROM matchs WHERE empresa = ?", [cnpj]
                    sql.execute "DELETE FROM especialidade_vaga WHERE vaga IN (SELECT id FROM vaga WHERE contratante = ?) ", [cnpj]
                    sql.execute "DELETE FROM curtida WHERE vaga IN (SELECT id FROM vaga WHERE contratante = ?)", [cnpj]
                    sql.execute "DELETE FROM vaga WHERE contratante = ?", [cnpj]
                    sql.execute "DELETE FROM empresa WHERE empresa_id = ?", [idParaDeletar]
                    sql.execute "DELETE FROM especialidade_usuario WHERE usuario= ?", [idParaDeletar]
                    sql.execute "DELETE FROM usuario WHERE id = ?", [idParaDeletar]
                }
            }
        }

        catch (Exception e) {
            throw new RuntimeException("Nao foi possivel salvar o dado  ${e.message}", e)
        }

    }


    @Override
    void editarPerfil(Object cnpj, String novoPais) {

        try {

            sql.execute "UPDATE empresa SET pais=? WHERE cnpj=?", [novoPais, cnpj]
        }

        catch(Exception e){
            throw  new RuntimeException("Não foi possivel editar o daod ${e.message}",e)
        }

    }

    @Override
    GroovyRowResult capturarInformacoesPerfil(String cnpj) {

        String busca = """
                SELECT u.id,u.nome,u.email,u.cep,u.estado,u.descricao,emp.pais,emp.cnpj, STRING_AGG(eu.especialidade, ', ')  AS competencias
                FROM usuario as u
                JOIN empresa as emp ON u.id=emp.empresa_id
                JOIN especialidade_usuario as eu ON u.id=eu.usuario
                WHERE emp.cnpj=?
                GROUP BY u.id,u.nome, u.email, u.cep, u.estado, u.descricao, emp.pais, emp.cnpj
            """

        try {


            GroovyRowResult infos = sql.firstRow(busca, [cnpj])

            return infos
        }

        catch (Exception e){
            throw new RuntimeException("Nao foi possivel retornar o dado  ${e.message}",e)
        }

    }

    @Override
    Integer capturarId(String cnpj) {
        try {

            GroovyRowResult linhaUsuario = sql.firstRow("SELECT empresa_id AS id FROM empresa WHERE cnpj=?", cnpj)

            Integer idUsuario = linhaUsuario.id as Integer
            return idUsuario
        }

        catch (Exception e) {
            throw new RuntimeException("Nao foi possivel retornar o dado  ${e.message}", e)
        }
    }

    @Override
    List<GroovyRowResult> buscarVagas(String cnpj) {
        try{
            List<GroovyRowResult> vagas = sql.rows "SELECT * FROM vaga WHERE contratante=?", [cnpj]
            return vagas
        }

        catch (Exception e ){
            throw new RuntimeException("Não foi possivel buscar dado ${e.message}", e)
        }
    }

    @Override
    List<GroovyRowResult> buscarMatchs(String cnpj) {
        try {

            String buscaMatchs = """
                SELECT u.nome, u.descricao, u.email, c.idade, u.estado, m.vaga
                FROM usuario AS u
                JOIN candidato AS c ON u.id = c.candidato_id
                JOIN matchs AS m ON m.candidato = c.cpf
                WHERE m.empresa = ?
            """

            List<GroovyRowResult> matchsEncontrados = sql.rows(buscaMatchs, [cnpj])

            return matchsEncontrados
        }

        catch (Exception e) {
            throw new RuntimeException("Nao foi possivel se conectar ao banco ${e.message}", e)
        }
    }

    @Override
    Integer criarMatch(Match match) {

        try {
            def insercao = sql.executeInsert "INSERT INTO matchs (empresa,candidato,vaga) VALUES (?,?,?)", [match.cnpj, match.cpf, match.idVaga]

            Integer idCurtida = insercao[0][0] as Integer


            return idCurtida
        }

        catch (Exception e){
            throw new RuntimeException("Nao foi possivel salvar o dado  ${e.message}",e)
        }
    }


}
