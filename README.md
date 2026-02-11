#Autor : Willian Carbone Bueno

--> executando o projeto:

        -execute Main.Groovy

        -é possivel listar candidatos ou empresas que ja estao em dados.json

        -é possivel logar com cpf ou cnpj que ja estejam no mesmo arquivo

        -como candidato é possivel verr vagas e dar curtidas

        -como empresa é possivel responder curtidas e criar vagas



--> Caracteristicas:

    -Um terminal para cada cadastro (candidato e empresa)

    - um terminal de ação para cada usuario (candidato empresa)
    
    - metodos.groovy contem métodos gerais e FileManeger.groovy todos os metodos que necessitam de leitura/escrita no banco de dados (json)
    
    -Persistencia de dados feita pro meio de Dados.json para cadastros, vagas.json para as vagas registradas, e match_registrados.json para matchs confirmados

    - cada curtida possui um id criptografado (encodeBase64) , que caracteriza o usuario que curtiu,mas mantem seu anonimato

--> Tecnologias usadas:

        Groovy e java 25
        
--> Funcionalidades

        - Adicionar cadastros (empresa e candidato)
        
        - como empresa,listar suas vagas , adicionar vagas e curtir candidados que curtiram uma vaga sua
        
        -como candidato, lisatr toas as vagas correntes e curtir vaga
        
        - listar todos os cadastros

