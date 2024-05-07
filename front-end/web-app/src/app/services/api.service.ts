import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:9090/prompt_service'; 

  constructor(private http: HttpClient) {}

  sendRequest(prompt: string, token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    });
    console.log('authorization:', headers.get('Authorization'));
    return this.http.get<any>(`${this.apiUrl}`, { headers }).pipe(
      catchError(error => {
        console.error('Error sending request:', error);
        throw error;
      })
    );

    
  }
}
