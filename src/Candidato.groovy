class Candidato extends Usuario {
    String cpf
    int idade

    Candidato (String nome,String cpf, int idade, String email, String cep, Estados estado, String descricao){
        super(nome,email,cep,estado,descricao)
        this.cpf=cpf
        this.idade=idade
    }


}
