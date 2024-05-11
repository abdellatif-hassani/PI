import { Component, inject } from '@angular/core';
import { AuthGoogleService } from '../../services/auth-google.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
    
    constructor(private authService: AuthGoogleService) {}

    signInWithGoogle() {
        this.authService.login();
    }
}
