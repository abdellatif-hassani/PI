import { Component, OnInit } from '@angular/core';
import { AuthGoogleService } from '../../services/auth-google.service';
import { ApiService } from '../../services/api.service'; 
import { Router } from '@angular/router';



@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
// toProfile() {
//     this.router.navigate(['/profile']);
// }
  profile: any;
  

  constructor(
    private authService: AuthGoogleService,
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

  
}
