import { Component, ElementRef, ViewChild, OnInit, AfterViewInit, QueryList, ViewChildren } from '@angular/core';
import { AuthGoogleService } from '../../services/auth-google.service';
import { ApiService } from '../../services/api.service';
import { CalendarDetails, AnyResponse, MessageResponse, PromptResponse, EmailResponse, CalendarResponse, RePromptRequest } from '../../models/response-types';
import { isMessageResponse, isEmailResponse, isCalendarResponse } from '../../helpers/response-type-guards';

import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit, AfterViewInit {
  requestsAndResponses: { 
    prompt: string, 
    response: AnyResponse, 
    isEmailBeingSent?: boolean, 
    isRequestTraited?: boolean,
    dataSource?: MatTableDataSource<CalendarDetails> 
  }[] = [];
  
  prompt: string = '';
  token: string = '';
  user: any = '';
  isFirstRequest: boolean = true;

  @ViewChild('messagesContainer') messagesContainer!: ElementRef;
  @ViewChildren(MatPaginator) paginators!: QueryList<MatPaginator>;
  @ViewChildren(MatSort) sorts!: QueryList<MatSort>;

  displayedColumns: string[] = ['summary', 'location', 'description', 'startTime', 'endTime'];

  constructor(
    private authService: AuthGoogleService,
    private apiService: ApiService
  ) {
    this.disableButton();
  }

  ngOnInit(): void {
    this.user = this.authService.getProfile();
    console.log('User profile:', this.user); // Debugging statement
    this.loadMessages();
    this.isFirstRequest = true;
  }

  ngAfterViewInit() {
    this.setPaginatorsAndSorts();
    this.paginators.changes.subscribe(() => this.setPaginatorsAndSorts());
    this.sorts.changes.subscribe(() => this.setPaginatorsAndSorts());
  }

  private setPaginatorsAndSorts() {
    if (this.paginators.length === this.requestsAndResponses.length) {
      this.requestsAndResponses.forEach((item, index) => {
        if (item.dataSource) {
          item.dataSource.paginator = this.paginators.toArray()[index];
          item.dataSource.sort = this.sorts.toArray()[index];
        }
      });
    }
  }

  updateTableData(response: CalendarResponse, index: number) {
    if (response.listEventsCalendar) {
      const dataSource = new MatTableDataSource<CalendarDetails>(response.listEventsCalendar);
      this.requestsAndResponses[index].dataSource = dataSource;
      if (this.paginators.toArray().length > index) {
        dataSource.paginator = this.paginators.toArray()[index];
      }
      if (this.sorts.toArray().length > index) {
        dataSource.sort = this.sorts.toArray()[index];
      }
    }
  }

  loadMessages() {
    const data = localStorage.getItem('requestsAndResponses');
    if (data) {
      const parsedData = JSON.parse(data);
      this.requestsAndResponses = parsedData.map((item: any) => ({
        ...item,
        dataSource: isCalendarResponse(item.response) ? new MatTableDataSource<CalendarDetails>(item.response.listEventsCalendar) : undefined
      }));
      this.requestsAndResponses.forEach((item, index) => {
        if (isCalendarResponse(item.response)) {
          this.updateTableData(item.response, index);
        }
      });
    }
  }

  async makeAcall() {
    if (this.isFirstRequest) {
      try {
        await this.sendRequest();
        if (this.getLastResponse() != undefined 
            && !this.getLastResponse()?.satisfied 
            && this.getLastResponse()?.typeAnswer !== 'message'
            && this.isNotList(this.getLastResponse() as AnyResponse)) {
          this.isFirstRequest = false;
        }
      } catch (error) {
        console.error('Error in sendRequest:', error);
      }
    } else {
      try {
        await this.reSendRequest();
        const result = this.getLastResponse();
        console.log('result***********:', result);
        if (result?.satisfied || result?.wantToCancel) {
          this.isFirstRequest = true;
        }
      } catch (error) {
        console.error('Error in reSendRequest:', error);
      }
    }
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
          if(response.methodToUse === 'get' || response.methodToUse === 'searchByKeyword'
            || response.methodToUse === 'searchByDate') {
            this.isFirstRequest = true;
          }
          const formattedResponse = this.formatResponse(response);
          if (formattedResponse) {
            const requestAndResponse = { prompt: this.prompt, response: formattedResponse };
            this.requestsAndResponses.push(requestAndResponse);
            const index = this.requestsAndResponses.length - 1;
            if (isCalendarResponse(formattedResponse)) {
              this.updateTableData(formattedResponse, index);
            }
            this.saveMessages();
            this.prompt = '';
            this.scrollToBottom();
          }
          resolve();
        },
        error => {
          console.error('Error:', error);
          this.prompt = '';
          this.disableButton();
          reject(error);
        }
      );
    });
  }

  reSendRequest(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (!this.prompt.trim()) {
        console.error('Prompt cannot be empty');
        return reject('Prompt cannot be empty');
      }
  
      this.token = this.authService.getToken() as string;
      const lastResponse = this.getLastResponse() as PromptResponse;
      this.apiService.sendRequestToReprompt(lastResponse, this.prompt, this.token).subscribe(
        response => {
          if (response.methodToUse === 'get' || response.methodToUse === 'searchByKeyword' || response.methodToUse === 'searchByDate') {
            this.isFirstRequest = true;
          }
          const formattedResponse = this.formatResponse(response);
          if (formattedResponse) {
            let requestAndResponse;
            if(formattedResponse.satisfied)
               requestAndResponse = { prompt: this.prompt, response: formattedResponse, isEmailBeingSent: true};
            else
                requestAndResponse = { prompt: this.prompt, response: formattedResponse};
            this.requestsAndResponses.push(requestAndResponse);
            const index = this.requestsAndResponses.length - 1;
            if (isCalendarResponse(formattedResponse)) {
              this.updateTableData(formattedResponse, index);
            }
            this.saveMessages();
            this.prompt = '';
            this.scrollToBottom();
            this.disableButton();
          }
          resolve();
        },
        error => {
          console.error('Error:', error);
          this.prompt = '';
          this.disableButton();
          reject(error);
        }
      );
      this.disableButton();
    });
  }

  confirmEmail(response: AnyResponse, i: number) {
    if (isEmailResponse(response)) {
      const rePromptRequest: RePromptRequest = {
        promptResponse: response,
        userText: "Please confirm sending this email"
      };

      this.apiService.sendEmail(rePromptRequest, this.token).subscribe(
        res => {
          res.satisfied = true;
          this.requestsAndResponses.push({ prompt: '', response: res, isEmailBeingSent: true });
          this.disableButton();
          this.isFirstRequest = true;
          this.saveMessages();
          this.loadMessages();
          this.scrollToBottom();
        },
        error => {
          console.error('Error sending email:', error);
          this.prompt = '';
          this.disableButton();
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
          this.requestsAndResponses.push({ prompt: '', response: res, isRequestTraited: true, isEmailBeingSent: true });
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
          this.disableButton();
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
    const serializableData = this.requestsAndResponses.map(item => ({
      prompt: item.prompt,
      response: this.serializeResponse(item.response),
      isEmailBeingSent: item.isEmailBeingSent,
      isRequestTraited: item.isRequestTraited
    }));
    localStorage.setItem('requestsAndResponses', JSON.stringify(serializableData));
  }

  serializeResponse(response: AnyResponse): AnyResponse {
    if (isCalendarResponse(response)) {
      return {
        typeAnswer: response.typeAnswer,
        methodToUse: response.methodToUse,
        satisfied: response.satisfied,
        wantToCancel: response.wantToCancel,
        answerRelatedToCalendar: response.answerRelatedToCalendar,
        listEventsCalendar: response.listEventsCalendar
      } as CalendarResponse;
    } else if (isEmailResponse(response)) {
      return {
        typeAnswer: response.typeAnswer,
        methodToUse: response.methodToUse,
        satisfied: response.satisfied,
        wantToCancel: response.wantToCancel,
        answerRelatedToGmail: response.answerRelatedToGmail
      } as EmailResponse;
    } else if (isMessageResponse(response)) {
      return {
        typeAnswer: response.typeAnswer,
        methodToUse: response.methodToUse,
        satisfied: response.satisfied,
        wantToCancel: response.wantToCancel,
        answerText: response.answerText
      } as MessageResponse;
    } else {
      console.error('Unexpected response type:', response);
      return response;
    }
  }

  deleteHistory() {
    if(localStorage.getItem('requestsAndResponses')){
      localStorage.removeItem('requestsAndResponses');
    }
    this.disableButton();
    this.requestsAndResponses = [];
    this.isFirstRequest = true;
  }

  disableButton() {
    if(this.requestsAndResponses.length > 1) {
      for(let i=0; i < this.requestsAndResponses.length-1; i++) {
        this.requestsAndResponses[i].isRequestTraited = true;
      }
    }
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
        answerRelatedToCalendar: response.answerRelatedToCalendar,
        listEventsCalendar: response.listEventsCalendar
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

  isCalendarResponse(response: AnyResponse): response is CalendarResponse {
    return response.typeAnswer === 'calendar';
  }

  getCalendarResponse(response: AnyResponse): CalendarResponse | null {
    if (this.isCalendarResponse(response)) {
      return response;
    }
    return null;
  }

  private getLastResponse(): AnyResponse | undefined {
    if (this.requestsAndResponses.length > 0) {
      return this.requestsAndResponses[this.requestsAndResponses.length - 1].response;
    }
    return undefined;
  }

  isNotList(response: AnyResponse): boolean {
    return !['searchByKeyword', 'searchByDate', 'get'].includes(response.methodToUse);
  }
}
