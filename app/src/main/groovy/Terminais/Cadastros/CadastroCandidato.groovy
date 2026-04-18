package Terminais.Cadastros
import Enuns.*

import GerenciadoresDeBanco.BuscadoresDeInformacao.ConfirmadorExistenciaCpf
import GerenciadoresDeBanco.BuscadoresDeInformacao.ConfirmadorExistenciaEmail
import GerenciadoresDeBanco.GerenciadorCandidato
import GerenciadoresDeBanco.GerenciadorUsuario
import Modulos.ConversoresEntrada.ConversorParaNomeEnum
import Modulos.ConversoresEntrada.RemovedorNaoDigitos
import Modulos.GerenciadoresTerminal.Requisitores.RequisidorDeEntradas
import Modulos.LocalizadorDadoEmEnum.LocalizadorEstado
import Modulos.validadoresDeEntradas.ValidadorCep

import Modulos.validadoresDeEntradas.ValidadorCpf
import Modulos.validadoresDeEntradas.ValidadorEmail
import Modulos.validadoresDeEntradas.ValidadorEstado
import Modulos.validadoresDeEntradas.ValidadorIdade
import Modulos.validadoresDeEntradas.ValidadorNome
import Objetos.Candidato
import Objetos.EspecialidadeUsuario
import groovy.sql.Sql

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
        Estados estado = new LocalizadorEstado().capturarDadoEnum(estadoNome)

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
