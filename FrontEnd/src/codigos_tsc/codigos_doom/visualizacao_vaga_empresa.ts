import {
capturar_usuario_logado,
capturar_vagas,
capturar_candidatos,
checar_matches_existentes,
salvar_match,
capturar_matchs,
capturar_dados_usuario
} from "../codigos_doom/localStorageControler";

import {criptografar,nomeEspecialidade,nomeFormacao} from "../codigos_logicos/utilitarios";

import {Candidato} from "../codigos_logicos/classes/candidato";
import { Empresa } from "../codigos_logicos/classes/empresa";



const listaVagas = document.getElementById("lista_vagas_empresa") as HTMLUListElement;
const listaCandidatos = document.getElementById("lista_candidatos") as HTMLUListElement;
const empresa = capturar_dados_usuario(capturar_usuario_logado()) as Empresa;


const usuarioLogado = capturar_usuario_logado();

const vagas = capturar_vagas();
const candidatos = capturar_candidatos();
const candidatosJaMostrados = new Set<string>();



const minhasVagas = vagas.filter(v => v.contratante === usuarioLogado);




document.addEventListener("DOMContentLoaded",()=>{

    carregarMatchs();

    minhasVagas.forEach(vaga=>{

    const li = document.createElement("li");

    li.innerHTML = `
    <h3>Nome:${vaga.nome}</h3>
    <p>descrição:${vaga.descricao}</p>
    <p>Requisitos: ${vaga.requisitos.map(nomeEspecialidade).join(", ")}</p>
    <p>Curtidas: ${vaga.curtidas.length}</p>
    `;

    listaVagas.appendChild(li);

    });




    minhasVagas.forEach(vaga=>{

        vaga.curtidas.forEach(curtidaCpf=>{

            const candidato = candidatos.find(
            c => criptografar(c.cpf) === curtidaCpf
            );

            if(!candidato) return;

        
            if(candidatosJaMostrados.has(candidato.cpf)){
                return;
            }

            
            if (checar_matches_existentes(candidato.cpf,usuarioLogado)){
                return;
            }

            candidatosJaMostrados.add(candidato.cpf);

            const li = criarItemCandidato(candidato);

            listaCandidatos.appendChild(li);

        });

    });

});






const botaoMatch = document.getElementById("curtir_candidatos") as HTMLButtonElement;

botaoMatch.addEventListener("click",()=>{

    const selecionados = document.querySelectorAll<HTMLInputElement>(".candidato_checkbox:checked");

    if(selecionados.length===0){
        alert("Selecione ao menos um candidato");
        return;
    }

    const listaMatchs = document.getElementById("lista_matchs") as HTMLUListElement;


    if(!empresa) return;

    selecionados.forEach(cb=>{

        const cpf = cb.value;

        const candidato = candidatos.find(c => c.cpf === cpf);

        if(!candidato) return;

        if(checar_matches_existentes(candidato.cpf, empresa.cnpj)){
            return;
        }

        salvar_match(
            candidato.cpf,
            candidato.nome,
            candidato.email,
            empresa.cnpj,
            empresa.nome,
            empresa.email
        );

        carregarMatchs();

    });

});


function carregarMatchs(){

    const listaMatchs = document.getElementById("lista_matchs") as HTMLUListElement;

    if(!listaMatchs) return;

    listaMatchs.innerHTML = "";

    const matchs = capturar_matchs();

    matchs.forEach(match => {

        if(match.cnpj !== usuarioLogado) return;

        const li = document.createElement("li");

        li.textContent =
        `Match com ${criptografar(match.cpf)} (${match.candidato} , ${match.email_candidato})`;

        listaMatchs.appendChild(li);

    });

}


function criarItemCandidato(candidato:Candidato):HTMLLIElement{

    const li = document.createElement("li");

    li.innerHTML=`

    <label>

    <input
    type="checkbox"
    class="candidato_checkbox"
    value="${candidato.cpf}"
    >

    ${criptografar(candidato.cpf)}

    </label>

    `;




    const infoBox = document.getElementById("info_usuario") as HTMLElement;

    li.addEventListener("mouseenter",()=>{

    const habilidades = candidato.competencias.map(nomeEspecialidade).join(", ");

    const formacao = nomeFormacao(candidato.formacao);

    infoBox.style.display="block";

    infoBox.innerHTML=`

    Email: ${candidato.email}<br>
    Formação: ${formacao}<br>
    Habilidades: ${habilidades}

    `;

    });


    li.addEventListener("mousemove",(e)=>{

    infoBox.style.left = e.pageX + 10 + "px";
    infoBox.style.top = e.pageY + 10 + "px";

    });


    li.addEventListener("mouseleave",()=>{

    infoBox.style.display="none";

    });

    return li;

}

