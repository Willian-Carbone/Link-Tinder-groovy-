import { ESTADOS, ESPECIALIDADES } from "../codigos_logicos/tiposFixos"; 
import { checagem_cnpj,checagem_cep, checagem_email } from "../codigos_logicos/utilitarios";
import { pegarDadosFormulario, dispararErro ,limpar_erros, aoMenosUmaCheckboxMarcada} from "./utilidades_domm";
import { checagem_cadastro } from "../codigos_doom/localStorageControler";
import {cadastrar_empresa} from "../codigos_doom/localStorageControler";



const selectEstado = document.getElementById("estado") as HTMLSelectElement;


ESTADOS.forEach(estado => {
  const option = document.createElement("option");
  option.value = estado.sigla;
  option.textContent = estado.nome;
  selectEstado.appendChild(option);
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





const form = document.querySelector("#formulario_cadastro_empresa") as HTMLFormElement;


if(form!= null){

    form.addEventListener("submit", (event) => {
    event.preventDefault(); 

    limpar_erros(form)

    const dados = pegarDadosFormulario(form);

    let mensagem_erro_formato = document.getElementById("cnpj_invalido")
    let mensagem_erro_existencia = document.getElementById("cadastro_repetido")
    let mensagem_erro_email = document.getElementById("email_invalido")
    let mensagem_erro_cep = document.getElementById("cep_invalido")
    let mensagem_erro_especialidade = document.getElementById("especialidade_faltante")
    let mensagem_sucesso = document.getElementById("cadastro_sucesso")


    let box_cnpj= document.getElementById("cnpj_empresa")
    let box_email= document.getElementById("email_empresa")
    let box_cep= document.getElementById("cep_empresa")



    const container_especialidades = document.getElementById("especialidades_container") as HTMLElement;
    
    

    if (dados.cnpj_empresa && dados.email_empresa && dados.cep_empresa ){

        if (!checagem_cnpj(dados.cnpj_empresa)){
            
            dispararErro(box_cnpj as HTMLElement, mensagem_erro_formato as HTMLElement);


        }

        else if(checagem_cadastro(dados.cnpj_empresa)){
            
            dispararErro(box_cnpj as HTMLElement, mensagem_erro_existencia as HTMLElement);

        }

       else if(!checagem_email(dados.email_empresa)){
            
            dispararErro(box_email as HTMLElement, mensagem_erro_email as HTMLElement);

        }

        else if(!checagem_cep(dados.cep_empresa)){
          
            dispararErro(box_cep as HTMLElement, mensagem_erro_cep as HTMLElement);
        }

        else if(!aoMenosUmaCheckboxMarcada(container_especialidades)){
            dispararErro(container_especialidades, mensagem_erro_especialidade as HTMLElement)
        }

        else{
            mensagem_sucesso?.classList.add("emitido");
            cadastrar_empresa(dados,container_especialidades);
            
        }
    }


        

} );

}






const limpar = document.getElementById("Limpar_form");

limpar?.addEventListener("click", () => {
    limpar_erros(form);
});

