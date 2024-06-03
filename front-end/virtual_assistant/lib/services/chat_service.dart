import 'dart:convert';

import 'package:http/http.dart';
import 'package:virtual_assistant/controllers/send_message_handler.dart';
import 'package:virtual_assistant/models/message.dart';
import 'package:virtual_assistant/models/prompt_request.dart';
import 'package:virtual_assistant/models/prompt_response.dart';

import '../wrappers/response.dart';
import 'IchatService.dart';

class HttpChatService extends IChatService {
  HttpChatService({required this.token,required this.url}) {
    headers = {
      'Content-Type': 'application/json',

    };
  }
  final String url;
  final Client client=Client();
  final String token;
  late Map<String, String> headers;



  @override
  Future<HttpResponse<PromptResponse>> sendMessage(Message message,String token)async{

    final body = jsonEncode({
      'message': message.message,
    });
    print(body);
    headers.addAll({
      'Authorization': 'Bearer $token'
    });
    print(headers);
    final uri = Uri.parse("$url/prompt");

    final response = await client.post(uri, headers: headers, body: body).timeout(const Duration(seconds: 15));
    if (response.statusCode == 200) {
      print(response.body);
      return HttpResponse<PromptResponse>(
          statusCode: response.statusCode,
          data: PromptResponse.fromJson(jsonDecode(response.body)));
    } else {
      //if request return fail
      print(response.statusCode);
      return HttpResponse<PromptResponse>(
          statusCode: response.statusCode,
          error: jsonDecode(response.body)['message']);
    }
  }
  @override
  Future<HttpResponse<PromptResponse>> sendRePromptRequest(PromptRequest promptRequest,String token) async {
    final uri = Uri.parse("$url/reprompt");
    headers.addAll({
      'Authorization': 'Bearer $token'
    });
    print(headers);

    final body = jsonEncode(promptRequest.toJson());
    print(body);

    final response = await client.post(uri, headers: headers, body: body).timeout(const Duration(seconds: 15));
    if (response.statusCode == 200) {
      print(response.body);
      return HttpResponse<PromptResponse>(
          statusCode: response.statusCode,
          data: PromptResponse.fromJson(jsonDecode(response.body)));
    } else {
      //if request return fail
      return HttpResponse<PromptResponse>(
          statusCode: response.statusCode,
          error: jsonDecode(response.body).toString());
    }

  }



}