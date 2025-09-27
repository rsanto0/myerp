// 🔐 SERVIÇO DE AUTENTICAÇÃO (como @Service no Spring)
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, firstValueFrom } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

// 📊 INTERFACES
export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  user: User;
  expiresIn: number;
}

export interface User {
  id: number;
  nome: string;
  email: string;
  empresaId: number;
  empresaNome: string;
  role: string;
  ativo: boolean;
}

@Injectable({
  providedIn: 'root' // Singleton como @Service
})
export class AuthService {
  
  // 🌐 URLs DO BACKEND
  private readonly AUTH_URL = environment.authUrl;
  
  // 📊 ESTADO DO USUÁRIO (como @Autowired)
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  private tokenSubject = new BehaviorSubject<string | null>(null);
  
  // 📖 OBSERVABLES PÚBLICOS
  public currentUser$ = this.currentUserSubject.asObservable();
  public token$ = this.tokenSubject.asObservable();
  public isAuthenticated$ = this.currentUserSubject.asObservable().pipe(
    // Transforma User em boolean
    map(user => !!user)
  );

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    // 🚀 INICIALIZAR - Verificar token salvo
    this.initializeAuth();
  }

  // 🚀 INICIALIZAÇÃO
  private initializeAuth(): void {
    const token = localStorage.getItem('myerp_token');
    const userData = localStorage.getItem('myerp_user');
    
    if (token && userData) {
      try {
        const user: User = JSON.parse(userData);
        this.tokenSubject.next(token);
        this.currentUserSubject.next(user);
      } catch (error) {
        console.error('Erro ao carregar dados salvos:', error);
        this.clearAuthData();
      }
    }
  }

  // 🔐 LOGIN
  async login(email: string, password: string): Promise<boolean> {
    try {
      const loginData: LoginRequest = { email, password };
      console.log('[AuthService] Dados enviados para login:', loginData);

      // 🌐 CHAMADA PARA BACKEND (auth-service:8081)
      console.log('[AuthService] Chamando backend para login:', `${this.AUTH_URL}/auth/login`);
      const response = await firstValueFrom(
        this.http.post<LoginResponse>(`${this.AUTH_URL}/auth/login`, loginData)
      );

      if (response.token && response.user) {
        // ✅ LOGIN SUCESSO - Salvar dados
        console.log('[AuthService] Login bem-sucedido. Usuário:', response.user);
        this.setAuthData(response.token, response.user);
        return true;
      }

      console.warn('[AuthService] Login falhou. Resposta:', response);
      return false;
    } catch (error) {
      console.error('Erro no login:', error);
      throw error;
    }
  }

  // 🚪 LOGOUT
  async logout(): Promise<void> {
    try {
      const token = this.tokenSubject.value;
      
      if (token) {
        // 🌐 NOTIFICAR BACKEND
        await firstValueFrom(
          this.http.post(`${this.AUTH_URL}/auth/logout`, {}, {
            headers: this.getAuthHeaders()
          })
        );
      }
    } catch (error) {
      console.error('Erro no logout:', error);
    } finally {
      // 🧹 LIMPAR DADOS LOCAIS
      this.clearAuthData();
      this.router.navigate(['/login']);
    }
  }

  // 🔍 VERIFICAR SE ESTÁ AUTENTICADO
  async isAuthenticated(): Promise<boolean> {
    const token = this.tokenSubject.value;
    
    if (!token) {
      return false;
    }

    try {
      // 🌐 VALIDAR TOKEN NO BACKEND
      const response = await firstValueFrom(
        this.http.get(`${this.AUTH_URL}/auth/validate`, {
          headers: this.getAuthHeaders()
        })
      );
      
      return true;
    } catch (error) {
      console.error('Token inválido:', error);
      this.clearAuthData();
      return false;
    }
  }

  // 🔄 REFRESH TOKEN
  async refreshToken(): Promise<boolean> {
    try {
      const response = await firstValueFrom(
        this.http.post<LoginResponse>(`${this.AUTH_URL}/auth/refresh`, {}, {
          headers: this.getAuthHeaders()
        })
      );

      if (response.token) {
        this.setAuthData(response.token, response.user);
        return true;
      }
      
      return false;
    } catch (error) {
      console.error('Erro ao renovar token:', error);
      this.clearAuthData();
      return false;
    }
  }

  // 💾 SALVAR DADOS DE AUTENTICAÇÃO
  private setAuthData(token: string, user: User): void {
    // 💾 SALVAR NO LOCALSTORAGE
    localStorage.setItem('myerp_token', token);
    localStorage.setItem('myerp_user', JSON.stringify(user));
    
    // 📊 ATUALIZAR ESTADO
    this.tokenSubject.next(token);
    this.currentUserSubject.next(user);
  }

  // 🧹 LIMPAR DADOS DE AUTENTICAÇÃO
  private clearAuthData(): void {
    localStorage.removeItem('myerp_token');
    localStorage.removeItem('myerp_user');
    
    this.tokenSubject.next(null);
    this.currentUserSubject.next(null);
  }

  // 📋 OBTER HEADERS DE AUTENTICAÇÃO
  private getAuthHeaders(): HttpHeaders {
    const token = this.tokenSubject.value;
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  // 📖 GETTERS PÚBLICOS
  get currentUser(): User | null {
    return this.currentUserSubject.value;
  }

  get token(): string | null {
    return this.tokenSubject.value;
  }

  get isLoggedIn(): boolean {
    return !!this.currentUser;
  }

  get userName(): string {
    return this.currentUser?.nome || 'Usuário';
  }

  get userEmail(): string {
    return this.currentUser?.email || '';
  }

  get empresaNome(): string {
    return this.currentUser?.empresaNome || '';
  }

  get isAdmin(): boolean {
    return this.currentUser?.role === 'ADMIN';
  }

  // 🔧 MÉTODO PARA HTTP INTERCEPTOR
  getAuthToken(): string | null {
    return this.token;
  }
}

import { map } from 'rxjs/operators';