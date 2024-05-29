import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AnyResponse, RePromptRequest, PromptResponse } from '../models/response-types';

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
    const body = { prompt }; // Assuming the backend expects an object
    console.log('body:', body);
    console.log(JSON.stringify(body));
    return this.http.post<any>(`${this.apiUrl}/prompt`, JSON.stringify(body), { headers }).pipe(
      catchError(error => {
        console.error('Error sending request:', error);
        throw error;
      })
    );
  }

  sendRequestToReprompt(result: PromptResponse, userText: string, token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    });
    const rePromptRequest: RePromptRequest = {
      promptResponse: result,
      userText: userText
    };
    const body = rePromptRequest ; // Assuming the backend expects an object
    console.log('body:', body);
    return this.http.post<any>(`${this.apiUrl}/reprompt`, JSON.stringify(body), { headers }).pipe(
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
    console.log('reprompt:', reprompt);
    return this.http.post<any>(`${this.apiUrl}/reprompt`, reprompt, { headers }).pipe(
      catchError(error => {
        console.error('Error sending email:', error);
        throw error;
      })
    );
    
  }


}


