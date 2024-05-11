import { MessageResponse, EmailResponse, CalendarResponse } from '../models/response-types';

export function isMessageResponse(response: any): response is MessageResponse {
  return response && response.typeAnswer === 'message';
}

export function isEmailResponse(response: any): response is EmailResponse {
  return response && response.typeAnswer === 'email';
}

export function isCalendarResponse(response: any): response is CalendarResponse {
  return response && response.typeAnswer === 'calendar';
}
