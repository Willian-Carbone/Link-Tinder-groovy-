import{ ESPECIALIDADES,Especialidade} from "../codigos_logicos/tiposFixos";
import { Candidato } from "../codigos_logicos/classes/candidato";
import { nomeEspecialidade, nomeFormacao } from "../codigos_logicos/utilitarios";
import { Empresa } from "../codigos_logicos/classes/empresa";





export function pegarDadosFormulario(
    form: HTMLFormElement
): Record<string, string> {

    const dados = Object.fromEntries(
        new FormData(form).entries()
    ) as Record<string, string>;

    return dados;
}



export function dispararErro(input: HTMLElement, mensagem: HTMLElement) {
    
    mensagem.classList.add("emitido");

    
    input.classList.add("animacao-erro");
    input.classList.add("erro"); 

    
    input.addEventListener("animationend", () => {
        input.classList.remove("animacao-erro");
    }, { once: true });
}


export function limpar_erros(form: HTMLFormElement) {
   
    const inputs = form.querySelectorAll<HTMLInputElement>('input[type="text"]');
    inputs.forEach(input => input.classList.remove("erro"));

    const mensagens = form.querySelectorAll<HTMLParagraphElement>('p.mensagem_erro');
    mensagens.forEach(p => p.classList.remove("emitido"));

    const sucesso = form.querySelector<HTMLHeadingElement>('h2.mensagem_sucesso');
    sucesso?.classList.remove("emitido");
   
    
}


export function aoMenosUmaCheckboxMarcada(container: HTMLElement): boolean {

    const checkboxes = container.querySelectorAll<HTMLInputElement>('input[type="checkbox"]');

    return Array.from(checkboxes).some(cb => cb.checked);

}



export function capturarEspecialidades(container: HTMLElement): Especialidade[] {

    const checkboxes = container.querySelectorAll<HTMLInputElement>('input[type="checkbox"]');

    const especialidadesSelecionadas: Especialidade[] = [];

    checkboxes.forEach(cb => {

        if (cb.checked) {

            const existe = ESPECIALIDADES.find(e => e.codigo === cb.value);

            if (existe) {
                especialidadesSelecionadas.push(existe.codigo);
            }

        }

    });

    return especialidadesSelecionadas;

}


export function ativarHoverCandidato(
    elemento: HTMLElement,
    candidato: Candidato,
    infoBox: HTMLElement
){
    elemento.addEventListener("mouseenter", () => {

        const habilidades = candidato.competencias
            ?.map(nomeEspecialidade)
            .join(", ") || "";

        const formacao = nomeFormacao(candidato.formacao);

        infoBox.style.display = "block";

        infoBox.innerHTML = 

        //Email: ${candidato.email}<br>
        
        `
            Formação: ${formacao}<br>
            Habilidades: ${habilidades}
        `;
    });

    elemento.addEventListener("mousemove", (event) => {

        infoBox.style.left = event.pageX + 10 + "px";
        infoBox.style.top = event.pageY + 10 + "px";

    });

    elemento.addEventListener("mouseleave", () => {

        infoBox.style.display = "none";

    });
}


export function ativarHoverEmpresa(
    elemento: HTMLElement,
    empresa: Empresa,
    infoBox: HTMLElement
){

    elemento.addEventListener("mouseenter", () => {

        const habilidades = empresa.competencias
            ?.map(nomeEspecialidade)
            .join(", ") || "";

        infoBox.style.display = "block";

        infoBox.innerHTML =

        //Email: ${empresa.email || ""}<br>
        
        
        `
            
            Habilidades que a empresa busca: ${habilidades}
        `;

    });

    elemento.addEventListener("mousemove", (event) => {

        infoBox.style.left = event.pageX + 10 + "px";
        infoBox.style.top = event.pageY + 10 + "px";

    });

    elemento.addEventListener("mouseleave", () => {

        infoBox.style.display = "none";

    });

}