package de.segoy.springboottradingdata.model.message;

import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TwsMessage extends BaseMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer messageId;

    @NotBlank
    @Lob
    private String message;

//    public void add(Collection<String> lines) {
//        for (String line : lines) {
//            add(line);
//        }
//    }
//    public void add(String line) {
////        m_textArea.append(line + lineSeparator);
////        moveCursorToEnd();
//    }
//    public void addText(String text) {
////        add(tokenizedIntoList(detabbed(text), LF));
//    }

}
