import { Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { AuthGoogleService } from '../../services/auth-google.service';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit{
  requestsAndResponses: { prompt: string, response: any, isUserPrompt: boolean }[] = [];
  prompt: string = '';
  token: any = '';
  user: any = '';

  
  ngOnInit(): void {
    this.user = this.authService.getProfile();
  }

  constructor(
    private authService: AuthGoogleService,
    private apiService: ApiService
  ) {
    this.requestsAndResponses = [
      { prompt: 'What is the capital of France?', response: 'Paris', isUserPrompt: false },
      { prompt: 'What is the capital of Spain?', response: 'Madrid', isUserPrompt: false },
      { prompt: 'What is the capital of Italy?', response: 'Rome', isUserPrompt: false },
      { prompt: 'What is the capital of Germany?', response: 'Berlin', isUserPrompt: false },
      { prompt: 'What is the capital of the United States?', response: 'Washington D.C.', isUserPrompt: false },
      { prompt: 'What is the capital of Canada?', response: 'Ottawa', isUserPrompt: false },
      { prompt: 'What is the capital of Mexico?', response: 'Mexico City', isUserPrompt: false },
      { prompt: 'What is the capital of Brazil?', response: 'Brasília', isUserPrompt: false },
      { prompt: 'What is the capital of Argentina?', response: 'Buenos Aires', isUserPrompt: false },
      { prompt: 'What is the capital of Australia?', response: 'Canberra', isUserPrompt: false }
    ];
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
        // Store the request and response
        this.requestsAndResponses.push({ prompt: this.prompt, response: response.answerText, isUserPrompt: true });
        // Clear the prompt
        this.prompt = '';
      },
      error => {
        console.error('Error:', error);
        // Handle error as needed
      }
    );
  }

  @ViewChild('messagesContainer') messagesContainer!: ElementRef;
  scrollToBottom() {
    this.messagesContainer.nativeElement.scrollTop = this.messagesContainer.nativeElement.scrollHeight;
  }
  
}
