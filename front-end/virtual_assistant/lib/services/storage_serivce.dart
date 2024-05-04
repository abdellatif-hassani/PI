// import 'package:hive/hive.dart';
// import 'package:hive_flutter/hive_flutter.dart';
//
// class StorageService {
//   late Box _box;
//
//   Future<void> init() async {
//     await Hive.initFlutter();
//     Hive.registerAdapter(MessageAdapter());
//     _box = await Hive.openBox('messages');
//   }
//
//   Future<void> writeData(List<Message> messages) async {
//     await _box.put('messages', messages.map((m) => m.toJson()).toList());
//   }
//
//   Future<List<Message>> readData() async {
//     var data = _box.get('messages', defaultValue: []) as List;
//     return data.map((m) => Message.fromJson(m)).toList();
//   }
// }
// part 'message.g.dart'; // Name of the generated file
//
// @HiveType(typeId: 0)
// class Message {
//   // ... existing code ...
//
//   @HiveField(0)
//   String? id;
//
//   @HiveField(1)
//   String? message;
//
//   @HiveField(2)
//   bool isSender;
//
//   @HiveField(3)
//   bool isSent;
//
//   @HiveField(4)
//   DateTime? timestamp;
//
//   // ... existing code ...
//
//   Map<String, dynamic> toJson() {
//     return {
//       'id': id,
//       'message': message,
//       'isSender': isSender,
//       'isSent': isSent,
//       'timestamp': timestamp?.toIso8601String(),
//     };
//   }
//
//   factory Message.fromJson(Map<String, dynamic> json) {
//     return Message(
//       id: json['id'],
//       message: json['message'],
//       isSender: json['isSender'],
//       isSent: json['isSent'],
//       timestamp: DateTime.parse(json['timestamp']),
//     );
//   }
// }
//
// class MessageAdapter extends TypeAdapter<Message> {
//   @override
//   final typeId = 0;
//
//   @override
//   Message read(BinaryReader reader) {
//     return Message.fromJson(reader.read());
//   }
//
//   @override
//   void write(BinaryWriter writer, Message obj) {
//     writer.write(obj.toJson());
//   }
// }