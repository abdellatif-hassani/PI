import { Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { AuthGoogleService } from '../../services/auth-google.service';
import { ApiService } from '../../services/api.service';
import { AnyResponse, MessageResponse, EmailDetails, PromptResponse
, EmailResponse, CalendarDetails, CalendarResponse, RePromptRequest
 } from '../../models/response-types';
 import { isMessageResponse, isEmailResponse, isCalendarResponse } from '../../helpers/response-type-guards';


@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit{

  requestsAndResponses: { prompt: string, response: AnyResponse, isEmailBeingSent?: boolean }[] = [];
  prompt: string = '';
  token: any = '';
  user: any = '';

  
  ngOnInit(): void {
    this.loadMessages();
    this.user = this.authService.getProfile();
  }

  constructor(
    private authService: AuthGoogleService,
    private apiService: ApiService
  ) {
    this.requestsAndResponses = [
      {
        prompt: '',
        response: {
          typeAnswer: 'message',
          methodToUse: 'message',
          satisfied: true,
          answerText: 'Hi, how can I help you?'
        },
      },
      //mail
      {
        prompt: 'Send an email to John Doe',
        response: {
          typeAnswer: 'email',
          methodToUse: 'send',
          satisfied: false,
          answerRelatedToGmail: {
            to: 'sasbo@gmail.com',
            subject: 'Meeting',
            message: 'Hi John, let\'s meet tomorrow at 10am.'
          }
        },
        isEmailBeingSent: true
      },
      {
        prompt: 'I would like to schedule a meeting',
        response: {
          typeAnswer: 'calendar',
          methodToUse: 'calendar',
          satisfied: true,
          answerRelatedToCalendar: {
            description: 'Meeting with John Doe',
            startTime: '2021-08-01T09:00:00',
            endTime: '2021-08-01T10:00:00',
            location: 'Office',
            summary: 'Meeting'
          }
        },
      },
      //mail
      {
        prompt: 'Send an email to John Doe',
        response: {
          typeAnswer: 'email',
          methodToUse: 'send',
          satisfied: false,
          answerRelatedToGmail: {
            to: 'sasbo@gmail.com',
            subject: 'Meeting',
            message: 'Hi John, let\'s meet tomorrow at 10am.'
          }
        },
        isEmailBeingSent: true
      }
    ];
  }

  sendRequest() {
      if (!this.prompt.trim()) {
        console.error('Prompt cannot be empty');
        return;
      }
    
      this.token = this.authService.getToken();
    
      this.apiService.sendRequest(this.prompt, this.token).subscribe(
        response => {
          let formattedResponse: AnyResponse;
    
          // Determine the type of response and format accordingly
          if (isMessageResponse(response)) {
            formattedResponse = {
              typeAnswer: 'message',
              methodToUse: response.methodToUse,
              satisfied: response.satisfied,
              wantToCancel: response.wantToCancel,
              answerText: response.answerText
            };
          } else if (isEmailResponse(response)) {
            formattedResponse = {
              typeAnswer: 'email',
              methodToUse: response.methodToUse,
              satisfied: response.satisfied,
              wantToCancel: response.wantToCancel,
              answerRelatedToGmail: {
                to: response.answerRelatedToGmail.to,
                subject: response.answerRelatedToGmail.subject,
                message: response.answerRelatedToGmail.message,
                attachments: response.answerRelatedToGmail.attachments
              }
            };
          } else if (isCalendarResponse(response)) {
            formattedResponse = {
              typeAnswer: 'calendar',
              methodToUse: response.methodToUse,
              satisfied: response.satisfied,
              wantToCancel: response.wantToCancel,
              answerRelatedToCalendar: {
                description: response.answerRelatedToCalendar.description,
                startTime: response.answerRelatedToCalendar.startTime,
                endTime: response.answerRelatedToCalendar.endTime,
                location: response.answerRelatedToCalendar.location,
                summary: response.answerRelatedToCalendar.summary
              }
            };
          } else {
            // Handle unexpected response types or errors
            console.error('Unexpected response type:', response);
            return;
          }
    
          // Store the formatted response
          console.log('Response:', formattedResponse);
          this.requestsAndResponses.push({ prompt: this.prompt, response: formattedResponse });
          this.saveMessages();
          this.prompt = '';
          this.scrollToBottom();
        },
        error => {
          console.error('Error:', error);
        }
      );
  }



  confirmEmail(response: AnyResponse) {
    if (isEmailResponse(response)) {
      const promptResponse: PromptResponse = {
        ...response,
        typeAnswer: response.typeAnswer,
        methodToUse: response.methodToUse,
        satisfied: response.satisfied,
        wantToCancel: response.wantToCancel,
        answerRelatedToGmail: response.answerRelatedToGmail
      };
  
      const rePromptRequest: RePromptRequest = {
        promptResponse: promptResponse,
        userText: "Please confirm sending this email"
      };
  
      console.log('Sending email:', rePromptRequest);
      this.apiService.sendEmail(rePromptRequest, this.token).subscribe(
        response => {
          console.log('Email sent:', response);
          this.requestsAndResponses.push({ prompt: '', response: response, isEmailBeingSent: true});
          this.saveMessages();
          this.loadMessages();
          console.log('Requests and responses:', this.requestsAndResponses);
          this.scrollToBottom();
        },
        error => {
          console.error('Error sending email:', error);
        }
      );
    } else {
      console.error('Provided response is not an email response:', response);
    }
  }


  @ViewChild('messagesContainer') messagesContainer!: ElementRef;
  scrollToBottom() {
    this.messagesContainer.nativeElement.scrollTop = this.messagesContainer.nativeElement.scrollHeight;
  }

  saveMessages() {
    localStorage.setItem('requestsAndResponses', JSON.stringify(this.requestsAndResponses));
  }

  loadMessages() {
    const data = localStorage.getItem('requestsAndResponses');
    if (data) {
      this.requestsAndResponses = JSON.parse(data);
    }
  }

  
  getMessageResponse(response: AnyResponse): MessageResponse | null {
    if (response.typeAnswer === 'message') {
      return response as MessageResponse;
    }
    return null;
  }

  getEmailResponse(response: AnyResponse): EmailResponse | null {
    if (response.typeAnswer === 'email') {
      return response as EmailResponse;
    }
    return null;
  }

  getCalendarResponse(response: AnyResponse): CalendarResponse | null {
    if (response.typeAnswer === 'calendar') {
      return response as CalendarResponse;
    }
    return null;
  }

  deleteHistory() {
    localStorage.removeItem('requestsAndResponses');
    this.requestsAndResponses = [];
  }

  
}
