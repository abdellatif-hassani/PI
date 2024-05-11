import 'dart:convert';

import 'package:http/http.dart';
import 'package:virtual_assistant/models/message.dart';
import 'package:virtual_assistant/models/prompt_request.dart';
import 'package:virtual_assistant/models/prompt_response.dart';

import '../wrappers/response.dart';
import 'IchatService.dart';

class HttpChatService extends IChatService {
  HttpChatService({required this.token,required this.url}) {
    headers = {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer $token',
    };
  }
  final String url;
  final Client client=Client();
  final String token;
  late Map<String, String> headers;



  @override
  Future<HttpResponse<PromptResponse>> sendMessage(Message message)async{

    final body = jsonEncode({
      'message': message.message,
    });
    final uri = Uri.parse('https://your-api-url.com/chats/messages');

    final response = await client.post(uri, headers: headers, body: body);
    if (response.statusCode == 200) {
      print(response.body);
      return HttpResponse<PromptResponse>(
          statusCode: response.statusCode,
          data: PromptResponse.fromJson(jsonDecode(response.body)));
    } else {
      //if request return fail
      return HttpResponse<PromptResponse>(
          statusCode: response.statusCode,
          error: jsonDecode(response.body)['message']);
    }
  }
  Future<HttpResponse<PromptResponse>> sendRePromptRequest(PromptRequest promptRequest) async {
    final uri = Uri.parse('$url/messages');
    final body = jsonEncode({promptRequest.toJson()});
    final response = await client.post(uri, headers: headers, body: body);
    if (response.statusCode == 200) {
      print(response.body);
      return HttpResponse<PromptResponse>(
          statusCode: response.statusCode,
          data: PromptResponse.fromJson(jsonDecode(response.body)));
    } else {
      //if request return fail
      return HttpResponse<PromptResponse>(
          statusCode: response.statusCode,
          error: jsonDecode(response.body)['message']);
    }
  }
  Future sendToExecutePromptRequest(PromptResponse promptResponse) async {
    final uri = Uri.parse('$url/messages');
    final body = jsonEncode({promptResponse.toJson()});
    final response = await client.post(uri, headers: headers, body: body);
    if (response.statusCode == 200) {
      print(response.body);
      return HttpResponse<PromptResponse>(
          statusCode: response.statusCode,
          data: PromptResponse.fromJson(jsonDecode(response.body)));
    } else {
      //if request return fail
      return HttpResponse<PromptResponse>(
          statusCode: response.statusCode,
          error: jsonDecode(response.body)['message']);
    }
  }


}