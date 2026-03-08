import { nomeEspecialidade, criptografar } from "../codigos_logicos/utilitarios";
import { Vaga } from "../codigos_logicos/classes/vaga";
import { capturar_vagas, capturar_usuario_logado,capturar_matchs, calcular_afinidade_vaga, capturar_dados_usuario} from "../codigos_doom/localStorageControler";
import { Candidato } from "../codigos_logicos/classes/candidato";




const usuarioLogado = capturar_usuario_logado();

const vagas = capturar_vagas();






document.addEventListener("DOMContentLoaded", () => {

    const lista_vagas = document.getElementById("lista_vagas") as HTMLUListElement;

  
    const vagasDisponiveis = vagas.filter(vaga => {

        const jaCurtiu = vaga.curtidas.includes(criptografar(usuarioLogado));

        return !jaCurtiu;

    });


    vagasDisponiveis.forEach(vaga => {

        let afinidade= calcular_afinidade_vaga(vaga,capturar_dados_usuario(usuarioLogado) as Candidato);

        

        const item = criarItemVaga(vaga,afinidade);
        lista_vagas.appendChild(item);

    });


    carregarMatchs();


});



const botaoCurtir = document.getElementById("curtir_vagas") as HTMLButtonElement;

botaoCurtir.addEventListener("click", () => {

    const checkboxes = document.querySelectorAll<HTMLInputElement>(".vaga_checkbox:checked");

    if (checkboxes.length === 0) {
        alert("Selecione ao menos uma vaga");
        return;
    }

    const vagas = capturar_vagas();

    const usuario = capturar_usuario_logado();

    checkboxes.forEach(cb => {

        const nomeVaga = cb.value;

        const vaga = vagas.find(v => v.nome === nomeVaga);

        if (!vaga) return;

        if (!vaga.curtidas.includes(criptografar(usuario))) {
            vaga.curtidas.push(criptografar(usuario));
        }

        
        
        const li = cb.closest("li");
        li?.remove();

    });

    localStorage.setItem("Vagas", JSON.stringify(vagas));

});


function criarItemVaga(vaga: Vaga,afinidade:number): HTMLLIElement {

    const li = document.createElement("li");
    li.className = "vaga_item";

    li.innerHTML = `
        <label class="vaga_label">

            <input 
                type="checkbox"
                class="vaga_checkbox"
                value="${vaga.nome}"
            >

            <div class="vaga_info">
                <h3>Nome: ${vaga.nome}</h3>
                <p>Descrição: ${vaga.descricao}</p>
                <p>Contratante: ${criptografar(vaga.contratante)}</p>
                <p>Requisitos: ${vaga.requisitos.map(nomeEspecialidade).join(", ")}</p>
                <p>Afinidade: ${afinidade}%</p>
            </div>

        </label>
    `;

    return li;
}



function carregarMatchs(){

    const listaMatchs = document.getElementById("lista_matchs") as HTMLUListElement;

    if(!listaMatchs) return;

    listaMatchs.innerHTML = "";

    const matchs = capturar_matchs();

    matchs.forEach(match => {

        if(match.cpf !== usuarioLogado) return;

        const li = document.createElement("li");

        li.textContent =
        `Match com ${criptografar(match.cnpj)} (${match.empresa} , ${match.email_empresa})`;

        listaMatchs.appendChild(li);

    });

}
