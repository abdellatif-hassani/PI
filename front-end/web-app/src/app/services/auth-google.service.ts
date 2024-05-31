import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment'; 
import { AuthConfig, OAuthService } from 'angular-oauth2-oidc';
import { interval, Observable, Subscription } from 'rxjs';
import { switchMap, catchError, filter } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthGoogleService {
  private refreshInterval$: Observable<number> = interval(60000); // Refresh interval
  private refreshSubscription!: Subscription;

  constructor(private oAuthService: OAuthService, private router: Router) {
    this.initConfiguration();
  }

  initConfiguration() {
    const authConfig: AuthConfig = environment.authConfig;

    this.oAuthService.configure(authConfig);
    this.oAuthService.setupAutomaticSilentRefresh();
    this.oAuthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      console.log('Discovery Document loaded:', this.oAuthService.discoveryDocumentLoaded$);
      if (this.oAuthService.hasValidAccessToken()) {
        this.storeToken(this.oAuthService.getAccessToken());
        this.startTokenRefreshTimer();
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
  }

  logout() {
    this.oAuthService.revokeTokenAndLogout();
    this.oAuthService.logOut();
    this.clearToken();
    this.clearMessages();
    this.router.navigate(['/login']);
  }

  private storeToken(token: string) {
    localStorage.setItem('access_token', token);
  }

  private clearToken() {
    localStorage.removeItem('access_token');
    // localStorage.removeItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('access_token');
  }

  getProfile() {
    return this.oAuthService.getIdentityClaims();
  }

  private clearMessages() {
    localStorage.removeItem('requestsAndResponses');
  }

  private startTokenRefreshTimer() {
    this.refreshSubscription = this.refreshInterval$.pipe(
      filter(() => this.isAuthenticated() && this.oAuthService.getAccessTokenExpiration() < Date.now() + 60000),
      switchMap(() => this.refreshTokenIfNeeded().pipe(
        catchError(error => {
          console.error('Error during token refresh:', error);
          return [];
        })
      ))
    ).subscribe();
  }

  private refreshTokenIfNeeded(): Observable<any> {
    return new Observable(observer => {
      this.oAuthService.refreshToken().then(() => {
        const newAccessToken = this.oAuthService.getAccessToken();
        this.storeToken(newAccessToken);
        console.log('Access Token refreshed:', newAccessToken);
        observer.next();
        observer.complete();
      }).catch((error) => {
        console.error('Error refreshing access token:', error);
        observer.error(error);
        observer.complete();
      });
    });
  }
}
