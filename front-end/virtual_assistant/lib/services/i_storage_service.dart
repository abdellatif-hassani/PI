// import 'dart:io';
//
// import 'package:hive/hive.dart';
// import 'package:hive_flutter/adapters.dart';
// import 'package:path_provider/path_provider.dart';
//
// class IStorageService<T,E> {
//     TypeAdapter adapter;
//     String name;
//     Box<Map>? _box;
//     IStorageService({required this.adapter,required this.name});
//
//     init()async{
//       final appDocumentDir = await getApplicationDocumentsDirectory();
//       await Hive.initFlutter();
//       await Hive
//       ..init(appDocumentDir.path)
//       ..registerAdapter(adapter);
//       _box = await Hive.openBox<Map>('messages');
//     }
//     Future<void> writeData(List<T> data) async {
//       await _box.putAll(data.map((e) => e.toJson()).toList().asMap());
//       return;
//     }
//     Future<List<Message>> readData() async {
//       var data = _box.values;
//       return data.map((e) => Message.fromJson(e.cast<String, dynamic>()) ).toList();
//
//     }
//     Future<void> saveMessage (Message message) {
//       return _box.put(_box.keys.length, message.toJson()) ;
//     }
//     ValueListenable<Box<dynamic>?>   getListener() {
//       return _box.listenable();
//     }
// }