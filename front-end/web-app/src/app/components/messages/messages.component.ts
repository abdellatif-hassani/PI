import { Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { AuthGoogleService } from '../../services/auth-google.service';
import { ApiService } from '../../services/api.service';
import { AnyResponse, MessageResponse, EmailDetails
, EmailResponse, CalendarDetails, CalendarResponse
 } from '../../models/response-types';
 import { isMessageResponse, isEmailResponse, isCalendarResponse } from '../../helpers/response-type-guards';


@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit{

  requestsAndResponses: { prompt: string, response: AnyResponse, isUserPrompt: boolean }[] = [];
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
      {
        prompt: 'Hello',
        response: {
          typeAnswer: 'message',
          methodToUse: 'message',
          satisfied: true,
          answerText: 'Hi, how can I help you?'
        },
        isUserPrompt: true
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
        isUserPrompt: true
      },
      //mail
      {
        prompt: 'Send an email to John Doe',
        response: {
          typeAnswer: 'email',
          methodToUse: 'send',
          satisfied: true,
          answerRelatedToGmail: {
            to: 'sasbo@gmail.com',
            subject: 'Meeting',
            message: 'Hi John, let\'s meet tomorrow at 10am.'
          }
        },
        isUserPrompt: true
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
          this.requestsAndResponses.push({ prompt: this.prompt, response: formattedResponse, isUserPrompt: true });
          this.prompt = '';
          this.scrollToBottom();
        },
        error => {
          console.error('Error:', error);
        }
      );
  }

  @ViewChild('messagesContainer') messagesContainer!: ElementRef;
  scrollToBottom() {
    this.messagesContainer.nativeElement.scrollTop = this.messagesContainer.nativeElement.scrollHeight;
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


  confirmEmail(reprompt: AnyResponse) {
    this.apiService.sendEmail(reprompt, this.token).subscribe(
      response => {
        console.log('Email sent:', response);
        this.requestsAndResponses.push({ prompt: '', response: response, isUserPrompt: false });
        this.scrollToBottom();
      },
      error => {
        console.error('Error sending email:', error);
      }
    );
  }



  
}
