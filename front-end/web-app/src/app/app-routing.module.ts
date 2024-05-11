// app-routing.module.ts

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ProfileComponent } from './components/profile/profile.component';
import { MessagesComponent } from './components/messages/messages.component';
import { PolicyComponent } from './components/policy/policy.component';
import { AuthGuard } from './guards/auth.guard';
import { TestLoginComponent } from './components/test-login/test-login.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { 
    path: 'dashboard',
    component: DashboardComponent,
     canActivate: [AuthGuard], // Protect the dashboard route
    children: [
      { path: '', redirectTo: 'messages', pathMatch: 'full' }, // Default route
      { path: 'profile', component: ProfileComponent },
      { path: 'messages', component: MessagesComponent },
    ]
  },
  { path: 'policy', component: PolicyComponent },
  { path: 'test', component: TestLoginComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
