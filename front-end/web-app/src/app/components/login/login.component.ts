import { Component, inject } from '@angular/core';
import { AuthGoogleService } from '../../services/auth-google.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
    private authService = inject(AuthGoogleService);

    signInWithGoogle() {
        this.authService.login();
    }
}
