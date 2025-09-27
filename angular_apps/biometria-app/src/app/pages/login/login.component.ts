// 🔐 COMPONENTE DE LOGIN (como @Controller no Spring)
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  
  // 📝 FORMULÁRIO REATIVO
  loginForm: FormGroup;
  
  // 📊 ESTADO DO COMPONENTE
  isLoading = false;
  hidePassword = true;
  
  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    // 🏗️ CONSTRUIR FORMULÁRIO COM VALIDAÇÕES
    this.loginForm = this.formBuilder.group({
      email: ['', [
        Validators.required,
        Validators.email
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(6)
      ]],
      rememberMe: [false]
    });
  }

  ngOnInit(): void {
    // 🔍 Se já estiver logado, redirecionar
    if (this.authService.isLoggedIn) {
      this.router.navigate(['/dashboard']);
    }
  }

  // 🔐 FAZER LOGIN
  async onLogin(): Promise<void> {
    if (this.loginForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.isLoading = true;

    try {
      const { email, password } = this.loginForm.value;
      
      // 🌐 CHAMAR SERVIÇO DE AUTENTICAÇÃO
      const success = await this.authService.login(email, password);

      if (success) {
        // ✅ LOGIN SUCESSO
        this.showSuccess('Login realizado com sucesso!');
        this.router.navigate(['/dashboard']);
      } else {
        // ❌ LOGIN FALHOU
        this.showError('Email ou senha incorretos');
      }
    } catch (error: any) {
      console.error('Erro no login:', error);
      
      // 🚨 TRATAR DIFERENTES TIPOS DE ERRO
      if (error.status === 401) {
        this.showError('Email ou senha incorretos');
      } else if (error.status === 0) {
        this.showError('Erro de conexão. Verifique sua internet.');
      } else {
        this.showError('Erro interno. Tente novamente.');
      }
    } finally {
      this.isLoading = false;
    }
  }

  // 👆 LOGIN COM BIOMETRIA (futuro)
  async onBiometricLogin(): Promise<void> {
    try {
      this.showInfo('Funcionalidade em desenvolvimento');
      // TODO: Implementar autenticação biométrica
    } catch (error) {
      this.showError('Biometria não disponível');
    }
  }

  // 🔄 ESQUECI MINHA SENHA
  onForgotPassword(): void {
    this.showInfo('Funcionalidade em desenvolvimento');
    // TODO: Implementar recuperação de senha
  }

  // 📝 VALIDAÇÕES DO FORMULÁRIO
  
  get emailControl() {
    return this.loginForm.get('email');
  }

  get passwordControl() {
    return this.loginForm.get('password');
  }

  get emailErrorMessage(): string {
    if (this.emailControl?.hasError('required')) {
      return 'Email é obrigatório';
    }
    if (this.emailControl?.hasError('email')) {
      return 'Email inválido';
    }
    return '';
  }

  get passwordErrorMessage(): string {
    if (this.passwordControl?.hasError('required')) {
      return 'Senha é obrigatória';
    }
    if (this.passwordControl?.hasError('minlength')) {
      return 'Senha deve ter pelo menos 6 caracteres';
    }
    return '';
  }

  // 🔧 MÉTODOS AUXILIARES

  private markFormGroupTouched(): void {
    Object.keys(this.loginForm.controls).forEach(key => {
      const control = this.loginForm.get(key);
      control?.markAsTouched();
    });
  }

  private showSuccess(message: string): void {
    this.snackBar.open(message, 'Fechar', {
      duration: 3000,
      panelClass: ['success-snackbar']
    });
  }

  private showError(message: string): void {
    this.snackBar.open(message, 'Fechar', {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }

  private showInfo(message: string): void {
    this.snackBar.open(message, 'Fechar', {
      duration: 3000,
      panelClass: ['info-snackbar']
    });
  }

  // 👁️ TOGGLE VISIBILIDADE DA SENHA
  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }
}