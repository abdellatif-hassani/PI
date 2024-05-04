


import 'package:virtual_assistant/models/message.dart';

abstract class IChatService {
  Future sendMessage(Message message);
}