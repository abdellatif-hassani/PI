import 'package:chat_bubbles/bubbles/bubble_special_three.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:virtual_assistant/controllers/ControllerFactory.dart';
import 'package:virtual_assistant/controllers/chat_controller.dart';
import 'package:virtual_assistant/models/message.dart';
class MessagesList extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    List<Message> messages=context.watch<ChatController>().messages;
    return Column(
      children: messages.map((message) {
        return BubbleSpecialThree(
          text: message.message!,
          color: message.isSender? Color(0xFF1B97F3):Color(0xFF4B5563),
          tail: true,
          textStyle: TextStyle(
              color: Colors.white,
              fontSize: 16
          ),
          sent: message.isSent,
          isSender: message.isSender,

        );
      }).toList(),
    );
  }
}