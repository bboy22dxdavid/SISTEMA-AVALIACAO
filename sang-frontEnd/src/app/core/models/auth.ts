

export type UserRole = 'ROLE_COORDINATOR' | 'ROLE_EVALUATOR' | 'ROLE_REVIEWER';


export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  userId: string;
  username: string;
  saram: string;
  posto: string;
  antiguidade: number;
  role: UserRole;
}
