export interface LoginRequest {
  username: string;
  password: string;
}

// Interface para a resposta do login
export interface LoginResponse {
  token: string;
  userId: string; // UUID retornado pelo Spring Boot
  username: string;
  role: string;   // Ex: 'ROLE_AVALIADOR', 'ROLE_REVISOR', 'ROLE_ADMIN'
}
