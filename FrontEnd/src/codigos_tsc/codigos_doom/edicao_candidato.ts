import { ESTADOS, ESPECIALIDADES ,FORMACAO} from "../codigos_logicos/tiposFixos"; 
import { checagem_cep, checagem_email, validador_nome} from "../codigos_logicos/utilitarios";
import { pegarDadosFormulario, dispararErro ,limpar_erros, aoMenosUmaCheckboxMarcada} from "./utilidades_domm";
import { capturar_dados_usuario, capturar_usuario_logado, } from "../codigos_doom/localStorageControler";
import {edicao_candidato} from "../codigos_doom/localStorageControler";
import { Candidato } from "../codigos_logicos/classes/candidato";




const usuario = capturar_usuario_logado()


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

    
    let mensagem_erro_email = document.getElementById("email_invalido")
    let mensagem_erro_cep = document.getElementById("cep_invalido")
    let mensagem_erro_especialidade = document.getElementById("especialidade_faltante")
    let mensagem_erro_nome = document.getElementById("nome_invalido")
    let mensagem_sucesso = document.getElementById("cadastro_sucesso")


    let box_nome= document.getElementById("nome_candidato")
    let box_email= document.getElementById("email_candidato")
    let box_cep= document.getElementById("cep_candidato")



    const container_especialidades = document.getElementById("especialidades_container") as HTMLElement;
    
    

    if ( dados.email_candidato && dados.cep_candidato && dados.nome_candidato){

       


        if(!checagem_email(dados.email_candidato)){
            
            dispararErro(box_email as HTMLElement, mensagem_erro_email as HTMLElement);

        }

        else if(!validador_nome(dados.nome_candidato)){
            
            dispararErro(box_nome as HTMLElement, mensagem_erro_nome as HTMLElement);
            

        }

        else if(!checagem_cep(dados.cep_candidato)){
          
            dispararErro(box_cep as HTMLElement, mensagem_erro_cep as HTMLElement);
        }

        else if(!aoMenosUmaCheckboxMarcada(container_especialidades)){
            dispararErro(container_especialidades, mensagem_erro_especialidade as HTMLElement)
        }

        else{
            mensagem_sucesso?.classList.add("emitido");
            edicao_candidato(dados,container_especialidades, usuario);
            

            
            
        }
    }


        

} );



}

    
const limpar = document.getElementById("Limpar_form");

limpar?.addEventListener("click", () => {
    limpar_erros(form);
});



let candidato = capturar_dados_usuario(capturar_usuario_logado()) as Candidato;



if(candidato){

    (document.getElementById("nome_candidato") as HTMLInputElement).value = candidato.nome;

    (document.getElementById("descricao") as HTMLInputElement).value = candidato.descricao;

    (document.getElementById("idade") as HTMLInputElement).value = String(candidato.idade);

    (document.getElementById("email_candidato") as HTMLInputElement).value = candidato.email;

    (document.getElementById("cep_candidato") as HTMLInputElement).value = candidato.cep;

    (document.getElementById("estado") as HTMLSelectElement).value = candidato.estado;

    (document.getElementById("formacao") as HTMLSelectElement).value = candidato.formacao;


    const checkboxes = document.querySelectorAll(
        "#especialidades_container input[type='checkbox']"
    ) as NodeListOf<HTMLInputElement>;

    checkboxes.forEach(box => {

        if(candidato.competencias.includes(box.value as any)){
            box.checked = true;
        }

    });

}
