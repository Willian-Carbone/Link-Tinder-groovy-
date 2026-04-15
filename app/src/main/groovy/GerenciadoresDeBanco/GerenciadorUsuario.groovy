package GerenciadoresDeBanco

import Enuns.Especialidades
import Enuns.Estados
import Objetos.EspecialidadeUsuario
import Objetos.Usuario
import groovy.sql.GroovyRowResult
import groovy.sql.Sql

import java.sql.SQLException

class GerenciadorUsuario extends GerenciadorBancoTemplate{

    GerenciadorUsuario(Sql sql) {
        super(sql)
    }

    Integer criarPerfilUsuario(Usuario usuario) {

        try{

            def insercao = sql.executeInsert "INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES (?,?,?,?,?)", [usuario.nome, usuario.email, usuario.cep, usuario.estado.getSigla(), usuario.descricao]
            Integer idGerado = insercao[0][0] as Integer

            return idGerado
        }

        catch (SQLException  e) {

            throw new RuntimeException("Erro no banco de dados: ${e.message}", e)

        }


    }


    Integer gravarEspecialidadeUsusario(EspecialidadeUsuario especialidadeUsuario) {

        try {
            def insercao = sql.executeInsert "INSERT INTO especialidade_usuario (especialidade,usuario) VALUES (?,?)", [especialidadeUsuario.especialidade.name(), especialidadeUsuario.idUsuario]
            Integer idGerado= insercao[0][0] as Integer
            return idGerado
        }
        catch (SQLException e){
            throw new RuntimeException ("Erro ao salvar arquivo no banco de dados : ${e.message}",e)
        }


    }




    void editarCepUsuario(Integer idUsuario, String novoCep) {

        try{
            sql.execute "UPDATE usuario SET cep=? WHERE id=?", [novoCep, idUsuario]
        }
        catch (Exception e){
            throw new RuntimeException("não foi editar o dado ${e.message}",e)
        }


    }

    void editarDescricaoUsuario(Integer idUsuario, String novaDescricao) {

        try{
            sql.execute "UPDATE usuario SET descricao=? WHERE id=?", [novaDescricao, idUsuario]
        }
        catch (Exception e){
            throw new RuntimeException("Não foi possivel editar o dado ${e.message}" ,e)
        }

    }


    void editarEmailUsuario(Integer idUsuario,String novoEmail) {
        try {
            sql.execute "UPDATE usuario SET email=? WHERE id=?", [novoEmail, idUsuario]
        }
        catch (Exception e){
            throw  new RuntimeException("Não foi possivel editar o Dado ${e.message}",e)
        }
    }

    void editarEstado(Integer idUsuario, Estados novoEstado) {

        try{
            sql.execute "UPDATE usuario SET estado=? WHERE id=?", [novoEstado.getSigla(), idUsuario]
        }

        catch (Exception e){
            throw new RuntimeException("não foi possivel editar o dado ${e.message}" ,e)
        }


    }

    void editarNomeUsuario(Integer id, String novoNome) {

        try{
            sql.execute "UPDATE usuario SET nome=? WHERE id=?", [novoNome, id]
        }

        catch (Exception e){
            throw new RuntimeException("Não foi possivel editar o dado iformado ${e.message}" ,e)
        }


    }

    void editarEspecialidadeDoUsuario(Integer idUsuario, ArrayList<EspecialidadeUsuario> especialidades) {

        sql.execute "DELETE FROM especialidade_usuario WHERE usuario = ?", [idUsuario]
        especialidades.forEach { especialidade ->
            sql.executeInsert "INSERT INTO especialidade_usuario (especialidade,usuario) VALUES (?,?)", [especialidade.especialidade.name(), especialidade.idUsuario]

        }

    }



    List<GroovyRowResult> buscarPorHabilidades(List<Especialidades> habilidadesBusca, String tipoUsuario) {
        List<String> habilidadesFormatadas = habilidadesBusca.collect { it.toString() }
        String placeholders = habilidadesFormatadas.collect { '?' }.join(',')
        Integer total = habilidadesFormatadas.size()

        String tabelaFiltro = tipoUsuario.equalsIgnoreCase("candidato") ? "candidato" : "empresa"
        String fkFiltro = tipoUsuario.equalsIgnoreCase("candidato") ? "candidato_id" : "empresa_id"

        String sqlConsulta = """
        SELECT u.id, u.nome, STRING_AGG(DISTINCT eu.especialidade, ', ') AS habilidades
        FROM usuario u
        JOIN especialidade_usuario eu ON u.id = eu.usuario
        WHERE EXISTS (SELECT 1 FROM ${tabelaFiltro} WHERE ${fkFiltro} = u.id)
        AND u.id IN (
        SELECT usuario FROM especialidade_usuario 
        WHERE especialidade IN ($placeholders)
        GROUP BY usuario HAVING COUNT(DISTINCT especialidade) = ?)
       GROUP BY u.id, u.nome
        """

        return sql.rows(sqlConsulta, habilidadesFormatadas + [total])
    }


    List<GroovyRowResult> buscarPorEstado(Estados estado, String tipoUsuario) {

        String tabelaFiltro =tipoUsuario.equalsIgnoreCase ("candidato") ? "candidato" : "empresa"
        String fkFiltro = tipoUsuario.equalsIgnoreCase("candidato") ? "candidato_id" : "empresa_id"

        String sqlConsulta = """
        SELECT 
            u.id, u.nome, 
            STRING_AGG(DISTINCT eu.especialidade, ', ') AS habilidades
        FROM usuario u
        INNER JOIN ${tabelaFiltro} perfil ON u.id = perfil.${fkFiltro}
        LEFT JOIN especialidade_usuario eu ON u.id = eu.usuario
        WHERE u.estado = ?
        GROUP BY u.id, u.nome
         """



        return sql.rows(sqlConsulta, [estado.getSigla()])
    }


}
