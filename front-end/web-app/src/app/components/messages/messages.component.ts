import { Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { AuthGoogleService } from '../../services/auth-google.service';
import { ApiService } from '../../services/api.service';
import { AnyResponse, MessageResponse, PromptResponse, EmailResponse, CalendarResponse, RePromptRequest } from '../../models/response-types';
import { isMessageResponse, isEmailResponse, isCalendarResponse } from '../../helpers/response-type-guards';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {
  requestsAndResponses: { prompt: string, response: AnyResponse, isEmailBeingSent?: boolean, isRequestTraited?: boolean }[] = [];
  prompt: string = '';
  token: string = '';
  user: any = '';
  isFirstRequest: boolean = true;

  @ViewChild('messagesContainer') messagesContainer!: ElementRef;

  constructor(
    private authService: AuthGoogleService,
    private apiService: ApiService
  ) {
    // this.initializeRequestsAndResponses();
    this.disableButton();
  }

  ngOnInit(): void {
    this.loadMessages();
    this.user = this.authService.getProfile();
  }

  async makeAcall() {
    if (this.isFirstRequest) {
      try {
        await this.sendRequest();
        console.log("******:" + this.getLastResponse()?.typeAnswer);
        console.log("******:" + this.getLastResponse());
        console.log("******:" + this.requestsAndResponses[0]);
        if (this.getLastResponse() != undefined 
            && !this.getLastResponse()?.satisfied 
            && this.getLastResponse()?.typeAnswer!='message'){
          this.isFirstRequest = false;
          console.log('isFirstRequest:', this.isFirstRequest);
          console.log('Satisfied :', this.getLastResponse()?.satisfied);
        }
      } catch (error) {
        console.error('Error in sendRequest:', error);
      }
    } else {
      console.log('RESEND : isFirstRequest:', this.isFirstRequest);
      console.log('RESEND : request :', this.getLastResponse()?.satisfied);
      this.reSendRequest();
      const result = this.getLastResponse();
      if (result?.satisfied || result?.wantToCancel) {
        this.isFirstRequest = true;
      }
    }
    console.log('makeAcall : ' + this.isFirstRequest);
  }
  

  sendRequest(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (!this.prompt.trim()) {
        console.error('Prompt cannot be empty');
        reject('Prompt cannot be empty');
        return;
      }
      this.disableButton();
  
      this.token = this.authService.getToken() as string;
      this.apiService.sendRequest(this.prompt, this.token).subscribe(
        response => {
          const formattedResponse = this.formatResponse(response);
          if (formattedResponse) {
            this.requestsAndResponses.push({ prompt: this.prompt, response: formattedResponse });
            this.saveMessages();
            this.prompt = '';
            this.scrollToBottom();
          }
          resolve();
        },
        error => {
          console.error('Error:', error);
          this.prompt = '';
          reject(error);
        }
      );
    });
  }
  

  reSendRequest() {
    if (!this.prompt.trim()) {
      console.error('Prompt cannot be empty');
      return;
    }

    this.token = this.authService.getToken() as string;
    const lastResponse = this.getLastResponse() as PromptResponse;
    this.apiService.sendRequestToReprompt(lastResponse, this.prompt, this.token).subscribe(
      response => {
        const formattedResponse = this.formatResponse(response);
        if (formattedResponse) {
          if(formattedResponse.satisfied) {
            this.requestsAndResponses.push({ prompt: this.prompt, response: formattedResponse, isRequestTraited: true, isEmailBeingSent: true});
            console.log("*******"+this.requestsAndResponses);
          }else {
            this.requestsAndResponses.push({ prompt: this.prompt, response: formattedResponse });
          }
          this.saveMessages();
          this.prompt = '';
          this.scrollToBottom();
        }
      },
      error => {
        console.error('Error:', error);
        this.prompt = '';
      }
    );
    this.disableButton();
  }

  confirmEmail(response: AnyResponse, i: number) {
    if (isEmailResponse(response)) {
      const rePromptRequest: RePromptRequest = {
        promptResponse: response,
        userText: "Please confirm sending this email"
      };

      this.apiService.sendEmail(rePromptRequest, this.token).subscribe(
        res => {
          this.requestsAndResponses.push({ prompt: '', response: res, isEmailBeingSent: true});
          this.disableButton();
          this.isFirstRequest = true;
          this.saveMessages();
          this.loadMessages();
          this.scrollToBottom();
        },
        error => {
          console.error('Error sending email:', error);
          this.prompt = '';
        }
      );
    } else {
      console.error('Provided response is not an email response:', response);
    }
  }


  addToCalendar(response: AnyResponse, i: number) {
    if (isCalendarResponse(response)) {
      const rePromptRequest: RePromptRequest = {
        promptResponse: response,
        userText: "Please add this event to the calendar"
      };

      this.apiService.sendEmail(rePromptRequest, this.token).subscribe(
        res => {
          this.requestsAndResponses.push({ prompt: '', response: res, isRequestTraited: true, isEmailBeingSent: true});
          console.log("*******"+i);
          this.requestsAndResponses[i].isRequestTraited = true;
          this.disableButton();
          this.isFirstRequest = true;
          this.saveMessages();
          this.loadMessages();
          this.scrollToBottom();
        },
        error => {
          console.error('Error adding to calendar:', error);
          this.prompt = '';
        }
      );
    } else {
      console.error('Provided response is not a calendar response:', response);
    }
  }



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

  deleteHistory() {
    localStorage.removeItem('requestsAndResponses');
    this.requestsAndResponses = [];
  }

  disableButton() {
    // let i = this.requestsAndResponses.length - 1;
    if(this.requestsAndResponses.length > 1) {
      for(let i=0; i < this.requestsAndResponses.length; i++) {
        this.requestsAndResponses[i].isRequestTraited = true;
      }
    }
  }

  private initializeRequestsAndResponses() {
    this.requestsAndResponses = [
      {
        prompt: '',
        response: {
          typeAnswer: 'message',
          methodToUse: 'message',
          satisfied: false,
          answerText: 'Hi, how can I help you?'
        },
      },
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
        isEmailBeingSent: false
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
      {
        prompt: 'Send an email to John Doe',
        response: {
          typeAnswer: 'email',
          methodToUse: 'send',
          satisfied: false,
          answerRelatedToGmail: {
            to: 'sasboabdo1205@gmail.com',
            subject: 'Meeting',
            message: 'Hi John, let\'s meet tomorrow at 10am.'
          }
        },
        isEmailBeingSent: true
      }
    ];
  }

  private formatResponse(response: AnyResponse): AnyResponse | null {
    if (isMessageResponse(response)) {
      return {
        typeAnswer: 'message',
        methodToUse: response.methodToUse,
        satisfied: response.satisfied,
        wantToCancel: response.wantToCancel,
        answerText: response.answerText
      };
    } else if (isEmailResponse(response)) {
      return {
        typeAnswer: 'email',
        methodToUse: response.methodToUse,
        satisfied: response.satisfied,
        wantToCancel: response.wantToCancel,
        answerRelatedToGmail: response.answerRelatedToGmail
      };
    } else if (isCalendarResponse(response)) {
      return {
        typeAnswer: 'calendar',
        methodToUse: response.methodToUse,
        satisfied: response.satisfied,
        wantToCancel: response.wantToCancel,
        answerRelatedToCalendar: response.answerRelatedToCalendar
      };
    } else {
      console.error('Unexpected response type:', response);
      return null;
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

  private getLastResponse(): AnyResponse | undefined{
      if (this.requestsAndResponses.length > 0) {
        return this.requestsAndResponses[this.requestsAndResponses.length - 1].response;
      }
      return undefined
  }
}
