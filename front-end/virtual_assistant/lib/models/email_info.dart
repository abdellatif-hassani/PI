
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
      to: json['to'],
      subject: json['subject'],
      message: json['message'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'to': to,
      'subject': subject,
      'message': message,
    };
  }
  @override
  String toString() {
    return 'here is the email {to: $to, subject: $subject, message: $message}';
  }
}
