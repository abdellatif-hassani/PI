import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AnyResponse, RePromptRequest } from '../models/response-types';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  
  private apiUrl = 'http://localhost:9000'; 

  constructor(private http: HttpClient) {}

  sendRequest(prompt: string, token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    });
    const body = { prompt }; // Assuming the backend expects an object
    console.log('body:', body);
    console.log(JSON.stringify(body));
    return this.http.post<any>(`${this.apiUrl}`, JSON.stringify(body), { headers }).pipe(
      catchError(error => {
        console.error('Error sending request:', error);
        throw error;
      })
    );
  }

  sendEmail(reprompt: RePromptRequest, token: any): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    });
    reprompt.promptResponse.satisfied = true;
    console.log('reprompt:', reprompt);
    console.log('authorization:', headers.get('Authorization'));
    return this.http.post<any>(`${this.apiUrl}/reprompt`, reprompt, { headers }).pipe(
      catchError(error => {
        console.error('Error sending email:', error);
        throw error;
      })
    );
    
  }


}
