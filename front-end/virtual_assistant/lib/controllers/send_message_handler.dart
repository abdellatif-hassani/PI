import 'dart:async';

import 'package:virtual_assistant/models/data.dart';
import 'package:virtual_assistant/models/message.dart';
import 'package:virtual_assistant/models/method_to_use_type.dart';
import 'package:virtual_assistant/models/prompt_request.dart';
import 'package:virtual_assistant/models/prompt_response.dart';
import 'package:virtual_assistant/models/prompt_response_type.dart';
import 'package:virtual_assistant/services/IchatService.dart';
import 'package:virtual_assistant/services/chat_service.dart';
import 'package:virtual_assistant/services/storage_serivce.dart';
import 'package:virtual_assistant/wrappers/response.dart';

 abstract class SendMessageHandler {
   IChatService? chatService;
   PromptResponse? lastPrompt;
   String? token;
   StorageService? storageService;
   Status? status ;

  void setStorageService(StorageService storageService){
    this.storageService=storageService;
  }
  void setChatService(IChatService chatService){
    this.chatService=chatService;
  }
  void setToken(String token){
    this.token=token;
  }
  void setLastPrompt(PromptResponse? promptResponse){
    this.lastPrompt=promptResponse;
  }
  Future<PromptResponse?> sendMessage(String message);
}

class SendEmailHandler  extends SendMessageHandler {
  @override
  Future<PromptResponse?> sendMessage(String message)async  {
      storageService!.saveMessage(Message(message: message, isSender: true));
      HttpResponse<PromptResponse> promptResponse = await chatService!
          .sendRePromptRequest(
          PromptRequest(promptResponse: lastPrompt!, userText: message),
          token!);

      if(promptResponse.statusCode==200) {
      lastPrompt=promptResponse.data;
        if (promptResponse.data!.satisfied == true) {
          storageService?.saveMessage(
              Message(message: "Email sent successfully",isSender: false));
        } else   if (promptResponse.data!.wantToCancel == true) {
          storageService?.saveMessage(
              Message(message: "Email creation canceled",isSender: false));
        } else {
          storageService!.saveMessage(
              Message(message: lastPrompt?.toString(), isSender: false));
        }
      }else{
        storageService!.saveMessage(
            Message(message: "Email not sent", isSender: false));
      }
      return promptResponse.data;

  }



}

class SendSimpleMessageHandler  extends SendMessageHandler {
  @override
  Future<PromptResponse?> sendMessage(String message)async  {
    storageService!.saveMessage(Message(message: message, isSender: true));
    HttpResponse<PromptResponse> promptResponse = await chatService!
        .sendMessage(
       Message(message: message),
        token!);
    if(promptResponse.statusCode==200) {
      storageService!.saveMessage(
          Message(message: promptResponse.data.toString(), isSender: false));
    }else{
      storageService!.saveMessage(
          Message(message: "Message not sent try again", isSender: false));
    }
    return promptResponse.data;
  }
}
class CreateEventHandler extends SendMessageHandler {
  @override
  Future<PromptResponse?> sendMessage(String message)async  {
    storageService!.saveMessage(Message(message: message, isSender: true));
    HttpResponse<PromptResponse> promptResponse = await chatService!
        .sendRePromptRequest(
        PromptRequest(promptResponse: lastPrompt!, userText: message),
        token!);
    if(promptResponse.statusCode==200) {
      lastPrompt = promptResponse.data;
      if (promptResponse.data!.satisfied == true) {
        storageService?.saveMessage(
            Message(message: "Event created successfully",isSender: false));
      } else   if (promptResponse.data!.wantToCancel == true) {
        storageService?.saveMessage(
            Message(message: "Event creation canceled",isSender: false));
      } else {
        storageService!.saveMessage(
            Message(message: lastPrompt?.toString(), isSender: false));
      }
    }else{
      storageService!.saveMessage(
          Message(message: "Event not sent", isSender: false));
    }
  return promptResponse.data;
  }
}
abstract class IHandlerChooser{
  SendMessageHandler getHandler(PromptResponse? promptResponse);
}
class HandlerChooser implements IHandlerChooser{
  @override

  SendMessageHandler getHandler(PromptResponse? promptResponse)  {
    if(promptResponse==null|| promptResponse.satisfied==true|| promptResponse.wantToCancel==true)
      return SendSimpleMessageHandler();
    switch(promptResponse.typeAnswer){
      case PromptResponseType.email:
        switch(promptResponse.methodToUse){
          case MethodToUseType.send:
            return SendEmailHandler();
          default:
            return SendSimpleMessageHandler();
        }
      case PromptResponseType.calendar:
        switch(promptResponse.methodToUse){
          case MethodToUseType.create:
            return CreateEventHandler();
          default:
            return SendSimpleMessageHandler();
        }
      case PromptResponseType.message:
        return SendSimpleMessageHandler();
      default:
        return SendSimpleMessageHandler();
    }
  }
}