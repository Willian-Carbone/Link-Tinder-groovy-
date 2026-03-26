#Autor : Willian Carbone Bueno

--> executando o projeto:

execute [Main.Groovy](app/src/main/groovy/Main.groovy)

um menu em terminal te guiara pelas opções disponiveis

Em paralalelo , tambem é possivel executar [index.html](FrontEnd/src/telas/index.html) , para uma versão em FRONTEND da aplicação

--> Caracteristicas:

    Um sistema MVP que simula uma plataforma de contratações as cegas, com um sistema integrado de registro, matchs ,criação de vagas e curtidas, com CRUD completo de perfis e vagas,
    possuindo cobertura de teste uniários e integração com spock para garantir a integridade e controle dos dados no desenvolvimento do sistema

--> Tecnologias usadas:

-Groovy
-java 21
-Gradle
-Spock
-webcrawler
-html
-css
-Typescrypt
-PostgreSQL
        
--> Funcionalidades

        -Funcionalidade de candidato e empresa:
            CRUD completo de perfil
            listar usuarios da categoria oposto pelos filtros informados 
            listar matchs confirmados

        -Funcionalidaes Empresa
            CRUD completo para vagas
            e curtir candidados que curtiram uma vaga da sua empresa
        
        -Funcionalidade Candidato
            curtir vagas de empresas cadastradas




--> Adesão de modelagem de banco de dados

- O modelo foi desenvolvido utilizando a ferramenta [dbdiagram.io](https://dbdiagram.io/).

- O arquivo de criação das entidades e relacionamentos pode ser encontrado na raiz do projeto: [comandosCriacaoBancoDados.sql](app/src/main/resources/comandosCriacaoBancoDados.sql)

![Diagrama do Banco de Dados](imagensREADME/diagramaBancoDados.png)


    

