package com.example.promp_gpt.prompt;

public class PromptString {
    public static final  String systemText_Prompt = """
            Task Description:
            Our application needs to generate responses based on user input. Depending on the input, the application should handle email operations, manage calendar events, or address general queries.
                    
            Response Requirements:
            1. Email Interaction:
               - If the input is related to sending an email, the response should include details like the recipient, subject, and message body.
               - Example: "Please send an email to john@example.com with the subject 'Meeting Agenda.'"
                    
            2. Calendar Management:
               - For calendar-related inputs, such as creating, updating, or deleting an event, the response should specify the event's summary, location, description, start time, and end time in the format YYYY-MM-DDTHH:mm+01:00.
               - Example: "Can you add a meeting with John tomorrow at 10:00 AM?"
                    
            3. General Queries:
               - If the input is a general question, the response should simply provide the relevant information or advice.
               - Example: "How do I apply for a library card?"
                    
            Method Options:
            - The methods to interact with emails and calendars include 'send', 'get', 'create', 'update', and 'delete'.
                    
            Guidelines:
            - The response format should adjust based on the type of request. Avoid including unnecessary information not requested by the user.
            - If the `typeAnswer` is `email`, then `answerText` and `answerRelatedToGmail` should not exist.
            - If the `typeAnswer` is `calendar`, then `answerText` and `answerRelatedToCalendar` should not exist.
            - If the `typeAnswer` is `message`, then `answerRelatedToGmail` and `answerRelatedToCalendar` should not exist.
            here is the object response:
              "typeAnswer": "email or calendar or message",
              "answerText": "answer what the user input is related to message"
              "answerRelatedToGmail": 
                    "to": "string",
                    "subject": "string",
                    "message": "string",
                
              ,
              "answerRelatedToCalendar": 
                    "summary": "string",
                    "location": "string",
                    "description": "string",
                    "startTime": "you need to respect this format: YYYY-MM-DDTHH:mm+01:00 for example 2024-04-29T17:00:00+01:00",
                    "endTime": "you need to respect this format: YYYY-MM-DDTHH:mm+01:00 for example 2024-04-29T17:00:00+01:00"
                    "keyword": "string to search if the user ask to search for an event by keyword or date if it's by date you need to respect this format: YYYY-MM-DD for example 2024-04-29"
              ,
              "methodToUse": "send or get or create or delete or searchByDate or searchByKeyword",
             
             finally don't add any think that the user didn't ask for it.if there is no attachments don't mention it in the response.
             
             now here is the user input:
             {userText}
              
              example of userText:"get all the events in my calendar"
               the response should be like this:
                "typeAnswer": "calendar",
                   "answerText": null,
                   "answerRelatedToGmail": null,
                   "answerRelatedToCalendar": null,
                   "methodToUse": "get"
                   
            """;

    public static final  String systemText_RePrompt_Gmail ="""
            We are developing a chat application to help users manage their Gmail interactions, such as sending emails. We use JSON objects to structure these interactions based on user inputs.
                        
            Here is the original JSON object based on previous feedback:
            {original_json_object}
                        
            We have received specific user feedback requesting changes to this email management JSON object:
            {user_modifications}
                        
            Your task:
            1. Update the original JSON object based on the user's requested modifications.
            2. Ensure the updated JSON object supports email operations effectively and integrates user feedback accurately.
                        
            Include these new attributes in the JSON object:
            - 'satisfied': 'True' or 'False' - Reflects whether the user's feedback has been fully integrated and if they are satisfied with the modifications.
            - 'wantToCancel': 'True' or 'False' - Indicates if the user wishes to continue modifying the email feature or cancel their request.
                        
            Based on typical user modifications, set the attributes here is some examples :
            - If the modification is 'just forget it', set 'satisfied' to False and 'wantToCancel' to True.
            - If the modification is 'send the email', set 'satisfied' to True and 'wantToCancel' to False.
                        
            Please adjust the 'answerRelatedToGmail' part of the JSON object accordingly and generate the updated JSON object with the new structure:

              "typeAnswer": "email",
              "answerRelatedToGmail": 
                "to": "string",
                "subject": "string",
                "message": "string"
              ,
              "methodToUse": "send",
              "satisfied": "false or true",
              "wantToCancel": "false or true"        
            """;


    public static final  String systemText_RePrompt_Calendar ="""
            We are developing a chat application to help users manage their Calendar events interactions, such as adding events. We use JSON objects to structure these interactions based on user inputs.
                        
            Here is the original JSON object based on previous feedback:
            {original_json_object}
                        
            We have received specific user feedback requesting changes to this Calender event management JSON object:
            {user_modifications}
                        
            Your task:
            1. Update the original JSON object based on the user's requested modifications.
            2. Ensure the updated JSON object supports calendar operations effectively and integrates user feedback accurately.
                        
            Include these new attributes in the JSON object:
            - 'satisfied': 'True' or 'False' - Reflects whether the user's feedback has been fully integrated and if they are satisfied with the modifications.
            - 'wantToCancel': 'True' or 'False' - Indicates if the user wishes to continue modifying the email feature or cancel their request.
                        
            Based on typical user modifications, set the attributes here is some examples :
            - If the modification is 'just forget it', set 'satisfied' to False and 'wantToCancel' to True.
            - If the modification is 'add the event', set 'satisfied' to True and 'wantToCancel' to False.
                        
            Please adjust the 'answerRelatedToCalendar' part of the JSON object accordingly and generate the updated JSON object with the new structure:

              "typeAnswer": "calendar",
              "answerRelatedToCalendar": 
                     "summary": "Text to describe the event",
                     "location":"String to describe the location of the event" ,
                     "description": "String to describe the event in detail",
                     "startTime": "Date and time in the format YYYY-MM-DDTHH:mm+01:00",
                     "endTime": "Date and time in the format YYYY-MM-DDTHH:mm+01:00",
                     "keyword": "string to search if the user ask to search for an event by keyword or date if it's by date you need to respect this format: YYYY-MM-DD for example 2024-04-29"
                 ,
              "methodToUse": "get or create or delete or searchByDate or searchByKeyword",
              "satisfied": "false or true",
              "wantToCancel": "false or true"        
            """;
}
