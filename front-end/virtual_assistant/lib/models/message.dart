import 'package:hive/hive.dart';

// Name of the generated file
import 'package:hive_flutter/hive_flutter.dart';
import 'package:virtual_assistant/services/collection_storage.dart';

import 'attachement.dart';

@HiveType(typeId: 1)
class Message  extends CollectionStorage{
  // ... existing code ...

  @HiveField(0)
  String? id;

  @HiveField(1)
  String? message;

  @HiveField(2)
  bool isSender=true;

  @HiveField(3)
  bool isSent=false;

  @HiveField(4)
  DateTime? timestamp;

  List<Attachment> attachments = [];

  Message({
    this.id,
    required this.message,
    this.isSender=true,
    this.isSent=false,
    this.timestamp,
    this.attachments = const [],
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'message': message,
      'isSender': isSender,
      'isSent': isSent,
      'timestamp': timestamp?.toIso8601String(),
      'attachments': attachments.map((e) => e.toJson()).toList(),
    };
  }

  factory Message.fromJson(Map<String, dynamic> json) {
    return Message(
      id: json['id'],
      message: json['message'],
      isSender: json['isSender'],
      isSent: json['isSent'],
      timestamp: json['timestamp'] != null
          ? DateTime.parse(json['timestamp'])
          : null,
    );
  }
    static messagesFromList(List<Message> messages) {
      return {
        "messages": messages.map((e) => Message.fromJson(e.toJson())).toList(),
      };
    }

}

