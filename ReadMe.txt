Project Documentation: Employee Onboarding System


Overview
This document outlines the architecture, modules, and functional workflows for a microservices-based employee onboarding system developed using Java, Spring Boot, and Domain-Driven Design (DDD). The system is designed for a consulting company to streamline the onboarding process, beginning from resume submission through document verification, onboarding confirmation, and task management.
Technology Stack
Backend: Java 17, Spring Boot 3.x, Spring Cloud, Spring Security
Databases: PostgreSQL, MongoDB
Messaging: RabbitMQ or Kafka
Caching: Redis
Search: Elasticsearch
AI Integration: OpenAI API, LangChain for Retrieval-Augmented Generation (RAG)
DevOps: Docker, Kubernetes, Jenkins, GitHub Actions
Authentication: JWT, OAuth2 (optional Keycloak for Identity and Access Management)
Architecture and Design Approach
Microservices architecture with bounded context per domain
Domain-Driven Design with aggregate roots and value objects
Event-driven communication between services
RESTful APIs for synchronous operations and message queues for asynchronous events
Hexagonal architecture (Ports and Adapters) for decoupling infrastructure from business logic

Functional Workflow

Resume Submission
The candidate uploads a resume through the portal. Supported file types include PDF and DOCX.

RAG Chatbot Evaluation
The resume is sent to a Retrieval-Augmented Generation (RAG) chatbot service that evaluates the content and initiates a set of domain-specific questions. Candidate responses are scored, and the system determines whether the candidate qualifies for the next step.

Screening Outcome
Candidates who pass the chatbot evaluation are marked as eligible and progress to the document verification stage.

Document Submission
The system prompts the candidate to upload official documents, including identity verification, address proof, and other compliance-related forms.

Onboarding Confirmation
Once documents are validated, the system confirms the onboarding. A welcome email is sent to the candidate.

Task Assignment and Tracking
Based on the role and department, initial onboarding tasks are auto-assigned. A task board resembling Jira is available for candidates and managers to update progress, post comments, and track task completion.

Domain Modules and Responsibilities
Candidate Service
Manages candidate registration, resume submission, and interview status
Aggregate Root: Candidate
Sub-entities: Resume, Evaluation Score, Status

Chatbot Service
Integrates with OpenAI or LangChain
Handles embedding and question-answering over resume content
Returns pass/fail outcomes based on scoring rules
Document Service
Validates and stores uploaded onboarding documents
Handles document versioning and expiration
Aggregate Root: DocumentPackage

Notification Service
Sends transactional emails (onboarding confirmation, rejection, etc.)
Uses templates with dynamic placeholders
Consumes onboarding-completed event via message queue
Task Management Service
Manages task creation, assignment, and updates
Provides a UI for drag-and-drop and inline task comments
Aggregate Root: TaskBoard

User Service
Manages login, user roles, and access tokens
Integrates with OAuth2 or external identity provider
Security
Secure APIs protected using OAuth2 or JWT-based authentication
Role-based access control to restrict actions per user type (candidate, HR, manager)
Document uploads encrypted using AES-256 encryption
Activity logging and audit trail maintained for compliance

Deployment Strategy
Microservices packaged as Docker containers
Helm charts used for Kubernetes deployment
Continuous Integration and Continuous Deployment (CI/CD) pipelines using Jenkins or GitHub Actions
Monitoring stack includes Prometheus and Grafana for system health
Centralized logging through ELK (Elasticsearch, Logstash, Kibana)

User Stories
As a candidate, I want to upload my resume so that I can be evaluated.
As the system, I want to evaluate resumes using a chatbot to automate pre-screening.
As a candidate, I want to upload my onboarding documents to complete the process.
As the system, I want to send an onboarding confirmation email once all requirements are met.
As a manager, I want to assign and track onboarding tasks using a visual task board.

Acceptance Criteria
Resume upload must be limited to files under 5MB in PDF or DOCX format
Chatbot evaluation must return a numerical score and a decision flag
Required documents must include proof of identity and residence
Email confirmation must be sent only after all previous steps are complete
Task board must support role-based updates and track status transitions

Next Steps and Deliverables
Available upon request:
Swagger API documentation
Entity-Relationship Diagrams (ERDs)
Kafka topic definitions and event schemas
UI wireframes or task board mockups
