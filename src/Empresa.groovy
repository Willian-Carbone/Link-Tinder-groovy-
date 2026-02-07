class Empresa extends Usuario{
    String cnpj
    String pais

    Empresa (String nome, String cnpj ,String pais, String cep ,String email, Estados estado,String descricao){
        super (nome,email,cep,estado,descricao)
        this.cnpj=cnpj
        this.pais=pais
    }


}
