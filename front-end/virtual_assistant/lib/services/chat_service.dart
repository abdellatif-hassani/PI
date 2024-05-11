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
    final uri = Uri.parse("https://run.mocky.io/v3/861eeb39-822a-4dcd-bd55-90daecf8d3ac");

    final response = await client.post(uri, headers: headers, body: body);
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
          error: jsonDecode(response.body)['message']+" "+response.statusCode.toString());
    }
  }
  Future<HttpResponse<PromptResponse>> sendRePromptRequest(PromptRequest promptRequest) async {
    final uri = Uri.parse('https://run.mocky.io/v3/084fe7e5-b153-4a7b-aa01-bbe757288c09');
    final body = jsonEncode(promptRequest.toJson());
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
          error: jsonDecode(response.body)['message']+" "+response.statusCode.toString());
    }
  }
  Future sendToExecutePromptRequest(PromptResponse promptResponse) async {
    final uri = Uri.parse('$url/messages');
    final body = jsonEncode(promptResponse.toJson());
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
          error: jsonDecode(response.body)['message']+" "+response.statusCode.toString());
    }
  }


}