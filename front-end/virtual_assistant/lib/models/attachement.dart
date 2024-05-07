class Attachment {

  final String name;
  final String data;
  final String contentType;

  Attachment({
    required this.name,
    required this.data,
    required this.contentType,
  });
  factory Attachment.fromJson(Map<String, dynamic> json) {
    return Attachment(
      name: json['name'],
      data: json['data'],
      contentType: json['contentType'],
    );
  }
  Map<String, dynamic> toJson() {
    return {
      'name': name,
      'data': data,
      'contentType': contentType,
    };
  }
}