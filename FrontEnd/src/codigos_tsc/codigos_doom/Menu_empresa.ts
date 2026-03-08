import { capturar_usuario_logado, capturar_dados_usuario, capturar_candidatos,cadastrar_vaga,capturar_vagas,  retornar_quantidade_candidatos ,remover_empresa, } from "../codigos_doom/localStorageControler";
import { criptografar , nomeEspecialidade} from "../codigos_logicos/utilitarios";
import { ESPECIALIDADES } from "../codigos_logicos/tiposFixos";
import { pegarDadosFormulario , aoMenosUmaCheckboxMarcada, dispararErro,capturarEspecialidades, ativarHoverCandidato } from "./utilidades_domm";
import  { Vaga} from "../codigos_logicos/classes/vaga";



let usuario_atual = capturar_dados_usuario(capturar_usuario_logado());


declare var Chart: any;
declare var ChartDataLabels: any;



document.addEventListener("DOMContentLoaded", () => {

    const saudacaoElement = document.getElementById("saudacao");

    if (saudacaoElement) {
        saudacaoElement.innerHTML = `Bem vindo ${usuario_atual?.nome || ""}!`;
    }

    const lista_candidatos = document.getElementById("usuarios_listados") as HTMLElement;

    if (lista_candidatos) lista_candidatos.innerHTML = "";

    const candidato_info = document.getElementById("info_usuario") as HTMLElement;

    let candidatos = capturar_candidatos();

    for (let i = 0; i < candidatos.length; i++) {

        const candidato = candidatos[i];

        if (candidato) {

        

            const li = document.createElement("li");

            li.textContent = criptografar(candidato.cpf || "");

            ativarHoverCandidato(li, candidato, candidato_info , usuario_atual as any);

            lista_candidatos.appendChild(li);

           
        }
    }




});

let visualizacao_vagas=document.getElementById("checar_vagas") as HTMLElement;

visualizacao_vagas.addEventListener("click", ()=>{
    window.location.href = "./visualizacao_vaga_empresa.html";
})



const total = retornar_quantidade_candidatos();



document.addEventListener("DOMContentLoaded", () => {

    const dados = localStorage.getItem("candidatos_tecnologias");
    if (!dados) return;

    const tecnologias = JSON.parse(dados);

   
    const labels = Object.keys(tecnologias).map(nomeEspecialidade);

    const valores = Object.values(tecnologias);

    const canvas = document.getElementById("grafico_tecnologias") as HTMLCanvasElement;

    new Chart(canvas, {

        type: "bar",

        data: {
            labels: labels,

            datasets: [{
                label: "Candidatos",
                data: valores
            }]
        },

        options: {

            responsive: true,

            plugins: {

                title: {
                    display: true,
                    text: `Tecnologias dos candidatos (Total: ${total})`
                },

                legend: {
                display: false  
                },

                tooltip: {
                    callbacks: {
                        label: function(context: any) {


                        const valor = context.raw;
                        const porcentagem = ((valor / total) * 100).toFixed(1);

                        return `${valor} candidatos (${porcentagem}%)`;
                            
                            

                            
                        }
                    }
                },

                datalabels: {
                    display :false,
                }

            },

            scales: {

                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: "Quantidade de candidatos"
                    }
                },

                x: {
                    title: {
                        display: true,
                        text: "Tecnologias"
                    }
                }

            }

        },

        plugins: [ChartDataLabels]

    });

});


const botaoAbrir = document.getElementById("criar_vaga");
const modal = document.getElementById("modal_vaga") as HTMLElement;
const form_vaga = document.getElementById("form_vaga") as HTMLFormElement;
const erroEspecialidade = document.getElementById("erro_especialidade") as HTMLElement;
const containerEspecialidades = document.getElementById("especialidades_vaga") as HTMLElement;


botaoAbrir?.addEventListener("click", () => {

    modal.style.display = "flex";

});


modal.addEventListener("click", (event) => {

    if (event.target === modal) {

        modal.style.display = "none";

    }

});





ESPECIALIDADES.forEach(esp => {

    const label = document.createElement("label");

    const input = document.createElement("input");

    input.type = "checkbox";
    input.value = esp.codigo;
    input.name = "especialidade";

    label.appendChild(input);
    label.appendChild(document.createTextNode(" " + esp.nome));

    containerEspecialidades.appendChild(label);

});



form_vaga.addEventListener("submit", (event) => {

    event.preventDefault();

    if (!aoMenosUmaCheckboxMarcada(containerEspecialidades)) {

        dispararErro(containerEspecialidades, erroEspecialidade);
        return;

    }

    erroEspecialidade.style.display = "none";

    const dados = pegarDadosFormulario(form_vaga);

    const especialidadesSelecionadas = capturarEspecialidades(containerEspecialidades);

    const usuarioLogado = capturar_usuario_logado();

    let vagas = capturar_vagas();



    const vagaDuplicada = vagas.some(vaga => 
        vaga.contratante === usuarioLogado &&
        vaga.nome === dados.nome_vaga &&
        vaga.descricao === dados.descricao_vaga
    );

    if (vagaDuplicada) {
        alert("Você já possui essa vaga. Tente novamente com outras informações.");
        return;
    }

    const novaVaga = new Vaga(
        dados.nome_vaga || "",
        dados.descricao_vaga || "",
        usuarioLogado,
        especialidadesSelecionadas
    );

    

    cadastrar_vaga(novaVaga)

    alert("Vaga cadastrada com sucesso!")

});


document.getElementById("editar_perfil")?.addEventListener("click" , ()=>{
    window.location.href = "./editar_perfil_empresa.html";

})


const botaoRemover = document.getElementById("remover_perfil");
const modalRemover = document.getElementById("modal_remover");
const apagar = document.getElementById("btn_apagar");

if (botaoRemover && modalRemover){

    botaoRemover.addEventListener("click", () => {

        modalRemover.style.display = "flex";

    });

}


modalRemover?.addEventListener("click", (event) => {

    if(modalRemover && event.target === modalRemover){
        modalRemover.style.display = "none";
    }

});


apagar?.addEventListener("click", () => {

    window.location.href = "./index.html";

    remover_empresa(capturar_usuario_logado());



});
