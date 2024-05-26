import 'package:virtual_assistant/models/method_to_use_type.dart';
import 'package:virtual_assistant/models/prompt_response_type.dart';
import 'package:virtual_assistant/pages/chat_page.dart';
import 'calendar_info.dart';
import 'email_info.dart';
import 'jsonable.dart';
class PromptResponse extends Jsonable {
  PromptResponseType? typeAnswer;
  String? answerText;
  EmailInfo? answerRelatedToGmail;
  CalendarInfo? answerRelatedToCalendar;
  List<CalendarInfo>   listEventsCalendar;
  MethodToUseType? methodToUse;
  bool? satisfied;
  bool? wantToCancel;

  PromptResponse({
    this.typeAnswer,
    this.answerText,
    this.answerRelatedToGmail,
    this.answerRelatedToCalendar,
    this.methodToUse,
    this.satisfied,
    this.wantToCancel,
    this.listEventsCalendar = const [],
  });
  factory PromptResponse.fromJson(Map<String, dynamic> json) {
    MethodToUseType? methodToUse;
    try {
      methodToUse = MethodToUseType.values.byName(json['methodToUse']);
    } catch (e) {
      methodToUse=null;
    }
    return PromptResponse(
      typeAnswer: PromptResponseType.values.byName(json['typeAnswer']),
      answerText: json['answerText'],
      answerRelatedToGmail: json['answerRelatedToGmail'] != null
          ? EmailInfo.fromJson(json['answerRelatedToGmail'])
          : null,
      answerRelatedToCalendar: json['answerRelatedToCalendar'] != null
          ? CalendarInfo.fromJson(json['answerRelatedToCalendar'])
          : null,
      methodToUse: methodToUse,
      satisfied: json['satisfied'],
      wantToCancel: json['wantToCancel'],
      listEventsCalendar: json['listEventsCalendar'] != null
          ? List<CalendarInfo>.from(
              json['listEventsCalendar'].map((x) => CalendarInfo.fromJson(x)))
          : [],
    );
  }
  @override
  Map<String, dynamic> toJson() {
    return {
      'typeAnswer': typeAnswer?.name,
      'answerText': answerText,
      'answerRelatedToGmail': answerRelatedToGmail?.toJson(),
      'answerRelatedToCalendar': answerRelatedToCalendar?.toJson(),
      'methodToUse': methodToUse?.name,
      'satisfied': satisfied,
      'wantToCancel': wantToCancel,
    };
  }
  @override
  String toString() {
    String messageText = answerText ?? "";
    if(typeAnswer == PromptResponseType.calendar) {
      if(methodToUse==MethodToUseType.get||methodToUse==MethodToUseType.searchByDate||methodToUse==MethodToUseType.searchByKeyword)
        {
          if(listEventsCalendar.isEmpty) {
            messageText = "You have no events in your calendar";
          } else {
          messageText = "Here are your events: \n";
          for (var event in listEventsCalendar) {
            messageText += event.toString() + "\n---------------\n";
          }
        }
    }else if(methodToUse==MethodToUseType.create){
      messageText  ="$messageText \n ${answerRelatedToCalendar?.toString()}";
    }
    } else if (typeAnswer == PromptResponseType.email) {
      if (methodToUse == MethodToUseType.send) {
        messageText = "$messageText \n ${answerRelatedToGmail?.toString()}";
      }
    }
    return messageText;
  }
}
