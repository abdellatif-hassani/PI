# Virtual Assistant Project

The Virtual Assistant project aims to develop an interactive application based on a virtual assistant, simplifying users' daily tasks. By integrating advanced technologies such as OpenAI's GPT-3 AI model and Google services like Calendar and Gmail, the application offers a seamless user experience.

## Key Features
- **Prompt Analysis with Spring AI**: Utilizes Spring AI to analyze user prompts and understand their requests.
- **Google Calendar and Tasks Integration**: Implements a microservice for seamless integration with Google Calendar, enabling users to manage events and tasks directly from the app.
- **Gmail Management**: Implements a microservice to integrate with Gmail, allowing users to send and receive emails directly from the application.
- **GPT-3 Interaction for Answering Questions**: Integrates the GPT-3 model to provide users with relevant answers to their questions.

## Technical Architecture
- **Microservices Architecture**: Divides the application into multiple modules for scalability and modularity.
  - **Auth Microservice**: Manages user authentication and authorization.
  - **Calendar / Google Task Microservice**: Integrates with Google Calendar for event and task management.
  - **Gmail Microservice**: Integrates with Gmail for email management.
  - **Chat GPT Native / Spring AI Microservice**: Analyzes user prompts and interacts with the GPT-3 model for responses.

## Technologies Used
- **Spring Boot**: Framework for microservices development.
- **Spring Security**: Handles authentication and authorization.
- **Spring AI**: Utilized for user prompt analysis.
- **Google APIs**: Used for integration with Google Calendar and Gmail.
- **Flutter**: Chosen for mobile UI development, offering a smooth, cross-platform user experience.
