



import 'package:virtual_assistant/models/message.dart';

import '../models/prompt_request.dart';
import '../models/prompt_response.dart';
import '../wrappers/response.dart';

abstract class IChatService {
  Future sendMessage(Message message,String token);
  Future<HttpResponse<PromptResponse>> sendRePromptRequest(PromptRequest promptRequest,String token);
}