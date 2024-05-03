package entiites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    private String from;
    private String to;
    private String subject;
    private String message;
    private List<Attachment> attachments;



}
