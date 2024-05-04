import 'dart:convert';

import 'package:http/http.dart';
import 'package:virtual_assistant/models/message.dart';

import '../wrappers/response.dart';
import 'IchatService.dart';

class HttpChatService extends IChatService {
  HttpChatService({required this.token,required this.url});
  final String url;
  final Client client=Client();
  final String token;
  @override
  Future<HttpResponse<Message>> sendMessage(Message message)async{
    final headers = {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer $token',
    };
    final body = jsonEncode({
      'message': message.message,
    });
    final uri = Uri.parse('https://your-api-url.com/chats/messages');

    final response = await client.post(uri, headers: headers, body: body);
    if (response.statusCode == 200) {
      print(response.body);
      return HttpResponse<Message>(
          statusCode: response.statusCode,
          data: Message.fromJson(jsonDecode(response.body)));
    } else {
      //if request return fail
      return HttpResponse<Message>(
          statusCode: response.statusCode,
          error: jsonDecode(response.body)['message']);
    }
  }

}