import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'; // Import HttpClientModule
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { OAuthModule } from 'angular-oauth2-oidc';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatTableModule} from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';

import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ProfileComponent } from './components/profile/profile.component';
import { MessagesComponent } from './components/messages/messages.component';
import { PolicyComponent } from './components/policy/policy.component';
import { TestLoginComponent } from './components/test-login/test-login.component';
import { MatSortModule } from '@angular/material/sort';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    ProfileComponent,
    MessagesComponent,
    PolicyComponent,
    TestLoginComponent,
  
  ],
  imports: [
    BrowserModule,
    HttpClientModule, // Include HttpClientModule here
    AppRoutingModule,
    OAuthModule.forRoot(),
    FormsModule, ReactiveFormsModule, MatCardModule,
    MatSidenavModule,MatListModule,MatFormFieldModule,MatInputModule,
    MatButtonModule, MatIconModule, MatToolbarModule, MatTableModule, 
    MatPaginatorModule, MatSortModule, BrowserAnimationsModule
  ],
  providers: [
    provideAnimationsAsync(),
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
