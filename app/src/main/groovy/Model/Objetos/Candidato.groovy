package Model.Objetos
import Model.Enuns.*
import Model.Objetos.Usuario


class Candidato extends Usuario {
    String cpf
    Integer idade


    Candidato (String nome, String cpf, Integer idade, String email, String cep, Estados estado, String descricao, ArrayList<Especialidades> competencias){
        super(nome,email,cep,estado,descricao,competencias)
        this.cpf=cpf
        this.idade=idade

    }


}
