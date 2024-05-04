class Message {
   String? id;
   String? message;
   bool isSender;

   bool isSent;
   DateTime? timestamp;

  Message({
    this.id,
    required this.message,
     this.isSender=true,
    this.isSent=false,
  });

  factory Message.fromJson(Map<String,String?> doc) {
    return Message(
      id: doc['id'],
      message: doc['message'],
      isSender: false,
      isSent: true,

    );
  }
}