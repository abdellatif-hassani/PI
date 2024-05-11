import 'package:virtual_assistant/models/prompt_response_type.dart';
import 'package:virtual_assistant/pages/chat_page.dart';

import 'calendar_info.dart';
import 'email_info.dart';
import 'jsonable.dart';
class PromptResponse extends Jsonable{
  final PromptResponseType typeAnswer;
  final String answerText;
  final EmailInfo? answerRelatedToGmail;
  final CalendarInfo? answerRelatedToCalendar;
  final String? methodToUse;
  final bool? satisfied;
  final bool? wantToCancel;

  PromptResponse({
    required this.typeAnswer,
    required this.answerText,
    required this.answerRelatedToGmail,
    required this.answerRelatedToCalendar,
    required this.methodToUse,
    required this.satisfied,
    required this.wantToCancel,
  });

  factory PromptResponse.fromJson(Map<String, dynamic> json) {
    return PromptResponse(
      typeAnswer:  PromptResponseType.values.byName(json['typeAnswer']),
      answerText: json['answerText'],
      answerRelatedToGmail: json['answerRelatedToGmail'] != null
          ? EmailInfo.fromJson(json['answerRelatedToGmail'])
          : null,
      answerRelatedToCalendar: json['answerRelatedToCalendar'] != null
          ? CalendarInfo.fromJson(json['answerRelatedToCalendar'])
          : null,
      methodToUse: json['methodToUse'],
      satisfied: json['satisfied'],
      wantToCancel: json['wantToCancel'],
    );
  }
@override
  Map<String, dynamic> toJson() {
    return {
      'typeAnswer': PromptResponseType.values.indexOf(typeAnswer),
      'answerText': answerText,
      'answerRelatedToGmail': answerRelatedToGmail?.toJson(),
      'answerRelatedToCalendar':
      answerRelatedToCalendar?.toJson(),
      'methodToUse': methodToUse,
      'satisfied': satisfied,
      'wantToCancel': wantToCancel,
    };
  }
  @override
  String toString() {
    String messageText= answerText+"\n";
    if(answerRelatedToCalendar != null) {
      messageText= "$messageText ${answerRelatedToCalendar!.toString()}";
    } else if(answerRelatedToGmail != null) {
      messageText= "$messageText ${answerRelatedToGmail!.toString()}";
    }
    return messageText;
  }
}