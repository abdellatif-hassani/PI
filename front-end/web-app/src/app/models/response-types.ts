// Base interface for all responses
export interface BaseResponse {
  typeAnswer: string;
  methodToUse: string;
  satisfied?: boolean;
  wantToCancel?: boolean;
}

// Specific response type for message interactions
export interface MessageResponse extends BaseResponse {
  answerText: string;
}

// Details of an email, used in EmailResponse
export interface EmailDetails {
  to: string;
  subject: string;
  message: string;
  attachments?: any; // Specify more detail if possible, like Array<Attachment>
}

// Response type for email interactions
export interface EmailResponse extends BaseResponse {
  answerRelatedToGmail: EmailDetails;
}

// Details of a calendar event, used in CalendarResponse
export interface CalendarDetails {
  description: string;
  startTime: string;
  endTime: string;
  location: string;
  summary: string;
}

// Response type for calendar interactions
export interface CalendarResponse extends BaseResponse {
  answerRelatedToCalendar: CalendarDetails;
}

// Union type for any possible response
export type AnyResponse = MessageResponse | EmailResponse | CalendarResponse;

// Represents a response that includes a prompt, possibly enriched with additional text
export interface PromptResponse extends BaseResponse {
  answerText?: string;
  answerRelatedToGmail?: EmailDetails;
  answerRelatedToCalendar?: CalendarDetails;
}

// A request structure that includes a PromptResponse and additional user text
export interface RePromptRequest {
  promptResponse: PromptResponse;
  userText: string;
}
