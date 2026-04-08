package Metodos

import Enuns.Especialidades
import Enuns.Estados
import Objetos.Candidato
import Objetos.Empresa
import Objetos.Vaga
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import java.sql.SQLException


class GerenciadorBancoDados {

    private String url = 'jdbc:postgresql://localhost:5432/linkEtinder'
    private String usuario = 'postgres'
    private String senha = '5550178wcb'
    private String driver = 'org.postgresql.Driver'
    private Sql sql


    GerenciadorBancoDados(Sql sql = null) throws SQLException {


        try {
            if (sql) {
                this.sql = sql
            } else {
                this.sql = Sql.newInstance(url, usuario, senha, driver)
            }

            this.sql.connection

        } catch (Exception ignored) {
            throw new SQLException("Não foi possivel se conectar ao Banco de dados")
        }


    }

    private Integer registrarUsuario(String nome, String email, String cep, Estados estado, String descricao, ArrayList<Especialidades> especialidades) {

        def insercao = sql.executeInsert "INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES (?,?,?,?,?)", [nome, email, cep, estado.getSigla(), descricao]
        Integer idGerado = insercao[0][0] as Integer

        especialidades.forEach { especialidade ->
            registrarEspecialidadeUsuario(idGerado, especialidade.name())
        }

        return idGerado


    }


    private void registrarEspecialidadeUsuario(Integer id, String siglaEspecialidade) {
        sql.executeInsert "INSERT INTO especialidade_usuario (especialidade,usuario) VALUES (?,?)", [siglaEspecialidade, id]

    }


    Integer registrarCandidato(Candidato candidato) {

        Integer idCandidato = registrarUsuario(candidato.getNome(), candidato.getEmail(), candidato.getCep(), candidato.getEstado(), candidato.getDescricao(), candidato.getCompetencias())
        sql.executeInsert "INSERT INTO  candidato (cpf,idade,candidato_id) VALUES (?,?,?)", [candidato.getCpf(), candidato.getIdade(), idCandidato]

        return idCandidato
    }


    Integer registrarEmpresa(Empresa empresa) {
        Integer idEmpresa = registrarUsuario(empresa.getNome(), empresa.getEmail(), empresa.getCep(), empresa.getEstado(), empresa.getDescricao(), empresa.getCompetencias())
        sql.executeInsert "INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", [empresa.getCnpj(), empresa.getPais(), idEmpresa]

        return idEmpresa
    }

    Integer registrarVaga(Vaga vaga) {
        def insercao = sql.executeInsert "INSERT INTO vaga (nome,descricao,contratante) VALUES (?,?,?)", [vaga.getNome(), vaga.getDescricao(), vaga.getContratante()]
        Integer idVaga = insercao[0][0] as Integer
        registrarEspecialidadeVaga(idVaga, vaga.getRequisitos())

        return idVaga


    }

    private void registrarEspecialidadeVaga(Integer id, ArrayList<Especialidades> especialidades) {

        especialidades.forEach { especialidade ->

            sql.executeInsert "INSERT INTO especialidade_vaga (vaga,especialidade) VALUES (?,?)", [id, especialidade.name()]


        }


    }

    Integer registrarCurtidaVaga(String cpf, Integer idVaga) {
        GroovyRowResult verificarExistencia = sql.firstRow "SELECT * FROM curtida WHERE candidato=? AND vaga=?", [cpf, idVaga]

        if (verificarExistencia) {
            return null
        }

        def mapa = sql.executeInsert "INSERT INTO curtida (vaga,candidato) VALUES (?,?)", [idVaga, cpf]

        Integer idCurtida = mapa[0][0] as Integer

        return idCurtida


    }

    void registrarMatch(String cpf, String cnpj, Integer idVaga) {
        sql.executeInsert "INSERT INTO matchs (empresa,candidato,vaga) VALUES (?,?,?)", [cnpj, cpf, idVaga]
    }


    boolean cpfEmUso(String cpf) {
        return sql.firstRow("SELECT 1 FROM candidato WHERE cpf = ?", [cpf]) ? true : false
    }

    boolean cnpjEmUso(String cnpj) {
        return sql.firstRow("SELECT 1 FROM empresa WHERE cnpj = ?", [cnpj]) ? true : false
    }

    boolean emailEmUso(String email) {

        GroovyRowResult linha = sql.firstRow("SELECT 1 FROM usuario WHERE email = ? LIMIT 1", [email])
        return linha != null

    }


    List<GroovyRowResult> listagemVagasCandidato(String cpf) {
        String buscaVagas = """
        SELECT 
            v.id AS id_vaga, 
            v.nome, 
            v.descricao, 
            u.id AS id_contratante,
            e.cnpj AS cnpj_empresa,
            STRING_AGG(ev.especialidade, ', ') AS competencias
        FROM 
            vaga v
        JOIN 
            empresa e ON v.contratante = e.cnpj
        JOIN 
            usuario u ON e.empresa_id = u.id
        LEFT JOIN 
            especialidade_vaga ev ON v.id = ev.vaga
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


    List<GroovyRowResult> buscarMatches(String identificador) {

        String buscaMatchs

        if (identificador.length() == 11) {

            buscaMatchs = """
            SELECT u.nome, u.descricao, u.email, emp.pais, u.estado, m.vaga
            FROM usuario AS u
            JOIN empresa AS emp ON u.id = emp.empresa_id
            JOIN matchs AS m ON m.empresa = emp.cnpj
            WHERE m.candidato = ?
            """
        } else {

            buscaMatchs = """
                SELECT u.nome, u.descricao, u.email, c.idade, u.estado, m.vaga
                FROM usuario AS u
                JOIN candidato AS c ON u.id = c.candidato_id
                JOIN matchs AS m ON m.candidato = c.cpf
                WHERE m.empresa = ?
            """

        }

        List<GroovyRowResult> matchsEncontrados = sql.rows(buscaMatchs, [identificador])

        return matchsEncontrados


    }

    void removerPerfil(String identificador) {

        if (identificador.length() == 11) {

            GroovyRowResult candidato = sql.firstRow("SELECT candidato_id FROM candidato WHERE cpf = ?", [identificador])
            Integer idParaDeletar = candidato.candidato_id as Integer

            sql.withTransaction {


                sql.execute "DELETE FROM matchs WHERE candidato = ?", [identificador]
                sql.execute "DELETE FROM curtida WHERE candidato = ?", [identificador]


                sql.execute "DELETE FROM candidato WHERE candidato_id = ?", [idParaDeletar]
                sql.execute "DELETE FROM especialidade_usuario WHERE usuario = ?", [idParaDeletar]
                sql.execute "DELETE FROM usuario WHERE id = ?", [idParaDeletar]
            }


        } else {
            GroovyRowResult empresa = sql.firstRow("SELECT empresa_id FROM empresa WHERE cnpj = ?", [identificador])

            if (empresa) {
                Integer idParaDeletar = empresa.empresa_id as Integer

                sql.withTransaction {

                    sql.execute "DELETE FROM matchs WHERE empresa = ?", [identificador]


                    sql.execute "DELETE FROM especialidade_vaga WHERE vaga IN (SELECT id FROM vaga WHERE contratante = ?) ", [identificador]


                    sql.execute "DELETE FROM curtida WHERE vaga IN (SELECT id FROM vaga WHERE contratante = ?)", [identificador]


                    sql.execute "DELETE FROM vaga WHERE contratante = ?", [identificador]


                    sql.execute "DELETE FROM empresa WHERE empresa_id = ?", [idParaDeletar]
                    sql.execute "DELETE FROM especialidade_usuario WHERE usuario= ?", [idParaDeletar]
                    sql.execute "DELETE FROM usuario WHERE id = ?", [idParaDeletar]
                }
            }
        }


    }

    GroovyRowResult capturarInfos(String identificador) {

        String busca

        if (identificador.length() == 11) {
            busca = """
                SELECT u.id,u.nome,u.email,u.cep,u.estado,u.descricao,c.idade,c.cpf, STRING_AGG(eu.especialidade, ', ')  AS competencias
                FROM usuario as u
                JOIN candidato as c ON u.id=c.candidato_id
                JOIN especialidade_usuario as eu ON u.id=eu.usuario
                WHERE c.cpf=?
                GROUP BY u.id, u.nome, u.email, u.cep, u.estado,u.descricao, c.idade,c.cpf
            """
        } else {
            busca = """
                SELECT u.id,u.nome,u.email,u.cep,u.estado,u.descricao,emp.pais,emp.cnpj, STRING_AGG(eu.especialidade, ', ')  AS competencias
                FROM usuario as u
                JOIN empresa as emp ON u.id=emp.empresa_id
                JOIN especialidade_usuario as eu ON u.id=eu.usuario
                WHERE emp.cnpj=?
                GROUP BY u.id,u.nome, u.email, u.cep, u.estado, u.descricao, emp.pais, emp.cnpj
            """

        }

        GroovyRowResult infos = sql.firstRow(busca, [identificador])

        return infos


    }

    Integer capturarIdUsurio(String identificador) {
        String busca
        if (identificador.length() == 11) {
            busca = "SELECT candidato_id AS id FROM candidato WHERE cpf=?"
        } else {
            busca = "SELECT empresa_id AS id FROM empresa WHERE cnpj=?"
        }

        GroovyRowResult linhaUsuario = sql.firstRow(busca, identificador)

        Integer idUsuario = linhaUsuario.id as Integer
        return idUsuario


    }

    List<GroovyRowResult> buscarPorHabilidades(List<Especialidades> habilidadesBusca, String tipo) {
        List<String> habilidadesFormatadas = habilidadesBusca.collect { it.toString() }
        String placeholders = habilidadesFormatadas.collect { '?' }.join(',')
        Integer total = habilidadesFormatadas.size()


        String tabelaRelacionada = (tipo == 'candidato') ? 'candidato' : 'empresa'
        String idRelacionado = (tipo == 'candidato') ? 'candidato_id' : 'empresa_id'
        String colunaIdentificacao = (tipo == 'candidato') ? 'c.cpf' : 'e.cnpj'
        String alias = (tipo == 'candidato') ? 'c' : 'e'

        String sqlConsulta = """
        SELECT 
            u.id, u.nome, $colunaIdentificacao,
            STRING_AGG(DISTINCT eu.especialidade, ', ') AS habilidades
        FROM usuario u
        JOIN $tabelaRelacionada $alias ON u.id = ${alias}.${idRelacionado}
        JOIN especialidade_usuario eu ON u.id = eu.usuario
        WHERE u.id IN (
            SELECT usuario FROM especialidade_usuario 
            WHERE especialidade IN ($placeholders)
            GROUP BY usuario HAVING COUNT(DISTINCT especialidade) = ?
        )
        GROUP BY u.id, u.nome, $colunaIdentificacao
    """

        return sql.rows(sqlConsulta, habilidadesFormatadas + [total])
    }


    List<GroovyRowResult> capturarVagasDaEmpresa(String cnpj) {
        List<GroovyRowResult> vagas = sql.rows "SELECT * FROM vaga WHERE contratante=?", [cnpj]
        return vagas
    }


    List<Map> capturarCandidatosInteressados(Integer vagaId, String cnpj) {

        List<GroovyRowResult> curtidas = sql.rows("SELECT c.candidato FROM curtida AS c WHERE c.vaga = ? AND NOT EXISTS (SELECT 1 FROM matchs AS m WHERE m.candidato = c.candidato AND m.empresa = ?)", [vagaId, cnpj])

        List<Map> listaCompleta = []


        curtidas.each { linha ->
            String cpfCandidato = linha.candidato
            GroovyRowResult infos = capturarInfos(cpfCandidato)

            listaCompleta << infos

        }

        return listaCompleta
    }


    void trocarNomeDoUsuario(Integer idUsuario, String novoNome) {

        sql.execute "UPDATE usuario SET nome=? WHERE id=?", [novoNome, idUsuario]

    }

    void trocarEmailDoUsuario(Integer idUsuario, String novoEmail) {

        sql.execute "UPDATE usuario SET email=? WHERE id=?", [novoEmail, idUsuario]

    }

    void trocarCepDoUsuario(Integer idUsuario, String novoCep) {

        sql.execute "UPDATE usuario SET cep=? WHERE id=?", [novoCep, idUsuario]

    }

    void trocarEstadoDoUsuario(Integer idUsuario, Estados novoEstado) {

        sql.execute "UPDATE usuario SET estado=? WHERE id=?", [novoEstado.getSigla(), idUsuario]

    }

    void trocarDescricaoDoUsuario(Integer idUsuario, String novaDescricao) {

        sql.execute "UPDATE usuario SET descricao=? WHERE id=?", [novaDescricao, idUsuario]

    }

    void trocarEspecialidadesDoUsuario(Integer idUsuario, ArrayList<Especialidades> especialidades) {

        sql.execute "DELETE FROM especialidade_usuario WHERE usuario = ?", [idUsuario]
        especialidades.forEach { especialidade ->
            registrarEspecialidadeUsuario(idUsuario, especialidade.name())
        }

    }

    void trocarIdadeDoCandidato(String cpf, Integer novaIdade) {
        sql.execute "UPDATE candidato SET idade=? WHERE cpf=?", [novaIdade, cpf]
    }

    void trocarPaisDaEmpresa(String cnpj, String novoPais) {
        sql.execute "UPDATE empresa SET pais=? WHERE cnpj=?", [novoPais, cnpj]
    }

    void trocarNomeDaVaga(Integer idUsuario, String novonome) {

        sql.execute "UPDATE vaga SET nome=? WHERE id=?", [novonome, idUsuario]

    }

    void trocarDescricaoDaVaga(Integer idUsuario, String novadescricao) {

        sql.execute "UPDATE vaga SET descricao=? WHERE id=?", [novadescricao, idUsuario]

    }

    List<GroovyRowResult> buscarCandidatosPorEstado(Estados estado) {
        String sqlConsulta = """
        SELECT 
            u.id, u.nome, c.cpf,
            STRING_AGG(DISTINCT eu.especialidade, ', ') AS habilidades
        FROM usuario u
        JOIN candidato c ON u.id = c.candidato_id
        LEFT JOIN especialidade_usuario eu ON u.id = eu.usuario
        WHERE u.estado = ?
        GROUP BY u.id, u.nome, c.cpf
    """
        return sql.rows(sqlConsulta, [estado.getSigla()])
    }



    List<GroovyRowResult> buscarEmpresasPorEstado(Estados estado) {
        String sqlConsulta = """
        SELECT 
            u.id, u.nome, e.cnpj,
            STRING_AGG(DISTINCT eu.especialidade, ', ') AS habilidades
        FROM usuario u
        JOIN empresa e ON u.id = e.empresa_id
        LEFT JOIN especialidade_usuario eu ON u.id = eu.usuario
        WHERE u.estado = ?
        GROUP BY u.id, u.nome, e.cnpj
    """
        return sql.rows(sqlConsulta, [estado.getSigla()])
    }



    Integer removerVaga(Integer idDaVagaRemovida){
        sql.withTransaction {
            sql.execute "DELETE FROM especialidade_vaga WHERE vaga = ?", [idDaVagaRemovida]
            sql.execute "DELETE FROM curtida WHERE vaga = ?", [idDaVagaRemovida]
            sql.execute "DELETE FROM matchs WHERE vaga = ?", [idDaVagaRemovida]
            sql.execute "DELETE FROM vaga WHERE id = ?", [idDaVagaRemovida]



        }

        return idDaVagaRemovida


    }


}