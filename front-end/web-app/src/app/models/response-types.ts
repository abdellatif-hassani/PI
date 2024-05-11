// src/app/models/response-types.ts

export interface BaseResponse {
    typeAnswer: string;
    methodToUse: string;
    satisfied?: boolean;
    wantToCancel?: boolean;
  }
  
  export interface MessageResponse extends BaseResponse {
    answerText: string;
  }
  
  export interface EmailDetails {
    to: string;
    subject: string;
    message: string;
    attachments?: any;  
  }
  
  export interface EmailResponse extends BaseResponse {
    answerRelatedToGmail: EmailDetails;
  }
  
  export interface CalendarDetails {
    description: string;
    startTime: string;
    endTime: string;
    location: string;
    summary: string;
  }
  
  export interface CalendarResponse extends BaseResponse {
    answerRelatedToCalendar: CalendarDetails;
  }
  
  export type AnyResponse = MessageResponse | EmailResponse | CalendarResponse;
  