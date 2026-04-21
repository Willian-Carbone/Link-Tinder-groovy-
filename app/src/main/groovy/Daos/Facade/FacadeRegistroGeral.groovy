package Daos.Facade


import Daos.GerenciadorUsuario
import Model.Enuns.Especialidades
import Model.Objetos.EspecialidadeUsuario
import Model.Objetos.Usuario
import groovy.sql.Sql

class FacadeRegistroGeral implements FacadeDao{
    @Override
    void realizarRequisicao(Sql conexao, Usuario user) {

        try {

            GerenciadorUsuario gerenciadorBase = new GerenciadorUsuario(conexao)

            Integer idGerado = gerenciadorBase.criarPerfilUsuario(user)

            user.identificador = idGerado

            user.competencias.forEach { Especialidades especialidade ->
                EspecialidadeUsuario especialidadeUsuario = new EspecialidadeUsuario(idGerado, especialidade)
                gerenciadorBase.gravarEspecialidadeUsusario(especialidadeUsuario)

            }


        }
        catch (Exception e){
            throw new RuntimeException("Não foi possivel completar o registro",e)
        }



    }
}
