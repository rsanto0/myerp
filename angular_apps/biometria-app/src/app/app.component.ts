// 🅰️ COMPONENTE PRINCIPAL DO APP (como @SpringBootApplication)
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'MyERP - Ponto Biométrico';
  isLoading = true;

  constructor() {}

  ngOnInit(): void {
    // 🔍 Verificar se usuário já está logado
    this.checkAuthStatus();
  }

  private checkAuthStatus(): void {
    // Simplificado - apenas parar loading
    setTimeout(() => {
      this.isLoading = false;
    }, 1000);
  }
}