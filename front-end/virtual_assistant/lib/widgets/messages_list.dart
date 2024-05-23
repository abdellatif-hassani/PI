import 'package:chat_bubbles/bubbles/bubble_special_three.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:hive/hive.dart';
import 'package:hive_flutter/adapters.dart';
import 'package:provider/provider.dart';
import 'package:virtual_assistant/controllers/ControllerFactory.dart';
import 'package:virtual_assistant/controllers/chat_controller.dart';
import 'package:virtual_assistant/models/message.dart';

class MessagesList extends StatelessWidget {
  final ScrollController scrollController = ScrollController();
  int messagesSize=0;
  void scrollToBottom() {
    scrollController.animateTo(
      scrollController.position.maxScrollExtent,
      duration: Duration(milliseconds: 100),
      curve: Curves.easeOut,
    );
  }
  MessagesList({Key? key}) : super(key: key){


  }
  @override
  Widget build(BuildContext context) {

    return ValueListenableBuilder(
      valueListenable: Hive.box<Map>('messages').listenable(),
      builder: (context, Box<Map> box, widget) {

        List<Message> messages = box.values
            .map((e) => Message.fromJson(e.cast<String, dynamic>()))
            .toList();
          if (messages.length > messagesSize) {
            WidgetsBinding.instance.addPostFrameCallback((_) {
              scrollToBottom();
            });

          }
        messagesSize =messages.length;
        return ListView.builder(
          itemCount: messages.length,
          controller: scrollController,
          itemBuilder: (context, index) {

            var message = messages[index];
            return BubbleSpecialThree(
              text: message.message ?? "",
              color: message.isSender ? Color(0xFF1B97F3) : Color(0xFFE8E8EE),
              tail: true,
              textStyle: TextStyle(color:( message.isSender ? Colors.white:Colors.black), fontSize: 16),
              sent: message.isSent,
              isSender: message.isSender,
            );
          },
        );
      },
    );
  }
}
