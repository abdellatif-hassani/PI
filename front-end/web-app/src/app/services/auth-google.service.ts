import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthConfig, OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class AuthGoogleService {

  constructor(private oAuthService: OAuthService, private router: Router) { 
    this.initConfiguration();
  }

  initConfiguration() {
    const authConfig: AuthConfig = {
      issuer: 'https://accounts.google.com',
      strictDiscoveryDocumentValidation: false,
      redirectUri: window.location.origin + '/dashboard',
      clientId: '620536565122-91ob5s78lu1t6pjcl2tbb0v0rdban5cj.apps.googleusercontent.com',
      scope: 'openid profile email https://www.googleapis.com/auth/calendar',
    };

    this.oAuthService.configure(authConfig);
    this.oAuthService.setupAutomaticSilentRefresh();
    this.oAuthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      if (this.oAuthService.hasValidAccessToken()) {
        this.storeToken(this.oAuthService.getAccessToken());
      }
    });
  }

  login() {
    this.oAuthService.initImplicitFlow();
  }

  logout() {
    this.oAuthService.revokeTokenAndLogout();
    this.oAuthService.logOut();
    this.clearToken(); // Clear token when logging out
    this.router.navigate(['/login']); // Navigate to login page after logout
  }

  private storeToken(token: string) {
    localStorage.setItem('access_token', token); // Store token in localStorage
  }

  private clearToken() {
    localStorage.removeItem('access_token'); // Remove token from localStorage
  }

  getToken(): string | null {
    return localStorage.getItem('access_token'); // Retrieve token from localStorage
  }

  getProfile() {
    return this.oAuthService.getIdentityClaims();
  }
  
}
