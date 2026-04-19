package Controller.Cadastros

import Daos.BuscadoresDeInformacao.ConfirmadorExistenciaCpf
import Daos.BuscadoresDeInformacao.ConfirmadorExistenciaEmail
import Daos.*
import Model.Enuns.Especialidades
import Model.Enuns.Estados
import Model.Objetos.Candidato
import Model.Objetos.EspecialidadeUsuario
import Model.validadoresDeEntradas.*
import Utilits.ConversorDados.ConversorParaNomeEnum
import View.Requisitores.RequisidorDeEntradas

import Utilits.ConversorDados.RemovedorNaoDigitos
import groovy.sql.Sql
import Utilits.LocalizadorDadoEmEnum.LocalizadorEstadoEmEnum

class CadastroCandidato implements TerminalCadastro {


    @Override

     void cadastrar (Sql conexao, Scanner scan){

        println "--- Cadastro de Candidato ---"

        GerenciadorUsuario gerenciadorUsuario = new GerenciadorUsuario(conexao)

         String nome =  RequisidorDeEntradas.solicitarDadoBasicoValido("nome",scan,new ValidadorNome())
         String cpf = RequisidorDeEntradas.solicitarCredencialValida(scan ,new ValidadorCpf(),new ConfirmadorExistenciaCpf(conexao),"CPF", new RemovedorNaoDigitos())
        String email = RequisidorDeEntradas.solicitarCredencialValida(scan,new ValidadorEmail(), new ConfirmadorExistenciaEmail(conexao),"EMAIL")
         String desc= RequisidorDeEntradas.solicitarDescricao(scan)
         String idade = RequisidorDeEntradas.solicitarDadoBasicoValido("idade",scan,new ValidadorIdade())
         String cep = RequisidorDeEntradas.solicitarDadoBasicoValido("CEP" ,scan, new ValidadorCep())

        String estadoNome = RequisidorDeEntradas.solicitarDadoBasicoValido( "ESTADO",scan,new ValidadorEstado(), new ConversorParaNomeEnum())
        Estados estado = new LocalizadorEstadoEmEnum().capturarDadoEnum(estadoNome)

        ArrayList<Especialidades> competencias = RequisidorDeEntradas.solicitarConjuntoEspecialidadesValidas(scan)


        Candidato candidato = new Candidato (nome,cpf,Integer.parseInt(idade),email,cep,estado,desc,competencias)


        Integer idGerado = gerenciadorUsuario.criarPerfilUsuario(candidato)

        candidato.identificador=idGerado

        candidato.competencias.forEach {Especialidades especialidade->
            EspecialidadeUsuario especialidadeUsuario = new EspecialidadeUsuario(idGerado,especialidade)
            gerenciadorUsuario.gravarEspecialidadeUsusario(especialidadeUsuario)

        }
        new GerenciadorCandidato(conexao).criarPerfil(candidato)

        println("Candidato cadastrado com sucesso")



    }
}
