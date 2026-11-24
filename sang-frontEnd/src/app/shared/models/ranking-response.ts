export interface RankingResponse {
  posicao: number;
  avaliadoId: string;
  avaliacaoId: string | null;
  nivelamentoId: string | null;
  saram: string;
  posto: string;
  nomeGuerra: string;
  fotoUrl: string | null;
  secao: string;
  antiguidade: number;
  notaOriginal: number;
  notaFinal: number;
  avaliado: boolean;
  nivelado: boolean;
}
