import { capturar_usuario_logado, capturar_dados_usuario,  capturar_empresas, retornar_quantidade_empresas,remover_candidato } from "./localStorageControler";
import { criptografar , nomeEspecialidade} from "../codigos_logicos/utilitarios";
import { ativarHoverEmpresa } from "./utilidades_domm";







let usuario_atual = capturar_dados_usuario(capturar_usuario_logado());


declare var Chart: any;
declare var ChartDataLabels: any;



document.addEventListener("DOMContentLoaded", () => {

    const saudacaoElement = document.getElementById("saudacao");

    if (saudacaoElement) {
        saudacaoElement.innerHTML = `Bem vindo ${usuario_atual?.nome || ""}!`;
    }

    const lista_empresa = document.getElementById("usuarios_listados") as HTMLElement;

    if (lista_empresa) lista_empresa.innerHTML = "";

    const empresa_info = document.getElementById("info_usuario") as HTMLElement;

    let empresas = capturar_empresas();

    for (let i = 0; i < empresas.length; i++) {

        const empresa = empresas[i];

        if (empresa) {

            const li = document.createElement("li");

            li.textContent = criptografar(empresa.cnpj || "");

            ativarHoverEmpresa(li, empresa, empresa_info,usuario_atual as any);

            lista_empresa.appendChild(li);

           
        }
    }




});

const total = retornar_quantidade_empresas();


document.addEventListener("DOMContentLoaded", () => {

    const dados = localStorage.getItem("empresa_tecnologia");
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
                label: "Empresas",
                data: valores
            }]
        },

        options: {

            responsive: true,

            plugins: {

                title: {
                    display: true,
                    text: `Tecnologias das empresas (Total: ${total})`
                },

                legend: {
                display: false  
                },

                tooltip: {
                    callbacks: {
                        label: function(context: any) {
                            const valor = context.raw;
                            const porcentagem = ((valor / total) * 100).toFixed(1);

                            return `${valor} empresas (${porcentagem}%)`;
                        }
                    }
                },

                datalabels: {

                    display:false,
                }

            },

            scales: {

                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: "Quantidade de Empresas"
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


document.getElementById("checar_vagas")?.addEventListener("click" , ()=>{
    window.location.href = "./visualizacao_vagas_candidatos.html";

})

document.getElementById("editar_perfil")?.addEventListener("click" , ()=>{
    window.location.href = "./editar_perfil_candidato.html";

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

    remover_candidato(capturar_usuario_logado());



});




