
class EmailInfo {
  final String to;
  final String subject;
  final String message;

  EmailInfo({
    required this.to,
    required this.subject,
    required this.message,
  });

  factory EmailInfo.fromJson(Map<String, dynamic> json) {
    return EmailInfo(
      to: json['to']?? "no destination",
      subject: json['subject']??"no subject",
      message: json['message']??"no message",
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'to': to,
      'subject': subject,
      'message': message,
    };
  }
  // @override
  // String toString() {
  //   return '${String.fromCharCode(0x1F4EC)}: $to \n ${String.fromCharCode(0x1f3af)}: $subject \n ${String.fromCharCode(0X1f4ac)}: $message';
  // }
  @override
  String toString() {
    return 'to: $to \n Subject $subject \n Body : $message';
  }
}
