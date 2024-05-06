import { Component, OnInit } from '@angular/core';
import { AuthGoogleService } from '../../services/auth-google.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  profile: any;

  constructor(private authService: AuthGoogleService) { }

  ngOnInit(): void {
    this.profile = this.authService.getProfile();
    console.log('profile', this.profile);
  }
}
