
import 'package:file_picker/file_picker.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:virtual_assistant/models/data.dart';
import 'package:virtual_assistant/services/chat_service.dart';
import 'package:virtual_assistant/wrappers/response.dart';

import '../models/message.dart';
import '../models/user.dart';

class ChatController with ChangeNotifier {
  Data<User?> user = Data<User?>(status: Status.NORMAL);
  void setUser(User? user) {
    this.user.data = user;
  //  notifyListeners();
  }

  HttpChatService chatService = HttpChatService(url: "", token: "");
  List<Message> messages = [
  ];
  Future sendMessage(String messageText) async {
    messages.add(Message(message: messageText));
    notifyListeners();
    try {
      HttpResponse<Message> recievedMessage = await chatService.sendMessage(
          messages.last);
      messages.last.isSent=true;
      if(recievedMessage.error!=null && recievedMessage.data!=null)
        messages.add(recievedMessage.data!);
      else
        messages.add(Message(message: "error has accured",isSender: false));
      notifyListeners();
      notifyListeners();

    } catch (e) {
      messages.add(Message(message: "error has accured",isSender: false));
      notifyListeners();
    }

  }
  void showPicker(BuildContext context) async {
    FilePickerResult? result = await FilePicker.platform.pickFiles();

    if(result != null) {
      PlatformFile file = result.files.first;

      print(file.name);
      print(file.bytes);
      print(file.size);
      print(file.extension);
      print(file.path);
    } else {
      // User canceled the picker
    }
  }

}