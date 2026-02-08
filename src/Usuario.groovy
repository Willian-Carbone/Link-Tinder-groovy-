import groovy.transform.Canonical

@Canonical

 abstract class Usuario {
    String nome
    String email
    String cep
    Estados estado
    String descricao
    ArrayList<String> competencias
}
