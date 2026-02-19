package Objetos
import Enuns.Especialidades
import Enuns.Estados


class Candidato extends Usuario {
    String cpf
    int idade

    Candidato (String nome, String cpf, int idade, String email, String cep, Estados estado, String descricao, ArrayList<Especialidades> competencias){
        super(nome,email,cep,estado,descricao,competencias)
        this.cpf=cpf
        this.idade=idade
    }


}
