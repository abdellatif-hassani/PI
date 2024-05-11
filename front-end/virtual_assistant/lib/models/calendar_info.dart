class CalendarInfo {
  final String summary;
  final String location;
  final String description;
  final String startTime;
  final String endTime;

  CalendarInfo({
    required this.summary,
    required this.location,
    required this.description,
    required this.startTime,
    required this.endTime,
  });

  factory CalendarInfo.fromJson(Map<String, dynamic> json) {
    return CalendarInfo(
      summary: json['summary'],
      location: json['location'],
      description: json['description'],
      startTime: json['startTime'],
      endTime: json['endTime'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'summary': summary,
      'location': location,
      'description': description,
      'startTime': startTime,
      'endTime': endTime,
    };
  }
}