import { Component, OnInit } from '@angular/core';
import { AuthGoogleService } from '../services/auth-google.service';
import { ApiService } from '../services/api.service'; 
import { Router } from '@angular/router';



@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  profile: any;
  prompt: string = '';
  token: any = '';

  constructor(
    private authService: AuthGoogleService,
    private apiService: ApiService, // Inject ApiService
    private router: Router
  ) {}

  ngOnInit(): void {
    this.showData();
    console.log("profile", this.profile);
    console.log("token : ", this.authService.getToken());
  }

  showData() {
    this.profile = this.authService.getProfile();
  }

  logOut() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  sendRequest() {
    // Check if prompt is empty
    if (!this.prompt.trim()) {
      console.error('Prompt cannot be empty');
      return;
    }

    // Retrieve token
    this.token = this.authService.getToken();

    // Send request to backend
    this.apiService.sendRequest(this.prompt, this.token).subscribe(
      response => {
        console.log('Response:', response);
        // Handle response as needed
      },
      error => {
        console.error('Error:', error);
        // Handle error as needed
      }
    );
  }
}
