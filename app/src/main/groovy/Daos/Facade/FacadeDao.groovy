package Daos.Facade

import Model.Objetos.Usuario
import groovy.sql.Sql

interface FacadeDao {

   void  realizarRequisicao(Sql conexo,Usuario user)

}