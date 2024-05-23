import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthConfig, OAuthService, OAuthEvent } from 'angular-oauth2-oidc';
import { interval, Observable, Subscription } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthGoogleService {
  private refreshInterval$: Observable<number> = interval(60000); // Refresh interval
  private refreshSubscription!: Subscription;

  constructor(private oAuthService: OAuthService, private router: Router) {
    
  }

  initConfiguration() {
    const authConfig: AuthConfig = {
      issuer: 'https://accounts.google.com',
      strictDiscoveryDocumentValidation: false,
      redirectUri: window.location.origin + '/test',
      clientId: '620536565122-91ob5s78lu1t6pjcl2tbb0v0rdban5cj.apps.googleusercontent.com',
      scope: 'openid profile email https://www.googleapis.com/auth/calendar https://www.googleapis.com/auth/gmail.modify ',
    };
    
    this.oAuthService.configure(authConfig);
    this.oAuthService.setupAutomaticSilentRefresh();
    this.oAuthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      console.log('************Discovery Document loaded:', this.oAuthService.discoveryDocumentLoaded$)
      if (this.oAuthService.hasValidAccessToken()) {
        this.storeToken(this.oAuthService.getAccessToken());
        this.oAuthService.setupAutomaticSilentRefresh();
      }
      console.log('Logged in:', this.oAuthService.hasValidAccessToken());
      console.log('Access Token:', this.oAuthService.getAccessToken());
    });
  }

  isAuthenticated() {
    return this.oAuthService.hasValidAccessToken();
  }

  login() {
    this.oAuthService.initImplicitFlow();
    this.initConfiguration();
    this.startTokenRefreshTimer();
  }

  logout() {
    this.oAuthService.revokeTokenAndLogout();
    this.oAuthService.logOut();
    this.clearToken(); 
    this.clearMessages();
    this.router.navigate(['/login']); 
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

  private clearMessages() {
    localStorage.removeItem('requestsAndResponses');
  }

  private startTokenRefreshTimer() {
    this.refreshInterval$ = interval(60000); // Refresh token every 60 seconds (adjust as needed)
    this.refreshSubscription = this.refreshInterval$.pipe(
      switchMap(() => this.refreshTokenIfNeeded())
    ).subscribe();
  }

  private refreshTokenIfNeeded(): Observable<any> {
    if (this.isAuthenticated() && this.oAuthService.getAccessTokenExpiration() < Date.now() + 60000) {
      return new Observable((observer) => {
        this.oAuthService.refreshToken().then(() => {
          const newAccessToken = this.oAuthService.getAccessToken();
          this.storeToken(newAccessToken);
          console.log('Access Token refreshed:', newAccessToken);
          observer.next(); // Emit a value to indicate completion
          observer.complete(); // Complete the observable
        }).catch((error) => {
          console.error('Error refreshing access token:', error);
          observer.error(error); // Emit an error if refreshing fails
          observer.complete(); // Complete the observable
        });
      });
    } else {
      return new Observable(); // Return an empty observable if refresh is not needed
    }
  }
}
