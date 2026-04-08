import { checagem_cnpj,checagem_cpf } from "../codigos_logicos/utilitarios";
import { pegarDadosFormulario, dispararErro, limpar_erros } from "./utilidades_domm";
import { checagem_cadastro , setar_usuario} from "./localStorageControler";


const form = document.querySelector("#formulario_login") as HTMLFormElement;




form.addEventListener("submit", (event) => {
    event.preventDefault(); 

    limpar_erros(form)


    

    const dados = pegarDadosFormulario(form);


    let mensagem_erro_entrada = document.getElementById("entrada_invalida")
    let mensagem_erro_inexistencia = document.getElementById("cadastro_inexistente")
    let box_entrada= document.getElementById("entrada_login")
    

    if (dados.entrada){

        if (!checagem_cnpj(dados.entrada) && !checagem_cpf(dados.entrada)){
            
            dispararErro(box_entrada as HTMLElement, mensagem_erro_entrada as HTMLElement);


            
        }

        else{


            if(checagem_cadastro(dados.entrada)){

                setar_usuario(dados.entrada);

                if (dados.entrada.length === 11){
                    window.location.href = "./Menu_candidato.html";
                }

                else{
                    window.location.href = "./Menu_empresa.html";
                }



            }

            else{
                dispararErro(box_entrada as HTMLElement, mensagem_erro_inexistencia as HTMLElement);
            }}


        }
        
    }

 

    
);

