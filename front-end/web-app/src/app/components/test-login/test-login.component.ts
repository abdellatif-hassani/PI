import { Component, OnInit } from '@angular/core';
import { AuthGoogleService } from '../../services/auth-google.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-test-login',
  templateUrl: './test-login.component.html',
  styleUrl: './test-login.component.css'
})
export class TestLoginComponent implements OnInit{
    
    constructor(private authService: AuthGoogleService, 
                private router: Router
    ) {
      
    }

    ngOnInit(): void {
      this.authService.initConfiguration();
      setTimeout(() => {
          this.router.navigate(['/dashboard']);
      }, 500); 
         
    }
    
}
