import 'dart:ffi';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:hive/hive.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:path_provider/path_provider.dart';

import '../models/message.dart';
import '../models/message_adapter.dart';

class StorageService {
  late Box<Map> _box;
  static final StorageService _instance = StorageService._();

  // Private constructor
  StorageService._();

  // Singleton instance
  static StorageService get instance {
    return _instance;
  }
  Future<void> init() async {
    final appDocumentDir = await getApplicationDocumentsDirectory();
    await Hive.initFlutter();
      await Hive
      ..init(appDocumentDir.path)
      ..registerAdapter(MessageAdapter());
    _box = await Hive.openBox<Map>('messages');
    int a=0;
  }

  Future<void> writeData(List<Message> messages) async {
    await _box.putAll(messages.map((e) => e.toJson()).toList().asMap());
    return;
  }
  Future<List<Message>> readData() async {
    var data = _box.values;
    return data.map((e) => Message.fromJson(e.cast<String, dynamic>()) ).toList();

  }
  Future<void> saveMessage (Message message) {
    return _box.put(_box.keys.length, message.toJson()) ;
  }
  ValueListenable<Box<dynamic>?>   getListener() {
    return _box.listenable();
  }
   Future deleteLastMessage() {
   return  _box.delete( _box.keys.last);
  }
  updateLastMessage(Message message) async{
    await deleteLastMessage();
   await  saveMessage(message);
  }
  Future clear() async {
    await _box.clear();
  }



}