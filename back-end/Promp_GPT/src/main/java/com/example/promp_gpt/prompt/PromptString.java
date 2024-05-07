package com.example.promp_gpt.prompt;

public class PromptString {
    public static final  String systemText_Prompt = """
            we are building an chat application when the user asks to interact with his gmail like sending email for him or adding event to his calender or deleting or updating an event on the his calender or the user just ask a normal question the application should replay to it. our service receives user input and needs to generate responses based on the input.

            Our service should be able to handle three types of responses:
            1. If the user input is related to sending an email, the service should respond with the necessary information to send the email, including the sender, recipient, subject, message, and any attachments.
            2. If the user input is related to creating, updating, or deleting a calendar event, the service should respond with the necessary information for the calendar event, including the summary, location, description, start time, and end time.
            3. If the user input does not require interaction with email or calendar APIs, the service should simply return the text of the response message.

            Your task is to generate a JSON object representing the response based on the user input. The JSON object should have the following structure:

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
              ,
              "methodToUse": "send or get or create or update or delete"
             
             finally don't add any think that the user didn't ask for it.if there is no attachments don't mention it in the response.
             
             now here is the user input:
             {userText}
             
             now I will give you some examples :
             if the userText is "Please send an email to john@example.com with the subject Meeting Agenda."
             the response should be like this:
                   "typeAnswer": "email",
                    "answerText": null,
                    "answerRelatedToGmail": 
                         "to": "john@example.com",
                          "subject": "Meeting Agenda",
                           "message": null,
                            ,
                     "answerRelatedToCalendar": null,
                     "methodToUse": "send"
              if the userText is "Can you add a meeting with John for tomorrow at 10:00 AM?"
              the response should be like this:
                "typeAnswer": "calendar",
                "answerText": null,
                "answerRelatedToGmail": null,
                "answerRelatedToCalendar": 
                  "summary": "Meeting with John",
                  "location": null,
                  "description": null,
                  "startTime": "2024-05-07T10:00:00+01:00",
                  "endTime": "2024-05-07T11:00:00+01:00"
                ,
                "methodToUse": "create"
                  if the userText is "How do I apply for a library card?"
                  the response should be like this:
                        "typeAnswer": "message",
                         "answerText": "To apply for a library card, you will need to visit the library's circulation desk and fill out an application form. You may be required to provide proof of identity and address.",
                if the userText is "I need to update the location of the team meeting to the conference room."
                the response should be like this:
                  "typeAnswer": "calendar",
                  "answerText": null,
                  "answerRelatedToGmail": null,
                  "answerRelatedToCalendar": 
                    "summary": "Team Meeting",
                    "location": "Conference Room",
                    "description": null,
                    "startTime": null,
                    "endTime": null
                   ,
                  "methodToUse": "update" 
                if the userText is "How do I change my Wi-Fi password?"
                  the response should be like this:
                       "typeAnswer": "message",
                        "answerText": "To change your Wi-Fi password, you will need to access your router's settings. Usually, you can do this by typing the router's IP address into a web browser and entering your login credentials. From there, you should be able to find the option to change the password.",
                                                                                                                       
                if the userText is "Cancel my appointment with the dentist on Friday."
                the response should be like this:
                   "typeAnswer": "calendar",
                   "answerText": null,
                   "answerRelatedToGmail": null,
                   "answerRelatedToCalendar": 
                     "summary": "Dentist Appointment",
                     "location": null,
                     "description": null,
                     "startTime": "2024-05-10T14:00:00+01:00",
                     "endTime": "2024-05-10T15:00:00+01:00"
                    ,
                   "methodToUse": "delete"
                  if the userText is "Can you recommend a good Italian restaurant in the area?" 
                  the response should be like this:
                      "typeAnswer": "message",
                       "answerText": "Yes, I recommend trying La Trattoria. They have excellent Italian cuisine."
                               
            """;

    public static final  String systemText_RePrompt ="""
              We received a user response indicating modifications to a previously generated JSON object. The original JSON object was designed to manage email responses based on user input. Here's the original object:
              
              {original_json_object}
              
              The user provided the following feedback and requested changes specifically related to email management:
              
              {user_modifications}
              
              Your task is to update the original JSON object according to the user's modifications, ensuring it is tailored to handle email operations more effectively. Ensure the structure adheres to the defined specifications and integrates the user's feedback accurately.
              
              Additionally, include two new attributes in the JSON object:
              1. "satisfied": "True" or "False" - Indicate whether the user's feedback has been fully integrated. If the user is satisfied with what we suggested to him.
              2. "wantToCancel": "True" or "False" - Indicate whether the user wants to continue modifying or interacting with the email management feature or if they want to cancel their request for any action or change. If the user wants to cancel his request and forget about it in case that he want to do another thing make it true
               or he don't want to send that email make it true
              
              Generate the updated JSON object and ensure it reflects the requested changes and additions.
               
               for example if the user user_modifications is "just forgive it" the satisfied should be false and wantToCancel should be false
                If the user modification is "just forgive it":
                satisfied: false
                wantToCancel: true
                If the user modification is "send the email":
                satisfied: true
                wantToCancel: false
                If the user modification is "i don't want to send that email":
                satisfied: false
                wantToCancel: true
                If the user modification is "i want to send that email":
                satisfied: true
                wantToCancel: false
                If the user modification is "please change the recipient to john@example.com":
                satisfied: false
                wantToCancel: false
                If the user modification is "update the subject to Meeting Agenda":
                satisfied: false
                wantToCancel: false
                If the user modification is "add an attachment with the file presentation.pdf":
                satisfied: false
                wantToCancel: false
                If the user modification is "remove the attachment":
                satisfied: false
                wantToCancel: false
                If the user modification is "change the message to Dear Team,":
                satisfied: false
                wantToCancel: false
                If the user modification is "cancel this email":
                satisfied: false
                wantToCancel: true
                If the user modification is "please proceed with sending the email":
                satisfied: true
                wantToCancel: false
                If the user modification is "discard this draft":
                satisfied: false
                wantToCancel: true
                
                You should respond with JSON object that has the following structure:
                      "typeAnswer": "email",
                      "answerRelatedToGmail": 
                            "to": "string",
                            "subject": "string",
                            "message": "string",
                            "attachments": 
                                  "name": "string",
                                  "url": "string"  
                      ,
                      "methodToUse": "send or get or create or update or delete"
                      "satisfied": "false or true"
                      "wantToCancel":"false or true"
            """;
}
