import { ESTADOS, ESPECIALIDADES ,FORMACAO} from "../codigos_logicos/tiposFixos"; 
import { checagem_cpf,checagem_cep, checagem_email,validador_nome } from "../codigos_logicos/utilitarios";
import { pegarDadosFormulario, dispararErro ,limpar_erros, aoMenosUmaCheckboxMarcada} from "./utilidades_domm";
import { checagem_cadastro } from "./localStorageControler";
import {cadastrar_candidato} from "./localStorageControler";



const selectEstado = document.getElementById("estado") as HTMLSelectElement;


ESTADOS.forEach(estado => {
  const option = document.createElement("option");
  option.value = estado.sigla;
  option.textContent = estado.nome;
  selectEstado.appendChild(option);
});


const selectFormacao = document.getElementById("formacao") as HTMLSelectElement;



FORMACAO.forEach(formacao => {

    const option = document.createElement("option");
    option.value = formacao.codigo;
    option.textContent = formacao.nome;
    selectFormacao.appendChild(option);

});





const container = document.getElementById("especialidades_container")!;

ESPECIALIDADES.forEach(esp => {
  const label = document.createElement("label");
  label.style.display = "block"; 

  const input = document.createElement("input");
  input.type = "checkbox";
  input.value = esp.codigo;
  input.name = "especialidade"; 

  label.appendChild(input);
  label.appendChild(document.createTextNode(` ${esp.nome}`));
  container.appendChild(label);
});





const form = document.querySelector("#formulario_cadastro_candidato") as HTMLFormElement;


if(form!= null){


    form.addEventListener("submit", (event) => {
    event.preventDefault(); 

    limpar_erros(form)

    const dados = pegarDadosFormulario(form);

    let mensagem_erro_formato = document.getElementById("cpf_invalido")
    let mensagem_erro_existencia = document.getElementById("cadastro_repetido")
    let mensagem_erro_email = document.getElementById("email_invalido")
    let mensagem_erro_cep = document.getElementById("cep_invalido")
    let mensagem_erro_nome = document.getElementById("nome_invalido")
    let mensagem_erro_especialidade = document.getElementById("especialidade_faltante")
    let mensagem_sucesso = document.getElementById("cadastro_sucesso")
    

    let box_nome= document.getElementById("nome_candidato")
    let box_cpf= document.getElementById("cpf_candidato")
    let box_email= document.getElementById("email_candidato")
    let box_cep= document.getElementById("cep_candidato")



    const container_especialidades = document.getElementById("especialidades_container") as HTMLElement;
    
    

    if (dados.cpf_candidato && dados.email_candidato && dados.cep_candidato && dados.nome_candidato){

        if (!checagem_cpf(dados.cpf_candidato)){
            
            dispararErro(box_cpf as HTMLElement, mensagem_erro_formato as HTMLElement);


        }

        else if(!validador_nome(dados.nome_candidato)){
            
            dispararErro(box_nome as HTMLElement, mensagem_erro_nome as HTMLElement);

        }


        else if(checagem_cadastro(dados.cpf_candidato)){
            
            dispararErro(box_cpf as HTMLElement, mensagem_erro_existencia as HTMLElement);

        }

       else if(!checagem_email(dados.email_candidato)){
            
            dispararErro(box_email as HTMLElement, mensagem_erro_email as HTMLElement);

        }

        else if(!checagem_cep(dados.cep_candidato)){
          
            dispararErro(box_cep as HTMLElement, mensagem_erro_cep as HTMLElement);
        }

        else if(!aoMenosUmaCheckboxMarcada(container_especialidades)){
            dispararErro(container_especialidades, mensagem_erro_especialidade as HTMLElement)
        }

        else{
            mensagem_sucesso?.classList.add("emitido");
            cadastrar_candidato(dados,container_especialidades);


            
            
        }
    }




} );


}


const limpar = document.getElementById("Limpar_form");

limpar?.addEventListener("click", () => {
    limpar_erros(form);
});

