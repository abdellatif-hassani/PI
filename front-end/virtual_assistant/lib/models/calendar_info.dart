import 'package:intl/intl.dart';

class CalendarInfo {
   String? summary;
   String? location;
   String? description;
   String? startTime;
   String? endTime;

  CalendarInfo({
     this.summary,
     this.location,
     this.description,
     this.startTime,
     this.endTime,
  });

  factory CalendarInfo.fromJson(Map<String, dynamic> json) {
    return CalendarInfo(
      summary: json['summary']?? "no summary",
      location: json['location']?? "no location",
      description: json['description']??"no description",
      startTime: json['startTime'] ?? "no start time",
      endTime: json['endTime']??"no end time",
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

  @override
  String toString() {
    return '${String.fromCharCode(0x1F4C5)}: '
        '${summary??'no summary'}  \n ${String.fromCharCode(0x1f4cd)}:'
        ' ${location??'no location'} \n ${String.fromCharCode(0x1f4dd)}: ${description??'no description'} \n'
        ' ${String.fromCharCode(0x1f55c)}: ${DateFormat("HH:mm d MMM yyyy").format(DateTime.parse(startTime??""))} \n'
        ' ${String.fromCharCode(0x1f55b)}: ${DateFormat("HH:mm d MMM yyyy").format(DateTime.parse(endTime??""))}';
  }
}