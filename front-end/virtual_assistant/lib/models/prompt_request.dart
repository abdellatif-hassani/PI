import 'package:virtual_assistant/models/prompt_response.dart';

class PromptRequest {
  final PromptResponse promptResponse;
  final String userText;

  PromptRequest({required this.promptResponse, required this.userText});
  Map<String, dynamic> toJson() {
    return {
      'prompt': promptResponse.toJson(),
      'userText': userText,
    };
  }
}