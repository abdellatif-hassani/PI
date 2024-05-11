import 'jsonable.dart';

class SimpleText implements Jsonable {
  String? value;

  setVal(String value) {
    this.value = value;
  }

  SimpleText(Map<String, dynamic> json) {
    this.value = json['value'];
  }
  @override
  Map<String, dynamic> toJson() {
    return {'value': value};
  }

  fromJson(Map<String, dynamic> json) {
    return SimpleText(json['value']);
  }
}
