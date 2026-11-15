export interface NivelamentoPayload {
  id?: string;
  avaliacao: { id: string };
  revisor: { id: string };
  qualidadetrabalho: number;
  produtividadetrabalho: number;
  conhecimentoprofissional: number;
  eficienciatrabalho: number;
  meiosmateriais: number;
  planejamento: number;
  julgamento: number;
  responsabilidade: number;
  disciplina: number;
  apresentacaopessoal: number;
  lideranca: number;
  iniciativa: number;
  adaptabilidade: number;
  comunicacaooralescrita: number;
  relacionamentoambientetrabalho: number;
  justificativa: string;
  conceitomoral: string;
  conceitoprofissional: string;
}
