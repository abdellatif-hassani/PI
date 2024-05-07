import 'package:hive/hive.dart';

import 'message.dart';


class MessageAdapter extends TypeAdapter<Message> {
  @override
  final typeId = 0;

  @override
  Message read(BinaryReader reader) {
    return Message.fromJson(reader.read());
  }

  @override
  void write(BinaryWriter writer, Message obj) {
    writer.write(obj.toJson());
  }
}