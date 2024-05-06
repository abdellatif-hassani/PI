import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8000'; 

  constructor(private http: HttpClient) {}

  sendRequest(prompt: string, token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    });
    console.log('authorization:', headers.get('Authorization'));
    return this.http.get<any>(`${this.apiUrl}/prompt_service/api?message=${prompt}`, { headers }).pipe(
      catchError(error => {
        console.error('Error sending request:', error);
        throw error;
      })
    );

    
  }
}
